package com.takkaiah.db.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.takkaiah.db.dto.ItemCategory;
import com.takkaiah.db.exception.AddException;
import com.takkaiah.db.exception.DeleteException;
import com.takkaiah.db.exception.FetchDataException;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.db.exception.RecordNotFoundException;
import com.takkaiah.db.exception.UpdateException;
import com.takkaiah.db.util.HibernateUtil;
import com.takkaiah.logger.POReaderLogger;


public class ItemCategoryDAO {
	
	POReaderLogger log = POReaderLogger.getLogger(ItemCategoryDAO.class.getName());
	SessionFactory SF = null;
	
	public ItemCategoryDAO(){
		SF = HibernateUtil.getSessionFactory();
	}
	
	public boolean addItemCategory(ItemCategory ic) throws AddException, PrimaryKeyException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
			session.save(ic);
	        trans.commit();
			status = true;
		}catch (Exception ex){
			trans.rollback();
			if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException){
				throw new PrimaryKeyException("Item Category Name already exists");
			} 
			log.error("Unable to Add Item Category Details :" + ex.getMessage());
			throw new AddException("Unable to add Item Category details");
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	
	
	public boolean updateItemCategory(ItemCategory ic) throws UpdateException,RecordNotFoundException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
	        ItemCategory oldItemCat = getItemCategory(ic.getCatID());
			    if (oldItemCat==null) {
			    	throw new FetchDataException("Customer Not Found with customer id :" + ic.getCatID());
			    } 
			    else {
					session = SF.getCurrentSession();
					trans = session.getTransaction();
			        trans.begin();
			        session.update(ic); 
			        trans.commit();
			        status = true;
			    }
			}catch (FetchDataException fde){
				throw new RecordNotFoundException(fde.getMessage());
			}catch (Exception ex){
				log.error("Unable to Update Item Category Details :" + ex.getMessage());
				trans.rollback();
				throw new UpdateException(ex.getMessage());
			} 
			finally{
				session.close();
			}
		return status;
	}

	
	
	public boolean deleteItemCategory(ItemCategory ic) throws DeleteException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
	        session.delete(ic); 
	        trans.commit();
	        status = true;
			
		}catch (Exception ex){
			log.error("Unable to Delete Item Category Details :" + ex.getMessage());
			trans.rollback();
			throw new DeleteException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}

	public ItemCategory getItemCategory(int catid) throws FetchDataException{
		ItemCategory itemCat = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
		    //emp = (Employee) session.get(Employee.class, empid); 
			itemCat = (ItemCategory) session.byId(ItemCategory.class).load(catid);	
		}catch (Exception ex){
			log.error("Unable to Fetch Item Category Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return itemCat;
	}

	public List<ItemCategory> getEmployeeList(ItemCategory ic) throws FetchDataException{
		List<ItemCategory> itemCats = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(ItemCategory.class);
			if (ic.getCatName() != null) {
			     ct.add( Restrictions.like("catName", ic.getCatName() + "%" ));  // Filter Condition
			}
			ct.addOrder(Order.asc("catName"));  // Order By condition
			itemCats =  (List<ItemCategory>) ct.list();  
		    
		}catch (Exception ex){
			log.error("Unable to Fetch Item Category Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return itemCats;
	}
	
	public List<ItemCategory> getAllItemCategory() throws FetchDataException{
		List<ItemCategory> itemCats = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(ItemCategory.class);
			ct.addOrder(Order.asc("catName"));  // Order By condition
			itemCats =  ct.list();  
			    
		}catch (Exception ex){
			log.error("Unable to Fetch Item Category Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return itemCats;
	}
	
}
