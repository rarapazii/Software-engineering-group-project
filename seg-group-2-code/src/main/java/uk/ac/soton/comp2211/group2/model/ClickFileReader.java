package uk.ac.soton.comp2211.group2.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ClickFileReader {

    List<Click> clickList = new ArrayList<>();

    public ClickFileReader(File file) throws FileNotFoundException, ParseException{
        try (Scanner scanner = new Scanner (file)){
        	if (!scanner.hasNext()) {
        		//Empty file
        		throw new ParseException("File empty", 0);
        		
        	}
        	String header = scanner.nextLine();
            if (!header.equals("Date,ID,Click Cost")) {
            	throw new ParseException(header, 0);
            };
            while (scanner.hasNextLine()) {
                this.clickList.add(new Click(scanner.nextLine()));
            }
        }
        
        if (this.clickList.isEmpty()) {
        	throw new ParseException("No data in file", 0);
        }
    }

    public List<Click> getClickList() {
        return this.clickList;
    }

}
