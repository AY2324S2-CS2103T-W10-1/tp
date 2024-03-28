package seedu.realodex.logic.parser;

import static seedu.realodex.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.realodex.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.realodex.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.realodex.logic.Messages;
import seedu.realodex.logic.commands.FilterCommand;
import seedu.realodex.model.person.predicates.NameContainsKeyphrasePredicate;
import seedu.realodex.model.person.predicates.RemarkContainsKeyphrasePredicate;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_validArgsWithName_returnsFilterCommand() {
        String userInput = " n/Alice";
        FilterCommand expectedCommand = new FilterCommand(new NameContainsKeyphrasePredicate("Alice"));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    void parse_validArgsWithRemark_returnsFilterCommand() {
        String userInput = " r/Loves cats";
        FilterCommand expectedCommand = new FilterCommand(new RemarkContainsKeyphrasePredicate("Loves cats"));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    void parse_invalidArgs_throwsParseException() {
        String userInput = " invalidArg";
        assertParseFailure(parser, userInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_invalidPrefix_throwsParseException() {
        String userInput = " p/999";
        assertParseFailure(parser, userInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_multiplePrefixes_throwsParseException() {
        String userInput = " n/Alice r/Loves cats";
        assertParseFailure(parser, userInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_FILTER_CONFLICT));
    }

    @Test
    void parse_multiplePrefixesWithEmptyRemark_throwsParseException() {
        String userInput = " n/Alice r/";
        assertParseFailure(parser, userInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_FILTER_CONFLICT));
    }

    @Test
    void parse_multiplePrefixesWithEmptyName_throwsParseException() {
        String userInput = " n/ r/Loves cats";
        assertParseFailure(parser, userInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_FILTER_CONFLICT));
    }
    @Test
    void parse_duplicatePrefixes_throwsParseException() {
        String userInput = " n/Alice n/Bob";
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
    }


    @Test
    void parse_emptyPreamble_throwsParseException() {
        String userInput = " yapyap n/Alice";
        assertParseFailure(parser, userInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

}
