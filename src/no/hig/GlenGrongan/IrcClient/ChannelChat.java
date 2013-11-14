package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jerklib.Channel;
import jerklib.Session;

public class ChannelChat extends ChatWindow{
	Session session;
	Channel channel;
	public ChannelChat(String s1, String s2) {
		super(s1, s2);
		// TODO Auto-generated constructor stub
	}
	public ChannelChat(Session s, Channel c){
		super(s.getConnectedHostName(), c.getName());
		sendButton.addActionListener(new sendEvent());
		inText.addActionListener(new sendEvent());
		outTextPanel.add(new ChannelUsers(), BorderLayout.EAST);
		
		session = s;
		channel = c;
		setVisible(true);
		setSize(500,300);
		
	}
	
	public void setSession(Session s){
		this.session = s;
	}
	
	public void setChannel(Channel c){
		this.channel = c;
	}
	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}
	
	class sendEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = inText.getText();
			if(message.startsWith("/")){
				String[] command = message.split(" ");
				switch(command[0]){
				case "/join" : session.join(command[1]); break;
				case "/part": channel.part(command[1]); break;
				default : outText.recieveMessage("Unknown command"); break;
				}
			}
			else{
				channel.say(inText.getText());
				System.out.println(inText.getText());
				System.out.println(channel.getName());
			}
			inText.setText("");
		
		}
	}

}
