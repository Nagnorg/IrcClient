package no.hig.GlenGrongan.IrcClient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;

public class IrcClientWorkspace extends JFrame{
	
	public IrcClientWorkspace(){
		JToolBar workspaceToolbar = createToolbar();
		
		add(workspaceToolbar, BorderLayout.NORTH);
		workspaceToolbar.setVisible(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public JToolBar createToolbar(){
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
	
	private void createMenu(){}

	class NewConnection implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame connectionWindow = new JFrame();
			ConnectOptions options = new ConnectOptions();
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
