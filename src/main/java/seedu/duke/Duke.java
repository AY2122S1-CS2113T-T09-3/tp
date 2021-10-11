package seedu.duke;

import seedu.duke.exceptions.DukeException;
import seedu.duke.exceptions.InsufficientParametersException;
import seedu.duke.exceptions.UnknownCommandException;
import seedu.duke.ingredients.Ingredient;
import seedu.duke.ingredients.IngredientList;
import seedu.duke.ui.UI;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Duke {

    private static UI ui;
    private static final Logger LOGGER = Logger.getLogger(Duke.class.getName());

    /**
     * Starts up the system by creating the UI.
     */
    public static void initialize() {
        ui = new UI();
    }


    /**
     * Prints the exit message, then closes the program.
     */
    public static void exit() {
        ui.printGoodbye();
        System.exit(0);
    }


    /**
     * Runs the command parser and return the message.
     *
     * @param command user's input command
     * @return result message
     */
    public static String runCommand(String command) {
        LOGGER.log(Level.INFO, "Start to parse user command");
        String msg;

        try {
            msg = Parser.parse(command);
            return msg;
        } catch (DukeException e) {
            LOGGER.log(Level.INFO, "Error in parsing user command");
            return e.getMessage();
        }
    }

    public static void run() {
        boolean isExit = false;
        String command;
        String resultMsg;

        while (!isExit) {
            command = ui.getUserCommand();
            resultMsg = runCommand(command);
            LOGGER.log(Level.INFO, "User Command Successfully Executed");
            isExit = Parser.isExit(command);

            if (!isExit) {
                ui.printCommandOutput(resultMsg);
            }
        }
    }

    public static void main(String[] args) {
        initialize();
        run();
        exit();
    }
}
