package com.sky;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.sky.beans.Tile;
import com.sky.service.SkyService;
import com.sky.service.SkyServiceImpl;

/**
 * Root resource (exposed at "schedules" path)
 */
@Path("/schedules")
public class CMSApi {

	@Inject
	private SkyService skyService;
	
	public CMSApi() {
		
	}
	public CMSApi(SkyService skyService) {
		this.skyService = skyService;
	}

    /**
     * 
     * @return
     */
    @GET
    @Path("tile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTile(@QueryParam("id") String id) {
    	
    	Tile returnedTile = skyService.getTile(id);//Return this really
    	
    	//return Response.status(Status.ACCEPTED).entity(tile).build();
    	return Response.ok(returnedTile, MediaType.APPLICATION_JSON).build();
    	//return null;
    }
    
    /**
     * Main target method for processing Tiles.
     * Will try and insert them into a Schedule if all the conditions are met:
     *  
     * @param tile
     * @return
     */
    @POST
    @Path("/tile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveTile(@Valid Tile tile) {
    	
    	//TODO Build response
    	System.out.println("SERVER BEING CALLED" + tile);
    	System.out.println("SERVER BEING CALLED");
    	
    	//String response = skyService.saveTileInSchedule(tile);
    	
    	return Response.ok().build();
    	
    }
}
