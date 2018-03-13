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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import com.takkaiah.db.dao.CustomerArticleCodeDAO;
import com.takkaiah.db.dao.CustomerGroupDAO;
import com.takkaiah.db.dao.ItemDAO;
import com.takkaiah.db.dto.CustomerArticleCode;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.Item;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.poreader.util.SearchForm;

public class CustomerGroupArticlecodesForm extends JInternalFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(CustomerGroupArticlecodesForm.class.getName());

	JButton  bt_save = new JButton("Save");
	JButton  bt_reset = new JButton("Reset");
	JButton  bt_search = new JButton("Search");
	
	JTextField  cgID_txt = new JTextField();
	JTextField  cgName_txt = new JTextField();
	JTextField  cgAlias_txt = new JTextField();
	JTextArea  address_txt = new JTextArea();
	JTextArea  remarks_txt = new JTextArea();
	JLabel    status_label = new JLabel(" ");

	
	JTable table;
	DefaultTableModel tModel = new DefaultTableModel(){
		private static final long serialVersionUID = 1L;
		@Override
    	   public boolean isCellEditable(int row, int column) {
    	       //Only the third column
    	       //return column == 3;
			 if (column==4) {
				 return true;
			 }
			return false;
    	   }
	};
	
	public CustomerGroupArticlecodesForm() {
		addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
            	//dispose();
            	setVisible(false);
            }
        }); 
		setTitle("Customer Item Article Code Form");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(false);
		setClosable(true);
		setBounds(100, 100, 750, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		table=new JTable(tModel);
		table.setAutoCreateRowSorter(true);
		tModel.addColumn("ID"); tModel.addColumn("Item ID"); tModel.addColumn("Item Name"); tModel.addColumn("EAN");  tModel.addColumn("Article Code"); 
	    
        
	   table.getColumnModel().getColumn(0).setWidth(0);
	    table.getColumnModel().getColumn(0).setMinWidth(0);
	    table.getColumnModel().getColumn(0).setMaxWidth(0); 

    	table.getColumnModel().getColumn(1).setWidth(0);
	    table.getColumnModel().getColumn(1).setMinWidth(0);
	    table.getColumnModel().getColumn(1).setMaxWidth(0); 

        
	    
	    table.getColumnModel().getColumn(0).setPreferredWidth(10);
	    table.getColumnModel().getColumn(1).setPreferredWidth(10);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(20);
        table.getColumnModel().getColumn(4).setPreferredWidth(20);
	   
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        cgID_txt.setEnabled(false);
    	cgName_txt.setEnabled(false);
    	cgAlias_txt.setEnabled(false);
    	address_txt.setEnabled(false);
    	remarks_txt.setEnabled(false);
    	
    	cgID_txt.setDisabledTextColor(Color.BLACK);
      	cgName_txt.setDisabledTextColor(Color.BLACK);
      	cgAlias_txt.setDisabledTextColor(Color.BLACK);
      	address_txt.setDisabledTextColor(Color.BLACK);
      	remarks_txt.setDisabledTextColor(Color.BLACK);
        
        status_label.setForeground(Color.BLUE);
        table.getTableHeader().setForeground(Color.blue);

        
        JPanel formPanel = new JPanel();
        GridLayout gl = new GridLayout(3, 4);
        formPanel.setLayout(gl);

        JPanel panel2 = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
        panel2.setLayout(fl);
        
        panel2.add(bt_save);
        panel2.add(bt_search);
        panel2.add(bt_reset);
        
        bt_save.addActionListener(this);
        bt_search.addActionListener(this);
        bt_reset.addActionListener(this);
        bt_save.setMnemonic(KeyEvent.VK_S);
        bt_save.setToolTipText("Save Customer Group Article Codes");
        bt_save.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoSave)));
        bt_search.setMnemonic(KeyEvent.VK_E);
        bt_search.setToolTipText("Search Customer Group");
        bt_search.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoSearch)));
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setToolTipText("Clear Form Details");
        bt_reset.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));
        table.setToolTipText("Press F2 to select Item Details");

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
        ImageIcon icon = new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.CustArticleCodeLogoPath));
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
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		 if (actionEvent.getActionCommand().equals("Search"))
		 {
			try{
				SearchForm searchFrm = new SearchForm("Customer Groups", null,POReaderParams.CustomerGroup);
				searchFrm.setVisible(true);
				if (searchFrm.getReturnID() >0) 
				{
					formClear();
					CustomerGroupDAO cgDAO = new CustomerGroupDAO();
					CustomerGroup cg = cgDAO.getCustomerGroup(searchFrm.getReturnID());
					
			        cgID_txt.setText( cg.getCgID() + "" );
			    	cgName_txt.setText( cg.getCgName() );
			    	cgAlias_txt.setText( cg.getCgAlias() );
			    	address_txt.setText( cg.getAddress() );
			    	remarks_txt.setText( cg.getRemarks() );
					
					CustomerArticleCodeDAO caCodeDAO = new CustomerArticleCodeDAO();
					
					List<Object[]> caCodes = caCodeDAO.getAllCustArticleCodes(cg);
					
					status_label.setText(" ");
					
					tModel.setNumRows(0);
					if (caCodes==null || caCodes.size()==0 )
					{
						fillTable();
					} else {
						for (int i=0;i<caCodes.size();i++)
						{
							Object[] caCode = caCodes.get(i);
							tModel.addRow(new Object[] { caCode[0]== null ? "":caCode[0], caCode[1] , caCode[2], caCode[3], caCode[4]== null ? "":caCode[4]} );
						}
					} 
					
				}
				searchFrm.dispose();
				searchFrm =null;
				this.repaint();
				this.revalidate();
			}catch(Exception e){
				log.error("Unable to fetch Customer Article Codes : " + e.getMessage());
			}
		 } else if (actionEvent.getActionCommand().equals("Save")){
			 table.getDefaultEditor(Object.class).stopCellEditing();
			 if (cgID_txt.getText().trim().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please select Customer Group!");
					bt_search.requestFocus();
					return;
			 }
			 if (checkDuplicateArticleCodes()==true){
				 JOptionPane.showInternalMessageDialog(this, "Article Code cannot be Duplicate!");
				 return;
			 }
			 List<CustomerArticleCode> caCodes = new ArrayList<>();
			 CustomerGroup cg = new CustomerGroup();
			 cg.setCgID( Integer.parseInt(cgID_txt.getText()) );
			 String articleCodeStr ;
			 for (int i=0;i<table.getRowCount();i++)
			 {
				 CustomerArticleCode articleCode = new CustomerArticleCode();
				 if (table.getValueAt(i, 0).toString().length()>0){
					 articleCode.setCgAID(Integer.parseInt(table.getValueAt(i, 0).toString()));
				 }
				 Item item = new Item();
				 item.setItemID(Integer.parseInt(table.getValueAt(i, 1).toString()));
				 articleCode.setCustItemGroup(cg);
				 articleCode.setCustItem(item);
				 articleCodeStr = table.getValueAt(i, 4).toString();
				 if (articleCodeStr != null) {
					 if (articleCodeStr.trim().length() > 0)
						 articleCode.setArticleCode(table.getValueAt(i, 4).toString() );
				 }
				 caCodes.add(articleCode);
			 }
			 try{
				 CustomerArticleCodeDAO cacDAO = new CustomerArticleCodeDAO();
				 if (cacDAO.addUpdateCustomerArticleCode(caCodes)==true){
					 status_label.setText("Customer Group Article Code Saved Successfully!");
				 }
				 
			 }catch(Exception er){
				 status_label.setText("Error Saving Customer Group Article Codes!");
				 log.error("Unable to save Customer Article Codes : " + er.getMessage());
			 }
			 
		 } else if (actionEvent.getActionCommand().equals("Reset")){
			 formClear();
			 fillTable();
		 }
	}
	
	// User Defined Methods
		private void fillTable(){
	        tModel.setNumRows(0);
	        try{
	        	 ItemDAO iDAO = new ItemDAO();
	        	 List<Item> cgList = iDAO.getAllitems();
	        	if (cgList!=null) {
					for(int i=0;i<cgList.size();i++) {
						Item row = cgList.get(i);
						tModel.addRow(new Object[] { "",row.getItemID() , row.getItemName(),row.getEanCode() , "" });
					}
	        	} 
	        }catch(Exception er)
	        {  log.error("Unable to fetch Item Details : " + er.getMessage());    }
			
		}
	
	public void formClear(){
		cgID_txt.setText("");
		cgName_txt.setText("");
		cgAlias_txt.setText("");
		address_txt.setText("");
		remarks_txt.setText("");
		status_label.setText(" ");

	}
	
	
	
	private boolean checkDuplicateArticleCodes(){
		boolean retStatus = false;
		String articleCodeStr;
		for (int i=0;i<table.getRowCount();i++){
			articleCodeStr = table.getValueAt(i, 4).toString();
			if (articleCodeStr !=null )
			{
				if (articleCodeStr.trim().length()>0) {
					for (int j=i+1;j<table.getRowCount();j++){
						if (table.getValueAt(i, 4).toString().equals(table.getValueAt(j, 4).toString())){
							table.scrollRectToVisible(table.getCellRect(i, 4, true));
	                        // this will automatically set the focus of the searched/selected row/value
	                        table.setRowSelectionInterval(i, i);
	                        i=table.getRowCount();
	                        retStatus=true;
	                        break;
						}
					}
				}
			}
		}
		return retStatus;
	}

}
