package uk.ac.soton.comp2211.group2.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlDataFilters;
import uk.ac.soton.comp2211.group2.view.Utilities.TopBar;
import uk.ac.soton.comp2211.group2.view.charts.BChart;

import javax.imageio.ImageIO;

public class SummaryScene extends BaseScene {
    protected StackPane graphPane;
    protected TabPane tabPane = new TabPane();
    protected ObservableList<Tab> tabList;

    /**
     * DASHBOARD SUMMARY SCENE
     *
     * @param mainWindow the Window this will be displayed in
     */
    public SummaryScene(MainWindow mainWindow) {
        super(mainWindow);
        tabPane = initTabPane();
    }

    public SummaryScene(MainWindow mainWindow, ArrayList<Tab> tabs) {
        super(mainWindow);
        tabPane = initTabPane();
        tabPane.getTabs().addAll(tabs);
    }
    @Override
    public ObservableList<String> getStylesheets() {
        return scene.getStylesheets();
    }
    /**
     * Build the layout
     */
    @Override
    public void build() throws FileNotFoundException, ParseException {
        root = new StackPane();
        var dashPane = new StackPane();
        root.setMaxWidth(mainWindow.getWidth());
        root.setMaxHeight(mainWindow.getHeight());
        dashPane.getStyleClass().add("overview-background");
        root.getChildren().add(dashPane);

        var mainPane = new BorderPane();
        dashPane.getChildren().add(mainPane);

        var sideMenu = new SideMenu(mainWindow);
        mainPane.setLeft(sideMenu);

        var centerBox = new VBox();

        TopBar topBar = new TopBar("Campaign Overview", mainWindow);
        graphPane = new StackPane();
        graphPane.setPadding(new Insets(10D, 0D, 0D, 0D));
        graphPane.getChildren().addAll(tabPane);
        centerBox.getChildren().addAll(topBar ,graphPane);
        centerBox.setVgrow(graphPane, Priority.ALWAYS);
        mainPane.setCenter(centerBox);
    }

    /**
     * Initialise the scene
     */
    @Override
    public void initialise() {
        // TEMPORARY EXPERIMENT
        this.getScene().setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.CONTEXT_MENU) {
                App.showHelp();
            }
        });

    }

    private TabPane initTabPane(){
        TabPane tabs = new TabPane();
        try {
            tabs = makeGraph();
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        return tabs;
    }
    /**
     * Overview section
     */
//    public VBox makeOverview() {
//        var overBox = new VBox();
//
//        var headBox = new HBox();
//        overTitle.getStyleClass().add("overview-title");
//        var titleBox = new HBox(overTitle);
//        HBox.setHgrow(titleBox, Priority.ALWAYS);
//
//        String moreURL = SummaryScene.class.getResource("/icons/more.png").toExternalForm();
//        Image moreImage = new Image(moreURL);
//        ImageView moreView = new ImageView(moreImage);
//        // setting the fit height and width of the image view
//        moreView.setFitHeight(30);
//        moreView.setFitWidth(30);
//        // Setting the preserve ratio of the image view
//        moreView.setPreserveRatio(true);
//
//        String helpURL = SummaryScene.class.getResource("/icons/help.png").toExternalForm();
//        Image helpImage = new Image(helpURL);
//        ImageView helpView = new ImageView(helpImage);
//        // setting the fit height and width of the image view
//        helpView.setFitHeight(30);
//        helpView.setFitWidth(30);
//        // Setting the preserve ratio of the image view
//        helpView.setPreserveRatio(true);
//
//        String setURL = SummaryScene.class.getResource("/icons/settings.png").toExternalForm();
//        Image setImage = new Image(setURL);
//        ImageView setView = new ImageView(setImage);
//        // setting the fit height and width of the image view
//        setView.setFitHeight(30);
//        setView.setFitWidth(30);
//        // Setting the preserve ratio of the image view
//        setView.setPreserveRatio(true);
//
//        headBox.getChildren().addAll(titleBox, moreView, helpView, setView);
//        headBox.setSpacing(20D);
//        overBox.getChildren().addAll(headBox);
//        return overBox;
//    }


    /**
     * Time period display bar
     * TODO: add dropdown for time selection
     *
     * @return HBox
     */
    // private HBox timeBar() {
    //     String times[] = {"hours", "days", "weeks", "months", "years"};
    //     TimeBar = new ComboBox(FXCollections
    //             .observableArrayList(times));
    //     var dateStart = new DatePicker();
    //     dateStart.setPrefSize(150, 10);
    //     var dateEnd = new DatePicker();
    //     HBox bar = new HBox();
    //     bar.getChildren().addAll(new Label("Displaying by:"),
    //             TimeBar,  new Label("Start Date"), dateStart, new Label("End Date"), dateEnd);
    //     dateEnd.setPrefSize(150, 10);
    //     bar.setSpacing(5);
    //     bar.getStyleClass().add("time-bar");
    //     bar.setMaxHeight(10);
    //     TimeBar.setOnAction(new EventHandler<ActionEvent>() {
    //         @Override
    //         public void handle(ActionEvent actionEvent) {
    //             var node = tabPane.getTabs().get(0).getContent();
    //                 try {
    //                     mainWindow.getFilterScene().getLChart().filterAndAdd(TimeBar.getValue().toString(),"Integer",mainWindow.getFilterScene().getLChart().getChart().getTitle(),ControlDataFilters.ANY);
    //                 } catch (FileNotFoundException e) {
    //                     e.printStackTrace();
    //                 } catch (ParseException e) {
    //                     e.printStackTrace();
    //                 }
    //             }

    //     });
    //     return bar;

    // }

    /**
     * helper function for overview panel
     */
    public StackPane overviewHelper(String title) {
        var stack = new StackPane();
        var rect = new Rectangle(60D, 40D);
        rect.setArcWidth(10D);
        rect.setArcHeight(10D);
        rect.getStyleClass().add("dark-box");
        var text = new Text(title);
        text.getStyleClass().add("overview");
        stack.getChildren().addAll(rect, text);

        return stack;
    }



    /**
     * Insert graphs
     */
   public TabPane makeGraph() throws FileNotFoundException, ParseException {
        BChart chart = new BChart("Time", "Clicks" , mainWindow);
		chart.filterAndAdd(Duration.ofDays(1), null, null, "Clicks","Integer","Clicks per day",ControlDataFilters.ANY);
		chart.initialize();

        BChart lchart_unfiltered = new BChart("Time", "Impressions", mainWindow);
		lchart_unfiltered.filterAndAdd(Duration.ofDays(1), null, null, "Impressions","Integer", "Impressions per day", ControlDataFilters.ANY);
		lchart_unfiltered.initialize();

     	BChart lChart_Bounces = new BChart("Time", "Bounces", mainWindow);
		lChart_Bounces.filterAndAdd(Duration.ofDays(1), null, null, "Bounces","Integer", "Bounces per day",ControlDataFilters.ANY);
		lChart_Bounces.initialize();

        tabPane.getTabs().addAll(new Tab("Bounce Rate",lChart_Bounces.getChart()), new Tab("Impressions",lchart_unfiltered.getChart()), new Tab("Clicks",chart.getChart()) );
        return tabPane;
    }

    public void saveGraphImage(File saveTo) throws IOException {
        WritableImage writableImage = getGraphImage();
        ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(writableImage, null), "png", saveTo);
    }

    public WritableImage getGraphImage() {
        return tabPane.snapshot(new SnapshotParameters(), null);
    }

    public StackPane getGraphPane() {
        return graphPane;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void addTab(Tab tab) {
        tabPane.getTabs().add(tab);
    }
}
