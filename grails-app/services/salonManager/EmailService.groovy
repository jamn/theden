package salonManager

class EmailService {

	public sendEmailConfirmation(List appointments){
		println "Sending email confirmation for appointment(s): "
		def emailBody = "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p>Hey "+appointments[0].client.firstName+". I have you down for the following appointment(s):</p><ul>"
		appointments.each(){ appointment ->
			println "    - " + appointment.client.getFullName() + " | " + appointment.service.description + " on " + appointment.appointmentDate.format('MM/dd/yy @ hh:mm a')
			emailBody += "<li>A <b>${appointment.service.description}</b> on ${appointment.appointmentDate.format('MM/dd @ hh:mm a [E]')}<br/>"
			emailBody += "&nbsp;&nbsp;&nbsp;&nbsp;reschedule: <a href='http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"'>http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"</a><br/>"
			emailBody += "&nbsp;&nbsp;&nbsp;&nbsp;cancel: <a href='http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"'>http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"</a></li>"
		}
		emailBody += "</ul><p>Thanks!</p>"

		try {
			sendMail {
				to "${appointments[0].client.email}"
				from "info@thedenbarbershop-kc.com"  
				subject "Appointment Booked @ The Den Barbershop"     
				html emailBody
			}
		}
		catch(Exception e) {
			println "ERROR"
			println e
		}
		try {
			def adminEmailBody = "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p><b>Client:</b> ${appointments[0].client.firstName} ${appointments[0].client.lastName}<br/><b>Phone:</b> ${appointments[0].client.phone}<br/><b>Email:</b> <a href='mailto:${appointments[0].client.email}'>${appointments[0].client.email}</a><br/><b>Service:</b> ${appointments[0].service.description}<br/><b>Time(s):</b> "
			appointments.eachWithIndex(){ appointment,index ->
				if (index > 0){
					adminEmailBody += " | "
				}
				adminEmailBody += "${appointment.appointmentDate.format('MMMM dd, yyyy @ hh:mm a')}"
			}
			adminEmailBody += "</p>"
			sendMail {     
				to "info@thedenbarbershop-kc.com"    
				from "info@thedenbarbershop-kc.com"    
				subject "New Appointment [${appointments[0].appointmentDate.format('MMMM dd, yyyy @ hh:mm a')}]"     
				html adminEmailBody
			}
		}
		catch(Exception e) {
			println "ERROR"
			println e
		}
	}

	public sendCancellationNotice(Appointment appointment){
		println "Sending cancellation notice: " + appointment.client.getFullName() + " | " + appointment.service.description + " on " + appointment.appointmentDate.format('MM/dd/yy @ hh:mm a')
		try {
			sendMail {     
				to "info@thedenbarbershop-kc.com"  
				from "info@thedenbarbershop-kc.com"  
				subject "** Appointment Cancelled ** [${appointment.appointmentDate.format('MMMM dd, yyyy @ hh:mm a')}]"     
				html "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p><b>Client:</b> ${appointment.client.firstName} ${appointment.client.lastName}<br/><b>Time:</b> ${appointment.appointmentDate.format('MMMM dd, yyyy @ hh:mm a')}<br/><b>Service:</b> ${appointment.service.description}</p>"
			}
		}
		catch(Exception e) {
			println "ERROR"
			println e
		}
	}

	public sendRescheduledConfirmation(Appointment appointment){
		println "Sending reschedule confirmation for appointment: " + appointment.client.getFullName() + " | " + appointment.service.description + " on " + appointment.appointmentDate.format('MM/dd/yy @ hh:mm a')
		def emailBody = "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p>${appointment.client.firstName},</p><p>Your appointment has been rescheduled. Your new appointment date is: <b>${appointment.appointmentDate.format('MM/dd @ hh:mm a [E]')}</b>. In the event you need to reschedule, please use this link:</p><p><a href='http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"'>http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"</a></p><p>To cancel your appointment, please use the following link:</p><p><a href='http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"'>http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"</a></p><p>Thanks!</p>"
		try {
			sendMail {
				to "${appointment.client.email}"
				from "info@thedenbarbershop-kc.com"  
				subject "Appointment Rescheduled @ The Den Barbershop"     
				html emailBody
			}
		}
		catch(Exception e) {
			println "ERROR"
			println e
		}
	}

	public sendReminderEmails(){
		Calendar almostTwentyFourHoursFromNow = new GregorianCalendar()
		almostTwentyFourHoursFromNow.add(Calendar.HOUR, 24)
		almostTwentyFourHoursFromNow.add(Calendar.MINUTE, -5)

		Calendar aLittleMoreThanTwentyFourHoursFromNow = new GregorianCalendar()
		aLittleMoreThanTwentyFourHoursFromNow.add(Calendar.HOUR, 24)
		aLittleMoreThanTwentyFourHoursFromNow.add(Calendar.MINUTE, 5)

		println "almostTwentyFourHoursFromNow: " + almostTwentyFourHoursFromNow.getTime()
		println "aLittleMoreThanTwentyFourHoursFromNow: " + aLittleMoreThanTwentyFourHoursFromNow.getTime()

		def appointment = Appointment.findByAppointmentDateBetween(almostTwentyFourHoursFromNow.getTime(), aLittleMoreThanTwentyFourHoursFromNow.getTime())
		println "appointment: " + appointment
		if (!appointment?.reminderEmailSent){
			println "Sending appointment reminder for: " + appointment.client.getFullName() + " | " + appointment.service.description + " on " + appointment.appointmentDate.format('MM/dd/yy @ hh:mm a')
			def emailBody = "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p>Hey ${appointment.client.firstName},</p><p>Just a reminder that your appointment for a ${appointment.service.description} is tomorrow at <b>${appointment.appointmentDate.format('hh:mm a')}</b>. In the event you need to reschedule, please use this link:</p><p><a href='http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"'>http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"</a></p><p>To cancel your appointment, please use the following link:</p><p><a href='http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"'>http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"</a></p><p>Thanks!</p>"
			try {
				sendMail {
					to "${appointment.client.email}"
					from "info@thedenbarbershop-kc.com"  
					subject "Appointment Reminder :: The Den Barbershop"     
					html emailBody
				}
				appointment.reminderEmailSent = true
				appointment.save()
			}
			catch(Exception e) {
				println "ERROR"
				println e
			}
		}
	}
}