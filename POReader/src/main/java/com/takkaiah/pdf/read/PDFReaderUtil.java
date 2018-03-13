package com.takkaiah.pdf.read;

import java.util.ArrayList;
import java.util.List;

public class PDFReaderUtil {
	
	public static final String POColumnsDelimeter="#";
	
	public PDFReaderUtil(){
		
	}
	
	 // Remove empty lines from the total page lines
	public static String removeEmptyLines(String data){
	  	 String lines[] = data.split("\\r?\\n");
	  	 String retStr="";
     	 for (int i=0;i<lines.length;i++){
    		 if (lines[i].trim().length()>0){
    			 retStr = retStr + lines[i].trim() + "\n";
    		 }
    		 
    	 }
    	 return retStr.trim();
	}
	
	public static String removeExtraSpaces(String line) {
		String retStr="";
		line = line.trim();
		List<String> cols = new ArrayList<String>();
		String lineCols[] = line.split(" ");
		for (int i=0;i<lineCols.length;i++){
			String colVal = lineCols[i].trim();
			if (colVal.length() > 0){
				cols.add(colVal);
			}
		}
		for (int j=0;j<cols.size();j++){
			retStr=retStr + cols.get(j) + " ";
		}
		return retStr.trim();
	}
	
	
	public static String reverseString(String line) {
		String retStr="";
		String cols[] = line.trim().split(" ");
		for (int i=cols.length-1; i>=0;i--){
			retStr = retStr + cols[i] + " ";
		}
		return retStr.trim();
	}


}
