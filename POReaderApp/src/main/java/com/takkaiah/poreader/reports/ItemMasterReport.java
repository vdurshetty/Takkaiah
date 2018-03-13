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

	
	public class ItemMasterReport {

		POReaderLogger log = POReaderLogger.getLogger(ItemMasterReport.class.getName());
		
		public ItemMasterReport() {
			try {
				JdbcConnection con = new JdbcConnection();
				build(con.getConnection());
			} catch (Exception e) {
				log.error("Unable to open Item Master Report :" + e.getMessage());
			} 
		}

		private void build(Connection con) {
			try {
				
				String sql = "select a.ItemID, a.hsn_code,a.EANCode, a.ItemName, b.CatName, a.Units, a.TaxPercent, a.CaseQty from ItemMaster a,ItemCategoryMaster b where b.CatId = a.CatID order by b.CatName";
				
				TextColumnBuilder<Integer> rowNumberColumn =   col.reportRowNumberColumn("No.").setFixedColumns(2).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER); 
				TextColumnBuilder<String> hsnCode = col.column("HSN Code", "hsn_code", type.stringType());
				TextColumnBuilder<String> eanCode = col.column("EAN Code", "EANCode", type.stringType());
				TextColumnBuilder<String> itemName = col.column("Item Name", "ItemName", type.stringType());
				TextColumnBuilder<String> CatName = col.column("Item Name", "CatName", type.stringType());
				TextColumnBuilder<String> Units = col.column("Units", "Units", type.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);;
				TextColumnBuilder<Integer> CaseQty = col.column("CaseQty", "CaseQty", type.integerType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);;
				TextColumnBuilder<Float> TaxPercent = col.column("TaxPercent", "TaxPercent", type.floatType());
				report()
				  .setTemplate(PORATemplates.reportTemplate)
				  .columns( CatName,rowNumberColumn, hsnCode, eanCode,  itemName,Units,CaseQty,TaxPercent)
				  .groupBy(CatName)
				  .title(PORATemplates.createTitleComponent("Item List"))
				  .pageFooter(PORATemplates.footerComponent)
				  .setDataSource(sql, con)
				  .show(false);
			} catch (DRException e) {
				log.error("Unable to open Item Master Report :" + e.getMessage());
			}
		}

		public static void main(String[] args) {
			new ItemMasterReport();
		}
	}