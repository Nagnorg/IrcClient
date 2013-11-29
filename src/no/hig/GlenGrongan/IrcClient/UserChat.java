package no.hig.GlenGrongan.IrcClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jerklib.Session;
/**
 * Class creating and maintaining a chatwindow for private chats.
 * @author Glen
 *
 */
public class UserChat extends ChatWindow{

	String user;
	Session session;
	public UserChat(String s1, String s2) {
		super(s1, s2);
		//Should never run
	}
	public UserChat(String s1, String s2, Session s) {
		super(s1, s2);
		user = s2;
		session = s;
		
		sendButton.addActionListener(new sendEvent());
		inText.addActionListener(new sendEvent());
		
		setVisible(true);
	}
	/**
	 * @return who the chat is with.
	 */
	public String getUserName() {
		return this.user;
	}
	
	class sendEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = inText.getText();
			if(message.length() <= 512 && message.length() > 3){
				if(message.startsWith("/")){
					String[] command = message.split(" ", 2);
					switch(command[0].toLowerCase()){
						case "/join":	if(command.length == 2)session.join(command[1]); 
										else outText.errorMessage("Not enough parameters"); break;
						case "/away":	if(command.length <= 2)if(session.isAway()) session.setAway(command[1]); else session.unsetAway(); break;
						case "/changenick":
						case "/nick":	session.changeNick(command[1]); break;
						default : outText.recieveMessage(res.getString("IrcClientChannelChat.popupMenu.whoItem"), "Error"); break;
					}
				}
				else{
					session.sayPrivate(user, message);
					outText.recieveMessage("You said: "+message, "Message");
				}
				inText.setText("");
			}
		}
	}

}
