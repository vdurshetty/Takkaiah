package com.takkaiah.env;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import com.takkaiah.logger.POReaderLogger;

public class POReaderPassword {
	
	static POReaderLogger log = POReaderLogger.getLogger(POReaderPassword.class.getName());
	
	public static Properties getAllPasswords() {
		Properties prop = new Properties();
        try
        {
			String pwdFileName = POReaderReadEnv.getEnvValue(POReaderEnvProp.PasswordFile);
			File pwdFile = new File(pwdFileName);
			if (!pwdFile.exists()){
				log.debug("Password File '" + pwdFile.getAbsolutePath() + "' not found!");
			}
            prop.load(new FileInputStream(pwdFile));
            if (!prop.containsKey(POReaderEnvProp.DBPwd)){
            	prop.setProperty(POReaderEnvProp.DBPwd , "");
            } 
            if (!prop.containsKey(POReaderEnvProp.EmailPwd)){
            	prop.setProperty(POReaderEnvProp.EmailPwd , "");
            } 
            if (!prop.containsKey(POReaderEnvProp.SmsPwd)){
            	prop.setProperty(POReaderEnvProp.SmsPwd , "");
            } 
        }
        catch(IOException ex)
        {
        	log.error("Unable to fetch passwords from password file" + ex.getMessage());
        }
		return prop;
	}
	
	public static String getPassword(String pwdType) {
		String pwd = null;
		Properties prop = new Properties();
        try
        {
			String pwdFileName = POReaderReadEnv.getEnvValue(POReaderEnvProp.PasswordFile);
			File pwdFile = new File(pwdFileName);
			if (!pwdFile.exists()){
				log.debug("Password File '" + pwdFile.getAbsolutePath() + "' not found!");
				return null;
			}
            prop.load(new FileInputStream(pwdFile));
            if (prop.containsKey(pwdType)){
            	pwd = prop.getProperty(pwdType);
            } 
        }
        catch(IOException ex)
        {
        	log.error("Unable to fetch password from password file" + ex.getMessage());
        }
		return pwd;
	}
	
	public static boolean savePasswords(Properties passwords) {
		boolean status = false;
		try{
			String pwdFileName = POReaderReadEnv.getEnvValue(POReaderEnvProp.PasswordFile);
			File pwdFile = new File(pwdFileName);
			if (!pwdFile.exists()){
				pwdFile.createNewFile();
			}
			
			String pwdLines="";
			if (passwords.containsKey(POReaderEnvProp.DBPwd)) {
				pwdLines = POReaderEnvProp.DBPwd + "=" + passwords.get(POReaderEnvProp.DBPwd) + "\n";
			} else {
				pwdLines = POReaderEnvProp.DBPwd + "="  + "\n";
			}
			if (passwords.containsKey(POReaderEnvProp.EmailPwd)) {
				pwdLines = pwdLines + POReaderEnvProp.EmailPwd + "=" + passwords.get(POReaderEnvProp.EmailPwd) + "\n";
			} else {
				pwdLines = pwdLines + POReaderEnvProp.EmailPwd + "=" + "\n";
			}
			if (passwords.containsKey(POReaderEnvProp.SmsPwd)) {
				pwdLines = pwdLines + POReaderEnvProp.SmsPwd + "=" + passwords.get(POReaderEnvProp.SmsPwd) + "\n";
			} else {
				pwdLines = pwdLines + POReaderEnvProp.SmsPwd + "=" + "\n";
			}
			writeToFile(pwdFileName,pwdLines);
		}catch(Exception e){
        	log.error("Unable to save passwords into password file" + e.getMessage());
		}
		return status;
	}
	
	private static boolean writeToFile(String fileName,String pwdLines) throws Exception{
		boolean status=false;
		FileWriter fw = new FileWriter(fileName,false);
		fw.write(pwdLines);
        fw.close();
		return status;
	}
	

}
