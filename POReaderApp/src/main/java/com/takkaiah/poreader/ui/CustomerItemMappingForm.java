package com.takkaiah.poreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.takkaiah.db.dao.CustomerArticleCodeDAO;
import com.takkaiah.db.dao.CustomerDAO;
import com.takkaiah.db.dao.CustomerItemMappingDAO;
import com.takkaiah.db.dao.ItemDAO;
import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.CustomerArticleCode;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.CustomerItemMapping;
import com.takkaiah.db.dto.Item;
import com.takkaiah.db.dto.MRPType;
import com.takkaiah.email.Email;
import com.takkaiah.email.EmailProperties;
import com.takkaiah.email.EmailService;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.poreader.util.SearchForm;

public class CustomerItemMappingForm extends JInternalFrame  implements ActionListener,KeyListener{
	
	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(CustomerItemMappingForm.class.getName());
	

	CustomerDAO cDAO = new CustomerDAO();
	JTable table;

	JButton  bt_save = new JButton("Save");
	JButton  bt_reset = new JButton("Reset");
	JButton  bt_search = new JButton("Search");
    

	int      cgID = 0;
	int      mrpTypeID = 0; 
	JTextField  cID_txt = new JTextField();
	JTextField  cName_txt = new JTextField();
	JTextField  cgName_txt = new JTextField();
	JTextArea  address_txt = new JTextArea();
	JTextField  email_txt = new JTextField();
	JTextField  mobile_txt = new JTextField();
	JTextArea  remarks_txt = new JTextArea();
	JTextField  mrpType_txt = new JTextField();
	JLabel    status_label = new JLabel(" ");
	
	DefaultTableModel tModel = new DefaultTableModel(){
		private static final long serialVersionUID = 1L;
		@Override
    	   public boolean isCellEditable(int row, int column) {
			 if (column==5) {
				 return true;
			 }
			 return false;
    	   }
	};
	 
	public CustomerItemMappingForm() {
		addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
            	//dispose();
            	setVisible(false);
            	//pack();
            }
        }); 
		setTitle("Customer Item Mapping Form");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(false);
		setClosable(true);
		setBounds(100, 100, 700, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table=new JTable(tModel);
		table.setAutoCreateRowSorter(true);
	    tModel.addColumn("ID"); tModel.addColumn("Item ID"); tModel.addColumn("Item Name");      tModel.addColumn("EAN");  
	    tModel.addColumn("Customer Article Code"); tModel.addColumn("Item Margin");     
	    
	    //DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        //rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getTableHeader().setForeground(Color.blue);


	    table.getColumnModel().getColumn(0).setWidth(0);
	    table.getColumnModel().getColumn(0).setMinWidth(0);
	    table.getColumnModel().getColumn(0).setMaxWidth(0); 

	    table.getColumnModel().getColumn(1).setWidth(0);
	    table.getColumnModel().getColumn(1).setMinWidth(0);
	    table.getColumnModel().getColumn(1).setMaxWidth(0); 
	    
	
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(40);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
	    table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        //table.getTableHeader().setForeground(Color.blue);
        table.setDefaultEditor(Object.class, new ItemCellEditor("Item Margin"));
        
        
        // Interchange columns in table
        table.getTableHeader().setReorderingAllowed(true);
        table.addKeyListener(this);
        
	    cID_txt.setEnabled(false);
        status_label.setForeground(Color.BLUE);
        
        JPanel formPanel = new JPanel();
        GridLayout gl1 = new GridLayout(5, 4);
        formPanel.setLayout(gl1);
  
        JPanel buttonPanel = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
        buttonPanel.setLayout(fl);
        
        buttonPanel.add(bt_save);
        buttonPanel.add(bt_reset);
        buttonPanel.add(bt_search);
        bt_save.addActionListener(this);
        bt_reset.addActionListener(this);
        bt_search.addActionListener(this);
               
        table.setToolTipText("Press F2 to Select Item Details");
        
        bt_save.setMnemonic(KeyEvent.VK_S);
        bt_save.setToolTipText("Save Customer Item Mappings");
        bt_save.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoSave)));
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setToolTipText("Clear Form Details");
        bt_reset.setIcon(POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));
        bt_search.setMnemonic(KeyEvent.VK_E);
        bt_search.setToolTipText("Search Customer Item Mapping Details");
        bt_search.setIcon(POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoSearch)));
        
        

        JLabel l1 = new JLabel("  Customer ID  ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("  Customer Name  ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("  Customer Group Name  ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l4 = new JLabel("  MRP Type   ");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l5 = new JLabel("  Margin Percent  ");
        l5.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JLabel l6 = new JLabel("  Email  ");
        l6.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l7 = new JLabel("  Mobile  ");
        l7.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l8 = new JLabel("  Address  ");
        l8.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l9 = new JLabel("  Remarks  ");
        l9.setHorizontalAlignment(SwingConstants.RIGHT);

        formPanel.add(l1);
        formPanel.add(cID_txt);
        formPanel.add(l2);
        formPanel.add(cName_txt);
        formPanel.add(l3);
        formPanel.add(cgName_txt);
        formPanel.add(l4);
        formPanel.add(mrpType_txt);
        formPanel.add(l6);
        formPanel.add(email_txt);
        formPanel.add(l7);
        formPanel.add(mobile_txt);
        
    	cName_txt.setEnabled(false);
    	cgName_txt.setEnabled(false);
    	address_txt.setEnabled(false);
    	email_txt.setEnabled(false);
    	mobile_txt.setEnabled(false);
    	remarks_txt.setEnabled(false);
    	mrpType_txt.setEnabled(false);
    	
    	cID_txt.setDisabledTextColor(Color.BLACK);
    	cName_txt.setDisabledTextColor(Color.BLACK);
    	cgName_txt.setDisabledTextColor(Color.BLACK);
    	address_txt.setDisabledTextColor(Color.BLACK);
    	email_txt.setDisabledTextColor(Color.BLACK);
    	mobile_txt.setDisabledTextColor(Color.BLACK);
    	remarks_txt.setDisabledTextColor(Color.BLACK);
    	mrpType_txt.setDisabledTextColor(Color.BLACK);
    
       JScrollPane addressScroll = new JScrollPane(address_txt);
       JScrollPane remarksScroll = new JScrollPane(remarks_txt);
         
       formPanel.add(l8);
       formPanel.add(addressScroll);
       formPanel.add(l9);
       formPanel.add(remarksScroll);
    
        

        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
    
        JLabel jLogo = new JLabel();
        ImageIcon icon = new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.CustItemMappingLogoPath));
	    Image newimg = icon.getImage().getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH ) ;  
	    icon = new ImageIcon( newimg );
        jLogo.setIcon(icon);

        cgpanel.add(jLogo,BorderLayout.WEST);
        cgpanel.add(formPanel , BorderLayout.CENTER);
        cgpanel.add(buttonPanel,BorderLayout.SOUTH);
        
       
        
        
        // set Form Layout	
		
        BorderLayout bl = new BorderLayout(1,1);
		getContentPane().setLayout(bl);
		
        getContentPane().add(cgpanel,BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(table),BorderLayout.CENTER);
        getContentPane().add(status_label,BorderLayout.SOUTH);
        formClear();
        
 	}
	
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("Save")){
			try{
				if (table.getDefaultEditor(Object.class).stopCellEditing()==false){
					table.requestFocus();
					return;
				}
				if (cID_txt.getText().trim().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please select Customer using search option");
					return;
				}  else if (table.getRowCount()==1){
						if (table.getValueAt(0, 1).toString().length()==0){
							JOptionPane.showInternalMessageDialog(this, "Please select the Items for selected customer");
							table.requestFocus();
							return;
						}
				}  
				
				List<CustomerItemMapping> cimList = new ArrayList<>();
				Customer cust = new Customer();
				cust.setCustID(Integer.parseInt(cID_txt.getText()));
				for (int i=0;i<table.getRowCount();i++){
					if (table.getValueAt(i,1).toString().length()>0){
						Item item = new Item();
						item.setItemID(Integer.parseInt(table.getValueAt(i, 1).toString()));
						CustomerItemMapping cim = new CustomerItemMapping();
						if (table.getValueAt(i, 0)!=null){
							if (table.getValueAt(i, 0).toString().length()>0)
								cim.setCimID(Integer.parseInt(table.getValueAt(i, 0).toString())); 
						}
						if (table.getValueAt(i, 5)!=null){
							if (table.getValueAt(i, 5).toString().length()>0)
								cim.setMarginPercent(Float.parseFloat(table.getValueAt(i, 5).toString())); 
						}
						cim.setCustMapItem(item);
						cim.setMapCustomer(cust);
						cimList.add(cim);
					}
				}
				
				CustomerItemMappingDAO cimDAO = new CustomerItemMappingDAO();
		        if (cimDAO.addUpdateCustomerItemMappings(cimList) ==true){
			        status_label.setText("Customer Items added/updated successfully!");
		        }
			}catch(Exception ee){
				log.error("Unable to add/update Customer Item Mapping:"+ee.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to add the '" + cName_txt.getText() + "' Customer");
			}
		}  else if (actionEvent.getActionCommand().equals("Reset")){
			formClear();
		} 	else if (actionEvent.getActionCommand().equals("Search")){
			try{
				SearchForm searchFrm = new SearchForm("Customer List", null,POReaderParams.Customer);
				searchFrm.setVisible(true);
				if (searchFrm.getReturnID()>0) {
					formClear();
					CustomerDAO cDAO = new CustomerDAO();
					Customer cust = cDAO.getCustomer(searchFrm.getReturnID());
					
					cID_txt.setText(cust.getCustID() + "");
					cName_txt.setText(cust.getCustomerName());
					CustomerGroup cg = cust.getCustGroup();
					if (cg!=null){
						cgID = cg.getCgID();
						cgName_txt.setText(cg.getCgName());
					}
					MRPType mrpType = cust.getCustMRP();
					if (mrpType!=null){
						mrpTypeID = mrpType.getMrpTID();
						mrpType_txt.setText(mrpType.getMrpTName());
					}
					
					mobile_txt.setText(cust.getMobile());
					email_txt.setText(cust.getEmail());
					address_txt.setText(cust.getAddress());
					remarks_txt.setText(cust.getRemarks());
					searchFrm.dispose();
					searchFrm =null;
					
					CustomerItemMappingDAO cimDAO = new CustomerItemMappingDAO();
					
					List<Object[]> cimList = cimDAO.getAllCustItemMappings(cust);
					
					if (cimList!=null){
						if (cimList.size()>0){
							tModel.setRowCount(0);
						}
						for (int i=0;i<cimList.size();i++){
							Object[] cim = cimList.get(i);
							tModel.addRow(new Object[] {cim[0] , cim[1] ,cim[2] ,cim[3] ,cim[4]== null ? "":cim[4],cim[5]== null ? "":cim[5]} ); 
						}
					}
				}
				this.repaint();
				this.revalidate();
			}catch(Exception e){
				log.error("Unable to fetch Customer Item Mapping:"+e.getMessage());
			}
		} else if (actionEvent.getActionCommand().equals("Email")){
			sendEmail();
		}
	}



	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource().equals(table))
		{
			if (e.getKeyCode() == KeyEvent.VK_F3 || e.getKeyCode() == KeyEvent.VK_INSERT){
				tModel.addRow(new Object[] { "" , "","","","","" } ); 
			} 
			else if (e.getKeyCode() == KeyEvent.VK_F4 || e.getKeyCode() == KeyEvent.VK_DELETE)
			{
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) 
				{
					if (JOptionPane.showInternalConfirmDialog(this, "Do you want to delete the Item Name - '" + tModel.getValueAt(selectedRow, 2).toString()  + "'?","Confirmation", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION) 
					{
						if (tModel.getValueAt(selectedRow, 0).toString().trim().length()>0)
						{
							int cimID = Integer.parseInt(tModel.getValueAt(selectedRow, 0).toString());  
							if (cimID>0){
								CustomerItemMappingDAO cimDAO = new CustomerItemMappingDAO();
								try {
									String itemName =  tModel.getValueAt(selectedRow, 2).toString();
									CustomerItemMapping cim = new CustomerItemMapping();
									cim.setCimID(cimID);
									if (cimDAO.deleteCIM(cim)==true) {
										tModel.removeRow(selectedRow);
										status_label.setText("Item  '" + itemName + "' deleted successfully!");
									}
								} catch(Exception ee){		
									log.error("Unable to delete Customer Item Mapping:"+ee.getMessage());
								}
							}
						}
						else 
						{
							tModel.removeRow(selectedRow);
						}
						if (tModel.getRowCount()==0){
							tModel.addRow(new Object[] { "" , "","","" ,"",""} ); 
						}
					}
				}
			}
			else if (table.getSelectedColumn()<5 &&  e.getKeyCode() == KeyEvent.VK_F2)
			{
					String[]  colNames = new String[3];
					colNames[0] = "ID"; colNames[1] ="Item Name"; colNames[2] = "EAN Code";
			    	SearchForm sfrm = new SearchForm("Item List", colNames, POReaderParams.Item);
			    	sfrm.setVisible(true);
					if (sfrm.getReturnID()>0) {
						try{
							if (cID_txt.getText().trim().length()==0){
								JOptionPane.showInternalMessageDialog(this, "Please select Customer using search option");
								return;
							} 
							if (isItemAvailable(sfrm.getReturnID() + "")==true){
								return;
							}
							ItemDAO iDAO = new ItemDAO();
							Item item = iDAO.getItem(sfrm.getReturnID() );   
							table.setValueAt(item.getItemID() + "",table.getSelectedRow(),1);
							table.setValueAt(item.getItemName() + "",table.getSelectedRow(),2);
							table.setValueAt(item.getEanCode() + "",table.getSelectedRow(),3);
							CustomerGroup cg = new CustomerGroup();
							cg.setCgID(cgID);
							
							CustomerArticleCode  cac = new CustomerArticleCode();
							cac.setCustItemGroup(cg); cac.setCustItem(item);
			
							CustomerArticleCodeDAO caDAO = new CustomerArticleCodeDAO();
							cac = caDAO.getCustomerArticleCode(cac);
							table.setValueAt("" ,table.getSelectedRow(),4);
							if (cac!=null){
								table.setValueAt(cac.getArticleCode() ,table.getSelectedRow(),4);
							}
						}catch(Exception ee){
							log.error("Unable to add Customer Item Mapping:"+ee.getMessage());
						}
	
					}
					sfrm.dispose();
					this.repaint();
					this.revalidate();
			}
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void formClear(){
		cID_txt.setText("");
		cName_txt.setText("");
		cgID = 0;
		cgName_txt.setText("");
		mrpTypeID = 0;
		mrpType_txt.setText("");
		mobile_txt.setText("");
		email_txt.setText("");
		address_txt.setText("");
		remarks_txt.setText("");
		status_label.setText(" ");
		tModel.setNumRows(0);
		tModel.addRow(new Object[] { "" , "","", "" ,""} );
	}
	
	private boolean isItemAvailable(String searchText){
		boolean status = false;
		try{
		 for (int row = 0; row <= table.getRowCount() - 1; row++) {
			 	String rowVal = table.getValueAt(row, 1).toString().toLowerCase(); 
                if ( rowVal.equalsIgnoreCase(searchText.toLowerCase())) {
                    // this will automatically set the view of the scroll in the location of the value
                    table.scrollRectToVisible(table.getCellRect(row, 0, true));

                    // this will automatically set the focus of the searched/selected row/value
                    table.setRowSelectionInterval(row, row);
                    status=true;
                    break;

                    //for (int i = 0; i <= table.getColumnCount() - 1; i++) {

                    //    table.getColumnModel().getColumn(i).setCellRenderer(new HighlightRenderer());
                    //}
                }
        }
		}catch(Exception e){
			log.error("Unable to find Item Mapping:"+e.getMessage());
		}
		 return status;
	}
	
	
	private void sendEmail(){
		Email email=new Email();
		try{
			if (cID_txt.getText().length()==0){
				JOptionPane.showMessageDialog(this, "Please Select Customer Details!");
				return;
			}
			email.setFrom(EmailProperties.auth);
			email.setTo(email_txt.getText());
	    	String htmlMsg = "<html> <body>";
	    	htmlMsg = htmlMsg + "Dear Sir/Madam, <br>";
	    	htmlMsg = htmlMsg + "Please find the below table with Item Details <br>";
	    	htmlMsg = htmlMsg + POUtil.buildEmailMessageTable(table,2) + "<br> Thank you in advance <br><br>";
	    	htmlMsg = htmlMsg + "Regards<br>Takkaiah and Co. <br> </body></html>";
	    	email.setMsg_body(htmlMsg);
	    	email.setSubject("Takkaiah & Co - Item Details");
	    	EmailService es =new EmailService();
	    	es.sendEmail(email);
	    	JOptionPane.showMessageDialog(this, "Email Successfully sent To :" + email.getTo()) ;
		}catch(Exception e){
			log.error("Unable to send Email :"+e.getMessage());
	    	JOptionPane.showMessageDialog(this, "Unable to send Email To :" + email.getTo()) ;
		}
	}
	
}
