package com.example.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemSetLists {

	private ArrayList<Item> earlyGameItems;
	private ArrayList<Item> midGameLosingItems;
	private ArrayList<Item> midGameWinningItems;
	private ArrayList<Item> lateGameItems;
	private ArrayList<Item> startingItems;
	
	
	public ArrayList<Item> getEarlyGameItems() {
		return earlyGameItems;
	}
	public void setEarlyGameItems(ArrayList<Item> earlyGameItems) {
		this.earlyGameItems = earlyGameItems;
	}
	public ArrayList<Item> getMidGameLosingItems() {
		return midGameLosingItems;
	}
	public void setMidGameLosingItems(ArrayList<Item> midGameLosingItems) {
		this.midGameLosingItems = midGameLosingItems;
	}
	public ArrayList<Item> getMidGameWinningItems() {
		return midGameWinningItems;
	}
	public void setMidGameWinningItems(ArrayList<Item> midGameWinningItems) {
		this.midGameWinningItems = midGameWinningItems;
	}
	public ArrayList<Item> getLateGameItems() {
		return lateGameItems;
	}
	public void setLateGameItems(ArrayList<Item> lateGameItems) {
		this.lateGameItems = lateGameItems;
	}
	public ArrayList<Item> getStartingItems() {
		return startingItems;
	}
	public void setStartingItems(ArrayList<Item> startingItems) {
		this.startingItems = startingItems;
	}
	
	
	
}
