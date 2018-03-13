
package com.takkaiah.db.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.CustomerItemMapping;
import com.takkaiah.db.exception.AddException;
import com.takkaiah.db.exception.DeleteException;
import com.takkaiah.db.exception.FetchDataException;
import com.takkaiah.db.util.HibernateUtil;
import com.takkaiah.logger.POReaderLogger;


public class CustomerItemMappingDAO {
	
	POReaderLogger log = POReaderLogger.getLogger(CustomerItemMappingDAO.class.getName());
	SessionFactory SF = null;
	
	public CustomerItemMappingDAO(){
		SF = HibernateUtil.getSessionFactory();
	}
	
	
	
	public boolean addUpdateCustomerItemMappings(List<CustomerItemMapping> custItemMapping) throws AddException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		
		try{
			session = SF.openSession();
			trans = session.getTransaction();
	        trans.begin();
			for (int i=0;i<custItemMapping.size();i++){
				session.saveOrUpdate(custItemMapping.get(i));	
			}
			trans.commit();
			status = true;
		}catch (Exception ex){
			log.error("Unable to Add/Update Customer Item Mapping Details :" + ex.getMessage());
			trans.rollback();
			throw new AddException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}
	
		
	
	public List<Object[]> getAllCustItemMappings(Customer cust) throws FetchDataException{
		List<Object[]> custItemMap = null;
		Session session = null;
		try{
			session = SF.openSession();
			session.beginTransaction();
			
			//String sql = "select a.cimid,a.itemid,a.itemname,a.eancode,b.articlecode,a.marginPercent from CustItemMapView a left join CGArticleCodes AS b on (b.cgid=a.custgroup and b.itemid=a.itemid ) where custid=" + cust.getCustID();
			String sql = "select cimid,itemid,itemname,eancode,articlecode,marginPercent from CustItemMapWithArticleCodeView where custid=" + cust.getCustID();
			
			NativeQuery<Object[]>	query = session.createSQLQuery(sql); 
			custItemMap = query.getResultList();  

			    
		}catch (Exception ex){
			log.error("Unable to Fetch Customer Item Mapping Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return custItemMap;
	}
	
	
	public boolean deleteCIM(CustomerItemMapping cim) throws DeleteException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
	        
	        session.delete(cim); 
	        trans.commit();
	        status = true;
		}catch (Exception ex){
			log.error("Unable to Delete Customer Item Mapping Details :" + ex.getMessage());
			trans.rollback();
			throw new DeleteException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	// For Reports
	public List<Object[]> getAllCustItemMapReport(Customer cust) throws FetchDataException{
		List<Object[]> custItemMap = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			String sql;
			
			if (cust.getCustMRP().getMrpTID()==2){
				sql = "select articlecode,eancode,itemname,marginpercent,BasicCostPrice,TaxPercent,TaxVal,NetLandingPrice,mrp,caseqty  from OutstationCustMapItemsView where custid=" + cust.getCustID();
			} else {
				sql = "select articlecode,eancode,itemname,marginpercent,BasicCostPrice,TaxPercent,TaxVal,NetLandingPrice,mrp,caseqty  from LocalCustMapItemsView where custid=" + cust.getCustID();
			}
			NativeQuery<Object[]>	query = session.createSQLQuery(sql); 
			custItemMap = query.getResultList();  

			    
		}catch (Exception ex){
			log.error("Unable to Fetch Customer Item Mapping Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return custItemMap;
	}
	
	
	
	public CustomerItemMapping getCustomerItemMapping(int custId, int itemID) throws FetchDataException{
		CustomerItemMapping cim = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			String hql = "FROM CustomerItemMapping CIM WHERE CIM.mapCustomer.custID=" + custId + " and CIM.custMapItem.itemID=" + itemID ;
			Query query = session.createQuery(hql);
			
			List<CustomerItemMapping> result = query.getResultList();
			if (result!=null) {
				if (result.size()>0)
					cim = (CustomerItemMapping) query.getResultList().get(0);
			}
		    
		}catch (Exception ex){
			log.error("Unable to Fetch MRPType Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return cim;
	}
}