package com.osimatic.android_helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Array {

	public static void removeNullValues(HashMap<String, String> array) {
		array.values().removeAll(Collections.singleton(null));
	}

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

	/**
	 * <p>Copies the given array and adds the given element at the end of the new array.
	 *
	 * <p>The new array contains the same elements of the input
	 * array plus the given element in the last position. The component type of
	 * the new array is the same as that of the input array.
	 *
	 * <p>If the input array is {@code null}, a new one element array is returned
	 *  whose component type is the same as the element, unless the element itself is null,
	 *  in which case the return type is Object[]
	 *
	 * <pre>
	 * ArrayUtils.add(null, null)      = IllegalArgumentException
	 * ArrayUtils.add(null, "a")       = ["a"]
	 * ArrayUtils.add(["a"], null)     = ["a", null]
	 * ArrayUtils.add(["a"], "b")      = ["a", "b"]
	 * ArrayUtils.add(["a", "b"], "c") = ["a", "b", "c"]
	 * </pre>
	 *
	 * @param <T> the component type of the array
	 * @param array  the array to "add" the element to, may be {@code null}
	 * @param element  the object to add, may be {@code null}
	 * @return A new array containing the existing elements plus the new element
	 * The returned array type will be that of the input array (unless null),
	 * in which case it will have the same type as the element.
	 * If both are null, an IllegalArgumentException is thrown
	 * @since 2.1
	 * @throws IllegalArgumentException if both arguments are null
	 */
	public static <T> T[] add(final T[] array, final T element) {
		Class<?> type;
		if (array != null) {
			type = array.getClass().getComponentType();
		} else if (element != null) {
			type = element.getClass();
		} else {
			throw new IllegalArgumentException("Arguments cannot both be null");
		}
		@SuppressWarnings("unchecked") // type must be T
		final
		T[] newArray = (T[]) copyArrayGrow1(array, type);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Returns a copy of the given array of size 1 greater than the argument.
	 * The last value of the array is left to the default value.
	 *
	 * @param array The array to copy, must not be {@code null}.
	 * @param newArrayComponentType If {@code array} is {@code null}, create a
	 * size 1 array of this type.
	 * @return A new copy of the array of size 1 greater than the input.
	 */
	private static Object copyArrayGrow1(final Object array, final Class<?> newArrayComponentType) {
		if (array != null) {
			final int arrayLength = java.lang.reflect.Array.getLength(array);
			final Object newArray = java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
			System.arraycopy(array, 0, newArray, 0, arrayLength);
			return newArray;
		}
		return java.lang.reflect.Array.newInstance(newArrayComponentType, 1);
	}
}
