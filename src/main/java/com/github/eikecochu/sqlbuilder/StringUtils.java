package com.github.eikecochu.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

final class StringUtils {

	private StringUtils() {
	}

	public static String join(final Iterable<String> it, String delim) {
		if (delim == null)
			delim = "";
		String d = "";
		final StringBuilder sb = new StringBuilder();
		for (final String s : it) {
			sb.append(d)
					.append(s);
			d = delim;
		}
		return sb.toString();
	}

	public static String leftPad(String s, final int length) {
		if (s == null || s.length() >= length)
			return s;
		final StringBuilder sBuilder = new StringBuilder(s);
		while (sBuilder.length() < length)
			sBuilder.insert(0, " ");
		s = sBuilder.toString();
		return s;
	}

	public static String repeat(final String s, final int times) {
		if (times <= 0)
			return "";
		if (s == null || s.isEmpty() || times == 1)
			return s;
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < times; i++)
			sb.append(s);
		return sb.toString();
	}

	public static String repeat(final char c, final int times) {
		return repeat("" + c, times);
	}

	public static boolean nullOrBlank(final String s) {
		return s == null || s.trim()
				.isEmpty();
	}

	public static List<String> filterValues(final String[] strings) {
		final List<String> list = new ArrayList<>(strings == null ? 0 : strings.length);
		if (strings != null && strings.length > 0)
			for (final String string : strings)
				if (!nullOrBlank(string))
					list.add(string);
		return list;
	}

}
