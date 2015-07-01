import com.laboon.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PlayerTests {
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

    /* Player tests */

    // test no-argument constructor
    @Test
    public void testPlayerConstructor() {
        Player p = null;
        p = new Player();
        assertNotNull(p);
    }

    // test constructor with arguments
    @Test
    public void testPlayerConstructorArguments() {
        Player p = null;
        p = new Player(false, false, false);
        assertNotNull(p);
    }

    // test constructor with all true arguments
    @Test
    public void testPlayerConstructorTrueArguments() {
        Player p = null;
        p = new Player(true, true, true);
        assertNotNull(p);
    }

    // test that getSugar results in obtaining sugar
    // already have cream and coffee
    @Test
    public void testGetSugar() {
        Player p = new Player(false, true, true);
        p.getSugar();
        assertTrue(p.hasAllItems());
    }

    // test that getCream results in obtaining cream
    // already have sugar and coffee
    @Test
    public void testGetCream() {
        Player p = new Player(true, false, true);
        p.getCream();
        assertTrue(p.hasAllItems());
    }

    // test that getCoffee results in obtaining coffee
    // already have sugar and cream
    @Test
    public void testGetCoffee() {
        Player p = new Player(true, true, false);
        p.getCoffee();
        assertTrue(p.hasAllItems());
    }

    // test hasAllItems with all items
    // hasAllItems should be true
    @Test
    public void testHasAllItemsHaveAllItems() {
        Player p = new Player(true, true, true);
        assertTrue(p.hasAllItems());
    }

    // test hasAllItems with sugar and cream
    // hasAllItems should be false
    @Test
    public void testHasAllItemsHaveSugarAndCream() {
        Player p = new Player(true, true, false);
        assertFalse(p.hasAllItems());
    }

    // test hasAllItems with just cream
    // hasAllItems should be false
    @Test
    public void testHasAllItemsHaveCream() {
        Player p = new Player(false, true, false);
        assertFalse(p.hasAllItems());
    }

    // test hasAllItems without any items
    // hasAllItems should be false
    @Test
    public void testHasAllItemsHaveNoItems() {
        Player p = new Player(false, false, false);
        assertFalse(p.hasAllItems());
    }

    // test showInventory with coffee
    // should print out the strings indicating possession of coffee
    @Test
    public void testShowInventoryHaveCoffee() {
        Player p = new Player(false, false, true);
        p.showInventory();
        String out = outStream.toString();
        assertTrue(out.contains("You have a cup of delicious coffee."));
    }

    // test showInventory with cream and sugar
    // should print out the strings indicating possession of cream and sugar
    @Test
    public void testShowInventoryHaveCreamAndSugar() {
        Player p = new Player(true, true, false);
        p.showInventory();
        String out = outStream.toString();
        assertTrue(out.contains("You have some fresh cream.") && out.contains("You have some tasty sugar."));
    }

    // test showInventory with no items
    // should print out the strings indicating nonpossession of each item
    @Test
    public void testShowInventoryNoItems() {
        Player p = new Player(false, false, false);
        p.showInventory();
        String out = outStream.toString();
        assertTrue(out.contains("YOU HAVE NO COFFEE!") && out.contains("YOU HAVE NO CREAM!") && out.contains("YOU HAVE NO SUGAR!"));
    }

    // test drink output with all items
    // should print message indicating victory
    @Test
    public void testDrinkHaveAllItemsOutput() {
        Player p = new Player(true, true, true);
        p.drink();
        String out = outStream.toString();
        assertTrue(out.contains("You drink the beverage and are ready to study!"));
    }

    // test drink output with coffee and cream
    // should print message indicating lack of sugar
    @Test
    public void testDrinkHaveCoffeeAndCreamOutput() {
        Player p = new Player(false, true, true);
        p.drink();
        String out = outStream.toString();
        assertTrue(out.contains("Without sugar, the coffee is too bitter.  You cannot study."));
    }

    // test drink output with sugar
    // should print message indicating lack of coffee
    @Test
    public void testDrinkHaveSugarOutput() {
        Player p = new Player(true, false, false);
        p.drink();
        String out = outStream.toString();
        assertTrue(out.contains("You eat the sugar, but without caffeine, you cannot study."));
    }

    // test drink all items
    // should return true for victory condition
    @Test
    public void testDrinkHaveAllItems() {
        Player p = new Player(true, true, true);
        assertTrue(p.drink());
    }

    // test drink with coffee and cream
    // should return false for losing condition
    @Test
    public void testDrinkHaveCoffeeAndCream() {
        Player p = new Player(false, true, true);
        assertFalse(p.drink());
    }

    // test drink with sugar
    // should return false for losing condition
    @Test
    public void testDrinkHaveSugar() {
        Player p = new Player(true, false, false);
        assertFalse(p.drink());
    }


    // test drink no items
    // should return false for losing condition
    @Test
    public void testDrinkHaveNothing() {
        Player p = new Player(false, false, false);
        assertFalse(p.drink());
    }
}