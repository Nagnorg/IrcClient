package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.prefs.Preferences;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

class RecieveTextPanel extends JPanel{
	JTextPane text;
	JScrollPane textScrollPane;
	Preferences pref;
	public RecieveTextPanel(){
		setLayout(new BorderLayout());
		add(textScrollPane = new JScrollPane(text = new JTextPane()));
		text.setContentType("charset=UTF-8");
		text.setEditable(false);
		pref = Preferences.userNodeForPackage( getClass() );
	}
	public void recieveMessage(String message, String eventType){
		int pos = text.getStyledDocument().getEndPosition().getOffset();
		SimpleAttributeSet sas = new SimpleAttributeSet ();
		StyleConstants.setFontFamily(sas, pref.get("customized"+eventType+"Font", "Calibri"));
		StyleConstants.setFontSize(sas, pref.getInt("customized"+eventType+"FontSize", 12));
		StyleConstants.setBold(sas, pref.getBoolean("customized"+eventType+"Bold", false));
		StyleConstants.setItalic(sas, pref.getBoolean("customized"+eventType+"Italic", false));
		StyleConstants.setForeground(sas, new Color(pref.getInt("customized"+eventType+"Foreground", Color.black.getRGB())));
		StyleConstants.setBackground(sas, new Color(pref.getInt("customized"+eventType+"Background", Color.white.getRGB())));
		
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