package coffee.machine.termui;

import coffee.machine.Error;

import java.io.PrintStream;

public class ResponseWriter {
    private final PrintStream writer;
    private final PrintStream errorWriter;

    public ResponseWriter(PrintStream responseWriter, PrintStream errorWriter) {
        this.writer = responseWriter;
        this.errorWriter = errorWriter;
    }

    public synchronized void error(Error error) {
        errorWriter.printf("ERROR: %s %s\n", error.code(), error.description());
    }

    public synchronized void message(String message) {
        writer.println(message);
    }

    public synchronized void ok() {
        writer.println("OK");
    }
}
