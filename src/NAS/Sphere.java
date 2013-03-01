/*
 * 
 */
package NAS;


// TODO: Auto-generated Javadoc
/**
 * The Class Sphere.
 */
public abstract class Sphere{

	/** The center of the Sphere. */
	private Coordinates center;
	
	/**
	 * Instantiates a new sphere.
	 *
	 * @param center 
	 * 			Center of the Sphere.
	 */
	public Sphere (Coordinates center){
		this.center = center;
	}
	
	/**
	 * Gets the center of the Sphere.
	 *
	 * @return the center
	 */
	public Coordinates getCenter(){
		return this.center;
	}
}
