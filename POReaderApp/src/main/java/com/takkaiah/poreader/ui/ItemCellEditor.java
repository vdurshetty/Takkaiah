package com.takkaiah.poreader.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;


  class ItemCellEditor extends DefaultCellEditor 
    {
	  /**
	 * 
	 */
	  private static final long serialVersionUID = 1L;
	  private String msg ="";
	  public ItemCellEditor(String msg)
      {
        super( new JTextField() );
		this.msg= msg;
      }
public boolean stopCellEditing()
{
    //JTable table = (JTable)getComponent().getParent();
 
            try
            {
                String editingValue = (String)getCellEditorValue();
                try{
                	if (editingValue.trim().length()>0) {
                		Float.parseFloat(editingValue);
                	}
                }catch(NumberFormatException ne){
                    JTextField textField = (JTextField)getComponent();
                    textField.setBorder(new LineBorder(Color.red));
                    textField.selectAll();
                    textField.requestFocusInWindow();
                    JOptionPane.showInternalMessageDialog(this.getComponent().getParent() , "Please enter proper " + msg + " Value.", "Alert!",JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            catch(ClassCastException exception)
            {
                return false;
            }
 
            return super.stopCellEditing();
        }
 
        public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column)
        {
            Component c = super.getTableCellEditorComponent(
                table, value, isSelected, row, column);
            ((JComponent)c).setBorder(new LineBorder(Color.black));
 
            return c;
        }

	
    }