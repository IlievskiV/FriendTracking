<?php
	session_start();
	require_once('./api/db_open.php');

	if(isset($_SESSION["username"])) {
		header("Location: index.php");
	}

	$message = "";

	if(isset($_POST["submit"])) {
		$username = $_POST["uname"];
		$password = $_POST["pwd"];
		if($username == "" || $password == "") {
			echo "Popolnete gi site podatoci";
		}

		$username = mysqli_real_escape_string($con, $username);
		$password = mysqli_real_escape_string($con, $password);

		$result = mysqli_query($con,"SELECT * FROM users WHERE username='$username' AND password='$password'");

		if($row = mysqli_fetch_array($result)) {

			$_SESSION["username"] = $username;
			$_SESSION["first_name"] = $row["first_name"];
			$_SESSION["last_name"] = $row["last_name"];
			$_SESSION["full_name"] = $row["first_name"]." ".$row["last_name"];
			header("Location: index.php");
		}
		else {
			$message = "Невалидни податоци. Ве молиме обидете се повторно.";
		}
	}

	//after register
	if(isset($_GET["success"])) {
		if($_GET["success"] == 1) {
			$message = "Успешно се регистриравте. Ве молиме најавете се.";
		}
		else {
			$error = $_GET["register"];
			if($error == 1)
				$message = "Настана проблем во регистрацијата. Ве молиме обидете се повторно.";
			if($error == 2)
				$message = "Корисничкото име е зафатено. Ве молиме изберете друго корисничко име.";
			if($error == 3)
				$message = "Информациите кои што ги внесовте не се комплетни. Ве молиме обидете се повторно.";
		}
	}

?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <link href="css/bootstrap.min.css" rel="stylesheet">
	<script src="js/jquery-2.1.1.min.js"></script>
	<style>
	body {
	  padding-top: 70px;
	  padding-bottom: 40px;
	  background-color: #eee;
	}

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
        body {
            padding-top: 70px;
        }

        .form-signin {
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}
.form-signin .form-signin-heading,
.form-signin .checkbox {
  margin-bottom: 10px;
}
.form-signin .checkbox {
  font-weight: normal;
}
.form-signin .form-control {
  position: relative;
  height: auto;
  -webkit-box-sizing: border-box;
     -moz-box-sizing: border-box;
          box-sizing: border-box;
  padding: 10px;
  font-size: 16px;
}
.form-signin .form-control:focus {
  z-index: 2;
}
.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

	</style>

	<script type="text/javascript">
		$(function(){
			var on = 1;
		  $("#btn_expand").click(function(){
		  	if(on == 1) {
			  	$("#div_register").show();
			  	$("#div_login").hide();
			  	on = 0;
			  	$("#btn_expand").html('<h5 style="color: #B452CD;">Back to Login</h5>');
			}
			else {
			  	$("#div_register").hide();
			  	$("#div_login").show();
			  	$("#btn_expand").html('<h5 style="color: #B452CD;">Don\'t have account?</h5>');
				on = 1;
			}

		  });
		});
	</script>
  </head>

  <body role="document">

<!-- Fixed navbar -->
    <div class="navbar navbar-ft navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header" style="margin: auto;">
          <a class="navbar-brand" href="#">
          <img src="./images/logo.png" width="32" height="32" alt="logo" />
          Friend Tracker</a>
        </div>
      </div>

      </div>
    </div>





    <div class="container">
    <?php
			if(isset($_GET["success"]) && $_GET["success"] == 1) {
    				  	echo '<div class="alert alert-success" style="text-align: center;" role="alert">'.$message.'</div>';
			}
			else if($message != "") {
    				  	echo '<div class="alert alert-danger" style="text-align: center;" role="alert">'.$message.'</div>';
    		}
    ?>
		<button id="btn_expand" class="btn" style="float: right;"><h5 style="color: #B452CD;">Don't have account?</h5></button>
		<div id="div_login">
	      <form class="form-signin" role="form" method="POST" action="login.php" >
			<h2 class="form-signin-heading" style="text-align: center; color: #B452CD;">Please Login</h2>
	        <input name="uname" class="form-control" placeholder="Username" required autofocus>
	        <input type="password" name="pwd" class="form-control" placeholder="Password" required>
	        <button class="btn btn-lg btn-primary btn-block" type="submit" name="submit">Login</button>
	      </form>
      	</div>
		<div id="div_register" style="display:none;";>
		<form class="form-signin" role="form" method="POST" action="register.php" >
		<div style="margin-bottom: 50px;">
			<h2 class="form-signin-heading" style="text-align: center; color: #B452CD;">Sign up</h2>
			<input  class="form-control" name="username" placeholder="Username" required>
			<input  class="form-control" name="first_name" placeholder="First name" >
			<input  class="form-control" name="last_name" placeholder="Last name" >
			
			<div >
				<label style="font-size: 15px; margin-right: 100px; color: #6D656A" >Choose your age</label>
				<select name="age">
				<?php
					for ($i=1; $i <= 100; $i++) { 
						echo "<option>".$i."</option>";
					}
				?>
				</select>
			</div>
			<input  class="form-control" name="hometown" placeholder="Hometown">
			<input  type="email" name="email" class="form-control" placeholder="E-mail" required>
			<input type="password" name="password" class="form-control" placeholder="Password" required>
			<div >
				<li  style="list-style:none;">
					<label style="font-size: 15px; margin-right: 100px; color: #6D656A" >Sex:</label>
					
						<span style="margin-right: 30px;">
						<input type="radio" name="gender" value="m" checked>Male</input></span>
						<input type="radio" name="gender" value="f">Female</input>						
						
				</li>
			</div>
			</br>
			<button class="btn btn-lg btn-primary btn-block" type="submit" name="submit">Sign up</button>
			
		</div>
      </form>
	  </div

    </div> 
	
  </body>
</html>
