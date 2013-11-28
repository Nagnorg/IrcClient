package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class ChannelUsers extends JPanel {
	List<User> users;

	JList userList;
	DefaultListModel userListModel;
	JScrollPane userListScroller;
	private JPopupMenu popupTable = new JPopupMenu();
	
	public ChannelUsers() {
		this.users = new ArrayList<User>();
		createLayout();
	}
	
	public ChannelUsers(List<User> users) {
		this.users = users;
		Collections.sort(users);
		createLayout();
	}
	
	public void createLayout() {
		setLayout(new BorderLayout());
		userListModel = new DefaultListModel();
		for(User usersInstance : users) userListModel.addElement(usersInstance.getName());
		userList = new JList(userListModel);
		userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		userList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		
		add(userListScroller = new JScrollPane(userList), BorderLayout.CENTER);
		userList.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e))
					;
				
			}
		});
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void addUser(User user) {
		users.add(user);
		Collections.sort(users);
		userListModel.add(users.indexOf(user), user.getName());
	}
	
	public void removeUser(String nick) {
		int index = getUserPosition(nick);
		if(index != -1) {
			
			users.remove(index);
			userListModel.remove(index);
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
