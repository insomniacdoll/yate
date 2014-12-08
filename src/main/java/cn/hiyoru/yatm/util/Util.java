package cn.hiyoru.yatm.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 工具类
 * 
 */
public class Util {

	public final static MiniParser MINI_PARSER = MiniParser.defaultInstance();
	public final static MiniParser RAW_MINI_PARSER = MiniParser
			.fullRawInstance();
	public final static MiniParser NO_QUOTE_MINI_PARSER = new MiniParser(
			MiniParser.DEFAULT_ESCAPE_CHAR, (char) -1, false, false, false);
	public final static MiniParser RAW_OUTPUT_MINI_PARSER = MiniParser
			.rawOutputInstance();

	/**
	 * 写字符串到文件
	 * 
	 * @param string
	 *            字符串
	 * @param file
	 *            文件
	 * @param charsetName
	 *            文件编码
	 */
	public static void stringToFile(String string, File file, String charsetName) {
		FileOutputStream fos = null;
		Writer writer = null;
		try {
			try {
				fos = new FileOutputStream(file);
				writer = new OutputStreamWriter(fos, charsetName);
				writer.write(string);
			} finally {
				if (writer != null) {
					writer.close();
				} else if (fos != null) {
					fos.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 文件转换为字符串
	 * 
	 * @param file
	 *            文件
	 * @param charsetName
	 *            文件编码
	 * @return 转换后的字符串
	 */
	public static String fileToString(File file, String charsetName) {
		FileInputStream fileInputStream = null;
		try {
			try {
				fileInputStream = new FileInputStream(file);
				return streamToString(fileInputStream, charsetName);
			} finally {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 文件转换为字符串
	 * 
	 * @param fileName
	 *            文件名
	 * @param charsetName
	 *            文件编码
	 * @return 转换后的字符串
	 */
	public static String fileToString(String fileName, String charsetName) {
		return fileToString(new File(fileName), charsetName);
	}

	public static String streamToString(InputStream is, String charsetName) {
		try {
			Reader r = null;
			try {
				r = new BufferedReader(new InputStreamReader(is, charsetName));
				return readerToString(r);
			} finally {
				if (r != null) {
					try {
						r.close();
					} catch (IOException e) {
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static String resourceToString(String resourceName,
			String charsetName) {
		InputStream templateStream = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(resourceName);
		String template = Util.streamToString(templateStream, "UTF-8");
		return template;
	}

	public static String readerToString(Reader reader) {
		try {
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				sb.append(buf, 0, numRead);
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static byte[] streamToBa(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numRead = 0;
			while ((numRead = is.read(buf)) != -1) {
				baos.write(buf, 0, numRead);
			}
			byte[] byteArray = baos.toByteArray();
			return byteArray;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Object> arrayAsList(Object value) {
		List list = null;
		if (value instanceof int[]) {
			list = new ArrayList();
			int[] array = (int[]) value;
			for (int i : array) {
				list.add(i);
			}
		} else if (value instanceof short[]) {
			list = new ArrayList();
			short[] array = (short[]) value;
			for (short i : array) {
				list.add(i);
			}
		} else if (value instanceof char[]) {
			list = new ArrayList();
			char[] array = (char[]) value;
			for (char i : array) {
				list.add(i);
			}
		} else if (value instanceof byte[]) {
			list = new ArrayList();
			byte[] array = (byte[]) value;
			for (byte i : array) {
				list.add(i);
			}
		} else if (value instanceof long[]) {
			list = new ArrayList();
			long[] array = (long[]) value;
			for (long i : array) {
				list.add(i);
			}
		} else if (value instanceof double[]) {
			list = new ArrayList();
			double[] array = (double[]) value;
			for (double i : array) {
				list.add(i);
			}
		} else if (value instanceof float[]) {
			list = new ArrayList();
			float[] array = (float[]) value;
			for (float i : array) {
				list.add(i);
			}
		} else if (value instanceof boolean[]) {
			list = new ArrayList();
			boolean[] array = (boolean[]) value;
			for (boolean i : array) {
				list.add(i);
			}
		} else if (value.getClass().isArray()) {
			Object[] array = (Object[]) value;
			list = Arrays.asList(array);
		}
		return list;
	}

	public static String trimFront(String input) {
		int i = 0;
		while (i < input.length() && Character.isWhitespace(input.charAt(i)))
			i++;
		return input.substring(i);
	}

	public static List<StartEndPair> scan(String input, String splitStart,
			String splitEnd, boolean useEscaping) {
		List<StartEndPair> result = new ArrayList<StartEndPair>();
		int fromIndex = 0;
		while (true) {
			int exprStart = input.indexOf(splitStart, fromIndex);
			if (exprStart == -1) {
				break;
			}
			if (useEscaping && Util.isEscaped(input, exprStart)) {
				fromIndex = exprStart + splitStart.length();
				continue;
			}

			exprStart += splitStart.length();
			int exprEnd = input.indexOf(splitEnd, exprStart);
			if (exprEnd == -1) {
				break;
			}
			while (useEscaping && Util.isEscaped(input, exprEnd)) {
				exprEnd = input.indexOf(splitEnd, exprEnd + splitEnd.length());
			}

			fromIndex = exprEnd + splitEnd.length();

			StartEndPair startEndPair = new StartEndPair(exprStart, exprEnd);
			result.add(startEndPair);
		}
		return result;
	}


	static boolean isEscaped(String input, int index) {
		return isEscaped(input, index, MiniParser.DEFAULT_ESCAPE_CHAR);
	}

	static boolean isEscaped(String input, int index, char escapeCharacter) {
		boolean escaped;
		int leftOfIndex = index - 1;
		if (leftOfIndex >= 0) {
			if (input.charAt(leftOfIndex) == escapeCharacter) {
				int leftOfleftOfIndex = leftOfIndex - 1;
				escaped = leftOfleftOfIndex < 0
						|| input.charAt(leftOfleftOfIndex) != escapeCharacter;
			} else {
				escaped = false;
			}
		} else {
			escaped = false;
		}
		return escaped;
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> asSet(T... a) {
		return a != null ? new HashSet(Arrays.asList(a)) : Collections
				.emptySet();
	}

	public static String unifyNewlines(String source) {
		final String regex = "\\r?\\n";
		final String clearedSource = source.replaceAll(regex, "\n");
		return clearedSource;
	}

}
