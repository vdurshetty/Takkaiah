package com.takkaiah.poreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.takkaiah.db.dao.CustomerDAO;
import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.MRPType;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.poreader.util.SearchForm;

public class CustomerForm extends JInternalFrame  implements ActionListener,KeyListener{
	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(ChangePwdForm.class.getName());
	
	CustomerDAO cDAO = new CustomerDAO();
	JButton  bt_add = new JButton("Add");
	JButton  bt_update = new JButton("Update");
	JButton  bt_delete = new JButton("Delete");
	JButton  bt_reset = new JButton("Reset");
	JButton  bt_search = new JButton("Search");
    

	int      cgID = 0;
	int      mrpTypeID = 0; 
	JTextField  cID_txt = new JTextField();
	JTextField  cName_txt = new JTextField();
	JTextField  cgName_txt = new JTextField();
	JTextArea  notes_txt = new JTextArea();
	JTextArea  address_txt = new JTextArea();
	JTextField  email_txt = new JTextField();
	JTextField  mobile_txt = new JTextField();
	JTextArea  remarks_txt = new JTextArea();
	JTextField  mrpType_txt = new JTextField();
	JLabel    status_label = new JLabel(" ");
	

	 
	public CustomerForm() {
		addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
            	//dispose();
            	setVisible(false);
            	//pack();
            }
        }); 
		setTitle("Customer Form");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(false);
		setClosable(true);
		setBounds(100, 100, 700, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    cID_txt.setEnabled(false);
	    cID_txt.setDisabledTextColor(Color.BLACK);
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
        cgName_txt.addKeyListener(this);
        mrpType_txt.addKeyListener(this);
        bt_add.setMnemonic(KeyEvent.VK_A);
        bt_add.setToolTipText("Add Customer Details");
        
         
        bt_add.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoAdd)));
        bt_update.setMnemonic(KeyEvent.VK_U);
        bt_update.setToolTipText("Update Customer Details");
        bt_update.setIcon(POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoUpdate)));
        bt_delete.setMnemonic(KeyEvent.VK_D);
        bt_delete.setToolTipText("Delete Customer Details");
        bt_delete.setIcon(POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoDelete)));
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setToolTipText("Clear Form Details");
        bt_reset.setIcon(POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));
        bt_search.setMnemonic(KeyEvent.VK_S);
        bt_search.setToolTipText("Search Customer Details");
        bt_search.setIcon(POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoSearch)));
       
        

        JLabel l1 = new JLabel("  Customer ID  ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("  Customer Name  ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("  Customer Group Name  ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l4 = new JLabel("  MRP Type   ");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l5 = new JLabel();
        
        JLabel l6 = new JLabel("  Email  ");
        l6.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l7 = new JLabel("  Mobile  ");
        l7.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l8 = new JLabel("  Address  ");
        l8.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l9 = new JLabel("  Remarks  ");
        l9.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l10 = new JLabel("  Notes  ");
        l10.setHorizontalAlignment(SwingConstants.RIGHT);

        formPanel.add(l1);
        formPanel.add(cID_txt);
        formPanel.add(l5);
        ImageIcon icon = new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.CustomerGrpLogoPath));
	    Image newimg = icon.getImage().getScaledInstance( 100, 40,  java.awt.Image.SCALE_SMOOTH ) ;  
        formPanel.add(new JLabel(new ImageIcon( newimg)));      
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
        
        cName_txt.setToolTipText("Enter Customer Name");
        cgName_txt.setToolTipText("Press F2 to Select Customer Group");
        mrpType_txt.setToolTipText("Press F2 to Select MRP Type");
        email_txt.setToolTipText("Enter Customer Email Address");
        mobile_txt.setToolTipText("Enter Customer Mobile Number");
        address_txt.setToolTipText("Enter Customer Address");
        remarks_txt.setToolTipText("Enter Remarks");
        
    
       JScrollPane addressScroll = new JScrollPane(address_txt);
       JScrollPane remarksScroll = new JScrollPane(remarks_txt);
       JScrollPane notesScroll = new JScrollPane(notes_txt);
         
       formPanel.add(l8);
       formPanel.add(addressScroll);
       formPanel.add(l9);
       formPanel.add(remarksScroll);
       formPanel.add(l10);
       formPanel.add(notesScroll);
    
        

        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
        
        cgpanel.add(formPanel , BorderLayout.CENTER);
        cgpanel.add(buttonPanel,BorderLayout.SOUTH);
        

        // set Form Layout	
		
        BorderLayout bl = new BorderLayout(1,1);
		getContentPane().setLayout(bl);
		
        getContentPane().add(cgpanel,BorderLayout.CENTER);
        getContentPane().add(status_label,BorderLayout.SOUTH);
        formClear();
        buttonEnable(POReaderParams.ButtonEnableForReset);
        
 	}
	
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("Add")){
			try{
				if (cName_txt.getText().trim().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please Enter Customer Name");
					cName_txt.requestFocus();
					return;
				}  else if (cgName_txt.getText().length()==0){
						JOptionPane.showInternalMessageDialog(this, "Please select Customer Group!");
						cgName_txt.requestFocus();
						return;
				}  else if (mrpType_txt.getText().length() ==0){
					JOptionPane.showInternalMessageDialog(this, "Please select MRP Type!");
					mrpType_txt.requestFocus();
					return;
				} else if (email_txt.getText().trim().length()>0 ) {
					  if(!POUtil.EmailValidator(email_txt.getText().trim())) {
							JOptionPane.showInternalMessageDialog(this, "Please Enter Valid Email Addess!");
							email_txt.requestFocus();
							return;
					  }
				}
				CustomerGroup cg = new CustomerGroup();
				cg.setCgID(cgID);
				MRPType mrpType = new MRPType();
				mrpType.setMrpTID(mrpTypeID);
				Customer cust = new Customer();
				cust.setCustomerName(cName_txt.getText());
				cust.setCustGroup(cg);
				cust.setCustMRP(mrpType);
				cust.setAddress(address_txt.getText());
				cust.setEmail(email_txt.getText());
				cust.setMobile(mobile_txt.getText());
				cust.setNotes( notes_txt.getText());
				cust.setRemarks(remarks_txt.getText());
		        if (cDAO.addCustomer(cust)==true){
			        status_label.setText("Customer '" + cName_txt.getText() + "' added successfully!");
			        buttonEnable(POReaderParams.ButtonEnableForAdd);
		        }
			} catch(PrimaryKeyException pke){
				JOptionPane.showInternalMessageDialog(this, "Customer Already Exists with the name - '" + cName_txt.getText() + "'");
				cName_txt.requestFocus();
			}
			catch(Exception ee){
				log.error("Unable to add customer :" + ee.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to add the '" + cName_txt.getText() + "' Customer");
			}
		}
		else if(actionEvent.getActionCommand().equals("Update")){
				if (cName_txt.getText().trim().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please Enter Customer Name");
					cName_txt.requestFocus();
					return;
				}  else if (cgName_txt.getText().length()==0){
						JOptionPane.showInternalMessageDialog(this, "Please select Customer Group!");
						cgName_txt.requestFocus();
						return;
				}  else if (mrpType_txt.getText().length() ==0){
					JOptionPane.showInternalMessageDialog(this, "Please select MRP Type!");
					mrpType_txt.requestFocus();
					return;
				} 
				CustomerGroup cg = new CustomerGroup();
				try{
					cg.setCgID(cgID);
					MRPType mrpType = new MRPType();
					mrpType.setMrpTID(mrpTypeID);
					Customer cust = new Customer();
					cust.setCustID(Integer.parseInt(cID_txt.getText() ));
					cust.setCustomerName(cName_txt.getText());
					cust.setCustGroup(cg);
					cust.setCustMRP(mrpType);
					cust.setAddress(address_txt.getText());
					cust.setEmail(email_txt.getText());
					cust.setMobile(mobile_txt.getText());
					cust.setNotes( notes_txt.getText());
					cust.setRemarks(remarks_txt.getText());
			        if (cDAO.updateCustomer(cust)==true){
					        status_label.setText("Customer  '" + cName_txt.getText() + "' updated successfully!");
					        buttonEnable(POReaderParams.ButtonEnableForUpdate);
					}
				}catch(Exception e){
					log.error("Unable to update customer :" + e.getMessage());
					JOptionPane.showInternalMessageDialog(this, "Unable to update the '" + cName_txt.getText() + "' Customer Group");
				}	
		}
		else if (actionEvent.getActionCommand().equals("Reset")){
			formClear();
			
		} 	else if (actionEvent.getActionCommand().equals("Search")){
			try{
				SearchForm searchFrm = new SearchForm("Customer List",null, POReaderParams.Customer);
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
					notes_txt.setText(cust.getNotes());
					searchFrm.dispose();
					searchFrm =null;
				}
				this.repaint();
				this.revalidate();
				buttonEnable(POReaderParams.ButtonEnableForSearch);
			}catch(Exception e){
				log.error("Unable to Fetch customer :" + e.getMessage());
			}
		}	else if (actionEvent.getActionCommand().equals("Delete")){
				Customer cust = new Customer();
				if (cID_txt.getText().trim().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please Select the Customer!");
					cName_txt.requestFocus();
				} 
				else 
				{
		        	String customerName = cName_txt.getText();
					if (JOptionPane.showInternalConfirmDialog(this, "Do you want to delete the Customer - '" + customerName + "'?","Confirmation", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION) 
					{
						cust.setCustID(Integer.parseInt(cID_txt.getText() ));
						cust.setCustomerName(cName_txt.getText());
						cust.setAddress(address_txt.getText());
						cust.setRemarks(remarks_txt.getText());
						try{
							if (cDAO.deleteCustomer(cust)==true){
								formClear();
								status_label.setText("Customer '" + customerName + "' deleted successfully!");
								buttonEnable(POReaderParams.ButtonEnableForDelete);
							}
						}catch(Exception e){
							log.error("Unable to delete customer :" + e.getMessage());
							JOptionPane.showInternalMessageDialog(this, "Unable to delete the '" + customerName + "' Customer Details");
						}	
					}
				}
			}
	}



	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource().equals(cgName_txt))
		{
			if (e.getKeyCode() == KeyEvent.VK_F2) {
		    	SearchForm sfrm = new SearchForm("Customer Groups",null,POReaderParams.CustomerGroup);
		    	sfrm.setVisible(true);
				if (sfrm.getReturnID()>0) {
					cgID = sfrm.getReturnID();
					cgName_txt.setText(sfrm.getReturnStr());
				}
				sfrm.dispose();
				this.repaint();
				this.revalidate();
			} 	 
		} 
		else if (e.getSource().equals(mrpType_txt)) 
		{
			if (e.getKeyCode() == KeyEvent.VK_F2) 
			{
		    	SearchForm sfrm = new SearchForm("MRP Type",null, POReaderParams.MRPType);
		    	sfrm.setVisible(true);
				if (sfrm.getReturnID()>0) {
					mrpTypeID = sfrm.getReturnID();
					mrpType_txt.setText(sfrm.getReturnStr());
				}
		    	sfrm.dispose();
				this.repaint();
				this.revalidate();
	    	} 
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource().equals(cgName_txt))
		{
			if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
				cgName_txt.setText("");
				cgID = 0;
			}
		}
		else if (e.getSource().equals(mrpType_txt)) 
		{
			if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
				mrpType_txt.setText("");
				mrpTypeID = 0;
			}
		}

	}


	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource().equals(cgName_txt))
		{
			e.consume();
		} 
		else if (e.getSource().equals(mrpType_txt)) 
		{
			e.consume();
		}
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
		notes_txt.setText("");
		status_label.setText(" ");
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
