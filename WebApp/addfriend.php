<?php
	session_start();
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

    <!-- My custom details -->
    <link href="./css/friendtracker.css" rel="stylesheet">
  </head>

  <body role="document">

    <?php require_once( 'navbar.php'); ?>

    <div class="container theme-showcase" role="main">
      <div>
        <div style="width: 600px; margin: auto;">
          <div class="input-group">
            <input id="search_text" type="text" class="form-control">
            <span class="input-group-btn">
              <button id="btn_search" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-search"></span> Search
              </button>
            </span>
          </div><!-- /input-group -->
        </div><!-- /.col-lg-6 -->

        <div style="clear: both;"></div>
        <br />
        <div id="results" style="width: 600px; margin: auto;">
          <div id="users" class="list-group">

          </div>
        </div>
      </div>
    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/jquery-2.1.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script type="text/javascript">
      <?php require_once('js_pure.php'); ?>
      <?php require_once('js_make_friendship.php'); ?>

      $(function () {
        var username = "<?php echo $_SESSION["username"]; ?>";
        <?php require_once('js_friend_requests.php'); ?>


        $("#btn_search").click(function () {
          var search_text = $("#search_text").val();
          console.log(search_text);

          if(search_text.length > 0) {
            var reqBody = {};
            reqBody["username"] = username;
            reqBody["search_text"] = search_text;
            $.ajax({
              type: "POST",
              url: "./api/searchUser.php",
              data: reqBody,
              dataType: 'json',
              success: function(data) {
                if(data.success == 1) {
                  var users = data.users;

                  // remove old users
                  $("#users").text("No results found");

                  var newData = "";


                  var upper = '<div class="list-group-item"> \
              <div style="float: left;"> \
              <img src="images/male.png" alt="" style="width: 50px; height: 50px; margin-right: 10px; margin-top: 5px;" /> \
              </div><div style="float: left;"> \
              <h4 class="list-group-item-heading">';
              var middle = '</h4><p class="list-group-item-text">Age: ';
              var age = '</p><p class="list-group-item-text">Hometown: ';
              var hometown = '</p></div> \
              <div style="float: right; margin-top: 12px;"> \
                <button onclick="addFriend(';
                var h2 =  ')" type="button" class="btn btn-default"> \
                  <span class="glyphicon glyphicon-plus"></span> Add friend \
                </button> </div> \
              <div style="clear: both;"></div></div>';

                  // add new users
                  for(var i=0;i<users.length;i++) {

                    var tempUser = "'" + users[i].username + "'";
                    var tempUser2 = "'" + username + "'";

                    newData += upper + users[i].first_name + " " + users[i].last_name;
                    newData += middle + users[i].age;
                    newData += age + users[i].hometown;
                    newData += hometown;
                    newData += tempUser +', ' + tempUser2;
                    newData += h2;
                  }

                  if(newData != "")
                    $("#users").html(newData);
                }
              }
            });
          }

        });
      });
    </script>
  </body>
</html>
