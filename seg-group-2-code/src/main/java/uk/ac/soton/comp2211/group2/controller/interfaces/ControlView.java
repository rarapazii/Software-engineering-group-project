package uk.ac.soton.comp2211.group2.controller.interfaces;

public interface ControlView {
	public void attachModel(ControlModel cm);
	public void begin(String[] args);
	public ControlModel getModel();
}
