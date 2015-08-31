<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.*"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.JSONArray"%>

<!doctype html>

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="Submission for Riot API Challenge 2.0">
<meta name="author" content="Bilanovic Marko">

<title>LOL Mages Build Helper</title>

<!-- CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/business-casual.css" rel="stylesheet">
<link href="css/fm.selectator.jquery.css" rel="stylesheet" />
<link href="css/combobox.css" rel="stylesheet">
<link href="css/font-trebuchet.css" rel="stylesheet">

<!-- JS -->
<script src="js/jquery-1.11.0.min.js"></script>
<script src="js/fm.selectator.jquery.js"></script>
<script src="js/resizeUpdater.js"></script>
<script src="js/combobox.js"></script>
<script src="js/myFunctions.js"></script>
<link rel="shortcut icon"
	href="http://l3cdn.riotgames.com/releases/live/system/lol.ico">

<c:choose>
	<c:when test = '${itemSetList != null }'>
		<script>
			window.onload=toBottom;
		</script>
	</c:when>
</c:choose>
	
<c:choose>
	<c:when test = '${magesChampions == null }'>
		<jsp:forward page = "HomeServlet" />
	</c:when>
</c:choose>
	
</head>

<body>

	<!-- Navigation -->
	<nav class="navbar navbar-default ">
		<div class="container">
			<ul class="nav navbar-nav">
				<li><a href="HomeServlet">Home</a></li>
				<li><a href="about.html">About</a></li>

			</ul>
		</div>
	</nav>

	<div class="container body-content">
		<form role='form' method="get" action="CalculateItemsSet">
		<div class="row" style="margin-top: -20px">
			<div class="col-md-2 hidden-xs"></div>
			<div class="col-md-4 col-xs-6  text-center">
				<h1 style="color: #5ED437">My Champion</h2>
			</div>
			<div class="col-md-4 col-xs-6  text-center">
				<h1 style="color: #F70505">Enemy Laner</h2>
			</div>
			<div class="col-md-2 hidden-xs"></div>
		</div>

		<!--MY AND ENEMY CHAMPION COMBOBOX -->
		<div class="row">
			<div class="col-md-2 hidden-xs"></div>
			<div class="col-md-4 col-xs-6  text-center">
				<select class="select1" id="select1" name="myChampion"
					onchange="changeMyImage(this.options[this.selectedIndex].getAttribute('champion-key'));">
					<option value="">&nbsp;</option>
					<c:forEach var="mageChampion" items="${magesChampions}" >
						<c:choose>
							<c:when test = '${pickedChampions.myChampionKey == mageChampion.key }'>
								<option value="${mageChampion.id}" selected="selected"
									champion-key="${mageChampion.key}"
									data-subtitle="${mageChampion.title}"
									data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${mageChampion.key}.png' class='img-circle'>">
									${mageChampion.name}</option>
							</c:when>
							<c:when test = '${pickedChampions.myChampionKey != mageChampion.key }'>
								<option value="${mageChampion.id}"
										champion-key="${mageChampion.key}"
										data-subtitle="${mageChampion.title}"
										data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${mageChampion.key}.png' class='img-circle'>">
										${mageChampion.name}</option>
							</c:when>
						</c:choose>
					</c:forEach>
				</select> <input value="activate selectator" id="activate_selectator1"
					type="hidden">
			</div>
			<div class="col-md-4 col-xs-6  text-center">
				<select class="select1" id="select2" name="enemyLaner"
					onchange="changeEnemyImage(this.options[this.selectedIndex].getAttribute('champion-key'));">
					<option value="">&nbsp;</option>
					<c:forEach var="champion" items="${allChampions}" >
						<c:choose>
							<c:when test = '${pickedChampions.enemyLanerKey == champion.key }'>
								<option value="${champion.id}" selected="selected"
									champion-key="${champion.key}"
									data-subtitle="${champion.title}"
									data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
									${champion.name}</option>
							</c:when>
							<c:when test = '${pickedChampions.enemyLanerKey != champion.key }'>
								<option value="${champion.id}"
										champion-key="${champion.key}"
										data-subtitle="${champion.title}"
										data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
										${champion.name}</option>
							</c:when>
						</c:choose>
					</c:forEach>
				</select> <input value="activate selectator" id="activate_selectator2"
					type="hidden">
			</div>
			<div class="col-md-2 hidden-xs"></div>
		</div>


		<div class="row" style="margin-top: 10px">
			<!--ALLIES COMBOBOX-->
			<div class="col-md-2 col-xs-6">
				<h3 style="text-align: center; color: #5ED437">Allies</h3>
				<p>
					<select class="select2" id="selectAllie1" name="allie1"
						onchange="changeAllie1Image(this.options[this.selectedIndex].getAttribute('champion-key'));">
						<option value="">&nbsp;</option>
						<c:forEach var="champion" items="${allChampions}" >
							<c:choose>
								<c:when test = '${pickedChampions.allie1Key == champion.key }'>
									<option value="${champion.id}" selected="selected"
										champion-key="${champion.key}"
										data-subtitle="${champion.title}"
										data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
										${champion.name}</option>
								</c:when>
								<c:when test = '${pickedChampions.allie1Key != champion.key }'>
									<option value="${champion.id}"
											champion-key="${champion.key}"
											data-subtitle="${champion.title}"
											data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
											${champion.name}</option>
								</c:when>
							</c:choose>
						</c:forEach>
					</select> <input value="activate selectator" id="activate_selectator3"
						type="hidden">
				</p>
				<p>
					<select class="select2" id="selectAllie2" name="allie2"
						onchange="changeAllie2Image(this.options[this.selectedIndex].getAttribute('champion-key'));">
						<option value="">&nbsp;</option>
						<c:forEach var="champion" items="${allChampions}" >
							<c:choose>
								<c:when test = '${pickedChampions.allie2Key == champion.key }'>
									<option value="${champion.id}" selected="selected"
										champion-key="${champion.key}"
										data-subtitle="${champion.title}"
										data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
										${champion.name}</option>
								</c:when>
								<c:when test = '${pickedChampions.allie2Key != champion.key }'>
									<option value="${champion.id}"
											champion-key="${champion.key}"
											data-subtitle="${champion.title}"
											data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
											${champion.name}</option>
								</c:when>
							</c:choose>
						</c:forEach>
					</select> <input value="activate selectator" id="activate_selectator4"
						type="hidden">
				</p>
				<p>
					<select class="select2" id="selectAllie3" name="allie3"
						onchange="changeAllie3Image(this.options[this.selectedIndex].getAttribute('champion-key'));">
						<option value="">&nbsp;</option>
						<c:forEach var="champion" items="${allChampions}" >
							<c:choose>
								<c:when test = '${pickedChampions.allie3Key == champion.key }'>
									<option value="${champion.id}" selected="selected"
										champion-key="${champion.key}"
										data-subtitle="${champion.title}"
										data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
										${champion.name}</option>
								</c:when>
								<c:when test = '${pickedChampions.allie3Key != champion.key }'>
									<option value="${champion.id}"
											champion-key="${champion.key}"
											data-subtitle="${champion.title}"
											data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
											${champion.name}</option>
								</c:when>
							</c:choose>
						</c:forEach>
					</select> <input value="activate selectator" id="activate_selectator5"
						type="hidden">
				</p>
				<p>
					<select class="select2" id="selectAllie4" name="allie4"
						onchange="changeAllie4Image(this.options[this.selectedIndex].getAttribute('champion-key'));">
						<option value="">&nbsp;</option>
						<c:forEach var="champion" items="${allChampions}" >
							<c:choose>
								<c:when test = '${pickedChampions.allie4Key == champion.key }'>
									<option value="${champion.id}" selected="selected"
										champion-key="${champion.key}"
										data-subtitle="${champion.title}"
										data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
										${champion.name}</option>
								</c:when>
								<c:when test = '${pickedChampions.allie4Key != champion.key }'>
									<option value="${champion.id}"
											champion-key="${champion.key}"
											data-subtitle="${champion.title}"
											data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
											${champion.name}</option>
								</c:when>
							</c:choose>
						</c:forEach>
					</select>
				</p>
				<input value="activate selectator" id="activate_selectator6"
					type="hidden">
			</div>
			
			<!--VS IMAGE-->
			<div class="col-md-8 hidden-xs" style="margin-top: 15px;">
				<div class="row center-block" id="vsDiv" style="width:98%">
					<div class="col-md-6" style="width: 100%">
						<img class="imgC1" width="105%"
							src="img/frame.png"
							style="position: absolute; left: -18px;top: -22px; z-index: 1;">
					</div>
					<div class="col-md-6" style="width: 100%">
					<c:choose>
						<c:when test = '${pickedChampions.myChampionKey != null }'>
							<img id="imgA1" width="50%" src="http://ddragon.leagueoflegends.com/cdn/img/champion/splash/${pickedChampions.myChampionKey}_0.jpg"
								onerror="this.src='img/onErrorBig.jpg';" style="position: absolute; top: 0px; left: 0px; z-index: 0;transform:scale(-1,1)"/>
						</c:when>
						<c:when test = '${pickedChampions.myChampionKey == null }'>
							<img id="imgA1" width="50%" src="" onerror="this.src='img/onErrorBig2.jpg';"
								style="position: absolute; top: 0px; left: 0px; z-index: 0;transform:scale(-1,1)"/>
						</c:when>
					</c:choose>
					</div>
					<div class="col-md-6" style="width: 100%">
					<c:choose>
						<c:when test = '${pickedChampions.enemyLanerKey != null }'>
							<img id="imgB1" width="50%" src="http://ddragon.leagueoflegends.com/cdn/img/champion/splash/${pickedChampions.enemyLanerKey}_0.jpg"
							onerror="this.src='img/onErrorBig.jpg';" style="position: absolute; top:-1px; left: 50%; z-index: 0;"/>
						</c:when>
						<c:when test = '${pickedChampions.enemyLanerKey == null }'>
							<img id="imgB1" width="50%" src="" onerror="this.src='img/onErrorBig2.jpg';"
									style="position: absolute; top:-1px; left: 50%; z-index: 0;"/>
						</c:when>
					</c:choose>
					</div>
				</div>
			</div>
			
			<!--ENEMIES COMBOBOX-->
			<div class="col-md-2 col-xs-6">
				<h3 style="text-align: center; color: #F70505">Enemy</h3>
				<p>
					<select class="select2" id="selectEnemy1" name="enemy1"
						onchange="changeEnemy1Image(this.options[this.selectedIndex].getAttribute('champion-key'));">
						<option value="">&nbsp;</option>
						<c:forEach var="champion" items="${allChampions}" >
							<c:choose>
								<c:when test = '${pickedChampions.enemy1Key == champion.key }'>
									<option value="${champion.id}" selected="selected"
										champion-key="${champion.key}"
										data-subtitle="${champion.title}"
										data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
										${champion.name}</option>
								</c:when>
								<c:when test = '${pickedChampions.enemy1Key != champion.key }'>
									<option value="${champion.id}"
											champion-key="${champion.key}"
											data-subtitle="${champion.title}"
											data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
											${champion.name}</option>
								</c:when>
							</c:choose>
						</c:forEach>
					</select> <input value="activate selectator" id="activate_selectator7"
						type="hidden">
				</p>
				<p>
					<select class="select2" id="selectEnemy2" name="enemy2"
						onchange="changeEnemy2Image(this.options[this.selectedIndex].getAttribute('champion-key'));">
						<option value="">&nbsp;</option>
						<c:forEach var="champion" items="${allChampions}" >
							<c:choose>
								<c:when test = '${pickedChampions.enemy2Key == champion.key }'>
									<option value="${champion.id}" selected="selected"
										champion-key="${champion.key}"
										data-subtitle="${champion.title}"
										data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
										${champion.name}</option>
								</c:when>
								<c:when test = '${pickedChampions.enemy2Key != champion.key }'>
									<option value="${champion.id}"
											champion-key="${champion.key}"
											data-subtitle="${champion.title}"
											data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
											${champion.name}</option>
								</c:when>
							</c:choose>
						</c:forEach>
					</select> <input value="activate selectator" id="activate_selectator8"
						type="hidden">
				</p>
				<p>
					<select class="select2" id="selectEnemy3" name="enemy3"
						onchange="changeEnemy3Image(this.options[this.selectedIndex].getAttribute('champion-key'));">
						<option value="">&nbsp;</option>
						<c:forEach var="champion" items="${allChampions}" >
							<c:choose>
								<c:when test = '${pickedChampions.enemy3Key == champion.key }'>
									<option value="${champion.id}" selected="selected"
										champion-key="${champion.key}"
										data-subtitle="${champion.title}"
										data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
										${champion.name}</option>
								</c:when>
								<c:when test = '${pickedChampions.enemy3Key != champion.key }'>
									<option value="${champion.id}"
											champion-key="${champion.key}"
											data-subtitle="${champion.title}"
											data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
											${champion.name}</option>
								</c:when>
							</c:choose>
						</c:forEach>
					</select> 
					<input value="activate selectator" id="activate_selectator9" type="hidden">
				</p>
				<p>
					<select class="select2" id="selectEnemy4" name="enemy4"
						onchange="changeEnemy4Image(this.options[this.selectedIndex].getAttribute('champion-key'));">
						<option value="">&nbsp;</option>
						<c:forEach var="champion" items="${allChampions}" >
							<c:choose>
								<c:when test = '${pickedChampions.enemy4Key == champion.key }'>
									<option value="${champion.id}" selected="selected"
										champion-key="${champion.key}"
										data-subtitle="${champion.title}"
										data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
										${champion.name}</option>
								</c:when>
								<c:when test = '${pickedChampions.enemy4Key != champion.key }'>
									<option value="${champion.id}"
											champion-key="${champion.key}"
											data-subtitle="${champion.title}"
											data-left="<img src='http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${champion.key}.png' class='img-circle'>">
											${champion.name}</option>
								</c:when>
							</c:choose>
						</c:forEach>
					</select> <input value="activate selectator" id="activate_selectator10"
						type="hidden">
				</p>
			</div>
		</div>


		<!--Other Champions -->
		</br>
		<div class="row" style="padding-top: 10px">
			<div class="col-md-2 hidden-xs"></div>
			<div class="col-md-4 hidden-xs">
				<div class="row">
					<div class="col-md-3 ">
						<c:choose>
							<c:when test = '${pickedChampions.allie1Key != null }'>
								<img id="imgAllie1" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" 
								src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${pickedChampions.allie1Key}.png">
							</c:when>
							<c:when test = '${pickedChampions.allie1Key == null }'>
								<img id="imgAllie1" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" src="">
							</c:when>
						</c:choose>
						<img  height="150px" style="position: absolute; left: -20px;top:-38px; z-index: 1;" src="img/roundBorder.png" >
					</div>
					<div class="col-md-3 ">
						<c:choose>
							<c:when test = '${pickedChampions.allie2Key != null }'>
								<img id="imgAllie2" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" 
								src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${pickedChampions.allie2Key}.png">
							</c:when>
							<c:when test = '${pickedChampions.allie2Key == null }'>
								<img id="imgAllie2" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" src="">
							</c:when>
						</c:choose>
						<img  height="150px" style="position: absolute; left: -20px;top:-38px; z-index: 1;" src="img/roundBorder.png" >
					</div>
					<div class="col-md-3 ">
						<c:choose>
							<c:when test = '${pickedChampions.allie3Key != null }'>
								<img id="imgAllie3" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" 
								src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${pickedChampions.allie3Key}.png">
							</c:when>
							<c:when test = '${pickedChampions.allie3Key == null }'>
								<img id="imgAllie3" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" src="">
							</c:when>
						</c:choose>
						<img  height="150px" style="position: absolute; left: -20px;top:-38px; z-index: 1;" src="img/roundBorder.png" >
					</div>
					<div class="col-md-3 ">
						<c:choose>
							<c:when test = '${pickedChampions.allie4Key != null }'>
								<img id="imgAllie4" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" 
								src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${pickedChampions.allie4Key}.png">
							</c:when>
							<c:when test = '${pickedChampions.allie4Key == null }'>
								<img id="imgAllie4" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" src="">
							</c:when>
						</c:choose>
						<img  height="150px" style="position: absolute; left: -20px;top:-38px; z-index: 1;" src="img/roundBorder.png" >
					</div>
				</div>
			</div>
			<div class="col-md-4 hidden-xs">
				<div class="row">
					<div class="col-md-3 ">
						<c:choose>
							<c:when test = '${pickedChampions.enemy1Key != null }'>
								<img id="imgEnemy1" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" 
								src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${pickedChampions.enemy1Key}.png">
							</c:when>
							<c:when test = '${pickedChampions.enemy1Key == null }'>
								<img id="imgEnemy1" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" src="">
							</c:when>
						</c:choose>
						<img  height="150px" style="position: absolute; left: -20px;top:-38px; z-index: 1;" src="img/roundBorder.png" >
					</div>
					<div class="col-md-3 ">
						<c:choose>
							<c:when test = '${pickedChampions.enemy2Key != null }'>
								<img id="imgEnemy2" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" 
								src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${pickedChampions.enemy2Key}.png">
							</c:when>
							<c:when test = '${pickedChampions.enemy2Key == null }'>
								<img id="imgEnemy2" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" src="">
							</c:when>
						</c:choose>
						<img  height="150px" style="position: absolute; left: -20px;top:-38px; z-index: 1;" src="img/roundBorder.png" >
					</div>
					<div class="col-md-3 ">
						<c:choose>
							<c:when test = '${pickedChampions.enemy3Key != null }'>
								<img id="imgEnemy3" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" 
								src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${pickedChampions.enemy3Key}.png">
							</c:when>
							<c:when test = '${pickedChampions.enemy3Key == null }'>
								<img id="imgEnemy3" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" src="">
							</c:when>
						</c:choose>
						<img  height="150px" style="position: absolute; left: -20px;top:-38px; z-index: 1;" src="img/roundBorder.png" >
					</div>
					<div class="col-md-3 ">
						<c:choose>
							<c:when test = '${pickedChampions.enemy4Key != null }'>
								<img id="imgEnemy4" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" 
								src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/${pickedChampions.enemy4Key}.png">
							</c:when>
							<c:when test = '${pickedChampions.enemy4Key == null }'>
								<img id="imgEnemy4" height="75px" class="img-circle"
								onerror="this.src='img/random.jpg';" src="" >
							</c:when>
						</c:choose>
						<img  height="150px" style="position: absolute; left: -20px;top:-38px; z-index: 1;" src="img/roundBorder.png" >
					</div>
				</div>
			</div>
			<div class="col-md-2 hidden-xs"></div>
		</div>

		</br>
		<div class="row" style="margin-top: 15px">
			<div class="col-md-12 col-xs-12"  style="text-align: center">
				<input type="submit" value="Calculate" class="btn btn-default" name="akcija"
					data-toggle="tooltip" data-placement="bottom" onClick = "return fieldCheck()"
					title="My Champion and Enemy Laner are mandatory">
			</div>
		</div>
		</form>


<c:choose>
	<c:when test = '${itemSetList != null }'>
		<hr>
		<div class="container" style="margin-top: 25px">
			<div class="row">
				<div class="col-md-9 col-xs-9"
					style="background-image: url('img/itemsBackground.jpg')">
					<div class="row">
						<div class="col-md-12 col-xs-12" style="text-align: center">
							<h1 style="color: #5ED437">Best Item Set</h1>
						</div>
					</div>
					
					<div class="row">
						<div class="col-md-12 col-xs-12" style="background-image: url('img/topBorder.png');no-repeat;background-size: 100%;text-align: center;">
							<p style="color: white; font-size:16px;padding-top:3px">&nbsp;Starting items</p>
						</div>
					</div>
					<div class="row" style="margin-left: 0px; margin-top: 5px">
						<c:if test = "${fn:length(itemSetList.startingItems) le 0 }">
							<div class="col-md-12 col-xs-12 text-center">
								<p style="color: #5ED437">No result</p>
							</div>
						</c:if>
						<c:if test = "${fn:length(itemSetList.startingItems) gt 0 }">
							<c:forEach var="item" items="${itemSetList.startingItems}" >
								<div class="col-md-3 col-xs-3"
									style="text-align: left; ">
									<div class="row">
										<div class="col-md-4 col-xs-4">
											<img
												src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/item/${item.id}.png"></img>
										</div>
										<div class="col-md-8 col-xs-8">
											<p class="text-success" style="font-size: 16px">${item.name}</p>
											<p class="text-warning" style="font-size: 14px">${item.plaintext}</p>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:if>
					</div>
					<c:if test = "${fn:length(itemSetList.startingItems) gt 0 }">
						<div class="row" style="margin-left: 0px">
						<c:forEach var="item" items="${itemSetList.startingItems}" >
							<div class="col-md-3 col-xs-3">
								<p class="text-info" style="font-size: 16px;float: left">
								Grade = <fmt:formatNumber value="${item.grade}" maxFractionDigits="2"/></p>
							</div>
						</c:forEach>
						</div>
					</c:if>
					
					<div class="row">
						<div class="col-md-12 col-xs-12" style="background-image: url('img/topBorder.png');no-repeat;background-size: 100%;text-align: center;">
							<p style="color: white; font-size:16px;padding-top:3px">&nbsp;Early game items (<10min)</p>
						</div>
					</div>
					<div class="row" style="margin-left: 0px; margin-top: 5px">
						<c:if test = "${fn:length(itemSetList.earlyGameItems) le 0 }">
							<div class="col-md-12 col-xs-12 text-center">
								<p style="color: #5ED437">No result</p>
							</div>
						</c:if>
						<c:if test = "${fn:length(itemSetList.earlyGameItems) gt 0 }">
							<c:forEach var="item" items="${itemSetList.earlyGameItems}" >
								<div class="col-md-3 col-xs-3"
									style="text-align: left; ">
									<div class="row">
										<div class="col-md-4 col-xs-4">
											<img src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/item/${item.id}.png"></img>
										</div>
										<div class="col-md-8 col-xs-8">
											<p class="text-success" style="font-size: 16px">${item.name}</p>
											<p class="text-warning" style="font-size: 14px">${item.plaintext}</p>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:if>
					</div>
					<c:if test = "${fn:length(itemSetList.earlyGameItems) gt 0 }">
						<div class="row" style="margin-left: 0px">
						<c:forEach var="item" items="${itemSetList.earlyGameItems}" >
							<div class="col-md-3 col-xs-3">
								<p class="text-info" style="font-size: 16px;float: left">
								Grade = <fmt:formatNumber value="${item.grade}" maxFractionDigits="2"/></p>
							</div>
						</c:forEach>
						</div>
					</c:if>
					
					<div class="row" style="margin-top: 10px;">
						<div class="col-md-12 col-xs-12" style="background-image: url('img/topBorder.png');no-repeat;background-size: 100%;text-align: center;">
							<p style="color: white; font-size:16px;padding-top:3px">&nbsp;Mid game items when losing (<~22min)</p>
						</div>
					</div>
					<div class="row" style="margin-left: 0px; margin-top: 5px">
						<c:if test = "${fn:length(itemSetList.midGameLosingItems) le 0 }">
							<div class="col-md-12 col-xs-12 text-center">
								<p style="color: #5ED437">No result</p>
							</div>
						</c:if>
						<c:if test = "${fn:length(itemSetList.midGameLosingItems) gt 0 }">
							<c:forEach var="item" items="${itemSetList.midGameLosingItems}" >
								<div class="col-md-3 col-xs-3"
									style="text-align: left; ">
									<div class="row">
										<div class="col-md-4 col-xs-4">
											<img
												src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/item/${item.id}.png"></img>
										</div>
										<div class="col-md-8">
											<p class="text-success" style="font-size: 16px">${item.name}</p>
											<p class="text-warning" style="font-size: 14px">${item.plaintext}</p>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:if>
					</div>
					<c:if test = "${fn:length(itemSetList.midGameLosingItems) gt 0 }">
						<div class="row" style="margin-left: 0px">
						<c:forEach var="item" items="${itemSetList.midGameLosingItems}" >
							<div class="col-md-3 col-xs-3">
								<p class="text-info" style="font-size: 16px;float: left">
								Grade = <fmt:formatNumber value="${item.grade}" maxFractionDigits="2"/></p>
							</div>
						</c:forEach>
						</div>
					</c:if>
					
					<div class="row">
						<div class="col-md-12 col-xs-12" style="background-image: url('img/topBorder.png');no-repeat;background-size: 100%;text-align: center;">
							<p style="color: white; font-size:16px;padding-top:3px">&nbsp;Mid game items when winning (<~22min)</p>
						</div>
					</div>
					<div class="row" style="margin-left: 5px; margin-top: 5px">
						<c:if test = "${fn:length(itemSetList.midGameWinningItems) le 0 }">
							<div class="col-md-12 col-xs-12 text-center">
								<p style="color: #5ED437">No result</p>
							</div>
						</c:if>
						<c:if test = "${fn:length(itemSetList.midGameWinningItems) gt 0 }">
							<c:forEach var="item" items="${itemSetList.midGameWinningItems}" >
								<div class="col-md-3 col-xs-3"
									style="text-align: left; ">
									<div class="row">
										<div class="col-md-4 col-xs-4">
											<img src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/item/${item.id}.png"></img>
										</div>
										<div class="col-md-8 col-xs-8">
											<p class="text-success" style="font-size: 16px">${item.name}</p>
											<p class="text-warning" style="font-size: 14px">${item.plaintext}</p>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:if>
					</div>
					<c:if test = "${fn:length(itemSetList.midGameWinningItems) gt 0 }">
						<div class="row" style="margin-left: 0px">
						<c:forEach var="item" items="${itemSetList.midGameWinningItems}" >
							<div class="col-md-3 col-xs-3">
								<p class="text-info" style="font-size: 16px;float: left">
								Grade = <fmt:formatNumber value="${item.grade}" maxFractionDigits="2"/></p>
							</div>
						</c:forEach>
						</div>
					</c:if>

					<div class="row">
						<div class="col-md-12 col-xs-12" style="background-image: url('img/topBorder.png');no-repeat;background-size: 100%;text-align: center;">
							<p style="color: white; font-size:16px;padding-top:3px">&nbsp;Late game items</p>
						</div>
					</div>
					<div class="row" style="margin-left: 5px; margin-top: 5px">
						<c:if test = "${fn:length(itemSetList.lateGameItems) le 0 }">
							<div class="col-md-12 col-xs-12 text-center">
								<p style="color: #5ED437">No result</p>
							</div>
						</c:if>
						<c:if test = "${fn:length(itemSetList.lateGameItems) gt 0 }">
							<c:forEach var="item" items="${itemSetList.lateGameItems}" >
								<div class="col-md-3 col-xs-3"
									style="text-align: left; ">
									<div class="row">
										<div class="col-md-4 col-xs-4">
											<img
												src="http://ddragon.leagueoflegends.com/cdn/5.16.1/img/item/${item.id}.png"></img>
										</div>
										<div class="col-md-8 col-xs-8">
											<p class="text-success" style="font-size: 16px">${item.name}</p>
											<p class="text-warning" style="font-size: 14px">${item.plaintext}</p>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:if>
					</div>
					<c:if test = "${fn:length(itemSetList.lateGameItems) gt 0 }">
						<div class="row" style="margin-left: 0px">
						<c:forEach var="item" items="${itemSetList.lateGameItems}" >
							<div class="col-md-3 col-xs-3">
								<p class="text-info" style="font-size: 16px;float: left">
								Grade = <fmt:formatNumber value="${item.grade}" maxFractionDigits="2"/></p>
							</div>
						</c:forEach>
						</div>
					</c:if>
				</div>
				

				<form role='form' method="get" action="DownloadItemSet">
					<div class="col-md-3 col-xs-3">
						<h1 class="text-info">
							You can download this item set and place it in folder:</br>
							</br>
							<pre>Riot Games\League of Legends\Config\Champions\<c:out value="${pickedChampions.myChampionKey}"/>\Recommended\</pre>
							</br> And you will have that item set loaded in your next game when you
							play <c:out value="${pickedChampions.myChampionName}"/>.
						</h1>
						<p style="text-align: center; font-size: 16px" class="text-info">
							Give your item set a name<span>
							<input type="text" name="itemSetFilename">
						</p>
						<p style="text-align: center">
							<input type="submit" value="Download" class="btn btn-default">
						</p>
						<p class="bg-info">
							IMPORTANT : File must be copied before champion select is finished or else item set won't be loaded
						</p>
					</div>
				</form>
			</div>
		</div>
		
	</c:when>
</c:choose>

	</div>




	<footer style="margin-top: 25px">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<p style="text-align: center; font-size: 14px">
						<strong><em>LoL Mages Build Helper</em></strong> isn&#39;t
						endorsed by Riot Games and doesn&#39;t reflect the views or
						opinions of Riot Games <br /> or anyone officially involved in
						producing or managing <em>League of Legends</em>.<br /> <em>League
							of Legends</em> and Riot Games are trademarks or registered trademarks
						of<br /> Riot Games, Inc. <em>League of Legends</em> &copy; Riot
						Games, Inc.
					</p>
				</div>
			</div>
		</div>
	</footer>

	<!-- jQuery -->
	<!--  <script src="js/jquery.js"></script>-->

	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>

</body>

</html>
