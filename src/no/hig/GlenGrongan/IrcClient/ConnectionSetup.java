package no.hig.GlenGrongan.IrcClient;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.events.IRCEvent;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.listeners.IRCEventListener;


public class ConnectionSetup implements IRCEventListener{
	JTextPane inText;
	public ConnectionSetup(String server, String nickName, JTextPane textPane)
	{
		inText = textPane;
		ConnectionManager conman = new ConnectionManager(new Profile(nickName));
		conman.requestConnection(server).addIRCEventListener(this);
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
			SimpleAttributeSet sas = new SimpleAttributeSet ();
			int pos = inText.getStyledDocument().getEndPosition().getOffset();
		    try {
		    	// add the text to the document
		    	inText.getStyledDocument().insertString(pos, e.getType() + " : " + e.getRawEventData()+"\n", sas);
		    } catch (BadLocationException ble) {
		    	ble.printStackTrace();
		    }
			
		}
	}
}
