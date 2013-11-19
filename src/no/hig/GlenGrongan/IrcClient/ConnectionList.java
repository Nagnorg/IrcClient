/**
 * 
 */
package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

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
 * @version 0.1
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
	
	public ConnectionList(ConnectionManager conMan){
		setLayout(new BorderLayout());
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(treePanel = createListPanel(), BorderLayout.WEST);
		mainPanel.add(informationPanel = new ServerInformation(), BorderLayout.CENTER);
		add(mainPanel,BorderLayout.CENTER);
	}
	
	private JPanel createListPanel() {
		JPanel layout = new JPanel();
		top = new DefaultMutableTreeNode("IRC servers");
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
		        if(node.getLevel() == 2){
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

		        }
		        else if(node.getLevel() == 1){
		        	changeInformationPanel((String) nodeInfo);
		        }
				conTree.clearSelection();
			}

			private void changeInformationPanel(String nodeString) {
	        	for(ServerInformation instance : knownServerInformation){
	        		if(instance.getSession().getConnectedHostName() == nodeString){
	        			if(instance != informationPanel){
	        				mainPanel.remove(informationPanel);
	        				informationPanel = instance;
	    		        	mainPanel.add(informationPanel, BorderLayout.CENTER);
	    		        	informationPanel.updateUI();
	        			}
	        		}
	        	}
				
			}
			
		});
		layout.add(new JScrollPane(conTree), BorderLayout.NORTH);
		return layout;
	}
	
	public void addServerNode(String serverName, ServerInformation si){
		serverNodes.add(new DefaultMutableTreeNode(serverName));
		knownServerInformation.add(si);
		top.add(serverNodes.get(serverNodes.size()-1));
		conTree.updateUI();
	}
	public void addChannelNode(String serverName, String channelName, ChatWindow cw){
		int index = 0;
		for(int i = 0; i<conManager.getSessions().size(); i++){
			if(conManager.getSessions().get(i).getConnectedHostName() == serverName) index = (conManager.getSessions().size()-1)-i;
		}
		channelNodes.add(new DefaultMutableTreeNode(channelName));
		knownChatWindows.add(cw);
		serverNodes.get(index).add(channelNodes.get(channelNodes.size()-1));
		conTree.updateUI();
	}
	/**
	 * @return The root node
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

}
