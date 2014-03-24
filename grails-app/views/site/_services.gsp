<h1>Select A Service</h1>
<g:each in="${services}" var="service">
	<div service="${service.description}" class="green-button service" id="service-${service.id}"><div class="label">${service.description} ($${service.price})</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>
</g:each>