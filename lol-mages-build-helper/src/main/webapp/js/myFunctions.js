function changeMyImage(imgName){
	var imgA = document.getElementById("imgA1");
	imgA.src = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + imgName + "_0.jpg";	
}
	
function changeEnemyImage(imgName){
	var imgB = document.getElementById("imgB1");
	imgB.src = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + imgName + "_0.jpg";	
}
	

function changeAllie1Image(imgName){
	var imgB = document.getElementById("imgAllie1");
	imgB.src = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/" + imgName + ".png";
}

function changeAllie2Image(imgName){
	var imgB = document.getElementById("imgAllie2");
	imgB.src = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/" + imgName + ".png";
}

function changeAllie3Image(imgName){
	var imgB = document.getElementById("imgAllie3");
	imgB.src = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/" + imgName + ".png";
}

function changeAllie4Image(imgName){
	var imgB = document.getElementById("imgAllie4");
	imgB.src = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/" + imgName + ".png";
}

function changeEnemy1Image(imgName){
	var imgB = document.getElementById("imgEnemy1");
	imgB.src = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/" + imgName + ".png";
}

function changeEnemy2Image(imgName){
	var imgB = document.getElementById("imgEnemy2");
	imgB.src = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/" + imgName + ".png";
}

function changeEnemy3Image(imgName){
	var imgB = document.getElementById("imgEnemy3");
	imgB.src = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/" + imgName + ".png";
}

function changeEnemy4Image(imgName){
	var imgB = document.getElementById("imgEnemy4");
	imgB.src = "http://ddragon.leagueoflegends.com/cdn/5.16.1/img/champion/" + imgName + ".png";
}

function fieldCheck()
{
	var myChampion = document.getElementById("select1").value;
	if(myChampion == "")
	{
		alert("My Champion must be selected!");
		return false;
	}
	var enemyLaner = document.getElementById("select2").value;
	if(enemyLaner == "")
	{
		alert("Enemy Laner must be selected!");
		return false;
	}
	var allie1 = document.getElementById("selectAllie1").value;
	if(allie1 != "")
	{
		if(allie1 == myChampion)
		{
			alert("Green team has two or more same champions!");
			return false;
		}
	}
	var allie2 = document.getElementById("selectAllie2").value;
	if(allie2 != "")
	{
		if(allie2 == allie1 || allie2 == myChampion)
		{
			alert("Green team has two or more same champions!");
			return false;
		}
	}
	var allie3 = document.getElementById("selectAllie3").value;
	if(allie3 != "")
	{
		if(allie3 == allie2 || allie3 == allie1 || allie3 == myChampion)
		{
			alert("Green team has two or more same champions!");
			return false;
		}
	}
	var allie4 = document.getElementById("selectAllie4").value;
	if(allie4 != "")
	{
		if(allie4 == allie3 || allie4 == allie2 || allie4 == allie1 || allie4 == myChampion)
		{
			alert("Green team has two or more same champions!");
			return false;
		}
	}
	
	var enemy1 = document.getElementById("selectEnemy1").value;
	if(enemy1 != "")
	{
		if(enemy1 == enemyLaner)
		{
			alert("Red team has two or more same champions!");
			return false;
		}
	}
	var enemy2 = document.getElementById("selectEnemy2").value;
	if(enemy2 != "")
	{
		if(enemy2 == enemy1 || enemy2 == enemyLaner)
		{
			alert("Red team has two or more same champions!");
			return false;
		}
	}
	var enemy3 = document.getElementById("selectEnemy3").value;
	if(enemy3 != "")
	{
		if(enemy3 == enemy2 || enemy3 == enemy1 || enemy3 == enemyLaner)
		{
			alert("Red team has two or more same champions!");
			return false;
		}
	}
	var enemy4 = document.getElementById("selectEnemy4").value;
	if(enemy4 != "")
	{
		if(enemy4 == enemy3 || enemy4 == enemy2 || enemy4 == enemy1 || enemy4 == enemyLaner)
		{
			alert("Red team has two or more same champions!");
			return false;
		}
	}
}