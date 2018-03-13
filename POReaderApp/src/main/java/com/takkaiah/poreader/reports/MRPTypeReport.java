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

	
	public class MRPTypeReport {

		POReaderLogger log = POReaderLogger.getLogger(MRPTypeReport.class.getName());
		
		public MRPTypeReport() {
			try {
				JdbcConnection con = new JdbcConnection();
				build(con.getConnection());
			} catch (Exception e) {
				log.error("Unable to open MRP Type List Report :" + e.getMessage());
			} 
		}

		private void build(Connection con) {
			try {
				
				String sql = "select mrpTName,mrpTAlias,mrpTaxPercent from MRPType order by mrpTName";
				
				TextColumnBuilder<Integer> rowNumberColumn =   col.reportRowNumberColumn("No.").setFixedColumns(2).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER); 
				TextColumnBuilder<String> mrpTName = col.column("MRP Type Name", "mrpTName", type.stringType());
				TextColumnBuilder<String> mrpTAlias = col.column("Alias", "mrpTAlias", type.stringType());
				TextColumnBuilder<Float> mrpTaxPercent = col.column("Tax Percent", "mrpTaxPercent", type.floatType());
				report()
				  .setTemplate(PORATemplates.reportTemplate)
				  .columns( rowNumberColumn,  mrpTName,mrpTAlias,mrpTaxPercent)
				  .title(PORATemplates.createTitleComponent("MRP Type List"))
				  .pageFooter(PORATemplates.footerComponent)
				  .setDataSource(sql, con)
				  .show(false);
			} catch (DRException e) {
				log.error("Unable to open MRP Type List Report :" + e.getMessage());
			}
		}

		public static void main(String[] args) {
			new MRPTypeReport();
		}
	}