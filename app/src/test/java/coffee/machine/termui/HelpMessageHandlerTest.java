package coffee.machine.termui;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static coffee.machine.termui.CommandParser.arguments;
import static coffee.machine.termui.CommandParser.command;
import static org.junit.Assert.assertEquals;

public class HelpMessageHandlerTest {
    @Test
    public void shouldPrintHelpMessage() {
        HelpMessageHandler handler = new HelpMessageHandler(List.of(
                command("foo", "prints foo"),
                command("bar", "prints bar", arguments("times", "how many times to print"))));

        ByteArrayOutputStream dummyOutputStream = new ByteArrayOutputStream();
        ResponseWriter writer = new ResponseWriter(new PrintStream(dummyOutputStream), null);
        handler.accept(new Command("help", Map.of()), writer);

        assertEquals("""
                Commands:
                ---------
                foo:\tprints foo

                bar:\tprints bar
                \tArguments:
                \t----------
                \ttimes:\thow many times to print

                """, dummyOutputStream.toString());
    }
}
