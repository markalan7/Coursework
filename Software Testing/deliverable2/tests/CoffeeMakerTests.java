import com.laboon.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CoffeeMakerTests {
    private final ByteArrayOutputStream outStream = new ByteArrayOutputStream();

    // set print stream to byte array output stream
    @Before
    public void setOutputStream() {
        System.setOut(new PrintStream(outStream));
    }

    // clear output stream
    @After
    public void clearOutputStream() {
        System.setOut(null);
    }

    /* tests for CoffeeMaker.java */

    // test that runArgs prints its strings
    @Test
    public void testRunArgsPrintsSuccessfully() {
    	CoffeeMaker cm = new CoffeeMaker();		// use default constructor
        cm.runArgs("");
        String out = outStream.toString();
        assertTrue(out.contains("You are a brave student trying to study for finals, but you need caffeine."));
    }

    // test that runArgs returns 0
    @Test
    public void testRunArgsReturnsZero() {
    	CoffeeMaker cm = new CoffeeMaker();		// use default constructor
        int ret = cm.runArgs("");
        assertEquals(0, ret);
    }
}