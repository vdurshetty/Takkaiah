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

	
	public class CustomerGroupReport {

		POReaderLogger log = POReaderLogger.getLogger(CustomerGroupReport.class.getName());
		
		public CustomerGroupReport() {
			try {
				JdbcConnection con = new JdbcConnection();
				build(con.getConnection());
			} catch (Exception e) {
				log.error("Unable to open Customer Group List Report :" + e.getMessage());
			} 
		}

		private void build(Connection con) {
			try {
				
				String sql = "select CustGrpName,CustGrpAlias,Address from CustomerGroup order by CustGrpName";
				
				TextColumnBuilder<Integer> rowNumberColumn =   col.reportRowNumberColumn("No.").setFixedColumns(2).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER); 
				TextColumnBuilder<String> CustGrpName = col.column("Customer Group Name", "CustGrpName", type.stringType());
				TextColumnBuilder<String> CustGrpAlias = col.column("Alias", "CustGrpAlias", type.stringType());
				TextColumnBuilder<String> Address = col.column("Address", "Address", type.stringType());
				report()
				  .setTemplate(PORATemplates.reportTemplate)
				  .columns( rowNumberColumn,  CustGrpName,CustGrpAlias,Address)
   				  .title(PORATemplates.createTitleComponent("Customer Group List"))
				  .pageFooter(PORATemplates.footerComponent)
				  .setDataSource(sql, con)
				  .show(false);
			} catch (DRException e) {
				log.error("Unable to open Customer Group List Report :" + e.getMessage());
			}
		}

		public static void main(String[] args) {
			new CustomerGroupReport();
		}
	}