
package com.takkaiah.db.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import com.takkaiah.db.dto.CustomerArticleCode;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.exception.FetchDataException;
import com.takkaiah.db.exception.UpdateException;
import com.takkaiah.db.util.HibernateUtil;
import com.takkaiah.logger.POReaderLogger;


public class CustomerArticleCodeDAO {
	
	POReaderLogger log = POReaderLogger.getLogger(CustomerArticleCodeDAO.class.getName());
	SessionFactory SF = null;
	public CustomerArticleCodeDAO(){
		SF = HibernateUtil.getSessionFactory();
	}
	
	public boolean addUpdateCustomerArticleCode(List<CustomerArticleCode> custArticleCodes) throws UpdateException {
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
			for (int i=0;i<custArticleCodes.size();i++){
				session.saveOrUpdate(custArticleCodes.get(i));	
			}
			trans.commit();
			status = true;
		}catch (Exception ex){
			log.error("Unable to save/update Customer Article Codes:" + ex.getMessage());
			trans.rollback();
			throw new UpdateException("Unable to save/update Customer Article Codes");
		} 
		finally{
			session.close();
		}
		return status;
	}
		
	public List<Object[]> getAllCustArticleCodes(CustomerGroup cg) throws FetchDataException {
		List<Object[]> custArticleCodes = null;
		Session session = null;
		try{
			String sql = "SELECT b.cgAID,a.itemID,a.itemName,a.eanCode,b.articleCode FROM ItemMaster AS a LEFT JOIN CGArticleCodes AS b ON b.itemId = a.itemID AND b.cgid=" + cg.getCgID();
			session = SF.openSession();
			session.beginTransaction();
			NativeQuery<Object[]>	query = session.createSQLQuery(sql); 
					
					//.setResultTransformer(Transformers.aliasToBean(KeyValueMaster.class));
					 
			custArticleCodes = query.getResultList();  
			
			
				//customerGroupNames.addAll(cr.list());
			
			
			    
		}catch (Exception ex){
			log.error("Unable to fetch Customer Article Codes :" + ex.getMessage());
			throw new FetchDataException("Unable to fetch Customer Article Codes");
		} 
		finally{
			session.close();
		}
		return custArticleCodes;
	}
	
	public CustomerArticleCode getCustomerArticleCode(CustomerArticleCode caCode) throws FetchDataException{
		
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			
			//String hql = "FROM CustomerArticleCode CA WHERE CA.custItemGroup.cgID=" + caCode.getCustItemGroup().getCgID() + " and CA.custItem.itemID=" + caCode.getCustItem().getItemID() ;
			String hql = "SELECT cgAID,itemID,itemName,eanCode,articleCode FROM CustArticleCodeView where cgid=" + caCode.getCustItemGroup().getCgID();
			Query query = session.createQuery(hql);
			
			List<CustomerArticleCode> result = query.getResultList();
			if (result!=null) {
				if (result.size()>0)
					caCode = (CustomerArticleCode) query.getResultList().get(0);
			}
			    
		}catch (Exception ex){
			log.error("Unable to Fetch Customer Article Code :" + ex.getMessage());
			throw new FetchDataException("Unable to fetch Customer Article Code");
		} 
		finally{
			session.close();
		}
		return caCode;
	}
	
}
