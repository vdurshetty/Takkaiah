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

	
	public class CustomerMasterReport {

		POReaderLogger log = POReaderLogger.getLogger(CustomerMasterReport.class.getName());
		
		public CustomerMasterReport() {
			try {
				JdbcConnection con = new JdbcConnection();
				build(con.getConnection());
			} catch (Exception e) {
				log.error("Unable to open Customer Master Report :" + e.getMessage());
			} 
		}

		private void build(Connection con) {
			try {
				
				String sql = "Select b.CustGrpName,a.CustName,c.mrpTName,a.Address,a.EmailID,a.Mobile 	from CustomerMaster a,	CustomerGroup b, mrpType c	where a.CustGroup=b.CustGrpID and c.mrpTID=a.mrpType";
				
				TextColumnBuilder<Integer> rowNumberColumn =   col.reportRowNumberColumn("No.").setFixedColumns(2).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
				
				TextColumnBuilder<String> CustGrpName = col.column("Customer Group Name", "CustGrpName", type.stringType());
				TextColumnBuilder<String> CustName = col.column("Customer Name", "CustName", type.stringType());
				TextColumnBuilder<String> mrpTName = col.column("MRP Type", "mrpTName", type.stringType());
				TextColumnBuilder<String> Address = col.column("Address", "Address", type.stringType());
				TextColumnBuilder<String> email = col.column("Email Address", "EmailID", type.stringType());
				TextColumnBuilder<String> mobile = col.column("Mobile", "Mobile", type.stringType());
				
				report()
				  .setTemplate(PORATemplates.reportTemplate)
				  .columns( rowNumberColumn,  CustName,mrpTName,Address,email,mobile)
				  .groupBy(CustGrpName)
				  .title(PORATemplates.createTitleComponent("Customer Master List"))
				  .pageFooter(PORATemplates.footerComponent)
				  .setDataSource(sql, con)
				  .show(false);
			} catch (DRException e) {
				log.error("Unable to open Customer Master Report :" + e.getMessage());
			}
		}

		public static void main(String[] args) {
			new CustomerMasterReport();
		}
	}