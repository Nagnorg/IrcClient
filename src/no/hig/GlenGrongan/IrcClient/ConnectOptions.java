/**
 * 
 */
package no.hig.GlenGrongan.IrcClient;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import no.hig.GlenGrongan.IrcClient.IrcClientWorkspace.CreateSession;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.prefs.Preferences;
import java.util.regex.*;
import java.util.List;

/**
 * Class containing creation of GUI for the user to create a connection.
 * Mostly autogenerated by the GUI renderer made in the last project.
 * @version 0.1
 * @author Glen & Martin
 *
 */
	public class ConnectOptions extends JFrame {
		ConnectOptionsModel networkList;
		ConnectOptionsModel serverList;
		
		Preferences pref;
		ResourceBundle res;
		//Autogenerated code
		JLabel label1;
		JComboBox networkChosen = new JComboBox();
		JComboBox serverChosen = new JComboBox();
		JButton connectButton;
		JButton addButton;
		JButton editButton;
		JButton removeButton;
		//JButton sortButton = new JButton("Sort");
		JLabel label2;
		JLabel label3;
		JLabel label4;
		JLabel label5;
		JTextField insertName = new JTextField("", 12);
		JTextField insertNick = new JTextField("", 12);
		JTextField insertAlt1 = new JTextField("", 10);
		JTextField insertAlt2 = new JTextField("", 10);
		JButton okButton;
		JButton cancelButton;
	
	public ConnectOptions () {
		super("Options");
		
		pref = Preferences.userNodeForPackage( getClass() );
		res =  ResourceBundle.getBundle("IrcClient", new Locale(pref.get("IrcClient.language", "en")));
		
		// Handles the creation of new servers
		addButton.addActionListener(
				new ActionListener(){
					// TODO: String resources
					@Override
					public void actionPerformed(ActionEvent e) {
						String name = JOptionPane.showInputDialog(null, "Provide a name for the new channel", "Add name", JOptionPane.PLAIN_MESSAGE);
						if(name.matches(".*\\w+\\.[\\w-]+\\.\\w+.*")) {
							editXML(name, "Add");
							serverList = new ConnectOptionsModel(loadXML());
							serverChosen.setModel(serverList);
						}
						else JOptionPane.showMessageDialog(null, "The name you provided does not appear to be a server name");
					}	
				});
		
		// Handles the editing of current servers
		editButton.addActionListener(
				new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						if(networkList.getSelectedIndex() == 0) {
							String oldName = serverList.getSelectedItem();
							String newName = JOptionPane.showInputDialog(null, "Provide a new name for the " +oldName, "Edit name", JOptionPane.PLAIN_MESSAGE);
							if(newName.matches(".*\\w+\\.[\\w-]+\\.\\w+.*")) {
								editXML(oldName, newName);
								serverList = new ConnectOptionsModel(loadXML());
								serverChosen.setModel(serverList);
							}
							else JOptionPane.showMessageDialog(null, "The name you provided does not appear to be a server name");
						} else JOptionPane.showMessageDialog(null, "You can only change the name of servers on your favorites list");
					}
				});
		
		// Handles the deletion of current servers
		removeButton.addActionListener(
				new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						if(networkList.getSelectedIndex() == 0) {
							String name = serverList.getSelectedItem();
							if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " +name+ "?", "Accept?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
								editXML(name, "Remove");
								serverList = new ConnectOptionsModel(loadXML());
								serverChosen.setModel(serverList);	
							}
							
						} else JOptionPane.showMessageDialog(null, "You can only remove the servers on your favorites list");
					}
				});
		
		// Initializes network and server list
		// Network list retrieves a list of names from the servers.ini file
		// Servers are added from the xml file, assuming that the user wants his custom options presented to him first
		networkList = new ConnectOptionsModel(loadIni((null)));
		networkChosen.setModel(networkList);
		serverList = new ConnectOptionsModel(loadXML());
		serverChosen.setModel(serverList);
		
		networkChosen.addItemListener(
				new ItemListener(){

					// Reads a new list of servers each time the network is changed
					// Data is taken from either the xml or ini file, depending on whether the network is set to favorites or not, the first network in the list
					@Override
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.SELECTED){
							if(networkList.getSelectedIndex() == 0) {
								serverList = new ConnectOptionsModel(loadXML());
								serverChosen.setModel(serverList);
							}
							else {
								serverList = new ConnectOptionsModel(loadIni(networkList.getSelectedItem()));
								serverChosen.setModel(serverList);
							}
						}
						
					}
					
				});
		
		//Autogenerated code
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(layout);
		
		label1 = new JLabel(res.getString("IrcClientConnectOptions.networkLabel"));
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(label1, gbc);
		add(label1);

		gbc.gridx = 5;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.EAST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(networkChosen, gbc);
		add(networkChosen);

		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.EAST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(serverChosen, gbc);
		add(serverChosen);
		
		connectButton = new JButton(res.getString("IrcClientConnectOptions.connectButton"));
		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		gbc.gridheight = 2;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(connectButton, gbc);
		add(connectButton);
		
		addButton = new JButton(res.getString("IrcClientConnectOption.addButton"));
		gbc.gridx = 6;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		layout.setConstraints(addButton, gbc);
		add(addButton);
		
		editButton = new JButton(res.getString("IrcClientConnectOption.editButton"));
		gbc.gridx = 6;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		layout.setConstraints(editButton, gbc);
		add(editButton);
		
		removeButton = new JButton(res.getString("IrcClientConnectOption.removeButton"));
		gbc.gridx = 6;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		layout.setConstraints(removeButton, gbc);
		add(removeButton);

		/*gbc.gridx = 6;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		layout.setConstraints(sortButton, gbc);
		add(sortButton);*/
		
		label2 = new JLabel(res.getString("IrcClientConnectOption.nameLabel"));
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(label2, gbc);
		add(label2);
		
		label3 = new JLabel(res.getString("IrcClientConnectOption.nickLabel"));
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(label3, gbc);
		add(label3);
		
		label4 = new JLabel(res.getString("IrcClientConnectOption.alt1Label"));
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(label4, gbc);
		add(label4);
		
		label5 = new JLabel(res.getString("IrcClientConnectOption.alt2Label"));
		gbc.gridx = 1;
		gbc.gridy = 9;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(label5, gbc);
		add(label5);

		gbc.gridx = 3;
		gbc.gridy = 6;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(insertName, gbc);
		add(insertName);

		gbc.gridx = 3;
		gbc.gridy = 7;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(insertNick, gbc);
		add(insertNick);

		gbc.gridx = 3;
		gbc.gridy = 8;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(insertAlt1, gbc);
		add(insertAlt1);

		gbc.gridx = 3;
		gbc.gridy = 9;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(insertAlt2, gbc);
		add(insertAlt2);
		
		okButton = new JButton(res.getString("IrcClientConnectOption.okButton"));
		gbc.gridx = 3;
		gbc.gridy = 10;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		layout.setConstraints(okButton, gbc);
		add(okButton);
		
		cancelButton = new JButton(res.getString("IrcClientConnectOption.cancelButton"));
		gbc.gridx = 4;
		gbc.gridy = 10;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		layout.setConstraints(cancelButton, gbc);
		add(cancelButton);
		// End of autogenerated code
		
		
		// Handles the creation of new servers
				addButton.addActionListener(
						new ActionListener(){
							// TODO: String resources
							@Override
							public void actionPerformed(ActionEvent e) {
								String name = JOptionPane.showInputDialog(null, "Provide a name for the new channel", "Add name", JOptionPane.PLAIN_MESSAGE);
								if(name.matches(".*\\w+\\.[\\w-]+\\.\\w+.*")) {
									editXML(name, "Add");
									serverList = new ConnectOptionsModel(loadXML());
									serverChosen.setModel(serverList);
								}
								else JOptionPane.showMessageDialog(null, "The name you provided does not appear to be a server name");
							}	
						});
				
				// Handles the editing of current servers
				editButton.addActionListener(
						new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								if(networkList.getSelectedIndex() == 0) {
									String oldName = serverList.getSelectedItem();
									String newName = JOptionPane.showInputDialog(null, "Provide a new name for the " +oldName, "Edit name", JOptionPane.PLAIN_MESSAGE);
									if(newName.matches(".*\\w+\\.[\\w-]+\\.\\w+.*")) {
										editXML(oldName, newName);
										serverList = new ConnectOptionsModel(loadXML());
										serverChosen.setModel(serverList);
									}
									else JOptionPane.showMessageDialog(null, "The name you provided does not appear to be a server name");
								} else JOptionPane.showMessageDialog(null, "You can only change the name of servers on your favorites list");
							}
						});
				
				// Handles the deletion of current servers
				removeButton.addActionListener(
						new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								if(networkList.getSelectedIndex() == 0) {
									String name = serverList.getSelectedItem();
									if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " +name, "Accept?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
										editXML(name, "Remove");
										serverList = new ConnectOptionsModel(loadXML());
										serverChosen.setModel(serverList);	
									}
									
								} else JOptionPane.showMessageDialog(null, "You can only remove the servers on your favorites list");
							}
						});
				
				
		
		
		}

	/**
	 * @return the networkChosen
	 */
	public JComboBox<String> getNetworkChosen() {
		return networkChosen;
	}

	/**
	 * @return the serverChosen
	 */
	public JComboBox getserverChosen() {
		return serverChosen;
	}

	/**
	 * @return the connectButton
	 */
	public JButton getConnectButton() {
		return connectButton;
	}

	/**
	 * @return the addButton
	 */
	public JButton getAddButton() {
		return addButton;
	}

	/**
	 * @return the editButton
	 */
	public JButton getEditButton() {
		return editButton;
	}

	/**
	 * @return the removeButton
	 */
	public JButton getRemoveButton() {
		return removeButton;
	}

	/**
	 * @return the insertName
	 */
	public JTextField getInsertName() {
		return insertName;
	}

	/**
	 * @return the insertNick
	 */
	public JTextField getInsertNick() {
		return insertNick;
	}

	/**
	 * @return the insertAlt1
	 */
	public JTextField getInsertAlt1() {
		return insertAlt1;
	}

	/**
	 * @return the insertAlt2
	 */
	public JTextField getInsertAlt2() {
		return insertAlt2;
	}

	/**
	 * @return the okButton
	 */
	public JButton getOkButton() {
		return okButton;
	}

	/**
	 * @return the cancelButton
	 */
	public JButton getCancelButton() {
		return cancelButton;
	}

	
	/**
	 * Reads through a local .ini file and processes the text in it, generating strings to be inserted into a drop-down list
	 * @param name The name of the parent network when fetching servers, or null when fetching networks
	 * @return elements A string list representing either servers or networks
	 */
	public List<String> loadIni(String name) {
		List<String> elements = new ArrayList<String>();
		
		elements = new ArrayList<String>();
		try{
			BufferedReader br = new BufferedReader(new FileReader("servers.ini"));
			try{
				String line = br.readLine();
				String docArea = "";
				while(line != null){
					
					// If the input is a bracket enclosed message, meaning [timestamp], [networks], [servers], [recent] or [success]
					if(line.matches("\\[[a-z]+\\]")){
						docArea = line;
						line = br.readLine();
					}
					
					// If the input starts with a network id, expressed as n\\d=
					if(line.matches("^n\\d{1,3}=.+")){
						
						// Reads network name, enters if name is set as null and the last bracked enclosed message was [network]
						if(docArea.contains("networks") && name == null){
							String[] networkName = line.split("=");
							elements.add(networkName[1]);
						}
						
						// Reads server names, enters if the input string contains the parent network
						else if(line.matches("^n\\d{1,3}=" +name+ ".+")){
							String[] serverName = line.split(":");
							elements.add(serverName[2]);
						}
					}
					line = br.readLine();
				}
				
			}
			catch(IOException e){
				JOptionPane.showMessageDialog(null, "An error occured while attempting to read a file.");
			}
			
		} catch(FileNotFoundException fnfe) { 
			System.err.println("Specified file not found"); 
		}
		
		// TODO: String manipulation
		if(elements.size() > 0 && name == null) elements.add(0, "Favorites");
		return elements;
	}
	
	/**
	 * Reads through an xml file and creates a list of all servers under the favorites tab
	 * @return servers A list containing the names of servers managed by the user
	 */
	public List<String> loadXML() {
		List<String> servers = new ArrayList<String>();
	
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// TODO: Add string resource
			Document document = builder.parse("src/favorites.xml");
			NodeList nodeList = document.getDocumentElement().getChildNodes();
			
			// Iterates through the address elements containing the string values
			for(int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				
				if(node instanceof Element) {
					String content = node.getLastChild().getTextContent().trim();
					if(node.getNodeName().equals("address")) servers.add(content);
				}
			}
		} catch (ParserConfigurationException e) {
			
		} catch (SAXException e) {
			
		} catch (IOException e) {
			
		}
		return servers;
	}
	
	/**
	 * Changes the content of the favorites.xml file, either by adding new addresses, editing their content, or removing them
	 * @param name The name of the node to be added, or the node to be manipulated
	 * @param mode Contains "Add" if a node is to be added, "Remove" if a node is to be removed, or the name of the new node if a noe is to be edited
	 */
	public void editXML(String name, String mode) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse("src/favorites.xml");
			document.getDocumentElement().normalize();
			Node servers = document.getElementsByTagName("servers").item(0);
			
			// Appends a new address node to the xml tree
			if(mode.equals("Add")) {
				Element address = document.createElement("address");
				address.appendChild(document.createTextNode(name));
				servers.appendChild(address);
			}
			
			// If we're removing or editing a node, iterate through the address nodes and locate it
			else {
				NodeList addresses = servers.getChildNodes();
				for(int i = 0; i < addresses.getLength(); i++) {
					Node node = addresses.item(i);
					
					// The name of the node matches the provided name, delete or edit, according to mode
					if(node.getTextContent().equals(name)) {
						if(mode.equals("Remove")) servers.removeChild(node);
						else node.setTextContent(mode);
						break; // The breakpoint ensures that only one element is changed, if multiple instances of it exist
					}
				}
			}
			
			// Save the changes to the xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			File file = new File("src/favorites.xml");
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			
		} catch (ParserConfigurationException e) {
			
		} catch (TransformerConfigurationException e) {
			
		} catch (TransformerException e) {
			
		} catch (SAXException e) {
			
		} catch (IOException e) {
			
		}
	}
}
