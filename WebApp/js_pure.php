
    var pathname = window.location.pathname;
    var patharray = pathname.split('/');
    var currentpath = patharray[patharray.length -1];
    switch(currentpath) {
    	case 'index.php':
    		$("#nav_index").addClass("active");
    		break;
    	case 'friends.php':
    		$("#nav_friends").addClass("active");
    		break;
    	case 'addfriend.php':
    		$("#nav_addfriend").addClass("active");
    		break;
    	case 'profile.php':
    		$("#nav_profile").addClass("active");
    		break;
    }