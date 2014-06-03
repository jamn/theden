<form class="login-box">
	<input placeholder="Email" type="text" name="email" id="email" value="${client?.email}" autofocus="autofocus">
	<input placeholder="Verify Email" type="text" name="email2" id="email2" style="display:none;">

	<input placeholder="Password" type="password" name="password" id="password" value="${client?.password}">

	%{--<input placeholder="First Name" type="text" name="first-name" id="firstName" class="new-user">

	<input placeholder="Last Name" type="text" name="last-name" id="lastName" class="new-user"><br>

	<input placeholder="Phone #" type="text" name="phone" id="phoneNumber" class="new-user"><br>
	
	<div style="text-align:center;"> <span id="registerLink">New Client?</span>
		<span class="left-divider"> | </span>--}%
		<span id="resetPassword">Reset Password</span>
		<span class="right-divider"> | </span>
		<span id="showLoginForm">Login Form</span>
	</div>
	
</form>
<div class="errorDetails"></div>
<div class="green-button" id="loginButton"><div class="label">Book Appointment</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>

<script type="text/javascript">
	$('#email').bind('keyup', function(){
		var value = $(this).val()
		$(this).val(value.replace(/\s+/g, ''));
	});
	$(document).ready(function(){
		$("#phoneNumber").mask("999-999-9999");
	});
</script>

