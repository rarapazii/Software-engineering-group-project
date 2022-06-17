package uk.ac.soton.comp2211.group2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uk.ac.soton.comp2211.group2.controller.interfaces.ControlFileLoader;
import uk.ac.soton.comp2211.group2.controller.interfaces.NewControlAuctionData;
import uk.ac.soton.comp2211.group2.controller.interfaces.NewControlDataFilters;

@Disabled
public class LoadSpeedTest {
	
	/*
	static NewControlAuctionData data;
	
	static long msInitTime;
	
	static Exception e;
	
	//TODO: Load failed on large file.
	@BeforeAll
	static void setup() throws FileNotFoundException, ParseException {
		try {
			Instant start = Instant.now();
			ControlFileLoader cfr = new Model().createFileLoader();
			cfr.setClickLogs(Path.of("data", "click_log.csv").toFile());
	        cfr.setImpressionLogs(Path.of("data", "impression_log.csv").toFile());
	        cfr.setServerLogs(Path.of("data", "server_log.csv").toFile());
	        data = cfr.getDataNewLoaderForce();
	        Instant end = Instant.now();
	        msInitTime = end.toEpochMilli() - start.toEpochMilli();
		}
		catch (Exception exc) {
			e = exc;
		}
	}
	
	@Test
	void checkStartTime() throws Exception {
		if (e != null) {
			throw e;
		}
		System.out.println(String.format("Took %dms to load files.", msInitTime));
		assertTrue(msInitTime <= 10000);
	}
	
	@Test
	void checkFilterTime() throws Exception {
		Instant start = Instant.now();
		data.getClicksPerTime(Duration.ofHours(1), NewControlDataFilters.Gender.FEMALE);
		Instant end = Instant.now();
		long ms = end.toEpochMilli() - start.toEpochMilli();
		System.out.println(String.format("[NEW] Took %dms to get clicks per time.", ms));
	}
	
	@Test
	void checkConversionCount() {
		assertEquals(1, data.getNumberOfConversions());
	}
	
	@Test
	void checkUniquesCount() {
		assertEquals(9, data.getUniques());
	}
	
	@Test
	void checkBounces() {
		assertEquals(1, data.getBounces());
	}
	*/
}
