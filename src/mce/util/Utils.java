package mce.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import output.Output;

/**
 * @author Mehmet
 *
 */
public class Utils {
	private static final Logger log = LoggerFactory.getLogger(Utils.class);
	public static final int TIMEOUT = 3600;// process time out, 1 hour in SECONDS
	public static final TimeUnit TIMEUNIT = TimeUnit.SECONDS;
	public static final String startInLowerPriority = "nice -n 10 ";
	private static final DecimalFormatSymbols DECIMAL_FORMAT = DecimalFormatSymbols.getInstance(Locale.ENGLISH);

	// Prints out message.
	public static void out(String message) {
		System.out.println(message);

	}

	private static long timeNOW() {
		return System.nanoTime();
	}

	public static void runCommand(String mcCommand, Output output) throws IOException, InterruptedException {

		long startTime = Utils.timeNOW();
		Process process = Runtime.getRuntime().exec(mcCommand);
		waitForTimeOut(startTime, process, output);
		int errCode = process.exitValue();
		// if no timeout error, get the output
		if (!hasTimeOutError(output)) {
			getExecutionOutput(process, output);
		}
		// make sure any error other than timeout, will make null the error and
		// time values.
		if (errCode != 0 || output.isError) {
			output.isError = true;
			output.verificationTime = null;
		}
	}

	public static Output runCommand(String[] mcCommand, Output output) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(mcCommand);

		long startTime = Utils.timeNOW();
		Process process = pb.start();
		waitForTimeOut(startTime, process, output);
		int errCode = process.exitValue();
		// if no timeout error, get the output
		if (!hasTimeOutError(output)) {
			getExecutionOutput(process, output);
		}
		// make sure any error other then timeout, will make null the error and
		// time values.
		if (errCode != 0 || output.isError) {
			output.isError = true;
			output.verificationTime = null;
		}
		return output;
	}

	/**
	 * Run external tools command, e.g., Prism, Assign their outputs to
	 * output.extRes
	 * 
	 * @param extToolCommand
	 * @param output
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public static void runExternalToolCommand(String[] extToolCommand, Output output)
			throws IOException, InterruptedException {

		ProcessBuilder pb = new ProcessBuilder(extToolCommand);

		long startTime = Utils.timeNOW();
		Process process = pb.start();

		waitForExtToolTimeOut(startTime, process, output);

		int errCode = process.exitValue();
		if (!hasTimeOutError(output)) {
			Utils.getExternalToolExecutionOutput(process, output);
		}
		// any error other than timeout will be taken
		if (errCode != 0 || output.isError) {
			output.isError = true;
			output.externalToolTime = null;
			String error = "Error rised from the EXTERNAL TOOL(for " + output.mcType + "). \n";
			output.error = error + output.error;
		}
	}

	public static void getExecutionOutput(Process process, Output output) throws IOException {
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		String result = "";
		String out = "";
		while ((result = stdInput.readLine()) != null) {
			if (!output.isError) {
				if (result.toLowerCase().contains("error"))
					output.isError = true;
			}
			out += result.isEmpty() ? "" : result + "\n";
		}
		result = "";
		while ((result = stdError.readLine()) != null) {
			output.isError = true;
			out += result.isEmpty() ? "" : result + "\n";
		}
		if (output.isError)
			output.error += out;
		else
			output.verResult += out;
	}

	/***
	 * It returns catch the output of external tools before the verification output.
	 * We use this to hide unnecessary outputs. Its details will be displayed only
	 * error occurs.
	 * 
	 * @param process
	 * @param output
	 * @throws IOException
	 */
	public static void getExternalToolExecutionOutput(Process process, Output output) throws IOException {
		output.hasExtTool = true;
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		String result = "";
		String out = "";
		while ((result = stdInput.readLine()) != null) {
			// The external tools cannot run model, so it
			if (!output.isError) {
				if (result.toLowerCase().contains("error"))
					output.isError = true;
			}
			out += result.isEmpty() ? "" : result + "\n";
		}
		result = "";
		while ((result = stdError.readLine()) != null) {
			output.isError = true;
			out += result.isEmpty() ? "" : result + "\n";
		}
		if (output.isError)
			output.error += out;
		else
			output.extResult += out;
	}

	private static boolean hasTimeOutError(Output output) {
		if (output.isError) {
			if (output.error.contains("TIMEOUT"))
				return true;
		}
		return false;
	}

	/**
	 * Sets timeout for process, makes sure the process will be completed in given
	 * time. If process doesn't exit on time, it is regarded as error
	 * 
	 * @param startTime
	 * @param process
	 * @param output
	 * @throws InterruptedException
	 */
	private static void waitForTimeOut(long startTime, Process process, Output output) throws InterruptedException {
		// TODO this timeout can be removed or leaved after tests.
		Long timeOut = (long) TIMEOUT;
		if (output.hasExtTool) {
			timeOut = timeOut - TimeUnit.SECONDS.convert(output.externalToolTime, TimeUnit.NANOSECONDS);// remained time
																										// for
																										// the MC tool
		}
		boolean onTime = process.waitFor(timeOut, TIMEUNIT);
		Long elapsedTime = Utils.timeNOW() - startTime;
		// process is not completed on time, so we have interrupted it.
		if (!onTime) {
			output.isError = true;
			output.error = "TIMEOUT:The execution of " + output.mcType + " took longer than " + TIMEOUT + " " + TIMEUNIT
					+ "\n";
			elapsedTime = null;
			if (process.isAlive()) {
				process.destroyForcibly();
				process.waitFor();
			}
		}
		output.verificationTime = elapsedTime;

	}

	/**
	 * Sets timeout for external tool process, makes sure the process will be
	 * completed in given time. If process doesn't exit on time, it is regarded as
	 * error
	 * 
	 * @param startTime
	 * @param process
	 * @param output
	 * @throws InterruptedException
	 */
	private static void waitForExtToolTimeOut(long startTime, Process process, Output output)
			throws InterruptedException {

		// TODO this timeout can be removed or leaved after tests.
		boolean onTime = process.waitFor(TIMEOUT, TIMEUNIT);
		Long elapsedTime = Utils.timeNOW() - startTime;
		// process is not completed on time, so we have interrupted it.
		if (!onTime) {
			output.isError = true;
			output.error += "TIMEOUT:The execution of EXTERNAL TOOL (for " + output.mcType + ") took longer than "
					+ TIMEOUT + " " + TIMEUNIT + "\n";
			elapsedTime = null;
			if (process.isAlive()) {
				process.destroyForcibly();
				process.waitFor();
			}
		}
		output.externalToolTime = elapsedTime;

	}

	/**
	 * When external program launched with process, it might not end with destroy,
	 * therefore we have to find and kill it separately
	 * 
	 * @param externalProcessName
	 *            the process name which will be killed
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void makeSureExternalProcessHasEnded(String externalProcessName)
			throws InterruptedException, IOException {
		// kill process is platform dependent
		switch (Utils.getOS()) {
		case WINDOWS:
			Utils.makeSureExternalWindowsProcessHasEnded(externalProcessName);
			break;
		default:
			Utils.makeSureExternalLinuxProcessHasEnded(externalProcessName);
			break;
		}

	}

	public static void makeSureExternalLinuxProcessHasEnded(String externalProcessName)
			throws InterruptedException, IOException {
		String command = "ps -ef | grep " + externalProcessName + " | grep -v grep | awk '{print $2}' | xargs kill";

		ProcessBuilder ps = new ProcessBuilder(new String[] { "/bin/sh", "-c", command });
		// ps.redirectErrorStream(true);
		Process pr = ps.start();
		// BufferedReader in = new BufferedReader(new InputStreamReader(
		// pr.getInputStream()));
		// String line;
		// while ((line = in.readLine()) != null) {
		// System.out.println(line);
		// }
		pr.waitFor();
		// in.close();
	}

	/**
	 * Has not tested for windows platform
	 * 
	 * @param externalProcessName
	 * @throws IOException
	 * @throws Exception
	 */
	public static void makeSureExternalWindowsProcessHasEnded(String externalProcessName) throws IOException {
		if (isWindowsProcessRunning(externalProcessName)) {
			killWindowsProcess(externalProcessName);
		}

	}

	public static boolean isWindowsProcessRunning(String externalProcessName) throws IOException {
		boolean running = false;
		final String TASKLIST = "tasklist";

		Process p = Runtime.getRuntime().exec(TASKLIST);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains(externalProcessName)) {
				running = true;
				break;
			}
		}
		reader.close();
		return running;

	}

	public static void killWindowsProcess(String externalProcessName) throws IOException {
		final String KILL = "taskkill /F /IM ";
		Runtime.getRuntime().exec(KILL + externalProcessName);

	}

	// TODO We can check the OS when app launches.
	public enum OS {
		WINDOWS, LINUX, MAC, SOLARIS
	};// Operating systems.

	public static OS getOS() {
		OS os = null;
		if (os == null) {
			String operSys = System.getProperty("os.name").toLowerCase();
			if (operSys.contains("win")) {
				os = OS.WINDOWS;
			} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
				os = OS.LINUX;
			} else if (operSys.contains("mac")) {
				os = OS.MAC;
			} else if (operSys.contains("sunos")) {
				os = OS.SOLARIS;
			}
		}
		return os;
	}

	/**
	 * Returns date and/or time with/without specified format.
	 * 
	 * @param isDate
	 *            should return date
	 * @param isTime
	 *            should return time
	 * @param format
	 *            use this format otherwise use dd/MM/YYYY HH:mm:ss format
	 * @return
	 */
	public static String getDateandTime(boolean isDate, boolean isTime, String format) {
		DateFormat dateFormat = null;
		// return specified format
		if (format != null && !format.isEmpty()) {
			dateFormat = new SimpleDateFormat(format);
		} else {
			// return both in static format
			if (isDate && isTime) {
				dateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
			} else if (isDate) {
				// return only date
				dateFormat = new SimpleDateFormat("dd/MM/YYYY");
			} else if (isTime) {
				// return only time
				dateFormat = new SimpleDateFormat("HH:mm:ss");
			}
		}
		if (dateFormat != null) {
			Date date = new Date();
			return dateFormat.format(date);
		} else {
			String error = "Date or Time is not set.";
			out(error);
			log.error(error);
			return error;
		}
	}

	/**
	 * Returns files inside current directory, excludes sub directories, and sorts
	 * files by size
	 * 
	 * @param sbmlDirectory
	 * @return
	 */
	public static List<File> getFileList(File sbmlDirectory) {
		File[] fList = sbmlDirectory.listFiles();
		List<File> filesOnly = new ArrayList<File>();
		for (int i = 0; i < fList.length; i++) {
			if (fList[i].isFile())
				filesOnly.add(fList[i]);

		}
		Collections.sort(filesOnly, (f1, f2) -> {
			return Long.compare(f1.length(), f2.length());
		});
		return filesOnly;
	}

	/**
	 * Reads the content of file, returns it as String
	 * 
	 * @param filePath
	 *            the filePath
	 * @return the content of the file, as String
	 */
	public static String readFileContent(String filePath) {
		String fileContent = "";
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(filePath), Charset.defaultCharset());
			for (String line : lines) {
				fileContent += line + "\n";
			}
		} catch (IOException e) {

			log.error("The model cannot read from:" + filePath);
			fileContent = null;
			e.printStackTrace();
		}
		return fileContent;
	}

	/**
	 * Writes file
	 * 
	 * @param outputFilePath
	 *            file path
	 * @param output
	 *            content
	 * @param append
	 *            true appends content, false rewrites the file
	 * @return
	 */

	public static String write2File(String outputFilePath, String output, boolean append) {
		BufferedWriter writer = null;
		try {
			File file = new File(outputFilePath);
			file.getParentFile().mkdirs();
			writer = new BufferedWriter(new FileWriter(file, append));
			writer.write(output);

		} catch (IOException e) {
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				log.error("The model cannot be saved to absolute path of:" + outputFilePath);
				outputFilePath = null;
			}

		}
		return outputFilePath;
	}

	public static String lineSeparator() {
		String lineSeparator = "\n=================--------------------------=================\n";
		return lineSeparator;
	}

	/**
	 * Delete a directory, sub-directories and files, recursively
	 * 
	 * @param directory
	 */
	public static void deleteDirectory(File directory) throws IOException {
		if (directory.isDirectory()) {
			// directory is empty, then delete it
			if (directory.list().length == 0) {
				directory.delete();
				System.out.println("Directory is deleted : " + directory.getAbsolutePath());

			} else {
				// list all the directory contents
				String files[] = directory.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(directory, temp);
					// recursive delete
					deleteDirectory(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (directory.list().length == 0) {
					directory.delete();
					System.out.println("Directory is deleted : " + directory.getAbsolutePath());
				}
			}

		} else {
			// if file, then delete it
			directory.delete();
			System.out.println("File is deleted : " + directory.getAbsolutePath());
		}
	}

	public static String convertFromScientificNotation(double number) {
		// Check if in scientific notation
		NumberFormat formatter = new DecimalFormat("0", DECIMAL_FORMAT);
		formatter.setMaximumFractionDigits(340);
		return formatter.format(number);
	}

	public static String convertFromScientificNotation(String number) {
		return convertFromScientificNotation(Double.valueOf(number));
	}
}
