<g:render template="datePicker" />
<g:if test="${timeSlotsMap.size() > 0}">
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
</g:if>
<g:else>
	<div class="noTimesAvailableMessage">
		<p>No times available on this day</p>
	</div>
</g:else>


<script type="text/javascript">
	$(document).ready(function(){
		$('#chooseDate').datepicker( {
			onSelect: function(date) {
				var date = $('#chooseDate').val();
				var baseUrl = $('body').attr('baseUrl');
				$.ajax({
					type: "POST",
					url: baseUrl+"site/getAvailableTimes",
					data: {d: date}
				}).done(function(timeSlots) {
					var success = timeSlots.search('"success":false');
					if (success === -1){
						var d = new Date(date);
						$('#dateText').empty();
						$('#dateText').append(weekday[d.getDay()]);
						$('#timeSlots').empty();
						$('#timeSlots').append(timeSlots);
					}
				});
			},
			minDate: 0,
			beforeShowDay: function(date) {
			 	var day = date.getDay();
			 	return [(day != 6 && day != 0)];
			}
		});
		$('#chooseDate').datepicker("setDate", new Date());
	});
</script>