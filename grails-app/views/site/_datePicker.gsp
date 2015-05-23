<div class="row date-picker">
	<div class="col-xs-offset-2 col-xs-8 col-sm-offset-3 col-sm-6 choose-a-time">
		<h1 id="dateText">Today</h1>
		<label id="chooseDateText" for="chooseDate">Choose a date:</label>
		<input class="form-control" id="chooseDate" name="chooseDate" type="text">
		<br />
		<label id="recurringAppointmentText" for="recurringAppointment">Recurring Appointment?</label>
		<br />
		<div id="recurringAppointmentOptions">
			<ul>
				<li><input type="checkbox" name="recurringAppointment" id="recurringAppointmentCheckbox"></li>
				<li class="recurring-appointment-options">Repeat every</li>
				<li class="recurring-appointment-options">
					<select  class="form-control" id="repeatDuration">
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
				<li class="recurring-appointment-options">
					week(s) for
				</li>
				<li class="recurring-appointment-options">
					<select  class="form-control" id="repeatNumberOfAppointments">
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select>
				</li>
				<li class="recurring-appointment-options">
					appointments total.
				</li>
			</ul>
		</div>
	</div>
</div>

<script type="text/javascript">

	var weekday=new Array(7);
	weekday[0]="Sunday";
	weekday[1]="Monday";
	weekday[2]="Tuesday";
	weekday[3]="Wednesday";
	weekday[4]="Thursday";
	weekday[5]="Friday";
	weekday[6]="Saturday";

	$(document).ready(function(){
		$('#chooseDate').datepicker( {
			onSelect: function(date) {
				var date = $('#chooseDate').val();
				console.log(date);
				var baseUrl = $('body').attr('baseUrl');
				$.ajax({
					type: "POST",
					url: baseUrl+"site/getTimeSlots",
					data: {d: date}
				}).done(function(timeSlots) {
					$('.time-slots').remove();
					$('.main-content .page[page="timeSlots"]').append(timeSlots);
					var d = new Date(date);
					$('#dateText').empty();
					$('#dateText').append(weekday[d.getDay()]).slideDown();
					$('#chooseDate').datepicker("setDate", d);
				}).fail(function(){
					console.log("there was an error retrieving time slots");
				});
			},
			minDate: 0,
			beforeShowDay: function(date) {
			 	var day = date.getDay();
			 	return [(day != 6 && day != 0)];
			}
		});
		$('#chooseDate').datepicker("setDate", new Date());
		$('.recurring-appointment-options').hide();
	});

	$(document).on("tap", "#recurringAppointmentCheckbox", function() {
		$(".recurring-appointment-options").fadeToggle();
	});
</script>