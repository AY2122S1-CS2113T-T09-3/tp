package seedu.duke.ui;

import java.util.Scanner;

public class UI {

    //TODO: CREATE SCANNER, MESSAGES TO PRINT, CONSTRUCTOR, I/O
    // (when taking inputs for name, units, rmb to set all to lowercase)

    Scanner line = new Scanner(System.in);

    public static final String DIVIDER = "___________________________________________\n";
    private static final String WELCOME_MESSAGE = DIVIDER
            + "Welcome to SITUS!\n"
            + "What would you like to do first?\n"
            + DIVIDER;
    private static final String GOODBYE_MESSAGE = DIVIDER + "Okay, see you soon! Goodbye.\n" + DIVIDER;


    public UI() {
        System.out.print(WELCOME_MESSAGE);
    }

    public String getUserCommand() {
        return line.nextLine().trim().toLowerCase();
    }

    public void printGoodbye() {
        System.out.print(GOODBYE_MESSAGE);
    }

    public void printCommandOutput(String commandOutput) {
        System.out.print(DIVIDER + commandOutput + "\n" + DIVIDER);
    }

    public void printErrorMessage(String message) {
    }
}
