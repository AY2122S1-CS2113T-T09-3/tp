package seedu.situs.command;

import seedu.situs.exceptions.SitusException;
import seedu.situs.ingredients.IngredientList;

import java.io.IOException;

public class SubtractCommand extends Command {

    private static final String STORAGE_ERROR_MESSAGE = "Cannot remove ingredient from memory file!";
    private static final String INVALID_NUMBER = "Ingredient number does not exist!";

    private String ingredientName;
    private Double subtractAmount;

    public SubtractCommand(String ingredientName, Double subtractAmount) {
        this.ingredientName = ingredientName.substring(0, 1).toUpperCase() + ingredientName.substring(1);
        this.subtractAmount = subtractAmount;
    }

    @Override
    public String run() throws SitusException {
        try {
            String resultMsg;
            IngredientList.getInstance()
                    .subtractIngredientFromGroup(ingredientName, subtractAmount);
            resultMsg = "Got it. " + subtractAmount
                    + " kg has been subtracted from " + ingredientName;
            return resultMsg;
        } catch (IndexOutOfBoundsException e) {
            throw new SitusException(INVALID_NUMBER);
        } catch (IOException e) {
            throw new SitusException(STORAGE_ERROR_MESSAGE);
        }
    }
}