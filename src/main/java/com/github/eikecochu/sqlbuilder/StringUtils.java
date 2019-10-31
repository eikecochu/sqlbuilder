package com.github.eikecochu.sqlbuilder;

abstract class StringUtils {

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
		while (s.length() < length)
			s = " " + s;
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

}
