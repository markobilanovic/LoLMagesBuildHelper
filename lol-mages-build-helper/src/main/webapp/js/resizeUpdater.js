$(window).ready(updateHeight);
$(window).resize(updateHeight);

function updateHeight()
{
	var div = $('#imgA1');
	var width = div.width();
	var p = width * 0.7;
	div.css('height', width * 0.7);
	
	div = $('#imgB1');
	div.css('height', p);
	
	div = $('#imgC1');
	div.css('height', p);
	
	div = $('#vsDiv');
	div.css('height', p);
	
	
}

function toBottom()
{
	window.scrollTo(0, 600);
}
