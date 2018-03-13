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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import com.takkaiah.db.dao.CustomerGroupDAO;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;

public class CustomerGroupForm extends JInternalFrame  implements ActionListener, MouseListener, KeyListener{
	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(CustomerGroupForm.class.getName());
	CustomerGroupDAO cgDAO = new CustomerGroupDAO();
	JTable table;
	JButton  bt_add = new JButton("Add");
	JButton  bt_update = new JButton("Update");
	JButton  bt_delete = new JButton("Delete");
	JButton  bt_reset = new JButton("Reset");
    
	JTextField  cgID_txt = new JTextField();
	JTextField  cgName_txt = new JTextField();
	JTextField  cgAlias_txt = new JTextField();
	JTextArea  address_txt = new JTextArea();
	JTextArea  remarks_txt = new JTextArea();
	JLabel    status_label = new JLabel(" ");
	
	DefaultTableModel tModel = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
	    	   public boolean isCellEditable(int row, int column) {
	    	       //Only the third column
	    	       //return column == 3;
				return false;
	    	   }
	};
	 
	public CustomerGroupForm() {
		addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
            	//dispose();
            	setVisible(false);
            }
        }); 
		setTitle("Customer Groups");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(false);
		setClosable(true);
		setBounds(100, 100, 750, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table=new JTable(tModel);
		table.setAutoCreateRowSorter(true);
	    tModel.addColumn("CG ID"); tModel.addColumn("Customer Group Name");  tModel.addColumn("Customer Group Alias"); 
	    tModel.addColumn("Address");  tModel.addColumn("Remarks"); 

        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setForeground(Color.BLUE);

        
        // Interchange columns in table
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(this);
        table.addKeyListener(this);
        
        cgID_txt.setEnabled(false);
        cgID_txt.setDisabledTextColor(Color.BLACK);
        status_label.setForeground(Color.BLUE);
        
            
        JPanel formPanel = new JPanel();
        GridLayout gl = new GridLayout(3, 4);
        formPanel.setLayout(gl);

        JPanel panel2 = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
        panel2.setLayout(fl);
        
        panel2.add(bt_add);
        panel2.add(bt_update);
        panel2.add(bt_delete);
        panel2.add(bt_reset);
        
        bt_add.addActionListener(this);
        bt_update.addActionListener(this);
        bt_delete.addActionListener(this);
        bt_reset.addActionListener(this);
        bt_add.setMnemonic(KeyEvent.VK_A);
        bt_add.setToolTipText("Add Customer Group Details");
        bt_add.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoAdd)));
        bt_update.setMnemonic(KeyEvent.VK_U);
        bt_update.setToolTipText("Update Customer Group Details");
        bt_update.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoUpdate)));
        bt_delete.setMnemonic(KeyEvent.VK_D);
        bt_delete.setToolTipText("Delete Customer Group Details");
        bt_delete.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoDelete)));
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setToolTipText("Clear Form Details");
        bt_reset.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));
        

        JLabel l1 = new JLabel("  Customer Group ID  ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("  Customer Group Name  ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("  Customer Group Alias  ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l4 = new JLabel("  Address  ");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l5 = new JLabel("  Remarks  ");
        l5.setHorizontalAlignment(SwingConstants.RIGHT);

        formPanel.add(l1);
        formPanel.add(cgID_txt);
        formPanel.add(l2);
        formPanel.add(cgName_txt);
        formPanel.add(l3);
        formPanel.add(cgAlias_txt);
        
        cgName_txt.setToolTipText("Enter Customer Group Name");
        cgAlias_txt.setToolTipText("Enter Customer Group Alias");
        address_txt.setToolTipText("Enter Customer Group Address");
        remarks_txt.setToolTipText("Enter Custoemr Group Remarks");
        table.setToolTipText("Press F2 or Double to select Customer Group Details");
        
        formPanel.add(l4);
        JScrollPane scroll1 = new JScrollPane(address_txt);
        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        formPanel.add(scroll1);
        formPanel.add(l5);
        JScrollPane scroll2 = new JScrollPane(remarks_txt);
        scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        formPanel.add(scroll2);
        
        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
        
        JLabel jLogo = new JLabel();
        ImageIcon icon = new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.CustomerGrpLogoPath));
	    Image newimg = icon.getImage().getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH ) ;  
	    icon = new ImageIcon( newimg );

        jLogo.setIcon(icon);

        cgpanel.add(jLogo,BorderLayout.WEST);
        cgpanel.add(formPanel , BorderLayout.CENTER);
        cgpanel.add(panel2,BorderLayout.SOUTH);
        

        // set Form Layout	
		
        BorderLayout bl = new BorderLayout(1,1);
		getContentPane().setLayout(bl);
		
        getContentPane().add(cgpanel,BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(table),BorderLayout.CENTER);
        getContentPane().add(status_label,BorderLayout.SOUTH);
        
        fillTable();
        buttonEnable(POReaderParams.ButtonEnableForReset);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("Add")){
			try{
				if (cgName_txt.getText().trim().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please Enter Customer Group Name");
					cgName_txt.requestFocus();
					return;
				}
				CustomerGroup cg = new CustomerGroup();
				cg.setCgName(cgName_txt.getText());
				cg.setCgAlias(cgAlias_txt.getText());
				cg.setAddress(address_txt.getText());
				cg.setRemarks(remarks_txt.getText());
		        if (cgDAO.addCustomerGroup(cg)==true){
			        status_label.setText("Customer Group '" + cgName_txt.getText() + "' added successfully!");
			        fillTable();
			        buttonEnable(POReaderParams.ButtonEnableForAdd);
		        }
			}catch(PrimaryKeyException pke){
				JOptionPane.showInternalMessageDialog(this, "Customer Group Already Exists with the name - '" + cgName_txt.getText() + "'");
				cgName_txt.requestFocus();
			}catch(Exception ee){
				log.error("Unable to add Customer Group :" + ee.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to add the '" + cgName_txt.getText() + "' Customer Group");
			}
		}
		else if(actionEvent.getActionCommand().equals("Update")){
			try{
				CustomerGroup cg = new CustomerGroup();
				if (cgID_txt.getText().trim().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please select the Customer Group from the below table.");
					table.requestFocus();
				} else {
					cg.setCgID(Integer.parseInt(cgID_txt.getText() ));
					cg.setCgName(cgName_txt.getText());
					cg.setCgAlias(cgAlias_txt.getText());
					cg.setAddress(address_txt.getText());
					cg.setRemarks(remarks_txt.getText());
			        if (cgDAO.updateCustomerGroup(cg)==true){
				        status_label.setText("Customer Group '" + cgName_txt.getText() + "' updated successfully!");
				        fillTable();
				        buttonEnable(POReaderParams.ButtonEnableForUpdate);
			        }
				}
			}catch(Exception e){
				log.error("Unable to update Customer Group :" + e.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to update the '" + cgName_txt.getText() + "' Customer Group");
			}	
		}
		else if (actionEvent.getActionCommand().equals("Reset")){
			formClear();
			
		}
		else if (actionEvent.getActionCommand().equals("Delete")){
					CustomerGroup cg = new CustomerGroup();
				if (cgID_txt.getText().trim().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please Select the customer group from the below table.");
					table.requestFocus();
				} 
				else 
				{
					if (cgID_txt.getText().length()>0)
					{
						String customerGroupName = cgName_txt.getText();
						if (JOptionPane.showInternalConfirmDialog(this, "Do you want to delete the Customer Group - '" + customerGroupName + "'?","Confirmation", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION) 
						{
							cg.setCgID(Integer.parseInt(cgID_txt.getText() ));
							try{
						        if (cgDAO.deleteCustomerGroup(cg)==true){
						        	formClear();
							        fillTable();
							        status_label.setText("Customer Group '" + customerGroupName + "' deleted successfully!");
							        buttonEnable(POReaderParams.ButtonEnableForDelete);
						        }
							}catch(Exception e){
								log.error("Unable to delete Customer Group :" + e.getMessage());
								JOptionPane.showInternalMessageDialog(this, "Unable to delete the '" + customerGroupName + "' Customer Group \n It has has the references in Customer..");
							}	
						}
					}
				}
		}
	}


	@Override
	public void keyPressed(KeyEvent ke) {
	    if (ke.getKeyCode() == KeyEvent.VK_F2) {
	      	formClear();
        	int rowNum = table.getSelectedRow();
        	if (table.getValueAt(rowNum, 0)!=null)
        		cgID_txt.setText( table.getValueAt(rowNum, 0).toString());
        	if (table.getValueAt(rowNum, 1)!=null)
        		cgName_txt.setText( table.getValueAt(rowNum, 1).toString());
        	if (table.getValueAt(rowNum, 2)!=null)
        		cgAlias_txt.setText( table.getValueAt(rowNum, 2).toString());
        	if (table.getValueAt(rowNum, 3)!=null)
        		address_txt.setText( table.getValueAt(rowNum, 3).toString());
        	if (table.getValueAt(rowNum, 4)!=null)
        		remarks_txt.setText( table.getValueAt(rowNum, 4).toString());
		    buttonEnable(POReaderParams.ButtonEnableForSearch);
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
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
        if (e.getClickCount() == 2) {
        	formClear();
        	int rowNum = table.getSelectedRow();
        	if (table.getValueAt(rowNum, 0)!=null)
        		cgID_txt.setText( table.getValueAt(rowNum, 0).toString());
        	if (table.getValueAt(rowNum, 1)!=null)
        		cgName_txt.setText( table.getValueAt(rowNum, 1).toString());
        	if (table.getValueAt(rowNum, 2)!=null)
        		cgAlias_txt.setText( table.getValueAt(rowNum, 2).toString());
        	if (table.getValueAt(rowNum, 3)!=null)
        		address_txt.setText( table.getValueAt(rowNum, 3).toString());
        	if (table.getValueAt(rowNum, 4)!=null)
        		remarks_txt.setText( table.getValueAt(rowNum, 4).toString());
		    buttonEnable(POReaderParams.ButtonEnableForSearch);

        }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	// User Defined Methods
	private void fillTable(){
        tModel.setNumRows(0);
        try{
        	 List<CustomerGroup> cgList = cgDAO.getAllCustomerGroups();
        	if (cgList!=null) {
				for(int i=0;i<cgList.size();i++) {
					CustomerGroup row = cgList.get(i);
					tModel.addRow(new Object[] { row.getCgID() , row.getCgName(),row.getCgAlias() , 
							row.getAddress(), row.getRemarks() });
				}
        	} 
        }catch(Exception er)
        {   log.error("Unable to fetch Customer Group :" + er.getMessage());    }
		
	}
	
	public void formClear(){
		cgID_txt.setText("");
		cgName_txt.setText("");
		cgAlias_txt.setText("");
		address_txt.setText("");
		remarks_txt.setText("");
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
