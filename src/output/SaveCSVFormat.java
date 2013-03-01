/*
 * 
 */
package output;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.Ostermiller.util.ExcelCSVPrinter;

import utils.ConfigUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveCSVFormat.
 */
public class SaveCSVFormat {

	/** The Excel CSV printer. */
	private static ExcelCSVPrinter ecsvp;

	/** The string to be written to the CSV file. */
	private static String tobeWrite = "";

	/** The log file name. */
	private static String logFileName = "";

	/** The log file lines. */
	private static int logFileLines = 0;

	/** The log file contents. */
	private static String[] logFileContents;

	/** The one line contents. */
	private static String[] oneLineContents;

	/** The last index. */
	private static int lastIndex = 0;

	/** The time stamp value. */
	private static String timeStamp = "";

	/**
	 * The main method.
	 * 
	 * @param args
	 *            The arguments.
	 */
	public static void main(String[] args) {

		String[] Label = { "Dimension", "Encryption", "Total Number of self points", "Randomness",
				"Confusion parameter", "Minimum coverage", "Total detectors", "Detector Coverage",
				"Detector Creation Time", "Min radius", "Max radius", "Avg.Radius", 
				"Standard Deviation", "Detection Rate" };
		
		ConfigUtils.loadProperty();
		String folderName = ConfigUtils.loadFileName("OutputCSVFile");
		
		timeStamp = new SimpleDateFormat("yyyyMMddkkmmss").format(new Date());
		
		try {
			ecsvp = new ExcelCSVPrinter(new FileWriter(folderName + "_"
					+ timeStamp + ".csv", false), false, true); // THE OUTPUT
																// FILE APPEND
																// IS NOT
																// ENABLED,
																// AUTOFLUSH IS
																// ENABLED BY
																// DEFAULT
			ecsvp.print(Label);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		ecsvp.println();

		for (int i = 0; i < args.length; i++) {
			logFileName = args[i];
			logFileLines = ConfigUtils.loadStrings(args[i]).length;

			if (logFileLines != 10) {
				System.err.println("Log file is not complete ");
				System.exit(0);
			}

			logFileContents = ConfigUtils.loadStrings(logFileName);
			oneLineContents = logFileContents[1].replaceAll("INFO: ", "").split("\\s+");
			lastIndex = oneLineContents.length;
			tobeWrite += oneLineContents[0] + " " + oneLineContents[1] + " " + oneLineContents[2] 
						+ " " + oneLineContents[3] + " " + oneLineContents[5] + " ";

			if (Integer.parseInt(oneLineContents[lastIndex - 1]) == 0) {
				ecsvp.println();
			}
			
			oneLineContents = logFileContents[9].replaceAll("INFO: ", "").split("(\\s+)|[ ,]+");

			tobeWrite += oneLineContents[2] + " " + oneLineContents[3] + " " + oneLineContents[4] + " " 
						+ oneLineContents[5] + " " + oneLineContents[6] + " " + oneLineContents[7] + " "
						+ oneLineContents[8] + " " + oneLineContents[9] + " " + oneLineContents[10];
			oneLineContents = tobeWrite.split("\\s+");
			ecsvp.println(oneLineContents);
			tobeWrite = "";
		}

		if (!ecsvp.checkError()){
			try {
				ecsvp.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
