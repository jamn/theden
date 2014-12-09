<h1>Book for Client</h1>
	<select id="clients">
		<option selected="selected">Choose a client...</option>
		<g:each in="${clients}">
			<option value="${it?.id}">${it?.lastName}, ${it?.firstName}</option>
		</g:each>
	</select>
	<select id="services">
		<option selected="selected">Choose a service...</option>
		<g:each in="${services}">
			<option value="${it?.id}">${it?.description}</option>
		</g:each>
	</select>
	<select id="timeSlots">
		<option class="no-timeslots-available">No timeslots available</option>
	</select>
	<label id="dateOfAppointmentLabel" for="dateOfAppointment">Date:</label>
	<input id="dateOfAppointment" name="dateOfAppointment" type="text" class="date">
	
	<div id="recurringAppointmentAdmin">
		<ul>
			<li><input type="checkbox" name="recurringAppointment" id="recurringAppointment" style="top:1px;"></li>
			<li>Recurring Appointment?</li>
			<li class="recurringAppointmentAdminOptions">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp; </li>
			<li class="recurringAppointmentAdminOptions">Repeat every</li>
			<li class="recurringAppointmentAdminOptions">
				<select id="repeatDuration">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
				</select>
			</li>
			<li class="recurringAppointmentAdminOptions">
				week(s) for
			</li>
			<li class="recurringAppointmentAdminOptions">
				<select id="repeatNumberOfAppointments">
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
				</select>
			</li>
			<li class="recurringAppointmentAdminOptions">
				appointments total.
			</li>
		</ul>
	</div>
	<div id="bookForClientButton" class="btn green-button">Book Appointment</div>

<script type="text/javascript">
	$('#dateOfAppointment').datepicker( {
		minDate: 0
	});
	$('#dateOfAppointment').datepicker("setDate", new Date());
	$('#bookForClientButton').confirmOn({
		classPrepend: 'confirmon',
		questionText: 'Book for client?',
		textYes: 'Yes',
		textNo: 'No'
	},'click', function(e, confirmed){
		if(confirmed){
			var cId = $('#clients').val();
			var sId = $('#services').val();
			var aDate = $('#dateOfAppointment').val();
			var sTime = $('#timeSlots').val();
			var recurringAppointment = $('#recurringAppointment').is(':checked');
			var repeatDuration = $('#repeatDuration').val();
			var repeatNumberOfAppointments = $('#repeatNumberOfAppointments').val();

			$('#bookForClientButton').html($('#waitingSpinner').html());

			$.ajax({
				type: "POST",
				url: "./bookForClient",
				data: { cId:cId, sId:sId, aDate:aDate, sTime:sTime, r:recurringAppointment, dur:repeatDuration, num:repeatNumberOfAppointments}
			}).done(function(response) {
				var jsonResponse = JSON.parse(response);
				if (jsonResponse.success === true){
					$('#bookForClientButton').html("Success");
					$('#bookForClientButton').removeClass('error-button animated fadeIn');
				}
				else{
					$('#bookForClientButton').html("Error");
					$('#bookForClientButton').addClass('error-button animated fadeIn');
				}
			});
		}
});
</script>