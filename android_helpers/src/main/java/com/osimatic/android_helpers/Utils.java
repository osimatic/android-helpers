package com.osimatic.android_helpers;

public class Utils {
	/**
	 * This method is a helper for classes to implement {@link java.lang.Object#equals(java.lang.Object)}
	 * checks if two objects are equals - two levels of checks are
	 * made - first if both are null or not null. If either is null,
	 * check is made whether both are null.
	 * If both are non null, equality also is checked if so indicated
	 * @param obj1 first object to be compared
	 * @param obj2 second object to be compared
	 * @param checkEquality flag to indicate whether object equality should
	 * be checked if obj1 and obj2 are non-null
	 * @return true if the two objects are equal
	 * false otherwise
	 */
	public static boolean checkNullEquals(Object obj1, Object obj2, boolean checkEquality) {
		if(obj1 == null || obj2 == null) {
			return obj1 == obj2;
		}
		if(checkEquality) {
			return obj1.equals(obj2);
		}
		return true;
	}


	/**
	 * This method is a helper for classes to implement {@link java.lang.Object#equals(java.lang.Object)}
	 * The method checks whether the two arguments are both null or both not null and
	 * whether they are of the same class
	 * @param obj1 first object to compare
	 * @param obj2 second object to compare
	 * @return true if both objects are null or both are not null
	 * and if both are of the same class if not null
	 * false otherwise
	 */
	public static boolean checkNullAndClass(Object obj1, Object obj2) {
		if (!checkNullEquals(obj1, obj2, false)) {
			return false;
		}
		return obj1 == null || obj1.getClass() == obj2.getClass();
	}
}
