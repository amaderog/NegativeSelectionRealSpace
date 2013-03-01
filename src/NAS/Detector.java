/*
 * 
 */
package NAS;


// TODO: Auto-generated Javadoc
/**
 * The Class Detector.
 */
public class Detector extends Sphere{
	
	/** The radius of the detector. */
	private double radius;
	

	/**
	 * Instantiates a new detector.
	 *
	 * @param radius 
	 * 			The detector radius.
	 * @param center 
	 * 			The center of the detector.
	 */
	public Detector(double radius, Coordinates center) {
		super(center);
		this.radius = radius;
	}
	
	
	/**
	 * Gets the radius.
	 *
	 * @return the radius of the detector.
	 */
	public double getRadius() {
		return radius;
	}
	
	
	/**
	 * Checks whether a coordinate is in the detector region or not.
	 *
	 * @param c 
	 * 			The coordinate that is to be compare with
	 * @return true, if the point is in the detector region; false, otherwise
	 */		
	public boolean isInDetectorRegion(Coordinates c){
		if((this.getCenter().getDistance(c) - this.radius) <= 0){
			return true;
		}
		return false;
	}
}
