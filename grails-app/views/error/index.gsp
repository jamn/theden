<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html><head>
<meta charset="UTF-8">
<title>:: The Den Barbershop | Cancel Appointment ::</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'reset.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'stylz.css?v0.3')}" />
<link media="handheld, only screen" href="${resource(dir:'css', file:'mobile.css')}" type="text/css" rel="stylesheet" />

</head><body baseUrl="${createLink()}">


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
		
		<div class="server-error-message">An error has occured. Click <span class="home">here</span> to start over.</div>

		<div class="address">1013 W 47th Street &bull; KCMO, 64112</div>
		<div class="google-map" style="display:none;">
			<img src="${resource(dir: 'images', file: 'map.png')}">
		</div>
		
		<div class="footer">&nbsp;</div>

		<script src="${resource(dir:'js', file:'jquery-1.10.2.min.js')}" type="text/javascript"></script>
		<script src="${resource(dir:'js', file:'jquery-ui-1.10.3.custom.min.js')}" type="text/javascript"></script>
		<script src="${resource(dir:'js', file:'jquery.mobile-1.3.2.min.js')}" type="text/javascript"></script>
		<script src="${resource(dir:'js', file:'application.min.js?v0.3')}" type="text/javascript"></script>
		
</body></html>