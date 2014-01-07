<!DOCTYPE html>
<html><head>
<meta charset="UTF-8">
<title>Salon Manager | Login</title>
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'style.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'admin.css')}" />
</head><body>
	<div class="content-wrapper">
		<div class="logo-small"><img id="logoSmall" src="${resource(dir:'images',file:'logo.png')}"></div>
		<div class="login-box">
			<input type="text" name="username" id="username" placeholder="username">
			<input type="password" name="password" id="password" placeholder="password">
			<div id="loginButton">Login</div>
		</div>
	</div>
<script src="${resource(dir:'js', file:'jquery-1.10.2.min.js')}" type="text/javascript"></script>
<script src="${resource(dir:'js', file:'a.min.js')}" type="text/javascript"></script>
</body></html>