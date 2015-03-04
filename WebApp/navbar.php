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
          <img src="./images/logo.png" width="32" height="32" alt="logo" />
          Friend Tracker</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li id="nav_index"><a href="index.php">My Tags</a></li>
            <li id="nav_friends"><a href="friends.php">My Friends</a></li>
            <li id="nav_addfriend"><a href="addfriend.php">Add Friend</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
          	<li>
          		<a id="btn1" style="cursor: pointer;" data-toggle="popover" data-placement="bottom" title="Accept friends" 
          		data-content="You don't have any new friend requests.">
  					<span class="glyphicon glyphicon-user"></span> Friend Request
  					<span id="friendRequestsCount" style="color: #ccc; font-weight: bold;"></span>
          		</a>
          	</li>
            <li id="nav_profile"><a href="#"><?php echo $_SESSION["full_name"]; ?></a></li>
            <li><a href="logout.php">Logout</a></li>
          </ul>
        </div>
      </div>

      </div>
    </div>