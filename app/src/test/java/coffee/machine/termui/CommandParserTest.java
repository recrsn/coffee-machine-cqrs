package coffee.machine.termui;

import coffee.machine.Error;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static coffee.machine.termui.CommandParser.arguments;
import static coffee.machine.termui.CommandParser.command;
import static org.junit.Assert.assertEquals;

public class CommandParserTest {
    @Test
    public void shouldReturnErrorIfCommandNotFound() {
        StringReader reader = new StringReader("unknown");

        CommandParser commandParser = new CommandParser(reader, List.of());

        assertEquals(List.of(Result.error(Error.Code.COMMAND_NOT_FOUND, "unknown")), commandParser.commands().toList());
    }

    @Test
    public void shouldReturnErrorIfArgumentMissing() {
        StringReader reader = new StringReader("test");

        CommandParser commandParser = new CommandParser(reader,
                List.of(command("test", "test-command", arguments("name", "name of test"))));

        assertEquals(List.of(Result.error(Error.Code.MISSING_ARGUMENT, "name")), commandParser.commands().toList());
    }

    @Test
    public void shouldParseCommandSuccessfully() {
        StringReader reader = new StringReader("test some-test");

        CommandParser commandParser = new CommandParser(reader,
                List.of(command("test", "test-command", arguments("name", "name of test"))));

        assertEquals(List.of(Result.success(new Command("test", Map.of("name", "some-test")))),
                commandParser.commands().toList());
    }

    @Test
    public void shouldParseCommandSuccessfullyAndIgnoreUnknownArgs() {
        StringReader reader = new StringReader("test some-test");

        CommandParser commandParser = new CommandParser(reader,
                List.of(command("test", "test-command")));

        assertEquals(List.of(Result.success(new Command("test", Map.of()))),
                commandParser.commands().toList());
    }
}
