<div class="choose-a-time">
	<h1 id="dateText">Today</h1>
	<label id="chooseDateText" for="chooseDate">Choose a date:</label>
	<input id="chooseDate" name="chooseDate" type="text">
	<label id="recurringAppointmentText" for="recurringAppointment">Recurring Appointment?</label>
	<input type="checkbox" name="recurringAppointment" id="recurringAppointment">
	<div id="recurringAppointmentOptions">
		<ul>
			<li>Repeat every</li>
			<li>
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
			<li>
				week(s) for
			</li>
			<li>
				<select id="repeatNumberOfAppointments">
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
				</select>
			</li>
			<li>
				appointments total.
			</li>
		</ul>
	</div>
</div>