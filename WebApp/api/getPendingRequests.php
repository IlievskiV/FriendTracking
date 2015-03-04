<?php
require_once('db_open.php');

header('Content-Type: application/json');

function returnValid($con, $users) {
	$result = [];
	$result["tag"] = "pending_requests";
	$result["success"] = 1;
	$result["users"] = $users;
	echo json_encode($result);
}

function returnInvalid($error) {
	$result = [];
	$result["tag"] = "pending_requests";
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

//escape 
$username = mysqli_real_escape_string($con, $username);

$user_id = getIdFromUsername($con, $username);
if($user_id == -1) {
	returnInvalid(3);
	die();
}

$query = "SELECT * FROM users where id in (SELECT user_id FROM friendship WHERE friend_id = '$user_id' AND is_active = 0)";

$result = mysqli_query($con,$query);

$resultArray = array();
while($row = mysqli_fetch_array($result)) {
	$user = [];
	$user["id"] = $row["id"];
	$user["username"] = $row["username"];
	$user["first_name"] = $row["first_name"];
	$user["last_name"] = $row["last_name"];
	$user["age"] = $row["age"];
	$user["hometown"] = $row["hometown"];
	$user["gender"] = $row["gender"];
	$user["email"] = $row["email"];
 	
 	array_push($resultArray, $user);
}

returnValid($con, $resultArray);

require_once('db_close.php');
?>