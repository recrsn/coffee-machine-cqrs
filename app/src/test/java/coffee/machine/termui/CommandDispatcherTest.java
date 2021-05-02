package coffee.machine.termui;

import coffee.machine.Error;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CommandDispatcherTest {
    private final CommandParser commandParser = mock(CommandParser.class);
    private final ResponseWriter responseWriter = mock(ResponseWriter.class);

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionForUnmappedCommand() {
        when(commandParser.commands())
                .thenReturn(Stream.of(Result.success(new Command("unknown", Map.of()))));

        CommandDispatcher dispatcher = new CommandDispatcher(Map.of(), directExecutor());
        dispatcher.attach(commandParser, responseWriter);
    }

    private Executor directExecutor() {
        return Runnable::run;
    }

    @Test
    public void shouldPrintErrorForParseErrors() {
        when(commandParser.commands())
                .thenReturn(Stream.of(Result.error(Error.Code.COMMAND_NOT_FOUND, "test")));

        CommandDispatcher dispatcher = new CommandDispatcher(Map.of(), directExecutor());
        dispatcher.attach(commandParser, responseWriter);

        verify(responseWriter, only()).error(new Error(Error.Code.COMMAND_NOT_FOUND, "test"));
    }

    @Test
    public void shouldCallHandlerOnReceivingNewCommand() {
        Command test = new Command("test", Map.of());
        when(commandParser.commands()).thenReturn(Stream.of(Result.success(test)));

        CommandDispatcher dispatcher = new CommandDispatcher(Map.of("test", (c, rw) -> {
            assertEquals(test, c);
            assertEquals(rw, responseWriter);
        }), directExecutor());

        dispatcher.attach(commandParser, responseWriter);
    }

    @Test
    public void shouldCallHandlerPrintErrorOnCommandHandlingError() {
        Command test = new Command("test", Map.of());
        when(commandParser.commands()).thenReturn(Stream.of(Result.success(test)));

        CommandDispatcher dispatcher = new CommandDispatcher(Map.of("test", (c, rw) -> {
            throw new RuntimeException("unknown");
        }), directExecutor());

        dispatcher.attach(commandParser, responseWriter);
        verify(responseWriter, only()).error(new Error(Error.Code.UNKNOWN, "unknown"));

    }
}
