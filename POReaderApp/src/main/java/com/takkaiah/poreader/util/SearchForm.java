package com.takkaiah.poreader.util;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.takkaiah.db.dao.CustomerDAO;
import com.takkaiah.db.dao.CustomerGroupDAO;
import com.takkaiah.db.dao.ItemCategoryDAO;
import com.takkaiah.db.dao.ItemDAO;
import com.takkaiah.db.dao.MRPTypeDAO;
import com.takkaiah.db.dao.UserDAO;
import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.Item;
import com.takkaiah.db.dto.ItemCategory;
import com.takkaiah.db.dto.MRPType;
import com.takkaiah.db.dto.User;
import com.takkaiah.logger.POReaderLogger;

public class SearchForm extends JDialog implements  MouseListener, KeyListener, WindowListener{
	private static final long serialVersionUID = 1L;
	
	POReaderLogger log = POReaderLogger.getLogger(SearchForm.class.getName());
	
	JTable table;
	JTextField  searchTxt = new JTextField();
	private int returnID = 0;
	private String returnStr = "";
	DefaultTableModel tModel = new DefaultTableModel(){
		private static final long serialVersionUID = 1L;
		@Override
    	   public boolean isCellEditable(int row, int column) {
			return false;
    	   }
    };
 
	public SearchForm(String title,String[] cols,int frmSearchPattern) {
		
		setTitle(title);
		setBounds(100, 100, 450, 300);
		setModal(true);
		setLocationRelativeTo(null);
		
		table=new JTable(tModel);
		table.setAutoCreateRowSorter(true);
		
		if (cols==null){
			tModel.addColumn("ID"); tModel.addColumn("Name");
			table.getColumnModel().getColumn(0).setPreferredWidth(20);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
		
		} else {
			for (int i=0;i<cols.length;i++){
				tModel.addColumn(cols[i]);
			}
		}

		if (tModel.getColumnCount()>0){
		    table.getColumnModel().getColumn(0).setWidth(0);
		    table.getColumnModel().getColumn(0).setMinWidth(0);
		    table.getColumnModel().getColumn(0).setMaxWidth(0); 
		}
	    
	    JPanel searchPanel = new JPanel();
	    searchPanel.setLayout(new BorderLayout());
	    searchPanel.add(new JLabel("Enter Search :"), BorderLayout.WEST);
	    searchPanel.add(searchTxt, BorderLayout.CENTER);
	    
		getContentPane().setLayout(new BorderLayout(2,2));
		getContentPane().add(searchPanel,BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(table),BorderLayout.CENTER);
		
		table.addKeyListener(this);
		table.addMouseListener(this);
		searchTxt.addKeyListener(this);
		searchTxt.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				startSearch(searchTxt.getText().trim());
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		addKeyListener(this);
		fillTable(frmSearchPattern, cols);
	}


	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode()== KeyEvent.VK_ESCAPE){
			returnID=0;
			dispose();
		}
		if (ke.getSource() instanceof JTable){
		    if (ke.getKeyCode() == KeyEvent.VK_F2 || ke.getKeyCode() == KeyEvent.VK_ENTER) {
		    	if (table.getSelectedRow()>=0){
		    		returnID = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
		    		returnStr = table.getValueAt(table.getSelectedRow(),1).toString();
		    		dispose();
		    	}
		    } 
		} else if (ke.getSource() instanceof JTextField) {
		    if (ke.getKeyCode() == KeyEvent.VK_F2 || ke.getKeyCode() == KeyEvent.VK_ENTER) {
		    	if (table.getSelectedRow()>=0){
		    		returnID = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
		    		returnStr = table.getValueAt(table.getSelectedRow(),1).toString();
		 	        dispose();
		    	}
		    }else  if (ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_DOWN) {
		    	table.scrollRectToVisible(table.getCellRect(0, 0, true));

                // this will automatically set the focus of the searched/selected row/value
                table.setRowSelectionInterval(0, 0);
		    	table.requestFocus();
		    }else {
		    	startSearch(searchTxt.getText().trim());
		    }
		}
		
	}


	@Override
	public void keyReleased(KeyEvent ke) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent ke) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseClicked(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent me) {
        if (me.getClickCount() == 2) {
 	        returnID = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
 	       returnStr = table.getValueAt(table.getSelectedRow(),1).toString();
 	        dispose();
        }
	}


	@Override
	public void mouseReleased(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}
	
	private void startSearch(String searchText)
	{
		 if (searchText.length()==0){
			 return;
		 }
		 for (int row = 0; row <= table.getRowCount() - 1; row++) 
		 {
                     for (int i = 0; i <= table.getColumnCount() - 1; i++) 
                     {
                    	 
         			 	String rowVal = table.getValueAt(row, i).toString().toLowerCase(); 
                        if ( rowVal.startsWith(searchText.toLowerCase())) 
                        {
                            // this will automatically set the view of the scroll in the location of the value
                            table.scrollRectToVisible(table.getCellRect(row, i, true));

                            // this will automatically set the focus of the searched/selected row/value
                            table.setRowSelectionInterval(row, row);
                            row = table.getRowCount();
                            break;
                        }
                     //    table.getColumnModel().getColumn(i).setCellRenderer(new HighlightRenderer());
                     //}
                      }
          }
	}

	

	
	private void fillTable(int frmSearchPattern, String[] cols){
		if (frmSearchPattern==POReaderParams.CustomerGroup){
			CustomerGroupDAO cgDAO = new CustomerGroupDAO();
			try{
				List<CustomerGroup> cgList =  cgDAO.getAllCustomerGroups();
				tModel.setNumRows(0);
		        if (cgList!=null) {
					for(int i=0;i<cgList.size();i++) {
						CustomerGroup row = cgList.get(i);
						tModel.addRow(new Object[] { row.getCgID() , row.getCgName()});
					}
		        } 
			}catch(Exception ex){
				log.error("Unable to fetch Customer Group Details:" + ex.getMessage() );
			}
			
		} 	else if (frmSearchPattern==POReaderParams.Customer){
			CustomerDAO cDAO = new CustomerDAO();
			try{
				List<Customer> cList =  cDAO.getAllCustomers();
				tModel.setNumRows(0);
		        if (cList!=null) {
					for(int i=0;i<cList.size();i++) {
						Customer row = cList.get(i);
						tModel.addRow(new Object[] { row.getCustID() , row.getCustomerName()});
					}
		        } 
			}catch(Exception ex){
				log.error("Unable to fetch Customer Details:" + ex.getMessage() );
			}
			
		} else if (frmSearchPattern==POReaderParams.MRPType){
			MRPTypeDAO mrpDAO = new MRPTypeDAO();
			try{
				List<MRPType> cList =  mrpDAO.getAllMRPTypes();
				tModel.setNumRows(0);
		        if (cList!=null) {
					for(int i=0;i<cList.size();i++) {
						MRPType row = cList.get(i);
						tModel.addRow(new Object[] { row.getMrpTID() , row.getMrpTName()});
					}
		        } 
			}catch(Exception ex){
				log.error("Unable to fetch MRP Type Details:" + ex.getMessage() );
			}
			
		} else if (frmSearchPattern==POReaderParams.ItemCategory){
			ItemCategoryDAO icDAO = new ItemCategoryDAO();
			try{
				List<ItemCategory> cList =  icDAO.getAllItemCategory();
				tModel.setNumRows(0);
		        if (cList!=null) {
					for(int i=0;i<cList.size();i++) {
						ItemCategory row = cList.get(i);
						tModel.addRow(new Object[] { row.getCatID() , row.getCatName()});
					}
		        } 
			}catch(Exception ex){
				log.error("Unable to fetch Item Category Details:" + ex.getMessage() );
			}
			
		} else if (frmSearchPattern==POReaderParams.Item){
			ItemDAO iDAO = new ItemDAO();
			try{
				List<Item> cList =  iDAO.getAllitems();
				tModel.setNumRows(0);
		        if (cList!=null) {
					for(int i=0;i<cList.size();i++) {
						Item row = cList.get(i);
						tModel.addRow(new Object[] { row.getItemID() , row.getItemName(),row.getEanCode()});
					}
		        } 
			}catch(Exception ex){
				log.error("Unable to fetch Item Details:" + ex.getMessage() );
			}
			
		} else if (frmSearchPattern==POReaderParams.Users){
			UserDAO uDAO = new UserDAO();
			try{
				List<User> cList =  uDAO.getAllUsers();
				tModel.setNumRows(0);
		        if (cList!=null) {
					for(int i=0;i<cList.size();i++) {
						User row = cList.get(i);
						tModel.addRow(new Object[] { row.getUid() , row.getUserName(),row.getFullName()});
					}
		        } 
			}catch(Exception ex){
				log.error("Unable to fetch User Details:" + ex.getMessage() );
			}
			
		}

	}
	
	public int getSelectedID(){
		return returnID;
	}


	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosed(WindowEvent e) {
		returnID =0;
		returnStr="";
	}


	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	public int getReturnID() {
		return returnID;
	}


	public void setReturnID(int returnID) {
		this.returnID = returnID;
	}


	public String getReturnStr() {
		return returnStr;
	}


	public void setReturnStr(String returnStr) {
		this.returnStr = returnStr;
	}

	
	
}
