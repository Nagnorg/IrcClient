package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import jerklib.Session;

/**
 * Panel creating and maintaining server specific informations and actions.
 * @version 0.1
 * @author Glen
 *
 */
class ServerInformation extends JPanel{
	RecieveTextPanel serverText;	// Panel for writing server information.
	JPanel channelListPanel;		// Panel containing the possibility to search for channels
	
	JList channelList;				// List with possibility to contain channels the user specify.
	DefaultListModel channelListModel;	// List model for the channelList.
	JTextField channelSearch;		// Area for user to search for channels.
	JButton channelSearchButton;
	JButton joinChannelButton;
	JScrollPane channelListScroller;
	Session session;				// Owner of object.
	
	Preferences pref;
	ResourceBundle res;
	
	public ServerInformation(){
		pref = Preferences.userNodeForPackage( getClass() );
		res =  ResourceBundle.getBundle("IrcClient", new Locale(pref.get("IrcClient.language", "en")));
		
		setLayout(new BorderLayout());
		add(serverText = new RecieveTextPanel(), BorderLayout.CENTER);
		add(channelListPanel = createChannelListPanel(), BorderLayout.EAST);
		session = null;
	}
	
	private JPanel createChannelListPanel() {
		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());
		layout.setMaximumSize(new Dimension(10, 20));
		channelList = new JList();
		channelListModel = new DefaultListModel();
		channelList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		channelList.setLayoutOrientation(JList.VERTICAL_WRAP);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		searchPanel.add(channelSearch = new JTextField("", 10), BorderLayout.CENTER);
		searchPanel.add(channelSearchButton = new JButton(res.getString("IrcClientServerInformation.channelList.searchButton")), BorderLayout.EAST);
		channelSearchButton.addActionListener(new searchForChannel());
		channelSearch.addActionListener(new searchForChannel());
		
		
		layout.add(channelListScroller = new JScrollPane(channelList), BorderLayout.CENTER);
		layout.add(searchPanel, BorderLayout.NORTH);
		layout.add(joinChannelButton = new JButton(res.getString("IrcClientServerInformation.channelList.joinButton")), BorderLayout.SOUTH);
		joinChannelButton.addActionListener(new joinChannel());
		return layout;
	}

	public DefaultListModel getChannelListModel(){
		return channelListModel;
	}
	
	public void updateList(){
		channelListPanel.remove(channelListScroller);
		channelList = new JList<String>(channelListModel);
		channelListPanel.add(channelListScroller = new JScrollPane(channelList));
		channelListPanel.updateUI();
	}
	
	
	
	class joinChannel implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			for(Object channelName : channelList.getSelectedValuesList())
			session.join((String)channelName);
		}
	}
	class searchForChannel implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			if(session != null){
				channelListModel.removeAllElements();
				String searchString = channelSearch.getText();
				if(searchString.length() > 3){
					if(searchString.startsWith("#")) session.chanList(searchString);
					else session.chanList();
				}
				serverText.recieveMessage(res.getString("IrcClientServerInformation.serverTest.searchingString"), "Information");
			}
		}
	}
	/**
	 * @return the serverText
	 */
	public RecieveTextPanel getServerText() {
		return serverText;
	}

	/**
	 * @return the channelList
	 */
	public JList getChannelList() {
		return channelList;
	}

	/**
	 * @return the channelSearch
	 */
	public JTextField getChannelSearch() {
		return channelSearch;
	}

	/**
	 * @return the channelSearchButton
	 */
	public JButton getChannelSearchButton() {
		return channelSearchButton;
	}

	/**
	 * @return the joinChannelButton
	 */
	public JButton getJoinChannelButton() {
		return joinChannelButton;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}
}


