<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html><head>
<meta charset="UTF-8">
<title>:: The Den Barbershop | Modify Appointment ::</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'animate.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'reset.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'stylz.css')}?v0.4" />
<link media="handheld, only screen" href="${resource(dir:'css', file:'mobile.css')}" type="text/css" rel="stylesheet" />

<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'jquery-ui-1.10.3.custom.min.css')}" />
</head><body baseUrl="${createLink()}">

		<div class="header">
			<img class="home" id="logoPlain" src="${resource(dir:'images',file:'logo-plain.png')}">
			<div id="newAddress">1013 W 47th Street<br/>KCMO, 64112</div>
		</div>
		<div class="grey-box">
			<ul>
				<li>
					<div class="message-board-backing">
						<div class="message-board">${message}</div>
					</div>
				</li>
				<li>
					<img id="logoLarge" src="${resource(dir:'images',file:'logo-large.png')}">
				</li>
			</ul>
		</div>
		
		<div class="main-content-area">
			
			<form class="reset-password-box">

				<input placeholder="New Password" type="password" name="newPassword" 
				id="newPassword">

				<input placeholder="Verify New Password" type="password" name="verifyNewPassword" id="verifyNewPassword">

				
			</form>
			<div class="errorDetails"></div>
			<div class="green-button" id="loginButton" attemptPasswordReset="true"><div class="label">Reset Password</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>

		</div>

		<div class="confirmation"></div>


    <script src="${resource(dir:'js', file:'jquery-1.10.2.min.js')}" type="text/javascript"></script>
	<script src="${resource(dir:'js', file:'jquery-ui-1.10.3.custom.min.js')}" type="text/javascript"></script>
	<script src="${resource(dir:'js', file:'jquery.mobile-1.3.2.min.js')}" type="text/javascript"></script>
	<script src="${resource(dir:'js', file:'application.min.js')}?v0.4" type="text/javascript"></script>
</body></html>