package com.takkaiah.poreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;

import com.takkaiah.email.Email;
import com.takkaiah.email.EmailService;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POUtil;

import say.swing.JFontChooser;

public class EmailForm extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(EmailForm.class.getName());
	
	//JTextPane   emailTxt = new JTextPane();
	//JEditorPane emailTxt = new JEditorPane();
	JTextPane emailTxt = new JTextPane();
	JButton  bt_email = new JButton("Send Email");
	
	JTextField emailFrom_txt = new JTextField();
	JTextField emailTo_txt = new JTextField();
	JTextField emailCC_txt = new JTextField();
	JTextField emailBCC_txt = new JTextField();
	JTextField emailSubject_txt = new JTextField();
	
	Email email;
	private final JPopupMenu popupMenu = new JPopupMenu();
	private final JMenuItem mntmBackgroundColor = new JMenuItem("Background Color");
	private final JMenuItem mntmForegroundColor = new JMenuItem("Foreground Color");
	private final JMenuItem mntmFont = new JMenuItem("Font");
	
	public EmailForm(Email email) {
		
		this.email = email;
		setTitle("Email Details");
		setResizable(true);
		setModal(true);
		setBounds(100, 100,750, 500);
		setLocationRelativeTo(null);
		
		BorderLayout bl = new BorderLayout(1,1);
		getContentPane().setLayout(bl);

	    JPanel formPanel = new JPanel();
        GridLayout gl1 = new GridLayout(3, 4);
        formPanel.setLayout(gl1);
        
        JLabel l1 = new JLabel("From   ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("To   ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("CC   ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l4 = new JLabel("BCC   ");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l5 = new JLabel("Subject   ");
        l5.setHorizontalAlignment(SwingConstants.RIGHT);
        
        emailFrom_txt.setText(email.getFrom());
        emailTo_txt.setText(email.getTo());
        emailSubject_txt.setText(email.getSubject());
        emailSubject_txt.setToolTipText("Enter Email Subject");
        emailFrom_txt.setToolTipText("Enter From Email");
        emailTo_txt.setToolTipText("Enter To Emails seperated with comma");
        emailBCC_txt.setToolTipText("Enter BCC Emails seperated with comma");
        emailCC_txt.setToolTipText("Enter CC Emails seperated with comma");
        emailTxt.setToolTipText("Change Email Body Content");
		 emailTxt.putClientProperty(JTextPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);

        formPanel.add(l1);
        formPanel.add(emailFrom_txt);
        formPanel.add(l3);
        formPanel.add(emailCC_txt);
        formPanel.add(l2);
        formPanel.add(emailTo_txt);
        formPanel.add(l4);
        formPanel.add(emailBCC_txt);
        formPanel.add(l5);
        formPanel.add(emailSubject_txt);
        
        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
        

        cgpanel.add(formPanel , BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
	    FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
	    buttonPanel.setLayout(fl);
	    
	    buttonPanel.add(bt_email);
	    bt_email.setMnemonic(KeyEvent.VK_E);
	    bt_email.setToolTipText("Email PO Details to Customer");
	    bt_email.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoEmail)));
	    bt_email.addActionListener(this);

	    
	    emailTxt.setContentType("text/html");
	    emailTxt.setText(email.getMsg_body());
	    
	    getContentPane().add(cgpanel,BorderLayout.NORTH);
	    getContentPane().add(new JScrollPane(emailTxt),BorderLayout.CENTER);
	    
	    addPopup(emailTxt, popupMenu);
	    
	    popupMenu.add(mntmBackgroundColor);
	    popupMenu.addSeparator();
	    popupMenu.add(mntmForegroundColor);
	    popupMenu.addSeparator();
	    popupMenu.add(mntmFont);
	    
	    mntmBackgroundColor.addActionListener(this);
	    mntmForegroundColor.addActionListener(this);
	    mntmFont.addActionListener(this);
	    
	    getContentPane().add(buttonPanel,BorderLayout.SOUTH);
	    
	}
	

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		
		if (actionEvent.getActionCommand().equals("Send Email")){
			this.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			Email finalEmail = new Email();
			finalEmail.setFrom(emailFrom_txt.getText());
			finalEmail.setTo(emailTo_txt.getText());
			finalEmail.setSubject(emailSubject_txt.getText());
			finalEmail.setCc(emailCC_txt.getText());
			finalEmail.setBcc(emailBCC_txt.getText());
			finalEmail.setMsg_body(emailTxt.getText());
			
			//System.out.println(emailTxt.getText());

	    	EmailService es =new EmailService();
	    	try{
	    		if( es.sendEmail(finalEmail)==true){
	    			JOptionPane.showMessageDialog(this, "Email Successfully sent To :" + emailTo_txt.getText()) ;
	    		}
	    	}catch(Exception er){
	    		log.error("Unable to send email :" + er.getMessage());
	    		JOptionPane.showMessageDialog(this, "Uanble to send Email To :" +  emailTo_txt.getText()) ;
	    	}
	    	this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else if (actionEvent.getActionCommand().equals("Foreground Color")){
			try{
				Color color = JColorChooser.showDialog(this, "Colors",Color.BLUE);
				StyledDocument doc = emailTxt.getStyledDocument();
				int from = emailTxt.getSelectionStart();
				int to = emailTxt.getSelectionEnd();
				if (from == to) { // No selection, cursor position.
				        return;
				    }
				   //Add span Tag
				String htmlStyle = "color:" + POUtil.getHTMLColor(color);
				SimpleAttributeSet attr = new SimpleAttributeSet();
				attr.addAttribute(HTML.Attribute.STYLE, htmlStyle);
				MutableAttributeSet outerAttr = new SimpleAttributeSet();
				outerAttr.addAttribute(HTML.Tag.SPAN, attr);
				//Next line is just an instruction to editor to change color
				StyleConstants.setForeground(outerAttr, color);
				doc.setCharacterAttributes(from,to-from,outerAttr,false);
			 }catch(Exception er){
				 log.error("Unable to set foreground Color :" + er.getMessage());
			 }
		}else if (actionEvent.getActionCommand().equals("Background Color")){
			 try{
				 Color color = JColorChooser.showDialog(this, "Colors",Color.BLUE);
				 StyledDocument doc = emailTxt.getStyledDocument();
				    int from = emailTxt.getSelectionStart();
				    int to = emailTxt.getSelectionEnd();
	                if (from == to) { // No selection, cursor position.
	                    return;
	                }
	                
	 			   //Add span Tag
			        String htmlStyle = "background-color:" + POUtil.getHTMLColor(color);
			        SimpleAttributeSet attr = new SimpleAttributeSet();
			        attr.addAttribute(HTML.Attribute.STYLE, htmlStyle);
			        MutableAttributeSet outerAttr = new SimpleAttributeSet();
			        outerAttr.addAttribute(HTML.Tag.SPAN, attr);
			        StyleConstants.setBackground(outerAttr, color);
				    doc.setCharacterAttributes(from, to-from, outerAttr, false);
				 }catch(Exception er){
					 log.error("Unable to set Background Color :" + er.getMessage());
				 }
			
		}else if (actionEvent.getActionCommand().equals("Font")){
			try{
				 JFontChooser fontChooser = new JFontChooser();
				 fontChooser.setSelectedFont(new Font("Times New Roman",Font.PLAIN,14));
				 if (fontChooser.showDialog(null) ==JFontChooser.OK_OPTION) {
					StyledDocument doc = emailTxt.getStyledDocument(); 
				    int from = emailTxt.getSelectionStart();
				    int to = emailTxt.getSelectionEnd();
	                if (from == to) { // No selection, cursor position.
	                    return;
	                }
	                
	  			   //Add span Tag
			        String htmlStyle = "font-size:" + fontChooser.getSelectedFontSize() +";font-family:" + fontChooser.getSelectedFontFamily() + ";font-style:bold" ;
			        SimpleAttributeSet attr = new SimpleAttributeSet();
			        attr.addAttribute(HTML.Attribute.STYLE, htmlStyle);
			        MutableAttributeSet outerAttr = new SimpleAttributeSet();
			        outerAttr.addAttribute(HTML.Tag.SPAN, attr);
			        //Next line is just an instruction to editor to change color
			        StyleConstants.setFontFamily(outerAttr, fontChooser.getSelectedFontFamily() );
			        StyleConstants.setFontSize(outerAttr, fontChooser.getSelectedFontSize() );
			        StyleConstants.setBold(outerAttr, (fontChooser.getSelectedFontStyle() & Font.BOLD) != 0);
			        StyleConstants.setItalic(outerAttr, (fontChooser.getSelectedFontStyle() & Font.ITALIC) != 0);
				    doc.setCharacterAttributes(from, to-from, outerAttr, false);
				 }
			 }catch(Exception er){
				 log.error("Unable to set Font :" + er.getMessage());
			 }
		}
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
