package uk.ac.soton.comp2211.group2.view.Utilities;

import java.util.ArrayList;
import java.util.function.Predicate;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlDataFilters;
import uk.ac.soton.comp2211.group2.model.IDLog;

public class ContextPick extends HBox {
    private ArrayList<String> contextDesc = new ArrayList<>();

    private RadioButton age1 = new RadioButton("<25");
    private RadioButton age2 = new RadioButton("25-34");
    private RadioButton age3 = new RadioButton("35-44");
    private RadioButton age4 = new RadioButton("45-54");
    private RadioButton age5 = new RadioButton(">54");
    VBox ageBox = new VBox();
    Label ageName = new Label("AGE:");
    ToggleGroup ageGroup = new ToggleGroup();

    private RadioButton gender1 = new RadioButton("Male");
    private RadioButton gender2 = new RadioButton("Female");
    VBox genderBox = new VBox();
    Label genderName = new Label("GENDER:");
    ToggleGroup genderGroup = new ToggleGroup();

    private RadioButton context1 = new RadioButton("News");
    private RadioButton context2 = new RadioButton("Shopping");
    private RadioButton context3 = new RadioButton("Social Media");
    private RadioButton context4 = new RadioButton("Blog");
    private RadioButton context5 = new RadioButton("Hobbies");
    private RadioButton context6 = new RadioButton("Travel");
    VBox contextBox = new VBox();
    Label contextName = new Label("CONTEXT:");
    ToggleGroup contextGroup = new ToggleGroup();

    private RadioButton income1 = new RadioButton("Low");
    private RadioButton income2 = new RadioButton("Medium");
    private RadioButton income3 = new RadioButton("High");
    VBox incomeBox = new VBox();
    Label incomeName = new Label("INCOME:");
    ToggleGroup incomeGroup = new ToggleGroup();

    HBox options = new HBox();

    public ContextPick() {
        ageName.getStyleClass().add("titleprop");
        ageBox.setSpacing(10);
        ageBox.getChildren().addAll(ageName, age1, age2, age3, age4, age5);
        //ageGroup.getToggles().addAll(age1, age2, age3, age4, age5);

        contextName.getStyleClass().add("titleprop");
        contextBox.setSpacing(10);
        contextBox.getChildren().addAll(contextName, context1, context2, context3, context4, context5, context6);
        //contextGroup.getToggles().addAll(context1, context2, context3, context4, context5, context6);

        incomeName.getStyleClass().add("titleprop");
        incomeBox.setSpacing(10);
        incomeBox.getChildren().addAll(incomeName, income1, income2, income3);
        //incomeGroup.getToggles().addAll(income1, income2, income3);

        genderName.getStyleClass().add("titleprop");
        genderBox.setSpacing(10);
        genderBox.getChildren().addAll(genderName, gender1, gender2);
        //genderGroup.getToggles().addAll(gender1, gender2);

        options.getChildren().addAll(ageBox, contextBox, incomeBox, genderBox);
        options.setSpacing(20);
        options.getStyleClass().add("filter-rectangle");

        getChildren().addAll(options);
    }

    
    /** 
     * gets the selected age range(s)
     * @return String
     */
    public String getAge() {
        return ((RadioButton) ageGroup.getSelectedToggle()).getText();
    }

    
    /** 
     * gets the selected gender(s)
     * @return String
     */
    public String getGender(){
        return ((RadioButton) genderGroup.getSelectedToggle()).getText();
    }

    
    /** 
     * gets the selected context(s)
     * @return String
     */
    public String getContext(){
        return ((RadioButton) contextGroup.getSelectedToggle()).getText();
    }

    
    /** 
     * gets the selected income(s)
     * @return String
     */
    public String getIncome(){
        return ((RadioButton) incomeGroup.getSelectedToggle()).getText();
    }

    
    /** 
     * returns the selected filters as a predicate
     * @return Predicate<IDLog>
     */
    public Predicate<IDLog> getPredicate() {
        ArrayList<Predicate<IDLog>> ageSelected = new ArrayList<Predicate<IDLog>>();
        ArrayList<Predicate<IDLog>> contextSelected = new ArrayList<Predicate<IDLog>>();
        if (age1.isSelected()) {
            ageSelected.add(ControlDataFilters.Age.UNDER_25);
            contextDesc.add("<25, ");
        }if (age2.isSelected()) {
            ageSelected.add(ControlDataFilters.Age.x25_TO_34);
            contextDesc.add("25-34, ");
        }if (age3.isSelected()) {
            ageSelected.add(ControlDataFilters.Age.x35_TO_44);
            contextDesc.add("35-44, ");
        }if (age4.isSelected()) {
            ageSelected.add(ControlDataFilters.Age.x45_TO_54);
            contextDesc.add("45-54, ");
        }if (age5.isSelected()) {
            ageSelected.add(ControlDataFilters.Age.OVER_54);
            contextDesc.add(">54, ");
        }
        Predicate<IDLog> finalAge;
        if (ageSelected.toArray().length > 0) {
            contextDesc.add(contextDesc.size() - ageSelected.size(), "Ages included:");
            contextDesc.add(".");
            finalAge = ControlDataFilters.combineOr(ageSelected.toArray(new Predicate[0]));
        } else {
            finalAge = ControlDataFilters.ANY;
        }
        if (context1.isSelected()) {
            contextSelected.add(ControlDataFilters.Context.NEWS);
            contextDesc.add("News, ");
        }if (context2.isSelected()) {
            contextSelected.add(ControlDataFilters.Context.SHOPPING);
            contextDesc.add("Shopping, ");
        }if (context3.isSelected()) {
            contextSelected.add(ControlDataFilters.Context.SOCIAL_MEDIA);
            contextDesc.add("SocialMedia, ");
        }if (context4.isSelected()) {
            contextSelected.add(ControlDataFilters.Context.BLOG);
            contextDesc.add("Blog, ");
        }if (context5.isSelected()) {
            contextSelected.add(ControlDataFilters.Context.HOBBIES);
            contextDesc.add("Hobbies, ");
        }if (context6.isSelected()) {
            contextSelected.add(ControlDataFilters.Context.TRAVEL);
            contextDesc.add("Travel, ");
        }
        Predicate<IDLog> finalContext;
        if (contextSelected.toArray().length > 0) {
            contextDesc.add(contextDesc.size() - contextSelected.size(), "Context included:");
            contextDesc.add(".");
            finalContext = ControlDataFilters.combineOr(contextSelected.toArray(new Predicate[0]));
        } else {
            finalContext = ControlDataFilters.ANY;
        }

        ArrayList<Predicate<IDLog>> incomeSelected = new ArrayList<Predicate<IDLog>>();
        if (income1.isSelected()) {
            incomeSelected.add(ControlDataFilters.Income.LOW);
            contextDesc.add("Low, ");
        }if (income2.isSelected()) {
            incomeSelected.add(ControlDataFilters.Income.MEDIUM);
            contextDesc.add("Medium, ");
        }if (income3.isSelected()) {
            incomeSelected.add(ControlDataFilters.Income.HIGH);
            contextDesc.add("High, ");
        }
        Predicate<IDLog> finalIncome;
        if (incomeSelected.toArray().length > 0) {
            finalIncome = ControlDataFilters.combineOr(incomeSelected.toArray(new Predicate[0]));
            contextDesc.add(contextDesc.size() - incomeSelected.size(), "Income included:");
            contextDesc.add(".");
        } else {
            finalIncome = ControlDataFilters.ANY;
        }
        ArrayList<Predicate<IDLog>> genderSelected = new ArrayList<Predicate<IDLog>>();
        if (gender1.isSelected()) {
            genderSelected.add(ControlDataFilters.Gender.MALE);
            contextDesc.add("Male, ");
        }if (gender2.isSelected()) {
            genderSelected.add(ControlDataFilters.Gender.FEMALE);
            contextDesc.add("Female, ");
        }
        Predicate<IDLog> finalGender;
        if (genderSelected.toArray().length > 0) {
            finalGender = ControlDataFilters.combineOr(genderSelected.toArray(new Predicate[0]));
            contextDesc.add(contextDesc.size() - genderSelected.size(), "Gender included:");
            contextDesc.add(".");
        } else {
            finalGender = ControlDataFilters.ANY;
        }

        Predicate<IDLog> finalPredicate = ControlDataFilters.combineAnd(finalAge,finalGender,finalContext,finalIncome);

        return  finalPredicate;
    }

    public ArrayList<String> getContextDesc() {
        return contextDesc;
    }
}
