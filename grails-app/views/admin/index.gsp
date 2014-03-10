<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html><head>
<meta charset="UTF-8">
<title>The Den Barbershop :: Admin</title>
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'style.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'admin.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'jquery-ui-1.10.3.custom.min.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'jquery.confirmon.css')}" />

<link rel="apple-touch-icon" sizes="57x57" href="${resource(dir:'images', file:'apple-icon-57x57.png')}" />
<link rel="apple-touch-icon" sizes="72x72" href="${resource(dir:'images', file:'apple-icon-72x72.png')}" />
<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir:'images', file:'apple-icon-114x114.png')}" />
<link rel="apple-touch-icon" sizes="144x144" href="${resource(dir:'images', file:'apple-icon-144x144.png')}" />
</head><body>

	<div id="log">log</div>
	<div id="divider">|</div>
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



	<hr>
	<h1>Block Off Whole Day</h1>
	<label id="fromText" for="fromWholeDay">&nbsp;&nbsp;&nbsp;From:</label>
	<input id="fromWholeDay" name="fromWholeDay" type="text">

	<label id="toText" for="toWholeDay">&nbsp;&nbsp;&nbsp;To:</label>
	<input id="toWholeDay" name="toWholeDay" type="text">

	<div id="blockOffDaysButton">Block Off Days</div>
	


	<hr>
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
	<select id="timeSlots"></select>
	<label id="dateOfAppointmentLabel" for="dateOfAppointment">Date:</label>
	<input id="dateOfAppointment" name="dateOfAppointment" type="text">
	
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
	<div id="bookForClientButton">Book Appointment</div>


	<hr>
	<div style="position:relative;">	
		<h1>Upcoming Appointments</h1>
		<div class="fourteenDayViewLink"><a id="fourteenDayViewLink" href="#">14 day view</a></div>
	</div>

	<%
	Calendar dayOne = new GregorianCalendar()
	dayOne.setTime(startTime.getTime())
	Calendar dayTwo = new GregorianCalendar()
	dayTwo.setTime(startTime.getTime())
	dayTwo.add(Calendar.DAY_OF_WEEK, 1)
	Calendar dayThree = new GregorianCalendar()
	dayThree.setTime(startTime.getTime())
	dayThree.add(Calendar.DAY_OF_WEEK, 2)
	Calendar dayFour = new GregorianCalendar()
	dayFour.setTime(startTime.getTime())
	dayFour.add(Calendar.DAY_OF_WEEK, 3)
	Calendar dayFive = new GregorianCalendar()
	dayFive.setTime(startTime.getTime())
	dayFive.add(Calendar.DAY_OF_WEEK, 4)
	Calendar daySix = new GregorianCalendar()
	daySix.setTime(startTime.getTime())
	daySix.add(Calendar.DAY_OF_WEEK, 5)
	Calendar daySeven = new GregorianCalendar()
	daySeven.setTime(startTime.getTime())
	daySeven.add(Calendar.DAY_OF_WEEK, 6)
	def now = new Date()
	%>
	<table id="fourteenDayView-week1">
	<tr class="dateHeader">
		<td></td>
		<td>${dayOne.getTime().format('EEE dd')}</td>
		<td>${dayTwo.getTime().format('EEE dd')}</td>
		<td>${dayThree.getTime().format('EEE dd')}</td>
		<td>${dayFour.getTime().format('EEE dd')}</td>
		<td>${dayFive.getTime().format('EEE dd')}</td>
		<td>${daySix.getTime().format('EEE dd')}</td>
		<td>${daySeven.getTime().format('EEE dd')}</td>
		<td></td>
	</tr>
	<% while (dayOne < endTime){ %>
		<tr class="halfHour" time="${dayOne.getTime().format('hh:mm a')}">
			<td rowspan="2" class="time">${dayOne.getTime().format('hh:mm a')}</td>
			<%
				def dayOneAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayOne.getTime() >= startDate.getTime() && dayOne.getTime() < endDate.getTime()){
						dayOneAppointment = it
					}
				}
				def dayOneDay = dayOne.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayOneDay == 1 || dayOneDay == 7){%>"unavailable"<%}else if(dayOneAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayOneAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayOneAppointment){%><div id="appointment-${dayOneAppointment?.id}" class="appointmentDetailsCallOut">${dayOneAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def dayTwoAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayTwo.getTime() >= startDate.getTime() && dayTwo.getTime() < endDate.getTime()){
						dayTwoAppointment = it
					}
				}
				def dayTwoDay = dayTwo.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayTwoDay == 1 || dayTwoDay == 7){%>"unavailable"<%}else if(dayTwoAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayTwoAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayTwoAppointment){%><div id="appointment-${dayTwoAppointment?.id}" class="appointmentDetailsCallOut">${dayTwoAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def dayThreeAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayThree.getTime() >= startDate.getTime() && dayThree.getTime() < endDate.getTime()){
						dayThreeAppointment = it
					}
				}
				def dayThreeDay = dayThree.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayThreeDay == 1 || dayThreeDay == 7){%>"unavailable"<%}else if(dayThreeAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayThreeAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayThreeAppointment){%><div id="appointment-${dayThreeAppointment?.id}" class="appointmentDetailsCallOut">${dayThreeAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def dayFourAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayFour.getTime() >= startDate.getTime() && dayFour.getTime() < endDate.getTime()){
						dayFourAppointment = it
					}
				}
				def dayFourDay = dayFour.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayFourDay == 1 || dayFourDay == 7){%>"unavailable"<%}else if(dayFourAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayFourAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayFourAppointment){%><div id="appointment-${dayFourAppointment?.id}" class="appointmentDetailsCallOut">${dayFourAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def dayFiveAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayFive.getTime() >= startDate.getTime() && dayFive.getTime() < endDate.getTime()){
						dayFiveAppointment = it
					}
				}
				def dayFiveDay = dayFive.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayFiveDay == 1 || dayFiveDay == 7){%>"unavailable"<%}else if(dayFiveAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayFiveAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayFiveAppointment){%><div id="appointment-${dayFiveAppointment?.id}" class="appointmentDetailsCallOut">${dayFiveAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def daySixAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (daySix.getTime() >= startDate.getTime() && daySix.getTime() < endDate.getTime()){
						daySixAppointment = it
					}
				}
				def daySixDay = daySix.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(daySixDay == 1 || daySixDay == 7){%>"unavailable"<%}else if(daySixAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(daySixAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (daySixAppointment){%><div id="appointment-${daySixAppointment?.id}" class="appointmentDetailsCallOut">${daySixAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def daySevenAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (daySeven.getTime() >= startDate.getTime() && daySeven.getTime() < endDate.getTime()){
						daySevenAppointment = it
					}
				}
				def daySevenDay = daySeven.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(daySevenDay == 1 || daySevenDay == 7){%>"unavailable"<%}else if(daySevenAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(daySevenAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (daySevenAppointment){%><div id="appointment-${daySevenAppointment?.id}" class="appointmentDetailsCallOut">${daySevenAppointment?.service?.description}</div><%}%>
			</td>
			

			<td rowspan="2" class="time">${dayOne.getTime().format('hh:mm a')}</td>
		</tr>

		<%dayOne.add(Calendar.MINUTE, 15)%>
		<%dayTwo.add(Calendar.MINUTE, 15)%>
		<%dayThree.add(Calendar.MINUTE, 15)%>
		<%dayFour.add(Calendar.MINUTE, 15)%>
		<%dayFive.add(Calendar.MINUTE, 15)%>
		<%daySix.add(Calendar.MINUTE, 15)%>
		<%daySeven.add(Calendar.MINUTE, 15)%>

		<tr class="fifteen" time="${dayOne.getTime().format('hh:mm a')}">
			<%
				dayOneAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayOne.getTime() >= startDate.getTime() && dayOne.getTime() < endDate.getTime()){
						dayOneAppointment = it
					}
				}
			%>
			<td class=<%if(dayOneDay == 1 || dayOneDay == 7){%>"unavailable"<%}else if(dayOneAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayOneAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayOneAppointment){%><div id="appointment-${dayOneAppointment?.id}" class="appointmentDetailsCallOut">${dayOneAppointment?.service?.description}</div><%}%>
			</td>

			<%
				dayTwoAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayTwo.getTime() >= startDate.getTime() && dayTwo.getTime() < endDate.getTime()){
						dayTwoAppointment = it
					}
				}
			%>
			<td class=<%if(dayTwoDay == 1 || dayTwoDay == 7){%>"unavailable"<%}else if(dayTwoAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayTwoAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayTwoAppointment){%><div id="appointment-${dayTwoAppointment?.id}" class="appointmentDetailsCallOut">${dayTwoAppointment?.service?.description}</div><%}%>
			</td>

			<%
				dayThreeAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayThree.getTime() >= startDate.getTime() && dayThree.getTime() < endDate.getTime()){
						dayThreeAppointment = it
					}
				}
			%>
			<td class=<%if(dayThreeDay == 1 || dayThreeDay == 7){%>"unavailable"<%}else if(dayThreeAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayThreeAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayThreeAppointment){%><div id="appointment-${dayThreeAppointment?.id}" class="appointmentDetailsCallOut">${dayThreeAppointment?.service?.description}</div><%}%>
			</td>

			<%
				dayFourAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayFour.getTime() >= startDate.getTime() && dayFour.getTime() < endDate.getTime()){
						dayFourAppointment = it
					}
				}
			%>
			<td class=<%if(dayFourDay == 1 || dayFourDay == 7){%>"unavailable"<%}else if(dayFourAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayFourAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayFourAppointment){%><div id="appointment-${dayFourAppointment?.id}" class="appointmentDetailsCallOut">${dayFourAppointment?.service?.description}</div><%}%>
			</td>

			<%
				dayFiveAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayFive.getTime() >= startDate.getTime() && dayFive.getTime() < endDate.getTime()){
						dayFiveAppointment = it
					}
				}
			%>
			<td class=<%if(dayFiveDay == 1 || dayFiveDay == 7){%>"unavailable"<%}else if(dayFiveAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayFiveAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayFiveAppointment){%><div id="appointment-${dayFiveAppointment?.id}" class="appointmentDetailsCallOut">${dayFiveAppointment?.service?.description}</div><%}%>
			</td>

			<%
				daySixAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (daySix.getTime() >= startDate.getTime() && daySix.getTime() < endDate.getTime()){
						daySixAppointment = it
					}
				}
			%>
			<td class=<%if(daySixDay == 1 || daySixDay == 7){%>"unavailable"<%}else if(daySixAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(daySixAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (daySixAppointment){%><div id="appointment-${daySixAppointment?.id}" class="appointmentDetailsCallOut">${daySixAppointment?.service?.description}</div><%}%>
			</td>

			<%
				daySevenAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (daySeven.getTime() >= startDate.getTime() && daySeven.getTime() < endDate.getTime()){
						daySevenAppointment = it
					}
				}
			%>
			<td class=<%if(daySevenDay == 1 || daySevenDay == 7){%>"unavailable"<%}else if(daySevenAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(daySevenAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (daySevenAppointment){%><div id="appointment-${daySevenAppointment?.id}" class="appointmentDetailsCallOut">${daySevenAppointment?.service?.description}</div><%}%>
			</td>
		</tr>
		<%dayOne.add(Calendar.MINUTE, 15)%>
		<%dayTwo.add(Calendar.MINUTE, 15)%>
		<%dayThree.add(Calendar.MINUTE, 15)%>
		<%dayFour.add(Calendar.MINUTE, 15)%>
		<%dayFive.add(Calendar.MINUTE, 15)%>
		<%daySix.add(Calendar.MINUTE, 15)%>
		<%daySeven.add(Calendar.MINUTE, 15)%>
	<%}%>
	</table>

	<%
	Calendar dayEight = new GregorianCalendar()
	dayEight.setTime(startTime.getTime())
	dayEight.add(Calendar.DAY_OF_WEEK, 7)
	Calendar dayEightEndTime = new GregorianCalendar()
	dayEightEndTime.setTime(endTime.getTime())
	dayEightEndTime.add(Calendar.DAY_OF_WEEK, 7)
	Calendar dayNine = new GregorianCalendar()
	dayNine.setTime(startTime.getTime())
	dayNine.add(Calendar.DAY_OF_WEEK, 8)
	Calendar dayTen = new GregorianCalendar()
	dayTen.setTime(startTime.getTime())
	dayTen.add(Calendar.DAY_OF_WEEK, 9)
	Calendar dayEleven = new GregorianCalendar()
	dayEleven.setTime(startTime.getTime())
	dayEleven.add(Calendar.DAY_OF_WEEK, 10)
	Calendar dayTwelve = new GregorianCalendar()
	dayTwelve.setTime(startTime.getTime())
	dayTwelve.add(Calendar.DAY_OF_WEEK, 11)
	Calendar dayThirteen = new GregorianCalendar()
	dayThirteen.setTime(startTime.getTime())
	dayThirteen.add(Calendar.DAY_OF_WEEK, 12)
	Calendar dayFourteen = new GregorianCalendar()
	dayFourteen.setTime(startTime.getTime())
	dayFourteen.add(Calendar.DAY_OF_WEEK, 13)
	%>
	<table id="fourteenDayView-week2">
	<tr class="dateHeader">
		<td></td>
		<td>${dayEight.getTime().format('EEE dd')}</td>
		<td>${dayNine.getTime().format('EEE dd')}</td>
		<td>${dayTen.getTime().format('EEE dd')}</td>
		<td>${dayEleven.getTime().format('EEE dd')}</td>
		<td>${dayTwelve.getTime().format('EEE dd')}</td>
		<td>${dayThirteen.getTime().format('EEE dd')}</td>
		<td>${dayFourteen.getTime().format('EEE dd')}</td>
		<td></td>
	</tr>
	<% while (dayEight < dayEightEndTime){ %>
		<tr class="halfHour" time="${dayEight.getTime().format('hh:mm a')}">
			<td rowspan="2" class="time">${dayEight.getTime().format('hh:mm a')}</td>
			<%
				def dayEightAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayEight.getTime() >= startDate.getTime() && dayEight.getTime() < endDate.getTime()){
						dayEightAppointment = it
					}
				}
				def dayEightDay = dayEight.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayEightDay == 1 || dayEightDay == 7){%>"unavailable"<%}else if(dayEightAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayEightAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayEightAppointment){%><div id="appointment-${dayEightAppointment?.id}" class="appointmentDetailsCallOut">${dayEightAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def dayNineAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayNine.getTime() >= startDate.getTime() && dayNine.getTime() < endDate.getTime()){
						dayNineAppointment = it
					}
				}
				def dayNineDay = dayNine.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayNineDay == 1 || dayNineDay == 7){%>"unavailable"<%}else if(dayNineAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayNineAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayNineAppointment){%><div id="appointment-${dayNineAppointment?.id}" class="appointmentDetailsCallOut">${dayNineAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def dayTenAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayTen.getTime() >= startDate.getTime() && dayTen.getTime() < endDate.getTime()){
						dayTenAppointment = it
					}
				}
				def dayTenDay = dayTen.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayTenDay == 1 || dayTenDay == 7){%>"unavailable"<%}else if(dayTenAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayTenAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayTenAppointment){%><div id="appointment-${dayTenAppointment?.id}" class="appointmentDetailsCallOut">${dayTenAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def dayElevenAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayEleven.getTime() >= startDate.getTime() && dayEleven.getTime() < endDate.getTime()){
						dayElevenAppointment = it
					}
				}
				def dayElevenDay = dayEleven.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayElevenDay == 1 || dayElevenDay == 7){%>"unavailable"<%}else if(dayElevenAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayElevenAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayElevenAppointment){%><div id="appointment-${dayElevenAppointment?.id}" class="appointmentDetailsCallOut">${dayElevenAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def dayTwelveAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayTwelve.getTime() >= startDate.getTime() && dayTwelve.getTime() < endDate.getTime()){
						dayTwelveAppointment = it
					}
				}
				def dayTwelveDay = dayTwelve.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayTwelveDay == 1 || dayTwelveDay == 7){%>"unavailable"<%}else if(dayTwelveAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayTwelveAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayTwelveAppointment){%><div id="appointment-${dayTwelveAppointment?.id}" class="appointmentDetailsCallOut">${dayTwelveAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def dayThirteenAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayThirteen.getTime() >= startDate.getTime() && dayThirteen.getTime() < endDate.getTime()){
						dayThirteenAppointment = it
					}
				}
				def dayThirteenDay = dayThirteen.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayThirteenDay == 1 || dayThirteenDay == 7){%>"unavailable"<%}else if(dayThirteenAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayThirteenAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayThirteenAppointment){%><div id="appointment-${dayThirteenAppointment?.id}" class="appointmentDetailsCallOut">${dayThirteenAppointment?.service?.description}</div><%}%>
			</td>

			<%
				def dayFourteenAppointment
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayFourteen.getTime() >= startDate.getTime() && dayFourteen.getTime() < endDate.getTime()){
						dayFourteenAppointment = it
					}
				}
				def dayFourteenDay = dayFourteen.get(Calendar.DAY_OF_WEEK)
			%>
			<td class=<%if(dayFourteenDay == 1 || dayFourteenDay == 7){%>"unavailable"<%}else if(dayFourteenAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayFourteenAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayFourteenAppointment){%><div id="appointment-${dayFourteenAppointment?.id}" class="appointmentDetailsCallOut">${dayFourteenAppointment?.service?.description}</div><%}%>
			</td>
			

			<td rowspan="2" class="time">${dayEight.getTime().format('hh:mm a')}</td>
		</tr>

		<%dayEight.add(Calendar.MINUTE, 15)%>
		<%dayNine.add(Calendar.MINUTE, 15)%>
		<%dayTen.add(Calendar.MINUTE, 15)%>
		<%dayEleven.add(Calendar.MINUTE, 15)%>
		<%dayTwelve.add(Calendar.MINUTE, 15)%>
		<%dayThirteen.add(Calendar.MINUTE, 15)%>
		<%dayFourteen.add(Calendar.MINUTE, 15)%>

		<tr class="fifteen" time="${dayEight.getTime().format('hh:mm a')}">
			<%
				dayEightAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayEight.getTime() >= startDate.getTime() && dayEight.getTime() < endDate.getTime()){
						dayEightAppointment = it
					}
				}
			%>
			<td class=<%if(dayEightDay == 1 || dayEightDay == 7){%>"unavailable"<%}else if(dayEightAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayEightAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayEightAppointment){%><div id="appointment-${dayEightAppointment?.id}" class="appointmentDetailsCallOut">${dayEightAppointment?.service?.description}</div><%}%>
			</td>

			<%
				dayNineAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayNine.getTime() >= startDate.getTime() && dayNine.getTime() < endDate.getTime()){
						dayNineAppointment = it
					}
				}
			%>
			<td class=<%if(dayNineDay == 1 || dayNineDay == 7){%>"unavailable"<%}else if(dayNineAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayNineAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayNineAppointment){%><div id="appointment-${dayNineAppointment?.id}" class="appointmentDetailsCallOut">${dayNineAppointment?.service?.description}</div><%}%>
			</td>

			<%
				dayTenAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayTen.getTime() >= startDate.getTime() && dayTen.getTime() < endDate.getTime()){
						dayTenAppointment = it
					}
				}
			%>
			<td class=<%if(dayTenDay == 1 || dayTenDay == 7){%>"unavailable"<%}else if(dayTenAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayTenAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayTenAppointment){%><div id="appointment-${dayTenAppointment?.id}" class="appointmentDetailsCallOut">${dayTenAppointment?.service?.description}</div><%}%>
			</td>

			<%
				dayElevenAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayEleven.getTime() >= startDate.getTime() && dayEleven.getTime() < endDate.getTime()){
						dayElevenAppointment = it
					}
				}
			%>
			<td class=<%if(dayElevenDay == 1 || dayElevenDay == 7){%>"unavailable"<%}else if(dayElevenAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayElevenAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayElevenAppointment){%><div id="appointment-${dayElevenAppointment?.id}" class="appointmentDetailsCallOut">${dayElevenAppointment?.service?.description}</div><%}%>
			</td>

			<%
				dayTwelveAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayTwelve.getTime() >= startDate.getTime() && dayTwelve.getTime() < endDate.getTime()){
						dayTwelveAppointment = it
					}
				}
			%>
			<td class=<%if(dayTwelveDay == 1 || dayTwelveDay == 7){%>"unavailable"<%}else if(dayTwelveAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayTwelveAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayTwelveAppointment){%><div id="appointment-${dayTwelveAppointment?.id}" class="appointmentDetailsCallOut">${dayTwelveAppointment?.service?.description}</div><%}%>
			</td>

			<%
				dayThirteenAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayThirteen.getTime() >= startDate.getTime() && dayThirteen.getTime() < endDate.getTime()){
						dayThirteenAppointment = it
					}
				}
			%>
			<td class=<%if(dayThirteenDay == 1 || dayThirteenDay == 7){%>"unavailable"<%}else if(dayThirteenAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayThirteenAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayThirteenAppointment){%><div id="appointment-${dayThirteenAppointment?.id}" class="appointmentDetailsCallOut">${dayThirteenAppointment?.service?.description}</div><%}%>
			</td>

			<%
				dayFourteenAppointment = null
				appointments.each(){
					Calendar startDate = new GregorianCalendar()
					startDate.setTime(it.appointmentDate)
					Calendar endDate = new GregorianCalendar()
					endDate.setTime(it.appointmentDate)
					endDate.add(Calendar.MILLISECOND, new BigDecimal(it.service.duration).intValueExact())
					if (dayFourteen.getTime() >= startDate.getTime() && dayFourteen.getTime() < endDate.getTime()){
						dayFourteenAppointment = it
					}
				}
			%>
			<td class=<%if(dayFourteenDay == 1 || dayFourteenDay == 7){%>"unavailable"<%}else if(dayFourteenAppointment?.service?.description == "Blocked Off Time"){%>"blocked-off"<%}else if(dayFourteenAppointment){%>"booked"<%}else{%>"available"<%}%>>
				<%if (dayFourteenAppointment){%><div id="appointment-${dayFourteenAppointment?.id}" class="appointmentDetailsCallOut">${dayFourteenAppointment?.service?.description}</div><%}%>
			</td>
		</tr>
		<%dayEight.add(Calendar.MINUTE, 15)%>
		<%dayNine.add(Calendar.MINUTE, 15)%>
		<%dayTen.add(Calendar.MINUTE, 15)%>
		<%dayEleven.add(Calendar.MINUTE, 15)%>
		<%dayTwelve.add(Calendar.MINUTE, 15)%>
		<%dayThirteen.add(Calendar.MINUTE, 15)%>
		<%dayFourteen.add(Calendar.MINUTE, 15)%>
	<%}%>
	</table>


	<table class="appointments">
		<tr>
			<td width="120px"><h2>Name:</h2></td>
			<td><h2>Service:</h2></td>
			<td width="150px"><h2>Date:</h2></td>
			<td><h2>Notes:</h2></td>
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
					<td>
						<%if (it.notes){%>
							${it.notes}
						<%}else{%>
							<b>PHONE:</b> ${it.client.phone}  | <b>EMAIL:</b> <a href="mailto:${it.client.email}">${it.client.email}</a>
						<%}%>
					</td>
				</tr>
				<tr class="edit-appointment edit-appointment-${it.id}">
					<td></td>
					<td colspan="3">(x) <a href="${createLink(controller:'site',action:'cancelAppointment',params:[c:it.code])}" onclick="return confirm('Cancel appointment?')">cancel</a> | Reschedule: <span id="edit-appointment-options-${it.id}"></span> <img src="${resource(dir:'images', file:'spinner-gray.gif')}" class="spinner-${it.id}" style="display:none;"></td>
				</tr>
			<%}%>
		</g:each>
	</table>


	<div id="waitingSpinner" style="display:none;"><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" class="spinner"></div>



<script src="${resource(dir:'js', file:'jquery-1.10.2.min.js')}" type="text/javascript"></script>
<script src="${resource(dir:'js', file:'jquery-ui-1.10.3.custom.min.js')}" type="text/javascript"></script>
<script src="${resource(dir:'js', file:'jquery.confirmon.js')}"></script>
<script src="${resource(dir:'js', file:'a.min.js')}" type="text/javascript"></script>

</body></html>