/*
 * 
 */
package Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.Utility;
import NAS.Coordinates;
import NAS.Detector;;

// TODO: Auto-generated Javadoc
/**
 * The Class DetectorThread.
 */
public class DetectorThread implements Runnable{
	
	/** Thread stop flag. */
	private boolean shouldStop = false;
	
	/** The index. */
	private int index = 0;
	
	/** The center. */
	private Coordinates center;
	
	/** The radius. */
	private double radius;
	
	/** The list of detectors. */
	private List<Detector> detectors = new ArrayList<Detector>();
	
	/** The number of decimal places to consider after decimal point. */
	private int decimalPoint;
	
	/**
	 * Instantiates a new detector thread.
	 *
	 * @param detectors 
	 * 			The list of detectors.
	 * @param decimalpoints 
	 * 			The number of decimal places to consider after decimal point.
	 * @param candidate 
	 * 			The candidate coordinator.
	 */
	public DetectorThread(List<Detector> detectors, int decimalpoints, Coordinates candidate){
		this.center = candidate;
		this.decimalPoint = decimalpoints;
		this.detectors = detectors;
	}
	
	/**
	 * Run method
	 * Checks candidate coordinate with all existing detectors. 
	 */
	@Override
	public void run() {
		while(!shouldStop) {
			if(index >= detectors.size()) {
				break;
			}

			Detector detector = getDetector();
			
			double distance = Utility.convertToFixedDecimalPlace(center.getDistance(
								detector.getCenter()) - detector.getRadius(), decimalPoint+2);
			
			
			if(distance < 0) {
				shouldStop = true;
				return;
			}
			
			//if they overlap
			double overlap = radius - distance; 
			
			double deduct = 0;
			
			if(radius > detector.getRadius()){
				if(overlap > (0.5*detector.getRadius())){
					deduct = overlap - (0.5*detector.getRadius());
				}
			}
			else{
				if(overlap > (0.5*radius)){
					deduct = overlap - (0.5*radius);
				}
			}
				
			if(deduct > 0){
				updateRadius(deduct);
			}
		}
	}
		
	
	/**
	 * Test the candidate coordinate with the existing detectors.
	 *
	 * @param r 
	 * 			The assumed radius.
	 * @return the calculated radius.
	 */
	public double testDetector(double r){
		int noOfThread = Runtime.getRuntime().availableProcessors();
		Thread t[] = new Thread[noOfThread];
		
		this.radius = r;
			   
		for(int i = 0; i < noOfThread; i++){
			t[i] = new Thread(this);
			t[i].start();
	   	}
		   
		try{
			for (int i = 0; i < noOfThread; i++)
				t[i].join();
		}
		catch(Exception ex){
			Logger.getLogger(DetectorThread.class.getCanonicalName())
			.log(Level.SEVERE, "Problem in joining for Detector List");
		}
		
		if(shouldStop)
			return -1;
		
		return radius;
	}
	
	/**
	 * Gets next detector from the detector list.
	 *
	 * @return the detector
	 */
	private synchronized Detector getDetector()
	{
		if(index >= detectors.size()){
			return detectors.get(index-1);
		}
		
		Detector detector = detectors.get(index);
		index++;
		return detector;
	}
	
	
	
	/**
	 * Update radius.
	 *
	 * @param deduct the deduct
	 */
	public synchronized void updateRadius(double deduct)
	{
		radius -= deduct;
	}
}