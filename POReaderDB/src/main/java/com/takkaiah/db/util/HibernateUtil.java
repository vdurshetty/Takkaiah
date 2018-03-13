package com.takkaiah.db.util;

	
import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderPassword;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.security.CipherTextInfo;
	 
	public class HibernateUtil {
	 
		private static POReaderLogger log = POReaderLogger.getLogger(HibernateUtil.class.getName());
	    private static  SessionFactory sessionFactory ;
	    
	    // Private constructor 
	    private HibernateUtil(){
	    	
	    }
	    
	 
	    // private method to build the session factory based on the given hibernate config file
	    private static void buildSessionFactory () {
	        try {
		    	final String hibernateConfFile = POReaderReadEnv.getEnvValue(POReaderEnvProp.HibernateConfFile);
		    	File hfile = new File(hibernateConfFile);
     	        Configuration configuration = new Configuration();
	            configuration.configure(hfile);
	            
	            /*configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres");                                
	            configuration.setProperty("hibernate.connection.username", "postgres");     
	            configuration.setProperty("hibernate.connection.password", "pgsql");
	            */
	            
	            configuration.setProperty("hibernate.connection.url", POReaderReadEnv.getEnvValue(POReaderEnvProp.DBUrl));                                
	            configuration.setProperty("hibernate.connection.username", POReaderReadEnv.getEnvValue(POReaderEnvProp.DBUserName));
	            String dbpwd = POReaderPassword.getPassword(POReaderEnvProp.DBPwd);
	            configuration.setProperty("hibernate.connection.password",  CipherTextInfo.decrypt(dbpwd));
	            
	            /*
	            configuration.setProperty("connection.driver_class","org.postgresql.Driver");
				configuration.setProperty("dialect", "org.hibernate.dialect.PostgreSQLDialect");
	            configuration.setProperty("current_session_context_class", "thread");
	            configuration.setProperty("show_sql", "true");
	            configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");
	            configuration.setProperty("hibernate.connection.pool_size", "10");
	            configuration.setProperty("hibernate.bytecode.use_reflection_optimizer", "false");
	        	*/
	            
	        	configuration.addAnnotatedClass (com.takkaiah.db.dto.ItemCategory.class);
	        	configuration.addAnnotatedClass (com.takkaiah.db.dto.MRPType.class);
	        	configuration.addAnnotatedClass (com.takkaiah.db.dto.CustomerGroup.class);
	        	configuration.addAnnotatedClass (com.takkaiah.db.dto.Customer.class);
	        	configuration.addAnnotatedClass (com.takkaiah.db.dto.Item.class);
	        	configuration.addAnnotatedClass (com.takkaiah.db.dto.ItemMRP.class);
	        	configuration.addAnnotatedClass (com.takkaiah.db.dto.CustomerArticleCode.class);
	        	configuration.addAnnotatedClass (com.takkaiah.db.dto.CustomerItemMapping.class);
	        	configuration.addAnnotatedClass (com.takkaiah.db.dto.User.class);
	        	configuration.addAnnotatedClass (com.takkaiah.db.dto.PORAFunctionalities.class);
	        	configuration.addAnnotatedClass (com.takkaiah.db.dto.UserPermissions.class);
	        	
	           
			     /*configuration.configure(hibernateConfFile);
			     configuration.setProperty("hibernate.connection.password","pgsql");*/
			     
			     StandardServiceRegistry stdServiceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			     //sessionFactory = new MetadataSources(stdServiceRegistry).buildMetadata().buildSessionFactory();
			     sessionFactory = configuration.buildSessionFactory(stdServiceRegistry);
	        }
	        catch (Exception ex) {
	            // Make sure you log the exception, as it might be swallowed
	        	log.error("Initial SessionFactory creation failed." + ex);
	        	ex.printStackTrace();
	            throw new ExceptionInInitializerError(ex);
	        }
	    }
	 
	    	   	  	    
	    public static SessionFactory getSessionFactory() {
	    	if (sessionFactory == null ) {
	    		try {
	    		buildSessionFactory();
	    		}catch(ExceptionInInitializerError er) {
	    			System.out.println("in getSession Factory");
	    			throw er;
	    		}
	    	} 		
	    			
	        return sessionFactory;
	    }
	 
	    public static void shutdown() {
	    	// Close caches and connection pools
	    	sessionFactory.close();
	    }
	 
	}
