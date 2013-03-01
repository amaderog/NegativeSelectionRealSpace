/*
 * 
 */
package NAS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import utils.ConfigUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Coordinates.
 */
public class Coordinates {
	
	/** The dimension of the space. */
	private int dimension;
	
	/** The coordinate point list. */
	private ArrayList<Double> coordinate = new ArrayList <Double> ();
	
	/** The number of decimal places to consider after decimal point. */
	private int decimalPoint = Integer.parseInt(ConfigUtils.loadFileName("DecimalPoint"));
	
	
	/**
	 * Instantiates a new coordinates.
	 *
	 * @param dimension 
	 * 			Dimension of the space.
	 */
	public Coordinates (int dimension){
		this.dimension = dimension;
		this.generateCoordinates();
	}
	
	
	/**
	 * Instantiates a new coordinates.
	 *
	 * @param points 
	 * 			Coordinate values of the point. 
	 * @param dimension 
	 * 			Dimension of the space.
	 * @param hashSize
	 * 			Size of the hashed string
	 */
	public Coordinates (long[] points, int dimension, int hashSize){
		long max = 0;
		this.dimension = dimension;
		double [] pointsToAdd = new double[dimension];
		
		if(hashSize == 32){
			max = 9223372036854775807L;
		}
		else if (hashSize == 16){
			max = 2147483648L;
		}
		
		for(int i = 0; i < dimension; i++){
			pointsToAdd[i] = 1.0*points[i]/max;
			pointsToAdd[i] = pointsToAdd[i]/2;
			pointsToAdd[i] += 0.5;
		}
		
		
		for(int i = 0; i < dimension; i++){
			coordinate.add(new BigDecimal(pointsToAdd[i]).setScale(decimalPoint, RoundingMode.HALF_UP).doubleValue());
		}
	}
	

	/**
	 * Instantiates a new coordinates.
	 *
	 * @param points 
	 * 			Coordinate values of the point. 
	 */
	public Coordinates (double[] points){
		this.dimension = points.length;
		
		for(int i = 0; i < dimension; i++){
			coordinate.add(points[i]);
		}
	}
	
	
	/**
	 * Generate coordinate points.
	 */
	private void generateCoordinates(){
		for(int i = 0; i < this.dimension; i++){
			Random randomGenerator = new Random();
			coordinate.add(new BigDecimal(randomGenerator.nextDouble()).
								setScale(decimalPoint, RoundingMode.HALF_UP).doubleValue());
		}
	}

	/**
	 * Gets the dimension.
	 *
	 * @return the dimension
	 */
	public int getDimension(){
		return dimension;
	}
	
	
	/**
	 * Gets the coordinates as a String.
	 *
	 * @return the coordinates.
	 */
	public String getCoordinatesStr(){
		String str = "";
		for(int i = 0; i < dimension; i++){
			str += (this.coordinate.get(i) + "\t");
		}
		
		return str;
	}
	
	
	/**
	 * Gets the coordinate at position i.
	 *
	 * @param i 
	 * 		Position of the point.
	 * @return the coordinate at position i.
	 */
	public double getCoordinatesAtPositioni(int i){
		return this.coordinate.get(i);
	}
	
	
	/**
	 * Compare two coordinates if all the coordinate values are same.
	 *
	 * @param c 
	 * 			Coordinate to compare.
	 * @return true, if equal; false, otherwise
	 */
	public boolean isEqual(Coordinates c)
	{
		int i;
		
		for(i = 0; i < this.dimension; i++){
			if(this.coordinate.get(i) != c.coordinate.get(i))
				return false;
		}
		
		return true;
	}
	
	
	/**
	 * Gets the distance with coordinate a.
	 *
	 * @param a 
	 * 		Coordinate a.
	 * @return the distance with coordinate a.
	 */
	public double getDistance(Coordinates a){
		double sum = 0;
	
		for(int i = 0; i < dimension; i++){
			sum += Math.pow((this.getCoordinatesAtPositioni(i) - a.getCoordinatesAtPositioni(i)), 2);
		}
		
		return Math.sqrt(sum);
	}
}

