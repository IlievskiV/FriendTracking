<?php
$con = mysqli_connect("dbhost","dbusername","dbpass","database");
if (!$con) {
    die('Could not connect to database: ' . mysql_error());
}
//echo 'Connected successfully';
//$con->set_charset("utf8");

mysqli_set_charset($con, 'utf8');

function getIdFromUsername($con, $username) {
	$result = mysqli_query($con,"SELECT * FROM users WHERE username='$username'");
	//Get user_id
	if($row = mysqli_fetch_array($result)) {
		$user_id = $row['id'];
  		return $user_id;
  	}
  	else {
  		return -1;
  	}
}

function getUserFromID($con, $id) {
  $result = mysqli_query($con,"SELECT * FROM users WHERE id='$id'");
  //Get user_id
  if($row = mysqli_fetch_array($result)) {
    $obj = [];
    $obj["id"] = $row['id'];
    $obj["username"] = $row['username'];
    $obj["password"] = $row['password'];
    $obj["first_name"] = $row['first_name'];
    $obj["last_name"] = $row['last_name'];
    $obj["age"] = $row['age'];
    $obj["email"] = $row['email'];
    $obj["created_at"] = $row['created_at'];
    $obj["updated_at"] = $row['updated_at'];
    return $obj;
  }
  else {
    return [];
  }
}

function updateToken($con, $id, $token) {
  $result = mysqli_query($con,"UPDATE users SET token='$token', updated_at=now() WHERE id='$id'");
}

?>