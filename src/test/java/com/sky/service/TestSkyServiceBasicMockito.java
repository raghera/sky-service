/**
 * 
 */
package com.sky.service;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.sky.beans.Schedule;
import com.sky.beans.Tile;
import com.sky.dao.SkyDao;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*; 

/**
 * Unit test for the SkyService
 * 
 * @author Ravi Aghera
 *
 */
/*
 * The System Under Test is SkyService
 * The dependency it has is SkyDao
 * We mock the dependency and tell it what to return - 
 * 	effectively stubbing it out without writing any code.  This can be done with Classes and Interfaces.
 */
public class TestSkyServiceBasicMockito {
	
	@BeforeClass
	public static void classSetUp() {
		
	}
	
	@Before
	public void setUp() {
		
	}
	
	/*
	 * Example of TDD without using a TestRunner. 
	 */
	@Test
	public void testSaveTileInSchedule() {

		Tile tile = new Tile(2, "testTile", "testSky", new Date(), null);
		Schedule schedule = new Schedule(1L, new Date(), Arrays.asList(tile),"clientId");

		//Mock the dependency to the 
		SkyDao mockDao = Mockito.mock(SkyDao.class);
		Mockito.when(mockDao.getSchedule(any(Date.class), anyString()))
			.thenReturn( schedule );
		Mockito.when(mockDao.getTile( any(Tile.class) ))
			.thenReturn(tile);

		final SkyServiceImpl service = new SkyServiceImpl(mockDao);
		final Tile returnedTile = service.getTile(new Tile());
		
		assertNotNull(returnedTile);
		assertEquals(2, returnedTile.getPosition());
		assertEquals("testTile", returnedTile.getLabel());
		assertEquals("testSky", returnedTile.getClientId());
	}
	
	@Test
	public void testGetTile() {
		
	}

}
