/*
 * 
 */
package hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import NAS.Coordinates;

// TODO: Auto-generated Javadoc
/**
 * The Class HashGenerator.
 */
public class HashGenerator {

	/** The Constant passwordAlphabet. */
	public static final char[] passwordAlphabet = { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '!', ':', '+', '-', '*' };

	/** The digest. */
	private MessageDigest digest;
	
	/** The dimension. */
	private int dimension; 

	/** The Random class. */
	private Random rnd = new Random();
	
	/**
	 * Gets the hash passwords.
	 * 
	 * @param password
	 *            The password.
	 * @return the hash passwords.
	 */
	public byte[] getHashPasswords(String password) {
		return digest.digest(password.getBytes());
	}

	/**
	 * Sets the digest.
	 * 
	 * @param algorithm
	 *            The new digest.
	 * @throws NoSuchAlgorithmException
	 *             The no such algorithm exception.
	 */
	public final void setDigest(String algorithm)
			throws NoSuchAlgorithmException {
		digest = MessageDigest.getInstance(algorithm);
	}

	/**
	 * Instantiates a new hash generator.
	 *
	 * @param algorithm The name of encryption algorithm.
	 * @param dimension the dimension
	 * @throws NoSuchAlgorithmException The no such algorithm exception.
	 */
	public HashGenerator(String algorithm, int dimension) throws NoSuchAlgorithmException {
		this.dimension = dimension;
		setDigest(algorithm);
	}

	/**
	 * To Coordinate.
	 * 
	 * @param s
	 *            The byte array.
	 * @param size
	 *            The size of byte array.
	 * @return the BitVector
	 */
	public Coordinates toCoordinate(byte[] s, int size) {
		int byteArrayIndex = 0;
		long[] points = new long[dimension];
		
		int ceil = (int) Math.ceil((1.0*s.length/dimension));
		
		for(int i = 0; i < dimension; i++){
			for(int j = 0; j < ceil; j++){
				points[i] = points[i] << 8;
				points[i] += s[byteArrayIndex];
				byteArrayIndex++;
				
				if(byteArrayIndex >= s.length){
					break;
				}
			}
		}
		
		return new Coordinates(points, dimension, size);
	}

	/**
	 * Gets the random hash passwords.
	 *
	 * @param familyNames The family names.
	 * @param passList The password list.
	 * @param sampleSize The sample size.
	 * @param proportionOfRndPass the proportion of rnd pass
	 * @param rndPassLength The random pass length.
	 * @return the random hash passwords
	 */
	public Coordinates[] getRandomHashPasswords(String[] familyNames, String[] passList, int sampleSize, int proportionOfRndPass, int rndPassLength) {

		if (proportionOfRndPass < 0 || proportionOfRndPass > 100) {
			throw new IllegalArgumentException(
					"proportionOfRndPass must be between 0 and 100.");
		}
		if (sampleSize <= 0) {
			throw new IllegalArgumentException(
					"sampleSize must be greater than 0.");
		}
		if (familyNames == null || passList == null) {
			throw new IllegalArgumentException(
					"familyNames and passList cannot be null or empty.");
		}
		if (rndPassLength < 1) {
			throw new IllegalArgumentException(
					"length must be larger than 0. Current value: "
							+ rndPassLength);
		}
		
		Coordinates[] output = new Coordinates[sampleSize];

		for (int i = 0; i < sampleSize; i++) {
			String name = ((char) ('a' + rnd.nextInt(28))) + familyNames[rnd.nextInt(familyNames.length)];
			String pass;
			
			if (rnd.nextInt(100) < proportionOfRndPass) {
				pass = getRndPassword(rndPassLength);
			} 
			else {
				pass = passList[rnd.nextInt(passList.length)];
			}
		
			byte[] hash = getHashPasswords(name + pass);
			output[i] = this.toCoordinate(hash, hash.length);
		}

		return output;
	}

	/**
	 * Gets the random password.
	 * 
	 * @param length
	 *            the length
	 * @return the random password
	 */
	private String getRndPassword(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(passwordAlphabet[rnd.nextInt(passwordAlphabet.length)]);
		}
		return sb.toString();
	}
}
