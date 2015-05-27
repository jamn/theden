<g:set var="formAction" value="${cancelAppointment ? 'Cancel' : 'Book'}" />
<g:set var="buttonId" value="${cancelAppointment ? 'cancelAppointmentLoginButton' : 'loginButton'}" />

<div class="col-xs-12 col-sm-offset-3 col-sm-6">
	<form class="login-box">

		<g:if test="${!cancelAppointment && session.client}">
			<div class="user-details">
				<a class="logout" onclick="logout();">Logout</a> | ${session.client.fullName}
			</div>
		</g:if>

		<g:set var="plural" value="${session?.bookedAppointments?.size() == 1 ? '' : 's'}" />
		<g:if test="${!cancelAppointment && session?.bookedAppointments?.size() > 0}">
			<h2>Confirm ${session?.bookedAppointments[0]?.service?.description}${plural}:</h2>
			<ul>
				<g:each in='${session.bookedAppointments}'>
					<li>${it.appointmentDate.format('EEEE, MMMM dd @ hh:mm a')}</li>
				</g:each>
			</ul>
		</g:if>

		<g:if test="${session.appointmentToDelete}">
			<h2>Confirm Cancellation:</h2>
			<ul>
				<li> ${session.appointmentToDelete.service.description}: ${session.appointmentToDelete.appointmentDate.format('EEEE, MMMM dd @ hh:mm a')}</li>
			</ul>
		</g:if>

		<g:if test="${!cancelAppointment}">
			<label>
				<input type="checkbox" name="emailReminder" id="emailReminder" checked> Send email reminder${plural}?
			</label>
			<label>
				<input type="checkbox" name="textMessageReminder" id="textMessageReminder" checked> Send text message reminder${plural}?
			</label>
			<div class="reminders-note">Reminders are sent 24 hours before your appontment.</div>
		</g:if>


		<div class="no-show-policy">There will be a $20 charge at your following visit if you cancel within 4 hours of your appointment. Unless previous arrangements have been made, anything past 10 minutes late will be considered a no show and you will need to reschedule.</div>

		<g:set var="loggedIn" value="${(!cancelAppointment && session?.client) ? 'logged-in' : ''}" />
		
		<input type="hidden" name="loggedIn" id="loggedIn" value="${loggedIn}" />
		
		<g:if test="${!cancelAppointment}">
			<input class="form-control ${loggedIn}" placeholder="Phone" type="text" name="phoneNumber" id="phoneNumber" value="${client?.phone}" autofocus="autofocus" />
		</g:if>

		<input class="form-control ${loggedIn}" placeholder="Email" type="text" name="email" id="email" value="${client?.email}" />

		<input class="form-control ${loggedIn}" placeholder="Password" type="password" name="password" id="password-${formAction}" value="${client?.password}" />

		<g:if test="${!cancelAppointment}">
			<label class="${loggedIn}">		
				<input type="checkbox" name="rememberMe" id="rememberMe" checked> Remember Me
			</label>
		</g:if>

		<div class="reset-password-links ${loggedIn}">
			<span id="resetPassword">Reset Password</span>
			<span class="right-divider"> | </span>
			<span id="showLoginForm">Login Form</span>
		</div>

		
		<div class="btn green-button login-button" id="${buttonId}"><div class="as-button-label">${formAction} Appointment${plural}</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>

	</form>



	<div class="errorDetails"></div>

</div>

<script type="text/javascript">

	function logout() {
		$(".user-details").slideUp();
		$(".logged-in").slideDown();
		$("#loggedIn").val("false");
	}

	$('#email').bind('keyup', function(){
		var value = $(this).val()
		$(this).val(value.replace(/\s+/g, ''));
	});
	$('#textMessageReminder').click(function() {
		if( $(this).is(':checked')) {
			$("#phoneNumber").slideDown();
		} else {
			$("#phoneNumber").slideUp();
		}
	});
	$(document).ready(function(){
		$("#phoneNumber").mask("999-999-9999");
	});
</script>
