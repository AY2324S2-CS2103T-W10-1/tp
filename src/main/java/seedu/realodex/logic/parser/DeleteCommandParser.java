package seedu.realodex.logic.parser;

import static seedu.realodex.logic.commands.DeleteCommand.MESSAGE_INDEX_AND_NAME_PROVIDED;
import static seedu.realodex.logic.commands.DeleteCommand.MESSAGE_NO_FIELDS_PROVIDED;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.realodex.commons.core.index.Index;
import seedu.realodex.logic.commands.DeleteCommand;
import seedu.realodex.logic.parser.exceptions.ParseException;
import seedu.realodex.model.person.Name;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME);

        Name name = parseName(argMultimap);
        Index index = parseIndex(argMultimap);

        return createDeleteCommand(name, index);
    }

    private Name parseName(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            ParserUtilResult<Name> name = ParserUtil.parseNameReturnStored(argMultimap.getValue(PREFIX_NAME)
                    .orElseThrow());
            if (!name.returnExceptionMessage().isEmpty()) {
                throw new ParseException(name.returnExceptionMessage());
            }
            return name.returnStoredResult();
        }
        return null;
    }

    private Index parseIndex(ArgumentMultimap argMultimap) throws ParseException {
        String preamble = argMultimap.getPreamble();
        if (!preamble.isBlank()) {
            return ParserUtil.parseIndex(preamble);
        }
        return null;
    }

    private DeleteCommand createDeleteCommand(Name name, Index index) throws ParseException {
        if (name != null && index != null) {
            throw new ParseException(MESSAGE_INDEX_AND_NAME_PROVIDED);
        } else if (name != null) {
            return new DeleteCommand(name);
        } else if (index != null) {
            return new DeleteCommand(index);
        } else {
            throw new ParseException(MESSAGE_NO_FIELDS_PROVIDED);
        }
    }
}
