package com.monzware.messaging.toolbox.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class InputStreamToStringConverter {

	private final InputStream is;
	private final String charset;

	public InputStreamToStringConverter(InputStream is) {
		this(is, null);
	}

	public InputStreamToStringConverter(InputStream is, String charset) {
		this.is = is;
		this.charset = charset;
	}

	public String getString() throws IOException {

		BufferedReader reader = getReader();
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		return sb.toString();
	}

	private BufferedReader getReader() throws UnsupportedEncodingException {

		InputStreamReader isReader = null;
		if (charset == null) {
			isReader = new InputStreamReader(is);
		} else {
			isReader = new InputStreamReader(is, charset);
		}

		return new BufferedReader(isReader);
	}

}
