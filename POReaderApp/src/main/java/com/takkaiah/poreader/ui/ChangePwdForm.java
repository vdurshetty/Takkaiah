package com.takkaiah.poreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.security.CipherTextInfo;

public class ChangePwdForm extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(ChangePwdForm.class.getName());
	
	JTextField     userName_txt = new JTextField();
	JPasswordField oldPwd_txt = new JPasswordField();
	JPasswordField newPwd1_txt = new JPasswordField();
	JPasswordField newPwd2_txt = new JPasswordField();
	JLabel    status_label = new JLabel(" ");
	
	JButton bt_update = new JButton("Save");
	JButton bt_reset = new JButton("Reset");

	
	User user;
	public ChangePwdForm(User user) {
		setTitle("Password Change Form");
		setBounds(100, 100, 500, 200);
		setModal(true);
		setLocationRelativeTo(null);
		this.user=user;
		userName_txt.setEnabled(false);
		userName_txt.setText(user.getUserName());
		
	    JPanel formPanel = new JPanel();
	    GridLayout gl = new GridLayout(4, 2);
	    formPanel.setLayout(gl);

		
	    JPanel panel2 = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
        panel2.setLayout(fl);
        
        panel2.add(bt_update);
        panel2.add(bt_reset);
        status_label.setForeground(Color.BLUE);

        
        bt_update.addActionListener(this);
        bt_update.setMnemonic(KeyEvent.VK_S);
        bt_update.setToolTipText("Reset Password");
        bt_update.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoSave)));

        bt_reset.addActionListener(this);
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setToolTipText("Clear Form Details");
        bt_reset.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));

        JLabel l1 = new JLabel("  User Name  ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("  Old Password  ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("  New Password  ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l4 = new JLabel("  Retype Password  ");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);

        
        formPanel.add(l1);
        formPanel.add( userName_txt);
        formPanel.add(l2);
        formPanel.add(oldPwd_txt);
        formPanel.add(l3);
        formPanel.add(newPwd1_txt);
        formPanel.add(l4);
        formPanel.add(newPwd2_txt);
      
        oldPwd_txt.setToolTipText("Enter Old Password");
        newPwd1_txt.setToolTipText("Enter New Password");
        newPwd2_txt.setToolTipText("ReEnter New Password");
        
        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
        
        JLabel jLogo = new JLabel();
        ImageIcon icon = new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.PwdResetLogoPath));
	    Image newimg = icon.getImage().getScaledInstance( 120, 120,  java.awt.Image.SCALE_SMOOTH ) ;  
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
		if (actionEvent.getActionCommand().equals("Save")){
			if (String.valueOf(oldPwd_txt.getPassword()).trim().length()==0){
				status_label.setText("Please Enter User Name");
				oldPwd_txt.requestFocus();
				return;
			} else if (String.valueOf(newPwd1_txt.getPassword()).trim().length()==0){
				status_label.setText("Please Enter User Password");
				newPwd1_txt.requestFocus();
				return;
			}else if (String.valueOf(newPwd2_txt.getPassword()).trim().length()==0){
				status_label.setText("Please Enter User Password");
				newPwd2_txt.requestFocus();
				return;
			}else if (! String.valueOf(newPwd1_txt.getPassword()).equals(String.valueOf(newPwd2_txt.getPassword()))){
				status_label.setText("New Password and Re-Enter Password do not match!");
				newPwd1_txt.requestFocus();
				return;
			}
			UserDAO uDAO = new UserDAO();
			try{
				
				User oldUser = new User();
				oldUser.setUserName(user.getUserName());
				oldUser.setPwd( CipherTextInfo.encrypt(String.valueOf(oldPwd_txt.getPassword())));
				oldUser = uDAO.validateUser(oldUser);
				if (oldUser==null){
					status_label.setText("Old Password for the User Name '" + user.getUserName() + "' is Wrong!");
					return;
				} 
				user.setPwd(CipherTextInfo.encrypt(String.valueOf(newPwd1_txt.getPassword())));
		        if (uDAO.updateUser(user)==true){
					status_label.setText("User Password Successfully Changed!");
				}
			}catch(Exception er){
				log.error("Unable to save user password :" + er.getMessage() );
			}
		} else if (actionEvent.getActionCommand().equals("Reset")){
			oldPwd_txt.setText("");
			newPwd1_txt.setText("");
			newPwd2_txt.setText("");
			status_label.setText(" ");
		}		
	}

}
