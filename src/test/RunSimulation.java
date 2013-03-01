/*
 * 
 */
package test;

import hash.HashGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import Threads.MCThread;
import utils.ConfigUtils;

import NAS.Coordinates;
import NAS.Detector;
import NAS.SelfPoint;
import NAS.Vdetector;

// TODO: Auto-generated Javadoc
/**
 * The Class RunSimulation.
 */
public class RunSimulation {
	
	/** File with the input parameters. */
	String filename;
	
	/** The logger instance. */
	private static Logger logger;

	/** The family names. */
	private static String[] familyNames = ConfigUtils.loadStrings(ConfigUtils.loadFileName("FamilyNames"));

	/** The passwords. */
	private static String[] pass = ConfigUtils.loadStrings(ConfigUtils.loadFileName("Passwords"));
	
	
	/** Dimension of the space. */
	private int dimension;

	/** Total no self points. */
	private int totalNoSelfPoints;

	/** Proportion randomness. */
	private int proportionRandomness;

	/** Random pass length. */
	private int randomPassLen;

	/** Confusion parameter value. */
	private double selfRadius;

	/** Reuse data. */
	private boolean reuseData = false;

	/** Coverage. */
	private double coverage;

	/** List of SelfPoints. */
	private List<SelfPoint> selfPoints;
	
	/** MonteCarlo program instance. */
	private MCThread monteCarloSimulation;

	/** The Output Log file handler. */
	private String handlerString = ConfigUtils.loadFileName("OutputLogFile");

	/** Save SelfPoints. */
	private boolean saveSelfPoints = ConfigUtils.loadFileName("SaveSelfPoints").equalsIgnoreCase("true");

	/** Save detectors. */
	private boolean saveDetectorPoints = ConfigUtils.loadFileName("SaveDetectorPoints").equalsIgnoreCase("true");

	/** Location to save SelfPoints. */
	private String saveSelfLocation = ConfigUtils.loadFileName("SaveSelfLocation");

	/** Location to save detectors. */
	private String saveDetectorLocation = ConfigUtils.loadFileName("SaveDetectorLocation");

	/** The time stamp value. */
	private String timeStamp = "";

	/** Maximum number of detectors. */
	private int maxNoOfDetectors;
	
	/** Number of test points for Monte Carlo Simulation. */
	private int totalTestPoints;

	/** The file handler. */
	private FileHandler fh;

	/** The end time. */
	private Date startTime,endTime; 
	
	/** The time difference. */
	private long diffTime;

	/** The decimal point. */
	private int decimalPoint = Integer.parseInt(ConfigUtils.loadFileName("DecimalPoint"));
	
	/**
	 * Initiates the RunSimulation.
	 *
	 * @param filename 
	 * 			Experiment suit filename.
	 */
	public RunSimulation(String filename){
		this.filename = filename;
	}
	
	/**
	 * Initiates the RunSimulation.
	 * 
	 * Empty constructor.
	 */
	public RunSimulation(){	}
	
	
	/**
	 * Simulation epoch.
	 * 
	 * @param oneLineInput
	 *            The parameter line.
	 */
	public void simulationEpoch(String oneLineInput) {
		if (oneLineInput == null || oneLineInput.length() == 0) {
			logger.log(Level.SEVERE, "The line is null or blank");
		}
		String[] parameters = oneLineInput.split("\\s+");
		
		//Dimension
		try {
			dimension = Integer.parseInt(parameters[0]);
			
			if(dimension <= 0){
				logger.log(Level.SEVERE, "Dimension can not be less than 1");
			}
		}
		catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse dimension");
		}
		
		//Encryption method
		String encrptionMethod = parameters[1];
		
		//Number of SelfPoints
		try {
			totalNoSelfPoints = Integer.parseInt(parameters[2]);
			if(totalNoSelfPoints <= 0){
				logger.log(Level.SEVERE, "Total no of Self Points can not be less than 1");
			}
		}
		catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse total number of self points");
		}
		
		//Randomness of the Self point Generation
		try {
			proportionRandomness = Integer.parseInt(parameters[3]);
			if(proportionRandomness <= 0){
				logger.log(Level.SEVERE, "Proportion Randomness can not be less than 1");
			}
		}
		catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse the proportion of randomness");
		}
		
		//Random password length
		try {
			randomPassLen = Integer.parseInt(parameters[4]);
			if(randomPassLen <= 0){
				logger.log(Level.SEVERE, "Random Password Length can not be less than 1");
			}
		}
		catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse the random password length");
		}
		
		//Self point radius (Confusion Parameter)
		try {
			selfRadius = 1.0/Math.pow(10, decimalPoint);
			selfRadius = Double.parseDouble(parameters[5]);
			if(selfRadius <= 0 || selfRadius >= 1){
				logger.log(Level.SEVERE, "Self Radius can not be less than or equal to 0 and greater than or equal to 1");
			}
		}
		catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse the confusion parameter");
		}
		
		//Minimum Coverage
		try {
			coverage = Double.parseDouble(parameters[6]);
			if(coverage <= 0 || coverage >= 1){
				logger.log(Level.SEVERE, "Coverage can not be less than or equal to 0 and greater than or equal to 1");
			}
		}
		catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse the minimum coverage");
		}
		
		//Number of Simulation points
		try {
			totalTestPoints = Integer.parseInt(parameters[7]);
			if(totalTestPoints <= 0){
				logger.log(Level.SEVERE, "Total No of Test Points can not be less than 1");
			}
		}
		catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse the total test points");
		}
		
		//Use previous data if 1, Generate new data if 0
		try {
			if (Integer.parseInt(parameters[8]) == 1) {
				reuseData = true;
			} else
				reuseData = false;
		}
		catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse Reuse Data parameter");
		}

		//Maximum Number of Detectors
		try {
			maxNoOfDetectors = Integer.parseInt(parameters[9]);
			if(maxNoOfDetectors <= totalNoSelfPoints){
				logger.log(Level.SEVERE, "Max no of Detectors can not be less than total no of Self Points");
			}
		}
		catch (NumberFormatException ex) {
			logger.log(Level.SEVERE, "Can not parse the maximum number of detectors");
		}
				
				
		//The time-stamp value is date and time in 24 hour format
		timeStamp = new SimpleDateFormat("yyyyMMddkkmmss").format(new Date());

		//File name with time-stamp
		String currentFileName = handlerString + "_" + timeStamp + ".log";
		
		//File for saving the data
		try {
			fh = new FileHandler(currentFileName, 30000, 4);
			logger = Logger.getLogger(RunSimulation.class.getCanonicalName() + "" + timeStamp);
			logger.addHandler(fh);
		}
		catch (SecurityException e) {
			logger.log(Level.SEVERE,
					"Can not create the log file for Security Exception");
		} 
		catch (IOException e) {
			logger.log(Level.SEVERE,
					"Can not create the log file for IO exception");
		}
		
		// save the input line to the first line of the log file
		logger.log(Level.INFO, "" + oneLineInput);
		
		//Creating Self points
		if (!reuseData) {
			logger.log(Level.INFO, "Self points creation started");
		
			selfPoints = new ArrayList<SelfPoint>();

			HashGenerator generator = null;
			
			try{
				generator = new HashGenerator(encrptionMethod, dimension);
			}
			catch(NoSuchAlgorithmException ex){
				logger.log(Level.SEVERE, "error creating generator", ex);
			}
			
			Coordinates[] hashes = generator.getRandomHashPasswords(familyNames, pass, 
					totalNoSelfPoints, proportionRandomness, randomPassLen);
			
			for(int i = 0; i < totalNoSelfPoints; i++){
				selfPoints.add(new SelfPoint(hashes[i], selfRadius));
			}			
		} 
		else {
			logger.log(Level.INFO, "Using Reused data");
		}
		
		//Save the self points
		if (saveSelfPoints) {
			currentFileName = saveSelfLocation + "_" + timeStamp + ".txt";
			saveSelfPoints(currentFileName);
		}
		
		startTime = new Date();
		logger.log(Level.INFO, "Detector creation started");
		
		//Create V-Detector
		Vdetector vDetector = new Vdetector(dimension, selfRadius, coverage, maxNoOfDetectors);
		
		if (selfPoints == null || selfPoints.isEmpty()) {
			logger.log(Level.SEVERE,
					"The first line of input should not set Reuse Data parameter");
			System.exit(1);
		}
		
		//Create detectors
		vDetector.createVDetectors(selfPoints);
		
		endTime = new Date();
		
		//convert the time difference in seconds
		diffTime = (endTime.getTime()-startTime.getTime())/1000;
		
		// Calculate the detector statistics
		List<Detector> detectors = vDetector.getDetectors();

		// save the detector points
		if (saveDetectorPoints) {
			currentFileName = saveDetectorLocation + "_" + timeStamp + ".txt";
			saveDetectorPoints(currentFileName, detectors);
		}
		
		//Statistics
		DescriptiveStatistics stats = new DescriptiveStatistics();

		for (Detector dt : detectors) {
			stats.addValue(dt.getRadius());
		}

		double minVal = stats.getMin();
		double maxVal = stats.getMax();
		double avgVal = stats.getMean();
		double stdVal = stats.getStandardDeviation();

		logger.log(Level.INFO, "Computing started for test points");
		
		//Calculate detection rate
		double detectionRate = 0.0;
		
		monteCarloSimulation = new MCThread(selfPoints, detectors, dimension);
		detectionRate = monteCarloSimulation.simulate();
		
		// Logging the values
		logger.log(Level.INFO, "" + parameters[2] + ", " + parameters[5] + ", "
				+ parameters[6] + ", " + detectors.size() + ", "
				+ detectionRate + ", " + diffTime + ", " + minVal
				+ ", " + maxVal + ", " + avgVal + ", " + stdVal + ", "
				+ detectionRate);

		fh.close();
	}
	
	
	/**
	 * Reads lines from the input file and execute each line separately.
	 *
	 * @param filename 
	 * 			The input filename.
	 */
	
	public void experimentSuit(String filename){
		int lineNo = 1;
		
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(filename));
			String line = bf.readLine().trim();
			
			while (line != null) {
				if (!"".equals(line)) {
					System.err.println("Input no " + lineNo++);
					simulationEpoch(line);
				}
				line = bf.readLine();
			}
		} 
		catch (IOException ex) {
			logger.log(Level.SEVERE, "error realding file", ex);
		}
		finally{
			try{
				if(bf != null)
					bf.close();
			}
			catch(IOException ex){
				logger.log(Level.SEVERE, "Error in file Closing");
			}
		}
	}
	
	/**
	 * Save detector points.
	 *
	 * @param currentFileName 
	 * 			The file name to save detectors
	 * @param detectors
	 * 			The list of detectors 
	 */
	private void saveDetectorPoints(String currentFileName,	List<Detector> detectors) {
		BufferedWriter bf = null;
		
		try {
			bf = new BufferedWriter(new FileWriter(currentFileName));
			
			for (Detector dt : detectors) {
				bf.write(dt.getCenter().getCoordinatesStr() + "\t" + dt.getRadius() + "\n");
			}
		} 
		catch (IOException ex) {
			logger.log(Level.SEVERE, "error saving detector points", ex);
		}
		finally{
			try{
				if(bf != null)
					bf.close();
			}
			catch(IOException ex){
				logger.log(Level.SEVERE, "Error in file Closing");
			}
		}
	}

	/**
	 * Save self points.
	 *
	 * @param currentFileName 
	 * 			The file name to save SelfPoints
	 */
	private void saveSelfPoints(String currentFileName) {
		BufferedWriter bf = null;		
		
		try {
			bf = new BufferedWriter(new FileWriter(currentFileName));
			
			for (SelfPoint sf : selfPoints) {
				bf.write(sf.getCoordinates().getCoordinatesStr() + "\t" + selfRadius + "\n");
			}
		}
		catch (IOException ex) {
			logger.log(Level.SEVERE, "error saving self points", ex);
		}
		finally{
			try{
				if(bf != null)
					bf.close();
			}
			catch(IOException ex){
				logger.log(Level.SEVERE, "Error in file Closing");
			}
		}
	}
}
