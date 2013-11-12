package no.hig.GlenGrongan.IrcClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.events.IRCEvent;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.events.MessageEvent;
import jerklib.events.MotdEvent;
import jerklib.events.NoticeEvent;
import jerklib.listeners.IRCEventListener;


public class ConnectionSetup implements IRCEventListener{
	ChatWindow chatWindow;
	public ConnectionSetup(String server, String nickName, ChatWindow chat)
	{
		chatWindow = chat;
		ConnectionManager conman = new ConnectionManager(new Profile(nickName));
		conman.requestConnection(server).addIRCEventListener(this);
	}
 
	public void receiveEvent(IRCEvent e)
	{
		String message;
		if(e.getType() == Type.CONNECT_COMPLETE)
		{
			e.getSession().join("#chat");
 
		}
		else if(e.getType() == Type.JOIN_COMPLETE)
		{
			JoinCompleteEvent jce = (JoinCompleteEvent)e;
			jce.getChannel().say("Hei");
		}
		else if(e.getType() == Type.MOTD)
		{
			MotdEvent me = (MotdEvent) e;
			message = me.getMotdLine();
			if(message.startsWith("-")){message = message + "\n";}
			chatWindow.recieveMessage(message);
		}
		else if(e.getType() == Type.NOTICE){
			NoticeEvent ne = (NoticeEvent) e;
			message = ne.getNoticeMessage();
			chatWindow.recieveMessage(message);
		}
		else if(e.getType()== Type.CHANNEL_MESSAGE)
		{
			MessageEvent me = (MessageEvent) e;
			message = "\n" + me.getNick() + ":" + me.getMessage();
			chatWindow.recieveMessage(message);
		}
		else
		{
			System.out.println(e.getType() + " : " + e.getRawEventData());
		}
	}
	
}
