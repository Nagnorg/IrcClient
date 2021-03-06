package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import jerklib.ConnectionManager;
import jerklib.Profile;
/**
 * Class containing creation of GUI and base functionality for the client.
 * @version 0.2
 * @author Glen & Martin
 *
 */
public class IrcClientWorkspace extends JFrame{
	ConnectionList connectionList;	// The connectionlist of the program.
	ConnectOptions cOptions;		// A frame for creating a new connection.
	ConnectionManager conManager;
	Profile profile;				// Profile of the clientuser.
	
	JPanel serverListPanel;
	JToolBar workspaceToolbar;
	
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
		

		
		workspaceToolbar = createToolbar();
		add(workspaceToolbar, BorderLayout.NORTH);
		workspaceToolbar.setVisible(pref.getBoolean("workspace.toolbarVisible", true));
		
		JMenuBar workspaceMenu = createMenu();
		setJMenuBar(workspaceMenu);
		
		
		addWindowListener(new java.awt.event.WindowAdapter() {
	    		@Override
	    		public void windowClosing(WindowEvent e) {
	    			// Exits the connectionmanager
	    			if(conManager != null) conManager.quit();
	    			
	    			// Saves the preferences of the workspace window.
	    			Dimension d = getSize();
	    			Point p = getLocation();
	    			pref.putInt("window width", d.width);
	   				pref.putInt("window height", d.height);
	   				pref.putInt("window x pos", p.x);
	   				pref.putInt("window y pos", p.y);
	   				pref.putBoolean("workspace.toolbarVisible", workspaceToolbar.isVisible());
	   			} 
	    	});
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	// Creates the toolbar for the client.
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
	// Creates the menu for the client.
	private JMenuBar createMenu(){
		JMenuItem newConnectionItem = new JMenuItem(res.getString("IrcClientWorkspace.menu.newConnection"));
		newConnectionItem.setMnemonic('N');
		newConnectionItem.addActionListener(new NewConnection());
		
		JMenuItem removeConnectionItem = new JMenuItem(res.getString("IrcClientWorkspace.menu.removeConnection"));
		removeConnectionItem.setMnemonic('R');
		removeConnectionItem.addActionListener(new RemoveConnection());
		
		JMenuItem customizeEventItem = new JMenuItem(res.getString("IrcClientWorkspace.menu.customizeEvents"));
		customizeEventItem.setMnemonic('C');
		customizeEventItem.addActionListener(new Customize());
		
		JMenuItem changeLanguageItem = new JMenuItem(res.getString("IrcClientWorkspace.menu.languageItem"));
		changeLanguageItem.setMnemonic('L');
		
		JMenuItem toolbarItem = new JMenuItem(res.getString("IrcClientWorkspace.menu.toolbarItem"));
		toolbarItem.setMnemonic('T');
		toolbarItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(workspaceToolbar.isVisible() == true) workspaceToolbar.setVisible(false);
				else workspaceToolbar.setVisible(true);
			}
		});
		
		JMenuItem windowHelpItem = new JMenuItem(res.getString("IrcClientWorkspace.menu.windowHelpItem"));
		windowHelpItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				connectionList.getInfoPanel().getServerText().recieveMessage(res.getString("IrcClient.help.windowLeft"), "Notice");
				
			}
			
		});
		JMenuItem commandHelpItem = new JMenuItem(res.getString("IrcClientWorkspace.menu.commandHelpItem"));
		commandHelpItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 1; i <=5; i++){
					connectionList.getInfoPanel().getServerText().recieveMessage(res.getString("IrcClient.help.command"+i), "Notice");
				}
				
			}
			
		});
		
		JMenu fileMenu = new JMenu(res.getString("IrcClientWorkspace.menu.file"));
		fileMenu.setMnemonic('F');
		fileMenu.add(newConnectionItem);
		fileMenu.add(removeConnectionItem);
		fileMenu.add(customizeEventItem);
		JMenu settingsMenu = new JMenu(res.getString("IrcClientWorkspace.menu.settings"));
		settingsMenu.setMnemonic('S');
		settingsMenu.add(changeLanguageItem);
		settingsMenu.add(toolbarItem);
		JMenu helpMenu = new JMenu(res.getString("IrcClientWorkspace.menu.help"));
		helpMenu.setMnemonic('H');
		helpMenu.add(windowHelpItem);
		helpMenu.add(commandHelpItem);
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(settingsMenu);
		menuBar.add(helpMenu);
		
		
		return menuBar;
		
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
