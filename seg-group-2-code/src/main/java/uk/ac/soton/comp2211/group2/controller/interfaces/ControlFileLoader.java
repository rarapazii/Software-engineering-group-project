package uk.ac.soton.comp2211.group2.controller.interfaces;

import java.io.File;
import java.io.IOException;

public interface ControlFileLoader {
	public void setClickLogs(File f);
	public void setImpressionLogs(File f);
	public void setServerLogs(File f);
	
	public ControlAuctionData getData() throws IOException;
	public void resetCalcs();
	public boolean hasFiles();
}
