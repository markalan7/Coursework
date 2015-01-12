<?php
/**
* @author Mark Silvis
* CS 1520
* Assignment 4
* database initialization for hangman
**/

// edit these variables to change database connection settings
$host = 'localhost';        // host name
$username = 'mark';         // username
$password = NULL;           // password
$database = 'hangman';      // database name
$file = 'words.txt';        // name of file that holds words

    // connect to database
    $db = new mysqli($host, $username, $password, $database);

    if($db->connect_error):
        die("Could not connect to db: " . $db->connect_error);
    endif;

    // create words table for the game
    $query = "CREATE TABLE words (ID int AUTO_INCREMENT, Word varchar(255), PRIMARY KEY(ID))";
    $result = $db->query($query);

    // get words from file
    $words = array();
    $file = fopen($file, 'r');
    while(!feof($file))
    {
        $line = trim(fgets($file));
        if($line == "")
            break;
        $words[] = $line;
    }
    fclose($file);

    // add words to database
    foreach($words as $word)
    {
        $query = "INSERT INTO words(word) values ('$word')";
	$result = $db->query($query);
    }

    $db->close();
    echo "Done";
?>
