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

	
	public class ItemCategoryReport {

		POReaderLogger log = POReaderLogger.getLogger(ItemCategoryReport.class.getName());
		
		public ItemCategoryReport() {
			try {
				JdbcConnection con = new JdbcConnection();
				build(con.getConnection());
			} catch (Exception e) {
				log.error("Unable to open Item Category List Report :" + e.getMessage());
			} 
		}

		private void build(Connection con) {
			try {
				
				String sql = "select CatName,CatAlias from ItemCategoryMaster order by CatName";
				
				TextColumnBuilder<Integer> rowNumberColumn =   col.reportRowNumberColumn("No.").setFixedColumns(2).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER); 
				TextColumnBuilder<String> CatName = col.column("Item Category Name", "CatName", type.stringType());
				TextColumnBuilder<String> alias = col.column("Alias", "CatAlias", type.stringType());
				report()
				  .setTemplate(PORATemplates.reportTemplate)
				  .columns( rowNumberColumn,  CatName,alias)
				  .title(PORATemplates.createTitleComponent("Item Category List"))
				  .pageFooter(PORATemplates.footerComponent)
				  .setDataSource(sql, con)
				  .show(false);
			} catch (DRException e) {
				log.error("Unable to open Item Category List Report :" + e.getMessage());
			}
		}

		public static void main(String[] args) {
			new ItemCategoryReport();
		}
	}