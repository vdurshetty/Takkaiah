/**
 * DynamicReports - Free Java reporting library for creating reports dynamically
 *
 * Copyright (C) 2010 - 2016 Ricardo Mariaca
 * http://www.dynamicreports.org
 *
 * This file is part of DynamicReports.
 *
 * DynamicReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamicReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */

package com.takkaiah.poreader.reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;

public class PORATemplates {
	public static final StyleBuilder rootStyle;
	public static final StyleBuilder boldStyle;
	public static final StyleBuilder italicStyle;
	public static final StyleBuilder boldCenteredStyle;
	public static final StyleBuilder bold12CenteredStyle;
	public static final StyleBuilder bold18CenteredStyle;
	public static final StyleBuilder bold22CenteredStyle;
	public static final StyleBuilder columnStyle;
	public static final StyleBuilder columnTitleStyle;
	public static final StyleBuilder groupStyle;
	public static final StyleBuilder subtotalStyle;

	public static final ReportTemplateBuilder reportTemplate;
	public static final CurrencyType currencyType;
	public static final ComponentBuilder<?, ?> dynamicReportsComponent;
	public static final ComponentBuilder<?, ?> footerComponent;
	public static final ComponentBuilder<?, ?> headerComponent;

	static {
		rootStyle           = stl.style().setPadding(2);
		boldStyle           = stl.style(rootStyle).bold();
		italicStyle         = stl.style(rootStyle).italic();
		boldCenteredStyle   = stl.style(boldStyle)
		                         .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
		bold12CenteredStyle = stl.style(boldCenteredStyle)
		                         .setFontSize(12);
		bold18CenteredStyle = stl.style(boldCenteredStyle)
		                         .setFontSize(18);
		bold22CenteredStyle = stl.style(boldCenteredStyle)
                             .setFontSize(22);
		columnStyle         = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
		columnTitleStyle    = stl.style(columnStyle)
		                         .setBorder(stl.pen1Point())
		                         .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
		                         .setBackgroundColor(Color.LIGHT_GRAY)
		                         .bold();
		groupStyle          = stl.style(boldStyle)
		                         .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
		subtotalStyle       = stl.style(boldStyle)
		                         .setTopBorder(stl.pen1Point());

		StyleBuilder crosstabGroupStyle      = stl.style(columnTitleStyle);
		StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
		                                          .setBackgroundColor(new Color(170, 170, 170));
		StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
		                                          .setBackgroundColor(new Color(140, 140, 140));
		StyleBuilder crosstabCellStyle       = stl.style(columnStyle)
		                                          .setBorder(stl.pen1Point());

		TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
			.setHeadingStyle(0, stl.style(rootStyle).bold());

		reportTemplate = template()
		                   .setLocale(Locale.ENGLISH)
		                   .setColumnStyle(columnStyle)
		                   .setColumnTitleStyle(columnTitleStyle)
		                   .setGroupStyle(groupStyle)
		                   .setGroupTitleStyle(groupStyle)
		                   .setSubtotalStyle(subtotalStyle)
		                   .highlightDetailEvenRows()
		                   .crosstabHighlightEvenRows()
		                   .setCrosstabGroupStyle(crosstabGroupStyle)
		                   .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
		                   .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
		                   .setCrosstabCellStyle(crosstabCellStyle)
		                   .setTableOfContentsCustomizer(tableOfContentsCustomizer);

		currencyType = new CurrencyType();

		HyperLinkBuilder link = hyperLink("http://www.takkaiah.com");
		dynamicReportsComponent =
		  cmp.horizontalList(
		  	cmp.image(POReaderReadEnv.getEnvValue(POReaderEnvProp.logoPath)).setFixedDimension(80, 80),
		  	cmp.verticalList(
		  		cmp.text("Takkaiah and Co.").setStyle(bold22CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
		  		cmp.text("http://www.takkaiah.com").setStyle(italicStyle).setHyperLink(link))).setFixedWidth(300);

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YYYY, HH:MM");
		StyleBuilder titleStyle =  DynamicReports.stl.style(stl.style().italic()).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setFontSize(10);

		footerComponent = cmp.horizontalList(
							cmp.pageXofY().setStyle(stl.style(boldCenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setTopBorder(stl.pen1Point())),
		                    cmp.text(sdf.format(new Date())).setStyle(stl.style(boldCenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setTopBorder(stl.pen1Point())));
		
		headerComponent = cmp.horizontalList()
				  		.add(
						  cmp.image(POReaderReadEnv.getEnvValue(POReaderEnvProp.logoPath)).setFixedDimension(90, 90).setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT),
		  				  cmp.text("  Takkaiah and co.\n  Dry fruits, Spices, Saffron\n  #F1, Block �A� Tirumala Residency, Saipuri Colony,\n  Malkajgiri,Hyderabad-500 047\n  Ph: +91-40-27053609, 65177732, 27050527").setStyle(stl.style(stl.style().italic())),
		  				  cmp.text(sdf.format(new Date())).setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
		  				);
	}

	/**
	 * Creates custom component which is possible to add to any report band component
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(String label) {
		return cmp.horizontalList()
		        .add(
		        	dynamicReportsComponent,
		        	cmp.text(label).setStyle(bold18CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
		        .newRow()
		        .add(cmp.line())
		        .newRow()
		        .add(cmp.verticalGap(10));
	}

	public static CurrencyValueFormatter createCurrencyValueFormatter(String label) {
		return new CurrencyValueFormatter(label);
	}

	public static class CurrencyType extends BigDecimalType {
		private static final long serialVersionUID = 1L;

		@Override
		public String getPattern() {
			return "Rs #,###.00";
		}
	}

	private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number> {
		private static final long serialVersionUID = 1L;

		private String label;

		public CurrencyValueFormatter(String label) {
			this.label = label;
		}

		@Override
		public String format(Number value, ReportParameters reportParameters) {
			return label + currencyType.valueToString(value, reportParameters.getLocale());
		}
	}
}