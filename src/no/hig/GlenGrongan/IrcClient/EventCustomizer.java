package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

public class EventCustomizer extends JFrame{
	JPanel listPanel;
	JList eventList;
	DefaultListModel eventListModel;
	JScrollPane eventListScroller;
	JPanel previewPanel;
	JTextArea previewArea;
	JPanel fontPanel;
	JComboBox fontOptions;
	JSpinner fontSize;
	JCheckBox bold;
	JCheckBox italic;
	JPanel colorPanel;
	JButton backGround;
	JButton foreGround;
	
	public EventCustomizer() {
		super("Event Customizer");
		setLayout(new BorderLayout());
		add(listPanel = createListPanel(), BorderLayout.CENTER);
		add(createTextPanel(), BorderLayout.SOUTH);
	}
	
	private JPanel createListPanel(){
		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());
		eventListModel = new DefaultListModel();
		String[] eventTypes = 
	{"Away", "ConnectionComplete", "ConnectionLost", "Error", "Invite", "Join", "Kick", "NickChange", "Notice", "Mode", "Motd", "Message", "Part", "Quit", "ServerInformation", "ServerVersion", "Topic", "Whi", "Whois", "WhoWas"};
		for(String event : eventTypes){ eventListModel.addElement(event);}
		eventList = new JList(eventListModel);
		eventList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		eventList.setLayoutOrientation(JList.VERTICAL_WRAP);
		
		layout.add(eventListScroller = new JScrollPane(eventList), BorderLayout.CENTER);
		return layout;
	}
	
	private JPanel createTextPanel(){
		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());
		previewPanel = new JPanel(); previewPanel.setLayout(new BorderLayout());
		previewPanel.add(previewArea = new JTextArea("This is a preview"), BorderLayout.CENTER);
		layout.add(previewArea, BorderLayout.NORTH);
		
		layout.add(createCustomPanel(), BorderLayout.CENTER);
		
		return layout;
		
	}
	
	private JPanel createCustomPanel(){
		JPanel layout = new JPanel(); layout.setLayout(new GridLayout(2,1));
		GridBagLayout l = new GridBagLayout();
		fontPanel = new JPanel(); fontPanel.setLayout(l);
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel tempLabel;
		
		gbc.gridx = 1; gbc.gridy = 1;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.EAST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		l.setConstraints(tempLabel = new JLabel("Font"), gbc);
		fontPanel.add(tempLabel);
		
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fonts = ge.getAvailableFontFamilyNames();
		fontOptions = new JComboBox(fonts);
		gbc.gridx = 2; gbc.gridy = 1;
		gbc.gridwidth = 3; gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.EAST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		l.setConstraints(fontOptions, gbc);
		fontPanel.add(fontOptions);
		
		bold = new JCheckBox("bold");
		gbc.gridx = 1; gbc.gridy = 2;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		l.setConstraints(bold, gbc);
		fontPanel.add(bold);
		
		italic = new JCheckBox("italic");
		gbc.gridx = 1; gbc.gridy = 3;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		l.setConstraints(italic, gbc);
		fontPanel.add(italic);
		
		gbc.gridx = 3; gbc.gridy = 2;
		gbc.gridwidth = 1; gbc.gridheight = 2;
		gbc.anchor = java.awt.GridBagConstraints.EAST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		l.setConstraints(tempLabel = new JLabel("Font size"), gbc);
		fontPanel.add(tempLabel);
		
		fontSize = new JSpinner();
		gbc.gridx = 4; gbc.gridy = 2;
		gbc.gridwidth = 1; gbc.gridheight = 2;
		gbc.anchor = java.awt.GridBagConstraints.EAST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		l.setConstraints(fontSize, gbc);
		fontPanel.add(fontSize);
		
		colorPanel = new JPanel();
		
		
		
		layout.add(fontPanel);
		layout.add(colorPanel);
		return layout;
	}
}
