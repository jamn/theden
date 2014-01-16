<p>
Your appointment(s) have been booked for:
</p>
<ul style="list-style: none;margin-left: -38px;">
	<g:each in="${appointments}">
		<li>- ${it.appointmentDate.format('EEEE, MMMM dd @ hh:mm a')}</li>
	</g:each>
</ul>
<%if (existingAppointments.size() > 0){%>
	<div id="existingAppointments">
		<div class="existing-appointments-text">Unfortunately we were unable to book an appointment for you on the following date(s):</div>
		<ul class="existing-appointments">
			<g:each in="${existingAppointments}">
				<li>
					${it.appointmentDate.format('MMMM dd')}
				</li>
			</g:each>
		</ul>
	</div>
<%}%>