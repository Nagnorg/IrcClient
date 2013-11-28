package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jerklib.Channel;
import jerklib.Session;

public class ChannelChat extends ChatWindow{
	Channel channel;
	ChannelUsers userList;
	JPopupMenu userInteraction;
	String myNick;
	public ChannelChat(String s1, String s2) {
		super(s1, s2);
		// Should never run.
	}
	public ChannelChat(Channel c, String nick){
		super(c.getSession().getConnectedHostName(), c.getName());
		
		channel = c;
		myNick = nick;
		sendButton.addActionListener(new sendEvent());
		inText.addActionListener(new sendEvent());
		outTextPanel.add(userList = new ChannelUsers(), BorderLayout.EAST);
		
		createPopupMenu();
		MouseListener popupListener = new PopupListener();
		userList.getUserList().addMouseListener(popupListener);

		setVisible(true);
		
		addWindowListener(new java.awt.event.WindowAdapter() {
    		@Override
    		public void windowClosing(WindowEvent e) {
    			channel.part("");
    			
    			// Saves the preferences of the workspace window.
    			Dimension d = getSize();
    			Point p = getLocation();
    			pref.putInt("chatwindow.width", d.width);
   				pref.putInt("chatwindow.height", d.height);
   				pref.putInt("chatwindow.x pos", p.x);
   				pref.putInt("chatwindow.y pos", p.y);
   			} 
    	});
		
	}
	
	private void createPopupMenu(){
		JMenuItem muteItem = new JMenuItem(res.getString("IrcClientChannelChat.popupMenu.muteItem"));
		JMenuItem privateChatItem = new JMenuItem(res.getString("IrcClientChannelChat.popupMenu.priChatItem"));
		JMenuItem whoItem = new JMenuItem(res.getString("IrcClientChannelChat.popupMenu.whoItem"));
		
		
		userInteraction = new JPopupMenu();
		userInteraction.add(muteItem);
		userInteraction.add(privateChatItem);
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
	
	
	public ChannelUsers getUserList() {
		return userList;
	}
	
	public void setUserList(ChannelUsers userList) {
		outTextPanel.remove(userList);
		this.userList = userList;
		MouseListener popupListener = new PopupListener();
		userList.getUserList().addMouseListener(popupListener);
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
	
	/**
	 * 
	 * Sends message written by user to server.
	 *
	 */
	class sendEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = inText.getText();
			if(message.length() <= 512 && message.length() > 3){
				if(message.startsWith("/")){
					String[] command = message.split(" ", 2);
					switch(command[0].toLowerCase()){
						case "/join":	if(command.length == 2)channel.getSession().join(command[1]); 
										else outText.errorMessage("Not enough parameters"); break;
						case "/part":	if(command.length == 2)channel.part(command[1]);  break;
						case "/away":	if(command.length <= 2)if(channel.getSession().isAway()) channel.getSession().setAway(command[1]); else channel.getSession().unsetAway(); break;
						case "/me": 
						case "/action":	channel.action(command[1]); break;
						case "/changenick":
						case "/nick":	channel.getSession().changeNick(command[1]); break;
						case "/devoice":
						case "/mute": 	break;
						case "/invite":	break;
						case "/modes": 	break;
						case "/setmode":
						case "/mode": 	break;
						case "/msg": 	if(command.length == 3) channel.getSession().sayPrivate(command[1], command[2]);
									 	else outText.errorMessage("Not enough parameters"); break; 
						case "/private":if(command.length == 2) channel.getSession().sayPrivate(myNick, command[1]);
					 					else outText.errorMessage("Not enough parameters"); break; 
						default : outText.recieveMessage(res.getString("IrcClientChannelChat.popupMenu.whoItem"), "Error"); break;
					}
				}
				else{
					channel.say(message);
					outText.recieveMessage("<"+myNick+">: "+message, "Message");
				}
				inText.setText("");
			}
		}
	}
	
	class PopupListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	    	userList.getUserList().setSelectedIndex(getRow(e.getPoint()));
	        showPopup(e);
	    }

		public void mouseReleased(MouseEvent e) {
			userList.getUserList().setSelectedIndex(getRow(e.getPoint()));
	        showPopup(e);
	    }

	    private void showPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	            userInteraction.show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }
	    private int getRow(Point point) {
            return userList.getUserList().locationToIndex(point);
	    }
	}


}
