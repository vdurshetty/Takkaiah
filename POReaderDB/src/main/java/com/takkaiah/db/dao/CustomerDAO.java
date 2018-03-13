
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

import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.KeyValueMaster;
import com.takkaiah.db.exception.AddException;
import com.takkaiah.db.exception.DeleteException;
import com.takkaiah.db.exception.FetchDataException;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.db.exception.RecordNotFoundException;
import com.takkaiah.db.exception.UpdateException;
import com.takkaiah.db.util.HibernateUtil;
import com.takkaiah.logger.POReaderLogger;


public class CustomerDAO {
	
	POReaderLogger log = POReaderLogger.getLogger(CustomerDAO.class.getName());
	SessionFactory SF = null;
	
	public CustomerDAO(){
		SF = HibernateUtil.getSessionFactory();
	}
	
	public boolean addCustomer(Customer cust) throws AddException, PrimaryKeyException {
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
			session.save(cust);
	        trans.commit();
			status = true;
		}catch (Exception  ex){
			trans.rollback();
			if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException){
				throw new PrimaryKeyException("Customer Name already exists");
			} 
			log.error("Unable to Add Customer Details :" + ex.getMessage());
			throw new AddException("Unable to add Customer details");
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	public boolean updateCustomer(Customer cust ) throws UpdateException, RecordNotFoundException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
	        Customer oldCust = getCustomer(cust.getCustID());
		    if (oldCust==null) {
		    	throw new FetchDataException("Customer Not Found with customer id :" + cust.getCustID());
		    } 
		    else {
				session = SF.getCurrentSession();
				trans = session.getTransaction();
		        trans.begin();
		        session.update(cust); 
		        trans.commit();
		        status = true;
		    }
		}catch (FetchDataException fde){
			throw new RecordNotFoundException(fde.getMessage());
		}catch (Exception ex){
			trans.rollback();
			log.error("Customer Update Error :" + ex.getMessage());
			throw new UpdateException("Unable to update customer details");
		} 
		finally{
			if (session!=null)
				session.close();
		}
		return status;
	}
	
	public boolean deleteCustomer(Customer cust) throws DeleteException,RecordNotFoundException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
	        Customer oldCust = getCustomer(cust.getCustID());
			if (oldCust==null) {
			   	throw new FetchDataException("Customer Not Found with customer id :" + cust.getCustID());
			} 
			else {
				session = SF.getCurrentSession();
				trans = session.getTransaction();
		        trans.begin();
		        session.delete(cust); 
		        trans.commit();
		        status = true;
			}
		}catch (FetchDataException fde){
			throw new RecordNotFoundException(fde.getMessage());
		}catch (Exception ex){
			trans.rollback();
			log.error("Customer Delete Error :" + ex.getMessage());
			throw new DeleteException("Unable to delete customer details");
		} 
		finally{
			if (session!=null)
				session.close();
		}
		return status;
	}

	public Customer getCustomer(int cid) throws FetchDataException{
		Customer customer = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			customer = (Customer) session.byId(Customer.class).load(cid);	
		}catch (Exception ex){
			log.error("Error getting customer details " + ex.getStackTrace());
			throw new FetchDataException("Error getting customer details ");
		} 
		finally{
			session.close();
		}
		return customer;
	}

	public List<Customer> getCustomerBy(Customer cust) throws FetchDataException{
		List<Customer> customers = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(Customer.class);
			if (cust.getCustomerName() != null) {
			     ct.add( Restrictions.like("customerName", cust.getCustomerName() + "%" ));  // Filter Condition
			}
			ct.addOrder(Order.asc("customerName"));  // Order By condition
			customers =  (List<Customer>) ct.list();  
		    
		}catch (Exception ex){
			log.error("Error getting customer details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return customers;
	}
	
	public List<Customer> getAllCustomers() throws FetchDataException{
		List<Customer> customers = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(Customer.class);
			ct.addOrder(Order.asc("customerName"));  // Order By condition
			customers =  ct.list();  
		}catch (Exception ex){
			log.error("Error getting customer details :" + ex.getMessage());
			throw new FetchDataException("Unable to Fetch customer details!");
		} 
		finally{
			session.close();
		}
		return customers;
	}
	
	public Vector<KeyValueMaster> getCustomerNames(CustomerGroup cg) throws FetchDataException{
		Vector<KeyValueMaster> customerNames = new Vector<>();
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria cr = session.createCriteria(Customer.class)
				    .setProjection(Projections.projectionList()
				      .add(Projections.property("custID"), "id")
				      .add(Projections.property("customerName"), "text"))
				      .add(Restrictions.eq("custGroup.cgID",cg.getCgID()))
				      .addOrder(Order.asc("customerName"))
				      .setResultTransformer(Transformers.aliasToBean(KeyValueMaster.class));
			customerNames.addAll(cr.list());
		}catch (Exception ex){
			log.error("Error getting customer details " + ex.getMessage());
			throw new FetchDataException("Unable to fetch customer details");
		} 
		finally{
			session.close();
		}
		return customerNames;
	}
	
}
