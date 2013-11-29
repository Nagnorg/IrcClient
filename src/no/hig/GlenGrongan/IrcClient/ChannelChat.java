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
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jerklib.Channel;
import jerklib.Session;
import jerklib.events.modes.ModeAdjustment;
/**
 * Class creating and maintaining the chatwindow of a channel. Originally known as chatWindow,
 * but were later made to inherit from this class instead.
 * @author Glen
 *
 */
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
	/**
	 * Sets the userlist of the channel, and updates it.
	 * @param userList a list of users.
	 */
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
	
	/**
	 * 
	 * Sends message written by user to server.
	 *	Takes text written in inText and handles it based on command/no command.
	 */
	class sendEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = inText.getText();
			if(message.length() <= 512 && message.length() > 3){
				if(message.startsWith("/")){
					String[] command = message.split(" ", 2);
					switch(command[0].toLowerCase()){
						case "/join":	if(command.length == 2)channel.getSession().join(command[1]);	// joins a channel 
										else outText.errorMessage(res.getString("IrcClientChannelChat.incorrectArguments2")); break;
						case "/part":	if(command.length >= 2)channel.part(command[1]); 	// parts a channel
										else channel.part(""); break;
						case "/away":	if(command.length <= 2) 
											if(channel.getSession().isAway()) 
												channel.getSession().setAway(command[1]); 
										else channel.getSession().unsetAway();
										break;
						case "/me": 
						case "/action":	channel.action(command[1]); break;		// User sends a action if command is /action or /me
						case "/changenick":
						case "/nick":	channel.getSession().changeNick(command[1]); break;		// User changes nick if command is /nick or /changenick
						case "/voice":
						case "/unmute": if(paramCheck(command.length, 2)) channel.voice(command[1]);
										break;
						case "/devoice":
						case "/mute": 	if(paramCheck(command.length, 2)) channel.deVoice(command[1]);
										break;
						case "/op":		if(paramCheck(command.length, 2)) channel.op(command[1]);
										break;
						case "/deop":	if(paramCheck(command.length, 2)) channel.deop(command[1]);
										break;
						case "/invite":	if(paramCheck(command.length, 3)) {
											String[] parameter = command[1].split(" ", 2);
											if(parameter.length == 2 && (channel.getSession().getChannel(parameter[1]) != null))
												channel.getSession().invite(command[0], channel.getSession().getChannel(parameter[1]));
											else if(paramCheck(parameter.length, 3));
											else res.getString("IrcClientChannelChat.channelNotFound");
										}
										break;
						case "/mode": 	// The mode adjustment class proved too arcane to fully comprehend how to utilize
										break;
						case "/who": 	if(paramCheck(command.length, 2)) channel.getSession().who(command[1]);
										break;
						case "/whois":  if(paramCheck(command.length, 2)) channel.getSession().whois(command[1]);
										break;
						case "/whowas": if(paramCheck(command.length, 2)) channel.getSession().whoWas(command[1]);
										break;
						case "/msg": 	if(paramCheck(command.length, 3)) {		// Sends a private message to user.
											String[] parameter = command[1].split(" ", 2);
											if(paramCheck(command.length, 3)) channel.getSession().sayPrivate(parameter[0], parameter[1]);
										}
										break;
						case "/private":if(paramCheck(command.length, 2)) channel.getSession().sayPrivate(myNick, command[1]);	// Opens a chatwindow for privatechat with user.
					 					break; 
						default : outText.recieveMessage(res.getString("IrcClientChannelChat.popupMenu.whoItem"), "Error"); break;
					}
				}
				else{	// If no command is written
					channel.say(message);
					outText.recieveMessage("<"+myNick+">: "+message, "Message");
				}
				inText.setText("");
			}
		}
	}
	
	/**
	 * Shortens the output when checking string length by excluding numerous else cases
	 * @param length The length of a string
	 * @param output The string resource to be used in case the length isn't 2
	 * @return true if length is 2, otherwise false
	 */
	public boolean paramCheck(int length, int output) {
		if(length == 2) return true;
		else if(output == 2) outText.recieveMessage(res.getString("IrcClientChannelChat.incorrectArguments2"), "Error");
		else if(output == 3) outText.recieveMessage(res.getString("IrcClientChannelChat.incorrectArguments3"), "Error");
		return false;
	}
	
	
	/**
	 * Creates popup for the channeluserlist.
	 * @author Glen
	 *
	 */
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
