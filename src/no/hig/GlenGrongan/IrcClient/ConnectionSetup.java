package no.hig.GlenGrongan.IrcClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.tree.DefaultMutableTreeNode;

import jerklib.Channel;
import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.events.AwayEvent;
import jerklib.events.ChannelListEvent;
import jerklib.events.ConnectionCompleteEvent;
import jerklib.events.ConnectionLostEvent;
import jerklib.events.CtcpEvent;
import jerklib.events.IRCEvent;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.events.JoinEvent;
import jerklib.events.KickEvent;
import jerklib.events.MessageEvent;
import jerklib.events.MotdEvent;
import jerklib.events.NickChangeEvent;
import jerklib.events.NoticeEvent;
import jerklib.events.PartEvent;
import jerklib.events.QuitEvent;
import jerklib.events.WhoEvent;
import jerklib.events.WhoisEvent;
import jerklib.events.WhowasEvent;
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
	List<ChatWindow> chatWindow = new ArrayList<ChatWindow>();// List with Channelchat windows, one for each channel part of session
	ConnectionList connectionList;			
	ServerInformation information;
	Profile myProfile;
	
	Preferences pref;
	ResourceBundle res;
	
	public ConnectionSetup(String server, ConnectionManager conMan, Profile profile,ConnectionList list)
	{
		pref = Preferences.userNodeForPackage( getClass() );
		res =  ResourceBundle.getBundle("IrcClient", new Locale(pref.get("IrcClient.language", "en")));
		
		connectionList = list;
		myProfile = profile;
		ConnectionManager conManager = conMan;
		//Creates a new session.
		session = conManager.requestConnection(server);
		//Creates a panel for information specific for this session.
		information = new ServerInformation();
		information.setSession(session);
		information.updateList();
		//Adds the listener to the session
		session.addIRCEventListener(this);;
	}
 
	public void receiveEvent(IRCEvent e)
	{
		String message;
		if(e.getType()==Type.AWAY_EVENT){
			AwayEvent ae = (AwayEvent) e;
			message = "<"+ae.getNick()+">: "+ae.getAwayMessage();
			// Writes message in the server specific textpane in the main window.
			information.getServerText().recieveMessage(message, "Message");
			message = ae.getNick()+" ";
			//gives information of user being set away or coming back, based on his/her status.
			message += (ae.isAway()) ? res.getString("IrcClientConnectionSetup.awayGone") : res.getString("IrcClientConnectionSetup.awayBack");
			information.getServerText().recieveMessage(message, "Away");
			
			
		}
		else if(e.getType()== Type.CHANNEL_MESSAGE)
		{
			MessageEvent me = (MessageEvent) e;
			message = "<" + me.getNick() + ">: " + me.getMessage();
			// writes message in the window belonging to the channel that recieved the message.
			chatWindow.get(findIndex(me.getChannel().getName())).getOutText().recieveMessage(message, "Message");
		}
		else if(e.getType() == Type.CONNECT_COMPLETE)
		{
			ConnectionCompleteEvent cce = (ConnectionCompleteEvent)e;
			connectionList.addServerNode(cce.getActualHostName(), information);
			session.join("#jerklib"); // For testing purposes.
			//session.chanList();
 
		}
		else if(e.getType() == Type.CONNECTION_LOST){
			message = "\n" + res.getString("IrcClientConnectionSetup.lostConnection")+"\n";
			information.serverText.recieveMessage(message, "ConnectionLost");
		}
		else if(e.getType()==Type.CTCP_EVENT)
		{
			CtcpEvent ctcpe = (CtcpEvent) e;
			String action = ctcpe.getCtcpString().replace("ACTION", " ");
			message = "*" + ctcpe.getNick() + action + "*";
			chatWindow.get(findIndex(ctcpe.getChannel().getName())).getOutText().recieveMessage(message, "Message");
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
		else if(e.getType()==Type.ERROR){
			//TODO: Create content
		}
		else if(e.getType()==Type.INVITE_EVENT){
			//TODO: Create content
		}
		// User Related
		else if(e.getType() == Type.JOIN) {
			JoinEvent je = (JoinEvent) e;
			User user = new User(je.getNick(), je.getChannel());
			
			((ChannelChat)chatWindow.get((session.getChannels().size())-1)).getUserList().addUser(user);
		}
		else if(e.getType() == Type.JOIN_COMPLETE)
		{
			JoinCompleteEvent jce = (JoinCompleteEvent)e;
			chatWindow.add(new ChannelChat(jce.getChannel(), myProfile.getActualNick()));
			ChannelChat ccObject = (ChannelChat) chatWindow.get((session.getChannels().size())-1); // Newest chat window
			connectionList.addChannelNode(session.getConnectedHostName(), jce.getChannel().getName(),ccObject);
			message = res.getString("IrcClientConnectionSetup.youJoinedMessage")+" "+jce.getChannel().getName();
			information.getServerText().recieveMessage(message, "JoinComplete");
			if(jce.getChannel().getTopic() != ""){
				message = message + res.getString("IrcClientConnectionSetup.topicMessage") + jce.getChannel().getTopic();
			}
			ccObject.getOutText().recieveMessage(message, "Topic"); // Writes topic
			
			List<User> nicks = new ArrayList<User>();
			for(String  nick : ccObject.getChannel().getNicks()) {
				nicks.add(new User(nick, ccObject.getChannel()));
			}
			
			ccObject.setUserList(new ChannelUsers(nicks));
			ccObject.systemOutput();
		}
		else if(e.getType()==Type.KICK_EVENT){
			KickEvent ke = (KickEvent)e;
			ChatWindow ccWindow = chatWindow.get(findIndex(ke.getChannel().getName()));
			if(isMe(ke.getUserName())){
				connectionList.removeChannelNode(session.getConnectedHostName(), ke.getChannel().getName(), ccWindow);
				chatWindow.remove(ccWindow);
				ccWindow.dispose();
				message = ke.getChannel().getName()+": "+res.getString("IrcClientConnectionSetup.userKicked")+" "+ke.byWho()+
						". "+res.getString("IrcClientConnectionSetup.kickEventReason")+" "+ke.getMessage();
			}
			else{
			message = ke.getWho()+" "+res.getString("IrcClientConnectionSetup.otherKicked")+" "+ke.byWho()+
					". "+res.getString("IrcClientConnectionSetup.kickEventReason")+" "+ke.getMessage();
			}
		}
		else if(e.getType()==Type.MODE_EVENT){
			//TODO: Create content
		}
		
		else if(e.getType() == Type.MOTD)
		{
			MotdEvent me = (MotdEvent) e;
			message = me.getMotdLine();
			if(message.startsWith("-")){message = message;}
			information.getServerText().recieveMessage(message, "Motd");
		}
		else if(e.getType() == Type.NICK_CHANGE) {
			NickChangeEvent nce = (NickChangeEvent) e;
			String oldnick = nce.getOldNick();
			String newnick = nce.getNewNick();
			for(ChatWindow instance : chatWindow){
				if(instance instanceof ChannelChat){
					if(((ChannelChat) instance).getChannel().getNicks().contains(oldnick)){
						((ChannelChat) instance).getUserList().renameUser(oldnick, newnick);
					}
				}
			}
		}
		else if(e.getType() == Type.NOTICE){
			NoticeEvent ne = (NoticeEvent) e;
			message = ne.getNoticeMessage();
			if(ne.getChannel() != null) chatWindow.get(findIndex(ne.getChannel().getName())).getOutText().recieveMessage(message, "Notice");
			else information.getServerText().recieveMessage(message, "Notice");
		}
		else if(e.getType()==Type.PART){
			PartEvent pe = (PartEvent)e;
			ChatWindow ccWindow = chatWindow.get(findIndex(pe.getChannel().getName()));
			if(isMe(pe.getUserName())){
				connectionList.removeChannelNode(session.getConnectedHostName(), pe.getChannelName(), ccWindow);
				chatWindow.remove(ccWindow);
				ccWindow.dispose();
			}
			else{
				((ChannelChat)ccWindow).getUserList().removeUser(pe.getWho());		// Updates user list
				message = "<"+pe.getWho()+">: "+pe.getPartMessage();
				ccWindow.getOutText().recieveMessage(message, "message");
				message = pe.getWho()+" "+res.getString("IrcClientConnectionSetup.partinformation");
				ccWindow.getOutText().recieveMessage(message, "Part");
			}
		}
		else if(e.getType()==Type.PRIVATE_MESSAGE){
			MessageEvent me = (MessageEvent) e;
			UserChat ccWindow;
			int index = (findIndex(me.getNick()));
			if(index!=-1){
				ccWindow = (UserChat) chatWindow.get(index);
				
			}
			else{
				// If event were sent by me.
				if(isMe(me.getUserName())){
					ccWindow = new UserChat(session.getConnectedHostName(), me.getMessage(), session);
					chatWindow.add(ccWindow);
				}
				//If Chatwindow doesn't exist.
				else{
					ccWindow = new UserChat(session.getConnectedHostName(), me.getNick(), session);
					chatWindow.add(ccWindow);
					message = res.getString("IrcClientConnectionSetup.privateChatStart")+" "+me.getNick();
					ccWindow.getOutText().recieveMessage(message, "Message");
				}
			}
			
			message = (!isMe(me.getUserName())) ? me.getNick() + " said: " + me.getMessage() : res.getString("IrcClientConnectionSetup.privateChatStart")+" "+me.getNick();
			// writes message in the window belonging to the channel that recieved the message.
			ccWindow.getOutText().recieveMessage(message, "Message");
		}
		else if(e.getType() == Type.QUIT) {
			QuitEvent qe = (QuitEvent) e;
			for(Channel channelInstance : qe.getChannelList()){
				((ChannelChat)chatWindow.get(findIndex(channelInstance.getName()))).getUserList().removeUser(qe.getNick());
			}
		}
		else if(e.getType()==Type.SERVER_INFORMATION){
			//TODO: Create content
		}
		else if(e.getType()==Type.SERVER_VERSION_EVENT){
			//TODO: Create content
		}
		else if(e.getType()==Type.TOPIC){
			//TODO: Create content
		}
		
		// Creates a message string with information about a /who event and sends it to the ServerInformation pane
		else if(e.getType()== Type.WHO_EVENT)
		{
			WhoEvent we = (WhoEvent)e;
			message = "\n";
			
			message += res.getString("IrcClientConnectionSetup.whoEvent.nick") +we.getNick()+ "\n";
			message += res.getString("IrcClientConnectionSetup.whoEvent.hostName") +we.getHostName()+ "\n";
			message += res.getString("IrcClientConnectionSetup.whoEvent.userName") +we.getUserName()+ "\n";
			message += res.getString("IrcClientConnectionSetup.whoEvent.realName") +we.getRealName()+ "\n";
			message += we.getNick()+ " " +res.getString("IrcClientConnectionSetup.whoEvent.location1") +we.getServerName()+ " , " +we.getHopCount()+ " " +res.getString("IrcClientConnectionSetup.whoEvent.location2");
			message += (we.isAway()) ? res.getString("IrcClientConnectionSetup.whoEvent.here") : res.getString("IrcClientConnectionSetup.whoEvent.away");
			
			information.getServerText().recieveMessage(message, "who");
		}
		
		// Creates a message string with information about a /whois event and sends it to the ServerInformation pane
		else if(e.getType()==Type.WHOIS_EVENT){
			WhoisEvent wie = (WhoisEvent)e;
			message = "\n";
			
			message += res.getString("IrcClientConnectionSetup.whoEvent.nick") +wie.getUser()+ "\n";
			message += res.getString("IrcClientConnectionSetup.whoEvent.userName") +wie.getHost()+ "\n";
			message += res.getString("IrcClientConnectionSetup.whoEvent.connections");
			for(String channel : wie.getChannelNames()) {
				message += channel+ " ";
			}
			message += "\n";
			message += (wie.isAnOperator()) ? res.getString("IrcClientConnectionSetup.whoEvent.isOp") : res.getString("IrcClientConnectionSetup.whoEvent.isNotOp");
			message += (wie.isIdle()) ? res.getString("IrcClientConnectionSetup.whoEvent.here") : res.getString("IrcClientConnectionSetup.whoEvent.away");
			
			information.getServerText().recieveMessage(message, "whois");
		}
		
		// Creates a message string with information about a /whowas event and sends it to the ServerInformation pane
		else if(e.getType()==Type.WHOWAS_EVENT){
			WhowasEvent wwe = (WhowasEvent)e;
			message = "\n";
			
			message += res.getString("IrcClientConnectionSetup.whoEvent.nick") +wwe.getNick()+ "\n";
			message += res.getString("IrcClientConnectionSetup.whoEvent.hostName") +wwe.getHostName()+ "\n";
			message += res.getString("IrcClientConnectionSetup.whoEvent.userName") +wwe.getUserName()+ "\n";
			message += res.getString("IrcClientConnectionSetup.whoEvent.realName") +wwe.getRealName()+ "\n";
			
			information.getServerText().recieveMessage(message, "who");
		}
		System.out.println(e.getType() + " : " + e.getRawEventData());
	}
	/**
	 * Finds the index of the chatWindow for a named channel.
	 * @param channelName
	 * @return The index of the chatWindow, 0 if it doesn't exist.
	 */
	private int findIndex(String name){
		for(int i = 0; i < chatWindow.size(); i++){
			if(chatWindow.get(i) instanceof ChannelChat){
				if(((ChannelChat)chatWindow.get(i)).getChannel().getName().equals(name)) return i;
			}
			else if(((UserChat)chatWindow.get(i)).getUserName().equals(name)) return i;
		}
		return -1;
		
	}
	
	private boolean isMe(String name){
		System.out.println(name+ " " +myProfile.getName());
		if(name.matches("~"+myProfile.getName())||name.equals(myProfile.getName())){
			return true;
		}
		return false;
	}
	
}
