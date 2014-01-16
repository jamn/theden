package salonManager

class EmailService {

	public sendEmailConfirmation(List appointments){
		println "Sending email confirmation for appointment(s): "
		def emailBody = "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p>We have you down for the following appointment(s):</p><ul>"
		appointments.each(){ appointment ->
			println "    - " + appointment.client.getFullName() + " | " + appointment.service.description + " on " + appointment.appointmentDate.format('MM/dd/yy @ hh:mm a')
			emailBody += "<li><b>${appointment.appointmentDate.format('MMMM dd, yyyy')}</b> @ <b>${appointment.appointmentDate.format('hh:mm a')}</b><br/>"
			emailBody += "&nbsp;&nbsp;&nbsp;&nbsp;reschedule: <a href='http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"'>http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"</a><br/>"
			emailBody += "&nbsp;&nbsp;&nbsp;&nbsp;cancel: <a href='http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"'>http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"</a></li>"
		}
		emailBody += "</ul>"

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
			def adminEmailBody = "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p><b>Client:</b> ${appointments[0].client.firstName} ${appointments[0].client.lastName}<br/><b>Service:</b> ${appointments[0].service.description}<br/><b>Time(s):</b> "
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
		def emailBody = "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p>Your appointment has been rescheduled. Your new appointment date is: <b>${appointment.appointmentDate.format('MMMM dd, yyyy')}</b> @ <b>${appointment.appointmentDate.format('hh:mm a')}</b>. In the event you need to reschedule, please use this link:</p><p><a href='http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"'>http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"</a></p><p>To cancel your appointment, please use the following link:</p><p><a href='http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"'>http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"</a></p><p>-Kalin</p>"
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
}