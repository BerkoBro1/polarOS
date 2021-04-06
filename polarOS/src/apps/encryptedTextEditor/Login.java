package apps.encryptedTextEditor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.*;
import java.util.*;

import javax.swing.*;

import sys64.CreateFrame;

public class Login extends JPanel implements ActionListener{
	JLabel userL = new JLabel("Username: ");
	JTextField userTF = new JTextField();
	JLabel passL = new JLabel("Password: ");
	JPasswordField passTF = new JPasswordField();
	JPanel loginP = new JPanel(new GridLayout(3, 2));
	JPanel panel = new JPanel();
	JButton login = new JButton("Login");
	JButton register = new JButton("Register");
	CardLayout cl;
	Login(){
		setLayout(new CardLayout());
		loginP.add(userL);
		loginP.add(userTF);
		loginP.add(passL);
		loginP.add(passTF);
		login.addActionListener(this);
		register.addActionListener(this);
		loginP.add(login);
		loginP.add(register);
		panel.add(loginP);
		add(panel, "login");
		cl = (CardLayout) getLayout();
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == login) {
			try {
				BufferedReader input = new BufferedReader(new FileReader("root\\apps\\textEditor\\passwords.txt"));
				String pass = null;
				String line = input.readLine();
				while(line != null) {
					StringTokenizer st = new StringTokenizer(line);
					if(userTF.getText().equals(st.nextToken())) {
						pass = st.nextToken();
					}
					line = input.readLine();
				}
				input.close();
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(new String(passTF.getPassword()).getBytes());
				byte byteData[] = md.digest();
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < byteData.length; i++) {
					sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
				}
				if(pass.equals(sb.toString())) {
					add(new FileBrowser("root\\apps\\textEditor\\" + userTF.getText()), "fb");
					cl.show(this, "fb");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		if(arg0.getSource() == register) {
			add(new Register(), "register");
			cl.show(this, "register");
		}
	}

	
	public static void start(JDesktopPane pane) {
		JInternalFrame frame = CreateFrame.create(600, 600, 0, 0, "Text Editor", pane, true, true, 2, false, null, true, "Encrypted Text Editor", "apps.encryptedTextEditor.Login");
		Login login = new Login();
		frame.setVisible(true);
		frame.add(login);
	}
	
}
