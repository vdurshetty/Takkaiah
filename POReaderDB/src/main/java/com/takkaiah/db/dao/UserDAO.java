
package com.takkaiah.db.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import com.takkaiah.db.dto.KeyValueMaster;
import com.takkaiah.db.dto.PORAFunctionalities;
import com.takkaiah.db.dto.User;
import com.takkaiah.db.dto.UserPermissions;
import com.takkaiah.db.exception.AddException;
import com.takkaiah.db.exception.DeleteException;
import com.takkaiah.db.exception.FetchDataException;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.db.exception.RecordNotFoundException;
import com.takkaiah.db.exception.UpdateException;
import com.takkaiah.db.util.HibernateUtil;
import com.takkaiah.logger.POReaderLogger;


public class UserDAO {
	
	POReaderLogger log = POReaderLogger.getLogger(UserDAO.class.getName());

	SessionFactory SF = null;
	
	public UserDAO(){
		SF = HibernateUtil.getSessionFactory();
	}
	
	public boolean addUser(User user) throws AddException, PrimaryKeyException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
			session.save(user);
	        trans.commit();
			status = true;
		}catch (Exception ex){
			trans.rollback();
			if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException){
				throw new PrimaryKeyException("User Name already exists");
			} 
			log.error("Unable to Add User Details :" + ex.getMessage());
			throw new AddException("Unable to add User details");
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	public boolean updateUser(User user ) throws UpdateException,RecordNotFoundException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		
	    try{
			  User oldUser= getUser(user.getUid());
			  if (oldUser==null) {
			    	throw new FetchDataException("User Not Found with User id :" + user.getUid());
			  } 
			  else {
					session = SF.getCurrentSession();
					trans = session.getTransaction();
			        trans.begin();
			        session.update(user); 
			        trans.commit();
			        status = true;
			   }
			}catch (FetchDataException fde){
				throw new RecordNotFoundException(fde.getMessage());
		}catch (Exception ex){
			trans.rollback();
			log.error("Unable to Update User Details :" + ex.getMessage());
			throw new UpdateException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	public boolean deleteUser(User user) throws DeleteException,RecordNotFoundException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
			  User oldUser= getUser(user.getUid());
			  if (oldUser==null) {
			    	throw new FetchDataException("User Not Found with User id :" + user.getUid());
			  } 
			  else {
					session = SF.getCurrentSession();
					trans = session.getTransaction();
			        trans.begin();
			        session.delete(user); 
			        trans.commit();
			        status = true;
			   }
		}catch (FetchDataException fde){
				throw new RecordNotFoundException(fde.getMessage());
		}catch (Exception ex){
			trans.rollback();
			log.error("Unable to Delete User Details :" + ex.getMessage());
			throw new DeleteException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}

	public User getUser(int uid) throws FetchDataException{
		User user = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			user = (User) session.byId(User.class).load(uid);	
		}catch (Exception ex){
			log.error("Unable to Fetch User Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return user;
	}


	public User validateUser(User user) throws FetchDataException{
		Session session = null;
		User validUser=null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(User.class);
			if (user != null) {
			     ct.add( Restrictions.eq("userName", user.getUserName()).ignoreCase() );  // Filter Condition
			     ct.add( Restrictions.eq("pwd", user.getPwd()));  // Filter Condition
			}
			//ct.addOrder(Order.asc("itemID"));  // Order By condition
			List<User> users = ct.list();
			if (users!=null){
				if (users.size()>0){
					validUser =   (User) ct.list().get(0);
				}
			}
		}catch (Exception ex){
			log.error("Unable to Fetch User Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return validUser;
	}
	
	public List<User> getAllUsers() throws FetchDataException{
		List<User> users = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(User.class);
			ct.addOrder(Order.asc("userName"));  // Order By condition
			users =  ct.list();  
			    
		}catch (Exception ex){
			log.error("Unable to Fetch User Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return users;
	}
	
	public Vector<PORAFunctionalities> getAllFunctionalities1() throws FetchDataException{
		Vector<PORAFunctionalities> funsList =new Vector<>();
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(PORAFunctionalities.class);
			ct.addOrder(Order.asc("fName"));  // Order By condition
			if (ct.list()!=null) {
				funsList.addAll(ct.list());
			}
			    
		}catch (Exception ex){
			log.error("Unable to Fetch User Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return funsList;
	}
	
	public Vector<KeyValueMaster> getAllFunctionalities() throws FetchDataException{
		Vector<KeyValueMaster> functionalities = new Vector<>();
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria cr = session.createCriteria(PORAFunctionalities.class)
				    .setProjection(Projections.projectionList()
				      .add(Projections.property("fID"), "id")
				      .add(Projections.property("fName"), "text"))
				      .addOrder(Order.asc("fName"))
				      .setResultTransformer(Transformers.aliasToBean(KeyValueMaster.class));
			functionalities.addAll(cr.list());
		}catch (Exception ex){
			log.error("Unable to Fetch User Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return functionalities;
	}
	
	
	public boolean addUserPermissions(User user) throws AddException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
	    	int uid = user.getUid();
			String sql = "delete from UserPermissions where uid=" + uid ;
			
			NativeQuery<Object[]>	query = session.createSQLQuery(sql); 
			query.executeUpdate();  
			List<UserPermissions> userPermissions = user.getUserPermissions();
			for (int i=0;i<userPermissions.size();i++){
				PORAFunctionalities pora = userPermissions.get(i).getpFunc();
				String upSql = "insert into UserPermissions(uid,fid) values ('" + uid  + "','" + pora.getfID()  + "')";
				query = session.createSQLQuery(upSql); 
				query.executeUpdate(); 
			}
	        trans.commit();
			status = true;
		}catch (Exception ex){
			trans.rollback();
			log.error("Unable to Add User Permissions :" + ex.getMessage());
			throw new AddException("Unable to add User Permissions");
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	public List<Object[]> getUserPermissions(int uid) throws FetchDataException{
		List<Object[]> userPermissions = new ArrayList<>();
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			String sql = "select uid,fid from UserPermissions where uid=" + uid;
			NativeQuery<Object[]>	query = session.createSQLQuery(sql); 
			userPermissions = query.getResultList();  
		}catch (Exception ex){
			log.error("Unable to Fetch User Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return userPermissions;
	}
	
}
