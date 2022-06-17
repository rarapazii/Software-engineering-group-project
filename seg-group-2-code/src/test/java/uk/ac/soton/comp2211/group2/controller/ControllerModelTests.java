package uk.ac.soton.comp2211.group2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import uk.ac.soton.comp2211.group2.controller.interfaces.ControlAuctionData;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlDataFilters;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlFileLoader;
import uk.ac.soton.comp2211.group2.controller.interfaces.NewControlDataFilters;
import uk.ac.soton.comp2211.group2.model.Model;

public class ControllerModelTests {
	
	static ControlAuctionData data;
	
	static long msInitTime;
	
	static Exception e;
	
	//TODO: Load failed on large file.
	@BeforeAll
	static void setup() throws FileNotFoundException, ParseException {
		try {
			Configurator.setRootLevel(Controller.ROOT_LOG_LEVEL);
			Instant start = Instant.now();
			ControlFileLoader cfr = new Model().createFileLoader();
			cfr.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
	        cfr.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
	        cfr.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
	        data = cfr.getData();
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
		LogManager.getLogger().info("Took {}ms to load files.", msInitTime);
		assertTrue(msInitTime <= 10000);
	}
	
	@Test
	void checkFilterTime() throws Exception {
		Instant start = Instant.now();
		data.getClicksPerTime(Duration.ofHours(1), ControlDataFilters.Gender.FEMALE);
		Instant end = Instant.now();
		long ms = end.toEpochMilli() - start.toEpochMilli();
		System.out.println(String.format("[OLD] Took %dms to get clicks per time.", ms));
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
	
}
