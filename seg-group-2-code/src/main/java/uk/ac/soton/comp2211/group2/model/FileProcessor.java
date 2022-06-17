package uk.ac.soton.comp2211.group2.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import uk.ac.soton.comp2211.group2.controller.interfaces.ControlFileLoader;

class FileProcessor implements ControlFileLoader{

    CompletableFuture<List<ServerLog>> slfr;
    CompletableFuture<List<Click>> cfr;
    CompletableFuture<List<Impression>> ifr;

    CompletableFuture<AuctionData> calculatedInfo;

    public FileProcessor(){

    }

	@Override
    public void resetCalcs() {
    	calculatedInfo = null;
    }
    
    private synchronized void tryComputation() {
        //If we have all files, attempt to calculate the campaign data.
    	if (calculatedInfo != null && calculatedInfo.isDone()) {
    		//Don't bother, we're already done
    		return;
    	}
    	calculatedInfo = new CompletableFuture<>();
    	try {
    		if (ifr != null && ifr.isDone() && slfr != null && slfr.isDone() && cfr != null && cfr.isDone()) {
    			//We have all the data we need to do our calculations.
    			AuctionData computedData = new AuctionData(cfr.get(), ifr.get(), slfr.get());
    			calculatedInfo.complete(computedData);
    		}
    	}
    	catch (Exception e) {
    		//Something went very wrong, complete exceptionally
    		calculatedInfo.completeExceptionally(e);
    	}
    }

    public void setImpressionLogs(File iLogs){
        Runnable r = () -> {
        	try {
				ifr.complete(new ImpressionFileReader(iLogs).getImpressionList());
			} catch (FileNotFoundException | ParseException e) {
				ifr.completeExceptionally(e);
			}
        };
        ifr = new CompletableFuture<>();
        new Thread(r).start(); //Read the file off-thread and return control to the GUI
    }

    public void setClickLogs(File cLogs){
        Runnable r = () -> {
        	try {
				cfr.complete(new ClickFileReader(cLogs).getClickList());
			} catch (FileNotFoundException | ParseException e) {
				cfr.completeExceptionally(e);
			}
        };
        cfr = new CompletableFuture<>();
        new Thread(r).start(); //Read the file off-thread and return control to the GUI
    }

    public void setServerLogs(File sLogs){
        Runnable r = () -> {
        	try {
				slfr.complete(new ServerLogFileReader(sLogs).getServerLogList());
			} catch (FileNotFoundException | ParseException e) {
				slfr.completeExceptionally(e);
			}
        };
        slfr = new CompletableFuture<>();
        new Thread(r).start(); //Read the file off-thread and return control to the GUI
    }

    @Override
    public AuctionData getData() throws IOException{
        //Sync on the three existing futures
    	try {
    		ifr.join();
    		cfr.join();
    		slfr.join();
    	}
    	catch (NullPointerException e1) {
    		//One or more of them haven't been set, that's an error
    		throw new IllegalStateException("One of the three files was not set", e1);
    	}
    	catch (CompletionException e2) {
    		throw new IOException("One of the files didn't exist or was incorrectly formatted.", e2);
    	}
    	resetCalcs();
    	//Do the computation (which we should already have done but that doesn't really matter, as it'll short circuit if already done).
    	tryComputation();
    	
    	//Wait until we have a value here, then return it
    	
    	try {
			return calculatedInfo.get();
		} catch (InterruptedException | ExecutionException e3) {
			throw new IOException("Data computation failed!.", e3);
		}
    }

	@Override
	public boolean hasFiles() {
		return (ifr != null) && (cfr != null) && (slfr != null);
	}


}
