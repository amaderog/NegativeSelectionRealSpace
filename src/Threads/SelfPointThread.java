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
import NAS.SelfPoint;

// TODO: Auto-generated Javadoc
/**
 * The Class SelfPointThread.
 */
public class SelfPointThread implements Runnable{
	
	/** Thread stop flag. */
	private boolean shouldStop = false;
	
	/** The index. */
	private int index = 0;
	
	/** The center. */
	private Coordinates center;
	
	/** The radius. */
	private double radius;
	
	/** The list of SelfPoints. */
	private List<SelfPoint> selfPoint = new ArrayList<SelfPoint>();
	
	/** The Confusion Parameter. */
	private double selfRadius;
	
	/** The number of decimal places to consider after decimal point. */
	private int decimalPoint;
	
	/** The minimum distance. */
	private double minimumDistance;
	
	/**
	 * Instantiates a new self point thread.
	 *
	 * @param selfP 
	 * 		The list of SelfPoints. 
	 * @param selfR 
	 * 		Confusion Parameter.
	 * @param decimalpoints 
	 * 		The number of decimal places to consider after decimal point. 
	 * @param candidate 
	 * 		The candidate coordinate.
	 */
	public SelfPointThread(List<SelfPoint> selfP, double selfR, int decimalpoints, Coordinates candidate){
		this.center = candidate;
		this.selfPoint = selfP;
		this.selfRadius = selfR;
		this.decimalPoint = decimalpoints;
		this.minimumDistance = (1.0/Math.pow(10,decimalPoint));
	}
	
	/**
	 * Run method
	 * Checks candidate coordinate with all existing SelfPoints. 
	 */
	@Override
	public void run() {
		while(!shouldStop){
			if(index >= selfPoint.size()) {
				break;
			}

			SelfPoint selfPoint = getSelfPoint();
			
			double distance = Utility.convertToFixedDecimalPlace(center.getDistance(
					selfPoint.getCoordinates()) - selfRadius, decimalPoint+2) - minimumDistance;
			
			if(distance < 0){
				shouldStop = true;
			}
			
			updateRadiusWithSelfPoint(distance);
		}
	}
		
	
	/**
	 * Test the candidate coordinate with the existing SelfPoints.
	 *
	  * @param r 
	 * 			The assumed radius.
	 * @return the calculated radius.
	*/
	public double testSelf(double r){
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
			Logger.getLogger(SelfPointThread.class.getCanonicalName())
			.log(Level.SEVERE, "Problem in joining for Self List");
		}
		
		if(shouldStop)
			return -1;
		
		return radius;
	}
	
	/**
	 * Gets next SelfPoint from the SelfPoints list.
	 *
	 * @return the SelfPoint.
	 */
	private synchronized SelfPoint getSelfPoint()
	{
		if(index >= selfPoint.size()){
			return selfPoint.get(index-1);
		}
		
		SelfPoint selfP = selfPoint.get(index);
		index++;
		return selfP;
	}


	/**
	 * Update radius with self point.
	 *
	 * @param distance 
	 * 			The calculated distance. 
	 */
	private synchronized void updateRadiusWithSelfPoint(double distance)
	{
		if(radius > distance){
			radius = distance;
		}
	}
}