/**
 * 
 */
package com.sky;

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

public class TestMockingRestResource extends JerseyTest {

	//REST Resource e.g. CMSApi
    @Path("/greeting")
    public static class GreetingResource {

        @Inject
        private GreetingService greetingService;

        @GET
        @Produces(MediaType.TEXT_PLAIN)
        public String getGreeting(@QueryParam("name") String name) {
            return greetingService.getGreeting(name);
        }
    }

    //Interface of Mock
    public static interface GreetingService {
        public String getGreeting(String name);
    }

    public static class MockGreetingServiceFactory 
                                  implements Factory<GreetingService> {
        @Override
        public GreetingService provide() {
            final GreetingService mockedService
                    = Mockito.mock(GreetingService.class);
            //Can change what your mock does here and return anything you want e.g. thenReturn()
            //Although probably want to define this in your test rather than here.
            Mockito.when(mockedService.getGreeting(Mockito.anyString()))
                    .thenAnswer(new Answer<String>() {
                        @Override
                        public String answer(InvocationOnMock invocation) 
                                                       throws Throwable {
                            String name = (String)invocation.getArguments()[0];
                            return "Hello " + name;
                        }

                    });
            return mockedService;
        }

        @Override
        public void dispose(GreetingService t) {}
    }

    @Override
    public Application configure() {
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(MockGreetingServiceFactory.class)
                        .to(GreetingService.class);
            }
        };
        ResourceConfig config = new ResourceConfig(GreetingResource.class);
        config.register(binder);
        return config;
    }

    
    /**
     * The test
     */
    @Test
    public void testMockedGreetingService() {
    	
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://localhost:9998/greeting")
                .queryParam("name", "peeskillet")
                .request(MediaType.TEXT_PLAIN).get();
        
        Assert.assertEquals(200, response.getStatus());

        String msg = response.readEntity(String.class);
        Assert.assertEquals("Hello peeskillet", msg);
        System.out.println("Message: " + msg);

        response.close();
        client.close();

    }
}
