package com.sky;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.validation.ValidationError;
import org.glassfish.jersey.servlet.WebComponent;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.sky.beans.Tile;
import com.sky.service.SkyService;
import com.sky.service.SkyServiceImpl;
import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestHttpValidation extends JerseyTest {

    private static HttpServer server;
    private static WebTarget target;
    private static final String PATH_SCHEDULES = "schedules";
    private static final String PATH_TILE = "schedules/tile";
    private static final String clientId = "skyClient";
    
    //public static final String BASE_URI = "http://localhost:8080/myapp/";
    //Default location
    public static final String BASE_URI = "http://localhost:9998/";
    
    //System Under Test
    private CMSApi cmsApi;
    
    //Mocks
    @Mock private static SkyService mockSkyService;

    /*
     * To inject mocks it is a little painful.
     * 
     * 1) You have a RESTFul Resource - in this case it's CMSApi
     * 2) Your resource has a dependency = SkyService which is represented by an Interface
     * 3) Provide a mock implementation for this interface using Factory - MockSkyServiceFactory.  
     * 		This helps to inject the implementation into your Restful Resource.
     * 		When Jersey attempts to inject dependencies or bindings, it will call this method to provide the impl
     * 4) Override the configure method to bind your dependencies into the running server
     * 		Do this by adding your Factory impl and linking it to the mock dependency type
     * 		Then add to the Server configuration
     * 5) For automatic dependency injection using HK2 annotate the dependency in your Restful Resource with @Inject
     * 
     * A decent Example is this: 
     * 
     * http://stackoverflow.com/questions/27430052/jersey-how-to-mock-service/27447345#27447345
     */
    
    public static class MockSkyServiceFactory implements Factory<SkyService> {

    	@Override
        public SkyService provide() {
	    	//final SkyService mockedService = Mockito.mock(SkyService.class);
//            Mockito.when(mockSkyService.getTile(Mockito.anyString())).thenReturn(new Tile(6, "label", "clientId", new Date(), new Date()) );
	    	return mockSkyService;
        }

		@Override
		public void dispose(SkyService arg0) {
			// TODO Auto-generated method stub
		}
    }
    
    @Override
    protected Application configure() {
    	AbstractBinder binder = new AbstractBinder() {
			
			@Override
			protected void configure() {
				bindFactory(MockSkyServiceFactory.class).to(SkyService.class);
				
			}
		};
		
		ResourceConfig config = new ResourceConfig(CMSApi.class).packages("com.sky");
		config.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		//Register your mock dependencies
	    config.register(binder);
	    return config;
    	
    }

    @Test 
    public void testGetTile() {
    	
	    Client client = ClientBuilder.newClient();
	    //target = client.target(BASE_URI);
	    
	    Mockito.when(mockSkyService.getTile(Mockito.anyString())).thenReturn(new Tile(66, "label", "clientId", new Date(), new Date()) );
	    
	    //Response response = target.path(PATH_TILE) //.queryParam("id", "id_string")
	    Response response = client.target("http://localhost:9998/schedules/tile")
	    		.queryParam("id", "id")
	    		.request( MediaType.APPLICATION_JSON, MediaType.TEXT_HTML)
	    		.accept(MediaType.APPLICATION_JSON)
	    		.accept(MediaType.TEXT_HTML)
	    		.get();
	
	    assertNotNull(response);
	    assertNotNull(response.getStatus());
	    
	    System.out.println("Response--: " + response.getStatus());
	    System.out.println("Response--: " + response.getStatusInfo());
	    System.out.println("Response--: " + response.readEntity(Tile.class));
    
    }
    
//    @Test
//    public void testGetTile() {
//    	
//    	Client client = ClientBuilder.newClient();
//    	target = client.target(BASE_URI);
//    	
//    	Invocation.Builder invocationBuilder = target.path(PATH_TILE).request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
//    	Response response = invocationBuilder.get();
//    	
//    	System.out.println("Response--: " + response.getStatus());
//    	System.out.println("Response--: " + response.readEntity(Tile.class));
//
//    	System.out.println("Response--: " +  response.getMediaType());
//    	
//    	assertEquals(Status.OK.getStatusCode(), response.getStatus());
//    	
//    }
//    
//    /**
//     * Positive test with valid inputs to check the success case
//     */
//    @Test
//    public void testSaveTile() {
//    	
//    	Client client = ClientBuilder.newClient();
//    	target = client.target(BASE_URI);
//    	
//    	//given
//    	final Invocation.Builder invocationBuilder = 
//    			target.path(PATH_TILE).request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
//    	
//    	Tile tile = new Tile(8, "PUT LABEL WITH SPACES AND 999", clientId, new Date(), null);
//    	
////    	Mockito.when(mockSkyService.saveTileInSchedule(tile)).thenReturn("SUCCESS");
//    	
//    	//when
//    	
//    	
//    	Response response = invocationBuilder.post(Entity.entity(tile, MediaType.APPLICATION_JSON_TYPE));
//
//    	
//    	//then
//    	
//    	System.out.println("Response: " + response.getStatus());
//    	System.out.println("Response: " + response.getStatusInfo());
//    	
//    	assertEquals(Status.OK.getStatusCode(), response.getStatus());
//       	
//    }
//    
//    /**
//     * Attempt an invalid request based on these rules:
//     * 	Label must not be blank
//     * 	Label must not be more than 30 characters
//     * 	Label must only contain alpha numeric characters and space
//     * 
//     */
//    @Test
//    public void testInvalidLabel() {
//    	
//    	Client client = ClientBuilder.newClient();
//    	target = client.target(BASE_URI);
//    	
//    	//Empty
//    	Tile tile = new Tile(8, null, clientId, new Date(), null);
//    	Response response = 
//    			target.path(PATH_TILE)
//    			.request(MediaType.APPLICATION_JSON)
//    			.accept(MediaType.APPLICATION_JSON)
//    			.post(Entity.entity(tile, MediaType.APPLICATION_JSON_TYPE));
//    	
//    	assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
//    	
//    	//TODO - Get errors in a method
//    	List<ValidationError> errors = response.readEntity(new GenericType<List<ValidationError>>() {});
//    	
////    	for (ValidationError validationError : errors) {
////    		System.out.println("=====================================Response Errors: " + validationError.getMessage() );
////		}
//       	assertNotNull(errors);
//       	assertEquals( errors.size(), 1 );
//       	assertNotNull(errors.get(0));
//       	assertEquals( errors.get(0).getMessage(), Tile.MESSAGE_LABEL_SIZE );
//       	
//       	System.out.println("Response Errors: " + errors.get(0).getMessage() );
//
//    	//Too many chars
//
//    	tile = new Tile(8, "qwertyuioplkjhgfdsazxcvbnmqwertyuioplkjh", clientId, new Date(), null);//40 chars
//    	response = 
//    			target.path(PATH_TILE)
//    			.request(MediaType.APPLICATION_JSON)
//    			.accept(MediaType.APPLICATION_JSON)
//    			.post(Entity.entity(tile, MediaType.APPLICATION_JSON_TYPE));
//
//    	System.out.println("Response: " + response.getStatus());
//    	System.out.println("Response: " + response.getStatus());
//    	
//    	assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
//    	//Check the number of chars was the reason
//    	
//       	errors = response.readEntity(new GenericType<List<ValidationError>>() {});
//       	assertNotNull(errors);
//       	assertEquals( errors.size(), 1 );
//       	assertNotNull(errors.get(0));
//       	assertEquals( errors.get(0).getMessage(), Tile.MESSAGE_LABEL_SIZE );
//       	
//       	System.out.println("Response Errors: " + errors.get(0).getMessage() );
//    }
//
//    /**
//     * Attempt an invalid request based on these rules:
//     * 
//     * Position must be greater than or equal to one
//     * Position must be less than or equal to eight
//     */
//    @Test
//    public void testInvalidPosition() {
//    	Client client = ClientBuilder.newClient();
//    	target = client.target(BASE_URI);
//    	
//    	//Too high
//    	Tile tile = new Tile(10, "Valid Label", clientId, new Date(), null);
//    	Response response = 
//    			target.path(PATH_TILE)
//    			.request(MediaType.APPLICATION_JSON)
//    			.accept(MediaType.APPLICATION_JSON)
//    			.post(Entity.entity(tile, MediaType.APPLICATION_JSON_TYPE));
//    	
//    	assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
//    	
//    	List<ValidationError> errors = response.readEntity(new GenericType<List<ValidationError>>() {});
//       	assertNotNull(errors);
//       	assertEquals( errors.size(), 1 );
//       	assertNotNull(errors.get(0));
//       	assertEquals( errors.get(0).getMessage(), Tile.MESSAGE_POS_SIZE );
//       	
//       	System.out.println("Response Errors: " + errors.get(0).getMessage() );
//
//    	
//    	//too low
//    	tile = new Tile(10, "Label", clientId, new Date(), null);
//    	response = 
//    			target.path(PATH_TILE)
//    			.request(MediaType.APPLICATION_JSON)
//    			.accept(MediaType.APPLICATION_JSON)
//    			.post(Entity.entity(tile, MediaType.APPLICATION_JSON_TYPE));
//    	
//    	assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
//    	
//    	errors = response.readEntity(new GenericType<List<ValidationError>>() {});
//       	assertNotNull(errors);
//       	assertEquals( errors.size(), 1 );
//       	assertNotNull(errors.get(0));
//       	assertEquals( errors.get(0).getMessage(), Tile.MESSAGE_POS_SIZE );
//    	
//    	//Negative
//    	tile = new Tile(-1, "Label", clientId, new Date(), null);
//    	response = 
//    			target.path(PATH_TILE)
//    			.request(MediaType.APPLICATION_JSON)
//    			.accept(MediaType.APPLICATION_JSON)
//    			.post(Entity.entity(tile, MediaType.APPLICATION_JSON_TYPE));
//    	
//    	assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
//    	
//    	errors = response.readEntity(new GenericType<List<ValidationError>>() {});
//       	assertNotNull(errors);
//       	assertEquals( errors.size(), 1 );
//       	assertNotNull(errors.get(0));
//       	assertEquals( errors.get(0).getMessage(), Tile.MESSAGE_POS_SIZE );
//    	
//    }
//    
//    @Test
//    public void testDate() {
//    	
//    	Calendar cal = Calendar.getInstance();
//    	cal.setTime(new Date());
//    	int day = cal.get(Calendar.DAY_OF_MONTH);
//    	int month = cal.get(Calendar.MONTH);
//    	int year = cal.get(Calendar.YEAR);
//    	int hour = cal.get(Calendar.HOUR_OF_DAY);
//    	int mins = cal.get(Calendar.MINUTE);
//    	int secs = cal.get(Calendar.SECOND);
//    	int millis = cal.get(Calendar.MILLISECOND);
//    	
//    	
//    	//THE DATE
//    	System.out.println("===============================================================THE DAY : " + day);
//    	System.out.println("===============================================================THE month : " + month);
//    	System.out.println("===============================================================THE Year : " + year);
//    	System.out.println("===============================================================THE Year : " + hour);
//    	System.out.println("===============================================================THE Year : " + mins);
//    	System.out.println("===============================================================THE Year : " + secs);
//    	System.out.println("===============================================================THE Year : " + millis);
//    	
//    }

}
