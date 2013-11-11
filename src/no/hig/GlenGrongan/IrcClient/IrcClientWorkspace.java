package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
/**
 * Class containing creation of GUI and most of its handling for the IRC client.
 * @version 0.1
 * @author Glen & Martin
 *
 */
public class IrcClientWorkspace extends JFrame{
	JPanel serverListPanel;
	JTextPane inText;
	JTextField outText;
	JButton sendButton;
	JScrollPane textScrollPane;
	public IrcClientWorkspace(){
		setLayout(new BorderLayout());
		add(serverListPanel = new JPanel(), BorderLayout.WEST);
		serverListPanel.setLayout(new BorderLayout());
		serverListPanel.add(new JLabel("IRC servers"), BorderLayout.NORTH);
		
		add(createChatWindow(), BorderLayout.CENTER);

		
		JToolBar workspaceToolbar = createToolbar();
		add(workspaceToolbar, BorderLayout.NORTH);
		workspaceToolbar.setVisible(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private JToolBar createToolbar(){
		JButton newConnection = new JButton("New Connection");
		JButton removeConnection = new JButton ("Remove Connection");
		JButton newChatWindow = new JButton("New chat window");
		
		JToolBar toolbar = new JToolBar();
		toolbar.add(newConnection);
		toolbar.add(removeConnection);
		toolbar.add(newChatWindow);
		
		newConnection.addActionListener(new NewConnection());
		
		return toolbar;
		
	}
	
	private JPanel createChatWindow(){
		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());
		layout.add (textScrollPane = new JScrollPane (inText = new JTextPane()), BorderLayout.CENTER);
		inText.setEditable(false);
		layout.add(createOutTextPanel(), BorderLayout.SOUTH);
		
		return layout;
	}
	
	private JPanel createOutTextPanel(){
		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());
		layout.add(outText = new JTextField(), BorderLayout.CENTER);
		layout.add(sendButton = new JButton("Send"), BorderLayout.EAST);
		
		return layout;
	}
	
	private void createMenu(){}

	class NewConnection implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame connectionWindow = new JFrame();
			ConnectOptions options = new ConnectOptions(inText);
			connectionWindow.add(options, BorderLayout.NORTH);
			connectionWindow.pack();
			connectionWindow.setVisible(true);
		}
		
	}
	
	
	public static void main(String[] args){
		IrcClientWorkspace workspace = new IrcClientWorkspace();
		workspace.setSize(700,300);
		
		workspace.setVisible(true);
	}
}
