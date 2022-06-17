package uk.ac.soton.comp2211.group2.model;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ServerLog {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTime entryDate;
    private long id;
    private LocalDateTime exitDate;
    private int pagesViewed;
    private boolean conversion;

    ServerLog (String lineOfCsv) throws ParseException {
        String[] fields = lineOfCsv.split(",");
        if (fields.length != 5) {
        	throw new ParseException(lineOfCsv, 0);
        }
        try {
	        this.entryDate = LocalDateTime.parse(fields[0], DATE_FORMAT);
	        this.id = Long.parseLong(fields[1]);
	        if (fields[2].equalsIgnoreCase("n/a")){
	            this.exitDate = null;
	        }
	        else {
	            this.exitDate = LocalDateTime.parse(fields[2], DATE_FORMAT);
	        }
	        this.pagesViewed = Integer.parseInt(fields[3]);
        }
        catch (NumberFormatException | DateTimeException e) {
        	throw new ParseException(lineOfCsv, 0);
        }
        switch (fields[4].toLowerCase()){
            case "yes":
                this.conversion = true;
                break;
            case "no":
                this.conversion = false;
                break;
            default:
                throw new ParseException(fields[4], 0);
        }
        //Field validation.
        if (this.pagesViewed < 1) {
        	throw new ParseException(fields[3], 0);
        }
        if (this.exitDate != null && this.entryDate.isAfter(exitDate)) {
        	throw new ParseException(lineOfCsv, 0);
        }
    }

    public LocalDateTime getEntryDate() {
        return this.entryDate;
    }

    public long getId() {
        return this.id;
    }

    public LocalDateTime getExitDate() {
        return this.exitDate;
    }

    public int getPagesViewed() {
        return this.pagesViewed;
    }

    public boolean getConversion() {
        return this.conversion;
    }

    public String toString(){
//        return String.format("SrvLog: EntryDate=%s, ID=%l, Exit=%s, Pages=%i, Conversion=%b",
//                            this.entryDate.toString(), this.id, (this.exitDate != null) ? this.exitDate.toString() : "n/a",
//                            this.pagesViewed, this.conversion);
        return "SrvLog: EntryDate=" + this.entryDate + ", ID=" + this.id + ", Exit=" + ((this.exitDate != null) ? this.exitDate.toString() : "n/a") +
                ", Pages=" + this.pagesViewed  + ", Conversion=" + this.conversion;
    }
}
