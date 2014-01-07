package database.scripts

import salonManager.*

class version0_2 {

	def init = { servletContext ->

		def kalin = User.findByCode("kp")
		def client = User.findByCode("rr")
		def service = Service.findWhere(stylist:kalin, description:"Haircut")

		def date = new Date()
		date.setHours(11)
		date.setMinutes(0)
		date.setSeconds(0)
		def appointment = Appointment.findByAppointmentDate(date) ?: new Appointment(appointmentDate:date, stylist:kalin, client:client, service:service).save()

	}

}