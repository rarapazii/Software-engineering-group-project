package uk.ac.soton.comp2211.group2.model;

import java.util.ArrayList;
import java.util.List;

import uk.ac.soton.comp2211.group2.controller.Age;
import uk.ac.soton.comp2211.group2.controller.Context;
import uk.ac.soton.comp2211.group2.controller.Gender;
import uk.ac.soton.comp2211.group2.controller.Income;

public class IDLog {

    private long id;
    private List<Click> clicks = new ArrayList<Click>();
    private List<ServerLog> serverLogs = new ArrayList<ServerLog>();
    private List<Impression> impressions = new ArrayList<Impression>();

    private Impression primaryImpression; //Where we pull personal info about this person from.

    IDLog(long idNumber) {
        this.id = idNumber;
    }

    void addClick(Click click) {
        this.clicks.add(click);
    }

    void addServerLog(ServerLog serverLog) {
        this.serverLogs.add(serverLog);
    }

    void addImpression(Impression impression) {
        this.impressions.add(impression);
        if (this.primaryImpression == null) {
        	this.primaryImpression = impression;
        }
    }
    
    long getId() {
    	return this.id;
    }

    public String toString() {
        StringBuilder string = new StringBuilder("");
        for (Click click : this.clicks) {
            string.append(click.toString()).append("\n");
        }
        for (ServerLog serverLog : this.serverLogs){
            string.append(serverLog.toString()).append("\n");
        }
        for (Impression impression : this.impressions) {
            string.append(impression.toString()).append("\n");
        }
        return string.toString();
    }

    public float getTotalCost() {
        float count = 0;
        for (Click click : this.clicks) {
            count += click.getClickCost();
        }
        for (Impression impression : this.impressions) {
            count += impression.getImpressionCost();
        }
        return count;
    }

	public Age getAge() {
		return this.primaryImpression.getAge();
	}

	public Gender getGender() {
		return this.primaryImpression.getGender();
	}
	
	public Income getIncome() {
		return this.primaryImpression.getIncome();
	}
	
	public Context getContext() {
		return this.primaryImpression.getContext();
	}
    
}
