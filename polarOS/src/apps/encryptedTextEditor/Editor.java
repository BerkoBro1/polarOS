package apps.encryptedTextEditor;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.*;

public class Editor extends JPanel implements ActionListener{
	File file;
	JButton save = new JButton("Save");
	JButton savec = new JButton("Save and Close");
	JTextArea text = new JTextArea();
	JScrollPane jsp = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	public Editor (String s) {
		file = new File(s);
		save.addActionListener(this);
		savec.addActionListener(this);
		if(file.exists()) {
			try {
				String password = "JKLjsljdflJHLDKFHjolhjohodshOHolfehwf_9h0h0h#@)dusfo";
				
				// The salt (probably) can be stored along with the encrypted data
				byte[] salt = new String("12345678").getBytes();

				// Decreasing this speeds up startup time and can be useful during testing, but it also makes it easier for brute force attackers
				int iterationCount = 40000;
				// Other values give me java.security.InvalidKeyException: Illegal key size or default parameters
				int keyLength = 128;
				SecretKeySpec key = createSecretKey(password.toCharArray(),salt, iterationCount, keyLength);
				BufferedReader input = new BufferedReader(new FileReader(file));
				String line = input.readLine();
				while(line != null) {
					String newLine = decrypt(line, key);
					text.append(newLine + "\n");
					line = input.readLine();
				}
				input.close();
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
			}
		}
		super.add(save);
		super.add(savec);
		jsp.setPreferredSize(new Dimension(400, 400));
		jsp.setViewportView(text);
		super.add(jsp);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			String password = "JKLjsljdflJHLDKFHjolhjohodshOHolfehwf_9h0h0h#@)dusfo";
			
			// The salt (probably) can be stored along with the encrypted data
			byte[] salt = new String("12345678").getBytes();

			// Decreasing this speeds down startup time and can be useful during testing, but it also makes it easier for brute force attackers
			int iterationCount = 40000;
			// Other values give me java.security.InvalidKeyException: Illegal key size or default parameters
			int keyLength = 128;
			SecretKeySpec key = createSecretKey(password.toCharArray(),salt, iterationCount, keyLength);
			FileWriter out = new FileWriter(file);
			String newText = encrypt(text.getText(), key);
			out.write(newText);
			out.close();
			if(arg0.getSource() == savec) {
				Login login = (Login) getParent();
				login.cl.show(login, "fb");
			}
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
	}

    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    private static String encrypt(String property, SecretKeySpec key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes("UTF-8"));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException, IOException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }
	
}
