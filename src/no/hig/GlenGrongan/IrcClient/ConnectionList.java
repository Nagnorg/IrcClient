/**
 * 
 */
package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import jerklib.Channel;
import jerklib.ConnectionManager;
import jerklib.Session;

/**
 * Class for containing and maintaining most of the "global" information. 
 * Most noteably it maintains the JTree for connections, but it's responsible for switching out the serverinformationpanels as well.
 * @version 0.2
 * @author Glen
 *
 */
public class ConnectionList extends JPanel{
	DefaultMutableTreeNode top; // Rootnode for the connectionlist tree
	JTree conTree; // connectionlist tree.
	ConnectionManager conManager;
	
	JPanel mainPanel;
	ServerInformation informationPanel; 
	JPanel treePanel;
	
	List<DefaultMutableTreeNode> serverNodes = new ArrayList<DefaultMutableTreeNode>();
	List<DefaultMutableTreeNode> channelNodes = new ArrayList<DefaultMutableTreeNode>();
	List<ServerInformation> knownServerInformation = new ArrayList<ServerInformation>();
	List<ChatWindow> knownChatWindows = new ArrayList<ChatWindow>();
	
	Preferences pref;
	ResourceBundle res;
	
	/**
	 * @param conMan the connectionmanager
	 */
	public ConnectionList(ConnectionManager conMan){
		pref = Preferences.userNodeForPackage( getClass() );
		res =  ResourceBundle.getBundle("IrcClient", new Locale(pref.get("IrcClient.language", "en")));
		
		setLayout(new BorderLayout());
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(treePanel = createListPanel(), BorderLayout.WEST);
		mainPanel.add(informationPanel = new ServerInformation(), BorderLayout.CENTER);
		add(mainPanel,BorderLayout.CENTER);
	}
	
	private JPanel createListPanel() {
		JPanel layout = new JPanel();
		top = new DefaultMutableTreeNode(res.getString("IrcClientconList.Tree.top"));
		conTree = new JTree(top);
		conTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		// Adds a listener for when a node is selected.
		conTree.addTreeSelectionListener(new TreeSelectionListener(){

			@Override
			public void valueChanged(TreeSelectionEvent e) {
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                        conTree.getLastSelectedPathComponent();
		        if (node == null) return;
		        Object nodeInfo = node.getUserObject();
		        if(node.getLevel() == 2){	// If channelnodes
		        	for(ChatWindow instance : knownChatWindows){
		        		if(((ChannelChat)instance).getChannel().getName() == (String) nodeInfo){
		        			if(instance.getState() == instance.ICONIFIED){
		        				instance.toFront();
		        				instance.setState(instance.NORMAL);
		        			}
		        			else instance.setState(instance.ICONIFIED);
		        		}
		        	}
		        	changeInformationPanel((String)((DefaultMutableTreeNode) node.getParent()).getUserObject());

		        }	// If server node
		        else if(node.getLevel() == 1){
		        	changeInformationPanel((String) nodeInfo);
		        }
				conTree.clearSelection();
			}
			
		});
		layout.add(new JScrollPane(conTree), BorderLayout.NORTH);
		return layout;
	}
	/**
	 * Changes the active informationpanel.
	 * @param newInfo name of session owning the new panel.
	 */
	private void changeInformationPanel(String newInfo) {
		ServerInformation newPanel = null;
			for(ServerInformation instance : knownServerInformation){
				if(instance.getSession().getConnectedHostName().equals(newInfo)){
					if(instance != informationPanel){
						newPanel = instance;
					}
				}
			}
		if(newPanel == null) newPanel = knownServerInformation.get(0);
		mainPanel.remove(informationPanel);
		informationPanel = newPanel;
		mainPanel.add(informationPanel, BorderLayout.CENTER);
		informationPanel.updateUI();
		
	}
	
	/**
	 * Adds a new servernode when user connects to new server.
	 * @param serverName name of the recently added server.
	 * @param si recently added server information
	 */
	public void addServerNode(String serverName, ServerInformation si){
		serverNodes.add(new DefaultMutableTreeNode(serverName));
		knownServerInformation.add(si);
		top.add(serverNodes.get(serverNodes.size()-1));
		conTree.updateUI();
	}
	/**
	 * Adds a new channelnode user joins new channel is.
	 * @param serverName name of server the channel belongs
	 * @param channelName name of recently added channel
	 * @param cw recently added chatwindow
	 */
	public void addChannelNode(String serverName, String channelName, ChatWindow cw){
		channelNodes.add(new DefaultMutableTreeNode(channelName));
		knownChatWindows.add(cw);
		serverNodes.get(getIndex(serverName, serverNodes)).add(channelNodes.get(channelNodes.size()-1));
		conTree.updateUI();
	}
	/**
	 * Removes a server node when the user quits the server
	 * @param serverName server to quit.
	 * @param si information of server to quit.
	 */
	public void removeServerNode(String serverName, ServerInformation si){
		top.remove(getIndex(serverName, serverNodes));
		serverNodes.remove(getIndex(serverName, serverNodes));
		knownServerInformation.remove(si);
		conTree.updateUI();
	}
	/**
	 * Removes a channelnode when user parts a channel.
	 * @param serverName name of server the channel belongs
	 * @param channelName name of parted channel/private chat
	 * @param cw chatwindow belonging to parted channel/private chat
	 */
	public void removeChannelNode(String serverName, String channelName, ChatWindow cw){
		serverNodes.get(getIndex(serverName, serverNodes)).remove(channelNodes.get(getIndex(channelName, channelNodes)));
		channelNodes.remove(channelName);
		knownChatWindows.remove(cw);
		updateUI();
	}
	/**
	 * Finds the index of a DefaultMutableTreeNode object in a list, based on its userobject and sent name
	 * @param findName name of object wanted
	 * @param list object to search through
	 * @return index of String, -1 if it's not found.
	 */
	private int getIndex(String findName, List list){
		int index = -1;
		for(int i = 0; i<list.size(); i++){
			if(((DefaultMutableTreeNode) list.get(i)).getUserObject().equals(findName)) index = i;
		}
		return index;
	}
	/**
	 * @return The root node of the tree.
	 */
	public DefaultMutableTreeNode getTop() {
		return top;
	}
	/**
	 * @return a tree containing the connections
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
	 * @param updates the connectionmanager.
	 */
	public void setConManager(ConnectionManager conManager) {
		this.conManager = conManager;
	}
	/**
	 * Returns the currently selected server.
	 * @return
	 */
	public ServerInformation getInfoPanel(){
		return informationPanel;
	}

}
