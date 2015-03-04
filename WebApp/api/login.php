<?php
require_once('db_open.php');

header('Content-Type: application/json');

function returnValid($con, $user_id) {
	$result = [];
	$result["tag"] = "login";
	$result["success"] = 1;
	$token = md5(uniqid(mt_rand(), true));
	$result["token"] = $token;
	$result["user"] = getUserFromID($con, $user_id);
	$result["time_created"] = $date = date('Y-m-d H:i:s');

	//TODO UPDATE DATABASE
	updateToken($con, $user_id, $token);
	echo json_encode($result);
}

function returnInvalid($error) {
	$result = [];
	$result["tag"] = "login";
	$result["success"] = 0;
	$result["error"] = $error;
	if($error == 1)
		$result["error_msg"] = "Incorrect email or password";
	if($error == 2)
		$result["error_msg"] = "Incomplete login informations";
	echo json_encode($result);
}

if (isset($_POST["username"]) && $_POST["username"] != "") {
	$username = $_POST["username"];
}
else {
	returnInvalid(2);
	die();
}

if (isset($_POST["password"]) && $_POST["password"] != "") {
	$password = $_POST["password"];
}
else {
	returnInvalid(2);
	die();
}


//escape 
$username = mysqli_real_escape_string($con, $username);
$password = mysqli_real_escape_string($con, $password);

$result = mysqli_query($con,"SELECT * FROM users WHERE username='$username' AND password='$password'");

if($row = mysqli_fetch_array($result)) {
	$user_id = $row['id'];
  	returnValid($con, $user_id);
}
else {
  	returnInvalid(1);
  	die();
}

require_once('db_close.php');
?>
