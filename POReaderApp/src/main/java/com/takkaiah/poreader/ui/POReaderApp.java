package com.takkaiah.poreader.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.takkaiah.db.dao.UserDAO;
import com.takkaiah.db.dto.User;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.reports.CustomerGroupReport;
import com.takkaiah.poreader.reports.CustomerMasterReport;
import com.takkaiah.poreader.reports.ItemCategoryReport;
import com.takkaiah.poreader.reports.ItemMasterReport;
import com.takkaiah.poreader.reports.ItemPriceReport;
import com.takkaiah.poreader.reports.MRPTypeReport;
import com.takkaiah.poreader.reports.UserListReport;
import com.takkaiah.poreader.reports.UserPermissionsReport;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;


public class POReaderApp implements ActionListener {
	
	static POReaderLogger log = POReaderLogger.getLogger(POReaderApp.class.getName());

	private JFrame frmPoReaderApplication;
	JDesktopPane desktopPane;
	
	private final JMenu mnPoApp = new JMenu("Application");
	JMenuItem menuPwdChange = new JMenuItem("Password Change");
	JMenuItem mntmUser = new JMenuItem("User Management");
	JMenuItem mntmUserPermissions = new JMenuItem("User Permissions");
	JMenuItem menuExit = new JMenuItem("Exit");

	private final JMenu mnAdmin = new JMenu("Master Data");
	JMenuItem mntmCustomerGroup = new JMenuItem("Customer Group");
	JMenuItem mntmCustomer = new JMenuItem("Customer Master");
	JMenuItem mntmMRPtype = new JMenuItem("MRP Type");
	JMenuItem mntmItemCategory = new JMenuItem("Item Category");
	JMenuItem mntmItem = new JMenuItem("Item Master");
	JMenuItem mntmCgArticleCodes = new JMenuItem("Customer Article Codes");
	JMenuItem mntmCustomerItemMapping = new JMenuItem("Customer Item Mapping");

	private final JMenu mnPoReader = new JMenu("Purchase Orders");
	JMenuItem mntmSinglePoReader = new JMenuItem("PO Reader Form");
	JMenuItem mntmCheckPoDetails = new JMenuItem("Manually Check PO Details");
	
	private final JMenu mnReports = new JMenu("Reports");
	JMenuItem mntmUserListReport = new JMenuItem("User List Report");
    JMenuItem mntmUserPermissionsReport = new JMenuItem("User Permissions Report");
	JMenuItem mntmMrpTypeReport = new JMenuItem("MRP Type Report");
	JMenuItem mntmItemMasterReport = new JMenuItem("Item Master Report");
	JMenuItem mntmItemPriceReport = new JMenuItem("Item Price Report");
	JMenuItem mntmItemCategoryReport = new JMenuItem("Item Category Report");
	JMenuItem mntmCustomerGroupReport = new JMenuItem("Customer Group Report");
	JMenuItem mntmCustomerMasterReport = new JMenuItem("Customer Master Report");
	JMenuItem mntmCustomerGroupArticle = new JMenuItem("Customer Group Article Codes Report");
	JMenuItem mntmCustomerItemsReport = new JMenuItem("Customer Items Report");

	
	
	CustomerGroupForm cgform;
	CustomerForm cform;
	MRPTypeForm ptForm;
	ItemCategoryForm icForm;
	CustomerForm cForm;
	ItemMasterForm itemForm;
	CustomerGroupArticlecodesForm cgaCodeForm;
	CustomerItemMappingForm cimForm;
	UserForm uForm;
	ChangePwdForm changePwdForm;
	UserPermissionsForm userPermissions;
	UserLogin login;
	User user ;
	
	CustomerItemsReport ciReport;
	CGArticleCodesReport cgACodeReport;
	
    List<Integer> permissions = new ArrayList<>();
    
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//com.sun.java.swing.plaf.gtk.GTKLookAndFeel
					//com.sun.java.swing.plaf.motif.MotifLookAndFeel
					//com.sun.java.swing.plaf.windows.WindowsLookAndFeel
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
					POReaderApp window = new POReaderApp();
					window.frmPoReaderApplication.setVisible(true);
				} catch (Exception e) {
					log.error("Error stating application"+e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public POReaderApp() {
		login = new UserLogin();
		login.setVisible(true);
		if (login.getUserID()>0){
			UserDAO uDAO = new UserDAO();
			try{
				user = uDAO.getUser(login.getUserID());
				List<Object[]>  userPermissions = uDAO.getUserPermissions(user.getUid());
				for (int i=0;i<userPermissions.size();i++){
					Object[] val = userPermissions.get(i);
					permissions.add(new Integer(val[1]+""));
				}
			}catch(Exception er){
				log.error("Error Validating user details : "+er.getMessage());
			}
		} else {
			System.exit(0);
		}
		login = null;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPoReaderApplication = new JFrame();
		frmPoReaderApplication.setIconImage(Toolkit.getDefaultToolkit().getImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.logoPath )));
		frmPoReaderApplication.setTitle("PO Reader Application - " + user.getFullName());
		frmPoReaderApplication.setBounds(100, 100, 850, 600);
		frmPoReaderApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPoReaderApplication.setLocationRelativeTo(null);
		
		JMenuBar poMenuBar = new JMenuBar();
		frmPoReaderApplication.setJMenuBar(poMenuBar);
		mnPoApp.setMnemonic(KeyEvent.VK_A);
		
		poMenuBar.add(mnPoApp);
		mnPoApp.add(menuPwdChange);
		mnPoApp.add(mntmUser);
		menuPwdChange.setToolTipText("User Password Change");
		mntmUser.setToolTipText("Create or Update User details");
		
		mnPoApp.add(mntmUserPermissions);
		mntmUserPermissions.setToolTipText("Grant/Revoke Access Permissions");
		mnPoApp.addSeparator();
		mnPoApp.add(menuExit);
		menuExit.setToolTipText("Close Application");
		
		menuPwdChange.setMnemonic(KeyEvent.VK_C);
		menuPwdChange.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoChangePwd)));
		menuPwdChange.addActionListener(this);
		mntmUser.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoUser)));
		mntmUser.setMnemonic(KeyEvent.VK_U);
		mntmUser.addActionListener(this);
		mntmUserPermissions.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoUserPermissions)));
		mntmUserPermissions.setMnemonic(KeyEvent.VK_P);
		mntmUserPermissions.addActionListener(this);
	
		menuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuExit.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoExit)));
		menuExit.addActionListener(this);
		
		poMenuBar.add(mnAdmin);	
		mnAdmin.setMnemonic(KeyEvent.VK_M);   
		mnAdmin.add(mntmCustomerGroup);
		mnAdmin.add(mntmItemCategory);
		mnAdmin.add(mntmMRPtype);
		mnAdmin.addSeparator();
		mnAdmin.add(mntmCustomer);
		mnAdmin.add(mntmItem);
		mnAdmin.addSeparator();
		mnAdmin.add(mntmCgArticleCodes);
		mnAdmin.add(mntmCustomerItemMapping);
	
		mntmCustomerGroup.setMnemonic(KeyEvent.VK_G);
		mntmCustomerGroup.setToolTipText("Manage Customer Group");
		mntmCustomerGroup.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoCustGroup)));
		mntmCustomerGroup.addActionListener(this);
		mntmCustomer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		mntmCustomer.setToolTipText("Manage Customers");
		mntmCustomer.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoCustomer)));
		mntmCustomer.addActionListener(this);
		mntmMRPtype.setMnemonic(KeyEvent.VK_M);
		mntmMRPtype.setToolTipText("Manage MRP Types");
		mntmMRPtype.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoMRPType)));
		mntmMRPtype.addActionListener(this);
		mntmItemCategory.setMnemonic(KeyEvent.VK_T);
		mntmItemCategory.setToolTipText("Manage Item Categories");
		mntmItemCategory.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoItemGroup)));
		mntmItemCategory.addActionListener(this);
		mntmItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		mntmItem.setToolTipText("Manage Items");
		mntmItem.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoItem)));
		mntmItem.addActionListener(this);
		mntmCgArticleCodes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		mntmCgArticleCodes.setToolTipText("Add/Update Customer Article Codes");
		mntmCgArticleCodes.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoCustArticleCode)));
		mntmCgArticleCodes.addActionListener(this);
		mntmCustomerItemMapping.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
		mntmCustomerItemMapping.setToolTipText("Add/Update Customer Specific Items");
		mntmCustomerItemMapping.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoCustMapping)));
		mntmCustomerItemMapping.addActionListener(this);

		
		poMenuBar.add(mnPoReader);
		mnPoReader.setMnemonic(KeyEvent.VK_P);
		mnPoReader.add(mntmSinglePoReader);
		mntmSinglePoReader.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		mntmSinglePoReader.setToolTipText("Extract and Validate Purchase Orders");
		mntmSinglePoReader.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoPoReader)));
		

		mnPoReader.add(mntmCheckPoDetails);
		//mntmCheckPoDetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		mntmCheckPoDetails.setToolTipText("Check Purchase Order Details Manually...");
		mntmCheckPoDetails.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuManualPOCheck )));

		
		
		poMenuBar.add(mnReports);
		//mnReports.setMnemonic(KeyEvent.VK_R);
		mntmCustomerItemsReport.setToolTipText("Customer Specific Items With Price Details");
		mntmUserListReport.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoUser)));
		mntmUserListReport.setMnemonic(KeyEvent.VK_L);
		mntmUserPermissionsReport.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoUserPermissions)));
		mntmUserPermissionsReport.setMnemonic(KeyEvent.VK_U);
		mntmMrpTypeReport.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoMRPType)));
		mntmMrpTypeReport.setMnemonic(KeyEvent.VK_T);
		mntmItemCategoryReport.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoItemGroup)));
		mntmItemCategoryReport.setMnemonic(KeyEvent.VK_C);
		mntmCustomerGroupReport.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoCustGroup)));
		mntmCustomerGroupReport.setMnemonic(KeyEvent.VK_G);
		mntmItemMasterReport.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoItem)));
		mntmItemMasterReport.setMnemonic(KeyEvent.VK_I);
		mntmItemPriceReport.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoMRPType)));
		mntmItemPriceReport.setMnemonic(KeyEvent.VK_P);
		mntmCustomerMasterReport.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoCustomer)));
		mntmCustomerMasterReport.setMnemonic(KeyEvent.VK_M);
		mntmCustomerGroupArticle.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoCustArticleCode)));
		mntmCustomerGroupArticle.setMnemonic(KeyEvent.VK_A);
		mntmCustomerItemsReport.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoCustMapping)));
		mntmCustomerItemsReport.setMnemonic(KeyEvent.VK_R);

		
		mntmUserListReport.addActionListener(this);
		mntmUserPermissionsReport.addActionListener(this);
		mntmMrpTypeReport.addActionListener(this);
		mntmItemCategoryReport.addActionListener(this);
		mntmCustomerGroupReport.addActionListener(this);
		mntmItemMasterReport.addActionListener(this);
		mntmItemPriceReport.addActionListener(this);
		mntmCustomerMasterReport.addActionListener(this);
		mntmCustomerGroupArticle.addActionListener(this);
		mntmCustomerItemsReport.addActionListener(this);
		mntmSinglePoReader.addActionListener(this);
		mntmCheckPoDetails.addActionListener(this);
		
		mnReports.add(mntmUserListReport);
		mnReports.add(mntmUserPermissionsReport);
		mnReports.addSeparator();
		mnReports.add(mntmMrpTypeReport);
		mnReports.add(mntmItemCategoryReport);
		mnReports.add(mntmCustomerGroupReport);
		mnReports.addSeparator();
		mnReports.add(mntmItemMasterReport);
		mnReports.add(mntmItemPriceReport);
		mnReports.add(mntmCustomerMasterReport);
		mnReports.addSeparator();
		mnReports.add(mntmCustomerGroupArticle);
		mnReports.add(mntmCustomerItemsReport);
		
		
		ImageIcon icon = new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.logoPath)); 
		JLabel label = new JLabel(icon);
		label.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
		desktopPane = new JDesktopPane();
		frmPoReaderApplication.setContentPane(desktopPane);
		setUserPermissions();
		
		frmPoReaderApplication.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				desktopPane.remove(label);
				int xPos = (desktopPane.getWidth() - label.getWidth()) / 2;
				int yPos = (desktopPane.getHeight() - label.getHeight()) / 2;
				label.setLocation(xPos, yPos);	
				desktopPane.add( label, JLayeredPane.FRAME_CONTENT_LAYER);
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				
			}
		});
	}

	
	private void setUserPermissions(){
		
		menuPwdChange.setEnabled(false);
		mntmUser.setEnabled(false);
		mntmUserPermissions.setEnabled(false);
	
		
		mntmCustomerGroup.setEnabled(false);
		mntmCustomer.setEnabled(false);
		mntmMRPtype.setEnabled(false);
		mntmItemCategory.setEnabled(false);
		mntmItem.setEnabled(false);
		mntmCgArticleCodes.setEnabled(false);
		mntmCustomerItemMapping.setEnabled(false);

		mntmSinglePoReader.setEnabled(false);
		mntmCheckPoDetails.setEnabled(false);
		
		
	    mntmUserListReport.setEnabled(false);
	    mntmUserPermissionsReport.setEnabled(false);
		mntmMrpTypeReport.setEnabled(false);
		mntmItemMasterReport.setEnabled(false);
		mntmItemPriceReport.setEnabled(false);
		mntmItemCategoryReport.setEnabled(false);
		mntmCustomerGroupReport.setEnabled(false);
		mntmCustomerMasterReport.setEnabled(false);
		mntmCustomerGroupArticle.setEnabled(false);
		mntmCustomerItemsReport.setEnabled(false);

		
		for (int i=0;i<permissions.size();i++){
			int val = permissions.get(i).intValue();
			if (POReaderParams.MRPTypeForm == val){
				mntmMRPtype.setEnabled(true);
			} 
			else if (POReaderParams.ItemCategoryForm == val){
				mntmItemCategory.setEnabled(true);
			}
			else if (POReaderParams.CustomerGroupForm == val)
			{
				mntmCustomerGroup.setEnabled(true);
			} else if(POReaderParams.CustomerForm == val){
				mntmCustomer.setEnabled(true);
			}else if(POReaderParams.ItemMasterForm == val){
				mntmItem.setEnabled(true);
			} else if(POReaderParams.CustArticleForm == val){
				mntmCgArticleCodes.setEnabled(true);
			} else if(POReaderParams.CustItemMapForm == val){
				mntmCustomerItemMapping.setEnabled(true);
			} else if(POReaderParams.UserPermissionsForm == val){
				mntmUserPermissions.setEnabled(true);
			} else if(POReaderParams.UserManagementForm == val){
				mntmUser.setEnabled(true);
			} else if(POReaderParams.PasswordChangeForm == val) {
				menuPwdChange.setEnabled(true);
			} else if(POReaderParams.POReaderForm == val){
				mntmSinglePoReader.setEnabled(true);
			}else if(POReaderParams.CheckPODetails  == val){
				mntmCheckPoDetails.setEnabled(true);
			} else if(POReaderParams.UserListReport == val){
			    mntmUserListReport.setEnabled(true);
			}else if(POReaderParams.UserPermissionsReport == val){
			    mntmUserPermissionsReport.setEnabled(true);
			}else if(POReaderParams.MRPTypeReport == val){
				mntmMrpTypeReport.setEnabled(true);
			}else if(POReaderParams.ItemCategoryReport == val){
				mntmItemCategoryReport.setEnabled(true);
			}else if(POReaderParams.CustomerGroupReport == val){
				mntmCustomerGroupReport.setEnabled(true);
			}else if(POReaderParams.ItemMasterReport == val){
				mntmItemMasterReport.setEnabled(true);
			}else if(POReaderParams.ItemPriceReport == val){
				mntmItemPriceReport.setEnabled(true);
			}else if(POReaderParams.CustomerMasterReport == val){
				mntmCustomerMasterReport.setEnabled(true);
			}else if(POReaderParams.CustomerArticleCodesReport == val){
				mntmCustomerGroupArticle.setEnabled(true);
			}else if(POReaderParams.CustomerItemsReport == val){
				mntmCustomerItemsReport.setEnabled(true);
			}
		}
		
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Dimension desktopSize = desktopPane.getSize();
		Dimension jInternalFrameSize;
		if (ae.getActionCommand().equals("Exit") ){
			System.exit(0);
		} else if (ae.getActionCommand().equals("Customer Group")){
			if (cgform==null){
				cgform = new CustomerGroupForm();
				desktopPane.add(cgform);
			}
			jInternalFrameSize = cgform.getSize();
			cgform.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			cgform.setVisible(true);
			cgform.repaint();
			cgform.revalidate();   
			cgform.formClear();
		}else if (ae.getActionCommand().equals("Customer Master")){
			if (cform==null){
				cform = new CustomerForm();
				desktopPane.add(cform);
			}
	
			jInternalFrameSize = cform.getSize();
			cform.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			cform.setVisible(true);
			cform.repaint();
			cform.revalidate();   
			//cForm.formClear();
		}
		else if (ae.getActionCommand().equals("Item Category")){
			if (icForm==null){
				icForm = new ItemCategoryForm();
				desktopPane.add(icForm);
			}
			jInternalFrameSize = icForm.getSize();
			icForm.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			icForm.setVisible(true);
			icForm.repaint();
			icForm.revalidate();   
			icForm.formClear();
		}	
		else if (ae.getActionCommand().equals("MRP Type")){
			if (ptForm==null){
				ptForm = new MRPTypeForm();
				desktopPane.add(ptForm);
			}
			jInternalFrameSize = ptForm.getSize();
			ptForm.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			ptForm.setVisible(true);
			ptForm.repaint();
			ptForm.revalidate();   
			ptForm.formClear();
		}	else if (ae.getActionCommand().equals("Item Master")){
			if (itemForm==null){
				itemForm = new ItemMasterForm();
				desktopPane.add(itemForm);
			}
			jInternalFrameSize = itemForm.getSize();
			itemForm.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			itemForm.setVisible(true);
			itemForm.repaint();
			itemForm.revalidate();   
			itemForm.formClear();

		}
		else if (ae.getActionCommand().equals("Customer Article Codes")){
			if (cgaCodeForm==null){
				cgaCodeForm = new CustomerGroupArticlecodesForm();
				desktopPane.add(cgaCodeForm);
			}
			jInternalFrameSize = cgaCodeForm.getSize();
			cgaCodeForm.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			cgaCodeForm.setVisible(true);
			cgaCodeForm.repaint();
			cgaCodeForm.revalidate();   
			cgaCodeForm.formClear();

		}
		else if (ae.getActionCommand().equals("Customer Item Mapping")){
			if (cimForm==null){
				cimForm = new CustomerItemMappingForm();
				desktopPane.add(cimForm);
			}
			jInternalFrameSize = cimForm.getSize();
			cimForm.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			cimForm.setVisible(true);
			cimForm.repaint();
			cimForm.revalidate();   
			cimForm.formClear();

		}	else if (ae.getActionCommand().equals("User Management")){
			if (uForm==null){
				uForm = new UserForm();
				desktopPane.add(uForm);
			}
			jInternalFrameSize = uForm.getSize();
			uForm.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			uForm.setVisible(true);
			uForm.repaint();
			uForm.revalidate();   
			uForm.formClear();

		}else if (ae.getActionCommand().equals("Password Change")){
			if (changePwdForm==null){
				changePwdForm = new ChangePwdForm(user);
				//desktopPane.add(changePwdForm);
			}
			changePwdForm.setVisible(true);
			changePwdForm.repaint();
			changePwdForm.revalidate();   
		}else if (ae.getActionCommand().equals("User Permissions")){
			if (userPermissions==null){
				userPermissions = new UserPermissionsForm();
				desktopPane.add(userPermissions);
			} 
			jInternalFrameSize = userPermissions.getSize();
			userPermissions.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			userPermissions.setVisible(true);
			userPermissions.repaint();
			userPermissions.revalidate();   
			userPermissions.formClear();
		}
		else if (ae.getActionCommand().equals("PO Reader Form")){
			POReaderForm read = new POReaderForm();
			jInternalFrameSize = read.getSize();
			read.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			desktopPane.add(read);
			read.setVisible(true);
		} else if (ae.getActionCommand().equals("Manually Check PO Details")){
			CheckPODetailsForm read = new CheckPODetailsForm();
			jInternalFrameSize = read.getSize();
			read.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			desktopPane.add(read);
			read.setVisible(true);
		}
		
		else if (ae.getActionCommand().equals("User List Report")){
			frmPoReaderApplication.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			new UserListReport(); 
			frmPoReaderApplication.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if (ae.getActionCommand().equals("User Permissions Report")){
			frmPoReaderApplication.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			new UserPermissionsReport(); 
			frmPoReaderApplication.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if (ae.getActionCommand().equals("MRP Type Report")){
			frmPoReaderApplication.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			new MRPTypeReport(); 
			frmPoReaderApplication.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if (ae.getActionCommand().equals("Item Category Report")){
			frmPoReaderApplication.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			new ItemCategoryReport(); 
			frmPoReaderApplication.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if (ae.getActionCommand().equals("Customer Group Report")){
			frmPoReaderApplication.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			new CustomerGroupReport(); 
			frmPoReaderApplication.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if (ae.getActionCommand().equals("Item Master Report")){
			frmPoReaderApplication.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			new ItemMasterReport(); 
			frmPoReaderApplication.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if (ae.getActionCommand().equals("Item Price Report")){
			frmPoReaderApplication.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			new ItemPriceReport(); 
			frmPoReaderApplication.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if (ae.getActionCommand().equals("Customer Master Report")){
			frmPoReaderApplication.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			new CustomerMasterReport(); 
			frmPoReaderApplication.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else if (ae.getActionCommand().equals("Customer Group Article Codes Report")){
			if (cgACodeReport==null){
				cgACodeReport = new CGArticleCodesReport();
				desktopPane.add(cgACodeReport);
			}
			jInternalFrameSize = cgACodeReport.getSize();
			cgACodeReport.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			cgACodeReport.setVisible(true);
			cgACodeReport.repaint();
			cgACodeReport.revalidate(); 
		}else if (ae.getActionCommand().equals("Customer Items Report")){
			if (ciReport==null){
				ciReport = new CustomerItemsReport();
				desktopPane.add(ciReport);
			}
			jInternalFrameSize = ciReport.getSize();
			ciReport.setLocation((desktopSize.width - jInternalFrameSize.width)/2,(desktopSize.height- jInternalFrameSize.height)/2);
			ciReport.setVisible(true);
			ciReport.repaint();
			ciReport.revalidate();   
		}
	}
}
