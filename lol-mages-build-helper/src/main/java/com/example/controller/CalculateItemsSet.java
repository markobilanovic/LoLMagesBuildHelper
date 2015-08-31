package com.example.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.example.model.ItemSetLists;
import com.example.model.Item;
import com.example.model.ItemSet;
import com.example.model.PickedChampions;

public class CalculateItemsSet extends HttpServlet {

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			String akcija = req.getParameter("akcija");
			if(akcija.equals("Calculate"))
			{
				String myChampId = req.getParameter("myChampion");
				String enemyLanerId = req.getParameter("enemyLaner");
				String allie1Id = req.getParameter("allie1");
				String allie2Id = req.getParameter("allie2");
				String allie3Id = req.getParameter("allie3");
				String allie4Id = req.getParameter("allie4");
				String enemy1Id = req.getParameter("enemy1");
				String enemy2Id = req.getParameter("enemy2");
				String enemy3Id = req.getParameter("enemy3");
				String enemy4Id = req.getParameter("enemy4");
				
				String enemyLanerKey = "";
				String enemy1Key = "";
				String enemy2Key = "";
				String enemy3Key = "";
				String enemy4Key = "";
				String allie1Key = "";
				String allie2Key = "";
				String allie3Key = "";
				String allie4Key = "";
				String myChamponName = "";
				
				URL url = new URL("http://lol-mages-build-helper.herokuapp.com/jsondb/" + myChampId + ".json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				JSONParser parser = new JSONParser();
				JSONObject jsonRoot = (JSONObject) parser.parse(reader);

				ItemSet itemSet = initItemSet();
				JSONArray enemyLaners = (JSONArray)jsonRoot.get("enemyLaners");
				myChamponName = (String)jsonRoot.get("name");
				Iterator iter = enemyLaners.iterator();
				while(iter.hasNext())
				{
					JSONObject enemyLaner = (JSONObject)iter.next();
					if(enemyLaner.get("id").toString().equals(enemyLanerId))
					{
						enemyLanerKey = enemyLaner.get("key").toString();
						
						setGrade((JSONArray)enemyLaner.get("startingItems"), itemSet.getStartingItems());
						setGrade((JSONArray)enemyLaner.get("earlyGameItems"), itemSet.getEarlyGameItems());
						setGrade((JSONArray)enemyLaner.get("midGameLosingItems"), itemSet.getMidGameLosingItems());
						setGrade((JSONArray)enemyLaner.get("midGameWinningItems"), itemSet.getMidGameWinningItems());
						setGrade((JSONArray)enemyLaner.get("lateGameItems"), itemSet.getLateGameItems());
						
						break;
					}
				}
				
				JSONArray enemies = (JSONArray)jsonRoot.get("otherEnemies");
				iter = enemies.iterator();
				while(iter.hasNext())
				{
					JSONObject enemy = (JSONObject)iter.next();
					String id = enemy.get("id").toString();
					if(id.equals(enemy1Id)|| id.equals(enemy2Id)|| id.equals(enemy3Id)|| id.equals(enemy4Id))
					{
						if(id.equals(enemy1Id))
							enemy1Key = enemy.get("key").toString();
						else if(id.equals(enemy2Id))
							enemy2Key = enemy.get("key").toString();							
						else if(id.equals(enemy3Id))
							enemy3Key = enemy.get("key").toString();							
						else if(id.equals(enemy4Id))
							enemy4Key = enemy.get("key").toString();							
						
						setGrade((JSONArray)enemy.get("midGameLosingItems"), itemSet.getMidGameLosingItems());
						setGrade((JSONArray)enemy.get("midGameWinningItems"), itemSet.getMidGameWinningItems());
						setGrade((JSONArray)enemy.get("lateGameItems"), itemSet.getLateGameItems());
					}
				}
				
				JSONArray allyChampions = (JSONArray)jsonRoot.get("allyChampions");
				iter = allyChampions.iterator();
				while(iter.hasNext())
				{
					JSONObject ally = (JSONObject)iter.next();
					String id = ally.get("id").toString();
					if(id.equals(allie1Id)|| id.equals(allie2Id)|| id.equals(allie3Id)|| id.equals(allie4Id))
					{
						if(id.equals(allie1Id))
							allie1Key = ally.get("key").toString();
						else if(id.equals(allie2Id))
							allie2Key = ally.get("key").toString();							
						else if(id.equals(allie3Id))
							allie3Key = ally.get("key").toString();							
						else if(id.equals(allie4Id))
							allie4Key = ally.get("key").toString();							

						setGrade((JSONArray)ally.get("midGameLosingItems"), itemSet.getMidGameLosingItems());
						setGrade((JSONArray)ally.get("midGameWinningItems"), itemSet.getMidGameWinningItems());
						setGrade((JSONArray)ally.get("lateGameItems"), itemSet.getLateGameItems());
					}
				}
				
				
				//remove low grades
				removeLowGradesFromHashMap(itemSet.getStartingItems());
				removeLowGradesFromHashMap(itemSet.getEarlyGameItems());
				removeLowGradesFromHashMap(itemSet.getMidGameLosingItems());
				removeLowGradesFromHashMap(itemSet.getMidGameWinningItems());
				removeLowGradesFromHashMap(itemSet.getLateGameItems());
				
				//sort by grade
				Comparator<Item> comparator = new Comparator<Item>() {
				    public int compare(Item c1, Item c2) {
				        return (int)(c2.getGrade() - c1.getGrade()); // use your logic
				    }
				};
				ItemSetLists itemSetList = new ItemSetLists();
				itemSetList.setStartingItems(sortItems(itemSet.getStartingItems().values(), comparator));
				itemSetList.setEarlyGameItems(sortItems(itemSet.getEarlyGameItems().values(), comparator));
				itemSetList.setMidGameLosingItems(sortItems(itemSet.getMidGameLosingItems().values(), comparator));
				itemSetList.setMidGameWinningItems(sortItems(itemSet.getMidGameWinningItems().values(), comparator));
				itemSetList.setLateGameItems(sortItems(itemSet.getLateGameItems().values(), comparator));
				
				cleanItemSetResult(itemSetList);
				
				PickedChampions pickedChampions = new PickedChampions();
				pickedChampions.setMyChampionId(myChampId);
				pickedChampions.setMyChampionKey(jsonRoot.get("key").toString());
				pickedChampions.setEnemyLanerId(enemyLanerId);
				pickedChampions.setEnemyLanerKey(enemyLanerKey);
				pickedChampions.setAllie1Id(allie1Id);
				pickedChampions.setAllie1Key(allie1Key);
				pickedChampions.setAllie2Id(allie2Id);
				pickedChampions.setAllie2Key(allie2Key);
				pickedChampions.setAllie3Id(allie3Id);
				pickedChampions.setAllie3Key(allie3Key);
				pickedChampions.setAllie4Id(allie4Id);
				pickedChampions.setAllie4Key(allie4Key);
				pickedChampions.setEnemy1Id(enemy1Id);
				pickedChampions.setEnemy1Key(enemy1Key);
				pickedChampions.setEnemy2Id(enemy2Id);
				pickedChampions.setEnemy2Key(enemy2Key);
				pickedChampions.setEnemy3Id(enemy3Id);
				pickedChampions.setEnemy3Key(enemy3Key);
				pickedChampions.setEnemy4Id(enemy4Id);
				pickedChampions.setEnemy4Key(enemy4Key);
				pickedChampions.setMyChampionName(myChamponName);
				
				HttpSession session = req.getSession();
				session.setAttribute("itemSetList", itemSetList);
				session.setAttribute("pickedChampions", pickedChampions);
				
				resp.sendRedirect("home.jsp");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void cleanItemSetResult(ItemSetLists itemSetList)
	{
		cutListTo4Elements(itemSetList.getStartingItems());
		
		cleanEarlyGameItems(itemSetList);
		cutListTo4Elements(itemSetList.getEarlyGameItems());
		
		cleanMidGameItems(itemSetList);
		cutListTo4Elements(itemSetList.getMidGameLosingItems());
		cutListTo4Elements(itemSetList.getMidGameWinningItems());
		
		cleanLateGameItems(itemSetList);
		cutListTo4Elements(itemSetList.getLateGameItems());
	}
	
	
	private void cleanDoubleBoots(ArrayList<Item> list)
	{
		int bootsOfSpeedId = 1001;
		int sorcerersShoesId = 3020;
		int ionianBootsOfLucidityId = 3158;
		
		boolean bootsFound = false;
		ArrayList<Item> deleteBoots = new ArrayList<Item>();
		for(Item i : list)
		{
			if(i.getId() == bootsOfSpeedId || i.getId() == sorcerersShoesId || i.getId() == ionianBootsOfLucidityId)
			{
				if(!bootsFound)
					bootsFound = true;
				else
					deleteBoots.add(i);
			}
		}
		
		for(Item i : deleteBoots)
			list.remove(i);
	}
	
	
	private void cleanDuplicateItems(ArrayList<Item> list, ArrayList<Item> previousList)
	{
		ArrayList<Item> deleteItems = new ArrayList<Item>();
		for(Item i : previousList)
		{
			boolean specialBootCase = false;
			if(i.getId() == 3020 || i.getId() == 3158)
			{
				for(Item i2 : list)
				{
					if(i2.getId() == 1001)
					{
						deleteItems.add(i2);
						if(specialBootCase)
							break;
					}
				}
			}
			if(specialBootCase)
				continue;
			for(Item i2 : list)
			{
				if(i2.getId() == i.getId())
					deleteItems.add(i2);
			}
		}
		
		for(Item i : deleteItems)
			list.remove(i);
	}
	
	private void cleanDuplicateItemsLateGame(ArrayList<Item> list, ArrayList<Item> midGameL, ArrayList<Item> midGameW)
	{
		ArrayList<Item> deleteItems = new ArrayList<Item>();
		for(Item i : midGameL)
		{
			boolean specialBootCase = false;
			if(i.getId() == 3020 || i.getId() == 3158)
			{
				for(Item i2 : midGameW)
				{
					if(i2.getId() == 1001)
					{
						for(Item i3 : list)
						{
							if(i3.getId() == 1001)
							{
								deleteItems.add(i3);
								specialBootCase = true;
							}
							if(specialBootCase)
								break;
						}
					}
					if(specialBootCase)
						break;
				}
			}
			if(specialBootCase)
				continue;
			
			for(Item i2 : midGameW)
			{
				if(i2.getId() == i.getId())
				{
					for(Item i3 : list)
					{
						if(i3.getId() == i.getId())
							deleteItems.add(i3);
					}
				}
			}
		}
		
		for(Item i : deleteItems)
			list.remove(i);
	}
	
	
	private void cleanEarlyGameItems(ItemSetLists itemSetList)
	{
		cleanDuplicateItems(itemSetList.getEarlyGameItems(), itemSetList.getStartingItems());
		cleanDoubleBoots(itemSetList.getEarlyGameItems());
	}
	
	private void cleanMidGameItems(ItemSetLists itemSetList)
	{
		cleanDuplicateItems(itemSetList.getMidGameLosingItems(), itemSetList.getEarlyGameItems());
		cleanDuplicateItems(itemSetList.getMidGameLosingItems(), itemSetList.getStartingItems());
		cleanDoubleBoots(itemSetList.getMidGameLosingItems());
		
		cleanDuplicateItems(itemSetList.getMidGameWinningItems(), itemSetList.getEarlyGameItems());
		cleanDuplicateItems(itemSetList.getMidGameWinningItems(), itemSetList.getStartingItems());
		cleanDoubleBoots(itemSetList.getMidGameWinningItems());
		
	}
	
	private void cleanLateGameItems(ItemSetLists itemSetList)
	{
		cleanDuplicateItemsLateGame(itemSetList.getLateGameItems(), itemSetList.getMidGameLosingItems(), itemSetList.getMidGameWinningItems());
		cleanDuplicateItems(itemSetList.getLateGameItems(), itemSetList.getEarlyGameItems());
		cleanDuplicateItems(itemSetList.getLateGameItems(), itemSetList.getStartingItems());
		cleanDoubleBoots(itemSetList.getLateGameItems());
	}
	
	private void removeLowGradesFromHashMap(HashMap<Long, Item> map)
	{
		ArrayList<Long> itemsForRemove = new ArrayList<Long>();
		for(Item i : map.values())
		{
			if(i.getGrade() <= 0)
			{
				itemsForRemove.add(new Long(i.getId()));
			}
		}
		
		for(Long key : itemsForRemove)
			map.remove(key);
	}
	
	private void setGrade(JSONArray itemsArray, HashMap<Long, Item> itemSetHashmap)
	{
		Iterator itemIterator = itemsArray.iterator();
		while(itemIterator.hasNext())
		{
			JSONObject jsonItem = (JSONObject)itemIterator.next();
			Item i = itemSetHashmap.get(new Long(jsonItem.get("id").toString()));
			i.setGrade(i.getGrade() + Double.parseDouble(jsonItem.get("grade").toString()));
		}
	}
	
	private ArrayList sortItems(Collection items, Comparator comparator)
	{
		ArrayList itemsList = new ArrayList(items);
		Collections.sort(itemsList, comparator);
		return itemsList;
	}
	
	private void cutListTo4Elements(ArrayList list)
	{
		int k = list.size();
		for(int i=4; i < k; i++)
			list.remove(4);
	}
	
	private ItemSet initItemSet()
	{
		ItemSet itemSet = new ItemSet();
		for(Item i : getListOfItems())
			itemSet.getEarlyGameItems().put(new Long(i.getId()), i);
		for(Item i : getListOfItems())
			itemSet.getMidGameLosingItems().put(new Long(i.getId()), i);
		for(Item i : getListOfItems())
			itemSet.getMidGameWinningItems().put(new Long(i.getId()), i);
		for(Item i : getListOfItems())
			itemSet.getLateGameItems().put(new Long(i.getId()), i);
		for(Item i : getListOfStartingItems())
			itemSet.getStartingItems().put(new Long(i.getId()), i);
		return itemSet;
	}
	
	private ArrayList<Item> getListOfStartingItems()
	{
		ArrayList<Item> startingItems = new ArrayList<Item>();

		Item bootsOfSpeed = new Item();
		bootsOfSpeed.setId(1001);
		bootsOfSpeed.setName("Boots of Speed");
		bootsOfSpeed.setPlaintext("Slightly increases Movement Speed");
		startingItems.add(bootsOfSpeed);

		Item doransRing = new Item();
		doransRing.setId(1056);
		doransRing.setName("Doran's Ring");
		doransRing.setPlaintext("Good starting item for casters");
		startingItems.add(doransRing);

		Item doransSheild = new Item();
		doransSheild.setId(1054);
		doransSheild.setName("Doran's Sheild");
		doransSheild.setPlaintext("Good defensive starting item");
		startingItems.add(doransSheild);

		Item clothArmor = new Item();
		clothArmor.setId(1029);
		clothArmor.setName("Cloth Armor");
		clothArmor.setPlaintext("Slightly increases Armor");
		startingItems.add(clothArmor);

		Item amplifyingTome = new Item();
		amplifyingTome.setId(1052);
		amplifyingTome.setName("Amplifying Tome");
		amplifyingTome.setPlaintext("Slightly increases Ability Power");
		startingItems.add(amplifyingTome);
		
		return startingItems;
	}
	
	private ArrayList<Item> getListOfItems()
	{
		ArrayList<Item> apItems = new ArrayList<Item>();

		Item rabadon = new Item();
		rabadon.setId(3089);
		rabadon.setName("Rabadon's Deathcap");
		rabadon.setPlaintext("Massively increases Ability Power");
		apItems.add(rabadon);

		Item zhonya = new Item();
		zhonya.setId(3157);
		zhonya.setName("Zhonya's Hourglass");
		zhonya.setPlaintext("Activate to become invincible but unable to take actions");
		apItems.add(zhonya);

		Item luden = new Item();
		luden.setId(3285);
		luden.setName("Luden's Echo");
		luden.setPlaintext("Increases Ability Power and Movement Speed");
		apItems.add(luden);

		Item rylai = new Item();
		rylai.setId(3116);
		rylai.setName("Rylai's Crystal Scepter");
		rylai.setPlaintext("Abilities slow enemies");
		apItems.add(rylai);

		Item archangel = new Item();
		archangel.setId(3003);
		archangel.setName("Archangel's Staff");
		archangel.setPlaintext("Increases Ability Power based on maximum Mana");
		apItems.add(archangel);

		Item rodOfAges = new Item();
		rodOfAges.setId(3027);
		rodOfAges.setName("Rod of Ages");
		rodOfAges.setPlaintext("Greatly increases Health, Mana, and Ability Power");
		apItems.add(rodOfAges);

		Item liandry = new Item();
		liandry.setId(3151);
		liandry.setName("Liandry's Torment");
		liandry.setPlaintext("Spell damage burns enemies for a portion of their Health");
		apItems.add(liandry);

		Item voidStaff = new Item();
		voidStaff.setId(3135);
		voidStaff.setName("Void Staff");
		voidStaff.setPlaintext("Increases magic damage");
		apItems.add(voidStaff);

		Item nashorsTooth = new Item();
		nashorsTooth.setId(3115);
		nashorsTooth.setName("Nashor's Tooth");
		nashorsTooth.setPlaintext("Increases Attack Speed, Ability Power, and Cooldown Reduction");
		apItems.add(nashorsTooth);

		Item willOfTheAncients = new Item();
		willOfTheAncients.setId(3152);
		willOfTheAncients.setName("Will of the Ancients");
		willOfTheAncients.setPlaintext("Grants Spell Vamp and Ability Power");
		apItems.add(willOfTheAncients);

		Item morellonomicon = new Item();
		morellonomicon.setId(3165);
		morellonomicon.setName("Morellonomicon");
		morellonomicon.setPlaintext("Greatly increases Ability Power and Cooldown Reduction");
		apItems.add(morellonomicon);

		Item athenes = new Item();
		athenes.setId(3174);
		athenes.setName("Athene's Unholy Grail");
		athenes.setPlaintext("Restores maximum Mana on kill or assist");
		apItems.add(athenes);

		Item abyssalScepter = new Item();
		abyssalScepter.setId(3001);
		abyssalScepter.setName("Abyssal Scepter");
		abyssalScepter.setPlaintext("Reduces Magic Resist of nearby enemies");
		apItems.add(abyssalScepter);

		Item bootsOfSpeed = new Item();
		bootsOfSpeed.setId(1001);
		bootsOfSpeed.setName("Boots of Speed");
		bootsOfSpeed.setPlaintext("Slightly increases Movement Speed");
		apItems.add(bootsOfSpeed);

		Item sorcerersShoes = new Item();
		sorcerersShoes.setId(3020);
		sorcerersShoes.setName("Sorcerer's Shoes");
		sorcerersShoes.setPlaintext("Enhances Movement Speed and magic damage");
		apItems.add(sorcerersShoes);
		
		Item ionianBootsOfLucidity = new Item();
		ionianBootsOfLucidity.setId(3158);
		ionianBootsOfLucidity.setName("Ionian Boots of Lucidity");
		ionianBootsOfLucidity.setPlaintext("Increases Movement Speed and Cooldown Reduction");
		apItems.add(ionianBootsOfLucidity);
		
		Item nlr = new Item();
		nlr.setId(1058);
		nlr.setName("Needlessly Large Rod");
		nlr.setPlaintext("Greatly increases Ability Power");
		apItems.add(nlr);

		Item chalice = new Item();
		chalice.setId(3028);
		chalice.setName("Chalice of Harmony");
		chalice.setPlaintext("Greatly increases Mana Regen");
		apItems.add(chalice);
		
		Item idol = new Item();
		idol.setId(3114);
		idol.setName("Forbidden Idol");
		idol.setPlaintext("Increases Mana Regeneration and Cooldown Reduction");
		apItems.add(idol);
		
		Item guise = new Item();
		guise.setId(3136);
		guise.setName("Haunting Guise");
		guise.setPlaintext("Increases magic damage");
		apItems.add(guise);
		
		Item tear = new Item();
		tear.setId(3070);
		tear.setName("Tear of the Goddess");
		tear.setPlaintext("Increases maximum Mana as Mana is spent");
		apItems.add(tear);
		
		Item blastingWand = new Item();
		blastingWand.setId(1026);
		blastingWand.setName("Blasting Wand");
		blastingWand.setPlaintext("Moderately increases Ability Power");
		apItems.add(blastingWand);
		
		Item codex = new Item();
		codex.setId(3108);
		codex.setName("Fiendish Codex");
		codex.setPlaintext("Increases Ability Power and Cooldown Reduction");
		apItems.add(codex);

		Item catalyst = new Item();
		catalyst.setId(3010);
		catalyst.setName("Catalyst the Protector");
		catalyst.setPlaintext("Restores Health and Mana upon leveling up");
		apItems.add(catalyst);
		
		Item sheen = new Item();
		sheen.setId(3057);
		sheen.setName("Sheen");
		sheen.setPlaintext("Grants a bonus to next attack after spell cast");
		apItems.add(sheen);
		
		Item wisp = new Item();
		wisp.setId(3113);
		wisp.setName("Aether Wisp");
		wisp.setPlaintext("Increases Ability Power and Movement Speed");
		apItems.add(wisp);
		
		return apItems;
	}
	
	
	
}
