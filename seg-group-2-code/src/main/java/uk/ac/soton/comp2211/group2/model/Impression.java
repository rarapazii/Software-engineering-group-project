package uk.ac.soton.comp2211.group2.model;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import uk.ac.soton.comp2211.group2.controller.Age;
import uk.ac.soton.comp2211.group2.controller.Context;
import uk.ac.soton.comp2211.group2.controller.Gender;
import uk.ac.soton.comp2211.group2.controller.Income;

class Impression {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTime date;
    private long id;
    private Gender gender;
    private Age age;
    private Income income;
    private Context context;
    private float impressionCost;

    Impression (String lineOfCsv) throws ParseException{
        String[] fields = lineOfCsv.split(",");
        if (fields.length != 7) {
        	throw new ParseException(lineOfCsv, 0);
        }
        try {
	        this.date = LocalDateTime.parse(fields[0], Impression.DATE_FORMAT);
	        this.id = Long.parseLong(fields[1]);
	        this.impressionCost = Float.parseFloat(fields[6]);
        }
        catch (NumberFormatException | DateTimeException e) {
        	throw new ParseException(lineOfCsv, 0);
        }
        
        if (this.impressionCost < 0) {
        	throw new ParseException(lineOfCsv, 0);
        }
        
        try {
	        this.gender = Gender.valueOf(fields[2].toUpperCase());
	        this.age = Age.fromString(fields[3].toUpperCase());
	        this.income = Income.valueOf(fields[4].toUpperCase());
	        this.context = Context.fromString(fields[5].toUpperCase());
        }
        catch (IllegalArgumentException e) {
        	throw new ParseException(lineOfCsv, 0);
        }
        
        
        
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public long getId() {
        return this.id;
    }

    public float getImpressionCost() {
        return this.impressionCost;
    }

    public Gender getGender() {
        return gender;
    }


    public Age getAge() {
        return age;
    }

    public Income getIncome() {
        return income;
    }

    public Context getContext() {
        return context;
    }

    public String toString(){
//        return String.format("Imp: Date=%s, id=%l, Gender=%s, Age=%s, Income=%s, Context=%s, Cost=%.4f",
//                             this.date.toString(), this.id, this.gender, this.age, this.income, this.context, this.impressionCost);
        return "Imp: Date=" + this.date.toString() + ", ID=" + this.id + ", Gender=" + this.gender +
        ", Age=" + this.age + ", Income=" + this.income + ", Context=" + this.context + ", Cost=" + this.impressionCost;
    }

}
