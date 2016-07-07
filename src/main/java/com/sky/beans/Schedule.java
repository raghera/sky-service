/**
 * 
 */
package com.sky.beans;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Ravi Aghera
 *
 */
public class Schedule {

	//Unique Id for the schedule
	private Long scheduleId;
	//Each Schedule is for 1 day
	private Date date;
	private List<Tile> tileList;
	private String clientId; //Assumption that a schedule belongs to a client
	
	public Schedule() {
		//Default
	}
	
	//Allows easy construction
	public Schedule(Long scheduleId, Date date, 
			List<Tile> tileList, 
			String clientId) {
		this.scheduleId = scheduleId;
		this.date = date;
		this.tileList = tileList;
		this.clientId = clientId;
	}
	
	/**
	 * Adds a tile to the list of Tiles for this Schedule
	 * Will ONLY do this if the business requirements are met:
	 * 
	 * 	There is no other tile within this Schedule at the same
	 * 	date, time, position 
	 * @param tile
	 */
	public void addTile(Tile tile) {
		
		//Sort by position?
		//Tile tileAtPosition = tileList.get(tile.getPosition());
		//TODO Add the tile if there isn't one in the given position
		tileList.add(tile);
		
	}
	
}
