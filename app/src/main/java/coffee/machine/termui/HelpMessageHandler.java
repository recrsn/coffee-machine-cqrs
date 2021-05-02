package coffee.machine.termui;

import coffee.machine.termui.spec.CommandSpec;

import java.util.List;
import java.util.function.BiConsumer;

public class HelpMessageHandler implements BiConsumer<Command, ResponseWriter> {
    private final List<CommandSpec> commandSpecList;

    public HelpMessageHandler(List<CommandSpec> commandSpecList) {
        this.commandSpecList = commandSpecList;
    }

    @Override
    public void accept(Command command, ResponseWriter responseWriter) {
        responseWriter.message("Commands:");
        responseWriter.message("---------");

        for (var cmd : commandSpecList) {
            responseWriter.message(String.format("%s:\t%s", cmd.name(), cmd.description()));

            if (!cmd.arguments().isEmpty()) {
                responseWriter.message("\tArguments:");
                responseWriter.message("\t----------");
            }
            for (var arg : cmd.arguments()) {
                responseWriter.message(String.format("\t%s:\t%s", arg.name(), arg.description()));
            }

            responseWriter.message("");
        }
    }
}
