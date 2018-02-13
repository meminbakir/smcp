/**
 * 
 */
package mce.util;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class StringUtils {
	/**
	 * 
	 * Checks many strings, if any of them is null or empty then return true, false otherwise
	 * 
	 * @param String
	 *            parameters, e.g., name, surname, address ...
	 * @return if any of them is null or empty then true, false otherwise
	 */
	public static boolean isNullOrEmpty(String... strings) {
		for (String str : strings) {
			if (str == null || str.isEmpty())
				return true;
		}
		return false;
	}

	/**
	 * Check one string, if it is null or empty return true, false otherwise
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.isEmpty())
			return true;
		else
			return false;
	}

}
