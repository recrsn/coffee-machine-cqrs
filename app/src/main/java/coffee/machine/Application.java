package coffee.machine;

import coffee.machine.termui.Command;
import coffee.machine.termui.CommandDispatcher;
import coffee.machine.termui.CommandParser;
import coffee.machine.termui.HelpMessageHandler;
import coffee.machine.termui.ResponseWriter;
import coffee.machine.termui.spec.CommandSpec;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static coffee.machine.termui.CommandParser.command;

public class Application {
    private final ResponseWriter responseWriter;
    private final CommandParser commandParser;
    private final CommandDispatcher commandDispatcher;

    public Application(ResponseWriter responseWriter, CommandParser commandParser, CommandDispatcher commandDispatcher) {
        this.responseWriter = responseWriter;
        this.commandParser = commandParser;
        this.commandDispatcher = commandDispatcher;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void start() {
        printWelcomeMessage();
        commandDispatcher.attach(commandParser, responseWriter);
    }

    private void printWelcomeMessage() {
        responseWriter.message("Interactive coffee maker");
        responseWriter.message("Use 'help' to see available commands");
    }

    public static class Builder {
        private final Map<String, BiConsumer<Command, ResponseWriter>> handlerMap = new HashMap<>();
        private final List<CommandSpec> commandSpecList = new ArrayList<>();
        private ResponseWriter responseWriter;
        private InputStreamReader reader;

        public Builder with(ResponseWriter responseWriter) {
            this.responseWriter = responseWriter;
            return this;
        }

        public Builder add(CommandSpec commandSpec, BiConsumer<Command, ResponseWriter> handler) {
            handlerMap.put(commandSpec.name().toLowerCase(), handler);
            commandSpecList.add(commandSpec);
            return this;
        }

        public Application build() {
            add(command("help", "show help"), new HelpMessageHandler(commandSpecList));
            CommandDispatcher commandDispatcher = new CommandDispatcher(handlerMap);
            CommandParser commandParser = new CommandParser(reader, commandSpecList);
            return new Application(responseWriter, commandParser, commandDispatcher);
        }

        public Builder with(InputStreamReader inputStreamReader) {
            this.reader = inputStreamReader;
            return this;
        }
    }
}
