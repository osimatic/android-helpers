package com.osimatic.android_helpers;

import java.util.Hashtable;
import java.util.Map;

public class Array {
	public static Hashtable<String, String> merge(Hashtable<String, String> array1, Hashtable<String, String> array2) {
		for(Map.Entry<String, String> entry : array1.entrySet()) {
			if (!array2.containsKey(entry.getKey())) {
				array2.put(entry.getKey(), entry.getValue());
			}

			/* if (array2.containsKey(entry.getKey())) {
				array2.remove(entry.getKey());
			}
			else {
				array2.put(entry.getKey(), entry.getValue());
			} */
		}
		return array2;
	}
}
