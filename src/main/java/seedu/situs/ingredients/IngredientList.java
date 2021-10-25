package seedu.situs.ingredients;

import seedu.situs.exceptions.SitusException;
import seedu.situs.storage.Storage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/*
 * Represents a list of multiple ingredients (multiple ingredient groups).
 *
 * E.g. (2 ingredient groups)
 * 1. Carrot | Total Amount: 3.5 kg
 *      Amount Left: 1.0 kg | Expiry Date: 04/11/2021
 *      Amount Left: 2.5 kg | Expiry Date: 10/12/2021
 *
 * 2. Radish | Total Amount: 1.0 kg
 *      Amount Left: 1.0 kg | Expiry Date: 18/02/2022
 */

public class IngredientList {

    private static final String INVALID_NUMBER = "Ingredient number does not exist!";
    private static final String NEGATIVE_NUMBER = "Amount updated must be positive!";
    private static final String INVALID_SUBTRACT = "Subtract amount can't be more than total amount!";

    protected static ArrayList<IngredientGroup> ingredientList;
    private static IngredientList instance = null;
    private static Storage storage;

    public IngredientList() throws SitusException {
        try {
            storage = new Storage();
            ingredientList = storage.loadIngredientsFromMemory();
        } catch (IOException e) {
            ingredientList = new ArrayList<>();
            throw new SitusException("Cannot open the memory file!");
        }
    }

    /**
     * Gets instance of ingredient list.
     *
     * @return ingredient list
     */
    public static IngredientList getInstance() throws SitusException {
        if (instance == null) {
            instance = new IngredientList();
        }
        return instance;
    }

    /**
     * Gets size of ingredient list.
     *
     * @return size
     */
    public int getSize() {
        return ingredientList.size();
    }

    /**
     * Gets ingredient list.
     *
     * @return ingredient list
     */
    public static ArrayList<IngredientGroup> getIngredientList() {
        return ingredientList;
    }

    /**
     * Uses ingredient name as key to search if ingredient currently exists in ingredient list.
     *
     * @param ingredientName ingredient to be searched
     * @return true if ingredient already exists, false if otherwise
     */
    public boolean isIngredientInList(String ingredientName) {
        for (int i = 0; i < getSize(); i++) {
            if (ingredientList.get(i).getIngredientGroupName().equals(ingredientName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Uses ingredient name as key to find the index of the corresponding ingredient.
     * in the ingredient list.
     *
     * @param ingredientName name of ingredient
     * @return index of ingredient, -1 if not found
     */
    public int findIngredientIndexInList(String ingredientName) {
        for (int i = 0; i < getSize(); i++) {
            IngredientGroup currentIngredient = ingredientList.get(i);
            if (ingredientName.equals(currentIngredient.getIngredientGroupName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds ingredient to ingredient list.
     *
     * @param ingredient ingredient to be added
     */
    public void add(Ingredient ingredient) throws IOException, IndexOutOfBoundsException {
        String ingredientName = ingredient.getName();
        boolean isRepeatedName = isIngredientInList(ingredientName);

        if (isRepeatedName) { //ingredient already exists, add to current ingredient group
            int ingredientIndex = findIngredientIndexInList(ingredientName);
            ingredientList.get(ingredientIndex).add(ingredient);

        } else { //create new ingredient group
            IngredientGroup newGroup = new IngredientGroup();
            ingredientList.add(newGroup);
            newGroup.setIngredientGroupName(ingredientName);
            newGroup.add(ingredient);
        }
        storage.writeIngredientsToMemory(ingredientList);
    }

    /**
     * Subtracts amount from total ingredient amount
     * @param ingredientName name of ingredient
     * @param subtractAmount amount to be subtracted from total amount
     * @throws SitusException
     * @throws IOException
     */
    public void subtractIngredientFromGroup(String ingredientName, Double subtractAmount) throws
            SitusException, IOException {
        try {
            int i = 0;
            int ingredientIndex = findIngredientIndexInList(ingredientName);
            IngredientGroup currentGroup = getIngredientGroup(ingredientIndex + 2);

            if (currentGroup.getTotalAmount() < subtractAmount) {
                throw new SitusException(INVALID_SUBTRACT);
            }

            currentGroup.subtractFromTotalAmount(subtractAmount);

            while (subtractAmount != 0.0) {
                if (subtractAmount <= currentGroup.get(i + 1).getAmount()) {
                    currentGroup.get(i + 1).setAmount(currentGroup.get(i + 1).getAmount() - subtractAmount);
                    subtractAmount = 0.0;
                } else {
                    subtractAmount -= currentGroup.get(i + 1).getAmount();
                    currentGroup.remove(i + 1);
                }
                i++;
            }
            storage.writeIngredientsToMemory(ingredientList);

        } catch (IndexOutOfBoundsException e) {
            throw new SitusException(INVALID_NUMBER);
        }
    }

    /**
     * Removes an ingredient from ingredient group.
     *
     * @param ingredientName the ingredient name to remove
     * @param expiryDate the expiration date of the removed ingredient
     * @return an ingredient object of the removed ingredient
     * @throws SitusException if the ingredient and/or expiry date are not matched
     * @throws IOException if the removed ingredient cannot be removed from memory
     */
    public Ingredient removeIngredientFromGroup(String ingredientName, LocalDate expiryDate)
            throws SitusException, IOException {
        Ingredient removedIngredient;
        int groupIndexToRemove = findIngredientIndexInList(ingredientName);

        if (groupIndexToRemove < 0) {
            throw new SitusException("Ingredient not found!");
        }

        int ingredientIndexToRemove = getIngredientGroup(groupIndexToRemove + 1)
                .findIngredientIndexByExpiry(expiryDate);

        if (ingredientIndexToRemove < 0) {
            throw new SitusException("No matching ingredient and expiry date found");
        }

        removedIngredient = getIngredientGroup(groupIndexToRemove + 1)
                .remove(ingredientIndexToRemove + 1);
        storage.writeIngredientsToMemory(ingredientList);

        return removedIngredient;
    }

    /**
     * Get ingredient group based on ingredient number (i.e. all duplicates of the same ingredient).
     *
     * @param ingredientNumber ingredient number
     * @return ingredient group
     * @throws SitusException index out of bounds, cannot access
     */
    public IngredientGroup getIngredientGroup(int ingredientNumber) throws SitusException {
        try {
            return ingredientList.get(ingredientNumber - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new SitusException(INVALID_NUMBER);
        }
    }

    /**
     * Get ingredient group based on ingredient name (i.e. all duplicates of the same ingredient).
     *
     * @param updatedIngredient to be updated ingredient
     * @throws SitusException index out of bounds, cannot access
     */
    public boolean update(Ingredient updatedIngredient) throws SitusException {
        try {
            int i;
            boolean expiryIsRepeated = false;
            int ingredientIndex = findIngredientIndexInList(updatedIngredient.getName());
            IngredientGroup currentGroup = getIngredientGroup(ingredientIndex + 1);
            for (i = 0; i < currentGroup.getIngredientGroupSize(); i++) {
                if (updatedIngredient.getExpiry().equals((currentGroup.getIngredientExpiry(i + 1)))) {
                    if (updatedIngredient.getAmount() <= 0) {
                        throw new SitusException(NEGATIVE_NUMBER);
                    }
                    currentGroup.updateTotalAmount(currentGroup.get(i + 1).getAmount(), updatedIngredient.getAmount());
                    (currentGroup.get(i + 1)).setAmount(updatedIngredient.getAmount());
                    expiryIsRepeated = true;
                }
            }
            storage.writeIngredientsToMemory(ingredientList);
            return expiryIsRepeated;
        } catch (IndexOutOfBoundsException | IOException e) {
            throw new SitusException(INVALID_NUMBER);
        }
    }

}
