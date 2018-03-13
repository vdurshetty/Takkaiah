package com.takkaiah.poreader.util;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.takkaiah.logger.POReaderLogger;

public class POUtil {
	
    private static final String EMAIL_PATTERN = 
	        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
	static POReaderLogger log = POReaderLogger.getLogger(POUtil.class.getName());

    
  	
	public static List<Object[]> convertToList(JTable table){
		List<Object[]> poRows = new ArrayList<>();
		int colCount = table.getColumnModel().getColumnCount();
		Object colRow[] = new Object[colCount];
		int cols=0;
		Enumeration<TableColumn> colNames = table.getColumnModel().getColumns();
	    while (colNames.hasMoreElements()){
	    	TableColumn tcol = colNames.nextElement();
	    	colRow[cols++] = tcol.getHeaderValue().toString();
	    } 
	    poRows.add(colRow);	
    	for (int j=0;j<table.getRowCount();j++){
		   Object row[] = new Object[colCount];
	    	 for (int k=0;k < colCount;k++){
	    		 	if (table.getValueAt(j, k)==null)
	    		 		row[k] = "";
	    		 	else
	    		 		row[k] = table.getValueAt(j, k).toString();
	    	 }
	    	 poRows.add(row);		 
	    }
		return poRows;
	}
	
	public static String buildEmailMessageTable(JTable table,int startCol){
		String msgTable = "<table border=1> <tr>";
		String tableData = "";
		try{
		int colCount = table.getColumnModel().getColumnCount();
		Enumeration<TableColumn> colNames = table.getColumnModel().getColumns();
		int colInc = 0;
	    while (colNames.hasMoreElements()){
    		TableColumn tcol = colNames.nextElement();
	    	if (colInc >= startCol) {
		    	if (tcol.getHeaderValue().toString().length()>0){
		    		msgTable = msgTable + "<td bgcolor=yellow>" + tcol.getHeaderValue().toString() + "</td>";
		    	}
	    	}
	    	colInc++;
	    } 
	    msgTable = msgTable + "</tr>";
	    
    	for (int j=0;j<table.getRowCount();j++)
    	{
	 	   msgTable = msgTable + "<tr>";		      
	    	 for (int k=startCol;k < colCount;k++){
    			 tableData=""; 
	    		 if (table.getValueAt(j, k)!=null){
	    			 tableData=  table.getValueAt(j, k).toString().trim();
	    		 }
	    		 if (isNumeric(tableData))
	    		 {
	    			 if (colCount>7)
	    			 {
	    				if (k==3 || k==5 || k==7)
	    				{
		   	    			 float val1 = 0;
			    			 float val2 = 0;
		    				 val1 = Float.parseFloat(tableData);
			    			 if (table.getValueAt(j, k+1)!=null){
				    			 try{
				    				 val2 = Float.parseFloat(table.getValueAt(j, k+1).toString().trim());
				    			 }catch(NumberFormatException nfe){
				    			 }
			    			 }
			    			 if (val1!=val2){
			    				 msgTable = msgTable + "<td > <p align=right> <font color=red>" + tableData + "</font></p></td>";
			    			 } else {
			    				 msgTable = msgTable + "<td> <p align=right>" + tableData + "</p></td>";
			    			 }
	    				} else {
		    				 msgTable = msgTable + "<td> <p align=right>" + tableData + "</p></td>";
	    				}
	    			 } else {
	    				 msgTable = msgTable + "<td> <p align=right>" + tableData + "</p></td>";
	    			 }
	    		 } else {
	    			msgTable = msgTable + "<td>" + tableData + "</td>";
	    		 }
	    	 }
	 	    msgTable = msgTable + "</tr>";		      
	    }
		}catch(Exception er){
			log.error("Error Reading Table Data : " + er.getMessage());
		}
    	msgTable = msgTable + "</table>";
		return msgTable;
	}
	
	private static boolean isNumeric(String txt){
		boolean status=false;
		try{
			Double.parseDouble(txt);
			status =true;
		}catch(Exception e){
		}
		return status;
	}
	
	
	
	public static ImageIcon getSmallImage(String ButtonLogo){
	      ImageIcon icon = new ImageIcon(ButtonLogo);
	      Image img = icon.getImage() ;  
	      Image newimg = img.getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH ) ;  
	      icon = new ImageIcon( newimg );
	      return icon;
	}
	
	public static boolean EmailValidator(final String hex) {
		    Pattern pattern;
		    Matcher matcher;
		    pattern = Pattern.compile(EMAIL_PATTERN);
	        matcher = pattern.matcher(hex);
	        return matcher.matches();
	}
	
	public static String getHTMLColor(Color color) {
	    if (color == null) {
	        return "#000000";
	    }
	    return "#" + Integer.toHexString(color.getRGB()).substring(2).toUpperCase();
	}
	
}
