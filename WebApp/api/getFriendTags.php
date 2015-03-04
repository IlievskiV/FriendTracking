<?php
require_once('db_open.php');

header('Content-Type: application/json; charset=utf-8');

function returnValid($tags) {
	$result = [];
	$result["tag"] = "user_tags";
	$result["success"] = 1;
	$result["user_tags"] = $tags;
	echo json_encode($result);
}

function returnInvalid($error) {
	$result = [];
	$result["tag"] = "user_tags";
	$result["success"] = 0;
	$result["error"] = $error;
	if($error == 1)
		$result["error_msg"] = "Incorrect";
	if($error == 2)
		$result["error_msg"] = "Incomplete informations";
	if($error == 3)
		$result["error_msg"] = "User not exist";
	echo json_encode($result);
}


if (isset($_POST["username"]) && $_POST["username"] != "") {
	$username = $_POST["username"];
}
else {
	returnInvalid(2);
	die();
}

if (isset($_POST["since"])) {
	$since = $_POST["since"];
}
else {
	$since = "";
}

//escape 
$username = mysqli_real_escape_string($con, $username);
$user_id = getIdFromUsername($con, $username);
if($user_id == -1) {
	returnInvalid(3);
	die();
}

$text = mysqli_real_escape_string($con, $since);

if($text == "")
	$sql="SELECT * FROM tags WHERE user_id = '$user_id'";
else
	$sql ="SELECT * FROM tags WHERE user_id = '$user_id' AND unix_timestamp(time_created)  > '$since'";

if (!($result = mysqli_query($con,$sql))) {
  returnInvalid(0);
}
else {
	$tags = array();
 	while( $row = mysqli_fetch_array($result)) {
 		$tag = [];
 		$tag["user_id"] = $row["user_id"];
 		$tag["latitude"] = $row["latitude"];
 		$tag["longitude"] = $row["longitude"];
 		$tag["text"] = $row["text"];
 		$tag["time_created"] = $row["time_created"];

 		array_push($tags, $tag);
 	}
	returnValid($tags);
}

require_once('db_close.php');
?>