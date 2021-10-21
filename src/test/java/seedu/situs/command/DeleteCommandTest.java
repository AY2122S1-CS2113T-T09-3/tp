package seedu.situs.command;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import seedu.situs.exceptions.DukeException;

public class DeleteCommandTest {

    @Test
    public void deleteCommandTest_invalidNumberInput_expectException() {
        try {
            int removeNumber = -1;
            String resultMsg = new DeleteCommand(removeNumber).run();
            fail();
        } catch (DukeException e) {
            assertEquals("Ingredient number does not exist!", e.getMessage());
        }
    }

}
