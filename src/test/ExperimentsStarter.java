/*
 * 
 */
package test;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.ConfigUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ExperimentsStarter.
 */
public class ExperimentsStarter {
	/** The Constant logger. */
    private static final Logger logger = Logger.getLogger(ExperimentsStarter.class.getCanonicalName());

	/**
	 * The main method.
	 *
	 * @param args 
	 * 		The arguments.
	 * @throws NoSuchAlgorithmException 
	 * 			The no such algorithm exception.
	 */
    public static void main(String[] args) throws NoSuchAlgorithmException {
    	ConfigUtils.loadProperty();
    	ConfigUtils.configLoggers(ConfigUtils.loadFileName("LoggingProperties"));
        
    	RunSimulation rs = new RunSimulation();
        
    	if (ConfigUtils.loadFileName("InputFile") != null){
            rs.experimentSuit(ConfigUtils.loadFileName("InputFile"));
        }else{
            logger.log(Level.SEVERE,"no filename in the arguments.");
        }
    }
}
