package no.hig.GlenGrongan.IrcClient;

import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.events.IRCEvent;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.listeners.IRCEventListener;


public class ConnectionSetup implements IRCEventListener{
	public ConnectionSetup()
	{
		ConnectionManager conman = new ConnectionManager(new Profile("jerkbot"));
		conman.requestConnection("chat.freenode.net").addIRCEventListener(this);
	}
 
	public void receiveEvent(IRCEvent e)
	{
		if(e.getType() == Type.CONNECT_COMPLETE)
		{
			e.getSession().join("#jerklib");
 
		}
		else if(e.getType() == Type.JOIN_COMPLETE)
		{
			JoinCompleteEvent jce = (JoinCompleteEvent)e;
			jce.getChannel().say("Hello World!!");
		}
		else
		{
			System.out.println(e.getType() + " : " + e.getRawEventData());
		}
	}
	
	public static void main(String[] args){
		ConnectionSetup cs = new ConnectionSetup();
	}
}
