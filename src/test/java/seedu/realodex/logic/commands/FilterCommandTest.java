package seedu.realodex.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.realodex.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.realodex.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.realodex.testutil.TypicalPersons.CARL;
import static seedu.realodex.testutil.TypicalPersons.DANIEL;
import static seedu.realodex.testutil.TypicalPersons.ELLE;
import static seedu.realodex.testutil.TypicalPersons.GEORGE;
import static seedu.realodex.testutil.TypicalPersons.getTypicalRealodex;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.realodex.model.Model;
import seedu.realodex.model.ModelManager;
import seedu.realodex.model.UserPrefs;
import seedu.realodex.model.person.predicates.NameContainsKeyphrasePredicate;
import seedu.realodex.model.person.predicates.RemarkContainsKeyphrasePredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {
    private Model model = new ModelManager(getTypicalRealodex(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalRealodex(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeyphrasePredicate firstPredicate =
                new NameContainsKeyphrasePredicate("first");
        NameContainsKeyphrasePredicate secondPredicate =
                new NameContainsKeyphrasePredicate("second");
        RemarkContainsKeyphrasePredicate thirdPredicate =
                new RemarkContainsKeyphrasePredicate("first");
        RemarkContainsKeyphrasePredicate fourthPredicate =
                new RemarkContainsKeyphrasePredicate("second");

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);
        FilterCommand filterThirdCommand = new FilterCommand(thirdPredicate);
        FilterCommand filterFourthCommand = new FilterCommand(fourthPredicate);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different predicate type, different keyphrase -> returns false
        assertFalse(filterFirstCommand.equals(filterFourthCommand));

        // same predicate type, different keyphrase -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));

        // different predicate type, same keyphrase -> returns false
        assertFalse(filterFirstCommand.equals(filterThirdCommand));
        assertFalse(filterSecondCommand.equals(filterFourthCommand));
    }

    @Test
    public void execute_nonMatchingNameKeyphrase_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        NameContainsKeyphrasePredicate predicate = prepareNamePredicate("yapyapyap");
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_nonMatchingRemarkKeyphrase_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        RemarkContainsKeyphrasePredicate predicate = prepareRemarkPredicate("abcdefghijklmnopqrstuvwxyz");
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }


    @Test
    public void execute_oneNameKeyphrase_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        NameContainsKeyphrasePredicate predicate = prepareNamePredicate("El");
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(DANIEL, ELLE), model.getFilteredPersonList());
    }

    @Test
    public void execute_oneRemarkKeyphrase_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        RemarkContainsKeyphrasePredicate predicate = prepareRemarkPredicate("the");
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, GEORGE), model.getFilteredPersonList());
    }


    @Test
    public void toStringMethod() {
        NameContainsKeyphrasePredicate predicate = new NameContainsKeyphrasePredicate("keyphrase");
        FilterCommand filterCommand = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeyphrasePredicate}.
     */
    private NameContainsKeyphrasePredicate prepareNamePredicate(String userInput) {
        return new NameContainsKeyphrasePredicate(userInput);
    }

    /**
     * Parses {@code userInput} into a {@code RemarkContainsKeyphrasePredicate}.
     */
    private RemarkContainsKeyphrasePredicate prepareRemarkPredicate(String userInput) {
        return new RemarkContainsKeyphrasePredicate(userInput);
    }
}
