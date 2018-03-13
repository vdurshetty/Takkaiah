package com.takkaiah.poreader.reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.sql.Connection;

import com.takkaiah.db.util.JdbcConnection;
import com.takkaiah.logger.POReaderLogger;

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;

	
	public class CustomerItemMappingReport {

		POReaderLogger log = POReaderLogger.getLogger(CustomerItemMappingReport.class.getName());
		
		public CustomerItemMappingReport(int custId) {
			try {
				JdbcConnection con = new JdbcConnection();
				build(con.getConnection(), custId);
			} catch (Exception e) {
				log.error("Unable to open Customer Item Mapping Report :" + e.getMessage());
			} 
		}

		private void build(Connection con, int custId) {
			try {
				
				String sql = "select CustName,itemname,hsn_code,eancode,articlecode,marginpercent from CustItemMapWithArticleCodeView where custid=" + custId;
				
				TextColumnBuilder<Integer> rowNumberColumn =   col.reportRowNumberColumn("No.").setFixedColumns(2).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER); 
				TextColumnBuilder<String> custName = col.column("Customer Name", "CustName", type.stringType());
				TextColumnBuilder<String> hsnCode = col.column("HSN Code", "hsn_code", type.stringType());
				TextColumnBuilder<String> eancode = col.column("EAN Code", "eancode", type.stringType());
				TextColumnBuilder<String> itemname = col.column("Item Name", "itemname", type.stringType());
				TextColumnBuilder<String> articlecode = col.column("Artical Code", "articlecode", type.stringType());
				TextColumnBuilder<Float> marginPercent = col.column("Margin Percent", "marginpercent", type.floatType() );
				report()
				  .setTemplate(PORATemplates.reportTemplate)
				  .columns(rowNumberColumn,hsnCode,eancode,itemname,articlecode,marginPercent)
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
			new CustomerItemMappingReport(1);
		}
	}