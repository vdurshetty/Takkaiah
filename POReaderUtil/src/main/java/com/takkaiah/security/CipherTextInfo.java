package com.takkaiah.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.takkaiah.logger.POReaderLogger;


public class CipherTextInfo {
   
		/*private static byte[] key = {
            0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
        };//"thisIsASecretKey";
    	*/
		
		//private static LCMSLogger log = LCMSLogger.getLogger(CipherTextInfo.class.getName());
	
		private static POReaderLogger log = POReaderLogger.getLogger(CipherTextInfo.class.getName());
		
		private static byte[] key = {
	            0x11, 0x48, 0x69, 0x73, 0x49, 0x7a, 0x41, 0x51, 0x65, 0x63, 0x7b, 0x65, 0x74, 0x42, 0x62, 0x69
	    };//"thisIsASecretKey";
		
		//static  byte[]  key = "!@#$!@#$%^&**&^%".getBytes();
		final static String algorithm="AES";

		public static void main(String a[]) throws Exception{
			String key="dvVRhbMqOioiilM+H+WJDw==";
			if (a.length>0){
				key = a[0];
			}
			log.error("Encrypt Code is :" + key );
			log.error("Decryption Code is :" + decrypt(key) );
		}
		
		public static String encrypt(String data){

		   byte[] dataToSend = data.getBytes();
		   Cipher c = null;
		   try {
		       c = Cipher.getInstance(algorithm);
		   } catch (NoSuchAlgorithmException e) {
		       // TODO Auto-generated catch block
		   	log.error("Encryption Error:" + e);
		   } catch (NoSuchPaddingException e) {
		       // TODO Auto-generated catch block
		   	log.error("Encryption Error:" + e);
		   }
		   SecretKeySpec k =  new SecretKeySpec(key, algorithm);
		   try {
		       c.init(Cipher.ENCRYPT_MODE, k);
		   } catch (InvalidKeyException e) {
		       // TODO Auto-generated catch block
		   	log.error("Encryption Error:" + e);
		   }
		   byte[] encryptedData = "".getBytes();
		   try {
		       encryptedData = c.doFinal(dataToSend);
		   } catch (IllegalBlockSizeException e) {
		       // TODO Auto-generated catch block
		   	log.error("Encryption Error:" + e);
		   } catch (BadPaddingException e) {
		       // TODO Auto-generated catch block
		   	log.error("Encryption Error:" + e);
		   }
		   byte[] encryptedByteValue =    new Base64().encode(encryptedData);
		   return  new String(encryptedByteValue);//.toString();
		}

		public static String decrypt(String data){

		   byte[] encryptedData  = new Base64().decode(data);
		   Cipher c = null;
		   try {
		       c = Cipher.getInstance(algorithm);
		   } catch (NoSuchAlgorithmException e) {
		       // TODO Auto-generated catch block
		       e.printStackTrace();
		   } catch (NoSuchPaddingException e) {
		       // TODO Auto-generated catch block
		       e.printStackTrace();
		   }
		   SecretKeySpec k =
		           new SecretKeySpec(key, algorithm);
		   try {
		       c.init(Cipher.DECRYPT_MODE, k);
		   } catch (InvalidKeyException e1) {
		       // TODO Auto-generated catch block
		       e1.printStackTrace();
		   }
		   byte[] decrypted = null;
		   try {
		       decrypted = c.doFinal(encryptedData);
		   } catch (IllegalBlockSizeException e) {
		       // TODO Auto-generated catch block
		       e.printStackTrace();
		   } catch (BadPaddingException e) {
		       // TODO Auto-generated catch block
		       e.printStackTrace();
		   }
		   return new String(decrypted);
		}
        
      }