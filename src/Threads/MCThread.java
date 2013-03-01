/*
 * 
 */
package Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.ConfigUtils;
import NAS.Coordinates;
import NAS.Detector;
import NAS.SelfPoint;

// TODO: Auto-generated Javadoc
/**
 * The Class MCThread.
 */
public class MCThread implements Runnable{
	
	/** List of SelfPoints. */
	private List<SelfPoint> selfPoint = new ArrayList<SelfPoint>();
	
	/** List of detectors. */
	private List<Detector> detectors = new ArrayList<Detector>();
	
	/** Number of MonteCarlo Simulation trials. */
	private int noOfTrials = Integer.parseInt(ConfigUtils.loadFileName("SimulationTrials"));
	
	/** Number of points in the Detector Region. */
	private int pointsInDetector = 0; 
	
	/** Trial number. */
	private int trialNo = 0;
	
	/** Dimension of the space. */
	private int dimension;
	
	/**
	 * Instantiates a new MC thread.
	 *
	 * @param selfPoint List of SelfPoints.
	 * @param detector List of detectors.
	 * @param dimension the dimension
	 */
	public MCThread(List<SelfPoint> selfPoint, List<Detector> detector, int dimension){
		this.dimension = dimension;
		this.selfPoint = selfPoint;
		this.detectors = detector;
	}
	
	/**
	 * Run method
	 * Checks the coverage with random candidate coordinates. 
	 */
	public void run() {
		while(trialNo < noOfTrials)	{
			Coordinates c = new Coordinates(dimension);

			for(SelfPoint selfP : selfPoint){
				if(selfP.isInSelfPointRegion(c)){
					continue;
				}
			}
			
			for(Detector d : detectors){
				if(d.isInDetectorRegion(c)){
					updatePointsInDetector();
					break;
				}
			}
			
			updateTrialNo();
		}
	}
		
	
	/**
	 * Runs the Monte Carlo Simulation.
	 *
	 * @return the coverage.
	 */
	public double simulate(){
		int noOfThread = Runtime.getRuntime().availableProcessors();
		Thread t[] = new Thread[noOfThread];
			   
		for(int i = 0; i < noOfThread; i++){
			t[i] = new Thread(this);
			t[i].start();
	   	}
		   
		try{
			for (int i = 0; i < noOfThread; i++)
				t[i].join();
		}
		catch(Exception ex){
			Logger.getLogger(MCThread.class.getCanonicalName())
			.log(Level.SEVERE, "Problem in joining for MonteCarlo Simulation");
		}
		
		return (this.pointsInDetector*100.0)/this.noOfTrials;
	}
	
	/**
	 * Update trial number.
	 */
	public synchronized void updateTrialNo()
	{
		this.trialNo++;
	}
	
	/**
	 * Update points in detector.
	 */
	public synchronized void updatePointsInDetector()
	{
		this.pointsInDetector++;
	}
}