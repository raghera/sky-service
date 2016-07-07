/**
 * 
 */
package com.sky;

import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sky.beans.Tile;
import com.sky.service.SkyServiceImpl;


/**
 * 
 * This demostrates how, when you have a REST Resource which has a dependency, how you 
 * can mock the dependency out in order to test it as an isolated Unit.
 * 
 * Not as just a POJO but when it is deployed within the Grizzly Http Server.
 * 
 * This is adapted from this StackOverflow example:
 * http://stackoverflow.com/questions/27430052/jersey-how-to-mock-service/27447345#27447345
 * 
 * @author Ravi Aghera
 *
 */

public class TestMockingRestResourceSky extends JerseyTest {

    @Path("/schedule")
    public static class RestResource {

        @Inject
        private SkyService skyService;

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Response getTile(@QueryParam("id") String id) {
        	System.out.println("CALLING THE SERVICE CORRECTLY");
            return Response.ok(skyService.getTile(id)).build() ;
        }
    }

    //Interface of Mock
    public static interface SkyService {
        public Tile getTile(String id);
    }

    public static class MockSkyServiceFactory implements Factory<SkyService> {
        
    	@Override
        public SkyService provide() {
            final SkyService mockedService = Mockito.mock(SkyService.class);

            Mockito.when(mockedService.getTile(Mockito.anyString())).thenReturn(new Tile(5, "label", "clientId", new Date(), new Date()));
//                    .thenAnswer(new Answer<String>() {
//                        @Override
//                        public String answer(InvocationOnMock invocation) 
//                                                       throws Throwable {
//                            String name = (String)invocation.getArguments()[0];
//                            return "Hello " + name;
//                        }
//
//                    });
            return mockedService;
        }

        @Override
        public void dispose(SkyService t) {}
    }

    @Override
    public Application configure() {
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(MockSkyServiceFactory.class)
                        .to(SkyService.class);
            }
        };
        ResourceConfig config = new ResourceConfig(RestResource.class);
        config.register(binder);
        return config;
    }

    
    /**
     * The test
     */
    @Test
    public void testMockedService() {
    	
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://localhost:9998/schedule")
                .queryParam("id", "tileId")
                .request(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_JSON).get();
        //.request(MediaType.APPLICATION_JSON)
				
        
        Assert.assertEquals(200, response.getStatus());

        Tile msg = response.readEntity(Tile.class);
        System.out.println("Message: " + msg);
        
//        Assert.assertEquals("Hello peeskillet", msg);
//        System.out.println("Message: " + msg);

        response.close();
        client.close();

    }
}
