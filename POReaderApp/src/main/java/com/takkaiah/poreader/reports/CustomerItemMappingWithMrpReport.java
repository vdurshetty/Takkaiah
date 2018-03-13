package com.takkaiah.poreader.reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.math.BigDecimal;
import java.sql.Connection;

import com.takkaiah.db.util.JdbcConnection;
import com.takkaiah.logger.POReaderLogger;

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;

	
	public class CustomerItemMappingWithMrpReport {

		POReaderLogger log = POReaderLogger.getLogger(CustomerItemMappingWithMrpReport.class.getName());
		
		public CustomerItemMappingWithMrpReport(int custId) {
			try {
				JdbcConnection con = new JdbcConnection();
				build(con.getConnection(), custId);
			} catch (Exception e) {
				log.error("Unable to open Customer Item Mapping Report :" + e.getMessage());
			} 
		}

		private void build(Connection con, int custId) {
			try {
				
				String sql ="";
				sql = "select CustName,articlecode,hsn_code,eancode,itemname,BasicCostPrice,TaxPercent,TaxVal,NetLandingPrice,mrp,caseqty,marginPercent  from LocalCustMapItemsView where custid=" + custId;
				//if (custId==2){
				//	sql = "select CustName,articlecode,hsn_code,eancode,itemname,BasicCostPrice,TaxPercent,TaxVal,NetLandingPrice,mrp,caseqty,marginPercent  from OutstationCustMapItemsView where custid=" + custId;
				//} else {
				//	sql = "select CustName,articlecode,hsn_code,eancode,itemname,BasicCostPrice,TaxPercent,TaxVal,NetLandingPrice,mrp,caseqty,marginPercent  from LocalCustMapItemsView where custid=" + custId;
				//}

				
				TextColumnBuilder<Integer> rowNumberColumn =   col.reportRowNumberColumn("No.").setFixedColumns(2).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER); 
				TextColumnBuilder<String> custName = col.column("Customer Name", "CustName", type.stringType());
				TextColumnBuilder<String> hsnCode = col.column("HSN Code", "hsn_code", type.stringType());
				TextColumnBuilder<String> eancode = col.column("EAN Code", "eancode", type.stringType());
				TextColumnBuilder<String> itemname = col.column("Item Name", "itemname", type.stringType());
				TextColumnBuilder<String> articlecode = col.column("Artical Code", "articlecode", type.stringType());
				TextColumnBuilder<Float> BasicCostPrice = col.column("Basic Cost Price", "BasicCostPrice", type.floatType());
				TextColumnBuilder<Float> TaxPercent = col.column("Tax %", "TaxPercent", type.floatType());
				TextColumnBuilder<Float> TaxVal = col.column("Tax Value", "TaxVal", type.floatType());
				TextColumnBuilder<Float> NetLandingPrice = col.column("Net Landing Price", "NetLandingPrice", type.floatType());
				TextColumnBuilder<BigDecimal> mrp = col.column("MRP", "mrp", PORATemplates.currencyType);
				TextColumnBuilder<Integer> caseqty = col.column("Case Qty", "caseqty", type.integerType());
				TextColumnBuilder<Float> marginPercent = col.column("Margin Percent", "marginPercent", type.floatType() );
				
				report()
				  .setTemplate(PORATemplates.reportTemplate)
				  .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
				  //.setPageColumnsPerPage(1)
				  //.setPageColumnSpace(15)
				  //.setPageMargin()
				  .columns(rowNumberColumn,hsnCode,eancode,articlecode,itemname,marginPercent,BasicCostPrice,TaxPercent,TaxVal,NetLandingPrice,mrp,caseqty)
				  .groupBy(custName)
				  .title(PORATemplates.createTitleComponent("Customer Item Mappings"))
				  .pageFooter(PORATemplates.footerComponent)
				  .setDataSource(sql, con)
				  .show(false);
			} catch (DRException e) {
				log.error("Unable to open Customer Item Mapping Report :" + e.getMessage());
			}
		}

		public static void main(String[] args) {
			new CustomerItemMappingWithMrpReport(1);
		}
	}