package lottoFX;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Window;
import lotto.db.LottoSerializedFileIn;
import lotto.db.LottoSerializedFileOut;
import lotto.model.LottoCombination;
import lotto.ui.BadLimitException;
import utilities.ConsoleGUI;
import utilities.FileIO;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.stage.FileChooser;

import javax.swing.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 * @Autor: Peter Raes
 * @Date: 23/10/2018
 * @Project: LottoFX
 * @Purpose: random lotto nummers generator
 */

public class Controller extends Window {


    public static final int MAX_COMBINATIE_LIMIET = 10;
    public static final int MIN_COMBINATIE_LIMIET = 4;
    /*
     * constants
     */
    private static final long serialVersionUID = 5357702772370410491L;
    private static final int MAX_BOVEN_LIMIET = 50;
    private static final int MAX_COMBINATIES_LIMIET = 10000;
    private static final int MAX_ONDER_LIMIET = 10;
    private static final int MIN_BOVEN_LIMIET = 15;
    private static final int MIN_COMBINATIES_LIMIET = 20;
    private static final int MIN_ONDER_LIMIET = 1;

    private ArrayList<LottoCombination> list;

    /*
     * variables
     */
    private Random rnd = new Random();
    private int min, max, elementsInCombination, numberOfCombinations;
    private File fileName;
    private LottoSerializedFileIn input;
    private LottoSerializedFileOut output;
    // private Controller controller;

    public ArrayList<LottoCombination> getList() {
        return list;
    }

    public Controller getController() {
        return this;
    }

    @FXML
    private TextArea area;

    @FXML
    private Label minLabel;

    @FXML
    private Button generateButton;

    @FXML
    private TextField minField;

    @FXML
    private Button loadButton;

    @FXML
    private Label MaxLabel;

    @FXML
    private Label elementsInCombinationLabel;

    @FXML
    private TextField numberOfCombinationsField;

    @FXML
    private Button verifyButton;

    @FXML
    private Label numberOfCombinationsLabel;

    @FXML
    private TextField maxField;

    @FXML
    private Button saveButton;

    @FXML
    private TextField elementsInCombinationsField;

    @FXML
    void generateButtonPressed(ActionEvent event) {

        // Write the code that should be performed when
        // the generate button is pressed here
        if (validateData()) {
            area.setText("");

            area.appendText("Generate button pressed\n");
            numberOfCombinations = Integer
                    .parseInt(numberOfCombinationsField.getText());
            max = Integer.parseInt(maxField.getText());
            LottoCombination combination = null;
            list = new ArrayList<LottoCombination>(numberOfCombinations);
            try {
                for (int i = 0; i < numberOfCombinations; i++) {
                    combination = new LottoCombination(rnd, getMin(),
                            getMax(), elementsInCombination);
                    list.add(combination);

                }
            } catch (BadLimitException e) {
                ConsoleGUI.write(e.getMessage());
            }
            Collections.sort(list);
            for (int i = 0; i < numberOfCombinations; i++) {
                area.appendText(list.get(i) + "\n");
            }
            area.appendText("\n" + list.size()
                    + " combinations have been generated!");
        }
        setInput();

    }

    /**
     * @return
     */
    public boolean validateData() {
        if (!testMinField()) {
            errorMessage("Min is a positive figure between "
                            + MIN_ONDER_LIMIET + " en " + MAX_ONDER_LIMIET,
                    "Minimum control");
            minField.setText("");
            minField.requestFocus();
            return false;
        }

        if (!testMaxField()) {
            errorMessage("Max is a positive figure between "
                            + MIN_BOVEN_LIMIET + " en " + MAX_BOVEN_LIMIET,
                    "Maximum control");
            maxField.setText("");
            maxField.requestFocus();
            return false;
        }

        if (!testElementsInCombinationField()) {
            errorMessage("Elements is a positive figure between "
                            + MIN_COMBINATIE_LIMIET + " en " + MAX_COMBINATIE_LIMIET,
                    "Elements control");
            elementsInCombinationsField.setText("");
            elementsInCombinationsField.requestFocus();
            return false;
        }

        if (!testNumbersInCombination()) {
            errorMessage("Combination is a positive figure between "
                            + MIN_COMBINATIES_LIMIET + " en " + MAX_COMBINATIES_LIMIET,
                    "Combinatie control");
            numberOfCombinationsField.setText("");
            numberOfCombinationsField.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * @return
     */
    public boolean testMaxField() {
        String number = maxField.getText();
        try {
            max = Integer.parseInt(number);
            return (max >= MIN_BOVEN_LIMIET) && (max <= MAX_BOVEN_LIMIET);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @return
     */
    public boolean testMinField() {
        String number = minField.getText();
        try {
            min = Integer.parseInt(number);
            return (min >= MIN_ONDER_LIMIET) && (min <= MAX_ONDER_LIMIET);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    /**
     *
     */
    private void setInput() {
        try {

            if (input != null)
                input.close();
            input = new LottoSerializedFileIn();
        } catch (IOException ioException) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid File Name");
            alert.setHeaderText(null);
            alert.setContentText("File does not exist");
            alert.showAndWait();

        }
    } // end method openFile

    private void errorMessage(String text, String titel) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();

    }

    /**
     * @return
     */
    public boolean testElementsInCombinationField() {
        String number = elementsInCombinationsField.getText();
        try {
            elementsInCombination = Integer.parseInt(number);
            return (elementsInCombination >= MIN_COMBINATIE_LIMIET)
                    && (elementsInCombination <= MAX_COMBINATIE_LIMIET);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @return
     */
    public boolean testNumbersInCombination() {
        String number = numberOfCombinationsField.getText();
        try {
            numberOfCombinations = Integer.parseInt(number);
            return (numberOfCombinations >= MIN_COMBINATIES_LIMIET)
                    && (numberOfCombinations <= MAX_COMBINATIES_LIMIET);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    void saveButtonPressed(ActionEvent event) {
        // Write the code that should be performed when
        // the save button is pressed here
        if (listEmpty()) {
            Controller.this
                    .errorMessage(
                            "First load or generate the combinations!",
                            "Combinations");
            return;
        }
        area.setText("");
        area.appendText("\nSave button pressed\n");
        saveSerializedFile(list);
        area.appendText(list.size()
                + " combinations have been written to disk:\n");
    }


    private boolean listEmpty() {
        return list == null;
    }

    // enable user to choose file to open

    /**
     * @param list
     */

    private void saveSerializedFile(ArrayList<LottoCombination> list) {
        // display dialog, so user can choose file

        FileChooser fileChooser = new FileChooser();
        fileName = fileChooser.showOpenDialog(this);

        if (fileName == null)
            return;

        try {
            output = new LottoSerializedFileOut();
            output.setOutput(FileIO.getObjectOutputStream(fileName.getName()));
            output.streamSerializedList(list);
        } catch (NullPointerException e) {
            ConsoleGUI.write(e.getMessage());

        } catch (IOException e) {
            ConsoleGUI.write(e.getMessage());
        } catch (BadLimitException e) {
            ConsoleGUI.write(e.getMessage());
        }

    }

    @FXML
    void loadButtonPressed(ActionEvent event) {

        // Write the code that should be performed when
        // the load button is pressed here
        area.setText("");
        area.appendText("Load button pressed\n");
        openSerializedFile();
        list = getInputStreamList();
        area.appendText("Uw cijfers!\n");
        Iterator<LottoCombination> it = list.iterator();
        Object obj = null;
        while (it.hasNext()) {
            obj = it.next();
            if (!validData())
                fillScreenFields((LottoCombination) obj);
            area.appendText(obj.toString() + "\n");
        }

        area.appendText(list.size()
                + " combinations have been read from disk:\n");

    }

    /**
     * enable user to choose file to open
     */
    private void openSerializedFile() {
        // display dialog, so user can choose file
        FileChooser fileChooser = new FileChooser();
        fileName = fileChooser.showOpenDialog(this);


        // if user clicked Cancel button on dialog, return

        if (fileName == null || fileName.getName().equals("")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid File Name");
            alert.setHeaderText(null);
            alert.setContentText("Invalid File Name");
            alert.showAndWait();
        }

        setInput();

    }

    private ArrayList<LottoCombination> getInputStreamList() {
        ArrayList<LottoCombination> list = null;
        try {
            list = input.getSerializedList(fileName.getName());
        } catch (FileNotFoundException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File Not Found");
            alert.setHeaderText(null);
            alert.setContentText("Error on reading " + fileName.getName());
            alert.showAndWait();

        } catch (IOException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("IO Error");
            alert.setHeaderText(null);
            alert.setContentText("Error on reading " + fileName.getName());
            alert.showAndWait();

        } catch (ClassNotFoundException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Class Not Found");
            alert.setHeaderText(null);
            alert.setContentText("Error on reading " + fileName.getName());
            alert.showAndWait();

        }
        return list;

    }

    /**
     * @return
     */
    public boolean validData() {
        if (!testMinField()) {
            return false;
        }

        if (!testMaxField()) {
            return false;
        }

        if (!testElementsInCombinationField()) {
            return false;
        }

        if (!testNumbersInCombination()) {
            return false;
        }
        return true;
    }

    public int getAmountInCombination() {
        // return amountInCombination;
        return elementsInCombination;
    }

    /**
     * @param obj
     */
    private void fillScreenFields(LottoCombination obj) {
        elementsInCombinationsField.setText(Integer.toString(obj
                .getElementsInCombination()));
        numberOfCombinationsField.setText(Integer.toString(list.size()));
        minField.setText(Integer.toString(obj.getMinimumValue()));
        maxField.setText(Integer.toString(obj.getMaximumValue()));
    }

    @FXML
    void verifyButtonPressed(ActionEvent event) {

        // make sure min, max, numbersInCombination and a valid collection
        // are specified
        validateData();
        if (listEmpty()) {
            Controller.this
                    .errorMessage(
                            "First load or generate the combinations!",
                            "Combinations");
        }

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VerifyDialog.fxml"));
            Parent parent = fxmlLoader.load();
            //VerifyDialogFXController dialogFXController = fxmlLoader.<VerifyDialogFXController>getController();
            VerifyDialogFXController dialogFXController = fxmlLoader.getController();
            dialogFXController.setStart(this, list);
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public LottoSerializedFileIn getInput() {
        return input;
    }
}

