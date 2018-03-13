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
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import com.takkaiah.db.dao.ItemCategoryDAO;
import com.takkaiah.db.dto.ItemCategory;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;

public class ItemCategoryForm extends JInternalFrame implements ActionListener, MouseListener, KeyListener{

	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(ItemCategoryForm.class.getName());
	
	ItemCategoryDAO icDAO = new ItemCategoryDAO();
	JTable table;
	JButton  bt_add = new JButton("Add");
	JButton  bt_update = new JButton("Update");
	JButton  bt_delete = new JButton("Delete");
	JButton  bt_reset = new JButton("Reset");
    
	JTextField  icID_txt = new JTextField();
	JTextField  icName_txt = new JTextField();
	JTextField  icAlias_txt = new JTextField();
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
	
	public ItemCategoryForm() {
		addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
            	//dispose();
            	setVisible(false);
            }
        }); 
		setTitle("Item Category");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(false);
		setClosable(true);
		setBounds(100, 100, 550, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table=new JTable(tModel);
		table.setAutoCreateRowSorter(true);
	    tModel.addColumn("IC ID"); tModel.addColumn("Item Category Name");  
	    tModel.addColumn("Alias");    
	    
	    //DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        //rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(40);

       // table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setForeground(Color.blue);

        
        
        // Interchange columns in table
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(this);
        table.addKeyListener(this);
        
        icID_txt.setEnabled(false);
        icID_txt.setDisabledTextColor(Color.BLACK);
        status_label.setForeground(Color.BLUE);
        
        JPanel rightPanel = new JPanel();
        GridLayout gl1 = new GridLayout(2, 2);
        rightPanel.setLayout(gl1);
        
        JPanel leftPanel = new JPanel();
        GridLayout gl = new GridLayout(3, 2);
        leftPanel.setLayout(gl);

        JPanel panel2 = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,1,1);
        panel2.setLayout(fl);
        
        panel2.add(bt_add);
        panel2.add(bt_update);
        panel2.add(bt_delete);
        panel2.add(bt_reset);
        bt_add.addActionListener(this);
        bt_update.addActionListener(this);
        bt_delete.addActionListener(this);
        bt_reset.addActionListener(this);
        bt_add.setMnemonic(KeyEvent.VK_A);
        bt_add.setToolTipText("Add Item Category Details");
        bt_add.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoAdd)));
        bt_update.setMnemonic(KeyEvent.VK_U);
        bt_update.setToolTipText("Update Item Category Details");
        bt_update.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoUpdate)));
        bt_delete.setMnemonic(KeyEvent.VK_D);
        bt_delete.setToolTipText("Delete Item Category Details");
        bt_delete.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoDelete)));
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setToolTipText("Clear Form Details");
        bt_reset.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));        

        JLabel l1 = new JLabel("   Item Category ID   ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("   Item Category Name   ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("   Alias   ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);

        leftPanel.add(l1);
        leftPanel.add(icID_txt);
        leftPanel.add(l2);
        leftPanel.add(icName_txt);
        leftPanel.add(l3);
        leftPanel.add(icAlias_txt);
        
        icName_txt.setToolTipText("Enter Item Category Name");
        icAlias_txt.setToolTipText("Enter Item Category Alias");
        table.setToolTipText("Press F2 or Double to select Item Category Details");
        
        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
        
        JLabel jLogo = new JLabel();
        ImageIcon icon = new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.ItemCatLogoPath));
	    Image newimg = icon.getImage().getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH ) ;  
	    icon = new ImageIcon( newimg );
        jLogo.setIcon(icon);

        cgpanel.add(jLogo,BorderLayout.WEST);
        cgpanel.add(leftPanel , BorderLayout.CENTER);
        cgpanel.add(panel2,BorderLayout.SOUTH);
        

        // set Form Layout	
		
        BorderLayout bl = new BorderLayout(1,1);
		getContentPane().setLayout(bl);
		
        getContentPane().add(cgpanel,BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(table),BorderLayout.CENTER);
        getContentPane().add(status_label,BorderLayout.SOUTH);
        
        fillTable();
        buttonEnable(POReaderParams.ButtonEnableForReset);
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("Add")){
			try{
				if (icName_txt.getText().trim().length()==0){
					JOptionPane.showMessageDialog(this, "Please Enter Item Category Name");
					icName_txt.requestFocus();
					return;
				}
				ItemCategory ic = new ItemCategory();
				ic.setCatName(icName_txt.getText());
				ic.setCatAlias(icAlias_txt.getText());
		        if (icDAO.addItemCategory(ic) ==true){
			        status_label.setText("Item Category '" + icName_txt.getText() + "' added successfully!");
			        fillTable();
			        buttonEnable(POReaderParams.ButtonEnableForAdd);
		        }
			}catch(PrimaryKeyException pke){
				JOptionPane.showInternalMessageDialog(this, "Item Category Already Exists with the name - '" + icName_txt.getText() + "'");
				icName_txt.requestFocus();
			}catch(Exception ee){
				log.error("Unable to add Item Category: "+ ee.getMessage());
				JOptionPane.showMessageDialog(this, "Unable to add the '" + icName_txt.getText() + "' Item Category");
			}
		}
		else if(actionEvent.getActionCommand().equals("Update")){
			try{
				ItemCategory ic = new ItemCategory();
				if (icID_txt.getText().trim().length()==0){
					JOptionPane.showMessageDialog(this, "Please select the Item Category from the below table.");
					table.requestFocus();
				} else {
					ic.setCatID(Integer.parseInt(icID_txt.getText() ));
					ic.setCatName(icName_txt.getText());
					ic.setCatAlias( icAlias_txt.getText());
			        if (icDAO.updateItemCategory(ic) ==true){
				        status_label.setText("Item Category '" + icName_txt.getText() + "' updated successfully!");
				        fillTable();
				        buttonEnable(POReaderParams.ButtonEnableForUpdate);
			        }
				}
			}catch(Exception e){
				log.error("Unable to update Item Category: "+ e.getMessage());
				JOptionPane.showMessageDialog(this, "Unable to update the '" + icName_txt.getText() + "' Item Category");
			}	
		}
		else if (actionEvent.getActionCommand().equals("Reset")){
			formClear();
			
		}
		else if (actionEvent.getActionCommand().equals("Delete")){
			if (JOptionPane.showInternalConfirmDialog(this, "Do you want to delete the Item Category - '" + icName_txt.getText() + "'?","Confirmation", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION) 
			{

				try{
						ItemCategory ic = new ItemCategory();
						ic.setCatID(Integer.parseInt(icID_txt.getText() ));
						ic.setCatName(icName_txt.getText());
						ic.setCatAlias( icAlias_txt.getText());
				        if (icDAO.deleteItemCategory(ic)==true){
					        status_label.setText("Item Category '" + icName_txt.getText() + "' deleted successfully!");
				        	formClear();
					        fillTable();
					        buttonEnable(POReaderParams.ButtonEnableForDelete);
				        }
				}catch(Exception e){
					log.error("Unable to delete Item Category: "+ e.getMessage());
					JOptionPane.showMessageDialog(this, "Unable to delete the '" + icName_txt.getText() + "' Item Category \n It has has the references in Item Master..");
				}
			}
		}
	}


	@Override
	public void keyPressed(KeyEvent ke) {
	    if (ke.getKeyCode() == KeyEvent.VK_F2) {
	        int rowNum = table.getSelectedRow();
  	        icID_txt.setText( table.getValueAt(rowNum, 0).toString());
		    icName_txt.setText( table.getValueAt(rowNum, 1).toString());
		    icAlias_txt.setText( table.getValueAt(rowNum, 2).toString());
		    buttonEnable(POReaderParams.ButtonEnableForSearch);
       }				
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
        if (e.getClickCount() == 2) {
        	int rowNum = table.getSelectedRow();
	        icID_txt.setText( table.getValueAt(rowNum, 0).toString());
	    	icName_txt.setText( table.getValueAt(rowNum, 1).toString());
	    	icAlias_txt.setText( table.getValueAt(rowNum, 2).toString());
	    	buttonEnable(POReaderParams.ButtonEnableForSearch);
        }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	// User Defined Methods
		private void fillTable(){
	        tModel.setNumRows(0);
	        try{
	        	List<ItemCategory> icList = icDAO.getAllItemCategory();
	        	if (icList!=null) {
					for(int i=0;i<icList.size();i++) {
						ItemCategory row = icList.get(i);
						tModel.addRow(new Object[] { row.getCatID() , row.getCatName(),row.getCatAlias() });
					}
	        	} 
	        }catch(Exception er)
	        {   log.error("Unable to fetch Item Category: "+ er.getMessage());    }
			
		}
		
		public void formClear(){
			icID_txt.setText("");
			icName_txt.setText("");
			icAlias_txt.setText("");
			status_label.setText(" ");
			buttonEnable(POReaderParams.ButtonEnableForReset);

		}
	private void buttonEnable(int enableType){
			
			if (enableType == POReaderParams.ButtonEnableForAdd){
				bt_add.setEnabled(true);
				bt_update.setEnabled(false);
				bt_delete.setEnabled(false);
				bt_reset.setEnabled(true);
			} else if (enableType == POReaderParams.ButtonEnableForUpdate){
				bt_add.setEnabled(true);
				bt_update.setEnabled(false);
				bt_delete.setEnabled(false);
				bt_reset.setEnabled(true);
			} else if (enableType == POReaderParams.ButtonEnableForDelete){
				bt_add.setEnabled(true);
				bt_update.setEnabled(false);
				bt_delete.setEnabled(false);
				bt_reset.setEnabled(true);
			} else if (enableType == POReaderParams.ButtonEnableForReset){
				bt_add.setEnabled(true);
				bt_update.setEnabled(false);
				bt_delete.setEnabled(false);
				bt_reset.setEnabled(true);
			} else if (enableType == POReaderParams.ButtonEnableForSearch){
				bt_add.setEnabled(false);
				bt_update.setEnabled(true);
				bt_delete.setEnabled(true);
				bt_reset.setEnabled(true);
			}

		}

}
