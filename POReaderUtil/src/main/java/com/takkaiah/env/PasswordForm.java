package com.takkaiah.env;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.security.CipherTextInfo;

public class PasswordForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(PasswordForm.class.getName());
	
	JPasswordField dbPwd_txt = new JPasswordField();
	JPasswordField emailPwd_txt = new JPasswordField();
	JPasswordField smsPwd_txt = new JPasswordField();
	JLabel    status_label = new JLabel(" ");
	
	JButton bt_update = new JButton("Save");
	JButton bt_reset = new JButton("Reset");

	public static void main(String a[]){
		new PasswordForm();
	}
	
	public PasswordForm() {
		setTitle("Set Passwords");
		setBounds(100, 100, 400, 200);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		
	    JPanel formPanel = new JPanel();
	    GridLayout gl = new GridLayout(3, 2);
	    formPanel.setLayout(gl);

		
	    JPanel panel2 = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
        panel2.setLayout(fl);
        
        panel2.add(bt_update);
        panel2.add(bt_reset);
        status_label.setForeground(Color.BLUE);

        
        bt_update.addActionListener(this);
        bt_update.setMnemonic(KeyEvent.VK_S);

        bt_reset.addActionListener(this);
        bt_reset.setMnemonic(KeyEvent.VK_R);

        JLabel l1 = new JLabel("  Database Password  ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("  Email Password  ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("  SMS Password  ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);

        
        formPanel.add(l1);
        formPanel.add(dbPwd_txt);
        formPanel.add(l2);
        formPanel.add(emailPwd_txt);
        formPanel.add(l3);
        formPanel.add( smsPwd_txt);
      
        
        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
        
        cgpanel.add(formPanel , BorderLayout.CENTER);
        cgpanel.add(panel2,BorderLayout.SOUTH);

    	
        BorderLayout bl = new BorderLayout(1,1);
		getContentPane().setLayout(bl);
		
        getContentPane().add(cgpanel,BorderLayout.CENTER);
        getContentPane().add(status_label,BorderLayout.SOUTH);
        
        try{
        	Properties pwds = POReaderPassword.getAllPasswords();
        	if (pwds!=null){
                if (pwds.containsKey(POReaderEnvProp.DBPwd)){
                	dbPwd_txt.setText(CipherTextInfo.decrypt(pwds.getProperty(POReaderEnvProp.DBPwd)));
                } 
                if (pwds.containsKey(POReaderEnvProp.EmailPwd)){
                	emailPwd_txt.setText(CipherTextInfo.decrypt(pwds.getProperty(POReaderEnvProp.EmailPwd)));
                } 
                if (pwds.containsKey(POReaderEnvProp.SmsPwd)){
                	smsPwd_txt.setText(CipherTextInfo.decrypt(pwds.getProperty(POReaderEnvProp.SmsPwd )));
                } 
        	}
        }catch(Exception er){
        	log.error("Unable to get passwords from passwd file:" + er.getMessage());
        }

	}
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("Save")){
			String chkStr = String.valueOf(dbPwd_txt.getPassword()) +String.valueOf(emailPwd_txt.getPassword()) +String.valueOf(smsPwd_txt.getPassword());
			if (chkStr.trim().length()==0){
				status_label.setText("Please Enter at lease one Password!");
				dbPwd_txt.requestFocus();
				return;
			} 
			Properties pwds = new Properties();
			try{
				pwds.put( POReaderEnvProp.DBPwd, CipherTextInfo.encrypt(String.valueOf(dbPwd_txt.getPassword())));
				pwds.put( POReaderEnvProp.EmailPwd, CipherTextInfo.encrypt(String.valueOf(emailPwd_txt.getPassword())));
				pwds.put( POReaderEnvProp.SmsPwd, CipherTextInfo.encrypt(String.valueOf(smsPwd_txt.getPassword())));
				POReaderPassword.savePasswords(pwds);
			}catch(Exception er){
				log.error("Unable to save passwords to passwd file :" + er.getMessage());
			}
			status_label.setText("User Password Successfully Changed!");
		} else if (actionEvent.getActionCommand().equals("Reset")){
			dbPwd_txt.setText("");
			emailPwd_txt.setText("");
			smsPwd_txt.setText("");
			status_label.setText(" ");
		}		
	}
	

	
}
