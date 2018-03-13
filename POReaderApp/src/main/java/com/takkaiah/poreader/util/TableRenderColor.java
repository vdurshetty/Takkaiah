package com.takkaiah.poreader.util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class TableRenderColor extends DefaultTableCellRenderer{
	private static final long serialVersionUID = 1L;
    
   
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, 
              boolean focused, int row, int column) {

                //setEnabled(table == null || table.isEnabled());

                setForeground(Color.black);
                setBackground(Color.white);
                setHorizontalAlignment(SwingConstants.LEFT);

            	// only if validation is clicked and 1 -> local and 2-> outstation
                if (table.getName().equals("Validate-2") || table.getName().equals("Validate-1")) {
                	float leftColVal = 0;
                	float rightColVal = 0;
               	
                   	if (column == 7) {
	                	if (table.getValueAt(row,6).toString().length()>0 && table.getValueAt(row,6).toString().length()>0 ){
	                		leftColVal = Float.parseFloat(table.getValueAt(row,6).toString());
	                		rightColVal = Float.parseFloat(table.getValueAt(row,7).toString());
	                    	if (leftColVal!=rightColVal){
	                			setBackground(Color.red);
	                    	}
	                	}
                	}

                	
                	if (column == 9) {
	                	if (table.getValueAt(row,8).toString().length()>0 && table.getValueAt(row,9).toString().length()>0 ){
	                		leftColVal = Float.parseFloat(table.getValueAt(row,8).toString());
	                		rightColVal = Float.parseFloat(table.getValueAt(row,9).toString());
	                    	if (leftColVal!=rightColVal){
	                			setBackground(Color.red);
	                    	}
	                	}
                	}
                	
                	if (column == 11) {
	                	if (table.getValueAt(row,10).toString().length()>0 && table.getValueAt(row,11).toString().length()>0 ){
	                		leftColVal = Float.parseFloat(table.getValueAt(row,10).toString());
	                		rightColVal = Float.parseFloat(table.getValueAt(row,11).toString());
	                    	if (leftColVal!=rightColVal){
	                			setBackground(Color.red);
	                    	}
	                	}
                	}

                   	if (column == 13) {
	                	if (table.getValueAt(row,12).toString().length()>0 && table.getValueAt(row,13).toString().length()>0 ){
	                		leftColVal = Float.parseFloat(table.getValueAt(row,12).toString());
	                		rightColVal = Float.parseFloat(table.getValueAt(row,13).toString());
	                    	if (leftColVal!=rightColVal){
	                			setBackground(Color.red);
	                    	}
	                	}
                	}
                   	
                  	if (table.getName().equals("Validate-1")) {
                       	if (column == 15) {
    	                	if (table.getValueAt(row,14).toString().length()>0 && table.getValueAt(row,15).toString().length()>0 ){
    	                		leftColVal = Float.parseFloat(table.getValueAt(row,14).toString());
    	                		rightColVal = Float.parseFloat(table.getValueAt(row,15).toString());
    	                    	if (leftColVal!=rightColVal){
    	                			setBackground(Color.red);
    	                    	}
    	                	}
                    	}
                 	}	
                	if (column > 3) {
                		setHorizontalAlignment(SwingConstants.RIGHT);
                	} 
                } else { 
	            	if (column > 4) {
	            		setHorizontalAlignment(SwingConstants.RIGHT);
	            	}
                }
                super.getTableCellRendererComponent(table, value, selected, focused, row, column);

                return this;
    }
    
}