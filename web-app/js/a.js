$(document).ready(function(){

	$('#logout').click(function(e) {
		console.log("logging out");
		window.location.href = "../access/logout"
	});

	$('#loginButton').click(function(e) {
		var user = $('#username').val();
		var password = $('#password').val();
		window.location.href = "../access/login?u="+user+"&p="+password
	});

	$('#saveTextButton').click(function(e) {
		console.log("saveTextButton clicked!");
		var message = $('#homepageText').val();
		$.ajax({
			type: "POST",
			url: "./saveHomepageMessage",
			data: { m: message }
		}).done(function(response) {
			var success = response.search('"success":false');
			if (success === -1){
				console.log("success:true");
			}
			else{
				console.log("success:false");
			}
		});
	});

	$('#password').keypress(function(e) {
		var user = $('#username').val();
		var password = $('#password').val();
		if (e.keyCode === 13){
			window.location.href = "../access/login?u="+user+"&p="+password
		}
	});

	$('#chooseDateToBlockOff').datepicker( {
		minDate: 0
	});

	$('#fromWholeDay').datepicker( {
		minDate: 0
	});

	$('#toWholeDay').datepicker( {
		minDate: 0
	});

	$('#blockOffTimeButton').click(function(e) {
		var date = $('#chooseDateToBlockOff').val();
		var fromHour = $('#fromHour').val();
		var fromMinute = $('#fromMinute').val();
		var fromMorningOrAfternoon = $('#fromMorningOrAfternoon').val();
		var toHour = $('#toHour').val();
		var toMinute = $('#toMinute').val();
		var toMorningOrAfternoon = $('#toMorningOrAfternoon').val();
		var from = fromHour + ":" + fromMinute + fromMorningOrAfternoon;
		var to = toHour + ":" + toMinute + toMorningOrAfternoon;

		$('#blockOffTimeButton').html($('#waitingSpinner').html());

		$.ajax({
			type: "POST",
			url: "./blockOffTime",
			data: { date:date, from:from, to:to}
		}).done(function(response) {
			var jsonResponse = JSON.parse(response);
			if (jsonResponse.success === true){
				console.log("success:true");
				$('#blockOffTimeButton').html("Success");
			}
			else{
				console.log("success:false");
				$('#blockOffTimeButton').html("Error");
			}
		});
	});

	$('#blockOffDaysButton').click(function(e) {
		var from = $('#fromWholeDay').val();
		var to = $('#toWholeDay').val();

		$('#blockOffDaysButton').html($('#waitingSpinner').html());

		$.ajax({
			type: "POST",
			url: "./blockOffWholeDay",
			data: { from:from, to:to}
		}).done(function(response) {
			var jsonResponse = JSON.parse(response);
			if (jsonResponse.success === true){
				console.log("success:true");
				$('#blockOffDaysButton').html("Success");
			}
			else{
				console.log("success:false");
				$('#blockOffDaysButton').html("Error");
			}
		});
	});

	$('#fourteenDayViewLink').click(function(e) {
		var text = $("#fourteenDayViewLink").html()
		if (text === "14 day view"){
			$('#fourteenDayView-week1').fadeIn();
			$('#fourteenDayView-week2').fadeIn();
			$("#fourteenDayViewLink").html("Hide 14 day view");
		}else{
			$('#fourteenDayView-week1').fadeOut();
			$('#fourteenDayView-week2').fadeOut();
			$("#fourteenDayViewLink").html("14 day view");
		}
	});

	$('.appointment-data').click(function(e) {
		$(".edit-appointment").fadeOut();
		$(".edit-appointment-"+e.currentTarget.id).fadeIn();
	});




});