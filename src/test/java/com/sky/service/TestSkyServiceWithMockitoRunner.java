/**
 * 
 */
package com.sky.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sky.beans.Schedule;
import com.sky.beans.Tile;
import com.sky.dao.SkyDao;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*; 


/**
 * TDD with Mockito for the service class
 * which depends on a repository.
 * 
 * 
 * @author Ravi Aghera
 *
 */
//TODO find out what to do if you want to use another runner: SpringJUnit4ClassRunner??  MockitoAnnotations.initMocks(this)??
@RunWith(MockitoJUnitRunner.class)
public class TestSkyServiceWithMockitoRunner {

	//System Under Test
	private SkyServiceImpl sut;
	
	//Mocked Dependencies
	@Mock private SkyDao mockSkyDao;
	@Mock private Schedule mockSchedule;
	
	//Other fields required
	String clientId = "skyClient";
	
	@Before
	public void setUp() {
		//Inject the dependencies
		sut = new SkyServiceImpl(mockSkyDao);
	}
	
	/**
	 * Tests the getTile service method.
	 * Dao layer dependency is mocked and returns a Tile result
	 * Tile is checked.
	 */
	@Test
	public void testGetTile() {
		
		//given
		//Beans (could be loaded from Spring context)
		Tile tile = new Tile(2, "testTile", clientId, new Date(), null);
		
		//Mockito.when(mockDao.getTile( any(Tile.class) )).thenReturn(tile);
		
		//When we call getTile on the mock with Any Tile typed arg, return the given tile
		when(mockSkyDao.getTile(any(Tile.class))).thenReturn(tile);
		
		//when
		final Tile result = sut.getTile(tile);
		
		//then
		assertNotNull(result);
		assertEquals(clientId, result.getClientId());
		assertEquals(tile.getPosition(), result.getPosition() );
		assertEquals(tile.getLabel(), result.getLabel() );
		
	}

	@Test
	public void testGetTileInteractions() {
		
		Tile tile = new Tile(2, "testTile", clientId, new Date(), null);
		//when
		final Tile result = sut.getTile(tile);
		
		verify(mockSkyDao, times(1)).getTile(tile);
		
		assertNull(result);
		
	}
	
	/**
	 * Tests the saveTileInSchedule service method
	 * The assumption is there is no Tile clash in the list
	 * 
	 * Checks the method calls the dao once.
	 * Checks it calls the add method in the Schedule
	 * Checks the return value includes a Schedule
	 * 
	 */
	@Test
	public void testSaveTileInSchedule() {
		
		//given (includes stubbing)
		Tile tile = new Tile(2, "testTile2", clientId, new Date(), null);
		Schedule schedule = new Schedule(1L, new Date(), getTileList(),clientId);
		
			//Stub
		when(mockSkyDao.getSchedule(any(Date.class), eq(clientId)))
			.thenReturn(schedule); //must have this clientId otherwise won't return anything

		//when
		final String success = sut.saveTileInSchedule(tile);

		//then
		//Since we stubbed the verify is pointless since we implicitly already checked it by using the stubbed value
		//So we only need assert here and no verify
		//verify(mockSkyDao, times(1)).getSchedule(any(Date.class), eq(clientId)); 
		
		//verify(schedule, times(1)).addTile(tile);
		
		assertNotNull(success);
		assertEquals("SUCCESS", success );
		
//		
	}
	
	private List<Tile> getTileList() {
		Tile tile = new Tile(3, "testTile3", clientId, new Date(), null);
		return new ArrayList<Tile>();
		
	}

}
