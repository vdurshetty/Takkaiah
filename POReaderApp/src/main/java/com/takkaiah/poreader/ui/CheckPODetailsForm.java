
package com.takkaiah.poreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.takkaiah.db.dao.CustomerDAO;
import com.takkaiah.db.dao.CustomerGroupDAO;
import com.takkaiah.db.dao.ItemDAO;
import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.KeyValueMaster;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.pdf.output.Export2Excel;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.poreader.util.SearchForm;

	public class CheckPODetailsForm extends JInternalFrame implements ActionListener,ItemListener, KeyListener {
		
		POReaderLogger log = POReaderLogger.getLogger(POReaderForm.class.getName());

		PurchaseOrderInfo poInfo; 
		DecimalFormat floatVal = new DecimalFormat("#.00");
		DecimalFormat qty = new DecimalFormat("#");
		 
		
		JTable table;
		JComboBox<KeyValueMaster> custGroup_cmb;
		JComboBox<KeyValueMaster> customer_cmb;
		JLabel   lblRows = new JLabel();
		JLabel   lblPONetValue = new JLabel();
		JLabel   lblActualNetValue = new JLabel();
		
		JButton  bt_reset = new JButton("Reset");
		JButton  bt_validate = new JButton("Calculate");
		JButton  bt_export = new JButton("Export");
	    
		CustomerDAO cDAO = new CustomerDAO();
		ItemDAO iDAO = new ItemDAO();
		Customer customer;
		float custMarginPercent = 0;
	    
	    
	    DefaultTableModel tModel = new DefaultTableModel(){
	    	private static final long serialVersionUID = 1L;
			@Override
		    public boolean isCellEditable(int row, int column) {
		    	       //Only the third column
		    	       //return column == 3;
					if (column == 5) {
						return true;
					} 
					return false;
		    }
		};
		


	    private static final long serialVersionUID = 1L;

		/**
		 * Create the frame.
		 */
		public CheckPODetailsForm() {
			setTitle("Manually Verify PO Details");
			setResizable(true);
			setMaximizable(true);
			setIconifiable(true);
			setClosable(true);
			setBounds(100, 100,750, 500);
			BorderLayout bl = new BorderLayout(1,1);
			getContentPane().setLayout(bl);
			
			lblRows.setForeground(Color.BLUE);
			
	        table=new JTable(tModel);
	        table.setAutoCreateRowSorter(true);
	        tModel.addColumn("Item Code");tModel.addColumn("HSN Code");tModel.addColumn("EAN Code"); tModel.addColumn("Customer Article No");  tModel.addColumn("Item Desctiption"); 
	        tModel.addColumn("Quantity");  tModel.addColumn("MRP"); tModel.addColumn("CGST");tModel.addColumn("SGST");tModel.addColumn("Cost Price"); tModel.addColumn("Base Cost Value");
	        tModel.addRow(new Object[] { "","","","","","","","","","",""} ); 
	        
	        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
	        table.getTableHeader().setForeground(Color.blue);
	        
	        //table.setDefaultRenderer(Object.class, new TableRenderColor());
	        table.setDefaultEditor(Object.class, new ItemCellEditor("Quantity"));
	        
	        table.getColumnModel().getColumn(0).setWidth(0);
		    table.getColumnModel().getColumn(0).setMinWidth(0);
		    table.getColumnModel().getColumn(0).setMaxWidth(0);
		    
	        //table.getColumnModel().getColumn(0).setPreferredWidth(6);
	        table.getColumnModel().getColumn(1).setPreferredWidth(15);
	        table.getColumnModel().getColumn(2).setPreferredWidth(15);
	        table.getColumnModel().getColumn(3).setPreferredWidth(20);
	        table.getColumnModel().getColumn(4).setPreferredWidth(150);
	        table.getColumnModel().getColumn(5).setPreferredWidth(20);
	        table.getColumnModel().getColumn(6).setPreferredWidth(20);
	        table.getColumnModel().getColumn(7).setPreferredWidth(10);
	        table.getColumnModel().getColumn(8).setPreferredWidth(10);
	        table.getColumnModel().getColumn(9).setPreferredWidth(20);
	        table.getColumnModel().getColumn(10).setPreferredWidth(20);

	        
	        table.getTableHeader().setReorderingAllowed(true);
	        table.addKeyListener(this);
	        
	        
	        // Grid for selecting customer_cmb Group and customer_cmb 
	        JPanel panel = new JPanel();
	        GridLayout gl = new GridLayout(3, 2);
	        panel.setLayout(gl);

	        JPanel panel2 = new JPanel();
	        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
	        panel2.setLayout(fl);

	        JLabel l1 = new JLabel("Select Customer Group   ");
	        l1.setHorizontalAlignment(SwingConstants.RIGHT);
	        JLabel l2 = new JLabel("Select Customer Name   ");
	        l2.setHorizontalAlignment(SwingConstants.RIGHT);
	        
		        
	        panel2.add(bt_reset);
	        panel2.add(bt_validate);
	        panel2.add(bt_export);
	        bt_validate.addActionListener(this);
	        bt_validate.setMnemonic(KeyEvent.VK_V);
	        bt_validate.setToolTipText("Validate PO Details");
	        bt_validate.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoValidate)));
	        bt_export.addActionListener(this);
	        bt_export.setMnemonic(KeyEvent.VK_X);
	        bt_export.setToolTipText("Export PO Details");
	        bt_export.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoExport)));
	        bt_reset.addActionListener(this);
	        bt_reset.setMnemonic(KeyEvent.VK_R);
	        bt_reset.setToolTipText("Clear Form Details");
	        bt_reset.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));
	        
	           
	        CustomerGroupDAO cgDAO = new CustomerGroupDAO();
	  
	        try{
	        	Vector<KeyValueMaster> items = cgDAO.getCustomerGroupNames();
	        	if (items!=null) {
	        		custGroup_cmb = new JComboBox<>(items);
	        		CustomerGroup cg = new CustomerGroup();
	        		cg.setCgID(items.get(0).getId());
	        		customer_cmb = new  JComboBox<>(cDAO.getCustomerNames(cg));
	        	} else {
	        		custGroup_cmb = new JComboBox<>();
	        		customer_cmb = new  JComboBox<>();
	        	}
	        }catch(Exception er)
	        {       }

	        custGroup_cmb.addItemListener(this);
			customer_cmb.addItemListener(this);
	        
	        panel.add(l1);
	        panel.add(custGroup_cmb);
	        panel.add(l2);
	        panel.add(customer_cmb);
	        
	        JLabel jLogo = new JLabel();
	        jLogo.setIcon(new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.ItemCatLogoPath)));
	        
	        JPanel formHeader = new JPanel();
	        formHeader.setLayout(new BorderLayout(2,2));
	        formHeader.add(jLogo, BorderLayout.WEST);
	        formHeader.add(panel, BorderLayout.CENTER);
	        
	        JPanel formFooter = new JPanel();
	        JPanel footerNetPrice = new JPanel();
	        formFooter.setLayout(new BorderLayout(2,2));
	        formFooter.add(panel2,BorderLayout.CENTER);
	        formFooter.add(lblRows,BorderLayout.WEST);
	        footerNetPrice.setLayout(new FlowLayout());
	        footerNetPrice.add(lblActualNetValue);
	        footerNetPrice.add(lblPONetValue);
	        formFooter.add(footerNetPrice,BorderLayout.EAST);
	        
	        getContentPane().add(formHeader,BorderLayout.NORTH);
	        getContentPane().add(new JScrollPane(table),BorderLayout.CENTER);
	        getContentPane().add(formFooter,BorderLayout.SOUTH);
	        
		} // End of constructor
		
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource().equals(custGroup_cmb)) {
	            if(e.getStateChange() == ItemEvent.SELECTED) {
	            	KeyValueMaster item = (KeyValueMaster)custGroup_cmb.getSelectedItem();
	                CustomerGroup cg = new CustomerGroup();
	         		cg.setCgID(item.getId());
	         		try{
	         			DefaultComboBoxModel< KeyValueMaster> model = new DefaultComboBoxModel<>(cDAO.getCustomerNames(cg));
	         			customer_cmb.setModel(model);
	         			if (customer_cmb.getItemCount()>0 ){
	         				customer_cmb.setSelectedIndex(0);
	         			}
	         		}catch(Exception ee){
	         			log.error("Error selecting customer Group details : "+ee.getMessage());
	         		}
	            	//JOptionPane.showMessageDialog(this, "Item Selected :" + custGroup_cmb.getSelectedItem());
	            }
			}  
			if (e.getSource().equals(customer_cmb)){
			       if(e.getStateChange() == ItemEvent.SELECTED) {
		            	KeyValueMaster item = (KeyValueMaster)customer_cmb.getSelectedItem();
		            	CustomerDAO cDAO = new CustomerDAO();
		         		try{
			            	customer = cDAO.getCustomer(item.getId());
		         		}catch(Exception ee){
		         			log.error("Error selecting customer details : "+ee.getMessage());
		         		}
		            }

			}
			
		}
		
		
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if(actionEvent.getActionCommand().equals("Export")){
				exportPODetails();
			}
			else if (actionEvent.getActionCommand().equals("Calculate")){
				for (int i=0;i<table.getRowCount();i++) {
  				    String qtyString =  table.getValueAt(i, 5) + "";
					if (qtyString.length() >0) {
						   String baseCostString =  table.getValueAt(i, 9) + "";
						   if (baseCostString.length()>0) {
							   float baseCostValue = Integer.parseInt(qtyString) * Float.parseFloat(baseCostString);
							   table.setValueAt(floatVal.format(baseCostValue) + "",i,10);
						   }
					}
				}
			   lblRows.setText("Item Count : " + table.getRowCount());
			   lblPONetValue.setText("PO Net Total Price : " +  floatVal.format(getTotalCostValue()));
			}
			else if (actionEvent.getActionCommand().equals("Reset")){
				formClear();
			}
			
		}

		/*private boolean formValidation() {
			boolean retVal = true;
			KeyValueMaster item = (KeyValueMaster)customer_cmb.getSelectedItem();
			try{
	        	customer = cDAO.getCustomer(item.getId());
	 		}catch(Exception ee){
	 			log.error("Error getting customer details:"+ee.getMessage());
	 			JOptionPane.showInternalMessageDialog(this, "Please Select the Customer Name","Empty Customer!",JOptionPane.PLAIN_MESSAGE);
	 			customer_cmb.requestFocus();
	 			retVal=false;
	 		}
			return retVal;
		} */
		
	 
	
		
		// This method is used to export PO detail to selected formats like  
		// xls  
		private void exportPODetails(){
			try{
				//if (poInfo==null){
				//	JOptionPane.showInternalMessageDialog(this, "Please Extract PO File before exporting the Purchase Order Details","Alert!",JOptionPane.ERROR_MESSAGE);
				//	return;
				//}
				String fileName=JOptionPane.showInputDialog("Enter Export XLS File Name");
				String outputFolder = POReaderReadEnv.getEnvValue(POReaderEnvProp.XSLXExportFolder);
				String xlsFileName = outputFolder + "/" + fileName +  ".xls";
				
				this.setCursor( new Cursor(Cursor.WAIT_CURSOR));
				if (Export2Excel.exportManualPODetails(POUtil.convertToList(table), xlsFileName)) {
					JOptionPane.showInternalMessageDialog(this, "File Exported Successfully to \n" + new File(xlsFileName).getAbsolutePath() ,"Success!",JOptionPane.PLAIN_MESSAGE) ;
				} else {
					JOptionPane.showInternalMessageDialog(this, "Unable to Exported File ","Alert!",JOptionPane.ERROR_MESSAGE) ;
				}
			}catch(Exception er){
				er.printStackTrace();
				log.error("Unable to export PO details : "+er.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to export PO Details!","Alert!",JOptionPane.ERROR_MESSAGE) ;
			}
			this.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
		private void formClear(){
			tModel.setNumRows(0);
			tModel.addRow(new Object[] { "","","","","","","","","","",""} );
			lblRows.setText("");
			lblPONetValue.setText("");
			lblActualNetValue.setText("");
			poInfo = null;
			custMarginPercent =0;
		}
		
		

		@Override
		public void keyPressed(KeyEvent e) {
			   if(e.getSource().equals(table))
			   {
					if (e.getKeyCode() == KeyEvent.VK_F3 || e.getKeyCode() == KeyEvent.VK_INSERT){
				        tModel.addRow(new Object[] { "","","","","","","","","","",""} ); 
  					    lblRows.setText("Item Count : " + table.getRowCount());
					} else if (e.getKeyCode() == KeyEvent.VK_F4 || e.getKeyCode() == KeyEvent.VK_DELETE)
					{
						int selectedRow = table.getSelectedRow();
						if (selectedRow >= 0) 
						{
							tModel.removeRow(selectedRow);
							if (tModel.getColumnCount()==0) {
								tModel.addRow(new Object[] { "","","","","","","","","","",""} );
							}
							lblRows.setText("Item Count : " + table.getRowCount());
						}
					}
					else if ((table.getSelectedColumn()==2 || table.getSelectedColumn()==3 || table.getSelectedColumn()==4 ) && e.getKeyCode() == KeyEvent.VK_F2){
						String[]  colNames = new String[3];
						colNames[0] = "ID"; colNames[1] ="Item Name"; colNames[2] = "EAN Code";
						SearchForm searchFrm = new SearchForm("Item List",colNames,POReaderParams.Item);
						searchFrm.setVisible(true);
						if (searchFrm.getReturnID()>0) {
							if (isItemAlreadySelected(searchFrm.getReturnID()+"") ==false)  
							{
								
								if (table.getSelectedRow()>=0){
									try {
											KeyValueMaster citem = (KeyValueMaster)customer_cmb.getSelectedItem();
											Object[] itemObject = (Object[]) iDAO.getItemPriceDetails(searchFrm.getReturnID(),citem.getId());
											float tax = Float.parseFloat(itemObject[6] + "");
											table.setValueAt(itemObject[0]  + "",table.getSelectedRow(),0);
											table.setValueAt(itemObject[1]  + "",table.getSelectedRow(),1);
											table.setValueAt(itemObject[2]  + "",table.getSelectedRow(),2);
											table.setValueAt(itemObject[3]  + "",table.getSelectedRow(),3);
											table.setValueAt(itemObject[4]  + "",table.getSelectedRow(),4);
											table.setValueAt(itemObject[5]  + "",table.getSelectedRow(),6);
											table.setValueAt((tax/2)  + "",table.getSelectedRow(),7);
											table.setValueAt((tax/2)  + "",table.getSelectedRow(),8);
											table.setValueAt(itemObject[8]  + "",table.getSelectedRow(),9);
											
									}catch(Exception er) {
										er.printStackTrace();
									}
								}
							}
						}
						searchFrm.dispose();
						this.repaint();
						this.revalidate();

					}

			   }
		}


		@Override
		public void keyReleased(KeyEvent e) {
			   if(e.getSource().equals(table))
			   {
				   if (table.getSelectedColumn()==5) {
					   String qtyString =  table.getValueAt(table.getSelectedRow(), 5) + "";
					   String baseCostString =  table.getValueAt(table.getSelectedRow(), 9) + "";
					   if (qtyString.length() > 0 && baseCostString.length()>0) {
						   float baseCostValue = Integer.parseInt(qtyString) * Float.parseFloat(baseCostString);
						   table.setValueAt(floatVal.format(baseCostValue) + "",table.getSelectedRow(),10);
						   lblPONetValue.setText("PO Net Total Price : " +  floatVal.format(getTotalCostValue()));
					   }
				   }
			   }
			
		}


		@Override
		public void keyTyped(KeyEvent e) {
		}

		
		private float getTotalCostValue() {
			float totCostValue = 0;
			for (int i=0;i<table.getRowCount();i++) {
				String colTotal = table.getValueAt(i, 10) + "";
				if (colTotal.length() >0) {
					totCostValue = totCostValue + Float.parseFloat(colTotal);
				}
			}
			return totCostValue;
		}
		
		private boolean isItemAlreadySelected(String itemCode) {
			for (int i=0;i<table.getRowCount();i++) {
				String tableItemCode = table.getValueAt(i, 0) + "";
				if (itemCode.equalsIgnoreCase(tableItemCode)) {
					table.setRowSelectionInterval(i,i);
					return true;
				}
			}
			return false;
		}
	}
