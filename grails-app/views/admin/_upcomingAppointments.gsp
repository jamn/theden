<div class="row">
	<g:render template="fourteenDayView" />
</div>
<div class="row">
	<table class="appointments">
		<tr>
			<td><h2>Name:</h2></td>
			<td width="210px"><h2>Service:</h2></td>
			<td width="210px"><h2>Date:</h2></td>
		</tr>
		<g:each in="${appointments}">
			<%if (it.service.description != 'Blocked Off Time'){%>
				<tr class="appointment-data appointment-data-${it.id}" id="${it.id}">
					<td>
						<%if (it.client.code == 'kp'){%>
							From Old System
						<%}else{%>
							${it.client.fullName}
						<%}%>
					</td>
					<td>${it.service.description}</td>
					<td>${it.appointmentDate.format('MM/dd @ hh:mm a [E]')}</td>
				</tr>
				<tr class="edit-appointment edit-appointment-${it.id}">
					<td colspan="4">
						<div class="col-xs-8">
							<h4>Reschedule:</h4>
							<hr />
							<span id="edit-appointment-options-${it.id}"></span> <img src="${resource(dir:'images', file:'spinner-gray.gif')}" class="spinner-${it.id}" style="display:none;">
						</div>
						<div class="col-xs-4">
							<div class="row">
							<a href="#" c="${it.code}" class="btn error-button cancel-appointment-button" onclick="return confirm('Cancel appointment?')">Cancel<br />Appointment</a>
							</div>
						</div>
					</td>
				</tr>
			<%}%>
		</g:each>
	</table>
</div>