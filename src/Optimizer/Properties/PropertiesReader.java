package Optimizer.Properties;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesReader {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, InstantiationException, InvocationTargetException {
		// TODO Auto-generated method stub

		// PropertiesReader app = new PropertiesReader();
		// app.printThemAll();

		HashMap<String, String> H = new PropertiesReader().Read("config.properties");
		System.out.println(H);
		new PropertiesSetter().Set(H);

	}

	// From https://www.mkyong.com/java/java-properties-file-examples/
	public HashMap<String, String> Read(String FilePath) {
		HashMap<String, String> PropertiesValues = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = FilePath;
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Unable to find " + filename);
				return null;
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				PropertiesValues.put(key, value);
				System.out.println("Key : " + key + ", Value : " + value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return PropertiesValues;
	}
}
