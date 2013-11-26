package no.hig.GlenGrongan.IrcClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class ConnectOptionsModel implements ComboBoxModel{

	List<String> elements;
	String selectedElement;
	@Override
	public int getSize() {
		return elements.size();
	}

	@Override
	public Object getElementAt(int index) {
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
	public Object getSelectedItem() {
		return selectedElement;
	}
	
	public ConnectOptionsModel(String type){
		elements = new ArrayList<String>();
		selectedElement = null;
		try{
			BufferedReader br = new BufferedReader(new FileReader("servers.ini"));
			try{
				String line = br.readLine();
				String docArea = "";
				while(line != null){
					if(line.matches("\\[[a-z]+\\]")){
						docArea = line;
						line = br.readLine();
					}
					if(line.matches("n\\d{1,3}=.+")){
						if(docArea.contains("networks") && type == null){
							String[] networkName = line.split("=");
							elements.add(networkName[1]);
							//System.out.println(index);
						}
						else if(line.matches("n\\d{1,3}="+type+".+")){
							String[] serverName = line.split(":");
							elements.add(serverName[2]);
						}
					}
					line = br.readLine();
				}
				
			}
			catch(IOException e){
				System.err.println("Mistake happened during reading.");
			}
		}catch(FileNotFoundException fnfe){System.err.println("Specified file not found");}
		if(elements.size()> 0) selectedElement = elements.get(0);
	}
}
