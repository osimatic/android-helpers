package com.osimatic.android_helpers;

import java.text.NumberFormat;

public class Currency {
	public static String formatCurrency(double money, String currencyCode) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		formatter.setCurrency(java.util.Currency.getInstance(currencyCode));
		return formatter.format(money);
	}
}
