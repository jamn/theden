var weekday=new Array(7);
weekday[0]="Sunday";
weekday[1]="Monday";
weekday[2]="Tuesday";
weekday[3]="Wednesday";
weekday[4]="Thursday";
weekday[5]="Friday";
weekday[6]="Saturday";

$(document).on("tap", ".home", function() {
	window.location.href = "/";
});

$(document).on("tap", ".jackson", function() {
	$(this).fadeOut();
});

$(document).ready(function(){

	$("#newAddress").click(function() {
		$('html, body').animate({
			scrollTop: $("#address").offset().top
		}, 2000);
		if ($(".google-map").is(":visible")){
			// $(".google-map").slideUp();
		}
		else{
			$(".google-map").slideDown();
		}
	});

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

	$('#recurringAppointment').click(function() {
		if ($(this).is(':checked')){
			$("#recurringAppointmentOptions").slideDown();
		}else{
			$("#recurringAppointmentOptions").slideUp();
		}
	});

	$('.address').click(function() {
		if ($(".google-map").is(":visible")){
			$(".google-map").slideUp();
		}
		else{
			$(".google-map").slideDown();
		}
	});
	
	$('.google-map').click(function() {
		var url = "https://local.google.com/maps/ms?msid=203990239811952052030.0004de4cdb5c11e4fc186&msa=0";
		var windowName = "Directions to The Den";
		window.open(url, windowName, "height=600,width=900");
	});
});

$(document).on("tap", "#bookNowButton", function() {
	var disabled = $(this).attr("disabled");
	if (disabled != "disabled") {
		disableButton(this);
		var data = { u: "kp" }
		var nextPage = "services"
		getPageContent(this, data, nextPage);
	}
});

$(document).on("tap", ".service", function() {
	var disabled = $(this).attr("disabled");
	if (disabled != "disabled") {
		disableButton(this);
		var service = this.getAttribute("service");
		var date = $('#chooseDate').val();
		var data = {s: service, d: date}
		var nextPage = "timeSlots"
		getPageContent(this, data, nextPage);
	}
});

$(document).on("tap", ".time-slot", function() {
	var disabled = $(this).attr("disabled");
	if (disabled != "disabled") {
		disableButton(this);
		var date = this.getAttribute("starttime");
		var recurringAppointment = $('#recurringAppointment').is(':checked');
		var repeatDuration = $('#repeatDuration').val();
		var repeatNumberOfAppointments = $('#repeatNumberOfAppointments').val();
		var data = {d: date, r: recurringAppointment, dur: repeatDuration, num: repeatNumberOfAppointments}
		var nextPage = "login"
		getPageContent(this, data, nextPage);
	}
});

$(document).on("tap", "#registerLink", function() {
	$('.right-divider').show();
	$('#password').show();
	$('#resetPassword').show();
	$('#showLoginForm').show();

	$('.left-divider').hide();
	$('#registerLink').hide();

	$('.new-user').slideDown();
	$('.new-user').addClass('animated fadeIn');

	$('.errorDetails').slideUp();

	$('#loginButton').attr("sendPasswordResetEmail", false);
	$('#loginButton').attr("attemptPasswordReset", false);
	$('#loginButton').removeClass('errorButton animated fadeIn');
	$('#loginButton .as-button-label').text("Register & Book");
});

$(document).on("tap", "#resetPassword", function() {
	$('.right-divider').hide();
	$('.left-divider').show();

	$('#password').hide();

	$('#resetPassword').hide();

	$('#registerLink').show();

	$('#showLoginForm').show();

	$('.new-user').slideUp();
	$('.new-user').removeClass('animated fadeIn');
	
	$('.errorDetails').slideUp();
	
	$('#loginButton').attr("sendPasswordResetEmail", true);
	$('#loginButton').attr("attemptPasswordReset", false);
	$('#loginButton').removeClass('errorButton animated fadeIn');
	$('#loginButton .as-button-label').text("Send Reset Email");
});

$(document).on("tap", "#showLoginForm", function() {
	$('.right-divider').hide();
	$('.left-divider').show();

	$('#password').show();

	$('#resetPassword').show();
	
	$('#registerLink').show();
	
	$('#showLoginForm').hide();
	
	$('.new-user').slideUp();
	$('.new-user').removeClass('animated fadeIn');

	$('.errorDetails').slideUp();
	
	$('#loginButton').attr("sendPasswordResetEmail", false);
	$('#loginButton').attr("attemptPasswordReset", false);
	$('#loginButton').removeClass('errorButton animated fadeIn');
	$('#loginButton .as-button-label').text("Book Appointment");
});

$(document).on("tap", "#loginButton", function() {
	var disabled = $(this).attr("disabled");
	$('#loginButton').removeClass('errorButton animated fadeIn');

	if (disabled != "disabled"){
		$(this).attr("disabled", "disabled");
		if ($(this).attr("sendPasswordResetEmail") === "true"){
			sendPasswordResetEmail();
		}else if ($(this).attr("attemptPasswordReset") === "true"){
			attemptPasswordReset();
		}else{
			bookAppointment();
		}
	}
});

$(document).on("keypress", "#password", function() {
	if (event.keyCode === 13) {
		$('#loginButton').removeClass('errorButton animated fadeIn');
		bookAppointment();
	}
});

$(document).on("keypress", "#password2", function() {
	if (event.keyCode === 13) {
		$('#cancelAppointmentLoginButton').removeClass('errorButton animated fadeIn');
		cancelAppointment();
	}
});

$(document).on("keypress", "#verifyNewPassword", function() {
	if (event.keyCode === 13) {
		$('#loginButton').removeClass('errorButton animated fadeIn');
		attemptPasswordReset();
	}
});


function sendPasswordResetEmail(){
	var email = $('#email').val();
	$('#loginButton .as-button-label').hide();
	$('#loginButton .spinner').show();
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl+"site/sendPasswordResetEmail",
		data: {e:email}
	}).done(function(confirmation) {
		processResults(confirmation);
	});
}

function attemptPasswordReset(){
	var p1 = $('#newPassword').val();
	var p2 = $('#verifyNewPassword').val();
	$('#loginButton .as-button-label').hide();
	$('#loginButton .spinner').show();
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl+"site/attemptPasswordReset",
		data: {p1:p1, p2:p2}
	}).done(function(confirmation) {
		processResults(confirmation);
	});
}

function bookAppointment(){
	var email = $('#email').val();
	var email2 = $('#email2').val();
	var password = $('#password').val();
	var firstName = $('#firstName').val();
	var lastName = $('#lastName').val();
	var phoneNumber = $('#phoneNumber').val();
	$('#loginButton .as-button-label').hide();
	$('#loginButton .spinner').show();
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl+"site/bookAppointment",
		data: {e:email, hp:email2, p:password, f:firstName, l:lastName, ph:phoneNumber}
	}).done(function(confirmation) {
		processResults(confirmation);
	});
}

function processResults(confirmation){
	var success = confirmation.search('"success":false')
	if (success > -1){
		var results = JSON && JSON.parse(confirmation) || $.parseJSON(confirmation);
		$('#loginButton .spinner').hide();
		$('#loginButton .as-button-label').show();
		$('#loginButton').attr("disabled", false);
		$('#loginButton').addClass('errorButton animated fadeIn');

		$('.errorDetails').html(results.errorMessage);
		$('.errorDetails').slideDown();
	}
	else{
		$('.login').removeClass('animated fadeInRightBig');
		$('.login').addClass('animated fadeOut');
		$('.login').slideUp();
		$('.main-content-area').removeClass('animated fadeInRightBig');
		$('.main-content-area').addClass('animated fadeOut');
		$('.main-content-area').slideUp();
		$('.confirmation').append(confirmation);
		$('.confirmation').slideDown();
		$('.confirmation').removeClass('animated fadeOut');
		$('.confirmation').addClass('animated fadeInRightBig');
	}
}

function processCancelAppointmentResults(confirmation){
	var success = confirmation.search('"success":false')
	if (success > -1){
		var results = JSON && JSON.parse(confirmation) || $.parseJSON(confirmation);
		$('#cancelAppointmentLoginButton .spinner').hide();
		$('#cancelAppointmentLoginButton .as-button-label').show();
		$('#cancelAppointmentLoginButton').attr("disabled", false);
		$('#cancelAppointmentLoginButton').addClass('errorButton animated fadeIn');

		$('.errorDetails').html(results.errorMessage);
		$('.errorDetails').slideDown();
	}
	else{
		$('.login').removeClass('animated fadeInRightBig');
		$('.login').addClass('animated fadeOut');
		$('.login').slideUp();
		$('.main-content-area').removeClass('animated fadeInRightBig');
		$('.main-content-area').addClass('animated fadeOut');
		$('.main-content-area').slideUp();
		$('.confirmation').append(confirmation);
		$('.confirmation').slideDown();
		$('.confirmation').removeClass('animated fadeOut');
		$('.confirmation').addClass('animated fadeInRightBig');
	}
}

function cancelAppointment(){
	var cancelAppointmentLoginButton = $('#cancelAppointmentLoginButton');
	var disabled = cancelAppointmentLoginButton.attr("disabled");
	cancelAppointmentLoginButton.removeClass('errorButton animated fadeIn');
	if (disabled != "disabled"){
		cancelAppointmentLoginButton.attr("disabled", "disabled");
		console.log('called cancel appointment.');
		var email = $('#email').val();
		var password = $('#password2').val();
		$('#cancelAppointmentLoginButton .as-button-label').hide();
		$('#cancelAppointmentLoginButton .spinner').show();
		var baseUrl = $('body').attr('baseUrl');
		$.ajax({
			type: "POST",
			url: baseUrl+"site/cancelAppointment",
			data: {e:email, p:password}
		}).done(function(confirmation) {
			processCancelAppointmentResults(confirmation);
		});
	}
}





function disableButton(button){
	$(button).attr("disabled", "disabled");
	$(button).find('.as-button-label').hide();
	$(button).find('.spinner').show();
}

function getPageContent(button, data, nextPage){
	var currentPage = $(button).closest(".page").attr("page");
	if (typeof currentPage != 'undefined' && typeof nextPage != 'undefined'){
		var baseUrl = $('body').attr('baseUrl');
		var action = "get" + nextPage.charAt(0).toUpperCase() + nextPage.slice(1)
		$.ajax({
			type: "POST",
			url: baseUrl+"site/"+action,
			data: data
		}).done(function(response) {
			var success = response.search('"success":false');
			if (success === -1){
				$('.main-content .page[page="'+currentPage+'"]').removeClass('fadeIn');
				$('.main-content .page[page="'+currentPage+'"]').addClass('animated fadeOut');
				$('.main-content .page[page="'+currentPage+'"]').slideUp()

				$('.main-content .page[page="'+nextPage+'"]').html(response);
				$('.main-content .page[page="'+nextPage+'"]').slideDown();
				$('.main-content .page[page="'+nextPage+'"]').removeClass('animated fadeOut');
				$('.main-content .page[page="'+nextPage+'"]').addClass('animated fadeInRightBig');
			}
			else{
				$(button).addClass('errorButton animated fadeIn');
				$(button).removeClass('errorButton animated fadeIn');
			}
		});
	}
}
