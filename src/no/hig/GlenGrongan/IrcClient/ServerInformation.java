package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import jerklib.Session;

/**
 * Panel giving server specific information.
 * @version 0.1
 * @author Glen
 *
 */
class ServerInformation extends JPanel{
	RecieveTextPanel serverText;
	JPanel channelListPanel;
	
	JList channelList;
	DefaultListModel channelListModel;
	JTextField channelSearch;
	JButton channelSearchButton;
	JButton joinChannelButton;
	JScrollPane channelListScroller;
	Session session;
	
	public ServerInformation(){
		setLayout(new BorderLayout());
		add(serverText = new RecieveTextPanel(), BorderLayout.CENTER);
		
		add(channelListPanel = createChannelListPanel(), BorderLayout.EAST);
	}
	
	private JPanel createChannelListPanel() {
		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());
		channelList = new JList();
		channelListModel = new DefaultListModel();
		channelList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		channelList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		searchPanel.add(channelSearch = new JTextField(10), BorderLayout.CENTER);
		searchPanel.add(channelSearchButton = new JButton("Search"), BorderLayout.EAST);
		channelSearchButton.addActionListener(new searchForChannel());
		channelSearch.addActionListener(new searchForChannel());
		
		
		layout.add(channelListScroller = new JScrollPane(channelList), BorderLayout.CENTER);
		layout.add(searchPanel, BorderLayout.NORTH);
		layout.add(joinChannelButton = new JButton("Join"), BorderLayout.SOUTH);
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
			channelList.setSelectedValue(channelSearch.getText(), true);
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


