package uk.ac.soton.comp2211.group2.model;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class Click {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTime date;
    private long id;
    private float clickCost;

    Click (String lineOfCsv) throws ParseException{
        String[] fields = lineOfCsv.split(",");
        if (fields.length != 3) {
        	throw new ParseException(lineOfCsv, 0);
        }
        try {
	        this.date = LocalDateTime.parse(fields[0], DATE_FORMAT);
	        this.id = Long.parseLong(fields[1]);
	        this.clickCost = Float.parseFloat(fields[2]);
        }
        catch (DateTimeParseException | NumberFormatException e) {
        	throw new ParseException(lineOfCsv, 0);
        }
        //Check fields
        if (this.clickCost < 0) {
        	throw new ParseException(fields[2], 0);
        }
        
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public long getId() {
        return this.id;
    }

    public float getClickCost() {
        return this.clickCost;
    }

    public String toString(){
//        return String.format("Click: Date=%s, ID=%l, Cost=%.4f", this.date.toString(), this.id, this.clickCost);
        return "Click: Date=" + this.date.toString() + ", ID=" + Long.toString(this.id) + ", Cost=" + Float.toString(this.clickCost);
    }

}
