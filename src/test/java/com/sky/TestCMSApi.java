/**
 * 
 */
package com.sky;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sky.beans.Tile;
import com.sky.service.SkyService;
import com.sky.service.SkyServiceImpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*; 

/**
 * This is a TDD Unit Test for the CMS Api
 * 
 * @author Ravi Aghera
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TestCMSApi {

	//System Under Test
	private CMSApi sut;
	
	//Mocks
	@Mock private SkyService skyService;
	
	//Other variables
	String clientId = "clientId";
	
	@Before
	public void setUp() {
		sut = new CMSApi(skyService);
	}

	/**
	 * Unit test without the http container layer
	 */
	@Test
	public void testGetJson() {
		
		//given
		Tile tile = new Tile(5, "label", "clientId", new Date(), null);
		
		//when
		sut.saveTile(tile);
		
		//then
		//verify(skyService).saveTileInSchedule(tile);
	}
	
	
	
	
}
