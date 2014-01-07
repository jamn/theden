<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html><head>
<meta charset="UTF-8">
<title>The Den Barbershop :: Admin</title>
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'style.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'admin.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'jquery-ui-1.10.3.custom.min.css')}" />
</head><body>

	<div id="logout">logout</div>



	<h1>Homepage Message</h1>
	<textarea id="homepageText" rows="8" cols="50">${homepageText}</textarea>
	<div id="saveTextButton">Save</div>



	<hr>
	<h1>Block Off Time</h1>
	<label id="chooseDateToBlockOffText" for="chooseDateToBlockOff">Choose a date:</label>
	<input id="chooseDateToBlockOff" name="chooseDateToBlockOff" type="text">
	<label id="fromText" for="from">&nbsp;&nbsp;&nbsp;From:</label>
	<select id="fromHour">
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
		<option value="11">11</option>
		<option value="12">12</option>
	</select>
	:
	<select id="fromMinute">
		<option value="00">00</option>
		<option value="15">15</option>
		<option value="30">30</option>
		<option value="45">45</option>
	</select>
	<select id="fromMorningOrAfternoon">
		<option value="am">AM</option>
		<option value="pm">PM</option>
	</select>
	<label id="toText" for="to">&nbsp;&nbsp;&nbsp;To:</label>
	<select id="toHour">
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
		<option value="11">11</option>
		<option value="12">12</option>
	</select>
	:
	<select id="toMinute">
		<option value="00">00</option>
		<option value="15">15</option>
		<option value="30">30</option>
		<option value="45">45</option>
	</select>
	<select id="toMorningOrAfternoon">
		<option value="am">AM</option>
		<option value="pm">PM</option>
	</select>
	<div id="blockOffTimeButton">Block Off Time</div>
	<div id="waitingSpinner" style="display:none;"><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" class="spinner"></div>




	<hr>
	<h1>Block Off Whole Day</h1>
	<label id="fromText" for="fromWholeDay">&nbsp;&nbsp;&nbsp;From:</label>
	<input id="fromWholeDay" name="fromWholeDay" type="text">

	<label id="toText" for="toWholeDay">&nbsp;&nbsp;&nbsp;To:</label>
	<input id="toWholeDay" name="toWholeDay" type="text">

	<div id="blockOffDaysButton">Block Off Days</div>
	<div id="waitingSpinner" style="display:none;"><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" class="spinner"></div>




	<hr>
	<div style="position:relative;">	
		<h1>Upcoming Appointments</h1>
		<div class="fourteenDayViewLink"><a id="fourteenDayViewLink" href="#">14 day view</a></div>
	</div>
	<g:render template="fourteenDayView" collection="${appointments}" />


	<table>
		<tr>
			<td width="120px"><h2>Name:</h2></td>
			<td><h2>Service:</h2></td>
			<td width="120px"><h2>Date:</h2></td>
			<td><h2>Notes:</h2></td>
		</tr>
		<g:each in="${appointments}">	
			<tr>
				<td>${it.client.fullName}</td>
				<td>${it.service.description}</td>
				<td>${it.appointmentDate.format('MM/dd/yy hh:mm a')}</td>
				<td>${it.notes}</td>
			</tr>
		</g:each>
	</table>
<script src="${resource(dir:'js', file:'jquery-1.10.2.min.js')}" type="text/javascript"></script>
<script src="${resource(dir:'js', file:'jquery-ui-1.10.3.custom.min.js')}" type="text/javascript"></script>
<script src="${resource(dir:'js', file:'a.min.js')}" type="text/javascript"></script>
</body></html>