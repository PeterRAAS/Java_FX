package lottoFX;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import lotto.model.LottoCombination;
import lotto.ui.BadLimitException;
import utilities.ConsoleGUI;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * @Autor: Peter Raes
 * @Date: 23/10/2018
 * @Project: LottoFX
 * @Purpose: random lotto nummers generator
 */

public class VerifyDialogFXController extends Dialog {

    private static final long serialVersionUID = 1L;
    private Stage thisStage;
    private LottoCombination combination;
    private Collection<LottoCombination> combinations;
    private Controller parent;
    private ArrayList<TextField> textFields;
    private TextArea area;

    @FXML
    private TilePane panel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button generateButton;

    @FXML
    private Button verifyButton;

    @FXML
    private Label infoLabel;

    public void setCombination(LottoCombination combination) {

        this.combination = combination;
    }


    public void setStart(Controller parent, Collection<LottoCombination> combinations) {

        this.parent = parent;
        this.combinations = combinations;
        parent.getInput().fillUp(new ArrayList<LottoCombination>(combinations));
        this.textFields = new ArrayList<TextField>();
         //generate a textfield for each number in the combination
        for (int i = 0; i < parent.getAmountInCombination(); i++) {

            TextField field = new TextField();
            field.setPrefWidth(30);
            panel.getChildren().add(field);
            textFields.add(field);

        }
        area = showNonUniqueCombinations();
        area.setPrefSize(Control.USE_COMPUTED_SIZE,500);
        scrollPane.setContent(area);

        infoLabel.setText(
                "Fill all the fields with a number between "
                        + parent.getMin()
                        + " and "
                        + parent.getMax()
                        + " or press the Generate button");

    }

    @FXML
    void generateButtonPressed(ActionEvent event) {

        // code for the Generate button behaviour
        try {
            combination =
                    new LottoCombination(
                            new Random(),
                            parent.getMin(),
                            parent.getMax(),
                            parent.getAmountInCombination());
        } catch (BadLimitException exc) {
            ConsoleGUI.write(exc.getMessage());
        }
        for (int i = 0; i < parent.getAmountInCombination(); i++) {
            textFields.get(i).setText(
                    Integer.toString(combination.getCombination()[i]));
        }

    }

    @FXML
    void verifyButtonPressed(ActionEvent event) {

        // code for the Verify button behaviour
        try {
            getCombination();
        } catch (NumberFormatException e1) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Fill all the fields with a number between "
                    + parent.getMin()
                    + " and "
                    + parent.getMax()
                    + " or press the Generate button");
            alert.showAndWait();

            return;
        }

        if (checkCombination((ArrayList<LottoCombination>) combinations, combination)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Win");
            alert.setHeaderText(null);
            alert.setContentText("You win!");
            alert.showAndWait();
        }

        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lose");
            alert.setHeaderText(null);
            alert.setContentText("You Lose!");
            alert.showAndWait();
        }

    }

    private void getCombination() throws NumberFormatException {

        Object[] figures = textFields.toArray();

        if(combination == null){
            combination = new LottoCombination();
        }
        for (int i = 0; i < figures.length; i++) {
            combination.setCombination(
                    i,
                    Integer.parseInt(((TextField) figures[i]).getText()));
        }
    }

    private boolean checkCombination(ArrayList <LottoCombination>list, LottoCombination combination) {

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                combination.sort();
                if (list.get(i).equals(combination)) {
                    return true;
                }
            }
        }
        return false;
    }
    public TextArea showNonUniqueCombinations() {

        TextArea area = null;

        if (!parent.getInput().getUniek().isEmpty()) {
            area =
                    new TextArea(
                            parent.getInput().getUniek().size()
                                    + " combinations are not unique!\n\n");
            area.appendText("Combinations\t\t#\n");
            area.appendText("____________\t\t_\n\n");
            LottoCombination actueelElement;
            for (Iterator <LottoCombination> iteration = parent.getInput().getUniek().iterator();
                 iteration.hasNext();
            ) {
                actueelElement = iteration.next();
                area.appendText(
                        actueelElement.toString()
                                + "\t"
                                + ((Integer) parent
                                .getInput()
                                .getCount()
                                .get(
                                        parent.getInput().getUniek().indexOf(
                                                actueelElement)))
                                .intValue()
                                + "\n");
            }
            area.appendText("____________\t\t_\n");
        } else
            area =
                    new TextArea("\nNone of the combinations exists more than ones!");
        return area;
    }

}
