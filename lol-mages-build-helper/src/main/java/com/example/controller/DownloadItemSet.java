package com.example.controller;

import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.example.model.Item;
import com.example.model.ItemSetLists;
import com.example.model.PickedChampions;

public class DownloadItemSet extends HttpServlet {


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String itemSetFilename = req.getParameter("itemSetFilename").trim();
		HttpSession session = req.getSession();
		ItemSetLists itemSetList = (ItemSetLists)session.getAttribute("itemSetList");
		PickedChampions pickedChampions = (PickedChampions)session.getAttribute("pickedChampions");

		if(itemSetFilename.equals(""))
			itemSetFilename = pickedChampions.getMyChampionKey() + "_vs_" + pickedChampions.getEnemyLanerKey();

		JSONObject root = new JSONObject();
		root.put("title", itemSetFilename);
		root.put("type", "custom");
		root.put("map", "any");
		root.put("mode", "any");
		root.put("priority", false);
		root.put("champion", pickedChampions.getMyChampionKey());
		root.put("isGlobalForChampions", false);

		JSONArray blocks = new JSONArray();
		JSONObject jsonItem = null;
		if(itemSetList.getStartingItems().size() > 0)
		{
			JSONObject startGame = new JSONObject();
			startGame.put("type", "Early game");
			JSONArray startingItems = new JSONArray();
			for(Item i : itemSetList.getStartingItems())
			{
				jsonItem = new JSONObject();
				jsonItem.put("id", String.valueOf(i.getId()));
				jsonItem.put("count", 1);
				startingItems.add(jsonItem);
			}
			startGame.put("items", startingItems);
			blocks.add(startGame);
		}
		if(itemSetList.getEarlyGameItems().size() > 0)
		{
			JSONObject earlyGame = new JSONObject();
			earlyGame.put("type", "Early game");
			JSONArray earlyGameItems = new JSONArray();
			jsonItem = null;
			for(Item i : itemSetList.getEarlyGameItems())
			{
				jsonItem = new JSONObject();
				jsonItem.put("id", String.valueOf(i.getId()));
				jsonItem.put("count", 1);
				earlyGameItems.add(jsonItem);
			}
			earlyGame.put("items", earlyGameItems);
			blocks.add(earlyGame);
		}

		if(itemSetList.getMidGameLosingItems().size() > 0)
		{
			JSONObject midGameL = new JSONObject();
			midGameL.put("type", "Mid game when losing");
			JSONArray midGameLItems = new JSONArray();
			jsonItem = null;
			for(Item i : itemSetList.getMidGameLosingItems())
			{
				jsonItem = new JSONObject();
				jsonItem.put("id", String.valueOf(i.getId()));
				jsonItem.put("count", 1);
				midGameLItems.add(jsonItem);
			}
			midGameL.put("items", midGameLItems);
			blocks.add(midGameL);
		}

		if(itemSetList.getMidGameWinningItems().size() > 0)
		{
			JSONObject midGameW = new JSONObject();
			midGameW.put("type", "Mid game when winning");
			JSONArray midGameWItems = new JSONArray();
			jsonItem = null;
			for(Item i : itemSetList.getMidGameWinningItems())
			{
				jsonItem = new JSONObject();
				jsonItem.put("id", String.valueOf(i.getId()));
				jsonItem.put("count", 1);
				midGameWItems.add(jsonItem);
			}
			midGameW.put("items", midGameWItems);
			blocks.add(midGameW);
		}

		if(itemSetList.getLateGameItems().size() > 0)
		{
			JSONObject lateGame = new JSONObject();
			lateGame.put("type", "Late game");
			JSONArray lateGameItems = new JSONArray();
			jsonItem = null;
			for(Item i : itemSetList.getLateGameItems())
			{
				jsonItem = new JSONObject();
				jsonItem.put("id", String.valueOf(i.getId()));
				jsonItem.put("count", 1);
				lateGameItems.add(jsonItem);
			}
			lateGame.put("items", lateGameItems);
			blocks.add(lateGame);
		}
		root.put("blocks", blocks);

		resp.setContentType("application/json");
		resp.setHeader("Content-disposition", "attachment; filename=\""+itemSetFilename+".json\"");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setHeader("Expires", "-1");


		resp.getOutputStream().print(root.toJSONString());
	}
}
