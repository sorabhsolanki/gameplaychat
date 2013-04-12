<html>

<head>
<title>DIC ChatBox Beta 1</title>
<link rel="stylesheet" type="text/css" href="css/cb_style.css">
<script language="JavaScript" src="js/jquery-1.8.3.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#send').click(function() {
			
			var username = $('#userName').val();
			if(username == ""){
				alert("Please Login");
				return;
			}
			var x = $('#chatBox').text();
			var msg = $('#message').val();
			
			$('#chatBox').text(x + "\n" + username + ":" + msg);
			$('#message').val("");
			
			// making ajax call for sending the data
			  $.ajax({url:"${pagecontext.request.contextpath}/gameplaychat/rest/chat/publish/" + msg + "/" + username,type: "get",success:function(result){
			  }});
		});
		
		$('#signInButt').click(function() {
			var username = $('#userName').val();
			// making ajax call for joining the chat group
			 $.ajax({url:"${pagecontext.request.contextpath}/gameplaychat/rest/chat/login/" + username,type: "get",success:function(result){
				    alert("Login :: " + result);
				  }});
		});
		
		$('#receiveButt').click(function() {
			var username = $('#userName').val();
			if(username == ""){
				alert("Please Login");
				return;
			}
			var x = $('#chatBox').text();
			$.ajax({ url: "${pagecontext.request.contextpath}/gameplaychat/rest/chat/receive/" + username, type: "get", success: function(data){
		    	$('#chatBox').text(x + "\n" + data);
		    }});
		});
		
		$('#logoutButt').click(function() {
			var username = $('#userName').val();
			$.ajax({ url: "${pagecontext.request.contextpath}/gameplaychat/rest/chat/logout/" + username, type: "get", success: function(data){
		    	alert("Logged Out : " + data);
		    }});
		});
		
		$('#chatHistory').click(function() {
			var username = $('#userName').val();
			if(username == ""){
				alert("Please Login");
				return;
			}
			$.ajax({ url: "${pagecontext.request.contextpath}/gameplaychat/rest/chat/history/" + username, type: "get", success: function(data){
				if(data == "Please login"){
					alert(data);
				}else{
					var result = data.split("|");
					var x = "";
					for(var i=0;i < result.length;i++){
						x = $('#chatBox').text();
						$('#chatBox').text(x + "\n" + result[i]);
					}
				}
		    }});
		});
		
		// Traditional Polling for recieving the data from chat server
 		setInterval(function(){
		    $.ajax({ url: "${pagecontext.request.contextpath}/gameplaychat/rest/chat/details", type: "get", success: function(data){
		    	var result = data.split("|");
		    	var users = "";
		    	for(var i=0;i < result.length;i++){
		    		users = users + "\n" + result[i];
		    	}
		    	$('#usersOnLine').text(users);
		    }
		  	 });
		}, 3000);
		
	});
</script>
</head>

<body>

<h1>Chat Box</h1>
<form id="signInForm">
	<input id="userName" type="text">
	<input id="signInButt" name="signIn" type="button" value="Join Chat">
	<input id="receiveButt" name="receive" type="button" value="Receive Chat">
	<span id="signInName">User name</span>
</form>

<div id="chatBox"></div>
Online Users
<div id="usersOnLine"></div>

<form id="messageForm">

	<input id="message" type="text">
	<input id="send" type="button" value="Send">
	<input id="chatHistory" name="chathistory" type="button" value="Chat History">
	<input id="logoutButt" name="logout" type="button" value="Sign Out">
<!--<div id="serverRes"></div>-->
</form>
</body>
</html>