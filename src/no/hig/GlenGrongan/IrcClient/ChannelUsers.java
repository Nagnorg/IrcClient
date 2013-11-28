package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import jerklib.Session;

public class ChannelUsers extends JPanel {
	List<User> users;

	JList userList;
	DefaultListModel userListModel;
	//JTextField channelSearch;
	//JButton channelSearchButton;
	JScrollPane userListScroller;
	public ChannelUsers() {
		this.users = new ArrayList<User>();
		Collections.sort(users);
		createLayout();
	}
	
	public ChannelUsers(List<User> users) {
		this.users = users;
		Collections.sort(users);
		createLayout();
	}
	
	public void createLayout() {
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(100,200));
		setMaximumSize(new Dimension(100, 200));
		userListModel = new DefaultListModel();
		for(User usersInstance : users) userListModel.addElement(usersInstance.getName());
		userList = new JList(userListModel);
		userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		userList.setLayoutOrientation(JList.VERTICAL_WRAP);
		
		add(userListScroller = new JScrollPane(userList), BorderLayout.CENTER);
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public JList getUserList() {  // Returns graphical user list.
		return this.userList;
	}
	
	public void addUser(User user) {
		users.add(user);
		Collections.sort(users);
		userListModel.add(users.indexOf(user), user.getName());
		updateUI();
	}
	
	public void removeUser(String nick) {
		int index = getUserPosition(nick);
		if(index != -1) {
			
			users.remove(index);
			userListModel.remove(index);
			updateUI();
		}
	}
	
	public void renameUser(String oldnick, String newnick) {
		int index = getUserPosition(oldnick);
		if(index != -1) { 
			userListModel.remove(index);
			users.get(index).setName(newnick);
			Collections.sort(users);
			userListModel.add(getUserPosition(newnick), newnick);
			updateUI();
		}
	}
	
	public int getUserPosition(String nick) {
		for(User user : users)
			if(user.getName().equals(nick))
				return users.indexOf(user);
		return -1;
	}
	
	public int getUserPosition(User user) {
		return users.indexOf(user);
	}
	
	public void messageUser(String nick) {
		
	}

}
