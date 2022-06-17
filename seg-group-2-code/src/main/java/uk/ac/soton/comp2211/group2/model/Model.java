package uk.ac.soton.comp2211.group2.model;

import uk.ac.soton.comp2211.group2.controller.interfaces.ControlFileLoader;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlModel;

public class Model implements ControlModel {

	@Override
	public ControlFileLoader createFileLoader() {
		return new FileProcessor();
	}

}
