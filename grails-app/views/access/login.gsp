<!DOCTYPE html>
<html><head>
<meta charset="UTF-8">
<title>Salon Manager | Login</title>
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'reset.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'stylz.css')}?v0.4" />
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
<script src="${resource(dir:'js', file:'a.min.js')}?v0.4" type="text/javascript"></script>
</body></html>