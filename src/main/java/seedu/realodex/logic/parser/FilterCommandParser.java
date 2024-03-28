package seedu.realodex.logic.parser;

import static seedu.realodex.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.realodex.logic.commands.FilterCommand;
import seedu.realodex.logic.parser.exceptions.ParseException;
import seedu.realodex.model.person.NameContainsKeyphrasePredicate;
import seedu.realodex.model.person.Person;
import seedu.realodex.model.person.RemarkContainsKeyphrasePredicate;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_REMARK);

        //if no name prefix and no tag prefix to add more soon
        if (!anyPrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_REMARK) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        } else if (moreThanOnePrefixPresent(argMultimap, PREFIX_NAME, PREFIX_REMARK)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_FILTER_CONFLICT)
            );
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_REMARK);

        Predicate<Person> predicate = producePredicate(argMultimap);

        return new FilterCommand(predicate);
    }


    /**
     * Returns true if some of the prefixes contains non-empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean anyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> {
                // For all other prefixes, use the standard presence check
                return argumentMultimap.getValue(prefix).isPresent();
        });
    }

    /**
     * Returns true if multiple prefixes contains non-empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean moreThanOnePrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        long count = Stream.of(prefixes).filter(prefix -> {
                // For all other prefixes, use the standard presence check
                return argumentMultimap.getValue(prefix).isPresent();
        }).count();
        return count > 1;
    }

    /**
     * Returns true if there is a PREFIX_NAME in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean isPrefixPresent(ArgumentMultimap argumentMultimap, Prefix prefix) {
        if (prefix.equals(PREFIX_REMARK)) {
            // For the remark prefix, consider it present if the returned string is not empty
            return argumentMultimap.getValue(prefix).map(value -> !value.isEmpty()).orElse(false);
        } else {
            // For all other prefixes, use the standard presence check
            return argumentMultimap.getValue(prefix).isPresent();
        }
    }
    private static Predicate<Person> producePredicate(ArgumentMultimap argumentMultimap) throws ParseException {
        if (isPrefixPresent(argumentMultimap, PREFIX_NAME)) {
            String keyphrase = argumentMultimap.getValue(PREFIX_NAME).get().trim();
            if (keyphrase.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
            }
            return new NameContainsKeyphrasePredicate(keyphrase);
        } else if (isPrefixPresent(argumentMultimap, PREFIX_REMARK)) {
            String keyphrase = argumentMultimap.getValue(PREFIX_REMARK).get().trim();
            if (keyphrase.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
            }
            return new RemarkContainsKeyphrasePredicate(keyphrase);
        }

        //should never reach here since passed anyPrefixPresent
        assert false;
        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

}
