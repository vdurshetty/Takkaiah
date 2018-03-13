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
import net.sf.dynamicreports.report.exception.DRException;

	
public class ItemPriceReport {

	POReaderLogger log = POReaderLogger.getLogger(ItemPriceReport.class.getName());
	
	public ItemPriceReport() {
		try {
			JdbcConnection con = new JdbcConnection();
			build(con.getConnection());
		} catch (Exception e) {
			log.error("Unable to open Item Price Report :" + e.getMessage());
		} 
	}

	private void build(Connection con) {
		
		try {
			String sql = "select a.ItemID,a.hsn_code, a.EANCode, a.ItemName, c.mrp, d.mrpTName from ItemMaster a,ItemMRP c, MRPType d where c.ItemID=a.ItemID and d.mrpTID=c.mrpType order by a.ItemID";
			TextColumnBuilder<String> eanCode = col.column("EAN Code", "EANCode", type.stringType());
			TextColumnBuilder<String> hsnCode = col.column("HSN Code", "hsn_code", type.stringType());
			TextColumnBuilder<String> itemName = col.column("Item Name", "ItemName", type.stringType());
			TextColumnBuilder<String> mrpTName = col.column("MRP Name", "mrpTName", type.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);
			TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("MRP", "mrp", PORATemplates.currencyType);
			
			report()
			  .setTemplate(PORATemplates.reportTemplate)
			  .columns( hsnCode,itemName,mrpTName,unitPriceColumn)
			  .groupBy(eanCode)
			  .title(PORATemplates.createTitleComponent("Item Price List"))
			  .pageFooter(PORATemplates.footerComponent)
			  .setDataSource(sql, con)
			  .show(false);
		} catch (DRException e) {
			log.error("Unable to open Item Price Report :" + e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		new ItemPriceReport();
	}
}