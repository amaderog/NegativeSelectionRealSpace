/*
 * 
 */
package NAS;

// TODO: Auto-generated Javadoc
/**
 * The Class SelfPoint.
 */
public class SelfPoint{
	
	/** Coordinate of the point. */
	private Coordinates coordinates;
	
	/** The self radius. */
	private double selfRadius;
	
	
	/**
	 * Instantiate a new SelfPoint.
	 *
	 * @param dimension 
	 * 			Dimension of the space.
	 * @param radius 
	 * 			Confusion parameter value.
	 */
	public SelfPoint(int dimension, double radius){
		coordinates = new Coordinates(dimension);
		this.selfRadius = radius;
	}
	
	/**
	 * Instantiates a new SelfPoint.
	 *
	 * @param coordinate 
	 * 			Coordinate of the SelfPoint.
	 * @param radius 
	 * 			Confusion parameter value.
	 */
	public SelfPoint(Coordinates coordinate, double radius){
		coordinates = coordinate;
		this.selfRadius = radius;
	}
	
	
	/**
	 * Returns the coordinates of the point.
	 *
	 * @return the coordinates.
	 */
	public Coordinates getCoordinates(){
		return this.coordinates;
	}
	
	/**
	 * Checks whether a coordinate is in a self region or not.
	 *
	 * @param c 
	 * 		the Coordinate that is to be compare with
	 * @return true, if the point is in self region; false, otherwise
	 */		
	public boolean isInSelfPointRegion(Coordinates c){
		if((this.coordinates.getDistance(c) - this.selfRadius) <= 0){
			return true;
		}
		return false;
	}
}
