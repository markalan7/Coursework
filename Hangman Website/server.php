<?php
/**
* @author Mark Silvis
* CS 1520
* Assignment 4
* Handles all communication with server
**/

// check what post is requesting
if ($_POST['func'] = 'getword')
	getWord();

// retrieve one random word from server
// output is in XML
function getWord()
{
	// edit these variables to change database connection settings
	$host = 'localhost';
	$username = 'mark';
	$password = NULL;
	$database = 'hangman';
	
	$db = new mysqli($host, $username, $password, $database);
	$query = "select word from words order by rand() limit 1";
	$result = $db->query($query);
	$row = $result->fetch_array();
	$word = $row["word"];
	header('Content-type: text/xml');
	echo "<?xml version='1.0' encoding='utf-8'?>";
	echo "<Word>";
	echo "<value>$word</value>";
	echo "</Word>";
	$db->close();
}
?>
