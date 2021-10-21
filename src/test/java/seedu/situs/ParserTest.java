package seedu.situs;

import org.junit.jupiter.api.Test;
import seedu.situs.exceptions.DukeException;
import seedu.situs.parser.Parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {
    @Test
    public void parseDeleteCommand_invalidNumberFormatInput_expectException() {
        try {
            String inputString = "delete abc";
            String resultMsg = Parser.parse(inputString);
            fail();
        } catch (DukeException e) {
            assertEquals("Invalid number format!", e.getMessage());
        }
    }

    /*@Test
    public void parseDeleteCommand_taskNumberInvalidInput_expectException() {
        try {
            String inputString = "delete -1";
            String resultMsg = Parser.parse(inputString);
            fail();
        } catch (DukeException e) {
            assertEquals("Ingredient number does not exist!", e.getMessage());
        }
    }*/

    @Test
    public void parseCommand_invalidCommandInput_success() throws DukeException {
        String inputString = "foo 2";
        String resultMsg = Parser.parse(inputString);
        assertEquals("Invalid command!", resultMsg);
    }

    @Test
    public void parseAddCommand_insufficientCommandParameters_expectException() {
        try {
            String inputString = "add carrot";
            String resultMsg = Parser.parse(inputString);
        } catch (DukeException e) {
            assertEquals("The number of parameters is wrong!", e.getMessage());
        }
    }

    @Test
    public void parseAddCommand_invalidAmountParameter_expectException() {
        try {
            String inputString = "add n/carrot a/five e/1aug";
            String resultMsg = Parser.parse(inputString);
        } catch (DukeException e) {
            assertEquals("Invalid number format!", e.getMessage());
        }
    }

    @Test
    public void parseUpdateCommand_insufficientCommandParameters_expectException() {
        try {
            String inputString = "update onion";
            String resultMsg = Parser.parse(inputString);
            fail();
        } catch (DukeException e) {
            assertEquals("The number of parameters is wrong!", e.getMessage());
        }
    }

    @Test
    public void parseAddCommand_invalidExpiryDate_expectException() {
        try {
            String inputString = "add n/carrot a/50 e/1 dec 2021";
            String resultMsg = Parser.parse(inputString);
            fail();
        } catch (DukeException e) {
            assertEquals("Invalid expiry date format!"
                    + '\n' + "Please key in the expiry date in the format dd/mm/yyyy!", e.getMessage());
        }
    }

    @Test
    public void parseUpdateCommand_ingredientNotFound_success() throws DukeException {
        String inputString1 = "add n/ apple a/ 300 e/ 12/10/2021";
        String inputString2 = "update n/ go out have fun a/ 12.3 e/ 12/10/2021";
        Parser.parse(inputString1);
        String resultMsg = Parser.parse(inputString2);
        assertEquals("Ingredient not found!", resultMsg);
    }

    @Test
    public void parseFindCommand_incorrectParameters_expectException() {
        try {
            String inputString = "find";
            String resultMsg = Parser.parse(inputString);
        } catch (DukeException e) {
            assertEquals("The number of parameters is wrong!", e.getMessage());
        }
    }
}
