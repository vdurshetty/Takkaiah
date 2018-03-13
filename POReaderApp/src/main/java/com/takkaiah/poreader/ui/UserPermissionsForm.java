package com.takkaiah.poreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.takkaiah.db.dao.UserDAO;
import com.takkaiah.db.dto.KeyValueMaster;
import com.takkaiah.db.dto.PORAFunctionalities;
import com.takkaiah.db.dto.User;
import com.takkaiah.db.dto.UserPermissions;
import com.takkaiah.db.exception.AddException;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.poreader.util.SearchForm;

public class UserPermissionsForm extends JInternalFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(UserPermissionsForm.class.getName());
	
	JList<KeyValueMaster> list_permissions = new JList<>();
	DefaultListModel<KeyValueMaster> dlm_permissions = new DefaultListModel<>();
	JList<KeyValueMaster> list_userPermissions = new JList<>();
	DefaultListModel<KeyValueMaster> dlm_userPermissions = new DefaultListModel<>();
	
	JButton bt_addOne = new JButton(">");
	JButton bt_removeOne = new JButton("<");
	JButton bt_addMultiple = new JButton(">>");
	JButton bt_removeMultiple = new JButton("<<");
	JButton bt_search = new JButton("Search");
	JButton bt_reset = new JButton("Reset");
	JButton bt_save = new JButton("Save");
	
	JTextField  txt_userId = new JTextField();
	JTextField  txt_userName = new JTextField();
	JTextField  txt_fullName = new JTextField();
	JTextField  txt_empid = new JTextField();
	JLabel  status_label = new JLabel(" ");

	
	
	
	
	public UserPermissionsForm() {
		addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
            	//dispose();
            	setVisible(false);
            }
        }); 
		setTitle("User Permissions Form");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(false);
		setClosable(true);
		setBounds(100, 100, 590, 370);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	  
	    status_label.setForeground(Color.BLUE);

		JPanel formPanel = new JPanel();
	    GridLayout gl1 = new GridLayout(2, 4);
	    formPanel.setLayout(gl1);
	    
	    JLabel l1 = new JLabel("User ID   ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("User Name   ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("Employee Name   ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l4 = new JLabel("Employee ID   ");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);
        
        formPanel.add(l1);
        formPanel.add(txt_userId);
        formPanel.add(l2);
        formPanel.add(txt_userName);
        formPanel.add(l3);
        formPanel.add(txt_fullName);
        formPanel.add(l4);
        formPanel.add(txt_empid);
        
    	txt_userId.setEnabled(false);
		txt_userName.setEnabled(false);
		txt_fullName.setEnabled(false);
		txt_empid.setEnabled(false);
		
	  
	    JPanel buttonPanel = new JPanel();
	    FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
	    buttonPanel.setLayout(fl);
	    buttonPanel.add(bt_save);
	    buttonPanel.add(bt_search);
	    buttonPanel.add(bt_reset);
	    
	    bt_reset.addActionListener(this);
	    bt_search.addActionListener(this);
	    bt_save.addActionListener(this);
        bt_save.setMnemonic(KeyEvent.VK_S);
        bt_save.setToolTipText("Click to Save User Permissions");
        bt_save.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoSave)));
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setToolTipText("Click to Clear Form Details");
        bt_reset.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));
        bt_search.setMnemonic(KeyEvent.VK_E);
        bt_search.setToolTipText("Click to Fetch User Details");
        bt_search.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoSearch)));

	    
	    
	    JPanel userFormPanel = new JPanel();
	    userFormPanel.setLayout(new BorderLayout(2,2));
	    
	    userFormPanel.add(formPanel, BorderLayout.CENTER);
	    userFormPanel.add(buttonPanel, BorderLayout.SOUTH);
	    
	    
	    JPanel innerButtonPanel = new JPanel();
	    //fl1.setAlignOnBaseline(true);
	    innerButtonPanel.setLayout(new GridLayout(4,1,40,10));
	    
	    innerButtonPanel.add(bt_addOne);
	    innerButtonPanel.add(bt_removeOne);
	    innerButtonPanel.add(bt_addMultiple);
	    innerButtonPanel.add(bt_removeMultiple);
	    bt_addOne.addActionListener(this);
	    bt_addOne.setToolTipText("Add Selected Form Permissions to User");
	    bt_removeOne.addActionListener(this);
	    bt_removeOne.setToolTipText("Remove Selected User Permissios");
	    bt_addMultiple.addActionListener(this);
	    bt_addMultiple.setToolTipText("Add All Permission to User");
	    bt_removeMultiple.addActionListener(this);
	    bt_removeMultiple.setToolTipText("Remove All User Permissions");
	    
		
		JPanel cgpanel = new JPanel();
	    cgpanel.setLayout(new BorderLayout(10,10));
	      
	    BorderLayout bl = new BorderLayout(1,1);
	   	getContentPane().setLayout(bl);
	   	
	   	list_permissions.setModel(dlm_permissions);
	   	list_userPermissions.setModel(dlm_userPermissions);
	    fillAllPermission();

	    getContentPane().add(userFormPanel,BorderLayout.NORTH);
	    getContentPane().add(new JScrollPane(list_permissions),BorderLayout.WEST);
	    getContentPane().add(innerButtonPanel,BorderLayout.CENTER);
	    getContentPane().add(new JScrollPane(list_userPermissions),BorderLayout.EAST);
	    getContentPane().add(status_label,BorderLayout.SOUTH);
	}


	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("Save")){
			if (txt_userId.getText().trim().length()==0){
				JOptionPane.showInternalMessageDialog(this, "Please Select User Details");
				txt_userId.requestFocus();
				return;
			} /*else if (dlm_userPermissions.getSize()==0){
				JOptionPane.showInternalMessageDialog(this, "Please Select The User Permissions!");
			    list_userPermissions.requestFocus();
				return;
			} */
			else if (txt_userId.getText().equals("1")){
				status_label.setText("User Permissions Cannot be changed for Administrator!");
				return;
			}
			List<UserPermissions> poraList = new ArrayList<>();
			User user = new User();
			user.setUid(Integer.parseInt(txt_userId.getText()));
			for (int i=0;i<dlm_userPermissions.size();i++){
				UserPermissions ups = new UserPermissions();
				KeyValueMaster val = dlm_userPermissions.get(i);
				PORAFunctionalities pora = new PORAFunctionalities();
				pora.setfID(val.getId());
			    ups.setpUser(user);
			    ups.setpFunc(pora);
			    poraList.add(ups);
			}
			user.setUserPermissions(poraList);
			try{
				UserDAO uDAO = new UserDAO();
				uDAO.addUserPermissions(user);
				status_label.setText("User Permissions successfully added!");
			}catch(AddException ex){
				log.error("Unable to add user permissions: " +ex.getMessage());
			}
			
		}else if (actionEvent.getActionCommand().equals("Reset")){
			formClear();
		}else if (actionEvent.getActionCommand().equals("Search")){
			String[]  colNames = new String[3];
			colNames[0] = "ID"; colNames[1] ="User Name"; colNames[2] = "Employee Name";
			SearchForm searchFrm = new SearchForm("User List",colNames,POReaderParams.Users);
			searchFrm.setVisible(true);
			if (searchFrm.getReturnID()>0) {
				formClear();
				UserDAO uDAO = new UserDAO();
				User user = null;
				try{
					user = uDAO.getUser(searchFrm.getReturnID());
					if (user!=null){
						txt_userId.setText(user.getUid()+"");
						txt_userName.setText(user.getUserName());
						txt_fullName.setText(user.getFullName());
						txt_empid.setText(user.getEmpid());
						status_label.setText(" ");
						fillUserPermissions(uDAO.getUserPermissions(user.getUid()));
					}
				}catch(Exception e){
					log.error("Unable to fetch user permissions : " +e.getMessage());
				}
			}
		}else if (actionEvent.getActionCommand().equals(">")){
			int selectedItems[] = list_permissions.getSelectedIndices();
			for (int i=selectedItems.length-1;i>=0;i--){
				KeyValueMaster val = dlm_permissions.get(selectedItems[i] );
				dlm_userPermissions.addElement(val);
				dlm_permissions.removeElement(val);
			}
		}else if (actionEvent.getActionCommand().equals("<")){
			int selectedItems[] = list_userPermissions.getSelectedIndices();
			for (int i=selectedItems.length-1;i>=0;i--){
				KeyValueMaster val = dlm_userPermissions.get(selectedItems[i] );
				dlm_permissions.addElement(val);
				dlm_userPermissions.removeElement(val);
			}
		}else if (actionEvent.getActionCommand().equals(">>")){
			for (int i=dlm_permissions.size()-1 ;i>=0;i--){
				KeyValueMaster val = dlm_permissions.get(i);
				dlm_userPermissions.addElement(val);
				dlm_permissions.removeElement(val);
			}
		}else if (actionEvent.getActionCommand().equals("<<")){
			for (int i=dlm_userPermissions.size()-1 ;i>=0;i--){
				KeyValueMaster val = dlm_userPermissions.get(i);
				dlm_permissions.addElement(val);
				dlm_userPermissions.removeElement(val);
			}

		}
		
	}
	
	
	public void formClear(){
		txt_userId.setText("");
		txt_userName.setText("");
		txt_fullName.setText("");
		txt_empid.setText("");
		dlm_permissions.removeAllElements();
		dlm_userPermissions.removeAllElements();
		fillAllPermission();
	}
	
	
	private void fillAllPermission(){
		try{
			UserDAO uDAO = new UserDAO();
			Vector<KeyValueMaster> items = uDAO.getAllFunctionalities();
        	if (items.size()>0) {
        		for (int i=0;i<items.size();i++){
        			dlm_permissions.addElement(items.get(i));
        		}
        	}
		}catch(Exception er){
			log.error("Unable to validate user permissions details: " +er.getMessage());
		}
	}

	private void fillUserPermissions(List<Object[]> permissions){
		for (int i=0;i<permissions.size();i++){
			Object[] userPermission = permissions.get(i);
			for (int j=0;j<dlm_permissions.size();j++){
				if (dlm_permissions.getElementAt(j).getId()==Integer.parseInt(userPermission[1]+"")) {
					dlm_userPermissions.addElement(dlm_permissions.get(j));
					dlm_permissions.remove(j);
					break;
				}
			}
		}
	}
}
