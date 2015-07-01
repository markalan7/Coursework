import com.laboon.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class HouseTests {
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
    
    // Tests House.getCurrentRoomInfo() when the player has not moved
	@Test
	public void testGetCurrentRoomInfo() {
		House h = new House(1);
		String message = h.getCurrentRoomInfo();
		assertNotNull(message);
	}

	// Tests House.moveNorth() when there is a door to the north
	@Test
	public void testMoveNorthWithDoor() {
		Room[] rooms = { new Room(false, false, false, true, true),
				new Room(false, false, false, true, true) };
		House h = new House(rooms);
		String message_before = h.getCurrentRoomInfo();
		h.moveNorth();
		String message_after = h.getCurrentRoomInfo();
		assertFalse(message_before.equals(message_after));
	}
	
	// Tests House.moveSouth() when there is a door to the south
	@Test
	public void testMoveSouthWithDoor() {
		Room[] rooms = { new Room(false, false, false, true, false),
				new Room(false, false, false, false, true) };
		House h = new House(rooms);
		String message_before = h.getCurrentRoomInfo();
		h.moveNorth();
		h.moveSouth();
		String message_after = h.getCurrentRoomInfo();
		assertTrue(message_before.equals(message_after));
	}
	
	// Tests House.moveSouth() when there is no door to the south
	// Because the player shouldn't be able to move south, the room
	// description should be the same before and after moving
	@Test
	public void testMoveSouthNoDoor() {
		Room[] rooms = { new Room(false, false, false, false, false) };
		House h = new House(rooms);
		String message_before = h.getCurrentRoomInfo();
		h.moveSouth();
		String message_after = h.getCurrentRoomInfo();
		assertTrue(message_before.equals(message_after));
	}
	
	// Tests House.moveNorth() when there is no door to the north
	@Test
	public void testMoveNorthNoDoor() {
		Room[] rooms = { new Room(false, false, false, false, false) };
		House h = new House(rooms);
		String message_before = h.getCurrentRoomInfo();
		h.moveNorth();
		String message_after = h.getCurrentRoomInfo();
		assertTrue(message_before.equals(message_after));
	}
	
	
	// Tests House.look() in a room with all items, expecting the player to pick all of them up
	// DEPENDANT ON: Room.hasItem(), Room.hasCoffee(), Room.hasCream(), and Room.hasSugar()
	@Test
	public void testLookWithItems() {
		Room mockRoom = mock(Room.class);
		// array of one mock room object
		Room[] rooms = {mockRoom};
		Player mockPlayer = mock(Player.class);
		House h = new House(rooms);
		
		// stub out Room.hasItem()
		when(mockRoom.hasItem()).thenReturn(true);
		
		// stub out Room.hasCoffee(), Room.hasCream(), and Room.hasSugar()
		when(mockRoom.hasCoffee()).thenReturn(true);		
		when(mockRoom.hasCream()).thenReturn(true);		
		when(mockRoom.hasSugar()).thenReturn(true);

		h.look(mockPlayer, rooms[0]);
		verify(mockRoom, times(1)).hasItem();
		verify(mockPlayer, times(1)).getCoffee();
		verify(mockPlayer, times(1)).getCream();
		verify(mockPlayer, times(1)).getSugar();
	}
	
	// Tests House.look() in a room with no items
	// Same dependencies as testLookWithItem()
	@Test
	public void testLookNoItems() {
		Room mockRoom = mock(Room.class);
		// array of one mock room object
		Room[] rooms = {mockRoom};
		Player mockPlayer = mock(Player.class);
		House h = new House(rooms);

		// stub out Room.hasItem()
		when(mockRoom.hasItem()).thenReturn(false);
		
		// stub out Room.hasCoffee(), Room.hasCream(), and Room.hasSugar()
		when(mockRoom.hasCoffee()).thenReturn(false);		
		when(mockRoom.hasCream()).thenReturn(false);		
		when(mockRoom.hasSugar()).thenReturn(false);

		h.look(mockPlayer, rooms[0]);
		verify(mockRoom, times(1)).hasItem();
		verify(mockPlayer, times(0)).getCoffee();
		verify(mockPlayer, times(0)).getCream();
		verify(mockPlayer, times(0)).getSugar();	
	}
	
	// Tests House.generateRooms() to make sure the rooms have the correct items
	// Expected results: room 0 has cream, room 2 has coffee, room 4 (i.e. last room) has sugar
	@Test
	public void testGenerateRoomsItems() {
		Room[] rooms = (new House(1)).generateRooms(5);
		boolean correctCream = rooms[0].hasCream();
		boolean correctCoffee = rooms[2].hasCoffee();
		boolean correctSugar = rooms[4].hasSugar();
		assertTrue(correctCream && correctCoffee && correctSugar);
	}
	
	// Tests House.generateRooms() to make sure the house has the correct number of rooms
	@Test
	public void testGenerateRoomsNumber() {
		Room[] rooms = (new House(1)).generateRooms(5);
		assertEquals(rooms.length, 5);
	}
}
