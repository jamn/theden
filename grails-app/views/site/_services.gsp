<h1>Select A Service</h1>
<div class="col-xs-12 col-sm-offset-3 col-sm-6">
	<g:each in="${services}" var="service">
		<div service="${service.description}" class="as-button green-button service" id="service-${service.id}"><div class="as-button-label">${service.description} ($${service.price})</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>
	</g:each>
</div>