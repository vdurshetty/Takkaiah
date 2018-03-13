package com.takkaiah.poreader.reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.sql.Connection;

import com.takkaiah.db.util.JdbcConnection;
import com.takkaiah.logger.POReaderLogger;

//import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;

	
	public class CustomerArticleCodesReport {

		POReaderLogger log = POReaderLogger.getLogger(CustomerArticleCodesReport.class.getName());
		
		public CustomerArticleCodesReport(int cgID) {
			try {
				JdbcConnection con = new JdbcConnection();
				build(con.getConnection(),cgID);
			} catch (Exception e) {
				log.error("Unable to open Customer Article Code Report :" + e.getMessage());
			} 
		}

		private void build(Connection con,int custId) {
			try {
				
				
				String sql ="select b.CustGrpName,a.itemName,a.hsn_code,a.eanCode,a.articleCode from " + 
						"CustArticleCodeView a, CustomerGroup b where b.CustGrpID=a.cgID and a.cgid=" + custId + " order by a.articleCode desc";
				TextColumnBuilder<Integer> rowNumberColumn =   col.reportRowNumberColumn("No.").setFixedColumns(2).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
				TextColumnBuilder<String> CustGrpName = col.column("Customer Group Name", "CustGrpName", type.stringType());
				TextColumnBuilder<String> hsnCode = col.column("HSN Code", "hsn_code", type.stringType());
				TextColumnBuilder<String> eancode = col.column("EAN Code", "eancode", type.stringType());
				TextColumnBuilder<String> itemname = col.column("Item Name", "itemname", type.stringType());
				TextColumnBuilder<String> articlecode = col.column("Artical Code", "articlecode", type.stringType());
				report()
				  .setTemplate(PORATemplates.reportTemplate)
				  .columns(rowNumberColumn,hsnCode,eancode,itemname,articlecode)
				  .groupBy(CustGrpName)
   				  .title(PORATemplates.createTitleComponent("Customer Article Codes"))
   				  .pageFooter(PORATemplates.footerComponent)
				  .setDataSource(sql, con)
				  .show(false);
			} catch (DRException e) {
				log.error("Unable to open Customer Article Code Report :" + e.getMessage());
			}
		}

	
		
		public static void main(String[] args) {
			new CustomerArticleCodesReport(1);
		}
	
	
	
	}
