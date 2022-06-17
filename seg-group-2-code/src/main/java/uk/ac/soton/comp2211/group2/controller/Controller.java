package uk.ac.soton.comp2211.group2.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.core.config.Configurator;

import uk.ac.soton.comp2211.group2.controller.interfaces.ControlModel;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlView;
import uk.ac.soton.comp2211.group2.model.Model;
import uk.ac.soton.comp2211.group2.view.View;

public class Controller {
	
	private static Logger contLogger = LogManager.getLogger();
	
	private static ControlModel appModel;
	private static ControlView appView;
	
	//What level of logging should the application have? TODO: Move this into actual l4j config files.
	public static final Level ROOT_LOG_LEVEL = Level.ALL;
	
	public static void main(String[] args) {
		//Temporary: should really use a proper properties file.
//		Configurator.setRootLevel(ROOT_LOG_LEVEL);
		
		contLogger.debug("Setting model...");
		appModel = new Model();
		
		contLogger.debug("Setting view...");
		appView = new View();
		
		contLogger.debug("Making view aware of model...");
		appView.attachModel(appModel);
		
		contLogger.debug("Launching program...");
		appView.begin(args);

	}

}
