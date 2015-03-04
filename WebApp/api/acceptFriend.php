<?php
require_once('db_open.php');

header('Content-Type: application/json');

function returnValid() {
	$result = [];
	$result["tag"] = "accept_friend";
	$result["success"] = 1;
	echo json_encode($result);
}

function returnInvalid($error) {
	$result = [];
	$result["tag"] = "accept_friend";
	$result["success"] = 0;
	$result["error"] = $error;
	if($error == 1)
		$result["error_msg"] = "You are friends already";
	if($error == 2)
		$result["error_msg"] = "Incomplete informations";
	if($error == 3)
		$result["error_msg"] = "Some user not exist";
	if($error == 4)
		$result["error_msg"] = "Error while creating friendship";
	if($error == 5)
		$result["error_msg"] = "You can't be friend yourself";
	echo json_encode($result);
}

if (isset($_POST["username"]) && $_POST["username"] != "") {
	$username = $_POST["username"];
}
else {
	returnInvalid(2);
	die();
}

if (isset($_POST["friendname"]) && $_POST["friendname"] != "") {
	$friendname = $_POST["friendname"];
}
else {
	returnInvalid(2);
	die();
}

//escape 
$username = mysqli_real_escape_string($con, $username);
$friendname = mysqli_real_escape_string($con, $friendname);

$user_id = getIdFromUsername($con, $username);
$friend_id = getIdFromUsername($con, $friendname);

if($user_id == -1 || $friend_id == -1) {
	returnInvalid(3);
	die();
}

if($user_id == $friend_id) {
	returnInvalid(5);
	die();
}

$result = mysqli_query($con,"SELECT * FROM friendship WHERE user_id='$user_id' AND friend_id='$friend_id'");

if($row = mysqli_fetch_array($result)) {
	returnInvalid(1);
}
else {
	// create Friendship
	$sql = "INSERT INTO friendship (user_id, friend_id, is_active) VALUES ('$user_id', '$friend_id', 1)";
	if (!mysqli_query($con,$sql)) {
	  returnInvalid(4);
	  die();
	}
	else {
		$sql = "UPDATE friendship SET is_active = 1 WHERE user_id = '$friend_id' AND friend_id = '$user_id'";
		if (!mysqli_query($con,$sql)) {
		  returnInvalid(4);
		  die();
		}
		returnValid();
	}
}

require_once('db_close.php');
?>
