/**
 * 
 */
package mce.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class Config {
	public static String pythonInterpreter = null;// the absolute path of active python3 interpreter path

	private static final Logger log = LoggerFactory.getLogger(Config.class);

	/**
	 * Load configurations from config.properties file
	 */
	public static void loadConfigurations() {
		Properties prop = new Properties();
		// InputStream input = null;

		try {
			// System.out.println("Start Config");

			File allExecutables = new File(System.getProperty("java.class.path"));
			String binPath = allExecutables.getAbsoluteFile().toString().split(File.pathSeparator)[0];
			File binDir = new File(binPath);
			File projDir = binDir.getAbsoluteFile().getParentFile();
			String projPath = projDir.toString();
			// System.out.println("path0:" + projPath);

			// File jarPath = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			// String pPat = jarPath.getParentFile().getAbsolutePath();
			// System.out.println(" propertiesPath-1:" + pPat);
			// File projectPath = new File(MCE.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			// String propertiesPath = projectPath.getParent();
			String fileName = projPath + File.separator + "config.properties";// propertiesPath + File.separator +
			// System.out.println("Path for Config:" + fileName);
			// input = Config.class.getClassLoader().getResourceAsStream(fileName);
			// if (input == null) {
			// System.out.println("Sorry, unable to find config file " + fileName);
			// return;
			// }
			// load a properties file from class path, inside static method
			prop.load(new FileInputStream(fileName));

			// Load each property to a static variable
			if (prop.getProperty("python") != null) {
				pythonInterpreter = prop.getProperty("python");
				// System.out.println(pythonInterpreter);
			} else {
				log.error("Python interpreter not found in " + fileName);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// finally {
		// if (input != null) {
		// try {
		// input.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// ClassLoader loader = MCE.class.getClassLoader();
		// System.out.println(new MCE().getClass().getProtectionDomain().getCodeSource().getLocation());
		// File projectPath = new File(MCE.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		// String propertiesPath = projectPath.getParentFile().getAbsolutePath();
		// System.out.println(" propertiesPath-" + propertiesPath);
		// // prop.load(new FileInputStream(propertiesPath + "/importer.properties"));

		Config.loadConfigurations();
	}

}
