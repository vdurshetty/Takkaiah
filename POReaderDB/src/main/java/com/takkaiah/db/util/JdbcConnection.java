package com.takkaiah.db.util;


import java.sql.Connection;
import java.sql.DriverManager;

import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderPassword;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.security.CipherTextInfo;

public class JdbcConnection {
	
	POReaderLogger log = POReaderLogger.getLogger(JdbcConnection.class.getName());
	public Connection getConnection() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			log.error("Unable to load Driver :" + e.getMessage());
			return null;
		}
		Connection connection = null;
		try {
			String url = POReaderReadEnv.getEnvValue(POReaderEnvProp.DBUrl);
			String uname = POReaderReadEnv.getEnvValue(POReaderEnvProp.DBUserName);
			String pwd = POReaderPassword.getPassword(POReaderEnvProp.DBPwd);
			pwd = CipherTextInfo.decrypt(pwd);
			connection = DriverManager.getConnection(url, uname,pwd);
		} catch (Exception e) {
			log.error("Connection Failed! Check output console :" + e.getMessage());
		}
		return connection;
	}

}