package uk.ac.soton.comp2211.group2.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;

import uk.ac.soton.comp2211.group2.controller.Pair;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlAuctionData;

class AuctionData implements ControlAuctionData {

    private List<Click> clicks;
    private List<Impression> impressions;
    private List<ServerLog> serverLogs;
    private HashMap<Long, IDLog> idHashMap = new HashMap<>();
    private int bounceTimeLimit = 5; //seconds
    private int pagesViewedBounceMinimum = 2;

    private int numberOfImpressions;
    private int numberOfClicks;
    private int numberOfUniques;
    private int numberOfConversions;

    private int bounces;
    private float totalCost;

    private String colourScheme = "style/main.css";

    AuctionData(List<Click> clicksList, List<Impression> impressionsList, List<ServerLog> serverLogsList) {
        this.clicks = clicksList;
        this.impressions = impressionsList;
        this.serverLogs = serverLogsList;

        makeIDHashMap();
        this.numberOfImpressions = this.impressions.size();
        this.numberOfClicks = this.clicks.size();
        this.numberOfUniques = this.countUniques(this.serverLogs);
        this.numberOfConversions = this.countConversions();

        this.bounces = this.countBounces();
        this.totalCost = this.countTotalCost();


    }


    private void throwIfInvalidDates(LocalDateTime start, LocalDateTime end) {
    	if (!end.isAfter(start)) {
    		//End is before or identical to start, invalid
    		throw new IllegalArgumentException("Start must be before end!");
    	}
    }
    
    List<Click> getFilteredClicks(Predicate<IDLog> check){
    	if (check == null) {
    		return this.clicks;
    	}
    	else {
    		List<Click> filtered = new ArrayList<Click>();
    		for (Click c: this.clicks) {
    			IDLog idl = this.idHashMap.get(c.getId());
    			if (check.test(idl)) {
    				filtered.add(c);
    			}
    		}
    		return filtered;
    	}
    }
    
    List<Impression> getFilteredImpressions(Predicate<IDLog> check){
    	if (check == null) {
    		return this.impressions;
    	}
    	else {
    		List<Impression> filtered = new ArrayList<Impression>();
    		for (Impression i: this.impressions) {
    			IDLog idl = this.idHashMap.get(i.getId());
    			if (check.test(idl)) {
    				filtered.add(i);
    			}
    		}
    		return filtered;
    	}
    }
    
    List<ServerLog> getFilteredServer(Predicate<IDLog> check){
    	if (check == null) {
    		return this.serverLogs;
    	}
    	else {
    		List<ServerLog> filtered = new ArrayList<ServerLog>();
    		for (ServerLog l: this.serverLogs) {
    			IDLog idl = this.idHashMap.get(l.getId());
    			if (check.test(idl)) {
    				filtered.add(l);
    			}
    		}
    		return filtered;
    	}
    }
    
    private boolean isBounce(ServerLog s) {
    	if (s.getExitDate() == null){return false;} //Not a bounce
        else if (s.getEntryDate().plusSeconds(this.bounceTimeLimit).isBefore(s.getExitDate())) {return false;}
    	return true;
    }
    
    private void makeIDHashMap() {
        for (Impression impression : this.impressions) {
            if (!this.idHashMap.containsKey(impression.getId())) {
                this.idHashMap.put(impression.getId(), new IDLog(impression.getId()));
            }
            this.idHashMap.get(impression.getId()).addImpression(impression);
        }
        for (Click click : this.clicks) {
            this.idHashMap.get(click.getId()).addClick(click);
        }
        for (ServerLog serverLog : this.serverLogs) {
            this.idHashMap.get(serverLog.getId()).addServerLog(serverLog);
        }
    }

    private int countUniques(List<ServerLog> sLog) {
        int count = 0;
        HashSet<Long> ids = new HashSet<Long>();
        for (ServerLog log : sLog) {
            if (!ids.contains(log.getId())) {
                count += 1;
                ids.add(log.getId());
            }
        }
        return count;
    }

    private int countBounces() {
        int count = 0;
        for (ServerLog serverLog : this.serverLogs) {
            //exit comes before the start time plus bounce time limit
            if (this.isBounce(serverLog)) {
            	count++;
            }

        }
        return count;
    }

    private int countConversions() {
        int count = 0;
        for (ServerLog log : this.serverLogs) {
            if (log.getConversion()) {
                count += 1;
            }
        }
        return count;
    }

    public float countTotalCost() {
        float count = 0;
        for (IDLog idLog : idHashMap.values()) {
            count += idLog.getTotalCost();
        }
        return count;
    }

    public HashMap<Long, IDLog> getIDHashMap() {
        return this.idHashMap;
    }

    @Override
    public float getCTR() {
        return (this.numberOfClicks / this.numberOfImpressions);
    }

    @Override
    public float getCPA() {
        return (this.totalCost / this.numberOfConversions);
    }

    @Override
    public float getCPC() {
        return (this.totalCost / this.numberOfClicks);
    }

    @Override
    public float getCPM() {
        return ((this.totalCost * 1000) / this.numberOfImpressions);
    }

    @Override
    public int getBounces() {
        return this.bounces;
    }

    @Override
    public int getUniques() {
        return this.numberOfUniques;
    }

    @Override
    public int getBounceTimeLimit() {
        return this.bounceTimeLimit;
    }

    @Override
    public void setBounceTimeLimit(int btl) {
        //TODO: remove this later
        System.out.println("Preferences saved");
        this.bounceTimeLimit = btl;
    }

    @Override
    public void setPagesViewedBounceMinimum(int pvbm) {
        //TODO: remove this later
        System.out.println("Preferences saved");
        this.pagesViewedBounceMinimum = pvbm;
    }

    @Override
    public int getPagesViewedBounceMinimum() {
        return this.pagesViewedBounceMinimum;
    }

    @Override
    public ArrayList<Pair<String, Integer>> getClicksPerTime(Duration delta, Predicate<IDLog> check){
        return this.getClicksPerTime(delta, this.clicks.get(0).getDate(), this.clicks.get(this.clicks.size() - 1).getDate(), check);
    }

    @Override
    public ArrayList<Pair<String, Integer>> getClicksPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter) {
    	
    	if (start == null) {
    		start = this.clicks.get(0).getDate();
    	}
    	if (end == null) {
    		end = this.clicks.get(this.clicks.size() - 1).getDate();
    	}
    	
    	this.throwIfInvalidDates(start, end);
    	
    	ArrayList<Pair<String, Integer>> clickBins = new ArrayList<>();
        TreeMap<LocalDateTime, Integer> bins = new TreeMap<>();
        
        LocalDateTime current = start;
        while (current.isBefore(end)) {
        	bins.put(current, 0);
        	current = current.plus(delta);
        }
        
        for (Click cl : this.getFilteredClicks(filter)) {
        	if (cl.getDate().isBefore(start)) {
        		continue;
        	}
        	else if (cl.getDate().isAfter(end)) {
        		break;
        	}
        	LocalDateTime bin = bins.floorKey(cl.getDate());
        	if (bin == null) {
        		continue;
        	}
        	bins.put(bin, bins.getOrDefault(bin, 0) + 1);
        }
        
        Entry<LocalDateTime, Integer> e;
        do {
        	e = bins.pollFirstEntry();
        	if (e != null) {
        		clickBins.add(new Pair<String,Integer>(e.getKey().toString(), e.getValue()));
        	}
        } while (e != null); //Definitely redundant but clearer to do it like this
        return clickBins;
    }

    @Override
    public ArrayList<Pair<String, Integer>> getImpressionsPerTime(Duration delta, Predicate<IDLog> check) {
        return this.getImpressionsPerTime(delta, this.impressions.get(0).getDate(), this.impressions.get(this.impressions.size() - 1).getDate(), check);
    }

    @Override
    public ArrayList<Pair<String, Integer>> getImpressionsPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter) {
    	
    	if (start == null) {
    		start = this.impressions.get(0).getDate();
    	}
    	if (end == null) {
    		end = this.impressions.get(this.impressions.size() - 1).getDate();
    	}
    	
    	this.throwIfInvalidDates(start, end);
    	
    	ArrayList<Pair<String, Integer>> iBins = new ArrayList<>();
        TreeMap<LocalDateTime, Integer> bins = new TreeMap<>();
        
        LocalDateTime current = start;
        while (current.isBefore(end)) {
        	bins.put(current, 0);
        	current = current.plus(delta);
        }
        
        for (Impression i : this.getFilteredImpressions(filter)) {
        	if (i.getDate().isBefore(start)) {
        		continue;
        	}
        	else if (i.getDate().isAfter(end)) {
        		break;
        	}
        	LocalDateTime bin = bins.floorKey(i.getDate());
        	if (bin == null) {
        		continue;
        	}
        	bins.put(bin, bins.getOrDefault(bin, 0) + 1);
        }
        
        Entry<LocalDateTime, Integer> e;
        do {
        	e = bins.pollFirstEntry();
        	if (e != null) {
        		iBins.add(new Pair<String,Integer>(e.getKey().toString(), e.getValue()));
        	}
        } while (e != null); //Definitely redundant but clearer to do it like this
        return iBins;
    }

    @Override
    public ArrayList<Pair<String, Integer>> getUniquesPerTime(Duration delta, Predicate<IDLog> check) {
        return this.getUniquesPerTime(delta, this.serverLogs.get(0).getEntryDate(), this.serverLogs.get(this.clicks.size() - 1).getEntryDate(), check);
    }

    
    //TODO: Is this unique in the timeframe or globally. I.e. if we're looking per hour and someone comes in two hours apart, are they counted in both or just the first buckets?
    //Also, do we group by entry date or exit? If exit, what do we do about 'n/a'?
    //Currently does globally unique and entry date.
    @Override
    public ArrayList<Pair<String, Integer>> getUniquesPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter) {
    	
    	if (start == null) {
    		start = this.serverLogs.get(0).getEntryDate();
    	}
    	if (end == null) {
    		end = this.serverLogs.get(this.serverLogs.size() - 1).getEntryDate();
    	}
    	
    	this.throwIfInvalidDates(start, end);
    	
    	ArrayList<Pair<String, Integer>> uBins = new ArrayList<>();
        TreeMap<LocalDateTime, Integer> bins = new TreeMap<>();
        
        Set<Long> seen = new HashSet<Long>();
        
        LocalDateTime current = start;
        while (current.isBefore(end)) {
        	bins.put(current, 0);
        	current = current.plus(delta);
        }
        
        for (ServerLog sl : this.getFilteredServer(filter)) {
        	if (sl.getEntryDate().isBefore(start)) {
        		continue;
        	}
        	else if (sl.getEntryDate().isAfter(end)) {
        		break;
        	}
        	else if (seen.contains(sl.getId())) {
        		continue; //Not unique
        	}
        	else {
        		seen.add(sl.getId());
	        	LocalDateTime bin = bins.floorKey(sl.getEntryDate());
	        	if (bin == null) {
	        		continue;
	        	}
	        	bins.put(bin, bins.getOrDefault(bin, 0) + 1);
        	}
        }
        
        Entry<LocalDateTime, Integer> e;
        do {
        	e = bins.pollFirstEntry();
        	if (e != null) {
        		uBins.add(new Pair<String,Integer>(e.getKey().toString(), e.getValue()));
        	}
        } while (e != null); //Definitely redundant but clearer to do it like this
        return uBins;
    }

    @Override
    public ArrayList<Pair<String, Integer>> getBouncesPerTime(Duration delta, Predicate<IDLog> check) {
        return this.getBouncesPerTime(delta, this.serverLogs.get(0).getEntryDate(), this.serverLogs.get(this.serverLogs.size() - 1).getEntryDate(), check);
    }

    @Override
    public ArrayList<Pair<String, Integer>> getBouncesPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter) {
    	
    	if (start == null) {
    		start = this.serverLogs.get(0).getEntryDate();
    	}
    	if (end == null) {
    		end = this.serverLogs.get(this.serverLogs.size() - 1).getEntryDate();
    	}
    	
    	this.throwIfInvalidDates(start, end);
    	
    	ArrayList<Pair<String, Integer>> bounceBins = new ArrayList<>();
        TreeMap<LocalDateTime, Integer> bins = new TreeMap<>();
        
        LocalDateTime current = start;
        while (current.isBefore(end)) {
        	bins.put(current, 0);
        	current = current.plus(delta);
        }
        
        for (ServerLog sl : this.getFilteredServer(filter)) {
        	if (sl.getEntryDate().isBefore(start)) {
        		continue;
        	}
        	else if (sl.getEntryDate().isAfter(end)) {
        		break;
        	}
        	else if (!this.isBounce(sl)) {
        		continue; //Not a bounce
        	}
        	LocalDateTime bin = bins.floorKey(sl.getEntryDate());
        	if (bin == null) {
        		continue;
        	}
        	bins.put(bin, bins.getOrDefault(bin, 0) + 1);
        }
        
        Entry<LocalDateTime, Integer> e;
        do {
        	e = bins.pollFirstEntry();
        	if (e != null) {
        		bounceBins.add(new Pair<String,Integer>(e.getKey().toString(), e.getValue()));
        	}
        } while (e != null); //Definitely redundant but clearer to do it like this
        return bounceBins;
    }

    @Override
    public ArrayList<Pair<String, Integer>> getConversionsPerTime(Duration delta, Predicate<IDLog> check) {
        return this.getConversionsPerTime(delta, this.serverLogs.get(0).getEntryDate(), this.serverLogs.get(this.serverLogs.size() - 1).getEntryDate(), check);
    }

    @Override
    public ArrayList<Pair<String, Integer>> getConversionsPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter) {
    	
    	if (start == null) {
    		start = this.serverLogs.get(0).getEntryDate();
    	}
    	if (end == null) {
    		end = this.serverLogs.get(this.serverLogs.size() - 1).getEntryDate();
    	}
    	
    	this.throwIfInvalidDates(start, end);
    	
    	ArrayList<Pair<String, Integer>> convBins = new ArrayList<>();
        TreeMap<LocalDateTime, Integer> bins = new TreeMap<>();
        
        LocalDateTime current = start;
        while (current.isBefore(end)) {
        	bins.put(current, 0);
        	current = current.plus(delta);
        }
        
        for (ServerLog sl : this.getFilteredServer(filter)) {
        	if (sl.getEntryDate().isBefore(start)) {
        		continue;
        	}
        	else if (sl.getEntryDate().isAfter(end)) {
        		break;
        	}
        	else if (!sl.getConversion()) {
        		continue; //Not a conversion
        	}
        	LocalDateTime bin = bins.floorKey(sl.getEntryDate());
        	if (bin == null) {
        		continue;
        	}
        	bins.put(bin, bins.getOrDefault(bin, 0) + 1);
        }
        
        Entry<LocalDateTime, Integer> e;
        do {
        	e = bins.pollFirstEntry();
        	if (e != null) {
        		convBins.add(new Pair<String,Integer>(e.getKey().toString(), e.getValue()));
        	}
        } while (e != null); //Definitely redundant but clearer to do it like this
        return convBins;
    }

    //TODO: Make this method cumulative? idk if it should be or not. Also sort the hashmap by date
    @Override
    public ArrayList<Pair<String, Float>> getTotalCostPerTime(Duration delta, Predicate<IDLog> check) {
        return this.getTotalCostPerTime(delta, this.clicks.get(0).getDate(), this.clicks.get(this.clicks.size() - 1).getDate(), check);
    }

    @Override
    public ArrayList<Pair<String, Float>> getTotalCostPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> check) {
        
    	if (start == null) {
    		start = this.clicks.get(0).getDate();
    	}
    	if (end == null) {
    		end = this.clicks.get(this.clicks.size() - 1).getDate();
    	}
    	
    	this.throwIfInvalidDates(start, end);
    	
    	ArrayList<Pair<String, Float>> totalCostBins = new ArrayList<>();
        TreeMap<LocalDateTime, Float> bins = new TreeMap<>();
        
        LocalDateTime current = start;
        while (current.isBefore(end)) {
        	bins.put(current, 0.0f);
        	current = current.plus(delta);
        }
        
        for (Click cl : this.getFilteredClicks(check)) {
        	LocalDateTime bin = bins.floorKey(cl.getDate());
        	if (bin == null) {
        		continue;
        	}
        	bins.put(bin, bins.getOrDefault(bin, 0.0f) + cl.getClickCost());
        }
        
        for (Impression imp : this.getFilteredImpressions(check)) {
        	LocalDateTime bin = bins.floorKey(imp.getDate());
        	if (bin == null) {
        		continue;
        	}
        	bins.put(bin, bins.getOrDefault(bin, 0.0f) + imp.getImpressionCost());
        }
        
        Entry<LocalDateTime, Float> e;
        do {
        	e = bins.pollFirstEntry();
        	if (e != null) {
        		totalCostBins.add(new Pair<String,Float>(e.getKey().toString(), e.getValue()));
        	}
        } while (e != null); //Definitely redundant but clearer to do it like this
        return totalCostBins;
    }

    @Override
    public ArrayList<Pair<String, Float>> getCTRPerTime(Duration delta, Predicate<IDLog> check) {
        return this.getCTRPerTime(delta, this.clicks.get(0).getDate(), this.clicks.get(this.clicks.size() - 1).getDate(), check);
    }

    @Override
    public ArrayList<Pair<String, Float>> getCTRPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter) {
        
    	if (start == null) {
    		start = this.clicks.get(0).getDate();
    	}
    	if (end == null) {
    		end = this.clicks.get(this.clicks.size() - 1).getDate();
    	}
    	
    	this.throwIfInvalidDates(start, end);
    	
    	ArrayList<Pair<String, Float>> ctrBins = new ArrayList<>();
        ArrayList<Pair<String, Integer>> clicksPerTime = getClicksPerTime(delta, start, end, filter);
        ArrayList<Pair<String, Integer>> impressionsPerTime = getImpressionsPerTime(delta, start, end, filter);

        for (int i = 0; i < clicksPerTime.size(); i++) {
        	float denom = clicksPerTime.get(i).getSecond();
        	if (denom == 0) {
        		ctrBins.add(new Pair<String, Float>(clicksPerTime.get(i).getFirst(), 0.0f));
        	}
        	else {
        		ctrBins.add(new Pair<String, Float>(clicksPerTime.get(i).getFirst(), ((float) clicksPerTime.get(i).getSecond()/ (float) impressionsPerTime.get(i).getSecond())));
        	}
        }
        return ctrBins;
    }

    @Override
    public ArrayList<Pair<String, Float>> getCPAPerTime(Duration delta, Predicate<IDLog> check) {
        return this.getCPAPerTime(delta, this.clicks.get(0).getDate(), this.clicks.get(this.clicks.size() - 1).getDate(), check);
    }

    @Override
    public ArrayList<Pair<String, Float>> getCPAPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter) {
        
    	if (start == null) {
    		start = this.clicks.get(0).getDate();
    	}
    	if (end == null) {
    		end = this.clicks.get(this.clicks.size() - 1).getDate();
    	}
    	
    	this.throwIfInvalidDates(start, end);
    	
    	ArrayList<Pair<String, Float>> cpaBins = new ArrayList<>();
        ArrayList<Pair<String, Float>> totalCostPerTime = getTotalCostPerTime(delta, start, end, filter);
        ArrayList<Pair<String, Integer>> conversionsPerTime = getConversionsPerTime(delta, start, end, filter);

        for (int i = 0; i < totalCostPerTime.size(); i++) {
        	if (conversionsPerTime.get(i).getSecond() == 0) {
        		cpaBins.add(new Pair<String, Float>(totalCostPerTime.get(i).getFirst(), 0.0f));
        	}
        	else {
        		cpaBins.add(new Pair<String, Float>(totalCostPerTime.get(i).getFirst(), ((float) totalCostPerTime.get(i).getSecond()/ (float) conversionsPerTime.get(i).getSecond())));
        	}
        }
        return cpaBins;
    }

    @Override
    public ArrayList<Pair<String, Float>> getCPCPerTime(Duration delta, Predicate<IDLog> check){
        return this.getCPCPerTime(delta, this.clicks.get(0).getDate(), this.clicks.get(this.clicks.size() - 1).getDate(), check);
    }

    @Override
    public ArrayList<Pair<String, Float>> getCPCPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter) {
        
    	if (start == null) {
    		start = this.clicks.get(0).getDate();
    	}
    	if (end == null) {
    		end = this.clicks.get(this.clicks.size() - 1).getDate();
    	}
    	
    	this.throwIfInvalidDates(start, end);
    	
    	ArrayList<Pair<String, Float>> cpcBins = new ArrayList<>();
        ArrayList<Pair<String, Float>> totalCostPerTime = getTotalCostPerTime(delta, start, end, filter);
        ArrayList<Pair<String, Integer>> clicksPerTime = getClicksPerTime(delta, start, end, filter);

        for (int i = 0; i < totalCostPerTime.size(); i++) {
        	float denom = clicksPerTime.get(i).getSecond();
        	if (denom == 0) {
        		cpcBins.add(new Pair<String, Float>(totalCostPerTime.get(i).getFirst(), 0.0f));
        	}
        	else {
        		cpcBins.add(new Pair<String, Float>(totalCostPerTime.get(i).getFirst(), ((float) totalCostPerTime.get(i).getSecond()/ denom )));
        	}
        }
        return cpcBins;
    }

    @Override
    public ArrayList<Pair<String, Float>> getCPMPerTime(Duration delta, Predicate<IDLog> check) {
        return this.getCPMPerTime(delta, this.clicks.get(0).getDate(), this.clicks.get(this.clicks.size() - 1).getDate(), check);
    }

    @Override
    public ArrayList<Pair<String, Float>> getCPMPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter) {
        
    	if (start == null) {
    		start = this.clicks.get(0).getDate();
    	}
    	if (end == null) {
    		end = this.clicks.get(this.clicks.size() - 1).getDate();
    	}
    	
    	this.throwIfInvalidDates(start, end);
    	
    	ArrayList<Pair<String, Float>> cpmBins = new ArrayList<>();
        ArrayList<Pair<String, Float>> totalCostPerTime = getTotalCostPerTime(delta, start, end, filter);
        ArrayList<Pair<String, Integer>> impressionsPerTime = getImpressionsPerTime(delta, start, end, filter);

        for (int i = 0; i < totalCostPerTime.size(); i++) {
            cpmBins.add(new Pair<String, Float>(totalCostPerTime.get(i).getFirst(), ((float) 1000 * totalCostPerTime.get(i).getSecond() / (float) impressionsPerTime.get(i).getSecond())));
        }
        return cpmBins;
    }

    @Override
    public ArrayList<Pair<String, Float>> getBounceRatePerTime(Duration delta, Predicate<IDLog> check) {
        return this.getBounceRatePerTime(delta, this.clicks.get(0).getDate(), this.clicks.get(this.clicks.size() - 1).getDate(), check);
    }

    @Override
    public ArrayList<Pair<String, Float>> getBounceRatePerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter) {
        
    	if (start == null) {
    		start = this.clicks.get(0).getDate();
    	}
    	if (end == null) {
    		end = this.clicks.get(this.clicks.size() - 1).getDate();
    	}
    	
    	this.throwIfInvalidDates(start, end);
    	
    	ArrayList<Pair<String, Float>> bounceRateBins = new ArrayList<>();
        ArrayList<Pair<String, Integer>> clicksPerTime = getClicksPerTime(delta, start, end, filter);
        ArrayList<Pair<String, Integer>> bouncesPerTime = getBouncesPerTime(delta, start, end, filter);

        for (int i = 0; i < clicksPerTime.size(); i++) {
        	float denom = clicksPerTime.get(i).getSecond();
        	if (denom == 0) {
        		bounceRateBins.add(new Pair<String, Float>(clicksPerTime.get(i).getFirst(), 0.0f));
        	}
        	else {
        		bounceRateBins.add(new Pair<String, Float>(clicksPerTime.get(i).getFirst(), ((float) bouncesPerTime.get(i).getSecond()/ (float) clicksPerTime.get(i).getSecond())));
        	}
        }
        return bounceRateBins;
    }
    
    @Override
    public ArrayList<Pair<Float, Integer>> getClickCostDistribution(Duration timeDelta, float costDelta, Predicate<IDLog> check){
    	return this.getClickCostDistribution(timeDelta, costDelta, this.clicks.get(0).getDate(), this.clicks.get(this.clicks.size() - 1).getDate(), check);
    }
    
	@Override
    public ArrayList<Pair<Float, Integer>> getClickCostDistribution(Duration timeDelta, float costDelta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> check){
    	
		if (start == null) {
    		start = this.clicks.get(0).getDate();
    	}
    	if (end == null) {
    		end = this.clicks.get(this.clicks.size() - 1).getDate();
    	}
		
		this.throwIfInvalidDates(start, end);
		
		ArrayList<Pair<Float, Integer>> clickDistribution = new ArrayList<Pair<Float, Integer>>();
    	
    	List<Click> clicks = this.getFilteredClicks(check);
    	
    	TreeMap<Float, Integer> bins = new TreeMap<Float, Integer>();
    	
    	for (Click c: clicks) {
    		if (c.getDate().isBefore(start) || c.getDate().isAfter(end)) {
    			continue; //Outside the range we're interested in.
    		}
    		//Figure out which bucket number we end up in (i.e. "how many buckets away from zero is this click")
    		int bucketCount = Math.round(c.getClickCost() / costDelta); 
    		//Calculate the actual key
    		float key = bucketCount * costDelta;
    		//Now add it
    		bins.put(key, bins.getOrDefault(key, 0) + 1);
    	}
    	
    	Entry<Float, Integer> e;
        do {
        	e = bins.pollFirstEntry();
        	if (e != null) {
        		clickDistribution.add(new Pair<Float, Integer>(e.getKey(), e.getValue()));
        	}
        } while (e != null); //Definitely redundant but clearer to do it like this
        return clickDistribution;
    	
    }

	/**
	 * @return the numberOfClicks
	 */
	@Override
	public int getNumberOfClicks() {
		return numberOfClicks;
	}

	/**
	 * @return the numberOfConversions
	 */
	@Override
	public int getNumberOfConversions() {
		return numberOfConversions;
	}

	/**
	 * @return the totalCost
	 */
	@Override
	public float getTotalCost() {
		return totalCost;
	}

    public void setColourScheme(String path){
        colourScheme = path;
    }

    public String getColourScheme(){
        return colourScheme;
    }
    
}
