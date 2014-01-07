package salonManager

class EmailService {

	public sendEmailConfirmation(Appointment appointment){
		def emailBody = "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p>Your appointment is set for <b>${appointment.appointmentDate.format('MMMM dd, yyyy')}</b> @ <b>${appointment.appointmentDate.format('hh:mm a')}</b>. In the event you need to reschedule, please use this link:</p><p><a href='http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"'>http://www.thedenbarbershop-kc.com/site/modifyAppointment?a="+appointment.id+"&cc="+appointment.client.code+"</a></p><p>To cancel your appointment, please use the following link:</p><p><a href='http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"'>http://www.thedenbarbershop-kc.com/site/cancelAppointment?c="+appointment.code+"</a></p><p>-Kalin</p>"
		sendMail {
			to "${appointment.client.email}"     
			subject "Appointment Booked @ The Den Barbershop"     
			html emailBody
		}

		sendMail {     
			to "info@thedenbarbershop-kc.com"     
			subject "New Appointment [${appointment.appointmentDate.format('MMMM dd, yyyy @ hh:mm a')}]"     
			html "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p><b>Client:</b> ${appointment.client.firstName} ${appointment.client.lastName}<br/><b>Time:</b> ${appointment.appointmentDate.format('MMMM dd, yyyy @ hh:mm a')}<br/><b>Service:</b> ${appointment.service.description}</p>"
		}
	}

	public sendCancellationNotice(Appointment appointment){
		sendMail {     
			to "info@thedenbarbershop-kc.com"  
			subject "** Appointment Cancelled ** [${appointment.appointmentDate.format('MMMM dd, yyyy @ hh:mm a')}]"     
			html "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p><b>Client:</b> ${appointment.client.firstName} ${appointment.client.lastName}<br/><b>Time:</b> ${appointment.appointmentDate.format('MMMM dd, yyyy @ hh:mm a')}<br/><b>Service:</b> ${appointment.service.description}</p>"
		}
	}

}