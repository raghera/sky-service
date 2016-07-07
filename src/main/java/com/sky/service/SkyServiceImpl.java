/**
 * 
 */
package com.sky.service;

import com.sky.beans.Schedule;
import com.sky.beans.Tile;
import com.sky.dao.SkyDao;

/**
 * Sky Service which exposes RESTful api 
 * 
 * @author Ravi Aghera
 *
 */
public class SkyServiceImpl {

	private SkyDao skyDao;
	
	public SkyServiceImpl(SkyDao skyDao) {
		this.skyDao = skyDao;
	}
	
	public String saveTileInSchedule(Tile tile) {
		
		//Get Schedule based on Clientid and startDate in Tile
		Schedule schedule = skyDao.getSchedule(tile.getStartDate(), tile.getClientId());
		
		schedule.addTile(tile);
		
		return "SUCCESS";
		
	}
	
	public Tile getTile(Tile tile) {
		
		return skyDao.getTile(tile);
		
	}
	
	public Tile getTile(String id) {
		
		return skyDao.getTileById(id);
		
	}
	
}
