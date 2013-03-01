/*
 * 
 */
package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

// TODO: Auto-generated Javadoc
/**
 * The Class Utility.
 */
public class Utility {
	
	
	/**
	 * Convert to fixed decimal places after the decimal point.
	 *
	 * @param number 
	 * 			The actual number.
	 * @param decimalPlaces 
	 * 			Decimal places after decimal point.
	 * @return the number with fixed decimal places after decimal point.
	 */
	public static Double convertToFixedDecimalPlace(Double number, int decimalPlaces){
		number = new BigDecimal(number).setScale(decimalPlaces, RoundingMode.HALF_UP).doubleValue();

		return number;
	}
	
	
	/**
	 * Elapsed time.
	 *
	 * @param start 
	 * 			Start time.
	 * @param process 
	 * 			Process name.
	 * @return Elapsed time.
	 */
	public static String elapsedTime(long start, String process){
		// Get elapsed time in milliseconds
		long elapsedTimeMillis = System.currentTimeMillis()-start;

		// Get elapsed time in seconds
		float elapsedTimeSec = elapsedTimeMillis/1000F;

		// Get elapsed time in minutes
		float elapsedTimeMin = (float) Math.floor(elapsedTimeMillis/(60*1000F));

		// Get elapsed time in hours
		float elapsedTimeHour = (float) Math.floor(elapsedTimeMillis/(60*60*1000F));

		// Get elapsed time in days
		float elapsedTimeDay = (float) Math.floor(elapsedTimeMillis/(24*60*60*1000F));
		
		return "Elapsed Time for " + process + ": " + elapsedTimeDay + " Day " + elapsedTimeHour + " Hour " + elapsedTimeMin + " Min " +
								elapsedTimeSec + " Sec. Total MilliSecond:  " + elapsedTimeMillis; 
	}
	
	/**
	 * Elapsed time in short form.
	 *
	 * @param start 
	 * 			Start time.
	 * @param process 
	 * 			Process name.
	 * @return Elapsed time.
	 */
	public static String elapsedTimeShortForm(long start, String process){
		// Get elapsed time in milliseconds
		long elapsedTimeMillis = System.currentTimeMillis()-start;

		// Get elapsed time in seconds
		float elapsedTimeSec = elapsedTimeMillis/1000F;

		// Get elapsed time in minutes
		float elapsedTimeMin = (float) Math.floor(elapsedTimeMillis/(60*1000F));

		// Get elapsed time in hours
		float elapsedTimeHour = (float) Math.floor(elapsedTimeMillis/(60*60*1000F));

		// Get elapsed time in days
		float elapsedTimeDay = (float) Math.floor(elapsedTimeMillis/(24*60*60*1000F));
		
		return elapsedTimeDay + ": " + elapsedTimeHour + ": " + elapsedTimeMin + ": " + elapsedTimeSec; 
	}
}
