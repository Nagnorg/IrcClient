package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jerklib.Channel;
import jerklib.Session;

public class ChannelChat extends ChatWindow{
	Channel channel;
	ChannelUsers userList;
	public ChannelChat(String s1, String s2) {
		super(s1, s2);
		// Should never run.
	}
	public ChannelChat(Channel c){
		super(c.getSession().getConnectedHostName(), c.getName());
		sendButton.addActionListener(new sendEvent());
		inText.addActionListener(new sendEvent());
		outTextPanel.add(userList = new ChannelUsers(), BorderLayout.EAST);
		
		channel = c;
		setVisible(true);
		setSize(500,300);
		
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
			if(message.length() <= 512 && message.length() > 3){
				if(message.startsWith("/")){
					String[] command = message.split(" ", 2);
					switch(command[0].toLowerCase()){
						case "/join" : channel.getSession().join(command[1]); break;
						case "/part": channel.part(command[1]); break;
						case "/away": if(channel.getSession().isAway()) channel.getSession().setAway(command[1]); else channel.getSession().unsetAway(); break;
						case "/me": 
						case "/action": channel.action(command[1]); break;
						case "/changenick": channel.getSession().changeNick(command[1]); break;
						default : outText.recieveMessage("Unknown command", "Information"); break;
					}
				}
				else{
					channel.say(message);
					/*System.out.println(message);
					System.out.println(channel.getName());*/
				}
				inText.setText("");
			}
		}
	}

	public ChannelUsers getUserList() {
		return userList;
	}
	
	public void setUserList(ChannelUsers userList) {
		//outTextPanel.remove(userList);
		this.userList = userList;
		outTextPanel.add(userList, BorderLayout.EAST);
		outTextPanel.updateUI();
	}
	
	// Nicklist commands
	
	public void ban(String nick, String type) {
		// TODO: Add content
	}
	
	public void ctcp() {
		// TODO: Add content, check parameters
	}
	
	public void invite(String nick, Channel channel) {
		// TODO: Add content, check resolution
	}
	
	public void kick(String nick) {
		// TODO: Add content
	}
	
	public void query(String nick, String message) {
		// TODO: Add content, parameter evaluation
	}
	
	public void who(String nick) {
		// TODO: Add content
	}
	
	
	
	public void systemOutput() {
		for(User user : userList.getUsers()) {
			System.out.println("--------");
			System.out.println("Nick: " +user.getName());
			if(user.getPrivilege()) System.out.println("Ops : Yes");
			else System.out.println("Ops : No");
			System.out.println("--------");
		}
	}

}
