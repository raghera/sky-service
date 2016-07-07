/**
 * 
 */
package com.sky;

import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sky.beans.Schedule;
import com.sky.beans.Tile;
import com.sky.service.SkyService;
import com.sky.service.SkyServiceImpl;

/**
 * Integration Test to Test Scenario 1
 * 
 * @author Ravi Aghera
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TestScenario {
	
//    private static HttpServer server;
//    private static WebTarget target;
//    private static final String PATH_SCHEDULES = "schedules";
//    private static final String PATH_TILE = "schedules/tile";

	//System Under Test
	CMSApi cmsApi;
	
	
	//Mocks
	@Mock SkyService skyService;
	
	//Other variables
	
	
	
    @BeforeClass
    public static void setUpServer() throws Exception {

    	// start the server and create the client
//        server = Main.startServer();
//        Client c = ClientBuilder.newClient();
//        target = c.target(Main.BASE_URI);
    }

    @AfterClass
    public static void tearDownServer() throws Exception {
        //server.stop();
    }
    
    @Before
    public void setUp() {
    	//Inject mocks
    	cmsApi = new CMSApi(skyService);

    }
    public void tearDown() {
    }
    

    /**
     * Test the following Scenario:
     * 		Given the schedule does not contain an existing tile at the given date, time and position for the given client
				And the schedule does contain a tile before the given date, time and position for the given client
				And the schedule does not contain a tile after the given date, time and position for the given client
				And the given tile is valid
				
			When the 'save' API is called
			
			Then the tile is added to the schedule at the given date, time and position for the given client
				And the end date and time is set to 1 Jan 2100 00:00:00 UTC
				And the end date and time of the previous tile for the given position and client is set to the start date and time of the given tile
				And the response HTTP code is 200
				And the response body contains the field 'code' with the value 'SUCESS'
     */
    @Test
    public void testScenario1() {
    	
    	Tile tile = new Tile(5, "label", "clientId", new Date(), null);
    	
    	//Schedule
    	Schedule schedule = new Schedule();
    	
    	//Given
    	
    	//When
    	
    	//Then
    	
    	
    }
    
    private Schedule getSchedule(Tile...tile) {
    	
    	return new Schedule();
    	
    }
    

}
