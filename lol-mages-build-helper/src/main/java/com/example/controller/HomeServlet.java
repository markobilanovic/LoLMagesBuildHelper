package com.example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HomeServlet extends HttpServlet{

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String jsonFile = null;
		BufferedReader reader = null;
		
	    try {
	    	String staticDataUrl = "https://global.api.pvp.net/api/lol/static-data/eune/v1.2/champion";
	    	String includeChampionTags = "?champData=tags";
	    	String apiKey = "&api_key="  + Helper.apiKey;
			URL url = new URL(staticDataUrl + includeChampionTags + apiKey);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			reader.close();
			
			
			JSONObject jsonData = (JSONObject)jsonObject.get("data");
			ArrayList<JSONObject> magesChampions = new ArrayList<JSONObject>();
			ArrayList<JSONObject> allChampions = new ArrayList<JSONObject>();
			Iterator iter = jsonData.values().iterator();
			while(iter.hasNext())
			{
				JSONObject champion = (JSONObject)iter.next();
				if(((JSONArray)champion.get("tags")).contains("Mage"))
				{
					magesChampions.add(champion);
				}
				allChampions.add(champion);
			}
			
			HttpSession session = req.getSession();
			
			session.setAttribute("magesChampions", magesChampions);
			session.setAttribute("allChampions", allChampions);
	    
			session.setAttribute("itemSetList", null);
			session.setAttribute("pickedChampions", null);
			
			resp.sendRedirect("home.jsp");
			
	    } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        if (reader != null)
	            reader.close();
	    }
		
	    
	   
			
	}
}
