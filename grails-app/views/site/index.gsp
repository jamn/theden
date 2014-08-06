<!-- 
           .-"-.
         .'     '.
         |       |
          \     /
         [_______]
          |##' ,|
          |#' ,#|
          |' ,##|
          | ,###|		SHAVE AND A HAIRCUT,
          |,###'|		AND SOME CONVERSATION,
          |###' |		AND PROBABLY A BEER TOO.
          |##' ,|
          |#' ,#|
          |'_,##|
         [_______]
          \     /
           |   |
 -->

 <!-- Made by Ben Jacobi | benjacobi.com -->
 <!-- v0.3 -->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html><head>
<meta charset="UTF-8">
<title>:: The Den Barbershop ::</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'animate.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'reset.css')}" />
<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'stylz.css?v0.4')}" />
<link media="handheld, only screen" href="${resource(dir:'css', file:'mobile.css')}" type="text/css" rel="stylesheet" />

<link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'jquery-ui-1.10.3.custom.min.css')}" />

<link rel="apple-touch-icon" sizes="57x57" href="${resource(dir:'images', file:'apple-icon-57x57.png')}" />
<link rel="apple-touch-icon" sizes="72x72" href="${resource(dir:'images', file:'apple-icon-72x72.png')}" />
<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir:'images', file:'apple-icon-114x114.png')}" />
<link rel="apple-touch-icon" sizes="144x144" href="${resource(dir:'images', file:'apple-icon-144x144.png')}" />

</head><body  baseUrl="${createLink()}">

		<div class="header">
			<img class="home" id="logoPlain" src="${resource(dir:'images',file:'logo-plain.png')}">
			<div id="newAddress">1013 W 47th Street<br/>KCMO, 64112</div>
		</div>
		<div class="jackson">
			<img class="pic" src="${resource(dir:'images',file:'jackson.jpg')}">
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
		<div class="main-content-area">
			<div class="pictures">
				<img width="100%" src="${resource(dir:'images',file:'shop.jpg')}">
			</div>
			<div class="green-button book-now-button"><div class="label">Book Now</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>
		</div>

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
	<script src="${resource(dir:'js', file:'application.min.js?v0.3')}" type="text/javascript"></script>
	<script src="${resource(dir:'js', file:'masked-input-plugin.min.js')}" type="text/javascript"></script>

</body></html>