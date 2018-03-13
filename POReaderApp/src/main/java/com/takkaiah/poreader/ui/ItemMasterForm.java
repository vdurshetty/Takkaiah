package com.takkaiah.poreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.takkaiah.db.dao.ItemDAO;
import com.takkaiah.db.dto.Item;
import com.takkaiah.db.dto.ItemCategory;
import com.takkaiah.db.dto.ItemMRP;
import com.takkaiah.db.dto.MRPType;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.poreader.util.SearchForm;

public class ItemMasterForm extends JInternalFrame implements ActionListener,KeyListener {
	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(ItemMasterForm.class.getName());

	JButton  bt_add = new JButton("Add");
	JButton  bt_update = new JButton("Update");
	JButton  bt_delete = new JButton("Delete");
	JButton  bt_reset = new JButton("Reset");
	JButton  bt_search = new JButton("Search");

	int      itemCatID = 0;
	JTextField  itemID_txt = new JTextField();
	JTextField  ean_txt = new JTextField();
	JTextField  itemName_txt = new JTextField();
	JTextField  alias_txt = new JTextField();
	JTextField  itemCatName_txt = new JTextField();
	JTextField  units_txt = new JTextField();
	JTextField  taxPercent_txt = new JTextField();
	JTextField  caseQty_txt = new JTextField();
	JTextField  hsnCode_txt = new JTextField();
	JLabel    status_label = new JLabel(" ");

	JTable table;
	DefaultTableModel tModel = new DefaultTableModel(){
		private static final long serialVersionUID = 1L;
		@Override
    	   public boolean isCellEditable(int row, int column) {
    	       //Only the third column
    	       //return column == 3;
			 if (column==3) {
				 return true;
			 }
			return false;
    	   }
	};
	
	public ItemMasterForm() {
		addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
            	//dispose();
            	setVisible(false);
            }
        }); 
		setTitle("Item Master Form");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(false);
		setClosable(true);
		setBounds(100, 100, 750, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		table=new JTable(tModel);
	    tModel.addColumn("ID "); tModel.addColumn("MRP Type ID"); tModel.addColumn("MRP Type");  tModel.addColumn("MRP "); tModel.addColumn("Row Status ");
	    
		    
	    table.getColumnModel().getColumn(0).setWidth(0);
	    table.getColumnModel().getColumn(0).setMinWidth(0);
	    table.getColumnModel().getColumn(0).setMaxWidth(0); 
       
	    //table.getColumnModel().getColumn(0).setPreferredWidth(30);

	  
	    table.getColumnModel().getColumn(1).setWidth(0);
	    table.getColumnModel().getColumn(1).setMinWidth(0);
	    table.getColumnModel().getColumn(1).setMaxWidth(0);
	   
	    
	    //table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        //table.getColumnModel().getColumn(2).setPreferredWidth(30);
	   
        table.getColumnModel().getColumn(4).setWidth(0);
	    table.getColumnModel().getColumn(4).setMinWidth(0);
	    table.getColumnModel().getColumn(4).setMaxWidth(0); 
      
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
	    table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

         
        table.setDefaultEditor(Object.class, new ItemCellEditor("MRP"));
        table.getTableHeader().setForeground(Color.blue);


		itemID_txt.setEnabled(false);
		itemID_txt.setDisabledTextColor(Color.BLACK);
        status_label.setForeground(Color.BLUE);

          
        JPanel formPanel = new JPanel();
        GridLayout gl1 = new GridLayout(5, 4);
        formPanel.setLayout(gl1);
  
        JPanel buttonPanel = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
        buttonPanel.setLayout(fl);
        
        buttonPanel.add(bt_add);
        buttonPanel.add(bt_update);
        buttonPanel.add(bt_delete);
        buttonPanel.add(bt_reset);
        buttonPanel.add(bt_search);
        bt_add.addActionListener(this);
        bt_update.addActionListener(this);
        bt_delete.addActionListener(this);
        bt_reset.addActionListener(this);
        bt_search.addActionListener(this);
        itemCatName_txt.addKeyListener(this);
        table.addKeyListener(this);
        bt_add.setMnemonic(KeyEvent.VK_A);
        bt_add.setToolTipText("Add Item Details");
        bt_add.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoAdd)));
        bt_update.setMnemonic(KeyEvent.VK_U);
        bt_update.setToolTipText("Update Item Details");
        bt_update.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoUpdate)));
        bt_delete.setMnemonic(KeyEvent.VK_D);
        bt_delete.setToolTipText("Delete Item Details");
        bt_delete.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoDelete)));
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setToolTipText("Clear Form Details");
        bt_reset.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));
        bt_search.setMnemonic(KeyEvent.VK_S);
        bt_search.setToolTipText("Search Item Details");
        bt_search.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoSearch)));
          
        
        
        JLabel l1 = new JLabel("  Item ID   ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("  EAN Code   ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("  Item Name   ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l4 = new JLabel("  Item Alias   ");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l5 = new JLabel("  Item Category Name   ");
        l5.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l6 = new JLabel("  Units   ");
        l6.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l7 = new JLabel("  GST Percent   ");
        l7.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l8 = new JLabel("  Case Quantity   ");
        l8.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l9 = new JLabel("  HSN Code  ");
        l9.setHorizontalAlignment(SwingConstants.RIGHT);
    
        

        formPanel.add(l1);
        formPanel.add(itemID_txt);
        formPanel.add(l2);
        formPanel.add(ean_txt);
        formPanel.add(l9);
        formPanel.add(hsnCode_txt);
        formPanel.add(l3);
        formPanel.add(itemName_txt);
        formPanel.add(l4);
        formPanel.add(alias_txt);
        
        formPanel.add(l5);
        formPanel.add(itemCatName_txt);      
        formPanel.add(l6);
        formPanel.add(units_txt);
        formPanel.add(l7);
        formPanel.add(taxPercent_txt);
        formPanel.add(l8);
        formPanel.add(caseQty_txt);
        
        ean_txt.setToolTipText("Enter Item EAN Code");
        itemName_txt.setToolTipText("Enter Item Name");
        alias_txt.setToolTipText("Enter Item Alias");
        itemCatName_txt.setToolTipText("Press F2 to Select Item Category");
        units_txt.setToolTipText("Enter Item Units");
        taxPercent_txt.setToolTipText("Enter GST Percent");
        caseQty_txt.setToolTipText("Enter Item Case Quantity");
        table.setToolTipText("Press Insert to Add Row or Delete to Remove Item MRP Details");
        
        taxPercent_txt.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if (((caracter < '0') || (caracter > '9') || taxPercent_txt.getText().length() >= 5 )
                        && (caracter != '\b') && (caracter!='.')) {
                    e.consume();
                }
            }
        });

        caseQty_txt.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if (((caracter < '0') || (caracter > '9') || caseQty_txt.getText().length() >= 3 )
                        && (caracter != '\b') ) {
                	   e.consume();
               }
             }
        });
        
        
        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
        
        JLabel jLogo = new JLabel();
        jLogo.setIcon(new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.ItemMasterLogoPath)));

        //cgpanel.add(jLogo,BorderLayout.WEST);
        cgpanel.add(formPanel , BorderLayout.CENTER);
        cgpanel.add(buttonPanel,BorderLayout.SOUTH);

        BorderLayout bl = new BorderLayout(1,1);
   		getContentPane().setLayout(bl);
   		
        getContentPane().add(cgpanel,BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(table),BorderLayout.CENTER);
        getContentPane().add(status_label,BorderLayout.SOUTH);
        formClear();
        buttonEnable(POReaderParams.ButtonEnableForReset);
		
	}

	
	private boolean formValidate(){
		boolean status = true;
		if (table.getDefaultEditor(Object.class).stopCellEditing()==false){
			table.requestFocus();
			status=false;
		} else if (ean_txt.getText().trim().length()==0){
			JOptionPane.showInternalMessageDialog(this, "Please Enter EAN Code");
			ean_txt.requestFocus();
			status=false;
		} else if (itemName_txt.getText().trim().length()==0){
			JOptionPane.showInternalMessageDialog(this, "Please Select the Item Name!");
			itemName_txt.requestFocus();
			status=false;
		} else if (itemCatName_txt.getText().trim().length()==0){
			JOptionPane.showInternalMessageDialog(this, "Please Select the Item Category!");
			itemCatName_txt.requestFocus();
			status=false;
		} else if (taxPercent_txt.getText().length()>0){
			try{
				Float.parseFloat(taxPercent_txt.getText());
			}catch(NumberFormatException nes){
				JOptionPane.showInternalMessageDialog(this, "Please Enter Decimal value for Tax Percent!");
				taxPercent_txt.requestFocus();
				status=false;
			}
		}
		return status;
	}
	
	private Item fillItemDetails(){
		Item item = new Item();
		
		if (itemID_txt.getText().length()>0){
			item.setItemID(Integer.parseInt( itemID_txt.getText() ));
		}
		
		ItemCategory ic = new ItemCategory();
		ic.setCatID(itemCatID);
		
		item.setItemCategory(ic);
		if (taxPercent_txt.getText().length()>0){
			item.setTaxPercent(Float.parseFloat(taxPercent_txt.getText().trim()));
		}
		item.setEanCode(ean_txt.getText());
		item.setItemName(itemName_txt.getText());
		if (hsnCode_txt.getText().trim().length()>0) {
			item.setHsnCode(hsnCode_txt.getText());
		} else {
			item.setHsnCode(null);
		}
		item.setAlias(alias_txt.getText());
		item.setUnits(units_txt.getText());
		if (caseQty_txt.getText().length()>0){
			item.setCaseQty(Integer.parseInt(caseQty_txt.getText() ));
		}
		
		List<ItemMRP> itemMRPList = new ArrayList<>();
		for (int i=0;i<table.getRowCount();i++){
			if (table.getValueAt(i, 2).toString().length()>0){
				ItemMRP itemMRP = new ItemMRP();
				MRPType mrpType = new MRPType();
				if (table.getValueAt(i, 0)!=null){
					if (table.getValueAt(i, 0).toString().length()>0){
						itemMRP.setiMRPID(Integer.parseInt(table.getValueAt(i, 0).toString()) );
					}
				}
				mrpType.setMrpTID(Integer.parseInt(table.getValueAt(i, 1).toString()) );
				itemMRP.setItem(item);
				itemMRP.setItemMRPType(mrpType);
				if (table.getValueAt(i, 3).toString().length()>0){
					itemMRP.setMrp(Float.parseFloat(table.getValueAt(i, 3).toString()) );
				}else{ 
					itemMRP.setMrp(0);
				}
				itemMRPList.add(itemMRP);
			}	
		}
		
		item.setItemPrice(itemMRPList);
		return item;
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) 
	{
		if (actionEvent.getActionCommand().equals("Add")){
			try{
				if (formValidate()==false)
					return;
				Item item = fillItemDetails();
				ItemDAO iDAO = new ItemDAO();
		        if (iDAO.addItem(item)==true){
			        status_label.setText("Item '" + ean_txt.getText() + "' added successfully!");
			        buttonEnable(POReaderParams.ButtonEnableForAdd);
		        }
			}catch(PrimaryKeyException pke){
				if (pke.getMessage().contains("EAN")) {
					JOptionPane.showInternalMessageDialog(this, "Item Already Exists with the EANCode - '" + ean_txt.getText() + "'");
					ean_txt.requestFocus();
				} else {
					JOptionPane.showInternalMessageDialog(this, "Item Already Exists with the Name - '" + itemName_txt.getText() + "'");
					itemName_txt.requestFocus();
				}
			}catch(Exception ee){
				log.error("Unable to add Item Details :" + ee.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to add the '" + ean_txt.getText() + "' Item");
			}
		} else if (actionEvent.getActionCommand().equals("Search")){
			try{
				String[]  colNames = new String[3];
				colNames[0] = "ID"; colNames[1] ="Item Name"; colNames[2] = "EAN Code";
				SearchForm searchFrm = new SearchForm("Item List",colNames,POReaderParams.Item);
				searchFrm.setVisible(true);
				if (searchFrm.getReturnID()>0) {
					formClear();
					ItemDAO iDAO = new ItemDAO();
					Item item = iDAO.getItem(searchFrm.getReturnID());
					
					ItemCategory ic = item.getItemCategory();
					if (ic!=null){
						itemCatID = ic.getCatID();
						itemCatName_txt.setText(ic.getCatName());
					}
					itemID_txt.setText(item.getItemID() + "");
					taxPercent_txt.setText(item.getTaxPercent() + "");
					ean_txt.setText(item.getEanCode());
					hsnCode_txt.setText(item.getHsnCode());
					itemName_txt.setText(item.getItemName());
					alias_txt.setText(item.getAlias());
					units_txt.setText(item.getUnits());
					if (item.getCaseQty()!=null) {
						caseQty_txt.setText(item.getCaseQty() + "");
					}
					status_label.setText("");
					
					List<ItemMRP> mrps = item.getItemPrice();
					tModel.setNumRows(0);
					if (mrps!=null){
						if (mrps.size()==0){
							tModel.addRow(new Object[] { "" , "","", "","new" } );
						} else {
							for (int i=0;i<mrps.size();i++){
								ItemMRP mrp = mrps.get(i);
								MRPType mrpType = mrp.getItemMRPType();
								if (mrpType!=null){ 		}
								tModel.addRow(new Object[] { mrp.getiMRPID() , mrpType.getMrpTID(),mrpType.getMrpTName() , mrp.getMrp() ,"old" } ); 
							}
						}
					} else {
						tModel.addRow(new Object[] { "" , "","", "","new" } );
					}
					
					searchFrm.dispose();
					searchFrm =null;
					buttonEnable(POReaderParams.ButtonEnableForSearch);
				}
				this.repaint();
				this.revalidate();
			}catch(Exception e){
				e.printStackTrace();
				log.error("Unable to Fetch Item Details :" + e.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to Fetch Item Details :" + e.getMessage(),"Fetch Error !",JOptionPane.ERROR_MESSAGE);
			}
		} else if (actionEvent.getActionCommand().equals("Update")){
			try{
				if (formValidate()==false)
					return;
				Item item = fillItemDetails();
				ItemDAO iDAO = new ItemDAO();
		        if (iDAO.updateItem(item)==true){
			        status_label.setText("Item '" + ean_txt.getText() + "' updated successfully!");
			        buttonEnable(POReaderParams.ButtonEnableForUpdate);
		        }
				this.repaint();
				this.revalidate();
			}catch(Exception e){
				log.error("Unable to Update Item Details :" + e.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to Update Item Details :" + e.getMessage(),"Update Error !",JOptionPane.ERROR_MESSAGE);
			}
		} else if (actionEvent.getActionCommand().equals("Reset")){
			formClear();
			
			
		} 
		else if (actionEvent.getActionCommand().equals("Delete"))
		{
			if (itemID_txt.getText().length()>0 ) 
			{
				if (JOptionPane.showInternalConfirmDialog(this, "Do you want to delete the Item - '" + ean_txt.getText() + " - "  + itemName_txt.getText() + "'?","Confirmation", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION) 
				{ 
					try{
						ItemDAO iDAO = new ItemDAO();
						Item item = new Item();
						item.setItemID(Integer.parseInt(itemID_txt.getText()));
						String itemName = itemName_txt.getText();
						if (iDAO.deleteItem(item)==true){
							status_label.setText("Item '" + itemName + "' deleted successfully!");
							buttonEnable(POReaderParams.ButtonEnableForDelete);
						}
						formClear();
					}catch(Exception delE){
						log.error("Unable to delete Item Details :" + delE.getMessage());
						JOptionPane.showInternalMessageDialog(this, "Unable to Delete Item Details :" + delE.getMessage(),"Delete Error !",JOptionPane.ERROR_MESSAGE);
					}
					
				}
			}
		}
			
	}

	@Override
	public void keyPressed(KeyEvent e) {
	   if(e.getSource().equals(table))
	   {
			if (e.getKeyCode() == KeyEvent.VK_F3 || e.getKeyCode() == KeyEvent.VK_INSERT){
				tModel.addRow(new Object[] { "" , "","","","new" } ); 
			} else if (e.getKeyCode() == KeyEvent.VK_F4 || e.getKeyCode() == KeyEvent.VK_DELETE)
			{
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) 
				{
					if (JOptionPane.showInternalConfirmDialog(this, "Do you want to delete the MRP Type - '" + tModel.getValueAt(selectedRow, 2).toString()  + "'?","Confirmation", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION) 
					{
						if (tModel.getValueAt(selectedRow, 0).toString().trim().length()>0)
						{
							int mrpTypeRowNum = Integer.parseInt(tModel.getValueAt(selectedRow, 0).toString());  
							if (mrpTypeRowNum>0){
								ItemDAO iDAO = new ItemDAO();
								ItemMRP itemMRP = new ItemMRP();
								itemMRP.setiMRPID(mrpTypeRowNum);
								try {
									String mrpValue =  tModel.getValueAt(selectedRow, 2).toString();
									iDAO.deleteItemMRP(itemMRP);
									tModel.removeRow(selectedRow);
									status_label.setText("Item MRP '" + mrpValue + "' deleted successfully!");
								} catch(Exception ee){		
									log.error("Unable to delete Item MRP Details :" + ee.getMessage());
								}
							}
						}
						else {
							tModel.removeRow(selectedRow);
						}
						if (tModel.getRowCount()==0){
							tModel.addRow(new Object[] { "" , "","","","new" } ); 
						}
					}
				}
			} else {
				if (table.getSelectedColumn()==2 && e.getKeyCode() == KeyEvent.VK_F2){
			    	SearchForm sfrm = new SearchForm("MRP Types", null,POReaderParams.MRPType);
			    	sfrm.setVisible(true);
					if (sfrm.getReturnID()>0) {
						if (table.getSelectedRow()>=0){
							table.setValueAt(sfrm.getReturnID() + "",table.getSelectedRow(),table.getSelectedColumn() - 1);
							table.setValueAt(sfrm.getReturnStr() + "",table.getSelectedRow(),table.getSelectedColumn());
						}
					}
					sfrm.dispose();
					this.repaint();
					this.revalidate();

				}
			}
		} else	if (e.getSource().equals(itemCatName_txt)){
			if (e.getKeyCode() == KeyEvent.VK_F2) {
		    	SearchForm sfrm = new SearchForm("Item Category",null, POReaderParams.ItemCategory);
		    	sfrm.setVisible(true);
				if (sfrm.getReturnID()>0) {
					itemCatID = sfrm.getReturnID();
					itemCatName_txt.setText(sfrm.getReturnStr());
				}
				sfrm.dispose();
				this.repaint();
				this.revalidate();
			}
		} 
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource().equals(itemCatName_txt))
		{
			if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
				itemCatName_txt.setText("");
				itemCatID = 0;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource().equals(itemCatName_txt))
		{
			e.consume();
		} 
	}
	
	public void formClear(){
		itemCatID = 0;
		itemID_txt.setText("");
		ean_txt.setText("");
		hsnCode_txt.setText("");
		itemName_txt.setText("");
		alias_txt.setText("");
		itemCatName_txt.setText("");
		units_txt.setText("");
		taxPercent_txt.setText("");
		caseQty_txt.setText("");
		status_label.setText(" ");
		tModel.setNumRows(0);
		tModel.addRow(new Object[] { "" , "","", "" ,"new" } );
		buttonEnable(POReaderParams.ButtonEnableForReset);
	}

	private void buttonEnable(int enableType){
		
		if (enableType == POReaderParams.ButtonEnableForAdd){
			bt_add.setEnabled(true);
			bt_update.setEnabled(false);
			bt_delete.setEnabled(false);
			bt_reset.setEnabled(true);
		} else if (enableType == POReaderParams.ButtonEnableForUpdate){
			bt_add.setEnabled(true);
			bt_update.setEnabled(false);
			bt_delete.setEnabled(false);
			bt_reset.setEnabled(true);
		} else if (enableType == POReaderParams.ButtonEnableForDelete){
			bt_add.setEnabled(true);
			bt_update.setEnabled(false);
			bt_delete.setEnabled(false);
			bt_reset.setEnabled(true);
		} else if (enableType == POReaderParams.ButtonEnableForReset){
			bt_add.setEnabled(true);
			bt_update.setEnabled(false);
			bt_delete.setEnabled(false);
			bt_reset.setEnabled(true);
		} else if (enableType == POReaderParams.ButtonEnableForSearch){
			bt_add.setEnabled(false);
			bt_update.setEnabled(true);
			bt_delete.setEnabled(true);
			bt_reset.setEnabled(true);
		}
	}
}
