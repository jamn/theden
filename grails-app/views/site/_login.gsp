<g:set var="formAction" value="${cancelAppointment ? 'Cancel' : 'Book'}" />
<g:set var="buttonId" value="${cancelAppointment ? 'cancelAppointmentLoginButton' : 'loginButton'}" />

<div class="col-xs-12 col-sm-offset-3 col-sm-6">
	<form class="login-box">
		<input class="form-control" placeholder="Email" type="text" name="email" id="email" value="${client?.email}" autofocus="autofocus" />
		<input class="form-control" placeholder="Verify Email" type="text" name="email2" id="email2" style="display:none;" />

		<input class="form-control" placeholder="Password" type="password" name="password" id="password-${formAction}" value="${client?.password}" />
		
		<div class="reset-password-links">
			<span id="resetPassword">Reset Password</span>
			<span class="right-divider"> | </span>
			<span id="showLoginForm">Login Form</span>
		</div>

		
		<div class="btn green-button login-button" id="${buttonId}"><div class="as-button-label">${formAction} Appointment</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>
	</form>

	<div class="errorDetails"></div>

</div>

<script type="text/javascript">
	$('#email').bind('keyup', function(){
		var value = $(this).val()
		$(this).val(value.replace(/\s+/g, ''));
	});
</script>
