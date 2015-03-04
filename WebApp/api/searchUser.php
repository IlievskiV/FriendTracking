<?php
require_once('db_open.php');

header('Content-Type: application/json');

function returnValid($con, $users) {
	$result = [];
	$result["tag"] = "search_user";
	$result["success"] = 1;
	$result["users"] = $users;
	echo json_encode($result);
}

function returnInvalid($error) {
	$result = [];
	$result["tag"] = "search_user";
	$result["success"] = 0;
	$result["error"] = $error;
	if($error == 1)
		$result["error_msg"] = "Incorrect";
	if($error == 2)
		$result["error_msg"] = "Incomplete informations";
	echo json_encode($result);
}

if (isset($_POST["username"]) && $_POST["username"] != "") {
	$username = $_POST["username"];
}
else {
	returnInvalid(2);
	die();
}

if (isset($_POST["search_text"]) && $_POST["search_text"] != "") {
	$search_text = $_POST["search_text"];
}
else {
	returnInvalid(2);
	die();
}


//escape 
$username = mysqli_real_escape_string($con, $username);
$search_text = mysqli_real_escape_string($con, $search_text);

$user_id = getIdFromUsername($con, $username);

$parts = explode(" ", $search_text);
$query = "SELECT * FROM users WHERE id != '$user_id'";

if(sizeof($parts) == 1) {
	$query = "SELECT * FROM `users` WHERE id != '$user_id' AND (username like '%$parts[0]%' OR first_name like '%$parts[0]%' OR last_name like '%$parts[0]%')";
}
else {
	$query = "SELECT * FROM `users` WHERE id != '$user_id' AND CONCAT(first_name, ' ', last_name) like '%$parts[0]%$parts[1]%'";

}

$query = $query . " AND id not in (SELECT friend_id FROM `friendship` WHERE user_id = '$user_id' OR (friend_id = '$user_id' AND is_active = 0))";

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