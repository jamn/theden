var weekday=new Array(7);
weekday[0]="Sunday";
weekday[1]="Monday";
weekday[2]="Tuesday";
weekday[3]="Wednesday";
weekday[4]="Thursday";
weekday[5]="Friday";
weekday[6]="Saturday";

$(document).ready(function(){

	$('#logoPlain').click(function() {
		window.location.href = "/"
	});

	$("#newAddress").click(function() {
		console.log("clicked new address");
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
			$.ajax({
				type: "POST",
				url: "./site/getAvailableTimes",
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
				else{
					// console.log("success:false");
				}
			});
		},
		minDate: 0,
		beforeShowDay: function(date) {
		 	var day = date.getDay();
		 	return [(day != 6 && day != 0)];
		}
	});

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

$(document).on("tap", ".book-now-button", function() {
	var disabled = $(this).attr("disabled");
	if (disabled != "disabled"){
		$(this).attr("disabled", "disabled");
		$('.book-now-button .label').hide();
		$('.book-now-button .spinner').show();
		$.ajax({
			type: "POST",
			url: "./site/getServices",
			data: { u: "kp" }
		}).done(function(services) {
			var success = services.search('"success":false');
			if (success === -1){
				$('.main-content-area').removeClass('fadeIn');
				$('.main-content-area').addClass('animated animated fadeOut');
				$('.main-content-area').slideUp()
				$('.select-a-service').append(services);
				$('.select-a-service').slideDown();
				$('.select-a-service').removeClass('animated fadeOut');
				$('.select-a-service').addClass('animated fadeInRightBig');
			}
			else{
				// console.log("success:false");
				$('.book-now-button').addClass('errorButton animated fadeIn');
				$('.book-now-button').removeClass('errorButton animated fadeIn');
			}
		});
	}

});

$(document).on("tap", ".service", function() {
	var disabled = $(this).attr("disabled");
	if (disabled != "disabled"){
		$(this).attr("disabled", "disabled");
		var id = this.id;
		$('#'+id+' .label').hide();
		$('#'+id+' .spinner').show();
		var service = this.getAttribute("service");
		var serviceButton = this;
		var date = $('#chooseDate').val();
		$.ajax({
			type: "POST",
			url: "./site/getAvailableTimes",
			data: {s: service, d: date}
		}).done(function(timeSlots) {
			var success = timeSlots.search('"success":false');
			if (success === -1){
				$('.select-a-service').removeClass('animated fadeInRightBig');
				$('.select-a-service').addClass('animated animated fadeOut');
				$('.select-a-service').slideUp();
				$('#timeSlots').append(timeSlots);
				$('.choose-a-time').slideDown();
				$('.choose-a-time').removeClass('animated fadeOut');
				$('.choose-a-time').addClass('animated fadeInRightBig');
			}
			else{
				// console.log("success:false");
				$(serviceButton).addClass('errorButton');
			}
		});
	}
});

$(document).on("tap", ".time-slot", function() {
	var disabled = $(this).attr("disabled");
	if (disabled != "disabled"){
		$(this).attr("disabled", "disabled");
		var id = this.id;
		$('#'+id+' .label').hide();
		$('#'+id+' .spinner').show();
		var date = this.getAttribute("starttime");
		var timeSlot = this;
		var recurringAppointment = $('#recurringAppointment').is(':checked');
		var repeatDuration = $('#repeatDuration').val();
		var repeatNumberOfAppointments = $('#repeatNumberOfAppointments').val();
		$.ajax({
			type: "POST",
			url: "./site/saveDate",
			data: {d: date, r: recurringAppointment, dur: repeatDuration, num: repeatNumberOfAppointments}
		}).done(function(loginForm) {
			var success = loginForm.search('"success":false');
			// console.log(success);
			if (success === -1){
				// DONE
				$('.choose-a-time').removeClass('animated fadeInRightBig');
				$('.choose-a-time').addClass('animated fadeOut');
				$('.choose-a-time').slideUp();
				$('.login').append(loginForm);
				$('.login').slideDown();
				$('.login').removeClass('animated fadeOut');
				$('.login').addClass('animated fadeInRightBig');
			}
			else{
				// console.log("success:false");
				$(timeSlot).addClass('errorButton');
				$(timeSlot).text("No longer available...");
				$(timeSlot).addClass('animated fadeOut');
			}
		});
	}
});

$(document).on("tap", "#registerLink", function() {
	$('.new-user').slideDown();
	$('.new-user').addClass('animated fadeIn');
	$('#registerLink').hide();
	$('#loginButton .label').text("Register & Book");
});

$(document).on("tap", "#loginButton", function() {
	var disabled = $(this).attr("disabled");
	if (disabled != "disabled"){
		$(this).attr("disabled", "disabled");
		bookAppointment();
	}
});

$(document).on("keypress", "#password", function() {
	if (event.keyCode == 13) {
		bookAppointment();
	}
});

function getAvailableTimes(){
	var date = $('#chooseDate').val();
	
}

function bookAppointment(){
	var email = $('#email').val();
	var email2 = $('#email2').val();
	var password = $('#password').val();
	var firstName = $('#firstName').val();
	var lastName = $('#lastName').val();
	var phoneNumber = $('#phoneNumber').val();
	$('#loginButton .label').hide();
	$('#loginButton .spinner').show();
	$.ajax({
		type: "POST",
		url: "./site/bookAppointment",
		data: {e: email, hp: email2, p: password, f: firstName, l: lastName, ph: phoneNumber}
	}).done(function(confirmation) {
		var success = confirmation.search('"success":false');
		if (success === -1){
			$('.login').removeClass('animated fadeInRightBig');
			$('.login').addClass('animated fadeOut');
			$('.login').slideUp();
			$('.confirmation').append(confirmation);
			$('.confirmation').slideDown();
			$('.confirmation').removeClass('animated fadeOut');
			$('.confirmation').addClass('animated fadeInRightBig');
		}
		else{
			$('#loginButton .spinner').hide();
			$('#loginButton .label').show();
			$('#loginButton').attr("disabled", false);
			$('#loginButton').addClass('errorButton animated fadeIn');
			// $('.login-box input').addClass('errorInput animated fadeIn');
			// $('#loginButton').removeClass('errorButton animated fadeIn');
		}
	});
}

