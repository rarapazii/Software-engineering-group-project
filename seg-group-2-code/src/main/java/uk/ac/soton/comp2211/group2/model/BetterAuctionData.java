package uk.ac.soton.comp2211.group2.model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

import uk.ac.soton.comp2211.group2.controller.Age;
import uk.ac.soton.comp2211.group2.controller.Context;
import uk.ac.soton.comp2211.group2.controller.Gender;
import uk.ac.soton.comp2211.group2.controller.Income;
import uk.ac.soton.comp2211.group2.controller.Pair;
import uk.ac.soton.comp2211.group2.controller.interfaces.NewControlAuctionData;


public class BetterAuctionData implements NewControlAuctionData {

	private String colourScheme = "style/main.css";

	private static class CategoryInfo implements Cloneable{
		
		private int impressions;
		private int clicks;
		private int bounces;
		private int conversions;
		
		private float iCost;
		private float cCost;
		
		private HashSet<Long> ids = new HashSet<Long>();
		
		void merge(CategoryInfo merging, boolean requiresUniques) {
			this.bounces += merging.bounces;
			this.clicks += merging.clicks;
			this.impressions += merging.impressions;
			this.iCost += merging.iCost;
			this.cCost += merging.cCost;
			if (requiresUniques) {
				this.ids.addAll(merging.ids);
			}
		}
		
		CategoryInfo(){
			this(0, 0, 0, 0);
		}
		
		CategoryInfo(int impressions, int clicks, int bounces, int conversions) {
			this.impressions = impressions;
			this.clicks = clicks;
			this.bounces = bounces;
			this.conversions = conversions;
		}

		/**
		 * @return the impressions
		 */
		public int getImpressions() {
			return impressions;
		}

		/**
		 * @return the clicks
		 */
		public int getClicks() {
			return clicks;
		}

		/**
		 * @return the uniques
		 */
		public int getUniques() {
			return ids.size();
		}

		public float getICost() {
			return this.iCost;
		}
		
		public float getCCost() {
			return this.cCost;
		}
		
		public int getBounces() {
			return bounces;
		}
		
		public int getConvs() {
			return conversions;
		}
		
		public void incImpressions() {
			this.impressions++;
		}

		public void incClicks() {
			this.clicks++;
		}

		public boolean addId(long id) {
			return ids.add(id);
		}

		public void incBounces() {
			this.bounces++;
		}
		
		public void incConvs() {
			this.conversions++;
		}
		
		public void addICost(float toAdd) {
			this.iCost += toAdd;
		}
		
		public void addCCost(float toAdd) {
			this.cCost += toAdd;
		}
		
		//Additional getters
		public float getCTR() {
			return ((float) this.clicks) / this.impressions; 
		}
		
		public float getCPA() {
			return this.getTotalCost() / this.conversions;
		}
		
		public float getCPC() {
			return this.getTotalCost() / this.clicks;
		}
		
		public float getCPM() {
			return (1000 * this.getTotalCost()) / this.impressions;
		}
		
		public float getTotalCost() {
			return this.cCost + this.iCost;
		}
	}
	
	private static final Duration GAP = Duration.ofHours(1);
	
	private NavigableSet<LocalDateTime> timesUsed = new TreeSet<LocalDateTime>();
	
	private NavigableMap<CategoryKey, CategoryInfo> data = new TreeMap<CategoryKey, CategoryInfo>();
	
	private int bounceTimeLimit = 5;

	private int pagesViewedBounce = 2;
	
	private CategoryInfo globalInfo = new CategoryInfo();
	
	private Collection<CategoryKey> possibilitiesFor(LocalDateTime at){
		Collection<CategoryKey> cats = new ArrayList<CategoryKey>();
		for (Age a: Age.values()) {
			for (Context c: Context.values()) {
				for (Gender g: Gender.values()) {
					for (Income i: Income.values()) {
						cats.add(new CategoryKey(at, a, g, c, i));
					}
				}
			}
		}
		return cats;
	}
	
	/**
	 * Checks if we need to add more CategoryKeys given a specific time. If so, add them.
	 */
	private boolean addKeysIfNeeded(LocalDateTime when) {
		LocalDateTime lowerThan = timesUsed.lower(when);
		if (lowerThan == null || lowerThan.plus(GAP).isBefore(when)) {
			//We've exceeded the gap and need a whole load of new key-value pairs.
			LocalDateTime addAt;
			if (lowerThan == null) {
				//We have nothing to base this off except what we were given.
				addAt = when;
			}
			else {
				addAt = lowerThan.plus(GAP);
			}
			for (CategoryKey ck : this.possibilitiesFor(addAt)) {
				data.put(ck, new CategoryInfo());
			}
			timesUsed.add(addAt);
			return true; //Changed something.
		}
		else {
			return false; //Changed nothing.
		}
	}
	
	private boolean isBounce(ServerLog s) {
    	if (s.getExitDate() == null){return false;} //Not a bounce
    	else if (s.getPagesViewed() > this.pagesViewedBounce) { return false; }
        else if (s.getEntryDate().plusSeconds(this.bounceTimeLimit).isBefore(s.getExitDate())) {return false;}
    	return true;
    }
	
	BetterAuctionData(List<Impression> imps, List<Click> clicks, List<ServerLog> srvLogs){
		//Get the earliest time in our list
		LocalDateTime start = imps.get(0).getDate();
		if (start.isAfter(clicks.get(0).getDate())) {
			start = clicks.get(0).getDate();
		}
		if (start.isAfter(srvLogs.get(0).getEntryDate())) {
			start = srvLogs.get(0).getEntryDate();
		}
		
		Map<Long, CategoryKey> idInfo = new HashMap<Long, CategoryKey>();
		
		//Do impressions and uniques.
		for (Impression i : imps) {
			addKeysIfNeeded(i.getDate());
			LocalDateTime timeKey = timesUsed.floor(i.getDate());
			CategoryKey ckFor = new CategoryKey(timeKey, i.getAge(), i.getGender(), i.getContext(), i.getIncome());
			//Aside: add this ID to our info log (for use by clicks and server logs)
			idInfo.putIfAbsent(i.getId(), ckFor);
			CategoryInfo inf = data.get(ckFor);
			inf.incImpressions();
			inf.addId(i.getId());
			inf.addICost(i.getImpressionCost());
			//Add to global
			globalInfo.incImpressions();
			globalInfo.addId(i.getId());
			globalInfo.addICost(i.getImpressionCost());
		}
		//Do clicks
		for (Click c : clicks) {
			addKeysIfNeeded(c.getDate());
			LocalDateTime timeKey = timesUsed.floor(c.getDate());
			CategoryKey contextInfo = idInfo.get(c.getId());
			CategoryKey ckFor = new CategoryKey(timeKey, contextInfo.getAge(), contextInfo.getGender(), contextInfo.getContext(), contextInfo.getIncome());
			CategoryInfo inf = data.get(ckFor);
			inf.incClicks();
			inf.addCCost(c.getClickCost());
			//Add to global again
			globalInfo.incClicks();
			globalInfo.addCCost(c.getClickCost());
		}
		//And finally conversions and bounces
		for (ServerLog sl : srvLogs) {
			addKeysIfNeeded(sl.getEntryDate());
			LocalDateTime timeKey = timesUsed.floor(sl.getEntryDate());
			CategoryKey contextInfo = idInfo.get(sl.getId());
			CategoryKey ckFor = new CategoryKey(timeKey, contextInfo.getAge(), contextInfo.getGender(), contextInfo.getContext(), contextInfo.getIncome());
			CategoryInfo inf = data.get(ckFor);
			if (sl.getConversion()) {
				inf.incConvs();
				globalInfo.incConvs();
			}
			if (this.isBounce(sl)) {
				inf.incBounces();
				globalInfo.incBounces();
			}
		}
		//I think we're done, if we got to here without running out of memory we should be fine.
	}

	@Override
	public float getCTR() {
		return globalInfo.getCTR();
	}

	@Override
	public float getCPA() {
		return globalInfo.getCPA();
	}

	@Override
	public float getCPC() {
		return globalInfo.getCPC();
	}

	@Override
	public float getCPM() {
		return globalInfo.getCPM();
	}

	@Override
	public int getBounces() {
		return globalInfo.getBounces();
	}

	@Override
	public int getUniques() {
		return globalInfo.getUniques();
	}

	@Override
	public int getBounceTimeLimit() {
		return this.bounceTimeLimit;
	}

	@Override
	public void setBounceTimeLimit(int btl) {
		this.bounceTimeLimit = btl;
	}

	@Override
	public void setPagesViewedBounceMinimum(int pvbm) {
		this.pagesViewedBounce = pvbm;
	}

	@Override
	public int getPagesViewedBounceMinimum() {
		return this.pagesViewedBounce;
	}

	@Override
	public void setColourScheme(String col) {
		this.colourScheme = col;
	}

	@Override
	public String getColourScheme() {
		return colourScheme;
	}

	private void throwIfInvalidDates(LocalDateTime start, LocalDateTime end) {
    	if (!end.isAfter(start)) {
    		//End is before or identical to start, invalid
    		throw new IllegalArgumentException("Start must be before end!");
    	}
    }
	
	private ArrayList<Entry<CategoryKey, CategoryInfo>> getFittingKeys(Predicate<CategoryKey> check){
		return this.getFittingKeys(check, null, null);
	}
	
	
	private ArrayList<Entry<CategoryKey, CategoryInfo>> getFittingKeys(Predicate<CategoryKey> check, LocalDateTime start, LocalDateTime end){
		if (start == null) {
			//Autofill with the first record
			start = this.data.firstKey().getStart();
		}
		if (end == null) {
			//Autofill with the last record
			end = this.data.lastKey().getStart();
		}
		ArrayList<Entry<CategoryKey, CategoryInfo>> allowedEntries = new ArrayList<>();
		for (Entry<CategoryKey, CategoryInfo> e: data.entrySet()) {
			//Is ordered apparently.
			CategoryKey ck = e.getKey();
			if (ck.getStart().isBefore(start) || ck.getStart().isAfter(end)) {
				//Ignore, outside required time range
				continue;
			}
			else if (!check.test(ck)) {
				//Ignore, failed the predicate
				continue;
			}
			allowedEntries.add(e);
		}
		return allowedEntries;
	}
	
	private NavigableMap<LocalDateTime,CategoryInfo> timeBasedMerge(Duration delta, List<Entry<CategoryKey, CategoryInfo>> cats, boolean copyUniques){
		NavigableMap<LocalDateTime, CategoryInfo> mergedData = new TreeMap<>();
		
		LocalDateTime start = cats.get(0).getKey().getStart();
		LocalDateTime current = start;
		LocalDateTime end = cats.get(cats.size() - 1).getKey().getStart();
		
		//long s = Instant.now().toEpochMilli();
		
		while (current.isBefore(end)) {
			mergedData.put(current, new CategoryInfo());
			current = current.plus(delta);
		}
		
		//long e1 = Instant.now().toEpochMilli();
		
		//ArrayList<Long> its1 = new ArrayList<Long>();
		//ArrayList<Long> its2 = new ArrayList<Long>();
		
		for (Entry<CategoryKey, CategoryInfo> e : cats) {
			//long itStart = Instant.now().toEpochMilli();
			LocalDateTime bin = mergedData.floorKey(e.getKey().getStart());
			//long iM = Instant.now().toEpochMilli();
			mergedData.get(bin).merge(e.getValue(), copyUniques);
			//long itEnd = Instant.now().toEpochMilli();
			//its1.add(iM - itStart);
			//its2.add(itEnd - iM);
		}
		
		//int repeats = its1.size();
		
		//double mean1 = ((double) its1.stream().reduce((a, b) -> {return a + b;}).get()) / repeats;
		//double mean2 = ((double) its2.stream().reduce((a, b) -> {return a + b;}).get()) / repeats;
		
		//System.out.printf("Within Step 2: Step 1: %dms. Step 2: %d iterations, avg. %.5fms/%.5fms per loop.\n", (e1 - s), repeats, mean1, mean2);
		
		return mergedData;
	}
	
	@Override
	public ArrayList<Pair<String, Integer>> getClicksPerTime(Duration delta, Predicate<CategoryKey> check) {
		return this.getClicksPerTime(delta, null, null, check);
	}

	@Override
	public ArrayList<Pair<String, Integer>> getClicksPerTime(Duration delta, LocalDateTime start, LocalDateTime end,
			Predicate<CategoryKey> filter) {
		if (start == null) {
			start = this.data.firstKey().getStart();
		}
		if (end == null) {
			end = this.data.lastKey().getStart();
		}
		
		this.throwIfInvalidDates(start, end);
		
		/*
		 * Brief explanation (since this is how every one of these methods works except click cost distribution).
		 * 1) Get a filtered list of all the CategoryKeys which are within our time and match the predicate.
		 * 2) Merge that list into a smaller map of Time -> CategoryInfo (similar to how the buckets worked in the old auction data)
		 *    NOTE: The boolean value on timeBasedMerge when false tells it to ignore the 'uniques' field of CategoryInfo, which can give a ludicrous speed boost if you don't need that info.
		 * 3) Iterate across that to return the stat we were asked to get.
		 */
		
		//I can't be bothered to type the types out constantly as they're all huge, so it's 'var' here.
		//long startT = Instant.now().toEpochMilli();
		var filterList = this.getFittingKeys(filter, start, end); //Step 1
		//long e1 = Instant.now().toEpochMilli();
		//Current performance drain
		var condensedData = this.timeBasedMerge(delta, filterList, false); //Step 2
		//long e2 = Instant.now().toEpochMilli();
		var returnVal = new ArrayList<Pair<String, Integer>>();
		
		for (var e : condensedData.entrySet()) {
			returnVal.add(new Pair<String, Integer>(e.getKey().toString(), e.getValue().getClicks()));
		}
		//long endT = Instant.now().toEpochMilli();
		
		//System.out.printf("Step 1: %dms, Step 2: %dms, Step 3: %dms, Total: %dms\n", (e1 - startT), (e2 - e1), (endT - e2), (endT - startT));
		
		return returnVal;
	}

	@Override
	public ArrayList<Pair<String, Integer>> getImpressionsPerTime(Duration delta, Predicate<CategoryKey> check) {
		return this.getImpressionsPerTime(delta, null, null, check);
	}

	@Override
	public ArrayList<Pair<String, Integer>> getImpressionsPerTime(Duration delta, LocalDateTime start,
			LocalDateTime end, Predicate<CategoryKey> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pair<String, Integer>> getUniquesPerTime(Duration delta, Predicate<CategoryKey> check) {
		return this.getUniquesPerTime(delta, null, null, check);
	}

	@Override
	public ArrayList<Pair<String, Integer>> getUniquesPerTime(Duration delta, LocalDateTime start, LocalDateTime end,
			Predicate<CategoryKey> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pair<String, Integer>> getBouncesPerTime(Duration delta, Predicate<CategoryKey> check) {
		return this.getUniquesPerTime(delta, null, null, check);
	}

	@Override
	public ArrayList<Pair<String, Integer>> getBouncesPerTime(Duration delta, LocalDateTime start, LocalDateTime end,
			Predicate<CategoryKey> check) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pair<String, Integer>> getConversionsPerTime(Duration delta, Predicate<CategoryKey> check) {
		return this.getConversionsPerTime(delta, null, null, check);
	}

	@Override
	public ArrayList<Pair<String, Integer>> getConversionsPerTime(Duration delta, LocalDateTime start,
			LocalDateTime end, Predicate<CategoryKey> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pair<String, Float>> getTotalCostPerTime(Duration delta, Predicate<CategoryKey> check) {
		return this.getTotalCostPerTime(delta, null, null, check);
	}

	@Override
	public ArrayList<Pair<String, Float>> getTotalCostPerTime(Duration delta, LocalDateTime start, LocalDateTime end,
			Predicate<CategoryKey> check) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pair<String, Float>> getCTRPerTime(Duration delta, Predicate<CategoryKey> check) {
		return this.getCTRPerTime(delta, null, null, check);
	}

	@Override
	public ArrayList<Pair<String, Float>> getCTRPerTime(Duration delta, LocalDateTime start, LocalDateTime end,
			Predicate<CategoryKey> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pair<String, Float>> getCPAPerTime(Duration delta, Predicate<CategoryKey> check) {
		return this.getCPAPerTime(delta, null, null, check);
	}

	@Override
	public ArrayList<Pair<String, Float>> getCPAPerTime(Duration delta, LocalDateTime start, LocalDateTime end,
			Predicate<CategoryKey> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pair<String, Float>> getCPCPerTime(Duration delta, Predicate<CategoryKey> check) {
		return this.getCPCPerTime(delta, null, null, check);
	}

	@Override
	public ArrayList<Pair<String, Float>> getCPCPerTime(Duration delta, LocalDateTime start, LocalDateTime end,
			Predicate<CategoryKey> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pair<String, Float>> getCPMPerTime(Duration delta, Predicate<CategoryKey> check) {
		return this.getCPMPerTime(delta, null, null, check);
	}

	@Override
	public ArrayList<Pair<String, Float>> getCPMPerTime(Duration delta, LocalDateTime start, LocalDateTime end,
			Predicate<CategoryKey> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pair<String, Float>> getBounceRatePerTime(Duration delta, Predicate<CategoryKey> check) {
		return this.getBounceRatePerTime(delta, null, null, check);
	}

	@Override
	public ArrayList<Pair<String, Float>> getBounceRatePerTime(Duration delta, LocalDateTime start, LocalDateTime end,
			Predicate<CategoryKey> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pair<Float, Integer>> getClickCostDistribution(Duration timeDelta, float costDelta,
			Predicate<CategoryKey> check) {
		return this.getClickCostDistribution(timeDelta, costDelta, null, null, check);
	}

	@Override
	public ArrayList<Pair<Float, Integer>> getClickCostDistribution(Duration timeDelta, float costDelta,
			LocalDateTime start, LocalDateTime end, Predicate<CategoryKey> check) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getTotalCost() {
		return globalInfo.getTotalCost();
	}

	@Override
	public int getNumberOfConversions() {
		return globalInfo.getConvs();
	}

	@Override
	public int getNumberOfClicks() {
		return globalInfo.getClicks();
	}

	
}
