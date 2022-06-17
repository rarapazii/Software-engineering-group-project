package uk.ac.soton.comp2211.group2.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ServerLogFileReader {

    List<ServerLog> serverLogList = new ArrayList<>();

    ServerLogFileReader (File file) throws FileNotFoundException, ParseException {
        try (Scanner scanner = new Scanner (file)){
        	if (!scanner.hasNext()) {
        		//Empty file
        		throw new ParseException("File empty", 0);
        		
        	}
            String header = scanner.nextLine();
            if (!header.equals("Entry Date,ID,Exit Date,Pages Viewed,Conversion")) {
            	throw new ParseException(header, 0);
            }
            while (scanner.hasNextLine()) {
                this.serverLogList.add(new ServerLog(scanner.nextLine()));
            }
        }
        
        if (this.serverLogList.isEmpty()) {
        	throw new ParseException("No data in file", 0);
        }
    }

    public List<ServerLog> getServerLogList() {
        return this.serverLogList;
    }

}
