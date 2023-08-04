package edu.miracostacollege.cs112.ic15_nobelpeaceprize.view;


import edu.miracostacollege.cs112.ic15_nobelpeaceprize.controller.Controller;
import edu.miracostacollege.cs112.ic15_nobelpeaceprize.model.NobelLaureate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * The <code>MainScene</code> represents the very first scene for the Nobel Peace Prize application.

 * The <code>MainScene</code> also allows for a user to add a new laureate or remove existing entries.
 */
public class MainScene extends Scene {
    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;

    private ImageView laureateIV = new ImageView();
    private ComboBox<String> laureateTypeCB = new ComboBox<>();
    private TextField nameTF = new TextField();
    private Label nameLabel = new Label("Individual's Name:");
    private Label nameErrLabel = new Label("Name is required.");

    private TextField countryTF = new TextField();
    private Label countryErrLabel = new Label("Country is required.");

    private TextField yearTF = new TextField();
    private Label yearErrLabel = new Label("Year is required.");

    private TextField prizeAmountTF = new TextField();
    private Label prizeAmountErrLabel = new Label("Prize amount is required.");

    private TextField motivationTF = new TextField();
    private Label motivationErrLabel = new Label("Motivation is required :)");

    private ListView<NobelLaureate> laureatesLV = new ListView<>();

    private Button removeButton = new Button("- Remove Laureate");
    private Button addButton = new Button("+ Add Laureate");

    private Controller controller = Controller.getInstance();
    private ObservableList<NobelLaureate> laureatesList;
    private NobelLaureate selectedLaureate;

    /**
     * Constructs a new <code>MainScene</code>, representing the very first scene for the Nobel Peace Price Laureates application.
     * <p>
     * The <code>MainScene</code> also allows for a user to add a new laureate or remove an existing one.
     */
    public MainScene() {
        super(new GridPane(), WIDTH, HEIGHT);

        GridPane pane = new GridPane();
        pane.setHgap(10.0);
        pane.setVgap(5);
        pane.setPadding(new Insets(5));

        // TODO: Uncomment after adding image to resources folder
        laureateIV.setImage(new Image("nobel_peace_laureates.png"));
        laureateIV.setFitWidth(WIDTH);
        pane.add(laureateIV, 0, 0, 3, 1);

        pane.add(new Label("Laureate Type:"), 0, 1);
        pane.add(laureateTypeCB, 1, 1);
        //Add the options for "Individual" and "Organization" into combo box
        laureateTypeCB.getItems().addAll("Individual", "Organization");

        laureateTypeCB.getSelectionModel().select(0);

        pane.add(nameLabel, 0, 2);
        pane.add(nameTF, 1, 2);
        pane.add(nameErrLabel, 2, 2);
        nameErrLabel.setTextFill(Color.RED);
        nameErrLabel.setVisible(false);

        pane.add(new Label("Award Year:"), 0, 3);
        pane.add(yearTF, 1, 3);
        pane.add(yearErrLabel, 2, 3);
        yearErrLabel.setTextFill(Color.RED);
        yearErrLabel.setVisible(false);

        pane.add(new Label("Motivation:"), 0, 4);
        pane.add(motivationTF, 1, 4);
        pane.add(motivationErrLabel, 2, 4);
        motivationErrLabel.setTextFill(Color.RED);
        motivationErrLabel.setVisible(false);

        pane.add(new Label("Country:"), 0, 5);
        pane.add(countryTF, 1, 5);
        pane.add(countryErrLabel, 2, 5);
        countryErrLabel.setTextFill(Color.RED);
        countryErrLabel.setVisible(false);


        pane.add(new Label("Prize Amount $"), 0, 6);
        pane.add(prizeAmountTF, 1, 6);
        pane.add(prizeAmountErrLabel, 2, 6);
        prizeAmountErrLabel.setTextFill(Color.RED);
        prizeAmountErrLabel.setVisible(false);


        pane.add(addButton, 1, 7);
        //Wire this to addLaureate method
        addButton.setOnAction(e -> addLaureate());

        //Wire up the laureates list view
        laureatesLV.getSelectionModel().selectedItemProperty().addListener((obsVal, oldVal, newVal) -> selectLaureate(newVal)) ;
        
        laureatesLV.setPrefWidth(WIDTH);
        pane.add(laureatesLV, 0, 8, 3, 1);
        pane.add(removeButton, 0, 9);

        //Wire up the remove button to removeLaureate method
        removeButton.setOnAction(e -> removeLaureate());

        // TODO: Uncomment when Controller.java is complete
        laureatesList = controller.getAllLaureates();
        laureatesLV.setItems(laureatesList);

        removeButton.setDisable(true);

        this.setRoot(pane);
    }

    private void selectLaureate(NobelLaureate newVal) {
        selectedLaureate = newVal;
        removeButton.setDisable(selectedLaureate == null);
    }

    /**
     * Allows the user to remove an existing laureate
     * However, if the selected laureate is null, just return (do nothing)
     */
    private void removeLaureate() {
        if (selectedLaureate == null)
            return;
        laureatesList.remove(selectedLaureate);
        laureatesLV.getSelectionModel().select(-1);
    }

    /**
     * Allows the user to add a new laureate, checking user input for omissions (or errors)
     */
    private void addLaureate() {
        String name, motivation, country;
        int year = 0;
        double prize = 0;

        name = nameTF.getText();
        nameErrLabel.setVisible(name.isEmpty());

        try {
            year = Integer.parseInt(yearTF.getText());
            yearErrLabel.setVisible(year < 1901);
        } catch (NumberFormatException e) {
            yearErrLabel.setVisible(true);
        }

        motivation = motivationTF.getText();
        motivationErrLabel.setVisible(motivation.isEmpty());

        country = countryTF.getText();
        countryErrLabel.setVisible(country.isEmpty());

        try {
            prize = Double.parseDouble(prizeAmountTF.getText());
            prizeAmountErrLabel.setVisible(prize < 0);
        } catch (NumberFormatException e) {
            prizeAmountErrLabel.setVisible(true);
        }
        
        //If there are no error labels visible, create a new NobelLaureate and add to list
        if (yearErrLabel.isVisible() || nameErrLabel.isVisible() || motivationErrLabel.isVisible()
        || countryErrLabel.isVisible() || prizeAmountErrLabel.isVisible())
            return;
        
        laureatesList.add(new NobelLaureate(name, year, motivation, country, prize));
        FXCollections.sort(laureatesList); //Sorts in specified order
    }
}
