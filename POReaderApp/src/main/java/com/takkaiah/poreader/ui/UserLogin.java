package com.takkaiah.poreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.takkaiah.db.dao.UserDAO;
import com.takkaiah.db.dto.User;
import com.takkaiah.db.util.JdbcConnection;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.security.CipherTextInfo;

public class UserLogin extends JDialog  implements ActionListener, KeyListener {

	
	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(UserLogin.class.getName());
	
	JTextField userName_txt = new JTextField("admin");
	JPasswordField userPwd_txt = new JPasswordField("adminadmin");
	JLabel    status_label = new JLabel(" ");
	
	JButton bt_login = new JButton("Login");
	JButton bt_reset = new JButton("Reset");

	
	int uid = 0;
	
	public int getUserID(){
		return uid;
	}
	
	public UserLogin() {
		setTitle("User Login");
		setBounds(100, 100, 420, 170);
		setModal(true);
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.logoPath )));

	    JPanel formPanel = new JPanel();
	    GridLayout gl = new GridLayout(2, 2);
	    formPanel.setLayout(gl);

		
	    JPanel panel2 = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
        panel2.setLayout(fl);
        
        panel2.add(bt_login);
        panel2.add(bt_reset);
        status_label.setForeground(Color.BLUE);

        
        bt_login.addActionListener(this);
        bt_login.setMnemonic(KeyEvent.VK_L);
        bt_login.setToolTipText("Click to Login");
        bt_login.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoLogin)));

        bt_reset.addActionListener(this);
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setToolTipText("Clear Form Details");
        bt_reset.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));

        JLabel l1 = new JLabel("User Name  ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("Password  ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        
        formPanel.add(l1);
        formPanel.add(userName_txt);
        formPanel.add(l2);
        formPanel.add(userPwd_txt);
        
        userName_txt.addKeyListener(this);
        userPwd_txt.addKeyListener(this);
        userName_txt.setToolTipText("Enter User Name");
        userPwd_txt.setToolTipText("Enter Password");
        
        
        
        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
        
        JLabel jLogo = new JLabel();
        
        ImageIcon icon = new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.logoPath));
	    Image newimg = icon.getImage().getScaledInstance( 70, 70,  java.awt.Image.SCALE_SMOOTH ) ;  
	    icon = new ImageIcon( newimg );

        jLogo.setIcon(icon);

        cgpanel.add(jLogo,BorderLayout.WEST);
        cgpanel.add(formPanel , BorderLayout.CENTER);
        cgpanel.add(panel2,BorderLayout.SOUTH);

    	
        BorderLayout bl = new BorderLayout(1,1);
		getContentPane().setLayout(bl);
		
        getContentPane().add(cgpanel,BorderLayout.CENTER);
        getContentPane().add(status_label,BorderLayout.SOUTH);

	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("Login")){
			validateUser();
		} else if (actionEvent.getActionCommand().equals("Reset")){
			userName_txt.setText("");
			userPwd_txt.setText("");
			status_label.setText(" ");
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode()==KeyEvent.VK_ENTER){
			validateUser();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		
	}
	
	
	private boolean checkDBConnection() {
		boolean retVal = false;
		Connection con = null;
		try {
			 JdbcConnection jdbc = new JdbcConnection();
			 con = jdbc.getConnection();
			 if (con==null) {
				 status_label.setText("Unable to connect to Database!");
				 //JOptionPane.showInternalMessageDialog(this, "Please verify the database connection details..." ,"Error Connecting to Database!",JOptionPane.ERROR_MESSAGE);
			 } else {
				 retVal = true;
			 }
		} catch(Exception dbError) {
			log.error("Database Connection Error !" + dbError.getMessage());
		} 
		return retVal;
	}
	
	private void validateUser(){
		if (userName_txt.getText().trim().length()==0){
			status_label.setText("Please Enter User Name");
			userName_txt.requestFocus();
			return;
		} else if (userPwd_txt.getPassword().length == 0){
			status_label.setText("Please Enter User Password");
			userPwd_txt.requestFocus();
			return;
		}
		if (!checkDBConnection()) {
			return;
		} 
		this.setCursor( new Cursor(Cursor.WAIT_CURSOR));
		try{
			UserDAO uDAO = new UserDAO();
			User user = new User();
			user.setUserName(userName_txt.getText());
			user.setPwd( CipherTextInfo.encrypt(String.valueOf(userPwd_txt.getPassword())));
			user = uDAO.validateUser(user);
			if (user==null){
				status_label.setText("Either User Name or Password may be wrong!");
			} else {
				uid = user.getUid();
				log.debug("User Valid -"+ user.getFullName() );
		   		dispose();
			}
		}catch(Exception er){
			log.error("Unable to validate user details: " +er.getMessage());
		}
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	

}
