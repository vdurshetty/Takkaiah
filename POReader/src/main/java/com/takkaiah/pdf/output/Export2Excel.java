package com.takkaiah.pdf.output;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;

public class Export2Excel {
	
	static POReaderLogger log = POReaderLogger.getLogger(Export2Excel.class.getName());

	public static boolean exportPODetails(PurchaseOrderInfo poInfo, List<Object[]> poList,String fileName, boolean Compare)  throws Exception{
		boolean status = false;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YYYY");
		String outputFolder = POReaderReadEnv.getEnvValue(POReaderEnvProp.XSLXExportFolder);
		if (fileName==null){
			log.error("Empty file name");
		}
		File oFolder = new File(outputFolder);
		if (!oFolder.exists()){
			oFolder.mkdir();
		}
		fileName = fileName.substring( fileName.lastIndexOf(File.separator));
		fileName = fileName.replaceAll(".pdf", ".xls");
		File file = new File(outputFolder + "/" + fileName);
		if (file.exists()){
			log.error("File Already Exists: " + file.getAbsolutePath());
		}else {
			File folder  =  new File(POReaderReadEnv.getEnvValue(POReaderEnvProp.XSLXExportFolder));
			if (!folder.exists() ) {
				folder.mkdirs();
			}
		}

		
		HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet spreadsheet = workbook.createSheet("PO Info");

	     HSSFRow row;

	      CellStyle borderLStyle = workbook.createCellStyle();
	      borderLStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      
	      CellStyle borderRStyle = workbook.createCellStyle();
	      borderRStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

	      HSSFCellStyle colorStyle = workbook.createCellStyle();
	      colorStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	      colorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      colorStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      colorStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      colorStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      colorStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      
	 	      
	      CellStyle noMatchColor = workbook.createCellStyle();
	      noMatchColor.setFillForegroundColor(IndexedColors.RED.getIndex());
	      noMatchColor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      noMatchColor.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      noMatchColor.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      noMatchColor.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      noMatchColor.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      noMatchColor.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

	      int rowid = 0;
          int cellid = 0;
          
	      row = spreadsheet.createRow(rowid++);
	      Cell cell1 = row.createCell(1); 
	      cell1.setCellValue("PO Number");  
	      Cell cell2 = row.createCell(2);
	      cell2.setCellValue(poInfo.getPoNumber());
	      
	      row = spreadsheet.createRow(rowid++);
	      Cell cell3 = row.createCell(1);
	      cell3.setCellValue("PO Date");
	      Cell cell4 = row.createCell(2);
	      cell4.setCellValue(sdf.format(poInfo.getPoDate()));

	      row = spreadsheet.createRow(rowid++);
	      Cell cell5 = row.createCell(1);
	      cell5.setCellValue("Delivery Date");
	      Cell cell6 = row.createCell(2);
	      cell6.setCellValue(sdf.format(poInfo.getDeliveryDate()));

	      row = spreadsheet.createRow(rowid++);
	      Cell cell7 = row.createCell(1);
	      cell7.setCellValue("Customer Name");
	      Cell cell8 = row.createCell(2);
	      cell8.setCellValue(poInfo.getCustomerName());

	      Object[] colVal =  poList.get(0);
	      int colcount = colVal.length;
	      rowid++;
	      row = spreadsheet.createRow(rowid++);
	      cellid=0;
	      for (int i=0;i<colcount;i++){
	            Cell cell = row.createCell(cellid++);
            	cell.setCellStyle(colorStyle);
            	cell.setCellValue((String)colVal[i]); 
	      }
	      for (int j=1;j<poList.size();j++){
		      Object[] rowVal =  poList.get(j);
		      row = spreadsheet.createRow(rowid++);
		      cellid=0;
		      for (int k=0;k<rowVal.length;k++){
		            Cell cell = row.createCell(cellid++);
	            	cell.setCellValue((String)rowVal[k]);
	            	try{
		            	Float.parseFloat((String)rowVal[k]);
	            		cell.setCellStyle(borderRStyle);
	            	} catch(NumberFormatException nfe){
		            	cell.setCellStyle(borderLStyle);
	            	}
		            if (Compare){
		            	if (k==7 || k==9 || k==11 || k==13 || k==15){
		            		float val1 = 0;
		            		float val2 = 0;
		            		try{
			            		val1 = Float.parseFloat((String)rowVal[k-1]);
			            		val2 = Float.parseFloat((String)rowVal[k]);
		            		}catch(NumberFormatException nfe){           		}
			    			if (val1!=val2){
			    				cell.setCellStyle(noMatchColor);
			    			} 
		            	}
		            } 
		      }
	      }
	      
	      
	      // set the auto column size to all columns 
	      for (int l=0;l<colcount;l++){
	    	  spreadsheet.autoSizeColumn(l);  
	      }
	      
	      //Write the workbook in file system
	      FileOutputStream out = new FileOutputStream(file);
	      workbook.write(out);
	      out.close();
	      workbook = null;
	      log.debug("written successfully" );
	      status = true;
		return status;
	}

	
	public static boolean exportCustItems(List<Object[]> custItemList,String fileName)  throws Exception{
		boolean status = false;
		if (fileName==null){
			log.error("Empty file name");
		}
		File file = new File(fileName);
		if (file.exists()){
			log.error("File Already Exists: " + file.getAbsolutePath());
		}else {
			File folder  =  new File(POReaderReadEnv.getEnvValue(POReaderEnvProp.XSLXExportFolder));
			if (!folder.exists() ) {
				folder.mkdirs();
			}
		}

		
	     //Create blank workbook
	      XSSFWorkbook workbook = new XSSFWorkbook(); 
	      //Create a blank sheet
	      XSSFSheet spreadsheet = workbook.createSheet("Customer Items");
	      
	      //Create row object
	      XSSFRow row;
	      
	      
	      XSSFCellStyle colorStyle = workbook.createCellStyle();
	      //colorStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(128, 0, 128)));
	      colorStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	      //colorStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
	      colorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	      //colorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      colorStyle.setBorderBottom(BorderStyle.THIN);
	      colorStyle.setBorderTop(BorderStyle.THIN);
	      colorStyle.setBorderLeft(BorderStyle.THIN);
	      colorStyle.setBorderRight(BorderStyle.THIN);
	      
	      
	      CellStyle borderLStyle = workbook.createCellStyle();
	      borderLStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      
	      CellStyle borderRStyle = workbook.createCellStyle();
	      borderRStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

	      int rowid = 0;
          int cellid = 0;
          
	      row = spreadsheet.createRow(rowid++);
		      
	      Object[] colVal =  custItemList.get(0);
	      rowid++;
	      row = spreadsheet.createRow(rowid++);
	      cellid=0;
	      for (int i=0;i<colVal.length;i++){
	            Cell cell = row.createCell(cellid++);
            	cell.setCellStyle(colorStyle);
            	cell.setCellValue((String)colVal[i]); 
	      }
	      for (int j=1;j<custItemList.size();j++){
		      Object[] rowVal =  custItemList.get(j);
		      row = spreadsheet.createRow(rowid++);
		      cellid=0;
		      for (int k=0;k<rowVal.length;k++){
		            Cell cell = row.createCell(cellid++);
	            	cell.setCellValue((String)rowVal[k]); 
	            	try{
		            	Float.parseFloat((String)rowVal[k]);
	            		cell.setCellStyle(borderRStyle);
	            	} catch(NumberFormatException nfe){
		            	cell.setCellStyle(borderLStyle);
	            	}
		      }
	      }
	      
	      
	      // set the auto column size to all columns 
	      int colcount = colVal.length;
	      for (int l=0;l<colcount;l++){
	    	  spreadsheet.autoSizeColumn(l);  
	      }
	      
	      //Write the workbook in file system
	      FileOutputStream out = new FileOutputStream(file);
	      workbook.write(out);
	      out.close();
	      workbook = null;
	      log.debug("written successfully" );
	      status = true;
		return status;
	}
	
	public static boolean exportManualPODetails(List<Object[]> custItemList,String fileName)  throws Exception{
		boolean status = false;
		if (fileName==null){
			log.error("Empty file name");
		}
		File file = new File(fileName);
		if (file.exists()){
			log.error("File Already Exists: " + file.getAbsolutePath());
		}  else {
			File folder  =  new File(POReaderReadEnv.getEnvValue(POReaderEnvProp.XSLXExportFolder));
			if (!folder.exists() ) {
				folder.mkdirs();
			}
		}

		
	     //Create blank workbook
	      XSSFWorkbook workbook = new XSSFWorkbook(); 
	      //Create a blank sheet
	      XSSFSheet spreadsheet = workbook.createSheet("PO Items Details");
	      
	      //Create row object
	      XSSFRow row;
	      
	      
	      XSSFCellStyle colorStyle = workbook.createCellStyle();
	      //colorStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(128, 0, 128)));
	      colorStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	      //colorStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
	      colorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	      //colorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	      colorStyle.setBorderBottom(BorderStyle.THIN);
	      colorStyle.setBorderTop(BorderStyle.THIN);
	      colorStyle.setBorderLeft(BorderStyle.THIN);
	      colorStyle.setBorderRight(BorderStyle.THIN);
	      
	      
	      CellStyle borderLStyle = workbook.createCellStyle();
	      borderLStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      borderLStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      
	      CellStyle borderRStyle = workbook.createCellStyle();
	      borderRStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      borderRStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

	      int rowid = 0;
          int cellid = 0;
          
	      row = spreadsheet.createRow(rowid++);
		      
	      Object[] colVal =  custItemList.get(0);
	      rowid++;
	      row = spreadsheet.createRow(rowid++);
	      cellid=0;
	      for (int i=0;i<colVal.length;i++){
	            Cell cell = row.createCell(cellid++);
            	cell.setCellStyle(colorStyle);
            	cell.setCellValue((String)colVal[i]); 
	      }
	      for (int j=1;j<custItemList.size();j++){
		      Object[] rowVal =  custItemList.get(j);
		      row = spreadsheet.createRow(rowid++);
		      cellid=0;
		      for (int k=0;k<rowVal.length;k++){
		            Cell cell = row.createCell(cellid++);
	            	cell.setCellValue((String)rowVal[k]); 
	            	try{
		            	Float.parseFloat((String)rowVal[k]);
	            		cell.setCellStyle(borderRStyle);
	            	} catch(NumberFormatException nfe){
		            	cell.setCellStyle(borderLStyle);
	            	}
		      }
	      }
	      
	      
	      // set the auto column size to all columns 
	      int colcount = colVal.length;
	      for (int l=0;l<colcount;l++){
	    	  spreadsheet.autoSizeColumn(l);  
	      }
	      
	      //Write the workbook in file system
	      FileOutputStream out = new FileOutputStream(file);
	      workbook.write(out);
	      out.close();
	      workbook = null;
	      log.debug("written successfully" );
	      status = true;
		return status;
	}
	
}
