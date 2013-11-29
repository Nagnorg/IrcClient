package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
/**
 * Class creating and maintaining the userlists found in chatwindows for chatchannels.
 */
public class ChannelUsers extends JPanel {
	List<User> users;

	JList userList;
	DefaultListModel userListModel;
	JScrollPane userListScroller;
	/**
	 * Initializes the object as empty.
	 */
	public ChannelUsers() {
		this.users = new ArrayList<User>();
		Collections.sort(users);
		createLayout();
	}
	
	/**
	 * Initializes the object with a list of users
	 * @param users a list of users.
	 */
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
	/**
	 * Sets this' objects userlist as users
	 * @param users
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}
	/**
	 * Gets this objects userlist
	 * 
	 */
	public List<User> getUsers() {
		return users;
	}
	/**
	 * 
	 * @return the graphical user list
	 */
	public JList getUserList() {  // Returns graphical user list.
		return this.userList;
	}
	
	/**
	 * adds a user in the graphical user list
	 * @param user object to be added.
	 */
	public void addUser(User user) {
		users.add(user);
		Collections.sort(users);
		userListModel.add(users.indexOf(user), user.getName());
		updateUI();
	}
	/**
	 * removes a user in the graphical user list
	 * @param user object to be removed.
	 */
	public void removeUser(String nick) {
		int index = getUserPosition(nick);
		if(index != -1) {
			
			users.remove(index);
			userListModel.remove(index);
			updateUI();
		}
	}
	/**
	 * Renames a user in the list.
	 * @param oldnick Old nickname of the user.
	 * @param newnick New nickname of the user.
	 */
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
	
	/**
	 * For finding the position of a known nickname in the userlist.
	 * @param nick Wanted nickname
	 * @return index of user with wanted nick, or -1 if this is not found
	 */
	public int getUserPosition(String nick) {
		for(User user : users)
			if(user.getName().equals(nick))
				return users.indexOf(user);
		return -1;
	}
	
	/**
	 * For finding the position of a known user object in the userlist.
	 * @param user
	 * @return index of user object
	 */
	public int getUserPosition(User user) {
		return users.indexOf(user);
	}

}
