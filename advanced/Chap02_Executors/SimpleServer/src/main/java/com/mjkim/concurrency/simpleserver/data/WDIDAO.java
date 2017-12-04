package com.mjkim.concurrency.simpleserver.data;

import java.io.StringWriter;
import java.util.List;

import com.mjkim.concurrency.simpleserver.common.Constants;
import com.mjkim.concurrency.simpleserver.loader.WDILoader;

public class WDIDAO {
	private static WDIDAO dao;
	private List<WDI> data;

	private WDIDAO(String route) {
		WDILoader loader = new WDILoader();
		data = loader.load(route);
	}

	public static WDIDAO getDAO() {
		if (dao == null) {
			dao = new WDIDAO(Constants.DATA_ROUTE);
		}
		return dao;
	}

	public List<WDI> getData() {
		return data;
	}

	public String query(String codCountry, String codIndicator) {
		WDI wdi = getWDI(codCountry, codIndicator);
		if (wdi != null) {
			StringWriter writer = new StringWriter();
			writer.write(codCountry);
			writer.write(";");
			writer.write(codIndicator);
			writer.write(";");
			Double[] years = wdi.getValues();
			for (int i = 0; i < years.length; i++) {
				writer.write(years[i].toString());
				if (i < years.length - 1) {
					writer.write(";");
				}
			}
			return writer.toString();
		}
		return "";
	}

	public String query(String codCountry, String codIndicator, short year) {
		WDI wdi = getWDI(codCountry, codIndicator);
		if (wdi != null) {
			StringWriter writer = new StringWriter();
			writer.write(codCountry);
			writer.write(";");
			writer.write(codIndicator);
			writer.write(";");
			try {
				writer.write(wdi.getValue(year).toString());
			} catch (Exception e) {
				return "";
			}
			return writer.toString();
		}
		return "";
	}

	public String report(String codIndicator) {
		StringWriter writer = new StringWriter();
		writer.write(codIndicator);
		writer.write(";");
		for (int i = 0; i < data.size(); i++) {
			WDI wdi = data.get(i);
			if (wdi.getIndicatorCode().equals(codIndicator)) {
				Double[] years = wdi.getValues();
				double mean = 0.0;
				for (int j = 0; j < years.length; j++) {
					mean += years[j];
				}
				mean /= years.length;
				writer.write(wdi.getCountryCode());
				writer.write(";");
				writer.write("" + mean);
				writer.write(";");
			}
		}

		return writer.toString();

	}

	private WDI getWDI(String codCountry, String codIndicator) {
		WDI wdi = null;
		for (int i = 0; i < data.size(); i++) {
			wdi = data.get(i);
			if ((wdi.getCountryCode().equals(codCountry)) && (wdi.getIndicatorCode().equals(codIndicator))) {
				return wdi;
			}
		}
		return wdi;
	}
}
