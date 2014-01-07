package database.scripts

import salonManager.*

class version0_1 {

	def s1
	def webAdmin
	def kalin
	def client

	def init = { servletContext ->
		
		def dateService = new DateService()

		if (User.count() < 1){
			webAdmin = new User()
			webAdmin.username = "bjacobi"
			webAdmin.password = "atlantis"
			webAdmin.type = "101"
			webAdmin.firstName = "Ben"
			webAdmin.lastName = "Jacobi"
			webAdmin.code = "bj333"
			webAdmin.email = "bjacobi@gmail.com"
			webAdmin.save(flush:true)

			kalin = new User()
			kalin.username = "kpfanmiller"
			kalin.password = "kjp620300"
			kalin.firstName = "Kalin"
			kalin.lastName = "Pfanmiller"
			kalin.email = "kalin@thedenbarbershop-kc.com"
			kalin.type = "110"
			kalin.startTime = 36000000 //10AM
			kalin.endTime  = 68400000 //7PM
			kalin.code = "kp"
			kalin.save(flush:true)

			client = new User()
			client.username = "rriggle"
			client.password = "password"
			client.firstName = "Rob"
			client.lastName = "Riggle"
			client.email = "rriggle@funnyguy.com"
			client.type = "001"
			client.code = "rr"
			client.save(flush:true)
			
		}

		if (Service.count() < 1){
			println "creating services..."
			s1 = new Service(
				description: "Haircut",
				stylist: kalin,
				duration: dateService.getMillis([minutes:30]),
				displayOrder: new Long(1)
			).save(flush:true)
			new Service(
				description: "Hot Towel Shave",
				stylist: kalin,
				duration: dateService.getMillis([minutes:30]),
				displayOrder: new Long(2)
			).save()
			new Service(
				description: "Buzz Cut",
				stylist: kalin,
				duration: dateService.getMillis([minutes:15]),
				displayOrder: new Long(3)
			).save()
			new Service(
				description: "Boy's Cut",
				stylist: kalin,
				duration: dateService.getMillis([minutes:30]),
				displayOrder: new Long(4)
			).save()
			new Service(
				description: "Father & Son",
				stylist: kalin,
				duration: dateService.getMillis([minutes:45]),
				displayOrder: new Long(5)
			).save()
			new Service(
				description: "Haircut & Color Camo",
				stylist: kalin,
				duration: dateService.getMillis([minutes:45]),
				displayOrder: new Long(6)
			).save()
			new Service(
				description: "Haircut & Hot Towel Shave",
				stylist: kalin,
				duration: dateService.getMillis([minutes:60]),
				displayOrder: new Long(7)
			).save()
			new Service(
				description: "Haircut & Brow Detail",
				stylist: kalin,
				duration: dateService.getMillis([minutes:45]),
				displayOrder: new Long(8)
			).save()
			new Service(
				description: "Blocked Off Time",
				stylist: kalin,
				duration: dateService.getMillis([minutes:15]),
				display: false
			).save()
		}


		def message = ApplicationProperty.findByName("HOMEPAGE_MESSAGE") ?: new ApplicationProperty(name:"HOMEPAGE_MESSAGE", value:"Hey guys,<br><br>I would like to thank everyone who made the transition over to the new location, and welcome our three new stylists to Salon 1013!!!!<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Kalin").save()

	}

}