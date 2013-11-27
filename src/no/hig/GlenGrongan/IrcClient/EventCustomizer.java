package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EventCustomizer extends JFrame{
	JPanel listPanel;
	JList eventList;
	DefaultListModel eventListModel;
	JScrollPane eventListScroller;
	JPanel textPanel;
	JPanel previewPanel;
	JTextArea previewArea;
	JPanel fontPanel;
	JComboBox fontOptions;
	JSpinner fontSize;
	JCheckBox bold;
	JCheckBox italic;
	JPanel colorPanel;
	JButton background;
	JButton foreground;
	Color foregroundColor;
	Color backgroundColor;
	Color tempColor; // Variable used to avoid duplication of code.
	JColorChooser cChooser;
	
	String selectedEvent;
	
	Preferences pref;
	ResourceBundle res;
	
	public EventCustomizer() {
		pref = Preferences.userNodeForPackage( getClass() );
		res =  ResourceBundle.getBundle("IrcClient", new Locale(pref.get("IrcClient.language", "en")));
		this.setTitle(res.getString("IrcClientEventCustomizer.title"));
		setLayout(new BorderLayout());
		add(listPanel = createListPanel(), BorderLayout.CENTER);	// Creates the list
		eventList.addListSelectionListener(		// Adds listener for selections of elements
				new ListSelectionListener(){

					@Override
					public void valueChanged(ListSelectionEvent e) {
						if(textPanel != null) remove(textPanel);
						add(textPanel = createTextPanel(), BorderLayout.SOUTH);	
						selectedEvent = (String) eventList.getSelectedValue();
						textPanel.updateUI();
				
					}
			
			});
		eventList.setSelectedIndex(0);
	}
	
	private JPanel createListPanel(){
		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());
		eventListModel = new DefaultListModel();
		String[] eventTypes = 
	{"Away", "ConnectionComplete", "ConnectionLost", "Error", "Invite", "Information", "Join", "JoinComplete", "Kick", 
				"Mode", "Motd", "Message", "NickChange", "Notice", "Part", "Quit", "ServerInformation", 
				"ServerVersion", "Topic", "Who", "Whois", "WhoWas"};
		for(String event : eventTypes){ 
			eventListModel.addElement(event);
		}
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
		layout.add(createCustomPanel(), BorderLayout.SOUTH);
		previewPanel.add(previewArea = new JTextArea(res.getString("IrcClientEventCustomizer.previewPanel.defText")), BorderLayout.CENTER);
		JButton setButton = new JButton(res.getString("IrcClientEventCustomizer.previewPanel.setStyle"));
		previewPanel.add(setButton, BorderLayout.EAST);
		setButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				pref.put("customized"+selectedEvent+"Font", (String) fontOptions.getSelectedItem());
				pref.putBoolean("customized"+selectedEvent+"Bold", bold.isSelected());
				pref.putBoolean("customized"+selectedEvent+"Italic", italic.isSelected());
				pref.putInt("customized"+selectedEvent+"FontSize", (Integer)fontSize.getValue());
				pref.putInt("customized"+selectedEvent+"Foreground", foregroundColor.getRGB());
				pref.putInt("customized"+selectedEvent+"Background", backgroundColor.getRGB());
				
			}
			
		});
		changePreviewArea();
		layout.add(previewPanel, BorderLayout.CENTER);
		
		return layout;
		
	}
	
	private JPanel createCustomPanel(){
		JPanel layout = new JPanel(); layout.setLayout(new GridLayout(1,2));
		GridBagLayout l = new GridBagLayout();
		
		
		fontPanel = new JPanel(); fontPanel.setLayout(l);
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel tempLabel;
		
		gbc.gridx = 1; gbc.gridy = 1;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.EAST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		l.setConstraints(tempLabel = new JLabel(res.getString("IrcClientEventCustomizer.customPanel.fontLabel")), gbc);
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
		fontOptions.setSelectedItem(pref.get("customized"+selectedEvent+"Font", "Calibri"));
		fontOptions.addActionListener(new ChangeFontElement());
		
		bold = new JCheckBox(res.getString("IrcClientEventCustomizer.customPanel.bold"));
		gbc.gridx = 1; gbc.gridy = 2;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		l.setConstraints(bold, gbc);
		fontPanel.add(bold);
		bold.setSelected(pref.getBoolean("customized"+selectedEvent+"Bold", false));
		bold.addActionListener(new ChangeFontElement());
		
		italic = new JCheckBox(res.getString("IrcClientEventCustomizer.customPanel.italic"));
		gbc.gridx = 1; gbc.gridy = 3;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		l.setConstraints(italic, gbc);
		fontPanel.add(italic);
		italic.setSelected(pref.getBoolean("customized"+selectedEvent+"Italic", false));
		italic.addActionListener(new ChangeFontElement());
		
		gbc.gridx = 3; gbc.gridy = 2;
		gbc.gridwidth = 1; gbc.gridheight = 2;
		gbc.anchor = java.awt.GridBagConstraints.EAST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		l.setConstraints(tempLabel = new JLabel(res.getString("IrcClientEventCustomizer.customPanel.sizeLabel")), gbc);
		fontPanel.add(tempLabel);
		
		fontSize = new JSpinner(new SpinnerNumberModel(pref.getInt("customized"+selectedEvent+"FontSize", 12), 6, 18, 1));
		gbc.gridx = 4; gbc.gridy = 2;
		gbc.gridwidth = 1; gbc.gridheight = 2;
		gbc.anchor = java.awt.GridBagConstraints.EAST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		l.setConstraints(fontSize, gbc);
		fontPanel.add(fontSize);
		fontSize.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				changePreviewArea();
				
			}
			
		}); 
		
		
		
		colorPanel = new JPanel(); colorPanel.setLayout(new GridLayout(2,1));
		foregroundColor = new Color(pref.getInt("customized"+selectedEvent+"Foreground", Color.black.getRGB()));
		colorPanel.add(foreground = new JButton(res.getString("IrcClientEventCustomizer.customPanel.foreground")));
		foreground.addActionListener(new ActionListener(){ // TODO: Fix duplication

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFrame colorChooser = new JFrame(res.getString("IrcClientEventCustomizer.customPanel.chooseColor"));
				cChooser = new JColorChooser(foregroundColor);
				cChooser.getSelectionModel().addChangeListener(new ChangeListener(){

					@Override
					public void stateChanged(ChangeEvent e) {
						Color newColor = cChooser.getColor();
						foregroundColor = newColor;
						changePreviewArea();
					}
					
				});
				colorChooser.add(cChooser);
				colorChooser.setVisible(true);
				colorChooser.pack();
				
			}
			
		});
		backgroundColor = new Color(pref.getInt("customized"+selectedEvent+"Background", Color.white.getRGB()));
		colorPanel.add(background = new JButton(res.getString("IrcClientEventCustomizer.customPanel.background")));
		background.addActionListener(new ActionListener(){ // TODO: Fix duplication

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFrame colorChooser = new JFrame(res.getString("IrcClientEventCustomizer.customPanel.chooseColor"));
				cChooser = new JColorChooser(backgroundColor);
				cChooser.getSelectionModel().addChangeListener(new ChangeListener(){

					@Override
					public void stateChanged(ChangeEvent e) {
						Color newColor = cChooser.getColor();
						backgroundColor = newColor;
						changePreviewArea();
					}
					
				});
				colorChooser.add(cChooser);
				colorChooser.setVisible(true);
				colorChooser.pack();
				
			}
			
		});
		
		
		layout.add(fontPanel);
		layout.add(colorPanel);
		return layout;
	}
	
	
	public void changePreviewArea(){		// Updates the preview of user edited fonts.
		Font newFont;
		if(bold.isSelected()&&italic.isSelected()){
			newFont = new Font((String) fontOptions.getSelectedItem(), Font.BOLD+Font.ITALIC, ((Integer)fontSize.getValue()));
		}
		else if(bold.isSelected() && italic.isSelected()!=true){
			newFont = new Font((String) fontOptions.getSelectedItem(), Font.BOLD, ((Integer)fontSize.getValue()));
		}
		else if(bold.isSelected()!=true&&italic.isSelected()){
			newFont = new Font((String) fontOptions.getSelectedItem(), Font.ITALIC, ((Integer)fontSize.getValue()));
		}
		else newFont = new Font((String) fontOptions.getSelectedItem(), Font.PLAIN, ((Integer)fontSize.getValue()));
		previewArea.setFont(newFont);
		previewArea.setForeground(foregroundColor);
		previewArea.setBackground(backgroundColor);
	}
	public void changeColor(){				// Changes the active foreground or background color UNUSED
		JFrame colorChooser = new JFrame("Choose a color");
		cChooser = new JColorChooser(tempColor);
		cChooser.getSelectionModel().addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				Color newColor = cChooser.getColor();
				tempColor = newColor;
				changePreviewArea();
			}
			
		});
		colorChooser.add(cChooser);
		colorChooser.setVisible(true);
		colorChooser.pack();
	}
	
	class ChangeFontElement implements ActionListener{	// Used by most font editors.

		@Override
		public void actionPerformed(ActionEvent e) {
			changePreviewArea();
			}
	}
}
