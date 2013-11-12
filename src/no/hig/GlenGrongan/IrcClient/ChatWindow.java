package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

import jerklib.Channel;
import jerklib.Session;

/**
 * Class containing and managing all chat functionality
 * @author Glen
 *
 */
public class ChatWindow extends JPanel{
	JTextPane inText;
	JTextField outText;
	JButton sendButton;
	JScrollPane textScrollPane;
	Session session;
	Channel channel;
	
	public ChatWindow(){
		setLayout(new BorderLayout());
		add (textScrollPane = new JScrollPane (inText = new JTextPane()), BorderLayout.CENTER);
		inText.setEditable(false);
		add(createOutTextPanel(), BorderLayout.SOUTH);
	}
	
	private JPanel createOutTextPanel(){
		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());
		layout.add(outText = new JTextField(), BorderLayout.CENTER);
		layout.add(sendButton = new JButton("Send"), BorderLayout.EAST);
		sendButton.addActionListener(new sendEvent());
		
		return layout;
	}

	/**
	 * Write messages in the textpane
	 * @param message to be written in textpane
	 */
	public void recieveMessage(String message){
		int pos = inText.getStyledDocument().getEndPosition().getOffset();
		SimpleAttributeSet sas = new SimpleAttributeSet ();
		try {
			// add the text to the document
			inText.getStyledDocument().insertString(pos, message, sas);
		} catch (BadLocationException ble) {
			ble.printStackTrace();
		}
	    SwingUtilities.invokeLater (new Thread() {
	        public void run () {
	        	// Get the scrollbar from the scroll pane
	        	JScrollBar scrollbar = textScrollPane.getVerticalScrollBar ();
	        	// Set the scrollbar to its maximum value
	        	scrollbar.setValue (scrollbar.getMaximum());
	        }
	    });
	}
	
	
	/**
	 * @return the textpane
	 */
	public JTextPane getInText() {
		return inText;
	}

	/**
	 * @return the outText
	 */
	public JTextField getOutText() {
		return outText;
	}

	/**
	 * @return the textScrollPane
	 */
	public JScrollPane getTextScrollPane() {
		return textScrollPane;
	}
	
	public void setSession(Session s){
		session = s;
	}
	
	public void setChannel(Channel c){
		channel = c;
	}
	class sendEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = outText.getText();
			if(message.startsWith("/")){
				
			}
			else{
				channel.say(outText.getText());
			}
		
		}
	}
}
