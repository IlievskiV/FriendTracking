<?php
require_once('./api/db_open.php');
?>

<div>
GOREN DEL

</div>

<?php
function returnValid($con, $user_id) {
	$result = array();
	$result["tag"] = "register";
	$result["success"] = 1;
	$result["error"] = 0;
	$token = md5(uniqid(mt_rand(), true));
	$result["token"] = $token;
	$result["user_id"] = $user_id;
	$result["user"] = getUserFromID($con, $user_id);

	// TODO UPDATE TOKEN 
	updateToken($con, $user_id, $token);


	header("Location: login.php?success=1");
}

function returnInvalid($error) {
	$result = array();
	$result["tag"] = "register";
	$result["success"] = 0;
	$result["error"] = $error;
	if($error == 1)
		$result["error_msg"] = "Error occured in registartion";
	if($error == 2)
		$result["error_msg"] = "User already exist";
	if($error == 3)
		$result["error_msg"] = "Incomplete user informations";
	
	header("Location: login.php?success=0&register=".$error);
}

if (isset($_POST["username"])) {
	$username = $_POST["username"];
}
else {
	returnInvalid(3);
	die();
}

if (isset($_POST["password"])) {
	$password = $_POST["password"];
}
else {
	returnInvalid(3);
	die();
}

if (isset($_POST["first_name"])) {
	$first_name = $_POST["first_name"];
}
else {
	returnInvalid(3);
	die();
}

if (isset($_POST["last_name"])) {
	$last_name = $_POST["last_name"];
}
else {
	returnInvalid(3);
	die();
}

if (isset($_POST["age"])) {
	$age = $_POST["age"];
}
else {
	returnInvalid(3);
	die();
}

if (isset($_POST["hometown"])) {
	$hometown = $_POST["hometown"];
}
else {
	$hometown = "";
}

if (isset($_POST["email"])) {
	$email = $_POST["email"];
}
else {
	$email = "";
}

if (isset($_POST["gender"])) {
	$gender = $_POST["gender"];
}
else {
	returnInvalid(3);
	die();
}

//escape 
$username = mysqli_real_escape_string($con, $username);
$password = mysqli_real_escape_string($con, $password);
$fist_name = mysqli_real_escape_string($con, $first_name);
$last_name = mysqli_real_escape_string($con, $last_name);
$age = mysqli_real_escape_string($con, $age);
$hometown = mysqli_real_escape_string($con, $hometown);
$email = mysqli_real_escape_string($con, $email);
$gender = mysqli_real_escape_string($con, $gender);

$gender = $gender[0];
if($hometown == "") $hometown = null;
if($email == "") $email = null;

//First check if user exist
$result = mysqli_query($con,"SELECT * FROM users WHERE username='$username'");
//User already exist
if($row = mysqli_fetch_array($result)) {
	returnInvalid(2);
	die();
}

$sql="INSERT INTO users (username, password, first_name, last_name, age, hometown, email, gender)
VALUES ('$username', '$password', '$first_name', '$last_name', '$age', '$hometown', '$email', '$gender')";

if (!mysqli_query($con,$sql)) {
  returnInvalid(1);
  die('Error: ' . mysqli_error($con));
}
else { // Success
	$result = mysqli_query($con,"SELECT * FROM users WHERE username='$username'");
	//Get user_id
	if($row = mysqli_fetch_array($result)) {
		$user_id = $row['id'];
  		returnValid($con, $user_id);
  	}
  	else {
  		returnInvalid(2);
  		die();
  	}
}

require_once('./api/db_close.php');
?>