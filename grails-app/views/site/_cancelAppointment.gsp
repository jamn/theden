<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html><head>
<meta charset="UTF-8">
<title>:: The Den Barbershop | Cancel Appointment ::</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'reset.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'stylz.css')}?v0.4" />
<link media="handheld, only screen" href="${resource(dir:'css', file:'mobile.css')}" type="text/css" rel="stylesheet" />

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

		<div class="login" style="display:initial;">

			<form class="login-box">
				<input placeholder="Email" type="text" name="email" id="email" autofocus="autofocus">

				<input placeholder="Password" type="password" name="password2" id="password2">

				<div style="text-align:center;"> 
					<span id="resetPassword">Reset Password</span>
					<span class="right-divider"> | </span>
					<span id="showLoginForm">Login Form</span>
				</div>
		
			</form>
			<div class="errorDetails"></div>
			<div class="green-button" id="cancelAppointmentLoginButton" onclick="cancelAppointment();"><div class="label">Cancel Appointment</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>

		</div>

		<div class="confirmation"></div>
		
		<div class="footer">&nbsp;</div>

		<script src="${resource(dir:'js', file:'jquery-1.10.2.min.js')}" type="text/javascript"></script>
		<script src="${resource(dir:'js', file:'application.min.js')}?v0.4" type="text/javascript"></script>

		<script type="text/javascript">
			$('#email').bind('keyup', function(){
				var value = $(this).val()
				$(this).val(value.replace(/\s+/g, ''));
			});
		</script>

		
</body></html>