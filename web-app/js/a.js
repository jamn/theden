$(document).ready(function(){

	$('#username').bind('keyup', function(){
		var value = $(this).val()
		$(this).val(value.replace(/\s+/g, ''));
	});

	$(".recurringAppointmentAdminOptions").fadeOut();

	$('#log').click(function(e) {
		window.location.href = "../admin/getLog"
	});

	$('#logout').click(function(e) {
		window.location.href = "../access/logout"
	});

	$('#loginButton').click(function(e) {
		var user = $('#username').val();
		var password = $('#password').val();
		window.location.href = "../access/login?u="+user+"&p="+password
	});

	$('#saveTextButton').click(function(e) {
		var message = $('#homepageText').val();
		$.ajax({
			type: "POST",
			url: "./saveHomepageMessage",
			data: { m: message }
		}).done(function(response) {
			var success = response.search('"success":false');
			if (success === -1){
			}
			else{
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

	$('#clientsDetailsSelector').on('change', function() {
		var cId = $(this).val();
		$.ajax({
			type: "POST",
			url: "./getClientDetails",
			data: { cId:cId }
		}).done(function(response) {
			var success = response.search('"success":false');
			if (success === -1){
				$('#mask').fadeIn();
				$('#clientDetailsPopup').html(response).fadeIn();
			}
			else{
			}
		});
	});

	$('#mask').click(function(e) {
		closeClientDetailsPopup();
	});

	$(document).keyup(function(e){
		if(e.keyCode == 27){
			closeClientDetailsPopup();
		}
	});

	function closeClientDetailsPopup(){
		$('#mask').fadeOut();
		$('#clientDetailsPopup').fadeOut();
	}

	$('#chooseDateToBlockOff').datepicker( {
		minDate: 0
	});

	$('#fromWholeDay').datepicker( {
		minDate: 0
	});

	$('#toWholeDay').datepicker( {
		minDate: 0
	});

	$('#dateOfAppointment').datepicker( {
		minDate: 0
	});
	$('#dateOfAppointment').datepicker("setDate", new Date());

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
				$('#blockOffTimeButton').html("Success");
				$('#blockOffTimeButton').removeClass('errorButton animated fadeIn');
			}
			else{
				$('#blockOffTimeButton').html("Error");
				$('#blockOffTimeButton').addClass('errorButton animated fadeIn');
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
				$('#blockOffDaysButton').html("Success");
				$('#blockOffDaysButton').removeClass('errorButton animated fadeIn');
			}
			else{
				$('#blockOffDaysButton').html("Error");
				$('#blockOffDaysButton').addClass('errorButton animated fadeIn');
			}
		});
	});


	$('#recurringAppointment').click(function() {
		if ($(this).is(':checked')){
			$(".recurringAppointmentAdminOptions").fadeIn();
		}else{
			$(".recurringAppointmentAdminOptions").fadeOut();
		}
	});


	$('#bookForClientButton').confirmOn({
			classPrepend: 'confirmon',
			questionText: 'Book for client?',
			textYes: 'Yes',
			textNo: 'No'
		},'click', function(e, confirmed){
			if(confirmed){
				var cId = $('#clients').val();
				var sId = $('#services').val();
				var aDate = $('#dateOfAppointment').val();
				var sTime = $('#timeSlots').val();
				var recurringAppointment = $('#recurringAppointment').is(':checked');
				var repeatDuration = $('#repeatDuration').val();
				var repeatNumberOfAppointments = $('#repeatNumberOfAppointments').val();

				$('#bookForClientButton').html($('#waitingSpinner').html());

				$.ajax({
					type: "POST",
					url: "./bookForClient",
					data: { cId:cId, sId:sId, aDate:aDate, sTime:sTime, r:recurringAppointment, dur:repeatDuration, num:repeatNumberOfAppointments}
				}).done(function(response) {
					var jsonResponse = JSON.parse(response);
					if (jsonResponse.success === true){
						$('#bookForClientButton').html("Success");
						$('#bookForClientButton').removeClass('errorButton animated fadeIn');
					}
					else{
						$('#bookForClientButton').html("Error");
						$('#bookForClientButton').addClass('errorButton animated fadeIn');
					}
				});
			}
	});

	$('#services').on('change', function() {
		getTimeSlotOptions();
	});

	$('#dateOfAppointment').on('change', function() {
		getTimeSlotOptions();
	});


	function getTimeSlotOptions(){
		var sId = $('#services').val();
		var aDate = $('#dateOfAppointment').val();
		$.ajax({
			type: "POST",
			url: "./getTimeSlotOptions",
			data: { sId:sId, aDate:aDate}
		}).done(function(response) {
			if (response.indexOf("ERROR") > -1){
				$('#bookForClientButton').html("Error");
				$('#bookForClientButton').addClass('errorButton animated fadeIn');
			}else{
				$('#timeSlots').html(response);
				$('#bookForClientButton').html("Book Appointment");
				$('#bookForClientButton').removeClass('errorButton animated fadeIn');
			}
		});
	}



	$('.appointment-data').click(function(e) {
		$(".edit-appointment").slideUp();
		var aId = e.currentTarget.id;
		$.ajax({
			type: "POST",
			url: "./getRescheduleOptions",
			data: { aId:aId }
		}).done(function(response) {
			if (response.indexOf("ERROR") === -1){
				$("#edit-appointment-options-"+aId).html(response);
				$('#dateOfRescheduledAppointment-'+aId).datepicker( {
					minDate: 0
				});
				$('#dateOfRescheduledAppointment-'+aId).datepicker("setDate", new Date());
				$('#servicesForRescheduledAppointment-'+aId).on('change', function() {
					getTimeSlotOptionsForRescheduledAppointment(aId);
				});
				$('#dateOfRescheduledAppointment-'+aId).on('change', function() {
					getTimeSlotOptionsForRescheduledAppointment(aId);
				});
				$('#rescheduleButton-'+aId).on('click', function() {
					var sId = $('#servicesForRescheduledAppointment-'+aId).val();
					var aDate = $('#dateOfRescheduledAppointment-'+aId).val();
					var sTime = $('#timeSlotsForRescheduledAppointment-'+aId).val();
					$('.spinner-'+aId).fadeIn();
					$.ajax({
						type: "POST",
						url: "./rescheduleAppointment",
						data: { aId:aId, sId:sId, aDate:aDate, sTime:sTime }
					}).done(function(response) {
						var jsonResponse = JSON.parse(response);
						if (jsonResponse.success === true){
							$('#rescheduleButton-'+aId).html('Success');
							$('#rescheduleButton-'+aId).removeClass('errorButton animated fadeIn');
							setTimeout(function() {window.location.href = "./";},1250);
						}else{
							$('#rescheduleButton-'+aId).html('Error');
							$('#rescheduleButton-'+aId).addClass('errorButton animated fadeIn');
						}
					});
				});
			}
		});
		$(".edit-appointment-"+aId).slideDown();
	});


	$('#fourteenDayViewLink').click(function(e) {
		var text = $("#fourteenDayViewLink").html()
		if (text === "14 day view"){
			$('#fourteenDayView-week1').slideDown();
			$('#fourteenDayView-week2').slideDown();
			$("#fourteenDayViewLink").html("Hide 14 day view");
		}else{
			$('#fourteenDayView-week1').slideUp();
			$('#fourteenDayView-week2').slideUp();
			$("#fourteenDayViewLink").html("14 day view");
		}
	});


});

function getTimeSlotOptionsForRescheduledAppointment(aId){
	var sId = $('#servicesForRescheduledAppointment-'+aId).val();
	var aDate = $('#dateOfRescheduledAppointment-'+aId).val();
	$.ajax({
		type: "POST",
		url: "./getTimeSlotOptions",
		data: { sId:sId, aDate:aDate}
	}).done(function(response) {
		if (response.indexOf("ERROR") > -1){
			$('#timeSlotsForRescheduledAppointment-'+aId).html("<option>No times available</option>");
		}else{
			$('#timeSlotsForRescheduledAppointment-'+aId).html(response);
		}
	});
}

$('.cancel-appointment-link').click(function(e) {
	var c = $(this).attr('c');
	$.ajax({
		type: "POST",
		url: "./cancelAppointment",
		data: { c: c }
	}).done(function(response) {
		var success = response.search('"success":false');
		if (success === -1){
			alert('You did it, appoiment deleted!');
			location.reload();
		}
		else{
			alert('That didn\'t work. Dang.');
		}
	});
});







