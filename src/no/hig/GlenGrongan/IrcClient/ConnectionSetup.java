package no.hig.GlenGrongan.IrcClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.tree.DefaultMutableTreeNode;

import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.events.ChannelListEvent;
import jerklib.events.ConnectionCompleteEvent;
import jerklib.events.ConnectionLostEvent;
import jerklib.events.CtcpEvent;
import jerklib.events.IRCEvent;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.events.MessageEvent;
import jerklib.events.MotdEvent;
import jerklib.events.NickChangeEvent;
import jerklib.events.NoticeEvent;
import jerklib.listeners.IRCEventListener;

/**
 * Class containing a session, and controls the connection to one server.
 * @version 0.2
 * @author Glen
 * 
 *
 */

public class ConnectionSetup implements IRCEventListener{
	Session session;// Easy access session for this listener.
	List<ChannelChat> chatWindow = new ArrayList<ChannelChat>();// List with Channelchat windows, one for each channel part of session
	ConnectionList connectionList;			
	ServerInformation information;
	public ConnectionSetup(String server, ConnectionManager conMan,ConnectionList list)
	{
		connectionList = list;
		ConnectionManager conManager = conMan;
		//Creates a new session.
		session = conManager.requestConnection(server);
		//Creates a panel for information specific for this session.
		information = new ServerInformation();
		information.setSession(session);
		information.updateList();
		//Adds the listener to the session
		session.addIRCEventListener(this);
	}
 
	public void receiveEvent(IRCEvent e)
	{
		String message;
		if(e.getType() == Type.CONNECT_COMPLETE)
		{
			ConnectionCompleteEvent cce = (ConnectionCompleteEvent)e;
			connectionList.addServerNode(cce.getActualHostName(), information);
			session.join("#jerklib");
			//session.chanList();
 
		}
		else if(e.getType() == Type.CONNECTION_LOST){
			information.serverText.recieveMessage("\n g&mIRC has lost connection to the server.");
		}
		else if(e.getType() == Type.CHANNEL_LIST_EVENT){
			ChannelListEvent cle = (ChannelListEvent)e;
			System.out.println(information.getChannelSearch().getText());
			if(information.getChannelSearch().getText()== ""){
				if(cle.getNumberOfUser() > 500){
					System.out.println(cle.getNumberOfUser());
					information.getChannelListModel().addElement(cle.getChannelName());
				}
			}
			else if(information.getChannelSearch().getText() != ""){
				if(cle.getChannelName().contains(information.getChannelSearch().getText())){
					information.getChannelListModel().addElement(cle.getChannelName());
				}
			}
		}
		else if(e.getType() == Type.JOIN_COMPLETE)
		{
			JoinCompleteEvent jce = (JoinCompleteEvent)e;
			chatWindow.add(new ChannelChat(jce.getChannel()));
			ChannelChat ccObject = chatWindow.get((session.getChannels().size())-1); // Newest chat window
			connectionList.addChannelNode(session.getConnectedHostName(), jce.getChannel().getName(),ccObject);
			message = "\nYou've joined " + jce.getChannel().getName();
			information.getServerText().recieveMessage(message);
			if(jce.getChannel().getTopic() != ""){
				message = message + "\nThe topic is " + jce.getChannel().getTopic();
			}
			ccObject.getOutText().recieveMessage(message); // Writes topic
			
			List<User> nicks = new ArrayList<User>();
			for(String  nick : ccObject.getChannel().getNicks()) {
				nicks.add(new User(nick, ccObject.getChannel()));
			}
			
			ccObject.setUserList(new ChannelUsers(nicks));
			ccObject.systemOutput();
		}
		else if(e.getType() == Type.MOTD)
		{
			MotdEvent me = (MotdEvent) e;
			message = me.getMotdLine();
			if(message.startsWith("-")){message = message + "\n";}
			information.getServerText().recieveMessage(message);
		}
		else if(e.getType() == Type.NOTICE){
			NoticeEvent ne = (NoticeEvent) e;
			message = "\n" + ne.getNoticeMessage();
			if(ne.getChannel() != null) chatWindow.get(findIndex(ne.getChannel().getName())).getOutText().recieveMessage(message);
			else information.getServerText().recieveMessage(message);
		}
		else if(e.getType()== Type.CHANNEL_MESSAGE)
		{
			MessageEvent me = (MessageEvent) e;
			message = "\n<" + me.getNick() + ">: " + me.getMessage();
			chatWindow.get(findIndex(me.getChannel().getName())).getOutText().recieveMessage(message);
		}
		else if(e.getType()==Type.CTCP_EVENT)
		{
			CtcpEvent ctcpe = (CtcpEvent) e;
			String action = ctcpe.getCtcpString().replace("ACTION", " ");
			message = "\n*" + ctcpe.getNick() + action + "*";
			chatWindow.get(findIndex(ctcpe.getChannel().getName())).getOutText().recieveMessage(message);
		}
		
		// User Related
		else if(e.getType() == Type.JOIN) {
			System.out.println("User joined: " +e.getRawEventData());
		}
		
		else if(e.getType() == Type.QUIT) {
			System.out.println("User quit: " +e.getRawEventData());
		}
		
		else if(e.getType() == Type.NICK_CHANGE) {
			NickChangeEvent nce = (NickChangeEvent) e;
			String oldnick = nce.getOldNick();
			String newnick = nce.getNewNick();
			
			chatWindow.get((session.getChannels().size())-1).getUserList().renameUser(oldnick, newnick);
			
			System.out.println("User changed name: " +e.getRawEventData());
		}
		
		System.out.println(e.getType() + " : " + e.getRawEventData());
	}
	/**
	 * Finds the index of the chatWindow for a named channel.
	 * @param channelName
	 * @return The index of the chatWindow, 0 if it doesn't exist.
	 */
	private int findIndex(String channelName){
		for(int i = 0; i < chatWindow.size(); i++){
			if(chatWindow.get(i).getChannel().getName() == channelName) return i;
		}
		return 0;
		
	}
	
}
