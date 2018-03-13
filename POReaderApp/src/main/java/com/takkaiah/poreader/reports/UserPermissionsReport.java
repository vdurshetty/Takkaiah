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

	
	public class UserPermissionsReport {

		POReaderLogger log = POReaderLogger.getLogger(UserPermissionsReport.class.getName());
		
		public UserPermissionsReport() {
			try {
				JdbcConnection con = new JdbcConnection();
				build(con.getConnection());
			} catch (Exception e) {
				log.error("Unable to open User Permissions Report :" + e.getMessage());
			} 
		}

		private void build(Connection con) {
			try {
				
				String sql = "select a.userName,a.fullName,c.fName from Users a,userPermissions b, PORA_Functionalities c where b.uid=a.uid and c.fid=b.fid order by a.userName";
				
				TextColumnBuilder<Integer> rowNumberColumn =   col.reportRowNumberColumn("No.").setFixedColumns(2).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER); 
				TextColumnBuilder<String> userName = col.column("User Name", "userName", type.stringType());
				TextColumnBuilder<String> fullName = col.column("Employee Name", "fullName", type.stringType());
				TextColumnBuilder<String> fName = col.column("Access Name", "fName", type.stringType());
				report()
				  .setTemplate(PORATemplates.reportTemplate)
				  .columns(rowNumberColumn,fullName,fName)
				  .groupBy(userName,fullName)
				  .title(PORATemplates.createTitleComponent("User Permissions"))
				  .pageFooter(PORATemplates.footerComponent)
				  .setDataSource(sql, con)
				  .show(false);
			} catch (DRException e) {
				log.error("Unable to open User Permissions Report :" + e.getMessage());
			}
		}

		public static void main(String[] args) {
			new UserPermissionsReport();
		}
	}