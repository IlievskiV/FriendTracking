<?php session_start(); if(!isset($_SESSION[ "username"])) { header( "Location: login.php"); } ?>

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
        <div class="panel panel-default" style="float: left;">
            <ul class="nav nav-pills nav-stacked left_part" >
                <li id="period1"><a href="#">Today</a>
                </li>
                <li id="period2"><a href="#">Yesterday</a>
                </li>
                <li id="period3" class="active"><a href="#">Last 7 days</a>
                </li>
                <li id="period4"><a href="#">Last 30 days</a>
                </li>
                <li id="period5"><a href="#">Last 3 months</a>
                </li>
                <li id="period6"><a href="#">Last year</a>
                </li>
            </ul>
        </div>
        <div style="float: left; margin-left:20px;">
            <div id="map-canvas" style="width:800px; height:580px;" />
        </div>
    </div>
    <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/jquery-2.1.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>

    <!-- GOOGLE MAPS -->
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCJrVehDTNmeTP6PjDlTvpq7G9H0YBuRPY"></script>
    <script type="text/javascript">
    	<?php require_once('js_pure.php'); ?>
    	<?php require_once('js_make_friendship.php'); ?>

        $(document).ready(function() {
            var username = "<?php echo $_SESSION["username"]; ?>";
            <?php require_once('js_friend_requests.php'); ?>

            var map = null;
            var markers = [];

            $("#period1").click(function() {
                changePeriod(1);
            });
            $("#period2").click(function() {
                changePeriod(2);
            });
            $("#period3").click(function() {
                changePeriod(3);
            });
            $("#period4").click(function() {
                changePeriod(4);
            });
            $("#period5").click(function() {
                changePeriod(5);
            });
            $("#period6").click(function() {
                changePeriod(6);
            });


            function changePeriod(time) {
                //remove css active class
                for (var i = 1; i <= 6; i++) {
                    $("#period" + i).removeClass("active");
                }
                $("#period" + time).addClass("active");

                updateData(time);
            }

            function updateData(period) {
                var reqBody = {};
                reqBody["username"] = username;
                deleteMarkers();

                var d = new Date();
                d.setMinutes(0);
                d.setSeconds(0);
                d.setMilliseconds(0);
                d.setHours(0);

                switch (period) {
                    case 1:
                        reqBody["since"] = d.getTime() / 1000;
                        break;
                    case 2:
                        d.setDate(d.getDate() - 1);
                        reqBody["since"] = d.getTime() / 1000;
                        break;
                    case 3:
                        d.setDate(d.getDate() - 7);
                        reqBody["since"] = d.getTime() / 1000;
                        break;
                    case 4:
                        d.setDate(d.getDate() - 30);
                        reqBody["since"] = d.getTime() / 1000;
                        break;
                    case 5:
                        d.setDate(d.getDate() - 90);
                        reqBody["since"] = d.getTime() / 1000;
                        break;
                    case 6:
                        d.setDate(d.getDate() - 365);
                        reqBody["since"] = d.getTime() / 1000;
                        break;
                }

                $.ajax({
                    type: "POST",
                    url: "./api/getFriendTags.php",
                    data: reqBody,
                    dataType: 'json',
                    success: function(data) {
                        var pins = data.user_tags;
                        for (var i = 0; i < pins.length; i++) {
                            var pos = new google.maps.LatLng(pins[i].latitude, pins[i].longitude);
                            addMarker(pos, pins[i].text);
                        }
                    }
                });
            }

            function initialize() {
                var mapOptions = {
                    center: new google.maps.LatLng(41.5, 22),
                    zoom: 8
                };
                map = new google.maps.Map(document.getElementById("map-canvas"),
                    mapOptions);

                updateData(3);
            }

            // Add a marker to the map and push to the array.
            function addMarker(location, text) {
                var marker = new google.maps.Marker({
                    position: location,
                    map: map,
                    title: text
                });
                markers.push(marker);
            }

            // Sets the map on all markers in the array.
            function setAllMap(map) {
                for (var i = 0; i < markers.length; i++) {
                    markers[i].setMap(map);
                }
            }

            // Removes the markers from the map, but keeps them in the array.
            function clearMarkers() {
                setAllMap(null);
            }

            // Shows any markers currently in the array.
            function showMarkers() {
                setAllMap(map);
            }

            // Deletes all markers in the array by removing references to them.
            function deleteMarkers() {
                clearMarkers();
                markers = [];
            }


            google.maps.event.addDomListener(window, 'load', initialize);

        })
    </script>

    <!-- END GOOGLE MAPS -->
</body>

</html>
