package uk.ac.soton.comp2211.group2.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ImpressionFileReader {

    List<Impression> impressionList = new ArrayList<>();

    ImpressionFileReader (File file) throws FileNotFoundException, ParseException{
        try (Scanner scanner = new Scanner (file)){
        	if (!scanner.hasNext()) {
        		//Empty file
        		throw new ParseException("File empty", 0);
        		
        	}
            String header = scanner.nextLine();
            if (!header.equals("Date,ID,Gender,Age,Income,Context,Impression Cost")) {
            	throw new ParseException(header, 0);
            }
            while (scanner.hasNextLine()) {
                this.impressionList.add(new Impression(scanner.nextLine()));
            }
        }
        if (this.impressionList.isEmpty()) {
        	throw new ParseException("No data in file", 0);
        }
    }

    public List<Impression> getImpressionList() {
        return this.impressionList;
    }

}
