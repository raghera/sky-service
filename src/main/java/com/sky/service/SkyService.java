package com.sky.service;

import javax.ws.rs.core.Response;

import com.sky.beans.Tile;

public interface SkyService {

	public Tile getTile(String id);
	
	//saveTile(Tile tile);
	
}
