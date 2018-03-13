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

import com.takkaiah.db.dao.CustomerArticleCodeDAO;
import com.takkaiah.db.dao.CustomerGroupDAO;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.pdf.output.Export2Excel;
import com.takkaiah.poreader.reports.CustomerArticleCodesReport;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.poreader.util.SearchForm;

public class CGArticleCodesReport extends JInternalFrame implements ActionListener {

	POReaderLogger log = POReaderLogger.getLogger(CGArticleCodesReport.class.getName());

	private static final long serialVersionUID = 1L;
	JTable table;
    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	JButton  bt_reset = new JButton("Reset");
	JButton  bt_search = new JButton("Search");
	JButton  bt_export = new JButton("Export");
	JButton  bt_report = new JButton("Report");

	
	JTextField  cgID_txt = new JTextField();
	JTextField  cgName_txt = new JTextField();
	JTextField  alias_txt = new JTextField();
	JTextArea  address_txt = new JTextArea();
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
	public CGArticleCodesReport() {
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
		
			
	    tModel.addColumn("Sl No"); tModel.addColumn("EAN Code"); tModel.addColumn("Item Name"); tModel.addColumn("Customer Article Code");

        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setForeground(Color.blue);

        
    
        JPanel formPanel = new JPanel();
        GridLayout gl1 = new GridLayout(2, 4);
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
 
        bt_export.addActionListener(this);
        bt_export.setMnemonic(KeyEvent.VK_X);
        bt_export.setToolTipText("Export Customer Article Codes");
        bt_export.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoExport)));
        
        bt_report.addActionListener(this);
        bt_report.setMnemonic(KeyEvent.VK_R);
        bt_report.setToolTipText("Customer Article Codes Report");
        bt_report.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.MenuLogoPoReport)));

        

        
        cgID_txt.setEnabled(false);
    	alias_txt.setEnabled(false);
    	cgName_txt.setEnabled(false);
    	address_txt.setEnabled(false);
    	
    	 cgID_txt.setDisabledTextColor(Color.BLACK);
     	alias_txt.setDisabledTextColor(Color.BLACK);
     	cgName_txt.setDisabledTextColor(Color.BLACK);
     	address_txt.setDisabledTextColor(Color.BLACK);
     	
        JLabel l1 = new JLabel("  Customer Group ID  ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("  Customer Group Name  ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("  Alias  ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l4 = new JLabel("  Address  ");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);

        formPanel.add(l1);
        formPanel.add(cgID_txt);
        formPanel.add(l2);
        formPanel.add(alias_txt);
        formPanel.add(l3);
        formPanel.add(cgName_txt);
        formPanel.add(l4);
        JScrollPane addressScroll = new JScrollPane(address_txt);
        formPanel.add(addressScroll);
        
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
				SearchForm searchFrm = new SearchForm("Customer Group List",null, POReaderParams.CustomerGroup);
				searchFrm.setVisible(true);
				if (searchFrm.getReturnID()>0) {
					formClear();
					CustomerGroupDAO cDAO = new CustomerGroupDAO();
					CustomerGroup custGroup = cDAO.getCustomerGroup(searchFrm.getReturnID());
					
					cgID_txt.setText(custGroup.getCgID() + "");
					cgName_txt.setText(custGroup.getCgName());
					alias_txt.setText(custGroup.getCgAlias());
					address_txt.setText(custGroup.getAddress());
					setCustArticleCodes(custGroup);
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
				if (cgID_txt.getText().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please select Customer Group!","Customer Group Empty!",JOptionPane.PLAIN_MESSAGE) ;
					return;
				}
				new CustomerArticleCodesReport(Integer.parseInt(cgID_txt.getText())); 	
				
			}catch(Exception e){
				log.error("Unable to Open Report - "+e.getMessage());
			}
		} else if (actionEvent.getActionCommand().equals("Export")){
			try{
				if (cgID_txt.getText().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please select Customer Group!","Customer Group Empty!",JOptionPane.PLAIN_MESSAGE) ;
					return;
				}
				String outputFolder = POReaderReadEnv.getEnvValue(POReaderEnvProp.XSLXExportFolder);
				String xlsFileName = outputFolder + "/" + cgName_txt.getText() +  "_AC.xls";
				if (Export2Excel.exportCustItems( POUtil.convertToList(table), xlsFileName)==true){
					JOptionPane.showInternalMessageDialog(this, "Customer Article Codes Exported to File Name - " + xlsFileName ,"Export Successfull!",JOptionPane.PLAIN_MESSAGE) ;
				}
				
			}catch(Exception e){
				log.error("Unable to Export - "+e.getMessage());
			}
		}		
		
	}
	
	public void formClear(){
		cgID_txt.setText("");
		alias_txt.setText("");
		cgName_txt.setText("");
		address_txt.setText("");
		status_label.setText(" ");
		tModel.setRowCount(0);
	}
	
	public void setCustArticleCodes(CustomerGroup cg){
		List<Object[]> items = null;
		CustomerArticleCodeDAO iDAO = new CustomerArticleCodeDAO();
		tModel.setRowCount(0);
		try{
			items =  iDAO.getAllCustArticleCodes(cg);
			if (items!=null){
				for(int i=0;i<items.size();i++) {
					Object[] row = items.get(i);
					tModel.addRow(new Object[] { (i+1) + "",  row[3] , row[2], row[4] });
				}
			}
		}catch(Exception eer){
			log.error("Error fetching the Customer item mapping details :" + eer.getMessage());
		}
	}
	
	
}
