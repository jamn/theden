<div class="close-popup-button"><span class="icon">x</span></div>
<h2>${client.fullName}</h2>
<h3>Notes:</h3>
<textarea id="clientNotes">${client.notes}</textarea>
<div id="saveClientNotesButton" class="green-button">Save</div>
<hr>
<h3>Contact:</h3>
<p><a href="tel:${it.client?.phone?.replace('-', '')}">${it.client.phone}</a> | <a href="mailto:${it.client.email}">${it.client.email}</a></p>
<hr>
<h3>History:</h3>
<%if (appointments.size() > 0){%>
	<table>
		<thead>
			<tr>
				<td style="width:120px;">Date:</td>
				<td>Service:</td>
			</tr>
		</thead>
		<tbody>
			<g:each in="${appointments}" var="appointment">
				<tr>
					<td>${appointment.appointmentDate.format('MM/dd/yy')}</td>
					<td>${appointment.service.description}</td>
				</tr>
			</g:each>
		</tbody>
	</table>
<%}else{%>
	<p> No past appointments found.</p>
<%}%>

<script type="text/javascript">
	$('#saveClientNotesButton').click(function(e) {
		console.log("clicked client notes button");
		var notes = encodeURIComponent($('#clientNotes').val());
		$.ajax({
			type: "POST",
			url: "./saveClientNotes",
			data: { n:notes }
		}).done(function(response) {
		});
	});

	$('.close-popup-button').click(function(e) {
		$('#mask').fadeOut();
		$('#clientDetailsPopup').fadeOut();
	});
</script>