<!DOCTYPE html>
<html><head>
<meta charset="UTF-8">
<title>Salon Manager | Login</title>
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'reset.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'stylz.css')}?v0.5" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'admin.css')}" />
</head><body>
	<div class="content-wrapper">
		<div class="logo-small"><img width="240px" height="240px" id="logoSmall" src="${resource(dir:'images',file:'logo.png')}"></div>
		<div class="login-box">
			<input type="text" name="username" id="username" placeholder="username">
			<input type="password" name="password" id="password" placeholder="password">
			<div class="green-button" id="loginButton">Login</div>
		</div>
	</div>
<script src="${resource(dir:'js', file:'jquery-1.10.2.min.js')}" type="text/javascript"></script>
<script type="text/javascript">
	$(document).bind('mobileinit',function(){
		$.mobile.loadingMessage = false; 	// Hide the jquery mobile loading message.
											// Must be done before loading jquery mobile.
	});
</script>
<script src="${resource(dir:'js', file:'jquery.mobile-1.3.2.min.js')}" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#username').bind('keyup', function(){
			var value = $(this).val()
			$(this).val(value.replace(/\s+/g, ''));
		});
		$('#password').keypress(function(e) {
			var user = $('#username').val();
			var password = $('#password').val();
			if (e.keyCode === 13){
				window.location.href = "../access/login?u="+user+"&p="+password
			}
		});
	});
	$(document).on('tap', '#loginButton', function(e) {
		var user = $('#username').val();
		var password = $('#password').val();
		window.location.href = "../access/login?u="+user+"&p="+password
	});
</script>
</body></html>