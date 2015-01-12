/**
* @author Mark Silvis
* CS 1520
* Assignment 4
* Javascript functions for hangman game
**/

var words = new Array();	// array to track words and prevent repeats
var currword;				// current word being used for this game
var guessedword;			// string of blanks that fill on a correct guess
var numguesses;				// number of guesses made
var numwrong;				// number of incorrect guesses
var lettersleft;			// number of letters left until the word has been solved

// stats
var rounds = 0.0;		// number of rounds played
var wins = 0.0;			// number of rounds won

// start new game of hangman
// removes home screen and shows game screen
function newGame()
{
	// start round
	newRound();

	// draw gallows
	var canvas = document.getElementById("game_gallows");
	var context = canvas.getContext("2d");
	context.lineWidth = 20;
	// frame top
	context.moveTo(60, 0);
	context.lineTo(250, 0);
	context.stroke();
	// frame pole
	context.moveTo(60, 0);
	context.lineTo(60, 300);
	context.stroke();
	// rope
	context.lineWidth = 4;
	context.moveTo(185, 0);
	context.lineTo(185, 30);
	context.stroke();

	// hide home screen
	$('#home').css('display', 'none');
	// display game screen
	$('#game').css('display', 'block');
}

// confirm that the player wants to start a new round
function newRoundConfirm() {
	if (lettersleft == 0 || numwrong == 7)
	{
		newRound();
	}
	else if(confirm("Quit current round and start a new one?"))
	{
		stopInput();
		newRound();
	}
}

// begin a new round of hangman
function newRound()
{
	guessedword = "";
	currword = "";
	numguesses = 0;
	numwrong = 0;
	rounds++;
	var canvas = document.getElementById("statichangman");
	canvas.width = canvas.width;
	$("#statichangman").removeClass();
	getWord(function()
	{
		for(var index = 0; index < currword.length; index++)
		{
			guessedword = guessedword + "_";
		}
		var newguessedword = "";
		for(var index = 0; index < guessedword.length; index++)
		{
			newguessedword = newguessedword + guessedword.charAt(index)+" ";
		}
		newguessedword = newguessedword + guessedword.charAt(guessedword.length);
		$("#currentword").html(newguessedword);
		lettersleft = currword.length;
	});

	$("#letters td").each(function(ind, el)
	{
		$(this).removeClass();
		$(this).unbind('click');
		$(this).html($(this).attr('id'));
		$(this).addClass("clickable");
		$(this).click(function()
			{
				pickLetter($(this).attr('id'));
			})
	});

	$("#guesstable").css("font-size", 20+"px");
	$("#totalguesses").html("Total guesses: "+numguesses);
	$("#wrongguesses").html("Incorrect guesses: "+numwrong);
	$("#pickedletterlist").html("");
}

// get a new word from the server
// recursively calls itself until a unique word is found
// and it uses a callback!
function getWord(callback)
{
	$.post("server.php", {func: "getword"}, function(data)
	{
		var randword = $(data).find('value').text();
		// word has already been used
		// recurse
		if(randword in words)
		{
			getWord(callback);
		}
		// unique word
		else
		{
			currword = randword;
			words[currword]=0;
			callback();
		}
	});
}

// handle what happens when a letter is picked
function pickLetter(id)
{
	var letterbutton = $("#"+id);
	var letter = id;
	letter = letter.toLowerCase();
	letterbutton.removeClass();
	letterbutton.addClass('unclickable');
	letterbutton.unbind('click');

	// fill in blanks with correctly guessed letter
	var fill = guessedword.split("");
	var find = currword.split("");
	var exists = false;

	for(var index = 0; index < find.length; index++)
	{
		if(find[index] == letter)
		{
			find[index] = "_";
			fill[index] = letter.toUpperCase();
			exists = true;
			lettersleft--;
		}
	}
	guessedword = fill.join("");
	currword = find.join("");
	var newguessedword = "";
	for(var index = 0; index < guessedword.length; index++)
	{
		newguessedword = newguessedword + guessedword.charAt(index)+" ";
	}
	newguessedword = newguessedword + guessedword.charAt(guessedword.length);
	$("#currentword").html(newguessedword);
	$("#pickedletterlist").append(letter.toUpperCase() + " ");
	if(!exists)
		numwrong++;
	numguesses++;
	$("#totalguesses").html("Total guesses: "+numguesses);
	$("#wrongguesses").html("Incorrect guesses: "+numwrong);

	// alert on victory
	if(lettersleft == 0)
	{
		alert("You've won!");
		wins++;
		stopInput();
	}
	
	// draw part of hangman when guess is wrong
	var canvas = document.getElementById("statichangman");
	var context = canvas.getContext("2d");
	context.lineWidth = 3;
	if(numwrong == 1)
	{		
		// head
		context.arc(150, 40, 30, 0, 2*Math.PI);
		context.closePath();
		context.stroke();
	}
	else if(numwrong == 2)
	{
		// body
		context.moveTo(150, 70);
		context.lineTo(150, 130);
		context.stroke();
	}
	else if(numwrong == 3)
	{
		// left arm
		context.moveTo(150, 80);
		context.lineTo(200, 70);
		context.stroke();
	}
	else if(numwrong == 4)
	{
		// right arm
		context.moveTo(150, 80);
		context.lineTo(100, 70);
		context.stroke()
	}
	else if(numwrong == 5)
	{
		// left leg
		context.moveTo(150, 130);
		context.lineTo(220, 170);
		context.stroke();
	}
	else if(numwrong == 6)
	{
		// right leg
		context.moveTo(150, 130);
		context.lineTo(80, 170);
		context.stroke();	
	}
	// alert on loss
	else if(numwrong == 7)
	{
		// eyes
		context.lineWidth = 1;
		// left eye
		context.moveTo(135, 30);
		context.lineTo(145, 40);
		context.stroke();
		context.moveTo(145, 30);
		context.lineTo(135, 40);
		context.stroke();
		// right eye
		context.moveTo(165, 30);
		context.lineTo(155, 40);
		context.stroke();
		context.moveTo(155, 30);
		context.lineTo(165, 40);
		context.stroke();

		// set animation
		$("#statichangman").addClass("animatehangman");

		// fill in remaining blanks
		var fill = guessedword.split("");
		var find = currword.split("");
		for(var index = 0; index < find.length; index++)
		{
			if(find[index] != "_")
			{
				fill[index] = find[index].toUpperCase();
			}
		}
		guessedword = fill.join("");
		var newguessedword = "";
		for(var index = 0; index < guessedword.length; index++)
		{
			newguessedword = newguessedword + guessedword.charAt(index)+" ";
		}
		newguessedword = newguessedword + guessedword.charAt(guessedword.length);
		$("#currentword").html(newguessedword);	
		alert("You lost this round!");
		stopInput();
	}
}

// prevents player from clicking on more letters after game is finished
function stopInput()
{
	// display results so far
	alert("Results so far\nTotal rounds played: "+rounds+"\nTotal rounds won: "+wins+"\nWin percentage: "+Math.round((wins/rounds)*100)+"%");
	$("#letters td").each(function(ind, el)
	{
		$(this).unbind('click');
	});
}
