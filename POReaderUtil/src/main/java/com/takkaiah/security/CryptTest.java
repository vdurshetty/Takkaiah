package com.takkaiah.security;
	import java.io.File;
	 
	/**
	 * A tester for the CryptoUtils class.
	 * @author www.codejava.net
	 *
	 */
	public class CryptTest {
	    public static void main(String[] args) {
	        String key = "Mary has one cat1";
	        
	        File inputFile = new File("passwd.txt");
	        File encryptedFile = new File("document.encrypted");
	        File decryptedFile = new File("document.decrypted");
	         
	        try {
	            CryptoUtils.encrypt(key, inputFile, encryptedFile);
	            CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
	        } catch (CryptoException ex) {
	            System.out.println(ex.getMessage());
	            ex.printStackTrace();
	        }
	    }
	}