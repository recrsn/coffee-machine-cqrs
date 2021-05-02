package coffee.machine.termui;

import coffee.machine.Error;

import java.util.Map;
import java.util.function.BiConsumer;

public class CommandDispatcher {
    private final Map<String, BiConsumer<Command, ResponseWriter>> handlerMap;

    public CommandDispatcher(Map<String, BiConsumer<Command, ResponseWriter>> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public void attach(CommandParser commandParser, ResponseWriter responseWriter) {
        commandParser.commands().forEach(result -> {
            if (result.hasError()) {
                responseWriter.error(result.error());
                return;
            }

            Command command = result.data();

            var handler = handlerMap.get(command.name());

            if (handler == null) {
                throw new IllegalStateException("Unmapped command in dispatcher");
            }

            try {
                handler.accept(command, responseWriter);
            } catch (Exception e) {
                responseWriter.error(new Error(Error.Code.UNKNOWN, e.getMessage()));
            }
        });
    }
}
