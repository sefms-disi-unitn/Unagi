package it.unitn.disi.unagi.gui.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public final class CollectionsUtil {
	/**
	 * TODO: document this method.
	 * 
	 * @param iter
	 * @param clazz
	 * @return
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
