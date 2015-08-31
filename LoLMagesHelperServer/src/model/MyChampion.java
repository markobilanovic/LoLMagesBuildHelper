package model;

import java.util.HashMap;

public class MyChampion extends BaseChampion {

	private HashMap<Long, OtherChampion> lanerEnemies;
	private HashMap<Long, OtherChampion> otherEnemies;
	private HashMap<Long, OtherChampion> allies;
	
	public MyChampion()
	{
		lanerEnemies = new HashMap<Long, OtherChampion>();
		otherEnemies = new HashMap<Long, OtherChampion>();
		allies = new HashMap<Long, OtherChampion>();
	}
	
	public HashMap<Long, OtherChampion> getLanerEnemies() {
		return lanerEnemies;
	}

	public HashMap<Long, OtherChampion> getOtherEnemies() {
		return otherEnemies;
	}

	public HashMap<Long, OtherChampion> getAllies() {
		return allies;
	}

	
	
	
}
