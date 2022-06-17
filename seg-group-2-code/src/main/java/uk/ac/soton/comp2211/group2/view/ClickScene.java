package uk.ac.soton.comp2211.group2.view;
import javafx.scene.control.*;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlDataFilters;
import uk.ac.soton.comp2211.group2.view.charts.BChart;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

//is this used? what for? where?
public class ClickScene {
    private MainWindow window;

    /**
     * Create a new menu scene
     *
     * @param mainWindow the Window this will be displayed in
     */
    public ClickScene(MainWindow window) {
        ArrayList<Tab> tabList;
        if (window.getFilterScene() != null) {
            tabList = window.getFilterScene().getTabs();
        } else {
            tabList = new ArrayList<>();
        }
        try {
            this.window = window;
            tabList.add(makeGraph());
            window.updateSummaryScene(tabList);
            window.startOverview();
        }catch (Exception e){}

    }


    public Tab makeGraph() throws FileNotFoundException, ParseException {
        BChart clickCost = new BChart("Time", "Frequency",window);
        clickCost.createClickCost("days",1.00f, ControlDataFilters.ANY,"ClickCost Distribution");
        clickCost.initialize();
        var tab = new Tab("ClickCost Distribution",clickCost.getChart());
        return tab;
    }
}
