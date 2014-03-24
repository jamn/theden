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
<%if (timeSlotsMap.size() == 0){%>
	<div class="noTimesAvailableMessage">
		<p>No times available on this day</p>
	</div>
<%}%>