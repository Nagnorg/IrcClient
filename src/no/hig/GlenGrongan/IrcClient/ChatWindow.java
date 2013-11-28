package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Class containing the shared gui for chat windows.
 * @version 0.4
 * @author Glen
 *
 */
public abstract class ChatWindow extends JFrame{
	JPanel outTextPanel;	// Panel containing information FROM server.
	JPanel inTextPanel;		// Panel containing information going TO server.
	
	RecieveTextPanel outText;	// Messages going FROM server.
	JTextField inText;		// Messages going TO server.
	JButton sendButton;
	JScrollPane textScrollPane;
	
	Preferences pref;
	ResourceBundle res;
	public ChatWindow(String s1, String s2){
		super(s1 + ":" + s2);
		pref = Preferences.userNodeForPackage( getClass() );
		res =  ResourceBundle.getBundle("IrcClient", new Locale(pref.get("IrcClient.language", "en")));
		setSize (pref.getInt("chatwindow.width", 300), pref.getInt("chatwindow.height", 100));
		setLocation (pref.getInt("chatwindow.x pos", 100), pref.getInt("chatwindow.y pos", 100));
		
		setLayout(new BorderLayout());
		add (outTextPanel = createOutTextPanel(), BorderLayout.CENTER);
		add(inTextPanel = createInTextPanel(), BorderLayout.SOUTH);
	}
	
	// Creates basic layout for outTextPanel.
	private JPanel createOutTextPanel() {
		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());
		layout.add (outText = new RecieveTextPanel(), BorderLayout.CENTER);
		return layout;
	}
	
	// Creates basic layout for inTextPanel.
	private JPanel createInTextPanel() {
		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());
		layout.add(inText = new JTextField(), BorderLayout.CENTER);
		layout.add(sendButton = new JButton("Send"), BorderLayout.EAST);
		
		return layout;
	}
	
	/**
	 * @return the textpane
	 */
	public RecieveTextPanel getOutText() {
		return outText;
	}

	/**
	 * @return the outText
	 */
	public JTextField getInText() {
		return inText;
	}

}
