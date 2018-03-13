package com.takkaiah.poreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.takkaiah.db.dao.CustomerDAO;
import com.takkaiah.db.dao.CustomerGroupDAO;
import com.takkaiah.db.dao.ItemDAO;
import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.ItemWithCustomerMaringPercent;
import com.takkaiah.db.dto.KeyValueMaster;
import com.takkaiah.db.exception.FetchDataException;
import com.takkaiah.email.Email;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.pdf.core.POFormats;
import com.takkaiah.pdf.core.POReader;
import com.takkaiah.pdf.core.POReaderFactory;
import com.takkaiah.pdf.output.Export2Excel;
import com.takkaiah.pdf.output.Export2XML;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;
import com.takkaiah.pdf.vo.PurchaseOrderItems;
import com.takkaiah.poreader.util.POUtil;
import com.takkaiah.poreader.util.TableRenderColor;

public class POReaderForm extends JInternalFrame implements ActionListener,ItemListener, KeyListener {
	
	POReaderLogger log = POReaderLogger.getLogger(POReaderForm.class.getName());

	PurchaseOrderInfo poInfo; 
	DecimalFormat floatVal = new DecimalFormat("#.00");
	DecimalFormat qty = new DecimalFormat("#");
	 
	
	JTable table;
	JComboBox<KeyValueMaster> custGroup_cmb;
	JComboBox<KeyValueMaster> customer_cmb;
	JTextField  poFileTxt;
	JTextPane   poHeaderTxt = new JTextPane();
	JLabel   lblRows = new JLabel();
	JLabel   lblPONetValue = new JLabel();
	JLabel   lblActualNetValue = new JLabel();
	
	JButton  bt_reset = new JButton("Reset");
	JButton  bt_file = new JButton("Select PO");
	JButton  bt_read = new JButton("Extract");
	JButton  bt_validate = new JButton("Validate");
	JButton  bt_export = new JButton("Export");
	JButton  bt_email = new JButton("Email");
	JCheckBox chk_xml = new JCheckBox("XML");
	JCheckBox chk_xlsx = new JCheckBox("XLS");
    
	CustomerDAO cDAO = new CustomerDAO();
	Customer customer;
	float custMarginPercent = 0;
    
    
    DefaultTableModel tModel = new DefaultTableModel(){
	private static final long serialVersionUID = 1L;
	@Override
    public boolean isCellEditable(int row, int column) {
    	       //Only the third column
    	       //return column == 3;
			return false;
    	   }
	};

    private static final long serialVersionUID = 1L;

	/**
	 * Create the frame.
	 */
	public POReaderForm() {
		setTitle("Read PO Details");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setBounds(100, 100,750, 500);
		BorderLayout bl = new BorderLayout(1,1);
		getContentPane().setLayout(bl);
		
		lblRows.setForeground(Color.BLUE);
		poHeaderTxt.setEditable(false);
		poHeaderTxt.setBackground(Color.LIGHT_GRAY);
		poHeaderTxt.setContentType("text/html");
		
        table=new JTable(tModel);
        table.setAutoCreateRowSorter(true);
        tModel.addColumn("Sl No");tModel.addColumn("HSN Code");tModel.addColumn("EAN Code"); tModel.addColumn("Customer Article No");  tModel.addColumn("Item Desctiption"); 
        tModel.addColumn("Quantity");  tModel.addColumn("MRP"); tModel.addColumn("CGST");tModel.addColumn("SGST");tModel.addColumn("Cost Price"); tModel.addColumn("Base Cost Value");
        
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setForeground(Color.blue);
        
        table.setDefaultRenderer(Object.class, new TableRenderColor());
        
        table.getColumnModel().getColumn(0).setPreferredWidth(6);
        table.getColumnModel().getColumn(1).setPreferredWidth(15);
        table.getColumnModel().getColumn(2).setPreferredWidth(15);
        table.getColumnModel().getColumn(3).setPreferredWidth(20);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(20);
        table.getColumnModel().getColumn(6).setPreferredWidth(20);
        table.getColumnModel().getColumn(7).setPreferredWidth(10);
        table.getColumnModel().getColumn(8).setPreferredWidth(10);
        table.getColumnModel().getColumn(9).setPreferredWidth(20);
        table.getColumnModel().getColumn(10).setPreferredWidth(20);

        
        table.getTableHeader().setReorderingAllowed(true);
        
        
        // Grid for selecting customer_cmb Group and customer_cmb 
        JPanel panel = new JPanel();
        GridLayout gl = new GridLayout(3, 2);
        panel.setLayout(gl);

        JPanel panel1 = new JPanel();
        BorderLayout bl1 = new BorderLayout(1, 1);
        panel1.setLayout(bl1);
        
        JPanel panel2 = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
        panel2.setLayout(fl);

        JLabel l1 = new JLabel("Select Customer Group   ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("Select Customer Name   ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("Select PO File Name   ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        
        poFileTxt = new JTextField();
        poFileTxt.addKeyListener(this);
        //poFileTxt.setEnabled(false);
        //poFileTxt.setDisabledTextColor(Color.BLACK);
        panel1.add(poFileTxt, BorderLayout.CENTER);
        panel1.add(bt_file,BorderLayout.EAST);
        bt_file.addActionListener(this);
        bt_file.setMnemonic(KeyEvent.VK_O);
        bt_file.setToolTipText("Select PO File");
        bt_file.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoOpen)));
        
        panel2.add(bt_reset);
        panel2.add(bt_read);
        panel2.add(bt_validate);
        panel2.add(bt_email);
        panel2.add(bt_export);
        panel2.add(chk_xml);
        panel2.add(chk_xlsx);
        bt_read.addActionListener(this);
        bt_read.setMnemonic(KeyEvent.VK_E);
        bt_read.setToolTipText("Extract Selected PO File");
        bt_read.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoExtract)));
        bt_validate.addActionListener(this);
        bt_validate.setMnemonic(KeyEvent.VK_V);
        bt_validate.setToolTipText("Validate PO Details");
        bt_validate.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoValidate)));
        bt_export.addActionListener(this);
        bt_export.setMnemonic(KeyEvent.VK_X);
        bt_export.setToolTipText("Export PO Details");
        bt_export.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoExport)));
        bt_reset.addActionListener(this);
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setToolTipText("Clear Form Details");
        bt_reset.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));
        
        bt_email.addActionListener(this);
        bt_email.setMnemonic(KeyEvent.VK_M);
        bt_email.setToolTipText("Email PO Details");
        bt_email.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoEmail)));
        
        
           
        CustomerGroupDAO cgDAO = new CustomerGroupDAO();
  
        try{
        	Vector<KeyValueMaster> items = cgDAO.getCustomerGroupNames();
        	if (items!=null) {
        		custGroup_cmb = new JComboBox<>(items);
        		CustomerGroup cg = new CustomerGroup();
        		cg.setCgID(items.get(0).getId());
        		customer_cmb = new  JComboBox<>(cDAO.getCustomerNames(cg));
        	} else {
        		custGroup_cmb = new JComboBox<>();
        		customer_cmb = new  JComboBox<>();
        	}
        }catch(Exception er)
        {       }

        custGroup_cmb.addItemListener(this);
		customer_cmb.addItemListener(this);
        
        panel.add(l1);
        panel.add(custGroup_cmb);
        panel.add(l2);
        panel.add(customer_cmb);
        panel.add(l3);
        panel.add(panel1);
        
        JLabel jLogo = new JLabel();
        jLogo.setIcon(new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.POReaderLogoPath)));
        
        JPanel formHeader = new JPanel();
        formHeader.setLayout(new BorderLayout(2,2));
        formHeader.add(jLogo, BorderLayout.WEST);
        formHeader.add(panel, BorderLayout.CENTER);
        formHeader.add(poHeaderTxt,BorderLayout.SOUTH);
        
        JPanel formFooter = new JPanel();
        JPanel footerNetPrice = new JPanel();
        formFooter.setLayout(new BorderLayout(2,2));
        formFooter.add(panel2,BorderLayout.CENTER);
        formFooter.add(lblRows,BorderLayout.WEST);
        footerNetPrice.setLayout(new FlowLayout());
        footerNetPrice.add(lblActualNetValue);
        footerNetPrice.add(lblPONetValue);
        formFooter.add(footerNetPrice,BorderLayout.EAST);
        
        getContentPane().add(formHeader,BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(table),BorderLayout.CENTER);
        getContentPane().add(formFooter,BorderLayout.SOUTH);
        
	} // End of constructor
	
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource().equals(custGroup_cmb)) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
            	KeyValueMaster item = (KeyValueMaster)custGroup_cmb.getSelectedItem();
                CustomerGroup cg = new CustomerGroup();
         		cg.setCgID(item.getId());
         		try{
         			DefaultComboBoxModel< KeyValueMaster> model = new DefaultComboBoxModel<>(cDAO.getCustomerNames(cg));
         			customer_cmb.setModel(model);
         			if (customer_cmb.getItemCount()>0 ){
         				customer_cmb.setSelectedIndex(0);
         			}
         		}catch(Exception ee){
         			log.error("Error selecting customer Group details : "+ee.getMessage());
         		}
            	//JOptionPane.showMessageDialog(this, "Item Selected :" + custGroup_cmb.getSelectedItem());
            }
		}  
		if (e.getSource().equals(customer_cmb)){
		       if(e.getStateChange() == ItemEvent.SELECTED) {
	            	KeyValueMaster item = (KeyValueMaster)customer_cmb.getSelectedItem();
	            	CustomerDAO cDAO = new CustomerDAO();
	         		try{
		            	customer = cDAO.getCustomer(item.getId());
	         		}catch(Exception ee){
	         			log.error("Error selecting customer details : "+ee.getMessage());
	         		}
	            }

		}
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("Select PO")){
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "PDF Files", "pdf");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	poFileTxt.setText(chooser.getSelectedFile().getAbsolutePath() );
		    	tModel.setNumRows(0);
		    	poHeaderTxt.setText("");
		    	lblRows.setText("");
				poInfo = null;
				custMarginPercent =0;
		    }
		}
		else if(actionEvent.getActionCommand().equals("Export")){
			exportPODetails();
		}
		else if (actionEvent.getActionCommand().equals("Extract")){
			table.setName("Extract");
   			extractPODetails();
		}else if (actionEvent.getActionCommand().equals("Validate")){
			
			if (poInfo==null){
				JOptionPane.showInternalMessageDialog(this, "Please Extract PO File before validating the Purchase Order Details","Empty PO Details!",JOptionPane.PLAIN_MESSAGE);
				return;
			}
			if (poInfo.isOutStation()) {
				table.setName("Validate-2");
			} else {
				table.setName("Validate-1");
			}
			validatePODetails();
		}
		else if (actionEvent.getActionCommand().equals("Reset")){
			formClear();
		}
		else if (actionEvent.getActionCommand().equals("Email")){
			if (poInfo==null){
				JOptionPane.showInternalMessageDialog(this, "Please Extract PO File before sending the Email","Empty PO Details!",JOptionPane.PLAIN_MESSAGE);
				return;
			}
			Email email=new Email();
			email.setFrom( POReaderReadEnv.getEnvValue(POReaderEnvProp.EmailFrom));
			email.setTo(customer.getEmail());
	    	email.setMsg_body(getHTMLMessage());
	    	email.setSubject("PO Number - " + poInfo.getPoNumber());
			EmailForm ef = new EmailForm(email);
			ef.setVisible(true);
			//sendEmail();
		}
		
		
	}

	private boolean formValidation() {
		boolean retVal = true;
		if (poFileTxt.getText().trim().length()==0){
			JOptionPane.showInternalMessageDialog(this, "Please Select the PO File","Empty PO File!",JOptionPane.PLAIN_MESSAGE) ;
			return false;
		}
		KeyValueMaster item = (KeyValueMaster)customer_cmb.getSelectedItem();
		try{
        	customer = cDAO.getCustomer(item.getId());
 		}catch(Exception ee){
 			log.error("Error getting customer details:"+ee.getMessage());
 			JOptionPane.showInternalMessageDialog(this, "Please Select the Customer Name","Empty Customer!",JOptionPane.PLAIN_MESSAGE);
 			customer_cmb.requestFocus();
 			retVal=false;
 		}
		return retVal;
	}
	
 
	// This method is used to extract the PO Details from pdf file based on the 
	// selected customer_cmb Group, customer_cmb and PDF File 
	private void extractPODetails(){

		if (formValidation() == false) {
			return;
		}
		lblActualNetValue.setText("");
		lblPONetValue.setText("");
    	lblRows.setText("");
        POReader poReader = null;
        poInfo = null;
		tModel.setNumRows(0);
		tModel.setColumnCount(0);
		poHeaderTxt.setText("");
		if (poInfo == null){
			KeyValueMaster cgitem = (KeyValueMaster)custGroup_cmb.getSelectedItem();
			if (cgitem.getId()==POFormats.ReliancePO) {
				poReader = POReaderFactory.getPOReader(POFormats.ReliancePO,true);
			} else if (cgitem.getId()==POFormats.AdityaBirlaPO) {
				poReader = POReaderFactory.getPOReader(POFormats.AdityaBirlaPO,true);
			} else if (cgitem.getId()==POFormats.HiperCityPO) {
				poReader = POReaderFactory.getPOReader(POFormats.HiperCityPO,true);
			} else if (cgitem.getId()==POFormats.MaxHyperMarketPO) {
				poReader = POReaderFactory.getPOReader(POFormats.MaxHyperMarketPO,true);
			} else if (cgitem.getId()==POFormats.SpencerPO) {
				poReader = POReaderFactory.getPOReader(POFormats.SpencerPO,true);
			} else if (cgitem.getId()==POFormats.BigBazarPO) {
				poReader = POReaderFactory.getPOReader(POFormats.BigBazarPO,true);
			}
			if (poReader!=null){
				try{
					poInfo = poReader.getPODetails( poFileTxt.getText());
					tModel.addColumn("Sl No");tModel.addColumn("PO HSN Code");tModel.addColumn("PO EAN Code"); tModel.addColumn("PO Article No");  tModel.addColumn("Item Desctiption"); 
			        tModel.addColumn("PO Quantity");  tModel.addColumn("PO MRP"); 
			        if (poInfo.isOutStation()) {
			        	tModel.addColumn("PO IGST");
			        } else {
			        	tModel.addColumn("PO CGST");tModel.addColumn("PO SGST");
			        }
			        tModel.addColumn("PO Base Cost");  tModel.addColumn("Total Base Value");

				}catch(Exception er)
				{ 
					log.error("Error etracting PO details : "+er.getMessage());
					JOptionPane.showInternalMessageDialog(this, er.getMessage() + "\n Note: Please verify if the selected PO is for right customer!","Error! Extracting PO Details",JOptionPane.ERROR_MESSAGE) ;
					poInfo = null;
					return;
				}
			} 
		}
		this.setCursor( new Cursor(Cursor.WAIT_CURSOR));
		float NetTotalPrice = 0;
		if (poInfo!=null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YYYY");
			List<PurchaseOrderItems> items = poInfo.getPoItems();
			
			String poHeadInfo = "<html><body><table><tr><td><font color=blue>PO Date : </font>" + sdf.format(poInfo.getPoDate() ) + "</td>";
			poHeadInfo = poHeadInfo + "<td><font color=blue>PO Number : </font>" + poInfo.getPoNumber() + "</td><td> <font color=blue>Out Station PO:</font> " + (poInfo.isOutStation()+"").toUpperCase() + "</td></tr>";
			poHeadInfo = poHeadInfo + "<tr><td><font color=blue>Delivery Date :</font> " + sdf.format(poInfo.getDeliveryDate()) + "</td>";
			poHeadInfo = poHeadInfo + "<td><font color=blue>Customer Name :</font> " + poInfo.getCustomerName() + "</td></tr></table></body></html>";
			
			poHeaderTxt.setText(poHeadInfo);
			String itemNum="";
			for(int i=0;i<items.size();i++) {
				PurchaseOrderItems row = items.get(i);
				itemNum = (i+1) +"";
				if (i<9) {
					itemNum = "0" + (i+1);
				} 
				NetTotalPrice = NetTotalPrice + row.getTotalBaseValue();
				if (poInfo.isOutStation()) {
					tModel.addRow(new Object[] { itemNum, row.getHsnNo(), row.getEanNo() , row.getArticleNo(),row.getMaterialDesc() , 
						qty.format(row.getQuantity()), floatVal.format(row.getMrp()), floatVal.format(row.getCgst()  + row.getSgst()), floatVal.format(row.getBasicCost()) , floatVal.format(row.getTotalBaseValue() )});
				} else {
					tModel.addRow(new Object[] { itemNum, row.getHsnNo(), row.getEanNo() , row.getArticleNo(),row.getMaterialDesc() , 
							qty.format(row.getQuantity()), floatVal.format(row.getMrp()), floatVal.format(row.getCgst() ), floatVal.format(row.getSgst()), floatVal.format(row.getBasicCost()),floatVal.format(row.getTotalBaseValue()) });
				}
			}
		    lblRows.setText("PO Extraction - Item Count : " + items.size());
		    lblPONetValue.setForeground(Color.BLUE);
	    	lblPONetValue.setText("PO Net Total Price : " + floatVal.format(NetTotalPrice));
		}
		this.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));

	}

	
	// This method is used to validate the PO Details from actual item details   
	// the comparision is made on the PO rates with actual item rates   
	private void validatePODetails( ){
		lblActualNetValue.setText("");
		lblPONetValue.setText("");
		this.setCursor( new Cursor(Cursor.WAIT_CURSOR));
		List<PurchaseOrderItems> items = poInfo.getPoItems();
		float netTotPrice = 0;
		float netPOTotPrice = 0;
		float actualBaseCost=0;
		float aCostPrice = 0;

		tModel.setNumRows(0);
		tModel.setRowCount(0);
		tModel.setColumnCount(0);
		tModel.addColumn("HSN Code"); tModel.addColumn("PO HSN Code");tModel.addColumn("PO EAN Code"); 
	    tModel.addColumn("Item Description"); tModel.addColumn("Customer %");tModel.addColumn("Quantity"); tModel.addColumn("MRP"); tModel.addColumn("PO MRP");  
	    if (poInfo.isOutStation()) {
	    	tModel.addColumn("IGST"); tModel.addColumn("PO IGST");
	    } else {
	    	tModel.addColumn("CGST"); tModel.addColumn("PO CGST"); tModel.addColumn("SGST"); tModel.addColumn("PO SGST");
	    }
	    tModel.addColumn("Cost Price"); tModel.addColumn("PO Cost Price");tModel.addColumn("Basic Cost Value");	tModel.addColumn("PO Basic Cost Value");	    
	    
	    String eanList = "";
	    for(int i=0;i<items.size();i++) {
	    	eanList = eanList + "'" +  items.get(i).getEanNo() + "',";
	    }
	    lblRows.setText("PO Validation - Item Count : " + items.size());
	    if (eanList.length()>0) {
	    	eanList = eanList.substring(0, eanList.length()-1);
	    }
		ItemDAO iDao = new ItemDAO();
		Map<String, ItemWithCustomerMaringPercent > itemMap= null;
		try {
			itemMap = iDao.getItemDetailsWithCustomerMarginPercent(customer, eanList);
		}catch(FetchDataException  fexp) {
			log.error("Fetch Item Details with Customer Percent value Error !" + fexp.getMessage());
			fexp.printStackTrace();
			this.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
			JOptionPane.showInternalMessageDialog(this, "Unable to fetch item price details for the selected customer! \n"  + fexp.getMessage(),"Error! Fetching Item Price Details",JOptionPane.ERROR_MESSAGE) ;
			return;
		}
		KeyValueMaster cgitem = (KeyValueMaster)custGroup_cmb.getSelectedItem();
	    for(int i=0;i<items.size();i++) {
			PurchaseOrderItems row = items.get(i);
			actualBaseCost=0;
			aCostPrice = 0;
			ItemWithCustomerMaringPercent item = itemMap.get(row.getEanNo());
			if (item!=null) {	
				aCostPrice = item.getMrp() - (item.getMrp() * (item.getCustMarginPercent()  /100) );
				actualBaseCost = aCostPrice * (100/(100 + item.getItemPercent()));

				// Actual Quantity -- Presently bulk quantity available for Reliance,  
				float aQty = 1;
				if (cgitem.getId()==POFormats.ReliancePO) {
					if (row.getcQty()>0) {
						aQty = row.getQuantity() / row.getcQty(); 	
					} else {
						aQty = row.getQuantity();
					}
				}
				if (poInfo.isOutStation()) {
					tModel.addRow(new Object[] { item.getHsnCode(), row.getHsnNo(),  row.getEanNo() , item.getItemDesc(), floatVal.format(item.getCustMarginPercent()) , qty.format(row.getQuantity()),
							floatVal.format((item.getMrp())), floatVal.format(row.getMrp()),  floatVal.format(item.getItemPercent()), floatVal.format(row.getCgst() + row.getSgst()), 
							floatVal.format(actualBaseCost * aQty),floatVal.format(row.getBasicCost()) ,floatVal.format(actualBaseCost * row.getQuantity()) , floatVal.format(row.getTotalBaseValue())});
				} else {
					tModel.addRow(new Object[] { item.getHsnCode(), row.getHsnNo(),  row.getEanNo() ,  item.getItemDesc() , floatVal.format(item.getCustMarginPercent()),qty.format(row.getQuantity()),
							floatVal.format((item.getMrp())), floatVal.format(row.getMrp()),  floatVal.format((item.getItemPercent()/2)), floatVal.format(row.getCgst()), floatVal.format((item.getItemPercent()/2)),floatVal.format(row.getSgst()), 
							floatVal.format(actualBaseCost * aQty), floatVal.format(row.getBasicCost()),floatVal.format(actualBaseCost * row.getQuantity()) , floatVal.format(row.getTotalBaseValue()) });
				}
				netTotPrice = netTotPrice +  (actualBaseCost * row.getQuantity());
			} else {
				if (poInfo.isOutStation()) {
					tModel.addRow(new Object[] { "", row.getHsnNo(),  row.getEanNo() , "","", qty.format(row.getQuantity()),
							"",floatVal.format(row.getMrp())  , "", floatVal.format(row.getCgst() + row.getSgst()), 
							 "",floatVal.format(row.getBasicCost()),"", floatVal.format(row.getTotalBaseValue())});
				} else {
					tModel.addRow(new Object[] { "", row.getHsnNo(), row.getEanNo() , "","", qty.format(row.getQuantity()),
							"",floatVal.format(row.getMrp()), "", floatVal.format(row.getCgst()), "",floatVal.format(row.getSgst()), 
						 "",floatVal.format(row.getBasicCost()),"",floatVal.format(row.getTotalBaseValue())});
				}
			}
			netPOTotPrice = netPOTotPrice + row.getTotalBaseValue();
		}
	    
	    if (qty.format(netTotPrice).equals(qty.format(netPOTotPrice))) {
	    	lblPONetValue.setForeground(Color.BLUE);
	    } else {
	    	lblPONetValue.setForeground(Color.RED);
	    }
	    lblActualNetValue.setText("Net Total Price : " + floatVal.format(netTotPrice) + "  | ");
	    lblPONetValue.setText("PO Net Total Price : " +  floatVal.format(netPOTotPrice));
	    this.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	// This method is used to export PO detail to selected formats like  
	// xlsx, xml  
	private void exportPODetails(){
		try{
			boolean compare = false;
			if (table.getName().contains("Validate")) {
				compare = true;
			}
			
			if (poInfo==null){
				JOptionPane.showInternalMessageDialog(this, "Please Extract PO File before exporting the Purchase Order Details","Alert!",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (chk_xml.isSelected()==false && chk_xlsx.isSelected()==false){
				JOptionPane.showInternalMessageDialog(this, "Please select the export type - either xml or xls","Information!",JOptionPane.INFORMATION_MESSAGE) ;
				return;
			}
			String outputFolder = POReaderReadEnv.getEnvValue(POReaderEnvProp.XSLXExportFolder);
			String fileName = poFileTxt.getText().substring( poFileTxt.getText().lastIndexOf(File.separator));
			fileName = fileName.substring(0,fileName.lastIndexOf("."));
			String xlsFileName = outputFolder + "/" + fileName +  ".xls";
			String xmlFileName = outputFolder + "/" + fileName + ".xml";
			
			this.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			if (chk_xml.isSelected()==false && chk_xlsx.isSelected()==true) {
				if (Export2Excel.exportPODetails(poInfo, POUtil.convertToList(table), xlsFileName,compare )) {
					JOptionPane.showInternalMessageDialog(this, "File Exported Successfully to \n" +new File(xlsFileName).getAbsolutePath()  ,"Success!",JOptionPane.PLAIN_MESSAGE) ;
				} else {
					JOptionPane.showInternalMessageDialog(this, "Unable to Exported File ","Alert!",JOptionPane.ERROR_MESSAGE) ;
				}
			} 
			else if (chk_xml.isSelected()==true && chk_xlsx.isSelected()==false) {
				if (Export2XML.exportPODetails(poInfo, POUtil.convertToList(table), xmlFileName )) {
					JOptionPane.showInternalMessageDialog(this, "File Exported Successfully to \n " + new File(xmlFileName).getAbsolutePath(),"Success!",JOptionPane.PLAIN_MESSAGE) ;
				} else {
					JOptionPane.showInternalMessageDialog(this, "Unable to Exported File","Alert!",JOptionPane.ERROR_MESSAGE) ;
				}
			} 
			else if (chk_xml.isSelected()==true && chk_xlsx.isSelected()==true) {
				int  expStatus = 0; 
				if (Export2Excel.exportPODetails(poInfo, POUtil.convertToList(table), xlsFileName,compare )) {
					expStatus++;
				} 
				if (Export2XML.exportPODetails(poInfo, POUtil.convertToList(table), xmlFileName )) {
					expStatus++;
				} 
				if (expStatus==2) {
					JOptionPane.showInternalMessageDialog(this, "File(s) Exported Successfully to \n" + new File(outputFolder).getAbsolutePath(),"Success!",JOptionPane.PLAIN_MESSAGE) ;
				} else {
					JOptionPane.showInternalMessageDialog(this, "Error in File(s) Export ","Alert!",JOptionPane.ERROR_MESSAGE) ;
				}

			} 
		}catch(Exception er){
			log.error("Unable to export PO details : "+er.getMessage());
			JOptionPane.showInternalMessageDialog(this, "Unable to export PO Details!","Alert!",JOptionPane.ERROR_MESSAGE) ;
		}
		this.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	private void formClear(){
		tModel.setNumRows(0);
		poHeaderTxt.setText("");
		poFileTxt.setText("");
		lblRows.setText("");
		lblPONetValue.setText("");
		lblActualNetValue.setText("");
		poInfo = null;
		custMarginPercent =0;
	}
	
	
	private String getHTMLMessage(){
    	String htmlMsg = "<html> <body>";
    	htmlMsg = htmlMsg + "Dear Sir/Madam, <br>";
    	htmlMsg = htmlMsg + "Please find the below table with PO Number :" + poInfo.getPoNumber() + " having descrepencies marked in red color<br>";
    	htmlMsg = htmlMsg + "Kindly request you to please updated the PO accordingly and send the same. <br><br><br>";
    	htmlMsg = htmlMsg + POUtil.buildEmailMessageTable(table,0) + "<br> Thank you in advance <br><br>";
    	htmlMsg = htmlMsg + "Regards<br>Takkaiah and Co. <br> </body></html>";
    	return htmlMsg;
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource().equals(poFileTxt))
		{
			e.consume();
		} 
		
	}

}
