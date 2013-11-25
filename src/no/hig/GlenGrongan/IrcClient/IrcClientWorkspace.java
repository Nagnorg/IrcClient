package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.prefs.Preferences;

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
	Preferences pref;

	public IrcClientWorkspace(){
		super("g&mIRC");
		pref = Preferences.userNodeForPackage( getClass() );
		setSize (pref.getInt("window width", 700), pref.getInt("window height", 400));
		setLocation (pref.getInt("window x pos", 100), pref.getInt("window y pos", 100));
		setLayout(new BorderLayout());
		add(connectionList = new ConnectionList(conManager), BorderLayout.CENTER);
		

		
		JToolBar workspaceToolbar = createToolbar();
		add(workspaceToolbar, BorderLayout.NORTH);
		workspaceToolbar.setVisible(true);
		

		addWindowListener(new java.awt.event.WindowAdapter() {
	    		@Override
	    		public void windowClosing(WindowEvent e) {
	    			if(conManager != null) conManager.quit();
	    			// Saves the preferences of the workspace window.
	    			Dimension d = getSize();
	    			Point p = getLocation();
	    			pref.putInt("window width", d.width);
	   				pref.putInt("window height", d.height);
	   				pref.putInt("window x pos", p.x);
	   				pref.putInt("window y pos", p.y);
	   			} 
	    	});
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private JToolBar createToolbar(){
		JButton newConnection = new JButton("New Connection");
		JButton removeConnection = new JButton ("Remove Connection");
		JButton newChatWindow = new JButton("Customize");
		
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
			JFrame connectionWindow = new JFrame("Options");
			cOptions = new ConnectOptions();
			connectionWindow.add(cOptions, BorderLayout.NORTH);
			connectionWindow.pack();
			cOptions.getInsertName().setText(pref.get("profile.userName", ""));
			cOptions.getInsertNick().setText(pref.get("profile.nickname", ""));
			cOptions.getInsertAlt1().setText(pref.get("profile.alt1", ""));
			cOptions.getInsertAlt2().setText(pref.get("profile.alt2", ""));
			cOptions.getConnectButton().addActionListener(new CreateSession());
			connectionWindow.setVisible(true);
		}
		
	}
	class CreateSession implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(conManager == null)
			{
				conManager = new ConnectionManager(new Profile(cOptions.getInsertName().getText(), cOptions.getInsertNick().getText(), cOptions.getInsertAlt1().getText(), cOptions.getInsertAlt2().getText()));
				pref.put("profile.userName", cOptions.getInsertName().getText());
				pref.put("profile.nickname", cOptions.getInsertNick().getText());
				pref.put("profile.alt1", cOptions.getInsertAlt1().getText());
				pref.put("profile.alt2", cOptions.getInsertAlt2().getText());
			}
			new ConnectionSetup(((String)cOptions.getserverChosen().getSelectedItem()), conManager, connectionList);
			connectionList.setConManager(conManager);
		}
		
	}
	
	
	public static void main(String[] args){
		IrcClientWorkspace workspace = new IrcClientWorkspace();
		
		workspace.setVisible(true);
	}
}
