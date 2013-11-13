/**
 * 
 */
package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import jerklib.Channel;
import jerklib.ConnectionManager;
import jerklib.Session;

/**
 * @author Glen
 *
 */
public class ConnectionList extends JPanel{
	DefaultMutableTreeNode top;
	JTree conTree;
	ConnectionManager conManager;
	List<DefaultMutableTreeNode> serverNodes = new ArrayList<DefaultMutableTreeNode>();
	public ConnectionList(ConnectionManager conMan){
		setLayout(new BorderLayout());
		top = new DefaultMutableTreeNode("IRC servers");
		//if(conManager != null) top.add(createNodes());
		conTree = new JTree(top);
		add(new JScrollPane(conTree), BorderLayout.NORTH);
	}
	
	public DefaultMutableTreeNode createNodes() {
		DefaultMutableTreeNode servernode = null;
		DefaultMutableTreeNode channelnode = null;
		for(Session sessionInstance : conManager.getSessions()) {
			servernode = new DefaultMutableTreeNode(sessionInstance.getConnectedHostName());
			for(Channel channelInstance : sessionInstance.getChannels()) {
				channelnode = new DefaultMutableTreeNode(channelInstance.getName());
				servernode.add(channelnode);
			}

		}
		return servernode;
	}
	
	public void addServerNode(String serverName){
		serverNodes.add(new DefaultMutableTreeNode(serverName));
		top.add(serverNodes.get(serverNodes.size()-1));
		conTree.updateUI();
	}
	public void addChannelNode(String serverName, String channelName){
		int index = 0;
		for(int i = 0; i<conManager.getSessions().size(); i++){
			if(conManager.getSessions().get(i).getConnectedHostName() == serverName) index = (conManager.getSessions().size()-1)-i;
		}
		serverNodes.get(index).add(new DefaultMutableTreeNode(channelName));
		conTree.updateUI();
	}
	/**
	 * @return the top
	 */
	public DefaultMutableTreeNode getTop() {
		return top;
	}
	/**
	 * @param top the top to set
	 */
	public void setTop(DefaultMutableTreeNode top) {
		this.top = top;
	}
	/**
	 * @return the conTree
	 */
	public JTree getConTree() {
		return conTree;
	}
	/**
	 * @param conTree the conTree to set
	 */
	public void setConTree(JTree conTree) {
		this.conTree = conTree;
	}

	/**
	 * @param conManager the conManager to set
	 */
	public void setConManager(ConnectionManager conManager) {
		this.conManager = conManager;
	}

}
