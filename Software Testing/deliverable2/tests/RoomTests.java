import com.laboon.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class RoomTests {

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

    /* room tests */

    // test room constructor with false arguments
    @Test
    public void testRoomConstructorFalse() {
        Room r = null;
        r = new Room(false, false, false, false, false);
        assertNotNull(r);        
    }
	
    // test room constructor with true arguments
    @Test
    public void testRoomConstructor() {
        Room r = null;
        r = new Room(true, true, true, true, true);
        assertNotNull(r);        
    }

    // test that hasItem returns true when room has coffee
    @Test
    public void testHasItemHaveCoffee() {
        Room r = new Room(true, false, false, false, false);
        assertTrue(r.hasItem());
    }

    // test that hasItem returns true when room has coffee and cream
    @Test
    public void testHasItemHaveCoffeeAndCream() {
        Room r = new Room(true, true, false, false, false);
        assertTrue(r.hasItem());
    }

    // test that hasItem returns false when room has no items
    @Test
    public void testHasItemHaveNoItems() {
        Room r = new Room(false, false, false, false, false);
        assertFalse(r.hasItem());
    }

    // test that hasSugar returns true when room has sugar
    @Test
    public void testHasSugarWithSugar() {
        Room r = new Room(true, true, true, true, true);
        assertTrue(r.hasSugar());
    }

    // test that hasSugar returns false when room doesn't have sugar
    @Test
    public void testHasSugarNoSugar() {
        Room r = new Room(false, false, false, false, false);
        assertFalse(r.hasSugar());
    }

    // test that hasCream returns true when room has cream
    @Test
    public void testHasCreamWithCream() {
        Room r = new Room(true, true, true, true, true);
        assertTrue(r.hasCream());
    }

    // test that hasCream returns false when room doesn't have cream
    @Test
    public void testHasCreamNoCream() {
        Room r = new Room(false, false, false, false, false);
        assertFalse(r.hasCream());
    }

    // test that hasCoffee returns true when room has coffee
    @Test
    public void testHasCoffeeWithCoffee() {
        Room r = new Room(true, true, true, true, true);
        assertTrue(r.hasCoffee());
    }

    // test that hasCoffee returns false when room doesn't have coffee
    @Test
    public void testHasCoffeeNoCoffee() {
        Room r = new Room(false, false, false, false, false);
        assertFalse(r.hasCoffee());
    }

    // test that northExit returns true when room has a north exit
    @Test
    public void northExitHasNorthExit() {
        Room r = new Room(true, true, true, true, true);
        assertTrue(r.northExit());
    }

    // test that northExit returns false when room doesn't have a north exit
    @Test
    public void northExitNoNorthExit() {
        Room r = new Room(false, false, false, false, false);
        assertFalse(r.northExit());
    }

    // test that southExit returns true when room has a south exit
    @Test
    public void southExitHasSouthExit() {
        Room r = new Room(true, true, true, true, true);
        assertTrue(r.southExit());
    }

    // test that southExit returns false when room doesn't have a south exit
    @Test
    public void southExitNoSouthExit() {
        Room r = new Room(false, false, false, false, false);
        assertFalse(r.southExit());
    }

    // test that getDescription() returns a non-null string
    @Test
    public void testGetDescriptionNotNull() {
        Room r = new Room(true, true, true, true, true);
        String d = null;
        d = r.getDescription();
        assertNotNull(d);
    }

    // test that getDescription() returns a non-empty string
    @Test
    public void testGetDescriptionNotEmpty() {
        Room r = new Room(true, true, true, true, true);
        String d = r.getDescription();
        assertFalse(d.equals(""));
    }
}