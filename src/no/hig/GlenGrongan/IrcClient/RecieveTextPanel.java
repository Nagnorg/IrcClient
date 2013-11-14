package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

class RecieveTextPanel extends JPanel{
	JTextPane text;
	JScrollPane textScrollPane;
	public RecieveTextPanel(){
		setLayout(new BorderLayout());
		add(textScrollPane = new JScrollPane(text = new JTextPane()));
		text.setEditable(false);
	}
	public void recieveMessage(String message){
		int pos = text.getStyledDocument().getEndPosition().getOffset();
		SimpleAttributeSet sas = new SimpleAttributeSet ();
		try {
			// add the text to the document
			text.getStyledDocument().insertString(pos, message, sas);
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
}