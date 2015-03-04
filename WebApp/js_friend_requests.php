		$("#btn1").popover({ html:true });
		var rb = {};
		rb["username"] = username;

		$.ajax({ 
          type: "POST", 
          url: "./api/getPendingRequests.php", 
          data: rb, 
          dataType: 'json',
          success: function (data) {
          	$("#friendRequestsCount").text("(" + data.users.length + ")");

          	var changedData = "";
          	for(var i=0;i<data.users.length;i++) {
          		changedData += '<div style="padding: 10px 5px;">';
          		var tempUser = "'" + data.users[i].username + "'";
          		var tempUser2 = "'" + username + "'";
          		changedData+=data.users[i].first_name + " " + data.users[i].last_name + '<button onclick="makeFriendship(' + tempUser +', ' + tempUser2 +')" type="button" class="btn btn-primary btn-xs" style="float:right; ">Accept</button>';
          		changedData += '</div>';
          	}

          	$("#btn1").attr("data-content", changedData);
          	for(var i=0;i<data.users.length;i++) {
          		$("#accept" + data.users[i].username).click(function () {
          			console.log("accepting");
          		});
          	}
          }
    	});