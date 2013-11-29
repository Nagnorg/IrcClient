package no.hig.GlenGrongan.IrcClient;

import jerklib.Channel;
import jerklib.events.modes.ModeAdjustment;

/**
 * The User class provides an interface for interacting with user objects, primarily sorting them through the Comparable implementation
 * Otherwise, it functions by and large like a string list, but is used to virtually represent a node in a list of users
 */
public class User implements Comparable<User> {
	String name;
	boolean operator;

	public User(String name, Channel channel) {
		this.name = name;
		operator = hasOp(channel, name);
	}
	
	/**
	 * Retrieves the name attribute
	 * @return The name of a User object
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Changes the name attribute
	 * @param name The new name for the User object
	 */
	public void setName(String name) {
		this.name = name;	
	}
	
	/**
	 * Checks if a user has operator privileges or not
	 * @return true if user is an operator, otherwise false
	 */
	public boolean getPrivilege() {
		return operator;
	}
	
	/**
	 * Checks if a user on a given channel has operator privileges
	 * Content by and large taken from http://hig-irc.googlecode.com/svn-history/r171/trunk/src/src/irc/controller/ConnectController.java
	 *  of which I didn't notice the author before long
	 * @param channel The channel as a context to get the list of operators
	 * @param name The name to check the user by
	 * @return True if user is operator on the given channel, otherwise false
	 */
	public boolean hasOp(Channel channel, String name) {
		return channel.getNicksForMode(ModeAdjustment.Action.PLUS,'o').toString().contains(name);
	}

	/**
	 * Overrides the way Collections.sort interacts with the objects
	 * Compares two objects, returns 1 if first user has op and second doesn't, -1 if the opposite
	 * If both are equal, it compares the strings instead to determine order
	 */
	@Override
	public int compareTo(User compareUser) {
		int comparePrivilege = (this.operator) ? 1 : -1;
		if(!this.operator == this.getPrivilege()) return comparePrivilege;
		return this.name.compareTo(compareUser.getName());
	}
	
	
}
