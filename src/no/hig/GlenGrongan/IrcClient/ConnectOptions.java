/**
 * 
 */
package no.hig.GlenGrongan.IrcClient;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class containing creation of GUI for the user to create a connection.
 * Mostly autogenerated by the GUI renderer made in the last project.
 * @version 0.1
 * @author Glen & Martin
 *
 */
	public class ConnectOptions extends JPanel {
		//Autogenerated code
		JLabel label1 = new JLabel("IRC Network");
		String[] networkArray = {"irc.freenode.net"};
		JComboBox<String> networkChosen = new JComboBox<String>(networkArray);
		JComboBox Var2 = new JComboBox();
		JButton connectButton = new JButton("Connect to server");
		JButton addButton = new JButton("Add");
		JButton editButton = new JButton("Edit");
		JButton removeButton = new JButton("Remove");
		JButton sortButton = new JButton("Sort");
		JLabel label2 = new JLabel("Full name");
		JLabel label3 = new JLabel("E-Mail");
		JLabel label4 = new JLabel("Nickname");
		JLabel label5 = new JLabel("Alternative");
		JTextField insertName = new JTextField("", 12);
		JTextField insertEmail = new JTextField("", 12);
		JTextField insertNick = new JTextField("", 10);
		JTextField insertAlternative = new JTextField("", 10);
		JButton okButton = new JButton("Ok");
		JButton cancelButton = new JButton("Cancel");
		
		JTextPane inText;
	public ConnectOptions (JTextPane textPane) {
		//Autogenerated code
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(layout);

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

		gbc.gridx = 5;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.EAST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(Var2, gbc);
		add(Var2);

		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		gbc.gridheight = 2;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(connectButton, gbc);
		add(connectButton);

		gbc.gridx = 6;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		layout.setConstraints(addButton, gbc);
		add(addButton);

		gbc.gridx = 6;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		layout.setConstraints(editButton, gbc);
		add(editButton);

		gbc.gridx = 6;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		layout.setConstraints(removeButton, gbc);
		add(removeButton);

		gbc.gridx = 6;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		layout.setConstraints(sortButton, gbc);
		add(sortButton);

		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(label2, gbc);
		add(label2);

		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(label3, gbc);
		add(label3);

		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(label4, gbc);
		add(label4);

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
		layout.setConstraints(insertEmail, gbc);
		add(insertEmail);

		gbc.gridx = 3;
		gbc.gridy = 8;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(insertNick, gbc);
		add(insertNick);

		gbc.gridx = 3;
		gbc.gridy = 9;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.WEST;
		gbc.fill = java.awt.GridBagConstraints.NONE;
		layout.setConstraints(insertAlternative, gbc);
		add(insertAlternative);
		
		gbc.gridx = 3;
		gbc.gridy = 10;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		layout.setConstraints(okButton, gbc);
		add(okButton);

		gbc.gridx = 4;
		gbc.gridy = 10;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = java.awt.GridBagConstraints.CENTER;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		layout.setConstraints(cancelButton, gbc);
		add(cancelButton);
		
		// Not autogenerated code
		inText = textPane;
		connectButton.addActionListener(new okButton());
		}
	
	class okButton implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			ConnectionSetup newConnection = new ConnectionSetup(((String)networkChosen.getSelectedItem()), insertNick.getText(), inText);
			
		}
		
	}
}
