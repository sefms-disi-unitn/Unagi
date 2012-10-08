package it.unitn.disi.util.etc;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class that operates on collections of objects.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public final class CollectionsUtil {
	/** This class is not meant to have objects. */
	private CollectionsUtil() {}
	
	/**
	 * Filters a collection (given its iterator) and produces a list of the elements of the collection that belong to a
	 * particular class (including any of its subclasses).
	 * 
	 * @param iter
	 *          The iterator of the collection.
	 * @param clazz
	 *          The interested class, used to filtering the collection.
	 * @return A List of objects of the specified class which were in the given collection's iterator.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> filterSelectionByClass(Iterator<?> iter, Class<T> clazz) {
		List<T> filteredList = new ArrayList<T>();

		// Includes in the filtered list only the elements that are of the specified class or one of its subclasses.
		while (iter.hasNext()) {
			Object elem = iter.next();
			if ((elem != null) && (elem.getClass().isAssignableFrom(clazz))) {
				filteredList.add((T) elem);
			}
		}

		return filteredList;
	}
}
