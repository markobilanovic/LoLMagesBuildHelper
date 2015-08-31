package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import no.stelar7.api.l4j.L4J;
import no.stelar7.api.l4j.basic.LibraryException;
import no.stelar7.api.l4j.basic.Server;
import no.stelar7.api.l4j.dto.match.Event;
import no.stelar7.api.l4j.dto.match.Frame;
import no.stelar7.api.l4j.dto.match.MatchDetail;
import no.stelar7.api.l4j.dto.match.Participant;
import no.stelar7.api.l4j.dto.match.ParticipantFrame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class RepositoryOfChampions {

	private HashMap<Long, MyChampion> mages;
	private HashMap<Long, GradedItem> apItems;
	private L4J riotAPI;
	private String apiKey = "";

	public RepositoryOfChampions()
	{
		mages = new HashMap<Long, MyChampion>();
		riotAPI = new L4J(apiKey);
		apItems = getHashMapOfItems();

	}

	public void setRiotAPIServer(Server server)
	{
		L4J.setRegion(server);
	}

	public void processMatch(long matchId, Server server)
	{
		try {
			MatchDetail match = riotAPI.getMatch(matchId, true);

			MyChampion myChampion = null;
			OtherChampion enemyLaner = null;
			ArrayList<OtherChampion> allies = new ArrayList< OtherChampion>();
			ArrayList<OtherChampion> otherEnemies = new ArrayList< OtherChampion>();

			int myChampionTeamId;
			int myChampionParticipantId;
			String myChampionLane = null;
			int enemyLanerParticipantId = 0;
			ArrayList<Integer> alliesParticipantsIds = new ArrayList<Integer>();
			ArrayList<Integer> otherEnemiesParticipantsIds = new ArrayList<Integer>();

			//FILL CHAMPIONS ATTRIBUTES
			for(Participant participant : match.getParticipants())
			{
				//IS SOLO LANE AND IS MAGE?
				if(mages.containsKey(new Long(participant.getChampionId())) && participant.getTimeline().getRole().equals("SOLO"))
				{
					myChampion = mages.get(new Long(participant.getChampionId()));
					myChampionTeamId = participant.getTeamId();
					myChampionParticipantId = participant.getParticipantId();
					myChampionLane = participant.getTimeline().getLane();

					for(Participant otherParticipant : match.getParticipants())
					{
						int otherParticipantId = otherParticipant.getParticipantId();
						if(otherParticipantId != myChampionParticipantId)
						{
							if(otherParticipant.getTeamId() == myChampionTeamId)
							{
								//allies
								OtherChampion allie = myChampion.getAllies().get(new Long(otherParticipant.getChampionId()));
								allie.incrementNumberOFAppearances();
								allies.add(allie);
								alliesParticipantsIds.add(otherParticipant.getParticipantId());
							}
							else
							{
								if(otherParticipant.getTimeline().getLane().equals(myChampionLane) &&
										otherParticipant.getTimeline().getRole().equals("SOLO"))
								{
									//enemyLaner
									enemyLaner = mages.get(myChampion.getId()).getLanerEnemies().get(new Long(otherParticipant.getChampionId()));
									enemyLaner.incrementNumberOFAppearances();
									enemyLanerParticipantId = otherParticipant.getParticipantId();
								}
								else if(!otherParticipant.getTimeline().getLane().equals(myChampionLane))
								{
									//otherEnemies
									OtherChampion otherEnemy = mages.get(myChampion.getId()).getOtherEnemies().get(new Long(otherParticipant.getChampionId()));
									otherEnemy.incrementNumberOFAppearances();
									otherEnemies.add(otherEnemy);
									otherEnemiesParticipantsIds.add(otherParticipant.getParticipantId());
								}
								//No case if its duo on same lane as myChampion
							}
						}
					}

					for(Frame frame : match.getTimeline().getFrames())
					{
						if(frame.getEvents() != null)
						{
							for(Event event : frame.getEvents())
							{
								long itemBoughtTimestamp;

								int myChampionCoinsStart = 0;
								int myChampionCoinsEnd = 0;
								int enemyLanerCoinsStart = 0;
								int enemyLanerCoinsEnd = 0;
								int myTeamCoinsStart = 0;
								int myTeamCoinsEnd = 0;
								int enemyTeamCoinsStart = 0;
								int enemyTeamCoinsEnd = 0;

								if(event.getParticipantId() == myChampionParticipantId &&
										event.getEventType().equals("ITEM_PURCHASED"))
								{
									if(enemyLaner != null && enemyLaner.getEarlyGameItems().containsKey(new Long(event.getItemId())))
									{
										int itemId = event.getItemId();
										itemBoughtTimestamp = frame.getTimestamp();

										//WRITING CURRENT STATUS
										for(ParticipantFrame participantFrame : frame.getParticipantFrames().values())
										{
											if(participantFrame.getParticipantId() == myChampionParticipantId)
											{
												myChampionCoinsStart = participantFrame.getCurrentGold() + participantFrame.getXp();
											}
											else if(participantFrame.getParticipantId() == enemyLanerParticipantId)
											{
												enemyLanerCoinsStart = participantFrame.getCurrentGold() + participantFrame.getXp();
											}
										}

										int endFrameIndex = match.getTimeline().getFrames().indexOf(frame) + 11;
										if(endFrameIndex >  match.getTimeline().getFrames().size() - 1)
											endFrameIndex =  match.getTimeline().getFrames().size() - 1;

										//WRITING STATUS IN 10 MIN
										Frame endFrame = match.getTimeline().getFrames().get(endFrameIndex);
										for(ParticipantFrame participantFrame : endFrame.getParticipantFrames().values())
										{
											if(participantFrame.getParticipantId() == myChampionParticipantId)
											{
												myChampionCoinsEnd = participantFrame.getCurrentGold() + participantFrame.getXp();
											}
											else if(participantFrame.getParticipantId() == enemyLanerParticipantId)
											{
												enemyLanerCoinsEnd = participantFrame.getCurrentGold() + participantFrame.getXp();
											}
										}

										double laningItemGrade = 0;
										if(enemyLaner != null)
											laningItemGrade = (myChampionCoinsEnd - enemyLanerCoinsEnd) - (myChampionCoinsStart - enemyLanerCoinsStart);

										if(endFrameIndex < 20)	//EARLY_GAME
										{
											if(enemyLaner != null)
											{
												enemyLaner.getEarlyGameItem(itemId).updateGrade(laningItemGrade);
											}
										}
									}
									if(apItems.containsKey(new Long(event.getItemId())))
									{
										//ITEM BOUGHT
										int itemId = event.getItemId();
										itemBoughtTimestamp = frame.getTimestamp();

										//WRITING CURRENT STATUS
										for(ParticipantFrame participantFrame : frame.getParticipantFrames().values())
										{
											if(participantFrame.getParticipantId() == myChampionParticipantId)
											{
												myChampionCoinsStart = participantFrame.getCurrentGold() + participantFrame.getXp();
												myTeamCoinsStart += participantFrame.getCurrentGold() + participantFrame.getXp();
											}
											else if(participantFrame.getParticipantId() == enemyLanerParticipantId)
											{
												enemyLanerCoinsStart = participantFrame.getCurrentGold() + participantFrame.getXp();
												enemyTeamCoinsStart += participantFrame.getCurrentGold() + participantFrame.getXp();
											}
											else if(alliesParticipantsIds.contains(participantFrame.getParticipantId()))
											{
												myTeamCoinsStart += participantFrame.getCurrentGold() + participantFrame.getXp();
											}
											else //otherEnemy
											{
												enemyTeamCoinsStart += participantFrame.getCurrentGold() + participantFrame.getXp();
											}
										}

										int endFrameIndex = match.getTimeline().getFrames().indexOf(frame) + 11;
										if(endFrameIndex >  match.getTimeline().getFrames().size() - 1)
											endFrameIndex =  match.getTimeline().getFrames().size() - 1;

										//WRITING STATUS IN 10 MIN
										Frame endFrame = match.getTimeline().getFrames().get(endFrameIndex);
										for(ParticipantFrame participantFrame : endFrame.getParticipantFrames().values())
										{
											if(participantFrame.getParticipantId() == myChampionParticipantId)
											{
												myChampionCoinsEnd = participantFrame.getCurrentGold() + participantFrame.getXp();
												myTeamCoinsEnd += participantFrame.getCurrentGold() + participantFrame.getXp();
											}
											else if(participantFrame.getParticipantId() == enemyLanerParticipantId)
											{
												enemyLanerCoinsEnd = participantFrame.getCurrentGold() + participantFrame.getXp();
												enemyTeamCoinsEnd += participantFrame.getCurrentGold() + participantFrame.getXp();
											}
											else if(alliesParticipantsIds.contains(participantFrame.getParticipantId()))
											{
												myTeamCoinsEnd += participantFrame.getCurrentGold() + participantFrame.getXp();
											}
											else //otherEnemy
											{
												enemyTeamCoinsEnd += participantFrame.getCurrentGold() + participantFrame.getXp();
											}
										}

										double laningItemGrade = 0;
										if(enemyLaner != null)
											laningItemGrade = (myChampionCoinsEnd - enemyLanerCoinsEnd) - (myChampionCoinsStart - enemyLanerCoinsStart);
										double teamItemGrade = ((myTeamCoinsEnd - enemyTeamCoinsEnd) - (myTeamCoinsStart - enemyTeamCoinsStart)) / 5;
										boolean wasLosingLane = false;
										if(myChampionCoinsStart - enemyLanerCoinsStart < 0)
											wasLosingLane = true;

										if(endFrameIndex < 20)	//EARLY_GAME
										{
											if(enemyLaner != null)
											{
												enemyLaner.getEarlyGameItem(itemId).updateGrade(laningItemGrade);
											}
										}
										else if(endFrameIndex >= 20 && endFrameIndex < 32)//MID_GAME
										{
											if(wasLosingLane)
											{
												if(enemyLaner != null)
												{
													enemyLaner.getMidGameLosingItem(itemId).updateGrade(laningItemGrade + teamItemGrade);
												}

												for(OtherChampion champ : allies)
												{
													champ.getMidGameLosingItem(itemId).updateGrade(laningItemGrade + teamItemGrade);
												}

												for(OtherChampion champ : otherEnemies)
												{
													champ.getMidGameLosingItem(itemId).updateGrade(laningItemGrade + teamItemGrade);
												}
											}
											else
											{
												if(enemyLaner != null)
												{
													enemyLaner.getMidGameWinningItem(itemId).updateGrade(laningItemGrade + teamItemGrade);
												}

												for(OtherChampion champ : allies)
												{
													champ.getMidGameWinningItem(itemId).updateGrade(laningItemGrade + teamItemGrade);
												}

												for(OtherChampion champ : otherEnemies)
												{
													champ.getMidGameWinningItem(itemId).updateGrade(laningItemGrade + teamItemGrade);
												}
											}
										}
										else	//LATE_GAME
										{
											if(enemyLaner != null)
											{
												enemyLaner.getLateGameItem(itemId).updateGrade(laningItemGrade + teamItemGrade);
											}

											for(OtherChampion champ : allies)
											{
												champ.getLateGameItem(itemId).updateGrade(laningItemGrade + teamItemGrade);
											}

											for(OtherChampion champ : otherEnemies)
											{
												champ.getLateGameItem(itemId).updateGrade(laningItemGrade + teamItemGrade);
											}
										}
									}
									else if(event.getTimestamp() < 150000)
									{
										if(enemyLaner != null)
										{
											if(enemyLaner.getStartingItems().containsKey(new Long(event.getItemId())))
											{
												int endFrameIndex = match.getTimeline().getFrames().indexOf(frame) + 7;

												//WRITING STATUS IN 7 MIN
												Frame endFrame = match.getTimeline().getFrames().get(endFrameIndex);
												for(ParticipantFrame participantFrame : endFrame.getParticipantFrames().values())
												{
													if(participantFrame.getParticipantId() == myChampionParticipantId)
													{
														myChampionCoinsEnd = participantFrame.getCurrentGold() + participantFrame.getXp();
													}
													else if(participantFrame.getParticipantId() == enemyLanerParticipantId)
													{
														enemyLanerCoinsEnd = participantFrame.getCurrentGold() + participantFrame.getXp();
													}
												}
												double laningItemGrade = myChampionCoinsEnd - enemyLanerCoinsEnd;
												enemyLaner.getStartingItem(event.getItemId()).updateGrade(laningItemGrade);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (LibraryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initializeRepository()
	{
		try{
			BufferedReader reader = null;
			try {
				URL url = new URL("https://global.api.pvp.net/api/lol/static-data/eune/v1.2/champion?champData=tags&api_key=6d866a5a-716c-4b87-8b32-7d6080aa3770");
				reader = new BufferedReader(new InputStreamReader(url.openStream()));
				JSONParser parser1 = new JSONParser();
				JSONObject a1 = (JSONObject) parser1.parse(reader);

				JSONObject data = (JSONObject)a1.get("data");
				initMages(data);
				initOtherChampions(data);
				initItems();
			}
			finally 
			{
				reader.close();
			} 
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initMages(JSONObject data)
	{
		Iterator iter = data.values().iterator();
		while(iter.hasNext())
		{
			JSONObject champion = (JSONObject)iter.next();
			MyChampion myChamp = new MyChampion();
			myChamp.setId((long)champion.get("id"));
			myChamp.setName((String)champion.get("name"));
			myChamp.setKey((String)champion.get("key"));
			if(((JSONArray)champion.get("tags")).contains("Mage"))
			{
				mages.put(myChamp.getId(), myChamp);
			}
		}
	}

	private void initOtherChampions(JSONObject data)
	{
		Iterator iter = data.values().iterator();
		while(iter.hasNext())
		{
			JSONObject champion = (JSONObject)iter.next();

			for(MyChampion mage : mages.values())
			{
				OtherChampion enemyLaner = new OtherChampion();
				enemyLaner.setId((long)champion.get("id"));
				enemyLaner.setName((String)champion.get("name"));
				enemyLaner.setKey((String)champion.get("key"));
				mage.getLanerEnemies().put(enemyLaner.getId(), enemyLaner);
			}

			for(MyChampion mage : mages.values())
			{
				OtherChampion otherEnemy = new OtherChampion();
				otherEnemy.setId((long)champion.get("id"));
				otherEnemy.setName((String)champion.get("name"));
				otherEnemy.setKey((String)champion.get("key"));
				mage.getOtherEnemies().put(otherEnemy.getId(), otherEnemy);
			}

			for(MyChampion mage : mages.values())
			{
				OtherChampion allieChamp = new OtherChampion();
				allieChamp.setId((long)champion.get("id"));
				allieChamp.setName((String)champion.get("name"));
				allieChamp.setKey((String)champion.get("key"));
				mage.getAllies().put(allieChamp.getId(), allieChamp);
			}
		}
	}

	private void initItems()
	{
		for(MyChampion mage : mages.values())
		{
			for(OtherChampion allie : mage.getAllies().values())
			{
				for(GradedItem item : getListOfItems())
					allie.addMidGameLosingItem(item);
				for(GradedItem item : getListOfItems())
					allie.addMidGameWinningItem(item);
				for(GradedItem item : getListOfItems())
					allie.addLateGameItem(item);
				for(GradedItem item : getListOfStartingItems())
					allie.addStartingItem(item);
			}

			for(OtherChampion lanerEnemy : mage.getLanerEnemies().values())
			{
				for(GradedItem item : getListOfEarlyGameItems())
					lanerEnemy.addEarlyGameItem(item);
				for(GradedItem item : getListOfItems())
					lanerEnemy.addMidGameLosingItem(item);
				for(GradedItem item : getListOfItems())
					lanerEnemy.addMidGameWinningItem(item);
				for(GradedItem item : getListOfItems())
					lanerEnemy.addLateGameItem(item);
				for(GradedItem item : getListOfStartingItems())
					lanerEnemy.addStartingItem(item);
			}

			for(OtherChampion otherEnemy : mage.getOtherEnemies().values())
			{
				for(GradedItem item : getListOfItems())
					otherEnemy.addMidGameLosingItem(item);
				for(GradedItem item : getListOfItems())
					otherEnemy.addMidGameWinningItem(item);
				for(GradedItem item : getListOfItems())
					otherEnemy.addLateGameItem(item);
				for(GradedItem item : getListOfStartingItems())
					otherEnemy.addStartingItem(item);
			}
		}
	}

	private HashMap<Long, GradedItem> getHashMapOfItems()
	{
		HashMap<Long, GradedItem> apItems = new HashMap<Long, GradedItem>();
		for(GradedItem item : getListOfItems())
		{
			apItems.put(item.getId(), item);
		}
		return apItems;
	}

	private ArrayList<GradedItem> getListOfItems()
	{
		ArrayList<GradedItem> apItems = new ArrayList<GradedItem>();

		GradedItem rabadon = new GradedItem();
		rabadon.setId(new Long(3089));
		rabadon.setName("Rabadon's Deathcap");
		apItems.add(rabadon);

		GradedItem zhonya = new GradedItem();
		zhonya.setId(new Long(3157));
		zhonya.setName("Zhonya's Hourglass");
		apItems.add(zhonya);

		GradedItem luden = new GradedItem();
		luden.setId(new Long(3285));
		luden.setName("Luden's Echo");
		apItems.add(luden);

		GradedItem rylai = new GradedItem();
		rylai.setId(new Long(3116));
		rylai.setName("Rylai's Crystal Scepter");
		apItems.add(rylai);

		GradedItem archangel = new GradedItem();
		archangel.setId(new Long(3003));
		archangel.setName("Archangel's Staff");
		apItems.add(archangel);

		GradedItem rodOfAges = new GradedItem();
		rodOfAges.setId(new Long(3027));
		rodOfAges.setName("Rod of Ages");
		apItems.add(rodOfAges);

		GradedItem liandry = new GradedItem();
		liandry.setId(new Long(3151));
		liandry.setName("Liandry's Torment");
		apItems.add(liandry);

		GradedItem voidStaff = new GradedItem();
		voidStaff.setId(new Long(3135));
		voidStaff.setName("Void Staff");
		apItems.add(voidStaff);

		GradedItem nashorsTooth = new GradedItem();
		nashorsTooth.setId(new Long(3115));
		nashorsTooth.setName("Nashor's Tooth");
		apItems.add(nashorsTooth);

		GradedItem willOfTheAncients = new GradedItem();
		willOfTheAncients.setId(new Long(3152));
		willOfTheAncients.setName("Will of the Ancients");
		apItems.add(willOfTheAncients);

		GradedItem morellonomicon = new GradedItem();
		morellonomicon.setId(new Long(3165));
		morellonomicon.setName("Morellonomicon");
		apItems.add(morellonomicon);

		GradedItem athenes = new GradedItem();
		athenes.setId(new Long(3174));
		athenes.setName("Athene's Unholy Grail");
		apItems.add(athenes);

		GradedItem abyssalScepter = new GradedItem();
		abyssalScepter.setId(new Long(3001));
		abyssalScepter.setName("Abyssal Scepter");
		apItems.add(abyssalScepter);

		GradedItem bootsOfSpeed = new GradedItem();
		bootsOfSpeed.setId(new Long(1001));
		bootsOfSpeed.setName("Boots of Speed");
		apItems.add(bootsOfSpeed);

		GradedItem sorcerersShoes = new GradedItem();
		sorcerersShoes.setId(new Long(3020));
		sorcerersShoes.setName("Sorcerer's Shoes");
		apItems.add(sorcerersShoes);

		GradedItem ionianBootsOfLucidity = new GradedItem();
		ionianBootsOfLucidity.setId(new Long(3158));
		ionianBootsOfLucidity.setName("Ionian Boots of Lucidity");
		apItems.add(ionianBootsOfLucidity);

		return apItems;
	}

	private ArrayList<GradedItem> getListOfStartingItems()
	{
		ArrayList<GradedItem> startingItems = new ArrayList<GradedItem>();

		GradedItem bootsOfSpeed = new GradedItem();
		bootsOfSpeed.setId(new Long(1001));
		bootsOfSpeed.setName("Boots of Speed");
		startingItems.add(bootsOfSpeed);

		GradedItem doransRing = new GradedItem();
		doransRing.setId(new Long(1056));
		doransRing.setName("Doran's Ring");
		startingItems.add(doransRing);

		GradedItem doransSheild = new GradedItem();
		doransSheild.setId(new Long(1054));
		doransSheild.setName("Doran's Sheild");
		startingItems.add(doransSheild);

		GradedItem clothArmor = new GradedItem();
		clothArmor.setId(new Long(1029));
		clothArmor.setName("Cloth Armor");
		startingItems.add(clothArmor);

		GradedItem amplifyingTome = new GradedItem();
		amplifyingTome.setId(new Long(1052));
		amplifyingTome.setName("Amplifying Tome");
		startingItems.add(amplifyingTome);

		/*		
		GradedItem manaPotion = new GradedItem();
		manaPotion.setId(new Long(2004));
		manaPotion.setName("Mana Potion");
		startingItems.add(manaPotion);

		GradedItem healthPotion = new GradedItem();
		healthPotion.setId(new Long(2003));
		healthPotion.setName("Health Potion");
		startingItems.add(healthPotion);
		 */
		return startingItems;
	}

	private ArrayList<GradedItem> getListOfEarlyGameItems()
	{
		ArrayList<GradedItem> earlyGameItems = new ArrayList<GradedItem>();

		earlyGameItems.addAll(getListOfItems());

		GradedItem nlr = new GradedItem();
		nlr.setId(new Long(1058));
		nlr.setName("Needlessly Large Rod");
		earlyGameItems.add(nlr);

		GradedItem chalice = new GradedItem();
		chalice.setId(new Long(3028));
		chalice.setName("Chalice of Harmony");
		earlyGameItems.add(chalice);

		GradedItem idol = new GradedItem();
		idol.setId(new Long(3114));
		idol.setName("Forbidden Idol");
		earlyGameItems.add(idol);

		GradedItem guise = new GradedItem();
		guise.setId(new Long(3136));
		guise.setName("Haunting Guise");
		earlyGameItems.add(guise);

		GradedItem tear = new GradedItem();
		tear.setId(new Long(3070));
		tear.setName("Tear of the Goddess");
		earlyGameItems.add(tear);

		GradedItem blastingWand = new GradedItem();
		blastingWand.setId(new Long(1026));
		blastingWand.setName("Blasting Wand");
		earlyGameItems.add(blastingWand);

		GradedItem codex = new GradedItem();
		codex.setId(new Long(3108));
		codex.setName("Fiendish Codex");
		earlyGameItems.add(codex);

		GradedItem catalyst = new GradedItem();
		catalyst.setId(new Long(3010));
		catalyst.setName("Catalyst the Protector");
		earlyGameItems.add(catalyst);

		GradedItem sheen = new GradedItem();
		sheen.setId(new Long(3057));
		sheen.setName("Sheen");
		earlyGameItems.add(sheen);

		GradedItem wisp = new GradedItem();
		wisp.setId(new Long(3113));
		wisp.setName("Aether Wisp");
		earlyGameItems.add(wisp);

		return earlyGameItems;
	}


	public void exportJSON(String folderPath)
	{
		for(MyChampion myChamp : mages.values())
		{
			JSONObject jsonChamp = new JSONObject();
			jsonChamp.put("enemyLaners", getJSONChampions(myChamp.getLanerEnemies().values()));
			jsonChamp.put("allyChampions", getJSONChampions(myChamp.getAllies().values()));
			jsonChamp.put("otherEnemies", getJSONChampions(myChamp.getOtherEnemies().values()));
			jsonChamp.put("name", myChamp.getName());
			jsonChamp.put("key", myChamp.getKey());
			jsonChamp.put("id", myChamp.getId());

			try {
				FileWriter file = new FileWriter(folderPath + "\\" + jsonChamp.get("id") + ".json");
				file.write(jsonChamp.toJSONString());
				file.flush();
				file.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private JSONArray getJSONItems(Collection<GradedItem> collection)
	{
		JSONArray jsonItemsArray = new JSONArray();
		for(GradedItem item : collection)
		{
			JSONObject jsonItem = new JSONObject();
			jsonItem.put("boughtCount", item.getBoughtCount());
			jsonItem.put("grade", item.getGrade());
			jsonItem.put("key", item.getName());
			jsonItem.put("id", item.getId());
			jsonItemsArray.add(jsonItem);
		}
		return jsonItemsArray;
	}

	private JSONArray getJSONChampions(Collection<OtherChampion> collection)
	{
		JSONArray jsonChampions = new JSONArray();
		for(OtherChampion enemyLaner : collection)
		{
			JSONObject champ = new JSONObject();

			champ.put("earlyGameItems", getJSONItems(enemyLaner.getEarlyGameItems().values()));
			champ.put("midGameLosingItems", getJSONItems(enemyLaner.getMidGameLosingItems().values()));
			champ.put("midGameWinningItems", getJSONItems(enemyLaner.getMidGameWinningItems().values()));
			champ.put("lateGameItems", getJSONItems(enemyLaner.getLateGameItems().values()));
			champ.put("startingItems", getJSONItems(enemyLaner.getStartingItems().values()));

			champ.put("numOfAppearances", enemyLaner.getNumberOfAppearances());
			//champ.put("champType", enemyLaner.getChampType().toString());
			champ.put("name", enemyLaner.getName());
			champ.put("key", enemyLaner.getKey());
			champ.put("id", enemyLaner.getId());

			jsonChampions.add(champ);
		}
		return jsonChampions;
	}


	private void updateItemsHashmapWithJSONArray(HashMap<Long, GradedItem> hashmap, JSONArray jsonArray)
	{
		Iterator iter = jsonArray.iterator();
		while(iter.hasNext())
		{
			JSONObject jsonGradedItem = (JSONObject)iter.next();
			GradedItem item = hashmap.get(new Long((long)jsonGradedItem.get("id")));
			item.setBoughtCount( ( (Long)jsonGradedItem.get("boughtCount") ).intValue() + item.getBoughtCount());
			item.updateGrade((double)jsonGradedItem.get("grade"));
		}
	}

	private void updateChampionHashmapWithJSONArray(HashMap<Long, OtherChampion> hashmap, JSONArray jsonArray)
	{
		Iterator iter = jsonArray.iterator();
		while(iter.hasNext())
		{
			JSONObject jsonOtherChampion = (JSONObject)iter.next();
			OtherChampion champion = hashmap.get(new Long((long)jsonOtherChampion.get("id")));
			champion.setNumberOfAppearances(((Long)jsonOtherChampion.get("numOfAppearances")).intValue());
			updateItemsHashmapWithJSONArray(champion.getEarlyGameItems(), (JSONArray)jsonOtherChampion.get("earlyGameItems"));
			updateItemsHashmapWithJSONArray(champion.getMidGameLosingItems(), (JSONArray)jsonOtherChampion.get("midGameLosingItems"));
			updateItemsHashmapWithJSONArray(champion.getMidGameWinningItems(), (JSONArray)jsonOtherChampion.get("midGameWinningItems"));
			updateItemsHashmapWithJSONArray(champion.getLateGameItems(), (JSONArray)jsonOtherChampion.get("lateGameItems"));
			updateItemsHashmapWithJSONArray(champion.getStartingItems(), (JSONArray)jsonOtherChampion.get("startingItems"));
		}
	}

	public void importJSON(String folderPath)
	{
		try {
			File folder = new File(folderPath);
			File[] listOfFiles = folder.listFiles();
			JSONParser parser = new JSONParser();
			for (int i = 0; i < listOfFiles.length; i++) 
			{
				JSONObject jsonRoot = (JSONObject) parser.parse(new FileReader( folderPath + "\\" + listOfFiles[i].getName()));
				MyChampion rootChamp = mages.get(new Long((long)jsonRoot.get("id")));
				updateChampionHashmapWithJSONArray(rootChamp.getLanerEnemies(), (JSONArray)jsonRoot.get("enemyLaners"));
				updateChampionHashmapWithJSONArray(rootChamp.getAllies(), (JSONArray)jsonRoot.get("allyChampions"));
				updateChampionHashmapWithJSONArray(rootChamp.getOtherEnemies(), (JSONArray)jsonRoot.get("otherEnemies"));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



		public HashMap<Long, MyChampion> getMages() {
			return mages;
		}






	}
