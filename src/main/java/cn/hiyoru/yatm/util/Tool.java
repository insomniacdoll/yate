package cn.hiyoru.yatm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

import cn.hiyoru.yatm.Engine;


/**
 * 命令行工具类
 * 
 */
public final class Tool {

	@SuppressWarnings("unchecked")
	public static String transform(File template, File propertiesFile,
			String charsetName) throws UnsupportedEncodingException,
			FileNotFoundException, IOException {
		if (!template.exists()) {
			throw new IllegalArgumentException(String.format(
					"Tempalte file '%s' does not exist", template
							.getCanonicalPath()));
		}
		if (!propertiesFile.exists()) {
			throw new IllegalArgumentException(String.format(
					"Properties file '%s' does not exist", propertiesFile
							.getCanonicalPath()));
		}
		FileInputStream stream = null;
		try {
			String input = Util.fileToString(template, charsetName);
			Engine engine = new Engine();

			Properties properties = new Properties();
			stream = new FileInputStream(propertiesFile);
			properties.load(stream);

			Map model = properties;
			String transformed = engine.transform(input, model);
			return transformed;
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}

	public static void main(String[] args) {
		try {
			String charsetName = "ISO-8859-15";
			if (args.length < 2) {
				System.err
						.println("We need at least template and properties file as input");
				System.exit(-1);
			}
			File template = new File(args[0]);
			File propertiesFile = new File(args[1]);
			if (args.length > 2) {
				charsetName = args[2];
			}
			String transformed = transform(template, propertiesFile,
					charsetName);
			System.out.println(transformed);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}
}
