/*Security Assignment 1
 *
 * Author: Christopher Jerrard-Dunne
 * Student No: C12449618
 *
 * Date Due: 16/03/2015
 *
 * This code tries to implement an RSA encryption and decryption program, with a friendly user interface
 * Requires Java 8 and greater to run.
 *
 * GUI Elements and code examples borrowed from: http://www.codeproject.com/Articles/33536/An-Introduction-to-Java-GUI-Programming
 * RSA Encryption elements borrowed from: https://javadigest.wordpress.com/2012/08/26/rsa-encryption-example/
 *
 */

//Imports Required
//java.math and java.util required for mathematical operations
import java.util.*;
import java.math.*;

//Java GUI imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//KeyPair and KeyPairGenerator required for Key Generation
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;

public class RSAEncrypt extends JFrame implements ActionListener{

	private static final String PUBLIC_KEY_FILE = "Public.key";
	private static final String PRIVATE_KEY_FILE = "Private.key";
	private static final String PLAINTEXT_FILE = "Plaintext.txt";
	private static final String ENCRYPTED_FILE = "Encrypted.txt";
	public static final String ALGORITHM = "RSA";
	
	
	KeyPair key;
	JPanel numberPanel, encryptPanel, decryptPanel, buttonPanel;
	JFrame warning;
	JLabel numberLabel, encryptLabel, decryptLabel;
	JButton genButton, encButton, decButton, encFileButton, decFileButton, findKeys;
	JTextField numberField, encryptField, decryptField;
	
	
	
	public static void main(String[] args){
		//Set up all needed parameters here
		
		//Run the program and show the GUI.
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				CreateAndShowGUI();
			}
		});
	
	}
	
	
	public void GenerateKey(){
		//Generates a KeyPair value for use by the RSA Algorithm
		
		try{
			//Generates a key pair
			final KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(ALGORITHM);
			keyGenerator.initialize(512);
			key = keyGenerator.generateKeyPair();
			
			//Write the keys to .key files
			File privateKeyFile = new File(PRIVATE_KEY_FILE);
			File publicKeyFile = new File(PUBLIC_KEY_FILE);
			
			privateKeyFile.createNewFile();
			publicKeyFile.createNewFile();
			
			ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
			publicKeyOS.writeObject(key.getPublic());
			publicKeyOS.close();
			
			ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
			privateKeyOS.writeObject(key.getPrivate());
			privateKeyOS.close();
			
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(warning, "Uh oh, a key pair could not be generated!");
			e.printStackTrace();
		}
	}
	
	
	public void GetKeyPairFromFile(){
		//Find they keys written in file, converts them to a Java-friendly format, and allocates the keys from there.
		
		try{
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
			final PublicKey publicKey = (PublicKey) inputStream.readObject();
			
			inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
			final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
			inputStream.close();
			
			key = new KeyPair (publicKey, privateKey);
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(warning, "Uh oh, both key pairs were not found!");
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	//Code to encrypt and decrypt strings of data.
	public String encrypt(String text){
		//Encrypts the given text string using the publickey as a key
		byte[] cipherText = null;
		java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
		
		try{
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
			cipherText = cipher.doFinal(text.getBytes());
		}
		catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(warning, "Uh oh, something went wrong while Encrypting!");
		}
		
		return encoder.encodeToString(cipherText);
	
	}
	

	public String decrypt(byte[] text){
		//decrypts the text using the privatekey as a key
		byte[] decryptedText = null;
		
		try{
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
			decryptedText = cipher.doFinal(text);
		}
		catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(warning, "Uh oh, something went wrong while Decrypting!");
			
		}
		
		return new String(decryptedText);
	}

	
	
	public JPanel createGUI(){
	
		//Create a window for the GUI to sit on
		JPanel fullGUI = new JPanel();
		fullGUI.setLayout(null);
		
		//Create a JFrame for messages and warnings
		warning = new JFrame();
		
		//Creates a panel to generate and show a large number
		numberPanel = new JPanel();
		numberPanel.setLayout(null);
		numberPanel.setLocation(10, 0);
		numberPanel.setSize(250, 30);
		fullGUI.add(numberPanel);
		
		numberLabel = new JLabel("Public Key: ");
		numberLabel.setLocation(0,0);
		numberLabel.setSize(100, 30);
		numberLabel.setHorizontalAlignment(0);
		numberPanel.add(numberLabel);
		
		numberField = new JTextField(64);
		numberField.setLocation(120, 10);
		numberField.setSize(120, 20);
		numberField.setHorizontalAlignment(0);
		numberPanel.add(numberField);
		
		
		
		//Creates a panel to encrypt your number (public key) generated
		encryptPanel = new JPanel();
		encryptPanel.setLayout(null);
		encryptPanel.setLocation(10, 50);
		encryptPanel.setSize(250, 30);
		fullGUI.add(encryptPanel);
		
		encryptLabel = new JLabel("Encrypted: ");
		encryptLabel.setLocation(0,0);
		encryptLabel.setSize(100, 30);
		encryptLabel.setHorizontalAlignment(0);
		encryptPanel.add(encryptLabel);
		
		encryptField = new JTextField(512);
		encryptField.setLocation(120,10);
		encryptField.setSize(120, 20);
		encryptField.setHorizontalAlignment(0);
		encryptPanel.add(encryptField);
		
		
		
		
		//Creates a panel to decrypt the code generated
		decryptPanel = new JPanel();
		decryptPanel.setLayout(null);
		decryptPanel.setLocation(10, 100);
		decryptPanel.setSize(250, 30);
		fullGUI.add(decryptPanel);
		
		decryptLabel = new JLabel("Decrypted: ");
		decryptLabel.setLocation(0,0);
		decryptLabel.setSize(100, 30);
		decryptLabel.setHorizontalAlignment(0);
		decryptPanel.add(decryptLabel);
		
		decryptField = new JTextField(512);
		decryptField.setLocation(120,10);
		decryptField.setSize(120, 20);
		decryptField.setHorizontalAlignment(0);
		decryptPanel.add(decryptField);
		
		
		
		
		
		//Creates a panel to house all buttons
		buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setLocation(10, 150);
		buttonPanel.setSize(250, 180);
		fullGUI.add(buttonPanel);
		
		//Add the buttons to the panel
		genButton = new JButton("Generate Key");
		genButton.setLocation(0,0);
		genButton.setSize(250, 30);
		genButton.addActionListener(this);
		buttonPanel.add(genButton);
		
		findKeys = new JButton("Find Keys");
		findKeys.setLocation(0,30);
		findKeys.setSize(250,30);
		findKeys.addActionListener(this);
		buttonPanel.add(findKeys);
		
		encButton = new JButton("Encrypt String");
		encButton.setLocation(0,60);
		encButton.setSize(250, 30);
		encButton.addActionListener(this);
		buttonPanel.add(encButton);
		
		decButton = new JButton("Decrypt String");
		decButton.setLocation(0,90);
		decButton.setSize(250, 30);
		decButton.addActionListener(this);
		buttonPanel.add(decButton);
		
		encFileButton = new JButton("Encrypt File");
		encFileButton.setLocation(0,120);
		encFileButton.setSize(250, 30);
		encFileButton.addActionListener(this);
		buttonPanel.add(encFileButton);
		
		decFileButton = new JButton("Decrypt File");
		decFileButton.setLocation(0,150);
		decFileButton.setSize(250, 30);
		decFileButton.addActionListener(this);
		buttonPanel.add(decFileButton);
		
		fullGUI.setOpaque(true);
		return fullGUI;
	}
	
	private static void CreateAndShowGUI(){
		//Method to render the full GUI as part of a thread
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("RSA Encryption Demonstration");
		
		//Create the content pane
		RSAEncrypt demo = new RSAEncrypt();
		frame.setContentPane(demo.createGUI());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(280, 380);
		frame.setVisible(true);
	}
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e){
		String text;
		String result;
		java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
		
		//Action Handler to handle all method calls from button presses
		if(e.getSource() == genButton){
			//Generates a new public key and shows it on the public key text box
			GenerateKey();
			numberField.setText(key.getPublic().toString());
		}
		else if (e.getSource() == findKeys){
			GetKeyPairFromFile();
			numberField.setText(key.getPublic().toString());
		}
		else if (e.getSource() == encButton){
			//Take a string, encrypt it, and give back the results
			text = decryptField.getText();
			result = encrypt(text);
			encryptField.setText(result);
		}
		else if (e.getSource() == decButton){
			//Take a string, return it to byte form, decrypt it, and pass it back as a string
			
			text = encryptField.getText();
			result = decrypt(decoder.decode(text));
			decryptField.setText(result);
		}
		else if (e.getSource() == encFileButton){
			//Take a plaintext file and encrypt the contents. Return it as encrypted.txt
			
			try{
				FileReader plainFile = new FileReader(PLAINTEXT_FILE);
				FileWriter encryptFile = new FileWriter(ENCRYPTED_FILE, false);
				
				//Read plain data then encrypt it, taking just the first line of data
				BufferedReader bufferedReader = new BufferedReader(plainFile);
				text = bufferedReader.readLine();
				bufferedReader.close();
				
				result = encrypt(text);
				
				BufferedWriter bufferedWriter = new BufferedWriter(encryptFile);
				bufferedWriter.write(result);
				bufferedWriter.close();
				
				JOptionPane.showMessageDialog(warning, "Encryption successful!");
			}
			catch(Exception ex){
				ex.printStackTrace();
				JOptionPane.showMessageDialog(warning, "Uh oh, something went wrong! Check to see if you have \"Plaintext.txt\" in this folder.");
			}
			
		}
		else if (e.getSource() == decFileButton){
			//Take an encrypted file and decrypt the contents. Return it as plaintext.txt
			
			try{
				FileWriter plainFile = new FileWriter(PLAINTEXT_FILE, false);
				FileReader encryptFile = new FileReader(ENCRYPTED_FILE);
				
				//Read encrypted data then decrypt it, taking just the first line
				BufferedReader bufferedReader = new BufferedReader(encryptFile);
				text = bufferedReader.readLine();
				bufferedReader.close();
				
				result = decrypt(decoder.decode(text));
				
				BufferedWriter bufferedWriter = new BufferedWriter(plainFile);
				bufferedWriter.write(result);
				bufferedWriter.close();
				
				JOptionPane.showMessageDialog(warning, "Decryption successful!");
			}
			catch(Exception ex){
				ex.printStackTrace();
				JOptionPane.showMessageDialog(warning, "Uh oh, something went wrong! Check to see if you have \"Encrypted.txt\" in this folder.");
			}
		}
		
	}
	
}