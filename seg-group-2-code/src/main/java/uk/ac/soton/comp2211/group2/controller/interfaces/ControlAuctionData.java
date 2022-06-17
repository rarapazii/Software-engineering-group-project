package uk.ac.soton.comp2211.group2.controller.interfaces;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Predicate;

import uk.ac.soton.comp2211.group2.controller.Pair;
import uk.ac.soton.comp2211.group2.model.IDLog;


public interface ControlAuctionData {
    public float getCTR();
    public float getCPA();
    public float getCPC();
    public float getCPM();
    public int getBounces();
    public int getUniques();
    public int getBounceTimeLimit();
    public void setBounceTimeLimit(int btl);
    public void setPagesViewedBounceMinimum(int pvbm);
    public int getPagesViewedBounceMinimum();

    //Cumulative methods
    public ArrayList<Pair<String, Integer>> getClicksPerTime(Duration delta, Predicate<IDLog> check);
    public ArrayList<Pair<String, Integer>> getClicksPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter);
    public ArrayList<Pair<String, Integer>> getImpressionsPerTime(Duration delta, Predicate<IDLog> check);
    public ArrayList<Pair<String, Integer>> getImpressionsPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter);
    public ArrayList<Pair<String, Integer>> getUniquesPerTime(Duration delta, Predicate<IDLog> check);
    public ArrayList<Pair<String, Integer>> getUniquesPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter);
    public ArrayList<Pair<String, Integer>> getBouncesPerTime(Duration delta, Predicate<IDLog> check);
    public ArrayList<Pair<String, Integer>> getBouncesPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> check);
    public ArrayList<Pair<String, Integer>> getConversionsPerTime(Duration delta, Predicate<IDLog> check);
    public ArrayList<Pair<String, Integer>> getConversionsPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter);
    public ArrayList<Pair<String, Float>> getTotalCostPerTime(Duration delta, Predicate<IDLog> check);
    public ArrayList<Pair<String, Float>> getTotalCostPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> check);
    public ArrayList<Pair<String, Float>> getCTRPerTime(Duration delta, Predicate<IDLog> check);
    public ArrayList<Pair<String, Float>> getCTRPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter);
    public ArrayList<Pair<String, Float>> getCPAPerTime(Duration delta, Predicate<IDLog> check);
    public ArrayList<Pair<String, Float>> getCPAPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter);
    public ArrayList<Pair<String, Float>> getCPCPerTime(Duration delta, Predicate<IDLog> check);
    public ArrayList<Pair<String, Float>> getCPCPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter);
    public ArrayList<Pair<String, Float>> getCPMPerTime(Duration delta, Predicate<IDLog> check);
    public ArrayList<Pair<String, Float>> getCPMPerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter);
    public ArrayList<Pair<String, Float>> getBounceRatePerTime(Duration delta, Predicate<IDLog> check);
    public ArrayList<Pair<String, Float>> getBounceRatePerTime(Duration delta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> filter);
	public ArrayList<Pair<Float, Integer>> getClickCostDistribution(Duration timeDelta, float costDelta, Predicate<IDLog> check);
	public ArrayList<Pair<Float, Integer>> getClickCostDistribution(Duration timeDelta, float costDelta, LocalDateTime start, LocalDateTime end, Predicate<IDLog> check);
	/**
	 * @return the totalCost
	 */
	public float getTotalCost();
	/**
	 * @return the numberOfConversions
	 */
	public int getNumberOfConversions();
	/**
	 * @return the numberOfClicks
	 */
	public int getNumberOfClicks();

    public void setColourScheme(String colour);
    public String getColourScheme();
}
