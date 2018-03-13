package com.takkaiah.poreader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import com.takkaiah.db.dao.MRPTypeDAO;
import com.takkaiah.db.dto.MRPType;
import com.takkaiah.db.exception.PrimaryKeyException;
import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.poreader.util.POReaderParams;
import com.takkaiah.poreader.util.POUtil;

public class MRPTypeForm extends JInternalFrame implements ActionListener, MouseListener, KeyListener{

	private static final long serialVersionUID = 1L;
	POReaderLogger log = POReaderLogger.getLogger(MRPTypeForm.class.getName());

	
	MRPTypeDAO ptDAO = new MRPTypeDAO();
	JTable table;
	JButton  bt_add = new JButton("Add");
	JButton  bt_update = new JButton("Update");
	JButton  bt_delete = new JButton("Delete");
	JButton  bt_reset = new JButton("Reset");
    
	JTextField  ptID_txt = new JTextField();
	JTextField  ptName_txt = new JTextField();
	JTextField  ptAlias_txt = new JTextField();
	JTextField  taxPercent_txt = new JTextField();
	JTextArea  remarks_txt = new JTextArea();
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

	
	public MRPTypeForm() {
		addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
            	//dispose();
            	setVisible(false);
            }
        }); 
		setTitle("MRP Types");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(false);
		setClosable(true);
		setBounds(100, 100, 650, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table=new JTable(tModel);
		table.setAutoCreateRowSorter(true);
	    tModel.addColumn("PT ID"); tModel.addColumn("Price Type Name");  
	    tModel.addColumn("Price Type Alias");  tModel.addColumn("Tax Percent"); tModel.addColumn("Remarks"); 

        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        //table.getTableHeader().setForeground(Color.blue);

        
        // Interchange columns in table
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(this);
        table.addKeyListener(this);
        table.getTableHeader().setForeground(Color.blue);
        
        ptID_txt.setEnabled(false);
        ptID_txt.setDisabledTextColor(Color.BLACK);
        status_label.setForeground(Color.BLUE);

        
            
        JPanel formPanel = new JPanel();
        GridLayout gl = new GridLayout(3, 2);
        formPanel.setLayout(gl);

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
        bt_add.setToolTipText("Add MRP Type Details");
        bt_add.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoAdd)));
        bt_update.setMnemonic(KeyEvent.VK_U);
        bt_update.setToolTipText("Update MRP Type Details");
        bt_update.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoUpdate)));
        bt_delete.setMnemonic(KeyEvent.VK_D);
        bt_delete.setToolTipText("Delete MRP Type Details");
        bt_delete.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoDelete)));
        bt_reset.setMnemonic(KeyEvent.VK_R);
        bt_reset.setToolTipText("Clear Form Details");
        bt_reset.setIcon( POUtil.getSmallImage(POReaderReadEnv.getEnvValue(POReaderEnvProp.ButtonLogoReset)));
          

        JLabel l1 = new JLabel("  MRP Type ID  ");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l2 = new JLabel("  MRP Type Name  ");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l3 = new JLabel("  MRP Type Alias  ");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l4 = new JLabel("  Tax Percent  ");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel l5 = new JLabel("  Remarks  ");
        l5.setHorizontalAlignment(SwingConstants.RIGHT);

        JScrollPane scroll2 = new JScrollPane(remarks_txt);
        scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        formPanel.add(l1);
        formPanel.add(ptID_txt);
        
        formPanel.add(new JLabel(""));
        formPanel.add(new JLabel(" "));


    
        formPanel.add(l2);
        formPanel.add(ptName_txt);
        
        formPanel.add(l3);
        formPanel.add(ptAlias_txt);

        formPanel.add(l4);
        formPanel.add(taxPercent_txt);
        
        formPanel.add(l5);
        formPanel.add(scroll2);
        
        ptName_txt.setToolTipText("Enter MRP Type Name");
        ptAlias_txt.setToolTipText("Enter MRP Type Alias");
        taxPercent_txt.setToolTipText("Enter MRP Type Tax Percent");
        table.setToolTipText("Press F2 or Double Click to Select MRP Type Details");

        taxPercent_txt.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if (((caracter < '0') || (caracter > '9') || taxPercent_txt.getText().length() >= 5 )
                        && (caracter != '\b') && (caracter!='.')) {
                    e.consume();
                }
            }
        });

        
        JPanel cgpanel = new JPanel();
        cgpanel.setLayout(new BorderLayout(10,10));
        
        JLabel jLogo = new JLabel();
        ImageIcon icon = new ImageIcon(POReaderReadEnv.getEnvValue(POReaderEnvProp.MRPTypeLogoPath));
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
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane,BorderLayout.CENTER);
        getContentPane().add(status_label,BorderLayout.SOUTH);
        
        fillTable();
        ptName_txt.requestFocus();
        buttonEnable(POReaderParams.ButtonEnableForReset);
        
	}
	
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals("Add")){
			try{
				if (ptName_txt.getText().trim().length()==0){
					JOptionPane.showMessageDialog(this, "Please Enter MRP Type Name");
					ptName_txt.requestFocus();
					return;
				} else if (taxPercent_txt.getText().length()>0){
					try{
						Float.parseFloat(taxPercent_txt.getText());
					}catch(NumberFormatException nes){
						JOptionPane.showInternalMessageDialog(this, "Please Enter Decimal value for Tax Percent!");
						taxPercent_txt.requestFocus();
						return;
					}
				} 
				MRPType mrpType = new MRPType();
				mrpType.setMrpTName(ptName_txt.getText());
				mrpType.setAlias(ptAlias_txt.getText());
				mrpType.setRemarks(remarks_txt.getText());
				if (taxPercent_txt.getText().length()>0){
					mrpType.setMrpTaxPercent( Float.parseFloat(taxPercent_txt.getText().trim()));
				}
		        if (ptDAO.addMRPType(mrpType) ==true){
			        status_label.setText("MRP Type '" + ptName_txt.getText() + "' added successfully!");
			        fillTable();
			        buttonEnable(POReaderParams.ButtonEnableForAdd);
		        }
			}catch(PrimaryKeyException pke){
				JOptionPane.showInternalMessageDialog(this, "MRP Type Already Exists with the name - '" + ptName_txt.getText() + "'");
				ptName_txt.requestFocus();
			}catch(Exception ee){
				log.error("Unable to add MRP Type :" + ee.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to add the '" + ptName_txt.getText() + "' MRP Type");
			}
		}
		else if(actionEvent.getActionCommand().equals("Update")){
			try{
				MRPType mrpType = new MRPType();
				if (ptID_txt.getText().trim().length()==0){
					JOptionPane.showInternalMessageDialog(this, "Please select the MRP Type from the below table.");
					table.requestFocus();
					return;
				} else if (ptName_txt.getText().trim().length()==0){
					JOptionPane.showMessageDialog(this, "Please Enter MRP Type Name");
					ptName_txt.requestFocus();
					return;
				} else if (taxPercent_txt.getText().length()>0){
					try{
						Float.parseFloat(taxPercent_txt.getText());
					}catch(NumberFormatException nes){
						JOptionPane.showInternalMessageDialog(this, "Please Enter Decimal value for Tax Percent!");
						taxPercent_txt.requestFocus();
						return;
					}
				} 
				mrpType.setMrpTID(Integer.parseInt(ptID_txt.getText() ));
				mrpType.setMrpTName(ptName_txt.getText());
				mrpType.setAlias(ptAlias_txt.getText());
				mrpType.setRemarks(remarks_txt.getText());
				if (taxPercent_txt.getText().length()>0){
					mrpType.setMrpTaxPercent( Float.parseFloat(taxPercent_txt.getText().trim()));
				}
		        if (ptDAO.updateMRPType(mrpType) ==true){
			        status_label.setText("MRP Type '" + ptName_txt.getText() + "' updated successfully!");
			        fillTable();
			        buttonEnable(POReaderParams.ButtonEnableForUpdate);
		        }
			}catch(Exception e){
				log.error("Unable to update MRP Type :" + e.getMessage());
				JOptionPane.showInternalMessageDialog(this, "Unable to update the '" + ptName_txt.getText() + "' MRP Type");
			}	
		}
		else if (actionEvent.getActionCommand().equals("Reset")){
			formClear();
			
		}
		else if (actionEvent.getActionCommand().equals("Delete")){
			MRPType mrpType = new MRPType();
			if (ptID_txt.getText().trim().length()==0){
				JOptionPane.showInternalMessageDialog(this, "Please Select the MRP Type from the below table.");
				table.requestFocus();
			} 
			else 
			{
				if (JOptionPane.showInternalConfirmDialog(this, "Do you want to delete the MRP Type - '" + ptName_txt.getText() + "'?","Confirmation", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION) 
				{
					mrpType.setMrpTID(Integer.parseInt(ptID_txt.getText() ));
					mrpType.setMrpTName(ptName_txt.getText());
					mrpType.setAlias(ptAlias_txt.getText());
					//mrpType.setMrpTaxPercent(taxPercent_txt.getText());
					mrpType.setRemarks(remarks_txt.getText());
					try{
							if (ptDAO.deleteMRPType(mrpType)==true){
								String mprTypeName = ptName_txt.getText();
								formClear();
								status_label.setText("MRP Type '" + mprTypeName + "' deleted successfully!");
								fillTable();
								buttonEnable(POReaderParams.ButtonEnableForDelete);
							}	
						}catch(Exception e){
							log.error("Unable to delete MRP Type :" + e.getMessage());
							JOptionPane.showInternalMessageDialog(this, "Unable to delete the '" + ptName_txt.getText() + "' MRP Type \n It has has the references in Item Master..");
						}
				}
			}
		}
	}


	@Override
	public void keyPressed(KeyEvent ke) {
	    if (ke.getKeyCode() == KeyEvent.VK_F2) {
	        int rowNum = table.getSelectedRow();
  	        ptID_txt.setText( table.getValueAt(rowNum, 0).toString());
		    ptName_txt.setText( table.getValueAt(rowNum, 1).toString());
		    ptAlias_txt.setText( table.getValueAt(rowNum, 2).toString());
		    taxPercent_txt.setText( table.getValueAt(rowNum, 3).toString());
		    if (table.getValueAt(rowNum, 4)!=null)
		    	remarks_txt.setText( table.getValueAt(rowNum, 4).toString());
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
	        ptID_txt.setText( table.getValueAt(rowNum, 0).toString());
	    	ptName_txt.setText( table.getValueAt(rowNum, 1).toString());
	    	ptAlias_txt.setText( table.getValueAt(rowNum, 2).toString());
	    	taxPercent_txt.setText(table.getValueAt(rowNum, 3).toString() );
		    if (table.getValueAt(rowNum, 4)!=null)
		    	remarks_txt.setText( table.getValueAt(rowNum, 4).toString());
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
	        	List<MRPType> ptList = ptDAO.getAllMRPTypes();
	        	if (ptList!=null) {
					for(int i=0;i<ptList.size();i++) {
						MRPType row = ptList.get(i);
						tModel.addRow(new Object[] { row.getMrpTID() , row.getMrpTName(),row.getAlias(), row.getMrpTaxPercent(), row.getRemarks() });
					}
	        	} 
	        }catch(Exception er)
	        {    log.error("Unable to fill MRP Type :" + er.getMessage());   }
			
		}
		
		public void formClear(){
			ptID_txt.setText("");
			ptName_txt.setText("");
			ptAlias_txt.setText("");
			remarks_txt.setText("");
			status_label.setText(" ");
			taxPercent_txt.setText("");
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
