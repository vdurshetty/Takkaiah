
package com.takkaiah.db.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.takkaiah.db.dto.MRPType;
import com.takkaiah.db.exception.AddException;
import com.takkaiah.db.exception.DeleteException;
import com.takkaiah.db.exception.FetchDataException;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.db.exception.RecordNotFoundException;
import com.takkaiah.db.exception.UpdateException;
import com.takkaiah.db.util.HibernateUtil;
import com.takkaiah.logger.POReaderLogger;


public class MRPTypeDAO {
	
	POReaderLogger log = POReaderLogger.getLogger(MRPTypeDAO.class.getName());
	SessionFactory SF = null;
	
	public MRPTypeDAO(){
		SF = HibernateUtil.getSessionFactory();
	}
	
	public boolean addMRPType(MRPType pt) throws AddException, PrimaryKeyException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
			session.save(pt);
	        trans.commit();
			status = true;
		}catch (Exception ex){
			trans.rollback();
			if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException){
				throw new PrimaryKeyException("MRPType Name already exists");
			} 
			log.error("Unable to Add MRPType Details :" + ex.getMessage());
			throw new AddException("Unable to add MRPType details");
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	public boolean updateMRPType(MRPType pt) throws UpdateException,RecordNotFoundException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		
		try{
			MRPType oldMrpType = getMRPType(pt.getMrpTID());
		    if (oldMrpType==null) {
		    	throw new FetchDataException("MRP Type Not Found with MRPType id :" + pt.getMrpTID());
		    } 
		    else {
				session = SF.getCurrentSession();
				trans = session.getTransaction();
		        trans.begin();
		        session.update(pt); 
		        trans.commit();
		        status = true;
		    }
		}catch (FetchDataException fde){
			throw new RecordNotFoundException(fde.getMessage());
		}catch (Exception ex){
			log.error("Unable to Update MRPType Details :" + ex.getMessage());
			trans.rollback();
			throw new UpdateException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	public boolean deleteMRPType(MRPType pt) throws DeleteException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
	        session.delete(pt); 
	        trans.commit();
	        status = true;
		}catch (Exception ex){
			log.error("Unable to Delete MRPType Details :" + ex.getMessage());
			trans.rollback();
			throw new DeleteException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}

	public MRPType getMRPType(int ptid) throws FetchDataException{
		MRPType priceType = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
		    //emp = (Employee) session.get(Employee.class, empid); 
			priceType = (MRPType) session.byId(MRPType.class).load(ptid);	
		}catch (Exception ex){
			log.error("Unable to Fetch MRPType Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return priceType;
	}

	public List<MRPType> getMRPTypeBy(MRPType pt) throws FetchDataException{
		List<MRPType> priceTypes = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(MRPType.class);
			if (pt.getMrpTName() != null) {
			     ct.add( Restrictions.like("mrpTName", pt.getMrpTName() + "%" ));  // Filter Condition
			}
			ct.addOrder(Order.asc("mrpTName"));  // Order By condition
			priceTypes =  (List<MRPType>) ct.list();  
		    
		}catch (Exception ex){
			log.error("Unable to Fetch MRPType Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return priceTypes;
	}
	
	public List<MRPType> getAllMRPTypes() throws FetchDataException{
		List<MRPType> priceTypes = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(MRPType.class);
			ct.addOrder(Order.asc("mrpTName"));  // Order By condition
			priceTypes =  ct.list();  
			    
		}catch (Exception ex){
			log.error("Unable to Fetch MRPType Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return priceTypes;
	}
	
}
