package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
	ResourceBundle res;

	public IrcClientWorkspace(){
		super("g&mIRC");
		pref = Preferences.userNodeForPackage( getClass() );
		res =  ResourceBundle.getBundle("IrcClient", new Locale(pref.get("IrcClient.language", "en")));
		
		setSize (pref.getInt("window width", 700), pref.getInt("window height", 400));
		setLocation (pref.getInt("window x pos", 100), pref.getInt("window y pos", 100));
		setLayout(new BorderLayout());
		add(connectionList = new ConnectionList(conManager), BorderLayout.CENTER);
		

		
		JToolBar workspaceToolbar = createToolbar();
		add(workspaceToolbar, BorderLayout.NORTH);
		workspaceToolbar.setVisible(true);
		
		JMenuBar workspaceMenu = createMenu();
		
		

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
		JButton newConnection = new JButton(new ImageIcon(getClass().getResource("/images/toolbar/newConnection.png")));
		JButton removeConnection = new JButton (new ImageIcon(getClass().getResource("/images/toolbar/removeConnection.png")));
		JButton customWindow = new JButton(new ImageIcon(getClass().getResource("/images/toolbar/customizeEvents.png")));
		
		JToolBar toolbar = new JToolBar();
		toolbar.add(newConnection);
		toolbar.add(removeConnection);
		toolbar.add(customWindow);
		
		newConnection.addActionListener(new NewConnection());
		removeConnection.addActionListener(new RemoveConnection());
		customWindow.addActionListener(new Customize());
		return toolbar;
		
	}
	
	private JMenuBar createMenu(){
		JMenuItem newConnectionItem = new JMenuItem(res.getString("IrcClientWorkspace.menu.newConnection"));
		JMenuItem removeConnectionItem = new JMenuItem(res.getString("IrcClientWorkspace.menu.removeConnection"));
		JMenuItem customizeEventItem = new JMenuItem(res.getString("IrcClientWorkspace.menu.customizeEvents"));
		
		return null;
		
	}
	
	
	
	class NewConnection implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			cOptions = new ConnectOptions();
			cOptions.pack();
			cOptions.getInsertName().setText(pref.get("profile.userName", ""));
			cOptions.getInsertNick().setText(pref.get("profile.nickname", ""));
			cOptions.getInsertAlt1().setText(pref.get("profile.alt1", ""));
			cOptions.getInsertAlt2().setText(pref.get("profile.alt2", ""));
			cOptions.getConnectButton().addActionListener(new CreateSession());
			cOptions.setVisible(true);
		}
		
	}
	class CreateSession implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Profile profile = null;
			String userName = cOptions.getInsertName().getText();
			String nickName = cOptions.getInsertNick().getText();
			String altNick1 = cOptions.getInsertAlt1().getText();
			String altNick2 = cOptions.getInsertAlt2().getText();
			
			if(conManager == null)
			{
				conManager = new ConnectionManager(profile = new Profile(userName, nickName, altNick1, altNick2));
				pref.put("profile.userName", userName);
				pref.put("profile.nickname", nickName);
				pref.put("profile.alt1", altNick1);
				pref.put("profile.alt2", altNick2);
			}
			if(conManager != null){
				new ConnectionSetup(((String)cOptions.getserverChosen().getSelectedItem()), conManager, profile, connectionList);
				connectionList.setConManager(conManager);
				cOptions.dispose();
			}
		}
	}
	class RemoveConnection implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String server = connectionList.getInfoPanel().getSession().getConnectedHostName();
			if(conManager.getSession(server) != null){
				connectionList.removeServerNode(server, connectionList.getInfoPanel());
				conManager.getSession(server).close("Disconnected");
			}
			
		}
		
	}
	class Customize implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			EventCustomizer customizer = new EventCustomizer();	
			customizer.pack();
			customizer.setLocation(100, 300);
			customizer.setResizable(false);
			customizer.setVisible(true);
		}
		
	}
	
	
	public static void main(String[] args){
		IrcClientWorkspace workspace = new IrcClientWorkspace();
		
		workspace.setVisible(true);
	}
}
