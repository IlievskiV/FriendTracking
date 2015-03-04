<?php
	session_start();
include 'db_open.php';
	if(!isset($_SESSION["username"])) {
		header("Location: login.php");		
	}
?>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="./favicon.ico">

    <title>Friend Tracker</title>

    <!-- Bootstrap core CSS -->
    <link href="./css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap theme -->
    <link href="./css/bootstrap-theme.min.css" rel="stylesheet">

    <style>
    	.navbar-ft {
    		border-color: #080808;
			background-color: #B452CD;
    	}

    	.navbar-ft a {
    		color: #fff;
    	}

		.navbar-ft .active a {
			background-color: #eee;
    		color: #7A378B;
    	}

    	.navbar-ft a:hover {
    		color: #000;
    	}

    </style>
  </head>

  <body role="document">

    <!-- Fixed navbar -->
    <div class="navbar navbar-ft navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">
          <img src="./logo.png" width="32" height="32" alt="logo" />
          Friend Tracker</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li><a href="index.php">My Profile</a></li>
            <li><a href="friends.php">My Friends</a></li>
            <li class="active"><a href="requests.php">Friend Requests</a></li>
            <li><a href="addfriend.php">Add Friend</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li><a>Welcome <?php echo $_SESSION["username"]; ?></a></li>
            <li><a href="logout.php">Logout</a></li>
          </ul>
        </div>
      </div>

      </div>
    </div>

    <div class="container theme-showcase" role="main">


    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>
