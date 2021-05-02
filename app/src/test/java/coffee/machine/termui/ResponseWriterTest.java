package coffee.machine.termui;

import coffee.machine.Error;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ResponseWriterTest {
    @Test
    public void shouldPrintMessage() {
        ByteArrayOutputStream dummyOutputStream = new ByteArrayOutputStream();
        ResponseWriter writer = new ResponseWriter(new PrintStream(dummyOutputStream), null);

        writer.message("hello world");
        assertEquals("hello world\n", dummyOutputStream.toString());
    }

    @Test
    public void shouldPrintError() {
        ByteArrayOutputStream dummyOutputStream = new ByteArrayOutputStream();
        ResponseWriter writer = new ResponseWriter(null, new PrintStream(dummyOutputStream));

        writer.error(new Error(Error.Code.UNKNOWN, "unknown"));
        assertEquals("ERROR: UNKNOWN unknown\n", dummyOutputStream.toString());
    }
}
