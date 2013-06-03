package net.frcdb.scoring.util;

import java.util.List;

/**
 *
 * @author tim
 */
public class StringUtil {

	/**
	 * Concatenates each token in the list <code>strs</code>, separated by
	 * <code>separator</code>
	 * @param strs The list of values
	 * @param separator The token to separate the strings with
	 * @return A String a list of tokens, separated by the provided separator.
	 */
	public static String concat(List<?> strs, String separator) {
		String ret = "";

		for (int i = 0; i < strs.size(); i++) {
			ret = ret + strs.get(i);

			if (i < strs.size() - 1) {
				ret = ret + separator;
			}
		}

		return ret;
	}

}
