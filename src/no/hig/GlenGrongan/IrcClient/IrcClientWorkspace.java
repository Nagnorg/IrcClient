package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import jerklib.Channel;
import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
/**
 * Class containing creation of GUI and most of its handling for the IRC client.
 * @version 0.2
 * @author Glen & Martin
 *
 */
public class IrcClientWorkspace extends JFrame{
	JPanel serverListPanel;
	ChatWindow chatWindow;
	ConnectionList connectionList;
	ConnectOptions cOptions;
	ConnectionManager conManager;
	ServerInformation informationPanel;

	public IrcClientWorkspace(){
		super("g&mIRC");
		setLayout(new BorderLayout());
		add(connectionList = new ConnectionList(conManager), BorderLayout.CENTER);
		

		
		JToolBar workspaceToolbar = createToolbar();
		add(workspaceToolbar, BorderLayout.NORTH);
		workspaceToolbar.setVisible(true);
		

		addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
	        	if(conManager != null) conManager.quit();
	     } });
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private JToolBar createToolbar(){
		JButton newConnection = new JButton("New Connection");
		JButton removeConnection = new JButton ("Remove Connection");
		JButton newChatWindow = new JButton("New chat window");
		
		JToolBar toolbar = new JToolBar();
		toolbar.add(newConnection);
		toolbar.add(removeConnection);
		toolbar.add(newChatWindow);
		
		newConnection.addActionListener(new NewConnection());
		
		return toolbar;
		
	}
	
	private void createMenu(){}

	class NewConnection implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame connectionWindow = new JFrame();
			cOptions = new ConnectOptions();
			connectionWindow.add(cOptions, BorderLayout.NORTH);
			connectionWindow.pack();
			cOptions.getConnectButton().addActionListener(new CreateSession());
			connectionWindow.setVisible(true);
		}
		
	}
	class CreateSession implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(conManager == null)
			{
				conManager = new ConnectionManager(new Profile(cOptions.getInsertNick().getText(), cOptions.getInsertAlternative().getText()));
			}
			new ConnectionSetup(((String)cOptions.getNetworkChosen().getSelectedItem()), conManager, connectionList);
			connectionList.setConManager(conManager);
		}
		
	}
	
	
	public static void main(String[] args){
		IrcClientWorkspace workspace = new IrcClientWorkspace();
		workspace.setSize(700,300);
		
		workspace.setVisible(true);
	}
}
