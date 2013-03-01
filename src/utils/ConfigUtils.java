/*
 * 
 */
package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigUtils.
 */
public class ConfigUtils {

	/** Class ConfigUtils. */ 
	
	/** The Constant logger. */
	public static final Logger logger = Logger.getLogger(ConfigUtils.class
			.getCanonicalName());
	
	/** Properties. */
	public static Properties prop = null;

	/**
	 * Load property.
	 */
	public static void loadProperty() {
		prop = new Properties();
		
		try {
			FileInputStream fis = new FileInputStream("configs/configuration.properties");
			prop.load(fis);
			fis.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Configures the Logger manager with specified configFile properties file.
	 * 
	 * @param configFile
	 *            Path of properties file.
	 * @see LogManager
	 */
	public static void configLoggers(String configFile) {
		try {
			FileInputStream fis = new FileInputStream(configFile);
			LogManager.getLogManager().readConfiguration(fis);
			fis.close();
		} 
		catch (IOException e) {
			logger.log(Level.WARNING, "Logging Properties File is invalid");
		}
	}
	
	/**
	 * Load file name.
	 *
	 * @param keyAtrribute the attribute name of the property file
	 * @return the filename
	 */
	public static String loadFileName(String keyAtrribute) {
		String fileName = prop.getProperty(keyAtrribute);
		return fileName;
	}
	
	
    /**
     * Load strings from a file.
     *
     * @param filename 
     * 		The filename
     * @return the string[]
     */
    public static String[] loadStrings(String filename){
        List<String> strings = new ArrayList<String>();
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader(filename));
            String line = bf.readLine();
            while(line!=null){
                strings.add(line.trim());
                line = bf.readLine();
            }           
        } catch (IOException ex) {
            Logger.getLogger(ConfigUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
        	try{
        		bf.close();
        	}
        	catch(Exception ex){
        		Logger.getLogger(ConfigUtils.class.getName()).log(Level.SEVERE, "Error is file closing", ex);
        	}
        }
        return strings.toArray(new String[0]);
    }
}
