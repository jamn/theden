function getTimeSlotOptions(){
	var sId = $('#services').val();
	var aDate = $('#dateOfAppointment').val();
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl + "/getTimeSlotOptions",
		data: { sId:sId, aDate:aDate}
	}).done(function(response) {
		if (response.indexOf("ERROR") > -1){
			$('#bookForClientButton').html("Error");
			$('#bookForClientButton').addClass('error-button animated fadeIn');
		}else{
			$('#timeSlots').html(response);
			$('#bookForClientButton').html("Book Appointment");
			$('#bookForClientButton').removeClass('error-button animated fadeIn');
		}
	});
}

function getTimeSlotOptionsForRescheduledAppointment(aId){
	var sId = $('#servicesForRescheduledAppointment-'+aId).val();
	var aDate = $('#dateOfRescheduledAppointment-'+aId).val();
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl + "/getTimeSlotOptions",
		data: { sId:sId, aDate:aDate}
	}).done(function(response) {
		if (response.indexOf("ERROR") > -1){
			$('#timeSlotsForRescheduledAppointment-'+aId).html("<option>No times available</option>");
		}else{
			$('#timeSlotsForRescheduledAppointment-'+aId).html(response);
		}
	});
}

$(document).on("tap", ".main", function() {
	var navMenuExpanded = ($('.navbar-collapse').attr('class').indexOf("in") >= 0);
	if(navMenuExpanded){
		$(".navbar-toggle").click();
	}
});

$(document).on('tap', '.nav a', function(e) {
	var section = $(this).attr("href");
	var baseUrl = $('body').attr('baseUrl');
	if (section === "#logout") {
		window.location.href = "./access/logout";
	}
	else {
		$('#mask').fadeIn();
		$.ajax({
			type: "POST",
			url: baseUrl + "/getSection",
			data: {section:section}
		}).done(function(response) {
			$('.active').removeClass("active");
			$('a[href="'+section+'"]').parent().addClass("active");
			$('.main').html(response);
			$(".navbar-toggle").click();
			$('#mask').fadeOut();
		});
	}
});

$(document).on('tap', '#addClient', function(e) {
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl + "/getClientDataForm"
	}).done(function(response) {
		if (response.indexOf("ERROR") > -1){
			alert('Dang... you broke it.');
		}else{
			$('#lastNameFilters').slideUp();
			$('#clientsDetailsSelector').slideUp();
			$('#clientDetails').slideUp();
			$('#addClient').fadeOut();
			$('#clientInfoForm').html(response).slideDown();
		}
	});
});

$(document).on('tap', '#editClient', function(e) {
	var cId = $("#clientsDetailsSelector").val();
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl + "/getClientDataForm",
		data: { cId:cId}
	}).done(function(response) {
		if (response.indexOf("ERROR") > -1){
			alert('Dang... you broke it.');
		}else{
			$('#lastNameFilters').slideUp();
			$('#clientsDetailsSelector').slideUp();
			$('#clientDetails').slideUp();
			$('#addClient').fadeOut();
			$('#clientInfoForm').html(response).slideDown();
		}
	});
});

$(document).on('tap', '.last-name-filter', function(e) {
	var lastNameStartsWith = $(e.currentTarget).attr('value');
	$("#clientsDetailsSelector").slideDown();
	if (lastNameStartsWith === "Reset"){
		console.log("here");
		getClientSelectMenuData();
		$('.reset-search').fadeOut();
	}
	else {
		$('.reset-search').fadeIn();
		getClientSelectMenuData(lastNameStartsWith);

	}
});

function getClientSelectMenuData(lastNameStartsWith){
	console.log("resetting clients drop down");
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl+"/getClientsSelectMenu",
		data: {lastNameStartsWith: lastNameStartsWith}
	}).done(function(confirmation) {
		var success = confirmation.search('"success":false');
		if (success === -1){
			$("#clientsDetailsSelector").html(confirmation);
		}
	});

}

$(document).on('tap', '#saveClientButton', function(e) {
	var email = $('#e').val();
	var email2 = $('#e2').val();
	var password = $('#p').val();
	var firstName = $('#f').val();
	var lastName = $('#l').val();
	var phoneNumber = $('#ph').val();
	var cId = $("#cId").val(); 
	if (email && password && firstName && lastName){
		$('#saveClientButton .label').hide();
		$('#saveClientButton .spinner').show();
		var baseUrl = $('body').attr('baseUrl');
		$.ajax({
			type: "POST",
			url: baseUrl+"/saveClient",
			data: {e:email, hp:email2, p:password, f:firstName, l:lastName, ph:phoneNumber, cId:cId}
		}).done(function(confirmation) {
			var success = confirmation.search('"success":false');
			if (success === -1){
				hideClientRegistrationForm();
				alert('User saved!');
			}
		});
	}
});

$(document).on('tap', '#cancelClientRegistrationButton', function(e) {
	hideClientRegistrationForm();
});

function hideClientRegistrationForm(){
	$('#clientInfoForm').slideUp();
	$('#clientsDetailsSelector').slideDown();
	$('#lastNameFilters').slideDown();
	$('#clientDetails').slideDown();
	$('#addClient').fadeIn();
}

$(document).on('tap', '.cancel-appointment-button', function(e) {
	var c = $(this).attr('c');
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl + "/cancelAppointment",
		data: { c: c }
	}).done(function(response) {
		var success = response.search('"success":false');
		if (success === -1){
			alert('*Appointment Deleted*');
			location.reload();
		}
		else{
			alert('That didn\'t work. Dang.');
		}
	});
});

$(document).on('change', '#clientsDetailsSelector', function() {
	$(this).slideUp();
	$('.reset-search').fadeIn();
	var cId = $(this).val();
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl + "/getClientDetails",
		data: { cId:cId }
	}).done(function(response) {
		var success = response.search('"success":false');
		if (success === -1){
			$('#clientDetails').html(response).fadeIn();
		}
	});
});

$(document).on('tap', '#saveTextButton', function(e) {
	var message = $('#homepageText').val();
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl + "/saveHomepageMessage",
		data: { m: message }
	});
});

$(document).on('tap', '#saveClientNotesButton', function(e) {
	var notes = encodeURIComponent($('#clientNotes').val());
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl + "/saveClientNotes",
		data: { n:notes }
	});
});



$(document).on('tap', '#blockOffTimeButton', function(e) {
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

	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl + "/blockOffTime",
		data: { date:date, from:from, to:to}
	}).done(function(response) {
		var jsonResponse = JSON.parse(response);
		if (jsonResponse.success === true){
			$('#blockOffTimeButton').html("Success");
			$('#blockOffTimeButton').removeClass('error-button animated fadeIn');
		}
		else{
			$('#blockOffTimeButton').html("Error");
			$('#blockOffTimeButton').addClass('error-button animated fadeIn');
		}
	});
});



$(document).on('tap', '#blockOffDaysButton', function(e) {
	var from = $('#fromWholeDay').val();
	var to = $('#toWholeDay').val();

	$('#blockOffDaysButton').html($('#waitingSpinner').html());

	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl + "/blockOffWholeDay",
		data: { from:from, to:to}
	}).done(function(response) {
		var jsonResponse = JSON.parse(response);
		if (jsonResponse.success === true){
			$('#blockOffDaysButton').html("Success");
			$('#blockOffDaysButton').removeClass('error-button animated fadeIn');
		}
		else{
			$('#blockOffDaysButton').html("Error");
			$('#blockOffDaysButton').addClass('error-button animated fadeIn');
		}
	});
});


$(document).on('tap', '#recurringAppointment', function() {
	var opts = $(".recurringAppointmentAdminOptions");
	if ($(opts).is(":visible")){
		$(opts).fadeOut();
	}else{
		$(opts).fadeIn();
	}
});

$(document).on('change', '#services', function() {
	getTimeSlotOptions();
});

$(document).on('change', '#dateOfAppointment', function() {
	getTimeSlotOptions();
});

$(document).on('tap', '.appointment-data', function(e) {
	$(".edit-appointment").slideUp();
	var aId = e.currentTarget.id;
	var baseUrl = $('body').attr('baseUrl');
	$.ajax({
		type: "POST",
		url: baseUrl + "/getRescheduleOptions",
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
			$('#rescheduleButton-'+aId).on('tap', function() {
				var sId = $('#servicesForRescheduledAppointment-'+aId).val();
				var aDate = $('#dateOfRescheduledAppointment-'+aId).val();
				var sTime = $('#timeSlotsForRescheduledAppointment-'+aId).val();
				$('.spinner-'+aId).fadeIn();
				$.ajax({
					type: "POST",
					url: baseUrl + "/rescheduleAppointment",
					data: { aId:aId, sId:sId, aDate:aDate, sTime:sTime }
				}).done(function(response) {
					var jsonResponse = JSON.parse(response);
					if (jsonResponse.success === true){
						$('#rescheduleButton-'+aId).html('Success');
						$('#rescheduleButton-'+aId).removeClass('error-button animated fadeIn');
						setTimeout(function() {window.location.href = "./";},1250);
					}else{
						$('#rescheduleButton-'+aId).html('Error');
						$('#rescheduleButton-'+aId).addClass('error-button animated fadeIn');
					}
				});
			});
		}
	});
	$(".edit-appointment-"+aId).slideDown();
});

