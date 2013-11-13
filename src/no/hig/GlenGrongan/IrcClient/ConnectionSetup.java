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
import jerklib.events.ConnectionCompleteEvent;
import jerklib.events.IRCEvent;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.events.MessageEvent;
import jerklib.events.MotdEvent;
import jerklib.events.NoticeEvent;
import jerklib.listeners.IRCEventListener;

/**
 * Class containing a session, and controlls the connection to one server.
 * @version 0.2
 * @author Glen
 *
 */

public class ConnectionSetup implements IRCEventListener{
	Session session;
	List<ChatWindow> chatWindow = new ArrayList<ChatWindow>();
	ConnectionList connectionList;
	String motd;
	public ConnectionSetup(String server, ConnectionManager conMan,ConnectionList list, ChatWindow chat)
	{
		//chatWindow = chat;
		connectionList = list;
		ConnectionManager conManager = conMan;
		session = conManager.requestConnection(server);
		session.addIRCEventListener(this);
	}
 
	public void receiveEvent(IRCEvent e)
	{
		String message;
		if(e.getType() == Type.CONNECT_COMPLETE)
		{
			ConnectionCompleteEvent cce = (ConnectionCompleteEvent)e;
			connectionList.addServerNode(cce.getActualHostName());
			e.getSession().join("#jerklib");
 
		}
		else if(e.getType() == Type.JOIN_COMPLETE)
		{
			JoinCompleteEvent jce = (JoinCompleteEvent)e;
			chatWindow.add(new ChatWindow(session, jce.getChannel()));
			connectionList.addChannelNode(session.getConnectedHostName(), jce.getChannel().getName());
			message = "\nYou've joined " + jce.getChannel().getName();
			if(jce.getChannel().getTopic() != ""){
				message = message + "\nThe topic is " + jce.getChannel().getTopic();
			}
			chatWindow.get((session.getChannels().size())-1).recieveMessage(motd);
			chatWindow.get((session.getChannels().size())-1).recieveMessage(message);
		}
		else if(e.getType() == Type.MOTD)
		{
			MotdEvent me = (MotdEvent) e;
			message = me.getMotdLine();
			if(message.startsWith("-")){message = message + "\n";}
			motd = motd + message;
		}
		/*else if(e.getType() == Type.NOTICE){
			NoticeEvent ne = (NoticeEvent) e;
			message = ne.getNoticeMessage();
			chatWindow.get(findIndex(ne.getChannel().getName())).recieveMessage(message);
		}*/
		else if(e.getType()== Type.CHANNEL_MESSAGE)
		{
			MessageEvent me = (MessageEvent) e;
			message = "\n" + me.getNick() + ":" + me.getMessage();
			chatWindow.get(findIndex(me.getChannel().getName())).recieveMessage(message);
		}
		else
		{
			System.out.println(e.getType() + " : " + e.getRawEventData());
		}
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
