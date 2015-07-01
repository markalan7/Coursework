import com.laboon.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class GameTests {
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
    
	// Tests Game.doSomething() with the input 'N'
	@Test
	public void testDoSomethingNorth() {
		House mockHouse = mock(House.class);
		Game g = new Game(new Player(), mockHouse);
		int retValue = g.doSomething("N");
		verify(mockHouse, times(1)).moveNorth();
		assertEquals(retValue, 0);
	}
	
	// Tests Game.doSomething() with the input 'n'
	@Test
	public void testDoSomethingNorthLowercase() {
		House mockHouse = mock(House.class);
		Game g = new Game(new Player(), mockHouse);
		int retValue = g.doSomething("n");
		verify(mockHouse, times(1)).moveNorth();
			
		assertEquals(retValue, 0);
	}
	
	// Tests Game.doSomething() with the input 'S'
	@Test
	public void testDoSomethingSouth() {
		House mockHouse = mock(House.class);
		Game g = new Game(new Player(), mockHouse);
		int retValue = g.doSomething("S");
		verify(mockHouse, times(1)).moveSouth();
		assertEquals(retValue, 0);
	}

	// Tests Game.doSomething() with the input 's'
	@Test
	public void testDoSomethingSouthLowercase() {
		House mockHouse = mock(House.class);
		Game g = new Game(new Player(), mockHouse);
		int retValue = g.doSomething("s");
		verify(mockHouse, times(1)).moveSouth();
		assertEquals(retValue, 0);
	}
	
	// Tests Game.doSomething() with the input 'L'
	@Test
	public void testDoSomethingLook() {
		House mockHouse = mock(House.class);
		Player mockPlayer = mock(Player.class);
		Game g = new Game(mockPlayer, mockHouse);
		int retValue = g.doSomething("L");
		verify(mockHouse, times(1)).look(mockPlayer, null);
		assertEquals(retValue, 0);
	}

	// Tests Game.doSomething() with the input 'l'
	@Test
	public void testDoSomethingLookLowercase() {
		House mockHouse = mock(House.class);
		Player mockPlayer = mock(Player.class);
		Game g = new Game(mockPlayer, mockHouse);
		int retValue = g.doSomething("l");
		verify(mockHouse, times(1)).look(mockPlayer, null);
		assertEquals(retValue, 0);
	}

	// Tests Game.doSomething() with the input 'I'
	@Test
	public void testDoSomethingInventory() {
		House mockHouse = mock(House.class);
		Player mockPlayer = mock(Player.class);
		Game g = new Game(mockPlayer, mockHouse);
		int retValue = g.doSomething("I");
		verify(mockPlayer, times(1)).showInventory();
		assertEquals(retValue, 0);
	}

	// Tests Game.doSomething() with the input 'i'
	@Test
	public void testDoSomethingInventoryLowercase() {
		House mockHouse = mock(House.class);
		Player mockPlayer = mock(Player.class);
		Game g = new Game(mockPlayer, mockHouse);
		int retValue = g.doSomething("i");
		verify(mockPlayer, times(1)).showInventory();
		assertEquals(retValue, 0);
	}
	
	// Tests Game.doSomething() with the input 'D' with winning conditions
	@Test
	public void testDoSomethingDrinkWin() {
		House mockHouse = mock(House.class);
		Player mockPlayer = mock(Player.class);
		// Stub out Player.drink()
		when(mockPlayer.drink()).thenReturn(true);
		Game g = new Game(mockPlayer, mockHouse);
		int retValue = g.doSomething("D");
		verify(mockPlayer, times(1)).drink();
		assertEquals(retValue, 1);
	}

	// Tests Game.doSomething() with the input 'd' with winning conditions
	@Test
	public void testDoSomethingDrinkWinLowercase() {
		House mockHouse = mock(House.class);
		Player mockPlayer = mock(Player.class);
		// Stub out Player.drink()
		when(mockPlayer.drink()).thenReturn(true);
		Game g = new Game(mockPlayer, mockHouse);
		int retValue = g.doSomething("d");
		verify(mockPlayer, times(1)).drink();
		assertEquals(retValue, 1);
	}

	// Tests Game.doSomething() with the input 'D' with losing conditions
	@Test
	public void testDoSomethingDrinkLose() {
		House mockHouse = mock(House.class);
		Player mockPlayer = mock(Player.class);
		// Stub out Player.drink()
		when(mockPlayer.drink()).thenReturn(false);
		Game g = new Game(mockPlayer, mockHouse);
		int retValue = g.doSomething("D");
		verify(mockPlayer, times(1)).drink();
		assertEquals(retValue, -1);
	}

	// Tests Game.doSomething() with the input 'H'
	@Test
	public void testDoSomethingHelp() {
		fail("Not implemented");
	}

	// Tests Game.doSomething() with the input 'h'
	@Test
	public void testDoSomethingHelpLowercase() {
		fail("Not implemented");
	}
	
	@Test
	public void testDoSomethingInvalid() {
		House mockHouse = mock(House.class);
		Game g = new Game(new Player(), mockHouse);
		int retValue = g.doSomething("a");
		verifyZeroInteractions(mockHouse);
		
		assertEquals(retValue, 0);
	}
}
