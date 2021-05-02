package coffee.machine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTest {

    private static final PrintStream systemOut = System.out;
    private static final PrintStream systemErr = System.err;
    private static final InputStream systemIn = System.in;

    private Scanner sOut;
    private PrintStream sIn;
    private ByteArrayOutputStream sErr;

    @Before
    public void setUp() throws Exception {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream mockSystemIn = new PipedInputStream(pipedOutputStream);

        PipedOutputStream mockSystemOut = new PipedOutputStream();

        sOut = new Scanner(new PipedInputStream(mockSystemOut));
        sIn = new PrintStream(pipedOutputStream);

        sErr = new ByteArrayOutputStream();

        System.setIn(mockSystemIn);
        System.setOut(new PrintStream(mockSystemOut));
        System.setErr(new PrintStream(sErr));
    }

    @After
    public void tearDown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
        System.setErr(systemErr);
    }

    @Test
    public void shouldMakeCoffee() {
        Executors.newSingleThreadExecutor().submit(() -> {
            Main.main(new String[]{});
        });

        assertEquals("Interactive coffee maker", sOut.nextLine().trim());
        assertEquals("Use 'help' to see available commands", sOut.nextLine().trim());

        sIn.println("init 1");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("fill-ingredient WATER 2");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("fill-ingredient MILK 2");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("fill-ingredient COFFEE 2");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("make-coffee mocha");
        assertTrue(sOut.nextLine().trim().matches("Order [\\w-]+ assigned slot slot-[\\w-]+"));
        assertTrue(sOut.nextLine().trim().matches("Order [\\w-]+ is brewing"));
        assertTrue(sOut.nextLine().trim().matches("Order [\\w-]+ is ready"));
        assertEquals("OK", sOut.nextLine().trim());

//        Thread.sleep(5100);

        assertEquals("", sErr.toString());
    }

    @Test
    public void shouldPrintErrorWhenIngredientUnavailable() {
        Executors.newSingleThreadExecutor().submit(() -> {
            Main.main(new String[]{});
        });

        assertEquals("Interactive coffee maker", sOut.nextLine().trim());
        assertEquals("Use 'help' to see available commands", sOut.nextLine().trim());

        sIn.println("init 1");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("make-coffee mocha");
        assertTrue(sOut.nextLine().trim().matches("Order [\\w-]+ assigned slot slot-[\\w-]+"));
        assertTrue(sOut.nextLine().trim().matches("Order [\\w-]+ is cancelled"));
        assertEquals("OK", sOut.nextLine().trim());

//        Thread.sleep(5100);

        assertEquals("", sErr.toString());
    }

    @Test
    public void shouldPrintWarningsOnLowIngredient() {
        Executors.newSingleThreadExecutor().submit(() -> {
            Main.main(new String[]{});
        });

        assertEquals("Interactive coffee maker", sOut.nextLine().trim());
        assertEquals("Use 'help' to see available commands", sOut.nextLine().trim());

        sIn.println("init 1");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("test 1");

        sIn.println("fill-ingredient WATER 2");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("fill-ingredient MILK 1");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("fill-ingredient COFFEE 2");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("make-coffee mocha");
        assertTrue(sOut.nextLine().trim().matches("Order [\\w-]+ assigned slot slot-[\\w-]+"));
        assertEquals("MILK in store store-coffee-machine-1 running low", sOut.nextLine().trim());
        assertTrue(sOut.nextLine().trim().matches("Order [\\w-]+ is brewing"));
        assertTrue(sOut.nextLine().trim().matches("Order [\\w-]+ is ready"));
        assertEquals("OK", sOut.nextLine().trim());

        assertEquals("ERROR: COMMAND_NOT_FOUND test", sErr.toString());
    }

    @Test
    public void shouldPrintErrorOnCommandNotFound() throws InterruptedException {
        Executors.newSingleThreadExecutor().submit(() -> {
            Main.main(new String[]{});
        });

        assertEquals("Interactive coffee maker", sOut.nextLine().trim());
        assertEquals("Use 'help' to see available commands", sOut.nextLine().trim());

        sIn.println("test 1");
        Thread.sleep(100);
        assertEquals("ERROR: COMMAND_NOT_FOUND test\n", sErr.toString());
    }

    @Test
    public void shouldPrintErrorInitArgumentMissing() throws InterruptedException {
        Executors.newSingleThreadExecutor().submit(() -> {
            Main.main(new String[]{});
        });

        assertEquals("Interactive coffee maker", sOut.nextLine().trim());
        assertEquals("Use 'help' to see available commands", sOut.nextLine().trim());

        sIn.println("init");
        Thread.sleep(100);
        assertEquals("ERROR: MISSING_ARGUMENT slots\n", sErr.toString());
    }

    @Test
    public void shouldPrintErrorOnFillItemsArgumentMissing() throws InterruptedException {
        Executors.newSingleThreadExecutor().submit(() -> {
            Main.main(new String[]{});
        });

        assertEquals("Interactive coffee maker", sOut.nextLine().trim());
        assertEquals("Use 'help' to see available commands", sOut.nextLine().trim());

        sIn.println("init 2");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("fill-ingredient");
        Thread.sleep(100);
        assertEquals("ERROR: MISSING_ARGUMENT name\n", sErr.toString());
    }

    @Test
    public void shouldPrintErrorOnFillItemsQuantityMissing() throws InterruptedException {
        Executors.newSingleThreadExecutor().submit(() -> {
            Main.main(new String[]{});
        });

        assertEquals("Interactive coffee maker", sOut.nextLine().trim());
        assertEquals("Use 'help' to see available commands", sOut.nextLine().trim());

        sIn.println("init 2");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("fill-ingredient WATER");
        Thread.sleep(100);
        assertEquals("ERROR: MISSING_ARGUMENT quantity\n", sErr.toString());
    }

    @Test
    public void shouldPrintErrorOnFillItemsWithInvalidIngredient() throws InterruptedException {
        Executors.newSingleThreadExecutor().submit(() -> {
            Main.main(new String[]{});
        });

        assertEquals("Interactive coffee maker", sOut.nextLine().trim());
        assertEquals("Use 'help' to see available commands", sOut.nextLine().trim());

        sIn.println("init 2");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("fill-ingredient SODA 10");
        Thread.sleep(100);
        assertEquals("ERROR: UNKNOWN_INGREDIENT SODA\n", sErr.toString());
    }

    @Test
    public void shouldPrintErrorForUnknownRecipe() throws InterruptedException {
        Executors.newSingleThreadExecutor().submit(() -> {
            Main.main(new String[]{});
        });

        assertEquals("Interactive coffee maker", sOut.nextLine().trim());
        assertEquals("Use 'help' to see available commands", sOut.nextLine().trim());

        sIn.println("init 1");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("fill-ingredient WATER 2");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("fill-ingredient MILK 2");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("fill-ingredient COFFEE 2");
        assertEquals("OK", sOut.nextLine().trim());

        sIn.println("make-coffee unknown");
        Thread.sleep(100);

        assertEquals("ERROR: UNKNOWN_RECIPE unknown\n", sErr.toString());
    }


}
