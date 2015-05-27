<div class="confirmation">
	<g:if test="${passwordReset && success}">
		<p>
			Your password has been reset. <span class="home">Book Now</span>
		</p>
	</g:if>
	<g:elseif test="${passwordReset}">
		<p>
			A link to reset your password has been sent to your email.
		</p>
	</g:elseif>
	<g:elseif test="${appointmentDeleted}">
		<p>
			Your appointment has been cancelled.
		</p>
	</g:elseif>
	<g:else>
		<g:if test="${appointments?.size() > 0}">
			<g:set var="plural" value="${appointments.size() == 1 ? 'appointment has' : 'appointments have'}" />
			<h2>
				Thank you. Your ${plural} been booked for:
			</h2>
			<ul style='list-style: none;margin-left: -38px;'>
				<g:each in='${appointments}'>
					<li>- ${it.appointmentDate.format('EEEE, MMMM dd @ hh:mm a')}</li>
				</g:each>
			</ul>
		</g:if>

		<g:if test="${existingAppointments.size() > 0}">
			<div id='existingAppointments'>
				<div class='existing-appointments-text'>Unfortunately we were unable to book an appointment for you on the following date(s):</div>
				<ul class='existing-appointments'>
					<g:each in='${existingAppointments}'>
						<li>
							${it.appointmentDate.format('MMMM dd')}
						</li>
					</g:each>
				</ul>
			</div>
		</g:if>
	</g:else>
</div>