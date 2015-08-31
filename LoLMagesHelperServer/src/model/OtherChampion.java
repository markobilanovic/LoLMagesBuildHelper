package model;

import java.util.HashMap;

public class OtherChampion extends BaseChampion {

	private int numberOfAppearances;
	private HashMap<Long, GradedItem> earlyGameItems;
	private HashMap<Long, GradedItem> midGameLosingItems;
	private HashMap<Long, GradedItem> midGameWinningItems;
	private HashMap<Long, GradedItem> lateGameItems;
	private HashMap<Long, GradedItem> startingItems;
	
	
	public OtherChampion()
	{
		earlyGameItems = new HashMap<Long, GradedItem>();
		midGameLosingItems = new HashMap<Long, GradedItem>();
		midGameWinningItems = new HashMap<Long, GradedItem>();
		lateGameItems = new HashMap<Long, GradedItem>();
		startingItems = new HashMap<Long, GradedItem>();
	}


	public int getNumberOfAppearances() {
		return numberOfAppearances;
	}


	public void setNumberOfAppearances(int numberOfAppearances) {
		this.numberOfAppearances = numberOfAppearances;
	}


	public void addEarlyGameItem(GradedItem item)
	{
		earlyGameItems.put(item.getId(), item);
	}
	
	public void addMidGameLosingItem(GradedItem item)
	{
		midGameLosingItems.put(item.getId(), item);
	}
	
	public void addMidGameWinningItem(GradedItem item)
	{
		midGameWinningItems.put(item.getId(), item);
	}
	
	public void addLateGameItem(GradedItem item)
	{
		lateGameItems.put(item.getId(), item);
	}
	
	public void addStartingItem(GradedItem item)
	{
		startingItems.put(item.getId(), item);
	}
	
	
	
	
	public GradedItem getEarlyGameItem(long id)
	{
		return earlyGameItems.get(id);
	}
	
	public GradedItem getMidGameLosingItem(long id)
	{
		return midGameLosingItems.get(id);
	}
	
	public GradedItem getMidGameWinningItem(long id)
	{
		return midGameWinningItems.get(id);
	}
	
	public GradedItem getLateGameItem(long id)
	{
		return lateGameItems.get(id);
	}
	
	public GradedItem getStartingItem(long id)
	{
		return startingItems.get(id);
	}
	
	
	
	public HashMap<Long, GradedItem> getEarlyGameItems() {
		return earlyGameItems;
	}



	public HashMap<Long, GradedItem> getMidGameLosingItems() {
		return midGameLosingItems;
	}



	public HashMap<Long, GradedItem> getMidGameWinningItems() {
		return midGameWinningItems;
	}




	public HashMap<Long, GradedItem> getLateGameItems() {
		return lateGameItems;
	}




	public HashMap<Long, GradedItem> getStartingItems() {
		return startingItems;
	}


	
	public void incrementNumberOFAppearances()
	{
		numberOfAppearances++;
	}
	
	
}


