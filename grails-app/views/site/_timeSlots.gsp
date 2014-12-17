<div class="row time-slots">
	<g:if test="${timeSlotsMap.size() > 0}">
		<g:each in="${timeSlotsMap}" var="timeSlots">
			
			<%if (timeSlots.key == 'morning'){%>
				<div class="col-sm-4 morning">
					<h2>Morning</h2>
					<g:each in="${timeSlots.value}">
						<div startTime="${it.startTime}" class="green-button time-slot" id="time-slot-${it.id}"><div class="as-button-label">${it.timeSlot}</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>
					</g:each>
				</div>
			<%}%>
			<%if (timeSlots.key == 'lunch'){%>
				<div class="col-sm-4 lunch">
					<h2>Lunch</h2>
					<g:each in="${timeSlots.value}">
						<div startTime="${it.startTime}" class="green-button time-slot" id="time-slot-${it.id}"><div class="as-button-label">${it.timeSlot}</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>
					</g:each>
				</div>
			<%}%>
			<%if (timeSlots.key == 'afternoon'){%>
				<div class="col-sm-4 afternoon">
					<h2>Afternoon</h2>
					<g:each in="${timeSlots.value}">
						<div startTime="${it.startTime}" class="green-button time-slot" id="time-slot-${it.id}"><div class="as-button-label">${it.timeSlot}</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>
					</g:each>
				</div>
			<%}%>
		</g:each>
	</g:if>
	<g:else>
		<div class="col-xs-offset-2 col-xs-8 col-sm-offset-3 col-sm-6 noTimesAvailableMessage">
			<p>No times available on this day</p>
		</div>
	</g:else>
</div>