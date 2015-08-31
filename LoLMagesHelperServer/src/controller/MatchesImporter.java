package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;


import model.RepositoryOfChampions;
import no.stelar7.api.l4j.L4J;
import no.stelar7.api.l4j.basic.LibraryException;
import no.stelar7.api.l4j.basic.Server;
import no.stelar7.api.l4j.dto.match.Frame;
import no.stelar7.api.l4j.dto.match.MatchDetail;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




public class MatchesImporter {


	private JSONParser parser = null;
	private RepositoryOfChampions repo = null;
	private int matchCount = 0;
	
	private int exportFolder = 1;
	private Server getServerFromString(String serverName)
	{
		Server server = null;

		if(serverName.equals("BR"))
			server = Server.BR;
		else if(serverName.equals("EUNE"))
			server = Server.EUNE;
		else if(serverName.equals("EUW"))
			server = Server.EUW;
		else if(serverName.equals("KR"))
			server = Server.KR;
		else if(serverName.equals("LAN"))
			server = Server.LAN;
		else if(serverName.equals("LAS"))
			server = Server.LAS;
		else if(serverName.equals("NA"))
			server = Server.NA;
		else if(serverName.equals("OCE"))
			server = Server.OCE;
		else if(serverName.equals("RU"))
			server = Server.RU;
		else if(serverName.equals("TR"))
			server = Server.TR;

		return server;
	}


	private void readJSONFiles(File[] files) {
		for (File file : files) {
			if (file.isDirectory()) {
				readJSONFiles(file.listFiles());
			} else 
			{
				try {
					Server server = getServerFromString(file.getName().substring(0, file.getName().length()-5));
					repo.setRiotAPIServer(server);
					JSONArray array = (JSONArray) parser.parse(new FileReader(file.getAbsoluteFile()));
					System.out.println("Starting with file: " + file.getAbsolutePath());
					for (int i = 0; i < array.size(); i++) {
						try{
							repo.processMatch((long)array.get(i), server);
							matchCount++;
						}catch(Exception e)
						{
							System.out.println("GRESKA");
						}
						System.out.println(i);
					}

					repo.exportJSON("D:\\2\\" + String.valueOf(exportFolder));
					System.out.println("D:\\2\\" + String.valueOf(exportFolder) + "....exported " + matchCount + " of matches");
					exportFolder++;
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
		}
	}

	public MatchesImporter()
	{
		repo = new RepositoryOfChampions();
		repo.initializeRepository();

		parser = new JSONParser();

		//readJSONFiles(files);
		//File[] files = new File("D:\\RiotApiChallenge2\\LoLMagesBuildHelper\\matchesIds").listFiles();
		
		repo.importJSON("D:\\2\\all");

	}

}
