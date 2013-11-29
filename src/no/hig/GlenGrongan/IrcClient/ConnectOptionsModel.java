package no.hig.GlenGrongan.IrcClient;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
/**
 * Modelclass for comboboxes in ConnectOptions.
 * @author Glen
 *
 */
public class ConnectOptionsModel implements ComboBoxModel{
	List<String> elements;
	String selectedElement;
	
	public ConnectOptionsModel(List<String> names) {
		if(names != null) {
			elements = names;
			if(elements.size() > 0) selectedElement = names.get(0);
		}
	}
	
	@Override
	public int getSize() {
		return elements.size();
	}

	@Override
	public String getElementAt(int index) {
		return elements.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		//System.out.println("Her");
		
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selectedElement = (String)anItem;
		
	}

	@Override
	public String getSelectedItem() {
		return selectedElement;
	}
	
	public int getSelectedIndex() {
		return elements.indexOf(selectedElement);
	}
}
