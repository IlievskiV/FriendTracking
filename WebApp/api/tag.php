<?php
require_once('db_open.php');

header('Content-Type: application/json');

function returnValid() {
	$result = [];
	$result["tag"] = "tag";
	$result["success"] = 1;
	echo json_encode($result);
}

function returnInvalid($error) {
	$result = [];
	$result["tag"] = "tag";
	$result["success"] = 0;
	$result["error"] = $error;
	if($error == 1)
		$result["error_msg"] = "Incomplete informations";
	if($error == 2)
		$result["error_msg"] = "Error while tagging";
	if($error == 3)
		$result["error_msg"] = "User not exist";
	echo json_encode($result);
}

if (isset($_POST["username"]) && $_POST["username"] != "") {
	$username = $_POST["username"];
}
else {
	returnInvalid(1);
	die();
}

if (isset($_POST["longitude"]) && $_POST["longitude"] != "") {
	$longitude = $_POST["longitude"];
}
else {
	returnInvalid(1);
	die();
}

if (isset($_POST["latitude"]) && $_POST["latitude"] != "") {
	$latitude = $_POST["latitude"];
}
else {
	returnInvalid(1);
	die();
}

if (isset($_POST["text"]) && $_POST["text"] != "") {
	$text = $_POST["text"];
}
else {
	$text = "";
}

//escape 
$username = mysqli_real_escape_string($con, $username);
$user_id = getIdFromUsername($con, $username);
if($user_id == -1) {
	returnInvalid(3);
	die();
}

$longitude = mysqli_real_escape_string($con, $longitude);
$latitude = mysqli_real_escape_string($con, $latitude);
$text = mysqli_real_escape_string($con, $text);

$sql="INSERT INTO tags (user_id, longitude, latitude, text)
VALUES ('$user_id', '$longitude', '$latitude', '$text')";

if (!mysqli_query($con,$sql)) {
  returnInvalid(2);
}
else {
	returnValid();
}

require_once('db_close.php');
?>