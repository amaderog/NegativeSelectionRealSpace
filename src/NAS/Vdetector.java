/*
 * 
 */
package NAS;

import java.util.List;
import java.util.ArrayList;

import Threads.DetectorThread;
import Threads.SelfPointThread;

import utils.ConfigUtils;
import utils.Utility;

// TODO: Auto-generated Javadoc
/**
 * The Class Vdetector.
 */
public class Vdetector {
	
	/** List of the SelfPoints. */
	private List<SelfPoint> selfPoint = new ArrayList<SelfPoint>();
	
	/** List of the detectors. */
	private List<Detector> detectors = new ArrayList<Detector>();
	
	/** Number of Detectors. */
	private int noOfDetector = 0;
	
	/** Dimension of the space. */
	private int dimension;
	
	/** Confusion parameter value. */
	private double selfRadius;
	
	/** The number of decimal places to consider after decimal point. */
	private int decimalPoint = Integer.parseInt(ConfigUtils.loadFileName("DecimalPoint"));
	
	/** The number of unsuccessful attempts. */
	private int noOfUnsuccessfulAttempts;
	
	/** Maximum number of detectors to be created. */
	private int maxNoOfDetectors;
	
	/** The minimum radius of the detector. */
	private double minimumRadius = Double.parseDouble(ConfigUtils.loadFileName("MinimumRadius"));
	
	/** The center. */
	private Coordinates center;
	
	/** The radius. */
	private double radius;
	
	/**
	 * Instantiates a new Vdetector.
	 *
	 * @param dimension 
	 * 			Dimension of the space.
	 * @param selfRadius 
	 * 			Confusion parameter value.
	 * @param coverage 
	 * 			Expected coverage.
	 * @param maxNoofDetectors 
	 * 			Maximum number of detectors.
	 */
	public Vdetector(int dimension, double selfRadius, double coverage, int maxNoofDetectors) {
		this.dimension = dimension;
		this.selfRadius = selfRadius;
		this.maxNoOfDetectors = maxNoofDetectors;
		this.noOfUnsuccessfulAttempts = (int)(1.0/(1.0-coverage));
	}

	/**
	 * Creates Detectors randomly.
	 *
	 * @return true, if detector created; false, otherwise
	 */
	public boolean createDetectors(){
		center = new Coordinates(this.dimension);
		radius = this.dimension; // in n dimensional space max distance is sqrt(n)
		
		SelfPointThread st = new SelfPointThread(selfPoint, selfRadius, decimalPoint, center);
		radius = st.testSelf(radius);
		
		if(radius < 0){
			return false;
		}
				
		DetectorThread dt = new DetectorThread(detectors, decimalPoint, center);
		radius = dt.testDetector(radius);
		
		if(radius < 0){
			return false;
		}			

		if (radius < minimumRadius)
			return false;
		
		detectors.add(new Detector(Utility.convertToFixedDecimalPlace(radius, decimalPoint), center));
		
		return true;
	}
	
	
	/**
	 * Creates the Vdetectors.
	 *
	 * @param selfPoints 
	 * 			The SelfPoint list.
	 */
	public void createVDetectors(List<SelfPoint> selfPoints){
		selfPoints.remove(null);
		if (selfPoints == null || selfPoints.size() == 0)
			return;
		
		this.selfPoint.addAll(selfPoints);
		int failureCount = 0;
		
		
		while(true){
			if(this.createDetectors()){
				failureCount = 0;
				noOfDetector++;
			}
			else {
				failureCount++;
			}
			
			if(failureCount > noOfUnsuccessfulAttempts || noOfDetector >= maxNoOfDetectors)
				break;
		}
	}
	
	
	/**
	 * Gets the detectors.
	 * 
	 * @return the list of detectors
	 */
	public List<Detector> getDetectors() {
		return detectors;
	}
}
