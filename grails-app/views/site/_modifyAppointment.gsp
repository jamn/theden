<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html><head>
<meta charset="UTF-8">
<title>:: The Den Barbershop | Modify Appointment ::</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'animate.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'reset.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'stylz-new.css')}?v0.4" />
<link media="handheld, only screen" href="${resource(dir:'css', file:'media.css')}" type="text/css" rel="stylesheet" />

<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'jquery-ui-1.10.3.custom.min.css')}" />
</head><body baseUrl="${createLink()}">

		<div class="existingAppointmentInfo"><b>Existing Appointment:</b> ${appointment.service.description} | ${appointment.appointmentDate.format('EEEE MMMM dd, yyyy')} @ ${appointment.appointmentDate.format('hh:mm a')}</div>

		<div class="header">
			<img class="home" id="logoPlain" src="${resource(dir:'images',file:'logo-plain.png')}">
			<div id="newAddress">1013 W 47th Street<br/>KCMO, 64112</div>
		</div>
		<div class="grey-box">
			<ul>
				<li>
					<div class="message-board-backing">
						<div class="message-board">${message}</div>
					</div>
				</li>
				<li>
					<img id="logoLarge" src="${resource(dir:'images',file:'logo-large.png')}">
				</li>
			</ul>
		</div>
		
		<div class="main-content-area"></div>
		
		<div class="select-a-service"></div>

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
			<ul id="timeSlots">
				<g:each in="${timeSlotsMap}" var="timeSlots">
					<%if (timeSlots.key == 'morning'){%>
						<li class="morning">
							<h2 class="time-slots-h2">Morning</h2>
							<g:each in="${timeSlots.value}">
								<div onclick="funtion(){}" startTime="${it.startTime}" class="green-button time-slot" id="time-slot-${it.id}"><div class="label">${it.timeSlot}</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>
							</g:each>
						</li>
					<%}%>
					<%if (timeSlots.key == 'lunch'){%>
						<li class="lunch">
							<h2 class="time-slots-h2">Lunch</h2>
							<g:each in="${timeSlots.value}">
								<div onclick="funtion(){}" startTime="${it.startTime}" class="green-button time-slot" id="time-slot-${it.id}"><div class="label">${it.timeSlot}</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>
							</g:each>
						</li>
					<%}%>
					<%if (timeSlots.key == 'afternoon'){%>
						<li class="afternoon">
							<h2 class="time-slots-h2">Afternoon</h2>
							<g:each in="${timeSlots.value}">
								<div onclick="funtion(){}" startTime="${it.startTime}" class="green-button time-slot" id="time-slot-${it.id}"><div class="label">${it.timeSlot}</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>
							</g:each>
						</li>
					<%}%>
				</g:each>
			</ul>
		</div>

		<div class="login"></div>

		<div class="confirmation"></div>

		<div id="address" class="address">1013 W 47th Street &bull; KCMO, 64112</div>
		<div class="google-map" style="display:none;">
			<img src="${resource(dir: 'images', file: 'map.png')}">
		</div>
		
		<div class="footer">&nbsp;</div>


    <script src="${resource(dir:'js', file:'jquery-1.10.2.min.js')}" type="text/javascript"></script>
	<script src="${resource(dir:'js', file:'jquery-ui-1.10.3.custom.min.js')}" type="text/javascript"></script>
	<script src="${resource(dir:'js', file:'jquery.mobile-1.3.2.min.js')}" type="text/javascript"></script>
	<script src="${resource(dir:'js', file:'application.min.js')}?v0.4" type="text/javascript"></script>
	<script src="${resource(dir:'js', file:'masked-input-plugin.min.js')}" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$('.choose-a-time').slideDown();
			$('.choose-a-time').addClass('animated fadeInRightBig');
			var d = new Date("${session.requestedDate}");
			$('#dateText').empty();
			$('#dateText').append(weekday[d.getDay()]);
			$("#chooseDate").datepicker("setDate", d);
			$('#registerLink').hide();
			$('.left-divider').hide();
		});
	</script>
</body></html>