package salonManager

import org.apache.commons.lang.RandomStringUtils
import java.text.SimpleDateFormat
import grails.converters.JSON

class AdminController {

	SimpleDateFormat dateFormatter = new SimpleDateFormat("EE dd MMM yyyy @ hh:mm a")
	SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MM/dd/yyyyhh:mma")
	SimpleDateFormat dateFormatter3 = new SimpleDateFormat("MM/dd/yyyy")

	def dateService
	def schedulerService
	def emailService

    def index() { 
    	println "\n" + new Date()
		println "params: " + params
    	if (session.adminUser){
    		Calendar now = new GregorianCalendar()
    		def now2 = new Date()
    		now.set(Calendar.HOUR_OF_DAY, 0)
			now.set(Calendar.MINUTE, 0)
			now.set(Calendar.SECOND, 0)
			now.set(Calendar.MILLISECOND, 0)
	    	def appointments = Appointment.findAllWhere(booked:true)?.findAll{it.appointmentDate > now.getTime()}?.sort{it.appointmentDate}
	    	now2 = new Date()
	    	def homepageText = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "ERROR: HOMEPAGE_MESSAGE record not found in the database. Tell Ben. He's good at fixing that stuff."
	    	now2 = new Date()
	    	def stylist = User.findByCode("kp")
	    	now2 = new Date()
	    	homepageText = homepageText.replace("<br>","\r")

	    	def clients = []
	    	User.list()?.each(){
	    		if (it.hasPermission("client")){
	    			clients.add(it)
	    		}
	    	}
	    	clients.sort{it.lastName}

	    	def services = Service.list().sort{it.displayOrder}.findAll{it.description != "Blocked Off Time"}

	    	def requestedDate = new Date()
	    	def service = services[0]
	    	def timeSlots = []
	    	schedulerService.getTimeSlotsAvailableMap(requestedDate, stylist, service)?.each(){ k,v ->
	    		timeSlots += v
	    	}

	    	def stylistStartTime = dateService.get24HourTimeValues(stylist.startTime)
			def stylistEndTime = dateService.get24HourTimeValues(stylist.endTime)

	    	Calendar startTime = new GregorianCalendar()
			startTime.set(Calendar.HOUR_OF_DAY, stylistStartTime.hour.intValue())
			startTime.set(Calendar.MINUTE, stylistStartTime.minute.intValue())
			startTime.set(Calendar.SECOND, 0)
			startTime.set(Calendar.MILLISECOND, 0)

			Calendar endTime = new GregorianCalendar()
			endTime.set(Calendar.HOUR_OF_DAY, stylistEndTime.hour.intValue())
			endTime.set(Calendar.MINUTE, stylistEndTime.minute.intValue())
			endTime.set(Calendar.SECOND, 0)
			endTime.set(Calendar.MILLISECOND, 0)

			now2 = new Date()

	    	return [appointments:appointments, homepageText:homepageText, stylist:stylist, startTime:startTime, endTime:endTime, clients:clients, services:services, timeSlots:timeSlots]
    	}else{
			println "ERROR: user in session is not an admin."
			println "    " + session
		}
    }

    def addExistingAppointments(){
    	println "\n" + new Date()
		println "params: " + params
    	if (session.adminUser){
			def baseDir = System.properties['base.dir'] + "/docs/"
			def file = new File(baseDir+"appointments.csv")
			def count = 0
			def kalin = User.findByCode("kp")
			def client = User.findByCode("kp")
			HashMap services = new HashMap()
			Service.list().each(){
				services.put(it.description, it)
			}
			HashSet appointments = new HashSet()
			Appointment.list()?.each(){
				appointments.add(it.appointmentDate)
			}
			file.eachCsvLine(){ tokens ->
				count++
				if (count > 1){
					def date = dateFormatter.parse(tokens[0])
					def service = services.get(tokens[3].replace("and", "&"))
					if (service){
						if (appointments.contains(date)){
							println "Time slot already booked"
						}
						else{
							def code = RandomStringUtils.random(14, true, true)
							def appointment = new Appointment(appointmentDate:date, stylist:kalin, client:client, service:service, booked:true, code:code)
							appointment.notes = "<b>NAME:</b> ${tokens[4]} | <b>PHONE:</b> ${tokens[5]} | <b>MOBILE:</b> ${tokens[6]} | <b>EMAIL:</b> ${tokens[7]}"
							appointment.save()
							if (appointment.hasErrors()){
								println "ERROR! Unable to save appointment: " + appointment.errors
							}
							else{
								println "Appointment saved! --> " + date
							}
						}
						
					}
					else{
						println "ERROR! Can't find a service description matching: " + tokens[3]
					}


				}
			}
			render "finshed"
		}else{
			println "ERROR: user in session is not an admin."
			println "    " + session
		}
	}

	def saveHomepageMessage(){
		println "\n" + new Date()
		println "params: " + params
		if (session.adminUser){
			def homepageText = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")
			homepageText.value = params.m.replace("\r", "<br>").replace("\n", "<br>")
			homepageText.save()
			if (homepageText.hasErrors()){
				println "ERROR: new message not saved."
				println homepageText.errors
			}
			else{
				println "SAVED!"
			}
		}else{
			println "ERROR: user in session is not an admin."
			println "    " + session
		}
	}

	def blockOffTime(){
		println "\n" + new Date()
		println "params: " + params
		if (session.adminUser){
			Boolean success = false
			Boolean appointmentFailedToSave = false
			try {
				def from = dateFormatter2.parse(params?.date+params?.from)
				def to = dateFormatter2.parse(params?.date+params?.to)

				def stylist = User.findByCode("kp")
				def service = Service.findByDescription("Blocked Off Time")
				Calendar currentDate = new GregorianCalendar()
				currentDate.setTime(from)

				while (currentDate.getTime() < to){
					def appointment = new Appointment()
					appointment.appointmentDate = currentDate.getTime()
					appointment.stylist = stylist
					appointment.service = service
					appointment.code = RandomStringUtils.random(14, true, true)
					appointment.client = stylist
					appointment.booked = true
					appointment.reminderEmailSent = true
					appointment.save(flush:true)
					if (appointment.hasErrors()){
						appointmentFailedToSave = true
						println "ERROR!"
						println appointment.errors
					}else{
						success = true
					}
					println "appointment: " + appointment
					currentDate.add(Calendar.MINUTE, 15)
				}
			}
			catch(Exception e) {
				println "ERROR: " + e
			}
			if (success && !appointmentFailedToSave){
				render ('{"success":true}') as JSON
			}else{
				render ('{"success":false}') as JSON
			}
		}else{
			println "ERROR: user in session is not an admin."
			println "    " + session
		}
	}

	def blockOffWholeDay(){
		println "\n" + new Date()
		println "params: " + params
		if (session.adminUser){
			Boolean success = false
			Boolean dayOffFailedToSave = false
			try {
				def from = dateFormatter3.parse(params?.from)
				def to = dateFormatter3.parse(params?.to)

				def stylist = User.findByCode("kp")
				Calendar currentDate = new GregorianCalendar()
				currentDate.setTime(from)
				currentDate.set(Calendar.HOUR_OF_DAY, 0)
				currentDate.set(Calendar.MINUTE, 0)
				currentDate.set(Calendar.SECOND, 0)
				currentDate.set(Calendar.MILLISECOND, 0)
				Calendar endDate = new GregorianCalendar()
				endDate.setTime(to)
				endDate.set(Calendar.HOUR_OF_DAY, 0)
				endDate.set(Calendar.MINUTE, 0)
				endDate.set(Calendar.SECOND, 0)
				endDate.set(Calendar.MILLISECOND, 0)

				while (currentDate.getTime() <= endDate.getTime()){
					def dayOff = new DayOff()
					dayOff.dayOffDate = currentDate.getTime()
					dayOff.stylist = stylist
					dayOff.save(flush:true)
					if (dayOff.hasErrors){
						dayOffFailedToSave = true
						println "ERROR!"
						println dayOff.errors
					}else{
						success = true
					}
					println "dayOff: " + dayOff
					currentDate.add(Calendar.DAY_OF_YEAR, 1)
				}
			}
			catch(Exception e) {
				println "ERROR: " + e
			}

			if (success && !dayOffFailedToSave){
				render ('{"success":true}') as JSON
			}else{
				render ('{"success":false}') as JSON
			}
		}
	}

	def bookForClient(){
		println "\n" + new Date()
		println "params: " + params
		Boolean success = false
		if (session.adminUser && params?.cId && params?.sId && params?.aDate && params?.sTime){
			success = schedulerService.bookForClient(params)
		}
		if (success){
			render ('{"success":true}') as JSON
		}
		else{
			render ('{"success":false}') as JSON
		}
	}

	def rescheduleAppointment(){
		println "\n" + new Date()
		println "params: " + params
		Boolean success = false
		if (session.adminUser && params?.aId && params?.sId && params?.aDate && params?.sTime){
			try {
				def existingApointment = Appointment.get(new Long(params.aId))
				params["cId"] = existingApointment.client.id
				params["rescheduledAppointment"] = "TRUE"
				success = schedulerService.bookForClient(params)
				if (success){
					schedulerService.deleteAppointment(existingApointment.id)
				}
			}
			catch(Exception e) {
				println "ERROR: " + e
			}
		}
		if (success){
			println "SUCCESS!"
			render ('{"success":true}') as JSON
		}
		else{
			println "ERROR!"
			render ('{"success":false}') as JSON
		}
	}

	def getTimeSlotOptions(){
		println "\n" + new Date()
		println "params: " + params
		def timeSlots = []
		if (session.adminUser && params?.aDate && params?.sId){
			def requestedDate = dateFormatter3.parse(params.aDate)
			def service = Service.get(new Long(params.sId))
			def stylist = User.findByCode("kp")
			schedulerService.getTimeSlotsAvailableMap(requestedDate, stylist, service)?.each(){ k,v ->
				timeSlots += v
			}
		}
		if (timeSlots.size() > 0){
			render (template: "timeSlotOptions", model: [timeSlots:timeSlots])
		}else{
			render "No times available this date"
		}
	}

	def getRescheduleOptions(){
		println "\n" + new Date()
		println "params: " + params
		def appointment
		if (session.adminUser && params?.aId){
			appointment = Appointment.get(new Long(params.aId))
		}
		if (appointment){
			def services = Service.list().sort{it.displayOrder}.findAll{it.description != "Blocked Off Time"}
			render (template: "rescheduleOptions", model: [appointment:appointment, services:services])
		}else{
			render "No appointment found"
		}
	}

	def getLog(){
		String log = ''
		try {
			log = new File('/var/log/tomcat7/catalina.out').text.replace('\n', '</br>').replace('\r', '</br>')
		}
		catch(Exception e) {
			println "ERROR: " + e	
		}
		render log
	}

}
