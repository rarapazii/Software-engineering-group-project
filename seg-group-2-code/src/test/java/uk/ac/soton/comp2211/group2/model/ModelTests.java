package uk.ac.soton.comp2211.group2.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

//import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import uk.ac.soton.comp2211.group2.controller.Age;
import uk.ac.soton.comp2211.group2.controller.Context;
import uk.ac.soton.comp2211.group2.controller.Gender;
import uk.ac.soton.comp2211.group2.controller.Income;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlDataFilters;

public class ModelTests {
    
    @Test
    void checkImpParse() throws Exception{
        String testImp = "2015-01-01 12:00:02,4620864431353617408,Male,25-34,High,Blog,0.001713";
        Impression i = new Impression(testImp);
        //Check contents
        assertEquals(LocalDateTime.parse("2015-01-01 12:00:02", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), i.getDate());
        assertEquals(4620864431353617408L, i.getId());
        assertEquals(Gender.MALE, i.getGender());
        assertEquals(Age.x25_TO_34, i.getAge());
        assertEquals(Income.HIGH, i.getIncome());
        assertEquals(Context.BLOG, i.getContext());
        assertEquals(0.001713, i.getImpressionCost(), 0.000001);
    }

    @Test
    void checkClickParse() throws Exception{
        String testClick = "2015-01-01 12:01:21,8895519749317550080,11.794442";
        Click c = new Click(testClick);
        //Check contents
        assertEquals(LocalDateTime.parse("2015-01-01 12:01:21", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), c.getDate());
        assertEquals(8895519749317550080L, c.getId());
        assertEquals(11.794442, c.getClickCost(), 0.000001);
    }

    @Test
    void checkServerParse() throws Exception{
        String testServerLog = "2015-01-01 12:01:21,8895519749317550080,2015-01-01 12:05:13,7,No";
        ServerLog l = new ServerLog(testServerLog);
        //Check contents
        assertEquals(LocalDateTime.parse("2015-01-01 12:01:21", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), l.getEntryDate());
        assertEquals(8895519749317550080L, l.getId());
        assertEquals(LocalDateTime.parse("2015-01-01 12:05:13", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), l.getExitDate());
        assertEquals(7, l.getPagesViewed());
        assertFalse(l.getConversion());
    }

    @Test
    void checkClickRead() throws IOException, ParseException{
        File cFile = Path.of("data", "click_log_2week.csv").toFile();
        ClickFileReader cfr = new ClickFileReader(cFile);
        var clickList = cfr.getClickList();
        Click first = clickList.get(0);
        Click last = clickList.get(clickList.size() - 1);

        //Check the first: same as above
        assertEquals(LocalDateTime.parse("2015-01-01 12:01:21", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), first.getDate());
        assertEquals(8895519749317550080L, first.getId());
        assertEquals(11.794442, first.getClickCost(), 0.000001);

        //Check the last
        assertEquals(LocalDateTime.parse("2015-01-14 11:59:55", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), last.getDate());
        assertEquals(6752478673601913856L, last.getId());
        assertEquals(0, last.getClickCost(), 0.000001);
    }

    @Test
    void checkImpressionRead() throws IOException, ParseException{
        File iFile = Path.of("data", "impression_log_2week.csv").toFile();
        ImpressionFileReader ifr = new ImpressionFileReader(iFile);
        var impList = ifr.getImpressionList();
        Impression first = impList.get(0);
        Impression last = impList.get(impList.size() - 1);

        //Check the first: same as above
        assertEquals(LocalDateTime.parse("2015-01-01 12:00:02", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), first.getDate());
        assertEquals(4620864431353617408L, first.getId());
        assertEquals(Gender.MALE, first.getGender());
        assertEquals(Age.x25_TO_34, first.getAge());
        assertEquals(Income.HIGH, first.getIncome());
        assertEquals(Context.BLOG, first.getContext());
        assertEquals(0.001713, first.getImpressionCost(), 0.000001);

        //Check the last
        assertEquals(LocalDateTime.parse("2015-01-14 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), last.getDate());
        assertEquals(923607590768080896L, last.getId());
        assertEquals(Gender.FEMALE, last.getGender());
        assertEquals(Age.UNDER_25, last.getAge());
        assertEquals(Income.MEDIUM, last.getIncome());
        assertEquals(Context.BLOG, last.getContext());
        assertEquals(0.002907, last.getImpressionCost(), 0.000001);
    }

    @Test
    void checkServerRead() throws IOException, ParseException{
        File sFile = Path.of("data", "server_log_2week.csv").toFile();
        ServerLogFileReader sfr = new ServerLogFileReader(sFile);
        var sLogList = sfr.getServerLogList();
        ServerLog first = sLogList.get(0);
        ServerLog last = sLogList.get(sLogList.size() - 1);

        //Check the first: same as above
        assertEquals(LocalDateTime.parse("2015-01-01 12:01:21", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), first.getEntryDate());
        assertEquals(8895519749317550080L, first.getId());
        assertEquals(LocalDateTime.parse("2015-01-01 12:05:13", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), first.getExitDate());
        assertEquals(7, first.getPagesViewed());
        assertFalse(first.getConversion());
        //Check the last
        assertEquals(LocalDateTime.parse("2015-01-14 11:59:56", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), last.getEntryDate());
        assertEquals(6752478673601913856L, last.getId());
        assertEquals(LocalDateTime.parse("2015-01-14 12:07:53", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), last.getExitDate());
        assertEquals(6, last.getPagesViewed());
        assertFalse(last.getConversion());
    }

    @Test
    void checkCampaignLoad() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_log_2week.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_log_2week.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_log_2week.csv").toFile());
        assertNotNull(fp.getData());
    }

    @Test
    void checkIDLog() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_log_2week.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_log_2week.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_log_2week.csv").toFile());
//        Configurator.setRootLevel(Level.ALL);
        System.out.println(fp.getData().getIDHashMap().get(8895519749317550080L).toString());
        assertEquals("Click: Date=2015-01-01T12:01:21, ID=8895519749317550080, Cost=11.794442\n" +
                        "SrvLog: EntryDate=2015-01-01T12:01:21, ID=8895519749317550080, Exit=2015-01-01T12:05:13, Pages=7, Conversion=false\n" +
                        "Imp: Date=2015-01-01T12:01:18, ID=8895519749317550080, Gender=FEMALE, Age=x25_TO_34, Income=HIGH, Context=SHOPPING, Cost=0.0\n",
                fp.getData().getIDHashMap().get(8895519749317550080L).toString());
//        LogManager.getLogger().debug("{}", fp.getData().getIDHashMap().get(8895519749317550080L).toString());
    }

    @Test
    void checkTotalCost() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_log_2week.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_log_2week.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_log_2week.csv").toFile());
//        Configurator.setRootLevel(Level.ALL);
        assertEquals(117907.53125, fp.getData().countTotalCost());
    }

    @Test
    void checkGetClicksPerTime() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:21, 2), (2015-01-01T12:02:21, 2), (2015-01-01T12:03:21, 4), (2015-01-01T12:04:21, 1)]",
                Arrays.toString(fp.getData().getClicksPerTime(Duration.ofMinutes(1L), ControlDataFilters.ANY).toArray()));
    }

    @Test
    void checkGetClicksPerTimeWithCustomTimeRanges() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:00, 0), (2015-01-01T12:01, 2), (2015-01-01T12:02, 2), (2015-01-01T12:03, 0), (2015-01-01T12:04, 5)]",
                Arrays.toString(fp.getData().getClicksPerTime(Duration.ofMinutes(1L), LocalDateTime.of(2015, 1, 1, 12, 0),
                        LocalDateTime.of(2015, 1, 1, 12, 5), ControlDataFilters.ANY).toArray()));
    }

    @Test
    void checkTotalCostPerTime() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:21, 23.51591), (2015-01-01T12:02:21, 0.002057), (2015-01-01T12:03:21, 19.170841), (2015-01-01T12:04:21, 0.001928)]",
                Arrays.toString(fp.getData().getTotalCostPerTime(Duration.ofMinutes(1L), ControlDataFilters.ANY).toArray()));
    }
    
    @Test
    void checkImpressionsPerTime() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:18, 3), (2015-01-01T12:02:18, 1), (2015-01-01T12:03:18, 4), (2015-01-01T12:04:18, 1)]",
                Arrays.toString(fp.getData().getImpressionsPerTime(Duration.ofMinutes(1L), ControlDataFilters.ANY).toArray()));
    }
    
    @Test
    void checkUniquesPerTime() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:21, 2), (2015-01-01T12:02:21, 2), (2015-01-01T12:03:21, 4), (2015-01-01T12:04:21, 1)]",
                Arrays.toString(fp.getData().getUniquesPerTime(Duration.ofMinutes(1L), ControlDataFilters.ANY).toArray()));
    }
    
    @Test
    void checkBouncesPerTime() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:21, 0), (2015-01-01T12:02:21, 0), (2015-01-01T12:03:21, 1), (2015-01-01T12:04:21, 0)]",
                Arrays.toString(fp.getData().getBouncesPerTime(Duration.ofMinutes(1L), ControlDataFilters.ANY).toArray()));
    }
    
    @Test
    void checkConversionsPerTime() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:21, 0), (2015-01-01T12:02:21, 0), (2015-01-01T12:03:21, 1), (2015-01-01T12:04:21, 0)]",
                Arrays.toString(fp.getData().getConversionsPerTime(Duration.ofMinutes(1L), ControlDataFilters.ANY).toArray()));
    }
    
    @Test
    void checkCTRPerTime() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:21, 1.0), (2015-01-01T12:02:21, 2.0), (2015-01-01T12:03:21, 1.0), (2015-01-01T12:04:21, 1.0)]",
                Arrays.toString(fp.getData().getCTRPerTime(Duration.ofMinutes(1L), ControlDataFilters.ANY).toArray()));
    }
    
    @Test
    void checkCPAPerTime() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:21, 0.0), (2015-01-01T12:02:21, 0.0), (2015-01-01T12:03:21, 19.170841), (2015-01-01T12:04:21, 0.0)]",
                Arrays.toString(fp.getData().getCPAPerTime(Duration.ofMinutes(1L), ControlDataFilters.ANY).toArray()));
    }
    
    @Test
    void checkCPCPerTime() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:21, 11.757955), (2015-01-01T12:02:21, 0.0010285), (2015-01-01T12:03:21, 4.7927103), (2015-01-01T12:04:21, 0.001928)]",
                Arrays.toString(fp.getData().getCPCPerTime(Duration.ofMinutes(1L), ControlDataFilters.ANY).toArray()));
    }
    
    @Test
    void checkCPMPerTime() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:21, 11757.955), (2015-01-01T12:02:21, 2.0570002), (2015-01-01T12:03:21, 4792.7104), (2015-01-01T12:04:21, 1.928)]",
                Arrays.toString(fp.getData().getCPMPerTime(Duration.ofMinutes(1L), ControlDataFilters.ANY).toArray()));
    }
    
    @Test
    void checkBouncRatePerTime() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:21, 0.0), (2015-01-01T12:02:21, 0.0), (2015-01-01T12:03:21, 0.25), (2015-01-01T12:04:21, 0.0)]",
                Arrays.toString(fp.getData().getBounceRatePerTime(Duration.ofMinutes(1L), ControlDataFilters.ANY).toArray()));
    }
    
    @Test
    void checkImpressionsPerTimeAgeFiltered() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:18, 2), (2015-01-01T12:02:18, 1), (2015-01-01T12:03:18, 2), (2015-01-01T12:04:18, 0)]",
                Arrays.toString(fp.getData().getImpressionsPerTime(Duration.ofMinutes(1L), ControlDataFilters.Age.x25_TO_34).toArray()));
    }
    
    @Test
    void checkImpressionsPerTimeGenderFiltered() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:18, 0), (2015-01-01T12:02:18, 0), (2015-01-01T12:03:18, 2), (2015-01-01T12:04:18, 0)]",
                Arrays.toString(fp.getData().getImpressionsPerTime(Duration.ofMinutes(1L), ControlDataFilters.Gender.MALE).toArray()));
    }
    
    @Test
    void checkImpressionsPerTimeIncomeFiltered() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:18, 2), (2015-01-01T12:02:18, 0), (2015-01-01T12:03:18, 2), (2015-01-01T12:04:18, 1)]",
                Arrays.toString(fp.getData().getImpressionsPerTime(Duration.ofMinutes(1L), ControlDataFilters.Income.MEDIUM).toArray()));
    }
    
    @Test
    void checkImpressionsPerTimeContextFiltered() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:18, 0), (2015-01-01T12:02:18, 1), (2015-01-01T12:03:18, 2), (2015-01-01T12:04:18, 0)]",
                Arrays.toString(fp.getData().getImpressionsPerTime(Duration.ofMinutes(1L), ControlDataFilters.Context.SOCIAL_MEDIA).toArray()));
    }
    
    @Test
    void checkImpressionsPerTimeAndFiltered() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:18, 1), (2015-01-01T12:02:18, 0), (2015-01-01T12:03:18, 0), (2015-01-01T12:04:18, 0)]",
                Arrays.toString(fp.getData().getImpressionsPerTime(Duration.ofMinutes(1L), ControlDataFilters.combineAnd(ControlDataFilters.Gender.FEMALE, ControlDataFilters.Income.HIGH)).toArray()));
    }
    
    @Test
    void checkImpressionsPerTimeOrFiltered() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:18, 3), (2015-01-01T12:02:18, 1), (2015-01-01T12:03:18, 3), (2015-01-01T12:04:18, 1)]",
                Arrays.toString(fp.getData().getImpressionsPerTime(Duration.ofMinutes(1L), ControlDataFilters.combineOr(ControlDataFilters.Gender.FEMALE, ControlDataFilters.Income.HIGH)).toArray()));
    }
    
    @Test
    void checkImpressionsPerTimeNegFiltered() throws IOException {
        FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:18, 2), (2015-01-01T12:02:18, 1), (2015-01-01T12:03:18, 3), (2015-01-01T12:04:18, 1)]",
                Arrays.toString(fp.getData().getImpressionsPerTime(Duration.ofMinutes(1L), ControlDataFilters.negate(ControlDataFilters.Income.HIGH)).toArray()));
    }
    
    @Test
    void checkImpressionsDateRangeEndBeforeStart() throws IOException {
    	FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertThrows(IllegalArgumentException.class, () -> {
        	fp.getData().getClicksPerTime(Duration.ofMinutes(1L), LocalDateTime.of(2015, 1, 1, 12, 20), LocalDateTime.of(2015, 1, 1, 12, 1), ControlDataFilters.ANY);
        });
                       
    }
    
    @Test
    void checkImpressionsDateStartEqualsEnd() throws IOException {
    	FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertThrows(IllegalArgumentException.class, () -> {
        	fp.getData().getClicksPerTime(Duration.ofMinutes(1L), LocalDateTime.of(2015, 1, 1, 12, 20), LocalDateTime.of(2015, 1, 1, 12, 20), ControlDataFilters.ANY);
        });
                       
    }
    
    @Test
    void checkImpressionsDateNullStart() throws IOException {
    	FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:18, 3), (2015-01-01T12:02:18, 1), (2015-01-01T12:03:18, 4), (2015-01-01T12:04:18, 1)]",
                Arrays.toString(fp.getData().getImpressionsPerTime(Duration.ofMinutes(1L), null, LocalDateTime.of(2015, 1, 1, 12, 5), ControlDataFilters.ANY).toArray()));
                       
    }
    
    @Test
    void checkImpressionsDateNullEnd() throws IOException {
    	FileProcessor fp = new FileProcessor();
        fp.setClickLogs(Path.of("data", "click_tiny.csv").toFile());
        fp.setImpressionLogs(Path.of("data", "impression_tiny.csv").toFile());
        fp.setServerLogs(Path.of("data", "server_tiny.csv").toFile());
        assertEquals("[(2015-01-01T12:01:18, 3), (2015-01-01T12:02:18, 1), (2015-01-01T12:03:18, 4), (2015-01-01T12:04:18, 1)]",
                Arrays.toString(fp.getData().getImpressionsPerTime(Duration.ofMinutes(1L), LocalDateTime.of(2015, 1, 1, 12, 1, 18), null, ControlDataFilters.ANY).toArray()));
                       
    }
    
    @Test
    void checkInvalidClickRead() throws IOException{
        File cFile = Path.of("data", "server_log_2week.csv").toFile();
        assertThrows(ParseException.class, () -> { 
        	new ClickFileReader(cFile);
        });
    }
    
    @Test
    void checkInvalidServerRead() throws IOException{
        File cFile = Path.of("data", "impression_log_2week.csv").toFile();
        assertThrows(ParseException.class, () -> { 
        	new ServerLogFileReader(cFile);
        });
    }
    
    @Test
    void checkInvalidImpressionRead() throws IOException{
        File cFile = Path.of("data", "click_log_2week.csv").toFile();
        assertThrows(ParseException.class, () -> { 
        	new ImpressionFileReader(cFile);
        });
    }
    
    static Collection<Arguments> invalidClickSrc(){
    	List<Arguments> args = new ArrayList<Arguments>();
    	for (String fName : new File("data").list()) {
    		if (fName.contains("click") && fName.contains("invalid")) {
    			args.add(Arguments.of(Path.of("data", fName).toFile()));
    		}
    	}
    	return args;
    }
    
    static Collection<Arguments> invalidImpSrc(){
    	List<Arguments> args = new ArrayList<Arguments>();
    	for (String fName : new File("data").list()) {
    		if (fName.contains("impression") && fName.contains("invalid")) {
    			args.add(Arguments.of(Path.of("data", fName).toFile()));
    		}
    	}
    	return args;
    }
    
    static Collection<Arguments> invalidSrvSrc(){
    	List<Arguments> args = new ArrayList<Arguments>();
    	for (String fName : new File("data").list()) {
    		if (fName.contains("server") && fName.contains("invalid")) {
    			args.add(Arguments.of(Path.of("data", fName).toFile()));
    		}
    	}
    	return args;
    }
    
    @ParameterizedTest
    @MethodSource("invalidClickSrc")
    void checkInvalidClickLogs(File src) throws IOException {
    	assertThrows(ParseException.class, () -> { 
        	new ClickFileReader(src);
        });
    }
    
    @ParameterizedTest
    @MethodSource("invalidImpSrc")
    void checkInvalidImpLogs(File src) throws IOException {
    	assertThrows(ParseException.class, () -> { 
        	new ImpressionFileReader(src);
        });
    }
    
    @ParameterizedTest
    @MethodSource("invalidSrvSrc")
    void checkInvalidSrvLogs(File src) throws IOException {
    	assertThrows(ParseException.class, () -> { 
        	new ServerLogFileReader(src);
        });
    }
    
}
