package com.osimatic.core_android;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Utility class providing helper methods for arrays, collections, and maps.
 *
 * <p>This class is not instantiable. All methods are static.
 *
 * @see java.util.Collections
 * @see java.util.Arrays
 */
public class Array {

	private Array() {}

	// =============================================================================================
	// Functional interfaces
	// =============================================================================================

	/**
	 * A functional interface representing a predicate (boolean-valued function) of one argument.
	 *
	 * <p>Note: on API 24+, prefer {@link java.util.function.Predicate} from the standard library.
	 * This interface is provided for compatibility with minSdk below 24.
	 *
	 * @param <T> the type of the input to the predicate
	 * @see java.util.function.Predicate
	 */
	public interface Predicate<T> {
		/**
		 * Evaluates this predicate on the given argument.
		 *
		 * @param value the input argument
		 * @return {@code true} if the input argument matches the predicate, {@code false} otherwise
		 */
		boolean apply(T value);
	}

	// =============================================================================================
	// Map utilities
	// =============================================================================================

	/**
	 * Removes all entries with a {@code null} value from the given map, in place.
	 *
	 * @param <K> the type of map keys
	 * @param <V> the type of map values
	 * @param map the map to clean; must not be {@code null}
	 */
	public static <K, V> void removeNullValues(Map<K, V> map) {
		map.values().removeAll(Collections.singleton(null));
	}

	/**
	 * Merges two maps by adding all entries from {@code source} into {@code target} that are not already present in {@code target}.
	 *
	 * <p>Entries already present in {@code target} are not overwritten. The {@code target} map is modified in place and returned.
	 *
	 * <pre>
	 * merge({"a": "1"}, {"b": "2"})    = {"b": "2", "a": "1"}
	 * merge({"a": "1"}, {"a": "2"})    = {"a": "2"}  // existing key not overwritten
	 * merge({"a": "1", "b": "2"}, {})  = {"a": "1", "b": "2"}
	 * </pre>
	 *
	 * @param <K>    the type of map keys
	 * @param <V>    the type of map values
	 * @param source the map whose entries are merged into {@code target}; must not be {@code null}
	 * @param target the map to merge into; must not be {@code null}
	 * @return the {@code target} map with entries from {@code source} added where the key was absent
	 */
	public static <K, V> Map<K, V> merge(Map<K, V> source, Map<K, V> target) {
		for (Map.Entry<K, V> entry : source.entrySet()) {
			if (!target.containsKey(entry.getKey())) {
				target.put(entry.getKey(), entry.getValue());
			}
		}
		return target;
	}

	// =============================================================================================
	// Collection utilities
	// =============================================================================================

	/**
	 * Returns a new collection containing only elements from {@code target} that satisfy the given predicate.
	 *
	 * @param <T>       the type of elements in the collection
	 * @param target    the collection to filter; must not be {@code null}
	 * @param predicate the predicate to test each element; must not be {@code null}
	 * @return a new {@link ArrayList} containing only the matching elements
	 */
	public static <T> Collection<T> filter(Collection<T> target, Predicate<T> predicate) {
		Collection<T> result = new ArrayList<>();
		for (T element : target) {
			if (predicate.apply(element)) {
				result.add(element);
			}
		}
		return result;
	}

	/**
	 * Returns {@code true} if at least one element in the collection satisfies the given predicate.
	 *
	 * @param <T>       the type of elements in the collection
	 * @param target    the collection to test; must not be {@code null}
	 * @param predicate the predicate to test each element; must not be {@code null}
	 * @return {@code true} if any element matches, {@code false} otherwise
	 */
	public static <T> boolean any(Collection<T> target, Predicate<T> predicate) {
		for (T element : target) {
			if (predicate.apply(element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns {@code true} if all elements in the collection satisfy the given predicate.
	 *
	 * <p>Returns {@code true} if the collection is empty (vacuous truth).
	 *
	 * @param <T>       the type of elements in the collection
	 * @param target    the collection to test; must not be {@code null}
	 * @param predicate the predicate to test each element; must not be {@code null}
	 * @return {@code true} if all elements match, {@code false} otherwise
	 */
	public static <T> boolean all(Collection<T> target, Predicate<T> predicate) {
		for (T element : target) {
			if (!predicate.apply(element)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns {@code true} if no element in the collection satisfies the given predicate.
	 *
	 * <p>Returns {@code true} if the collection is empty.
	 *
	 * @param <T>       the type of elements in the collection
	 * @param target    the collection to test; must not be {@code null}
	 * @param predicate the predicate to test each element; must not be {@code null}
	 * @return {@code true} if no element matches, {@code false} otherwise
	 */
	public static <T> boolean none(Collection<T> target, Predicate<T> predicate) {
		return !any(target, predicate);
	}

	/**
	 * Returns the first element of the given list, or {@code null} if the list is empty.
	 *
	 * @param <T>  the type of elements in the list
	 * @param list the list to retrieve the first element from; must not be {@code null}
	 * @return the first element, or {@code null} if the list is empty
	 */
	public static <T> T first(List<T> list) {
		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * Returns the last element of the given list, or {@code null} if the list is empty.
	 *
	 * @param <T>  the type of elements in the list
	 * @param list the list to retrieve the last element from; must not be {@code null}
	 * @return the last element, or {@code null} if the list is empty
	 */
	public static <T> T last(List<T> list) {
		return list.isEmpty() ? null : list.get(list.size() - 1);
	}

	/**
	 * Returns a new list containing the elements of the given collection with duplicates removed.
	 * The order of first occurrences is preserved.
	 *
	 * @param <T>    the type of elements in the collection
	 * @param target the collection to deduplicate; must not be {@code null}
	 * @return a new {@link ArrayList} with duplicates removed
	 */
	public static <T> List<T> distinct(Collection<T> target) {
		List<T> result = new ArrayList<>();
		for (T element : target) {
			if (!result.contains(element)) {
				result.add(element);
			}
		}
		return result;
	}

	// =============================================================================================
	// Array utilities
	// =============================================================================================

	/**
	 * Copies the given array and appends the given element at the end.
	 *
	 * <p>The returned array has the same component type as the input array, or the type of the element if the array is {@code null}.
	 *
	 * <pre>
	 * add(null, null)       = IllegalArgumentException
	 * add(null, "a")        = ["a"]
	 * add(["a"], null)      = ["a", null]
	 * add(["a"], "b")       = ["a", "b"]
	 * add(["a", "b"], "c")  = ["a", "b", "c"]
	 * </pre>
	 *
	 * @param <T>     the component type of the array
	 * @param array   the array to append to; may be {@code null}
	 * @param element the element to append; may be {@code null}
	 * @return a new array containing the existing elements plus the appended element
	 * @throws IllegalArgumentException if both {@code array} and {@code element} are {@code null}
	 * @see java.util.Arrays#copyOf
	 */
	public static <T> T[] add(final T[] array, final T element) {
		final Class<?> type;
		if (array != null) {
			type = array.getClass().getComponentType();
		} else if (element != null) {
			type = element.getClass();
		} else {
			throw new IllegalArgumentException("Arguments cannot both be null");
		}
		@SuppressWarnings("unchecked")
		final T[] newArray = (T[]) copyArrayGrow1(array, type);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Converts the given array to a mutable {@link List}.
	 *
	 * @param <T>   the component type of the array
	 * @param array the array to convert; must not be {@code null}
	 * @return a new {@link ArrayList} containing all elements of the array
	 * @see java.util.Arrays#asList
	 */
	public static <T> List<T> toList(final T[] array) {
		List<T> result = new ArrayList<>();
		Collections.addAll(result, array);
		return result;
	}

	/**
	 * Returns a copy of the given array with its size increased by one. The last slot is left at its default value.
	 *
	 * <p>If {@code array} is {@code null}, a new single-element array of type {@code newArrayComponentType} is returned.
	 *
	 * @param array                 the array to copy; may be {@code null}
	 * @param newArrayComponentType the component type to use when {@code array} is {@code null}; must not be {@code null}
	 * @return a new array of size {@code array.length + 1}, or a single-element array if {@code array} is {@code null}
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