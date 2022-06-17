package uk.ac.soton.comp2211.group2.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.ac.soton.comp2211.group2.controller.interfaces.ControlModel;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlView;

public class View implements ControlView {

	private static Logger viewLogger = LogManager.getLogger();

	private ControlModel model;

	
	/** connect the view to the control model
	 *
	 * @param cm - the control model
	 */
	@Override
	public void attachModel(ControlModel cm) {
		model = cm;
	}

	
	/** gets the model
	 * @return ControlModel
	 */
	@Override
	public ControlModel getModel() {
		return model;
	}

	
	/** Call the application start method
	 * @param args
	 */
	@Override
	public void begin(String[] args) {
		viewLogger.debug("Starting application...");
		App.main(model, args);
	}

}
