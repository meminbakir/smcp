package modify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModifySBML {
	static String dir = "./resources/meb/";

	// public static void main(String[] args) {
	// // Read files from director
	// String from = dir + "from";// "test/";//
	// List<File> rawSBMLList = Utils.getFileList(new File(from));
	// // Validate each file
	// // rawSBMLList = isApproriate4Translation(rawSBMLList);// appropriate
	// // models only.
	// // Modify File
	// String targetDir = dir + "to/noName/";// "test/res/";//
	// modifySBMLs(rawSBMLList, targetDir);
	// }

	private static void modifySBMLs(List<File> rawSBMLList, String targetDir) {
		// int fileNo = 1;
		for (File rawFile : rawSBMLList) {
			String toPath = Modifier.modify(rawFile, (targetDir));// + fileNo+"_"
			// If model successfully modified, then run it with mce that make
			// sure there is no problem
			if (toPath == null) {
				System.out.println("There is error for modification of " + rawFile);
			} else {
				System.out.println(toPath + " modified successfully");
			}
			// MCETest.testByCommand(toPath);
			// fileNo++;
		}
	}

	/**
	 * Check if all source sbml are appropriate for translation
	 * 
	 * @param differ
	 */
	private static List<File> isApproriate4Translation(List<File> differ) {
		List<File> approriate = new ArrayList<File>();
		List<File> notAppropriate = new ArrayList<File>();
		for (File file : differ) {
			if (SBMLValidate.isApproriate4Translation(file.getPath())) {
				approriate.add(file);
			} else {
				notAppropriate.add(file);
				System.err.println(file.getPath() + " is not appropriate for translation");
				SBMLValidate.pressAnyKeyToContinue();
			}
		}
		System.out.println("Total not appropriate for translation " + notAppropriate.size());
		return approriate;
	}

	/**
	 * Loads the SWIG-generated libSBML Java module when this class is loaded,
	 * or reports a sensible diagnostic message about why it failed.
	 */
	static {
		try {
			System.loadLibrary("sbmlj");
			// For extra safety, check that the jar file is in the classpath.
			Class.forName("org.sbml.libsbml.libsbml");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Error encountered while attempting to load libSBML:");
			System.err
					.println("Please check the value of your "
							+ (System.getProperty("os.name").startsWith("Mac OS") ? "DYLD_LIBRARY_PATH"
									: "LD_LIBRARY_PATH")
							+ " environment variable and/or your" + " 'java.library.path' system property (depending on"
							+ " which one you are using) to make sure it list the" + " directories needed to find the "
							+ System.mapLibraryName("sbmlj") + " library file and"
							+ " libraries it depends upon (e.g., the XML parser).");
			System.exit(1);
		} catch (ClassNotFoundException e) {
			System.err.println("Error: unable to load the file 'libsbmlj.jar'."
					+ " It is likely that your -classpath command line "
					+ " setting or your CLASSPATH environment variable " + " do not include the file 'libsbmlj.jar'.");
			e.printStackTrace();

			System.exit(1);
		} catch (SecurityException e) {
			System.err.println("Error encountered while attempting to load libSBML:");
			e.printStackTrace();
			System.err.println("Could not load the libSBML library files due to a" + " security exception.\n");
			System.exit(1);
		}
	}

}
