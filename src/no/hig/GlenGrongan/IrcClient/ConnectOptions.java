/**
 * 
 */
package no.hig.GlenGrongan.IrcClient;

import javax.swing.*;
import java.awt.*;


/**
 * @author Glen
 *
 */
public class ConnectOptions extends JPanel{
	public void createLayout() {
		GridBagLayout layout = new GridBagLayout ();
		setLayout(layout);
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets (0,2,2,2);
		
		valueConstraints(gbc, 0, 0, 2, 1);
		gbc.anchor = GridBagConstraints.EAST;
		FlowLayout flow = new FlowLayout();
		JPanel networkName = new JPanel();
		networkName.setLayout(flow);
		String[] networkStrings = {"A IRC server", "11", "another IRC thingy"};
		JComboBox networkNames = new JComboBox(networkStrings);
		networkName.add(networkNames);
		layout.setConstraints(networkName, gbc);
		add(networkName);
		
		valueConstraints(gbc, 3, 0, 1, 2);
		
		JPanel editButtons = new JPanel();
		editButtons.setLayout(new GridLayout(4,0));
		JButton addButton = new JButton("Legg til");
		JButton editButton = new JButton("Endre");
		JButton deleteButton = new JButton ("Slett");
		JButton sortButton = new JButton("Sorter");
		editButtons.add(addButton);
		editButtons.add(editButton);
		editButtons.add(deleteButton);
		editButtons.add(sortButton);
		layout.setConstraints(editButtons, gbc);
		add(editButtons);
		
		
	}
	public GridBagConstraints valueConstraints(GridBagConstraints gbc, int xValue, int yValue, int widthValue, int heightValue){
		gbc.gridx = xValue;
		gbc.gridy = yValue;
		gbc.gridwidth = widthValue;
		gbc.gridheight = heightValue;
		return gbc;
	}
}
