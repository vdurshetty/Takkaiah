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

	
	public class UserListReport {

		POReaderLogger log = POReaderLogger.getLogger(UserListReport.class.getName());
		
		public UserListReport() {
			try {
				JdbcConnection con = new JdbcConnection();
				build(con.getConnection());
			} catch (Exception e) {
				log.error("Unable to open User List Report :" + e.getMessage());
			} 
		}

		private void build(Connection con) {
			try {
				
				String sql = "Select userName,fullName,empid from Users order by userName";
				
				TextColumnBuilder<Integer> rowNumberColumn =   col.reportRowNumberColumn("No.").setFixedColumns(2).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER); 
				TextColumnBuilder<String> userName = col.column("User Name", "userName", type.stringType());
				TextColumnBuilder<String> fullName = col.column("Employee Name", "fullName", type.stringType());
				TextColumnBuilder<String> empid = col.column("Employee ID", "empid", type.stringType());
				report()
				  .setTemplate(PORATemplates.reportTemplate)
				  .columns( rowNumberColumn,  userName,fullName,empid)
				  .title(PORATemplates.createTitleComponent("User List"))
				  .pageFooter(PORATemplates.footerComponent)
				  .setDataSource(sql, con)
				  .show(false);
			} catch (DRException e) {
				log.error("Unable to open User List Report :" + e.getMessage());
			}
		}

		public static void main(String[] args) {
			new UserListReport();
		}
	}