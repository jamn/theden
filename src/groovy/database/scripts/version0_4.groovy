package database.scripts

import salonManager.*

class version0_4 {

	def init = { servletContext ->

		def service1 = Service.findWhere(description: "Haircut")
		service1.price = 0
		service1.save()
		def service2 = Service.findWhere(description: "Hot Towel Shave")
		service2.price = 0 
		service2.save()
		def service3 = Service.findWhere(description: "Buzz Cut")
		service3.price = 0 
		service3.save()
		def service4 = Service.findWhere(description: "Boy's Cut")
		service4.price = 0 
		service4.save()
		def service5 = Service.findWhere(description: "Father & Son")
		service5.price = 0 
		service5.save()
		def service6 = Service.findWhere(description: "Haircut & Color Camo")
		service6.price = 0 
		service6.save()
		def service7 = Service.findWhere(description: "Haircut & Hot Towel Shave")
		service7.price = 0 
		service7.save()
		def service8 = Service.findWhere(description: "Haircut & Brow Detail")
		service8.price = 0 
		service8.save()
		def service9 = Service.findWhere(description: "Blocked Off Time")
		service9.price = 0 
		service9.save()

		def message = ApplicationProperty.findByName("JOBS__SEND_APPOINTMENT_REMINDER_EMAIL_JOB__ACTIVE") ?: new ApplicationProperty(name:"JOBS__SEND_APPOINTMENT_REMINDER_EMAIL_JOB__ACTIVE", value:"1").save()

	}
}