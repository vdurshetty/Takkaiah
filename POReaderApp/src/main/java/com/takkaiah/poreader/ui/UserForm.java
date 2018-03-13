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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import com.takkaiah.db.dao.UserDAO;
import com.takkaiah.db.dto.User;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.security.CipherTextInfo;

public class UserForm extends JInternalFrame implements ActionListener, MouseListener, KeyListener{

	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(UserForm.class.getName());
	
	UserDAO uDAO = new UserDAO();
	
	JTable table;
	JButton  bt_add = new JButton("Add");
	JButton  bt_update = new JButton("Update");
	JButton  bt_delete = new JButton("Delete");
	JButton  bt_reset = new JButton("Reset");
	JButton  bt_reset_pwd = new JButton("Reset Password");
    
	JTextField  uID_txt = new JTextField();
	JTextField  userName_txt = new JTextField();
	String user_pwd ="";
	JTextField  fullName_txt = new JTextField();
	JTextField  empid_txt = new JTextField();
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
	
	public UserForm() {
		addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
            	//dispose();
            	setVisible(false);
            }
        }); 
		setTitle("Users Form");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(false);
		setClosable(true);
		setBounds(100, 100, 750, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table=new JTable(tModel);
		table.setAutoCreateRowSorter(true);
	    tModel.addColumn("User ID"); tModel.addColumn("User Name");  tModel.addColumn("Employee Name"); tModel.addColumn("EmpID"); tModel.addColumn("pwd"); 
	  
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        
        table.getColumnModel().getColumn(4).setWidth(0);
	    table.getColumnModel().getColumn(4).setMinWidth(0);
	    table.getColumnModel().getColumn(4).setMaxWidth(0); 

        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        //table.getTableHeader().setForeground(Color.blue);

        
        // Interchange columns in table
        table.getTableHeader().setReorderingAllowed(false);
        
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(this);
        table.addKeyListener(this);
        table.getTableHeader().setForeground(Color.blue);

        
        
        uID_txt.setEnabled(false);
        status_label.setForeground(Color.BLUE);
        
        JPanel formPanel = new JPanel();
        GridLayout gl = new GridLayout(2, 4);
        formPanel.setLayout(gl);

        JPanel panel2 = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
        panel2.setLayout(fl);
        
        panel2.add(bt_add);
        panel2.add(bt_update);
        panel2.add(bt_delete);
        panel2.add(bt_reset);
        panel2.add(bt_reset_pwd);
        
        bt_add.addActionListener(this);
        bt_update.addActionListener(this);
        bt_delete.addActionListener(this);
        bt_reset.addActionListener(this);
        bt_reset_pwd.addActionListener(this);
        bt_add.setMnemonic(KeyEvent.VK_A);
        bt_add.setToolTipText("Add User Details");
        bt_add.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoAdd)));
        bt_update.setMnemonic(KeyEvent.VK_U);
        bt_update.setToolTipText("Update User Details");
        bt_update.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoUpdate)));
        bt_delete.setMnemonic(KeyEvent.VK_D);
        bt_delete.setToolTipText("Delete User Details");
        bt_delete.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoDelete)));
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));
        bt_reset_pwd.setMnemonic(KeyEvent.VK_P);
        bt_reset_pwd.setToolTipText("Reset User Password");
        bt_reset_pwd.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoResetPwd)));
        
        JLabel l1 = new JLabel("  User ID  ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("  User Name  ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("  Employee Name  ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l4 = new JLabel("  EmpID  ");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);

        formPanel.add(l1);
        formPanel.add(uID_txt);
        formPanel.add(l2);
        formPanel.add(userName_txt);
        formPanel.add(l3);
        formPanel.add(fullName_txt);
        formPanel.add(l4);
        formPanel.add(empid_txt);
        
        userName_txt.setToolTipText("Enter User Name");
        fullName_txt.setToolTipText("Enter User Full Name");
        empid_txt.setToolTipText("Enter User Employee ID");
        table.setToolTipText("Press F2 to Fetch Select User Details");
          
        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
        
        JLabel jLogo = new JLabel();
        
	      ImageIcon icon = new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.UserMgmtLogoPath ));
	      Image img = icon.getImage() ;  
	      Image newimg = img.getScaledInstance( 60, 60,  java.awt.Image.SCALE_SMOOTH ) ;  
	      icon = new ImageIcon( newimg );

        jLogo.setIcon( icon);

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
				if (userName_txt.getText().trim().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please Enter User Name");
					userName_txt.requestFocus();
					return;
				}
				User user = new User();
				user.setUserName(userName_txt.getText());
				user.setPwd(CipherTextInfo.encrypt("password"));
				user.setFullName(fullName_txt.getText());
				user.setEmpid(empid_txt.getText());
				user.setPwd("pwd");
		        if (uDAO.addUser(user)==true){
			        status_label.setText("User '" + userName_txt.getText() + "' added successfully!");
			        fillTable();
			        buttonEnable(POReaderParams.ButtonEnableForAdd);
		        }
			}catch(PrimaryKeyException pke){
				JOptionPane.showInternalMessageDialog(this, "User Already Exists with the name - '" + userName_txt.getText() + "'","Alert!",JOptionPane.ERROR_MESSAGE) ;
				userName_txt.requestFocus();
			}catch(Exception ee){
				log.error("Unable to add User Deails:"+ ee.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to add the '" + userName_txt.getText() + "' User ","Alert!",JOptionPane.ERROR_MESSAGE) ;
			}
		}
		else if(actionEvent.getActionCommand().equals("Update")){
			try{
					if (uID_txt.getText().trim().length()==0){
						JOptionPane.showInternalMessageDialog(this, "Please select the User Details from the below table.");
						table.requestFocus();
						return;
					} else if (uID_txt.getText().equals("1")){
						JOptionPane.showInternalMessageDialog(this, "Admin Details cannot be updated!");
						table.requestFocus();
						return;
					} 
					
					User user = new User();
					user.setUid(Integer.parseInt( uID_txt.getText()));
					user.setUserName(userName_txt.getText());
					user.setPwd(user_pwd);
					user.setFullName(fullName_txt.getText());
					user.setEmpid(empid_txt.getText());
			        if (uDAO.updateUser(user)==true){
				        status_label.setText("User '" + userName_txt.getText() + "' updated successfully!");
				        fillTable();
				        buttonEnable(POReaderParams.ButtonEnableForUpdate);
			        }
			}catch(Exception e){
				log.error("Unable to update User Deails:"+ e.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to update the '" + userName_txt.getText() + "' User","Alert!",JOptionPane.ERROR_MESSAGE) ;
			}	
		}
		else if (actionEvent.getActionCommand().equals("Reset")){
			formClear();
			
		} else if (actionEvent.getActionCommand().equals("Reset Password")){
			
			user_pwd= JOptionPane.showInternalInputDialog(this, "Please enter reset password!");
			try{
				user_pwd = CipherTextInfo.encrypt(user_pwd);
				status_label.setText("Password Successfully Reset! Click update to save user details");
			}catch(Exception er){
				log.error("Unable to reset user password:"+ er.getMessage(),"Alert!",JOptionPane.ERROR_MESSAGE) ;
			}
			//buttonEnable(FormSearchPattern.ButtonEnableForReset);
		}
		else if (actionEvent.getActionCommand().equals("Delete")){
				User user = new User();
				if (uID_txt.getText().trim().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please Select the User Details from the below table.");
					table.requestFocus();
					return;
				} 
				if (uID_txt.getText().length()>0)
				{
						String userName = userName_txt.getText();
						if (JOptionPane.showInternalConfirmDialog(this, "Do you want to delete the User - '" + userName + "'?","Confirmation", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION) 
						{
							user.setUid( Integer.parseInt(uID_txt.getText()));
							try{
						        if (uDAO.deleteUser(user)==true){
						        	formClear();
							        fillTable();
							        status_label.setText("User '" + userName + "' deleted successfully!");
							        buttonEnable(POReaderParams.ButtonEnableForDelete);
						        }
							}catch(Exception e){
								log.error("Unable to delete User Deails:"+ e.getMessage());
								JOptionPane.showInternalMessageDialog(this, "Unable to delete the '" + userName + "' User ","Alert!",JOptionPane.ERROR_MESSAGE) ;
							}	
						}
			}
		}		
	}
	
	

	@Override
	public void keyPressed(KeyEvent ke) {
	    if (ke.getKeyCode() == KeyEvent.VK_F2) {
	        int rowNum = table.getSelectedRow();
	        formClear();
	        if (table.getValueAt(rowNum, 0)!=null)
	        	uID_txt.setText( table.getValueAt(rowNum, 0).toString());
	        if (table.getValueAt(rowNum, 1)!=null)
	        	userName_txt.setText( table.getValueAt(rowNum, 1).toString());
	        if (table.getValueAt(rowNum, 2)!=null)
	        	fullName_txt.setText( table.getValueAt(rowNum, 2).toString());
	        if (table.getValueAt(rowNum, 3)!=null)
	        	empid_txt.setText( table.getValueAt(rowNum, 3).toString());
	        if (table.getValueAt(rowNum, 4)!=null)
	        	user_pwd = table.getValueAt(rowNum, 4).toString();
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
	        	int rowNum = table.getSelectedRow();
	        	formClear();
		        uID_txt.setText( table.getValueAt(rowNum, 0).toString());
		    	userName_txt.setText( table.getValueAt(rowNum, 1).toString());
		    	if (table.getValueAt(rowNum, 2)!=null){
		    		fullName_txt.setText( table.getValueAt(rowNum, 2).toString());
		    	}
		    	if (table.getValueAt(rowNum, 3)!=null){
		    		empid_txt.setText( table.getValueAt(rowNum, 3).toString());
		    	}
		    	if (table.getValueAt(rowNum, 4)!=null){
		    		user_pwd = table.getValueAt(rowNum, 4).toString();
		    	}
			    buttonEnable(POReaderParams.ButtonEnableForSearch);

	        }		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	// User Defined Methods
		private void fillTable(){
	      tModel.setNumRows(0);
	        try{
	        	 List<User> uList = uDAO.getAllUsers();
	        	if (uList!=null) {
					for(int i=0;i<uList.size();i++) {
						User row = uList.get(i);
						tModel.addRow(new Object[] { row.getUid() , row.getUserName(),row.getFullName() , 
								row.getEmpid(), row.getPwd() });
					}
	        	} 
	        }catch(Exception er)
	        {      log.error("Unable to fetch User Deails:"+ er.getMessage()); }
		}
		
		public void formClear(){
			uID_txt.setText("");
			userName_txt.setText("");
			fullName_txt.setText("");
			empid_txt.setText("");
			user_pwd="";
			status_label.setText(" ");
			buttonEnable(POReaderParams.ButtonEnableForReset);

		}
		
		private void buttonEnable(int enableType){
			
			if (enableType == POReaderParams.ButtonEnableForAdd){
				bt_add.setEnabled(true);
				bt_update.setEnabled(false);
				bt_delete.setEnabled(false);
				bt_reset_pwd.setEnabled(false);
				bt_reset.setEnabled(true);
			} else if (enableType == POReaderParams.ButtonEnableForUpdate){
				bt_add.setEnabled(true);
				bt_update.setEnabled(false);
				bt_delete.setEnabled(false);
				bt_reset_pwd.setEnabled(false);
				bt_reset.setEnabled(true);
			} else if (enableType == POReaderParams.ButtonEnableForDelete){
				bt_add.setEnabled(true);
				bt_update.setEnabled(false);
				bt_delete.setEnabled(false);
				bt_reset_pwd.setEnabled(false);
				bt_reset.setEnabled(true);
			} else if (enableType == POReaderParams.ButtonEnableForReset){
				bt_add.setEnabled(true);
				bt_update.setEnabled(false);
				bt_delete.setEnabled(false);
				bt_reset_pwd.setEnabled(false);
				bt_reset.setEnabled(true);
			} else if (enableType == POReaderParams.ButtonEnableForSearch){
				bt_add.setEnabled(false);
				bt_update.setEnabled(true);
				bt_delete.setEnabled(true);
				bt_reset_pwd.setEnabled(true);
				bt_reset.setEnabled(true);
			}

		}
}
