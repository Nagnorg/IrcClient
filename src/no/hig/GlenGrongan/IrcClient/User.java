package no.hig.GlenGrongan.IrcClient;

import jerklib.Channel;
import jerklib.events.modes.ModeAdjustment;

public class User implements Comparable<User> {
	String name;
	boolean operator; // I have no idea how to use this thing yet

	public User(String name, Channel channel) {
		this.name = name;
		operator = hasOp(channel, name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;	
	}
	
	public boolean getPrivilege() {
		return operator;
	}
	
	// http://hig-irc.googlecode.com/svn-history/r171/trunk/src/src/irc/controller/ConnectController.java
	public boolean hasOp(Channel channel, String name) {
		return channel.getNicksForMode(ModeAdjustment.Action.PLUS,'o').toString().contains(name);
	}

	@Override
	public int compareTo(User compareUser) {
		int comparePrivilege = (this.operator) ? 1 : -1;
		if(!this.operator == this.getPrivilege()) return comparePrivilege;
		return this.name.compareTo(compareUser.getName());
	}
	
	
}
