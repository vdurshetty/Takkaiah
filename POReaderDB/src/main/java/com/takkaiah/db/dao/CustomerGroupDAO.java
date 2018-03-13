
package com.takkaiah.db.dao;

import java.util.List;
import java.util.Vector;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.KeyValueMaster;
import com.takkaiah.db.exception.AddException;
import com.takkaiah.db.exception.DeleteException;
import com.takkaiah.db.exception.FetchDataException;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.db.exception.RecordNotFoundException;
import com.takkaiah.db.util.HibernateUtil;
import com.takkaiah.logger.POReaderLogger;


public class CustomerGroupDAO {
	
	POReaderLogger log = POReaderLogger.getLogger(CustomerGroupDAO.class.getName());

	SessionFactory SF = null;
	
	public CustomerGroupDAO(){
		SF = HibernateUtil.getSessionFactory();
	}
	
	public boolean addCustomerGroup(CustomerGroup cg) throws AddException, PrimaryKeyException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
			session.save(cg);
	        trans.commit();
			status = true;
		}catch (Exception ex){
			trans.rollback();
			if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException){
				throw new PrimaryKeyException("Customer Group Name already exists");
			} 
			log.error("Unable to Add Customer Group Details :" + ex.getMessage());
			throw new AddException("Unable to add Customer Group details");
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	public boolean updateCustomerGroup(CustomerGroup cg) throws Exception{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
	        session.update(cg); 
	        trans.commit();
	        status = true;
		}catch (Exception ex){
			log.error("Unable to Update Customer Group Details :" + ex.getMessage());
			trans.rollback();
			throw new Exception(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	public boolean deleteCustomerGroup(CustomerGroup cg) throws DeleteException,RecordNotFoundException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
		      CustomerGroup oldCustGrp = getCustomerGroup(cg.getCgID());
				if (oldCustGrp==null) {
				   	throw new FetchDataException("Customer Group Not Found with customer group id :" + cg.getCgID());
				} 
				else {
					session = SF.getCurrentSession();
					trans = session.getTransaction();
			        trans.begin();
			        session.delete(cg); 
			        trans.commit();
			        status = true;
				}
		}catch (FetchDataException fde){
				throw new RecordNotFoundException(fde.getMessage());
		}catch (Exception ex){
			log.error("Unable to Delete Customer Group Details :" + ex.getMessage());
			trans.rollback();
			throw new DeleteException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}

	public CustomerGroup getCustomerGroup(int cgid) throws FetchDataException{
		CustomerGroup CustomerGroup = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
		    //emp = (Employee) session.get(Employee.class, empid); 
			CustomerGroup = (CustomerGroup) session.byId(CustomerGroup.class).load(cgid);	
		}catch (Exception ex){
			log.error("Unable to Fetch Customer Group Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return CustomerGroup;
	}

	public List<CustomerGroup> getCustomerGroupBy(CustomerGroup cg) throws FetchDataException{
		List<CustomerGroup> CustomerGroups = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(CustomerGroup.class);
			if (cg.getCgName() != null) {
			     ct.add( Restrictions.like("cgName", cg.getCgName() + "%" ));  // Filter Condition
			}
			ct.addOrder(Order.asc("cgName"));  // Order By condition
			CustomerGroups =  (List<CustomerGroup>) ct.list();  
		    
		}catch (Exception ex){
			log.error("Unable to Fetch Customer Group Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return CustomerGroups;
	}
	
	public List<CustomerGroup> getAllCustomerGroups() throws FetchDataException{
		List<CustomerGroup> customerGroups = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(CustomerGroup.class);
			ct.addOrder(Order.asc("cgName"));  // Order By condition
			customerGroups =  ct.list();  
			    
		}catch (Exception ex){
			log.error("Unable to Fetch Customer Group Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return customerGroups;
	}
	
	public Vector<KeyValueMaster> getCustomerGroupNames() throws FetchDataException{
		Vector<KeyValueMaster> customerGroupNames = new Vector<>();
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria cr = session.createCriteria(CustomerGroup.class)
				    .setProjection(Projections.projectionList()
				      .add(Projections.property("cgID"), "id")
				      .add(Projections.property("cgName"), "text"))
				      .addOrder(Order.asc("cgName"))
				      .setResultTransformer(Transformers.aliasToBean(KeyValueMaster.class));
			customerGroupNames.addAll(cr.list());
		}catch (Exception ex){
			log.error("Unable to Fetch Customer Group Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return customerGroupNames;
	}
	
}
