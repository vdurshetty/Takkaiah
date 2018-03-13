
package com.takkaiah.db.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.NativeQuery;

import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.Item;
import com.takkaiah.db.dto.ItemMRP;
import com.takkaiah.db.dto.ItemWithCustomerMaringPercent;
import com.takkaiah.db.exception.AddException;
import com.takkaiah.db.exception.DeleteException;
import com.takkaiah.db.exception.FetchDataException;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.db.exception.RecordNotFoundException;
import com.takkaiah.db.exception.UpdateException;
import com.takkaiah.db.util.HibernateUtil;
import com.takkaiah.logger.POReaderLogger;


public class ItemDAO {
	
	POReaderLogger log = POReaderLogger.getLogger(ItemDAO.class.getName());
	SessionFactory SF = null;
	
	public ItemDAO(){
		SF = HibernateUtil.getSessionFactory();
	}
	
	public boolean addItem(Item item) throws AddException, PrimaryKeyException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
			session.save(item);
			List<ItemMRP> itemPrices = item.getItemPrice(); 
			for (int i=0;i<itemPrices.size();i++){
				session.save(itemPrices.get(i));
			}
	        trans.commit();
			status = true;
		}catch (Exception ex){
			trans.rollback();
			if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException){
				ConstraintViolationException cve = (ConstraintViolationException) ex.getCause();
				if (cve.getConstraintName().contains("eancode")) {
					throw new PrimaryKeyException("EAN Code Already Exists");
				} else {
					throw new PrimaryKeyException("Item Name already exists");
				}
			} 
			log.error("Unable to Add Item Details :" + ex.getMessage());
			throw new AddException("Unable to add Item details");
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	
	public boolean updateItem(Item item ) throws UpdateException,RecordNotFoundException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		
		try{
			 Item oldItem = getItem(item.getItemID());
			    if (oldItem==null) {
			    	throw new FetchDataException("Item Not Found with Item id :" + item.getItemID());
			    } 
			    else {
					session = SF.getCurrentSession();
					trans = session.getTransaction();
			        trans.begin();
			        session.update(item); 
			    	List<ItemMRP> itemPrices = item.getItemPrice(); 
					for (int i=0;i<itemPrices.size();i++){
						session.saveOrUpdate(itemPrices.get(i));
					}
			        trans.commit();
			        status = true;
			 }
		}catch (FetchDataException fde){
				throw new RecordNotFoundException(fde.getMessage());
		}catch (Exception ex){
			log.error("Unable to Update Item Details :" + ex.getMessage());
			trans.rollback();
			throw new UpdateException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}

	
	
	public boolean deleteItem(Item item) throws DeleteException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
	        trans.begin();
	        item = (Item) session.get(Item.class,item.getItemID() ); 
		    if (item==null) {
		    	trans.rollback();
		    } 
		    else {
		        session.delete(item); 
		        trans.commit();
		        status = true;
		    }
			
		}catch (Exception ex){
			log.error("Unable to Delete Item Details :" + ex.getMessage());
			trans.rollback();
			throw new DeleteException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}
	
	
	public boolean deleteItemMRP(ItemMRP itemMRP) throws DeleteException{
		boolean status = false;
		Session session = null;
		Transaction trans = null;
		try{
			session = SF.getCurrentSession();
			trans = session.getTransaction();
			trans.begin();
			itemMRP = (ItemMRP) session.get(ItemMRP.class,itemMRP.getiMRPID()); 
			if (itemMRP!=null){
				session.delete(itemMRP); 
				trans.commit();
				status = true;
			} else {
				trans.rollback();
			}
		}catch (Exception ex){
			log.error("Unable to Delete Item MRP Details :" + ex.getMessage());
			trans.rollback();
			throw new DeleteException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return status;
	}

	public Item getItem(int itemId) throws FetchDataException{
		Item item = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			item = (Item) session.byId(Item.class).load(itemId);	
		}catch (Exception ex){
			log.error("Unable to Fetch Item Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return item;
	}
	
	public Item getItemByEAN(String eanCode) throws FetchDataException{
		Item item = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(Item.class);
			if (eanCode != null) {
			     ct.add( Restrictions.eq("eanCode", eanCode));  // Filter Condition
			}
			//ct.addOrder(Order.asc("itemID"));  // Order By condition
			List<Item> items = ct.list();
			if (items!=null){
				if (items.size()>0){
					item =   (Item) ct.list().get(0);
				}
			}
		}catch (Exception ex){
			log.error("Unable to Fetch Item Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return item;
	}

	public List<Item> getItemBy(Item item) throws FetchDataException{
		List<Item> items = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			
			
			
			Criteria ct = session.createCriteria(Item.class);
			if (item.getItemName() != null) {
			     ct.add( Restrictions.like("itemName", item.getItemName() + "%" ));  // Filter Condition
			}
			ct.addOrder(Order.asc("itemID"));  // Order By condition
			items =  (List<Item>) ct.list();  
		    
		}catch (Exception ex){
			log.error("Unable to Fetch Item Details :" + ex.getMessage());
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return items;
	}
	
	public List<Item> getAllitems() throws FetchDataException{
		List<Item> items = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			Criteria ct = session.createCriteria(Item.class);
			ct.addOrder(Order.asc("itemName"));  // Order By condition
			items =  ct.list();  
			    
		}catch (Exception ex){
			log.error("Unable to Fetch Item Details :" + ex.getMessage());
			ex.printStackTrace();
			throw new FetchDataException(ex.getMessage());
		} 
		finally{
			session.close();
		}
		return items;
	}

	public Object[] getItemPriceDetails(int itemId, int custId) throws FetchDataException{
		
		List<Object[]> custItemMap = null;
		Session session = null;
		try{
			session = SF.getCurrentSession();
			session.beginTransaction();
			String sql="";
			
			if (itemId>0){
				sql = "select itemid,COALESCE(hsn_code,'') hsn_code,eancode,COALESCE(articlecode,'') articlecode ,itemname, mrp, taxpercent,marginpercent,basiccostprice,taxval from LocalCustMapItemsView where itemid=" + itemId + " and custid=" + custId;
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
		
		return custItemMap.get(0);
	}
	
	// For Reports
	public Map<String, ItemWithCustomerMaringPercent> getItemDetailsWithCustomerMarginPercent(Customer cust, String eanNoList) throws FetchDataException{
			Map<String, ItemWithCustomerMaringPercent> itemMap = new HashMap<>();
			
			List<Object[]> custItemList = null;
			Session session = null;
			try{
				session = SF.getCurrentSession();
				session.beginTransaction();
				String sql = "select a.itemid,a.eancode,COALESCE( a.hsn_code, '' ) as hsn_code,a.itemname,a.taxpercent, b.mrp,c.marginpercent,COALESCE( d.articlecode, '' ) as articlecode from itemMaster a,itemmrp b,custitemmapping c ,cgarticlecodes d "
						+ "where a.itemid=b.itemid and b.mrptype=" + cust.getCustMRP().getMrpTID() + " and c.itemid=a.itemid and c.custid=" + cust.getCustID() + " and d.cgid=" + cust.getCustGroup().getCgID() + " and d.itemid=a.itemid and eancode in (" + eanNoList + ")";

				NativeQuery<Object[]>	query = session.createSQLQuery(sql); 
				custItemList = query.getResultList();  
				
				for (int i=0;i<custItemList.size();i++) {
					ItemWithCustomerMaringPercent itemWPercent = new ItemWithCustomerMaringPercent();
					Object[] item = custItemList.get(i);
					itemWPercent.setItemId( Integer.parseInt(item[0].toString()));
					itemWPercent.setEanCode(item[1].toString());
					itemWPercent.setHsnCode(item[2].toString());
					itemWPercent.setItemDesc(item[3].toString());
					itemWPercent.setItemPercent(Float.parseFloat(item[4].toString()));
					itemWPercent.setMrp(Float.parseFloat(item[5].toString()));
					itemWPercent.setCustMarginPercent(Float.parseFloat(item[6].toString()));
					itemWPercent.setCustArticleCode(item[7].toString());
					itemMap.put(item[1].toString(), itemWPercent);
				}
			}catch (Exception ex){
				log.error("Unable to Fetch Customer Item Mapping Details :" + ex.getMessage());
				throw new FetchDataException(ex.getMessage());
			} 
			finally{
				session.close();
			}
			return itemMap;
		}

	
		
}
