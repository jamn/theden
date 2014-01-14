<g:each in="${timeSlots}">
	<%def slash = it?.timeSlot?.indexOf('/')%>
	<option value="${it?.timeSlot.substring(0,slash-1)}">${it?.timeSlot}</option>
</g:each>