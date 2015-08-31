package com.example.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemSet {

	private HashMap<Long, Item> earlyGameItems;
	private HashMap<Long, Item> midGameLosingItems;
	private HashMap<Long, Item> midGameWinningItems;
	private HashMap<Long, Item> lateGameItems;
	private HashMap<Long, Item> startingItems;
	
	public ItemSet()
	{
		earlyGameItems = new HashMap<Long, Item>();
		midGameLosingItems = new HashMap<Long, Item>();
		midGameWinningItems = new HashMap<Long, Item>();
		lateGameItems = new HashMap<Long, Item>();
		startingItems = new HashMap<Long, Item>();
	}

	public HashMap<Long, Item> getEarlyGameItems() {
		return earlyGameItems;
	}

	public HashMap<Long, Item> getMidGameLosingItems() {
		return midGameLosingItems;
	}

	public HashMap<Long, Item> getMidGameWinningItems() {
		return midGameWinningItems;
	}

	public HashMap<Long, Item> getLateGameItems() {
		return lateGameItems;
	}

	public HashMap<Long, Item> getStartingItems() {
		return startingItems;
	}

	public void setEarlyGameItems(HashMap<Long, Item> earlyGameItems) {
		this.earlyGameItems = earlyGameItems;
	}

	public void setMidGameLosingItems(HashMap<Long, Item> midGameLosingItems) {
		this.midGameLosingItems = midGameLosingItems;
	}

	public void setMidGameWinningItems(HashMap<Long, Item> midGameWinningItems) {
		this.midGameWinningItems = midGameWinningItems;
	}

	public void setLateGameItems(HashMap<Long, Item> lateGameItems) {
		this.lateGameItems = lateGameItems;
	}

	public void setStartingItems(HashMap<Long, Item> startingItems) {
		this.startingItems = startingItems;
	}
	
	
}
