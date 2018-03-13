package com.takkaiah.poreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

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
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.takkaiah.db.dao.CustomerDAO;
import com.takkaiah.db.dao.CustomerItemMappingDAO;
import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.MRPType;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.pdf.output.Export2Excel;
import com.takkaiah.poreader.reports.CustomerItemMappingReport;
import com.takkaiah.poreader.reports.CustomerItemMappingWithMrpReport;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.poreader.util.SearchForm;

public class CustomerItemsReport extends JInternalFrame implements ActionListener {

	POReaderLogger log = POReaderLogger.getLogger(CustomerItemsReport.class.getName());

	private static final long serialVersionUID = 1L;
	JTable table;
    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	JButton  bt_reset = new JButton("Reset");
	JButton  bt_search = new JButton("Search");
	JButton  bt_report = new JButton("Report");
	JButton bt_export = new JButton("Export");

	
	JTextField  cID_txt = new JTextField();
	JTextField  cName_txt = new JTextField();
	JTextField  cgName_txt = new JTextField();
	JTextArea  address_txt = new JTextArea();
	JTextField  email_txt = new JTextField();
	JTextField  mobile_txt = new JTextField();
	JTextArea  remarks_txt = new JTextArea();
	JTextField  mrpType_txt = new JTextField();
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
	public CustomerItemsReport() {
		addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
            	//dispose();
            	setVisible(false);
            	//pack();
            }
        }); 
		
		setTitle("Customer Items Report");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(false);
		setClosable(true);
		setBounds(100, 100, 700, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table=new JTable(tModel);
		table.setAutoCreateRowSorter(true);
		
		
	    tModel.addColumn("Sl No"); tModel.addColumn("Customer Article Code");tModel.addColumn("EAN Code");  
	    tModel.addColumn("Item Name");  tModel.addColumn("Margin Percent"); tModel.addColumn("Basic Cost Price");	    tModel.addColumn("Tax %");  
	    tModel.addColumn("Tax Value");	    tModel.addColumn("Net Landing Price");  
	    tModel.addColumn(" MRP "); tModel.addColumn("Case Qty");
	    
	    rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
	    
	    table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
	    table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
	    table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
	    table.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
	    table.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
	    table.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
	    table.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);


        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setForeground(Color.blue);

        
    
        JPanel formPanel = new JPanel();
        GridLayout gl1 = new GridLayout(5, 4);
        formPanel.setLayout(gl1);
        
        JPanel buttonForm = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
        buttonForm.setLayout(fl);
        
        buttonForm.add(bt_search);
        buttonForm.add(bt_reset);
        buttonForm.add(bt_export);
        buttonForm.add(bt_report);

        bt_reset.addActionListener(this);
        bt_reset.setMnemonic(KeyEvent.VK_E);
        bt_reset.setToolTipText("Clear Form Details");
        bt_reset.setIcon(POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));

        bt_search.setMnemonic(KeyEvent.VK_S);
        bt_search.setToolTipText("Search Customer");
        bt_search.setIcon(POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoSearch)));
        bt_search.addActionListener(this);
 
        bt_report.addActionListener(this);
        bt_report.setMnemonic(KeyEvent.VK_R);
        bt_report.setToolTipText("Customer Specific Items Report");
        bt_report.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoPoReport)));

        bt_export.addActionListener(this);
        bt_export.setMnemonic(KeyEvent.VK_X);
        bt_export.setToolTipText("Export Customer Specific Items");
        bt_export.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoExport)));


        
        cID_txt.setEnabled(false);
    	cName_txt.setEnabled(false);
    	cgName_txt.setEnabled(false);
    	address_txt.setEnabled(false);
    	email_txt.setEnabled(false);
    	mobile_txt.setEnabled(false);
    	remarks_txt.setEnabled(false);
    	mrpType_txt.setEnabled(false);
    	
    	
        cID_txt.setDisabledTextColor(Color.BLACK);
    	cName_txt.setDisabledTextColor(Color.BLACK);
    	cgName_txt.setDisabledTextColor(Color.BLACK);
    	address_txt.setDisabledTextColor(Color.BLACK);
    	email_txt.setDisabledTextColor(Color.BLACK);
    	mobile_txt.setDisabledTextColor(Color.BLACK);
    	remarks_txt.setDisabledTextColor(Color.BLACK);
    	mrpType_txt.setDisabledTextColor(Color.BLACK);
        
        JLabel l1 = new JLabel("  Customer ID  ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("  Customer Name  ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("  Customer Group Name  ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l4 = new JLabel("  MRP Type   ");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JLabel l6 = new JLabel("  Email  ");
        l6.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l7 = new JLabel("  Mobile  ");
        l7.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l8 = new JLabel("  Address  ");
        l8.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l9 = new JLabel("  Remarks  ");
        l9.setHorizontalAlignment(SwingConstants.RIGHT);

        formPanel.add(l1);
        formPanel.add(cID_txt);
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
        
        JScrollPane addressScroll = new JScrollPane(address_txt);
        JScrollPane remarksScroll = new JScrollPane(remarks_txt);
          
        formPanel.add(l8);
        formPanel.add(addressScroll);
        formPanel.add(l9);
        formPanel.add(remarksScroll);
        
        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
        
        cgpanel.add(formPanel , BorderLayout.CENTER);
        cgpanel.add(buttonForm,BorderLayout.SOUTH);
        
        BorderLayout bl = new BorderLayout(1,1);
		getContentPane().setLayout(bl);
		
		getContentPane().add(cgpanel,BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(table),BorderLayout.CENTER);
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("Search")){
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
						cgName_txt.setText(cg.getCgName());
					}
					MRPType mrpType = cust.getCustMRP();
					if (mrpType!=null){
						mrpType_txt.setText(mrpType.getMrpTName());
					}
					mobile_txt.setText(cust.getMobile());
					email_txt.setText(cust.getEmail());
					address_txt.setText(cust.getAddress());
					remarks_txt.setText(cust.getRemarks());
					setCustItems(cust);
					searchFrm.dispose();
					searchFrm =null;
				}
				this.repaint();
				this.revalidate();
			}catch(Exception er){
				log.error("Error fetching the Customer details :" + er.getMessage());
			}
		} else 	if (actionEvent.getActionCommand().equals("Reset")){
			formClear();
		}  else if (actionEvent.getActionCommand().equals("Report")){
			try{
				if (cID_txt.getText().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please select Customer!","Customer Empty!",JOptionPane.PLAIN_MESSAGE) ;
					return;
				}
				if (JOptionPane.showInternalConfirmDialog(this, "Show Customer Item Mappings Report with Detail MRP?","Confirmation", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION) 
				{ 
					new CustomerItemMappingWithMrpReport(Integer.parseInt(cID_txt.getText()));
				} else {
					new CustomerItemMappingReport(Integer.parseInt(cID_txt.getText())); 	
				}
				
			}catch(Exception e){
				log.error("Unable to export to xlsx - "+e.getMessage());
			}
		}	else if (actionEvent.getActionCommand().equals("Export")){
			try{
				if (cID_txt.getText().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please select Customer !","Customer Empty!",JOptionPane.PLAIN_MESSAGE) ;
					return;
				}
				
				String outputFolder = POReaderReadEnv.getEnvValue(POReaderEnvProp.XSLXExportFolder);
				String xlsFileName = outputFolder + "/" + cName_txt.getText() +  "_IM.xls";
				if (Export2Excel.exportCustItems( POUtil.convertToList(table), xlsFileName)==true){
					JOptionPane.showInternalMessageDialog(this, "Customer Item Mappings are exported to File Name - " + xlsFileName  ,"Export Successfull!",JOptionPane.PLAIN_MESSAGE) ;
				}
				
			}catch(Exception e){
				log.error("Unable to Export - "+e.getMessage());
			}
		}		
	}
	
	public void formClear(){
		cID_txt.setText("");
		cName_txt.setText("");
		cgName_txt.setText("");
		mrpType_txt.setText("");
		mobile_txt.setText("");
		email_txt.setText("");
		address_txt.setText("");
		remarks_txt.setText("");
		status_label.setText(" ");
		tModel.setRowCount(0);
	}
	
	public void setCustItems(Customer cust){
		List<Object[]> items = null;
		CustomerItemMappingDAO iDAO = new CustomerItemMappingDAO();
		tModel.setRowCount(0);
		try{
			items =  iDAO.getAllCustItemMapReport(cust);
			if (items!=null){
				for(int i=0;i<items.size();i++) {
					Object[] rowCol = items.get(i);
					tModel.addRow(new Object[] { (i+1) + "",  rowCol[0] , rowCol[1],  rowCol[2] ,rowCol[3], 
							rowCol[4], rowCol[5] , rowCol[6], rowCol[7], rowCol[8] ,rowCol[9]});
				}
			}
		}catch(Exception eer){
			log.error("Error fetching the Customer item mapping details :" + eer.getMessage());
		}
	}
	
	
}
