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
import jerklib.events.IRCEvent;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.events.MessageEvent;
import jerklib.events.MotdEvent;
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
	List<ChatWindow> chatWindow = new ArrayList<ChatWindow>();// List with chatwindows, one for each channel part of session
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
			session.chanList();
 
		}
		else if(e.getType() == Type.CHANNEL_LIST_EVENT){
			ChannelListEvent cle = (ChannelListEvent)e;
			if(cle.getNumberOfUser()>5) information.getChannelListModel().addElement(cle.getChannelName());
		}
		else if(e.getType() == Type.JOIN_COMPLETE)
		{
			JoinCompleteEvent jce = (JoinCompleteEvent)e;
			chatWindow.add(new ChatWindow(session, jce.getChannel()));
			connectionList.addChannelNode(session.getConnectedHostName(), jce.getChannel().getName(),chatWindow.get((session.getChannels().size())-1));
			message = "\nYou've joined " + jce.getChannel().getName();
			information.getServerText().recieveMessage(message);
			if(jce.getChannel().getTopic() != ""){
				message = message + "\nThe topic is " + jce.getChannel().getTopic();
			}
			chatWindow.get((session.getChannels().size())-1).getInText().recieveMessage(message);
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
			message = ne.getNoticeMessage();
			if(ne.getChannel() != null) chatWindow.get(findIndex(ne.getChannel().getName())).getInText().recieveMessage(message);
			else information.getServerText().recieveMessage(message);
		}
		else if(e.getType()== Type.CHANNEL_MESSAGE)
		{
			MessageEvent me = (MessageEvent) e;
			message = "\n" + me.getNick() + ": " + me.getMessage();
			chatWindow.get(findIndex(me.getChannel().getName())).getInText().recieveMessage(message);
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
