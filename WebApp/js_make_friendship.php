        function makeFriendship(friendname, username) {
            reqBody = {};
            reqBody["username"] = username;
            reqBody["friendname"] = friendname;

            $.ajax({
                type: "POST",
                url: "./api/acceptFriend.php",
                data: reqBody,
                dataType: 'json',
                success: function(data) {
                    console.log(data.success + "Result");
                }
            });
        }

        function addFriend(friendname, username) {
            reqBody = {};
            reqBody["username"] = username;
            reqBody["friendname"] = friendname;

            $.ajax({
                type: "POST",
                url: "./api/addFriend.php",
                data: reqBody,
                dataType: 'json',
                success: function(data) {
                    console.log(data.success + "Result");
                }
            });
        }