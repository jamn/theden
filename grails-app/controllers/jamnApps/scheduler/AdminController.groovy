package jamnApps.scheduler

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
	def userService

	/*********************************
				NAVIGATION
	**********************************/

    def index() { 
    	println "\n" + new Date()
		println "params: " + params
    	return [homepageText:getHomepageText().homepageText]
    }

    def getSection() {
    	println "\n" + new Date()
		println "params: " + params

    	List allowedSections = [
    		"homepageMessage", 
    		"clients",
    		"upcomingAppointments",
    		"bookAppointment",
    		"blockOffTime",
    		"log"
    	]

    	def template = ""
    	def model = [:]

    	// ASIGN TEMPLATE

    	def section = params?.section.replace("#", "")
    	if (section in allowedSections){
    		template = section
    	}

    	// RETRIEVE DATA FOR MODEL

    	if (template == "homepageMessage"){
    		model = getHomepageText()
    	}
    	else if (template == "clients"){
			model = getClients()
		}
		else if (template == "upcomingAppointments"){
			model = getUpcomingAppointments() + getStylistInfo()
		}
		else if (template == "bookAppointment"){
			model = getClients() + getServices()
		}
		else if (template == "log"){
			model = getLog()
		}

    	try {
    		render(template: template, model: model)
    	}
    	catch(Exception e) {
    		println "ERROR: " + e
    		render(template: "error")
    	}
    }

    private Map getHomepageText(){
    	def homepageText = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "ERROR: HOMEPAGE_MESSAGE record not found in the database. Tell Ben. He's good at fixing that stuff."
	    homepageText = homepageText.replace("<br>","\r")
	    return [homepageText:homepageText]
    }

    private Map getClients(){
		def clients = []
		User.list()?.each(){
			if (it.hasPermission("client")){
				clients.add(it)
			}
		}
		clients.sort{it.lastName}
		def filterLetters = clients.collect{it.lastName.substring(0,1).toUpperCase()}?.unique()
		return [clients:clients, filterLetters:filterLetters]
    }

    private Map getServices(){
    	def services = Service.list()?.sort{it?.displayOrder}.findAll{it?.description != "Blocked Off Time"} ?: []
    	return [services:services]
    }

    private Map getUpcomingAppointments(){
		Calendar now = new GregorianCalendar()
		now.set(Calendar.HOUR_OF_DAY, 0)
		now.set(Calendar.MINUTE, 0)
		now.set(Calendar.SECOND, 0)
		now.set(Calendar.MILLISECOND, 0)
		def appointments = Appointment.findAllWhere(booked:true)?.findAll{it.appointmentDate > now.getTime()}?.sort{it.appointmentDate}
		return [appointments:appointments]
    }

    private Map getStylistInfo(){
    	def stylist = User.findByCode("kp")

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

		return [startTime:startTime, endTime:endTime]
    }

    private Map getLog(){
		String log = ''
		try {
			log = new File('/var/log/tomcat7/catalina.out').text.replace('\n', '</br>').replace('\r', '</br>')
		}
		catch(Exception e) {
			println "ERROR: " + e
			log = 'No log to show.'
		}
		return [log:log]
	}



	/*********************************
				ENDPOINTS
	**********************************/

	def saveHomepageMessage(){
		println "\n" + new Date()
		println "params: " + params
		if (session.adminUser){
			def homepageText = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")
			homepageText.value = params.m.replace("\r", "<br>").replace("\n", "<br>")
			homepageText.save(flush:true)
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
		return true
	}

	def getClientDetails(){
		println "\n" + new Date()
		println "params: " + params
		if (params?.cId){
			def client = User.get(params.cId)
			session.editClient = client
			def appointments = Appointment.findAllWhere(client:client, booked:true)?.sort{it.appointmentDate}?.sort{it.appointmentDate}
			if (client){
				render (template: "client", model: [client:client, appointments:appointments])
			}
		}
		return true
	}

	def getClientDataForm(){
		println "\n" + new Date()
		println "params: " + params
    	def client
    	def submitText = "Register Client"
    	if (params?.cId){
    		client = User.get(params.cId)
    		if (client){
    			submitText = "Save"
    		}
    	}
    	render(template: "clientInfoForm", model: [client:client, submitText:submitText])
    }

	def saveClientNotes(){
		println "\n" + new Date()
		println "params: " + params
		if (params?.n){
			def coder = new org.apache.commons.codec.net.URLCodec() 
			session.editClient.notes = coder.decode(params.n)
			session.editClient.save(flush:true)
			return [success:true] as JSON
		}
		else{
			return [success:true] as JSON
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
			return false
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
					if (dayOff.hasErrors()){
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
		render (template: "timeSlotOptions", model: [timeSlots:timeSlots])
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

	def saveClient(){
		println "\n" + new Date()
		println "params: " + params
		Boolean success = false

		if (params?.cId) {
			def existingClient = User.get(params.cId)
			if (existingClient) {
				success = userService.updateClient(existingClient, params)
			}
		}
		else {
			def newClient = userService.createNewClient(params)
			if (newClient.hasErrors()){
				println "ERROR: " + newClient.errors
			}
			else{
				success = true
			}
		}
		render ('{"success":'+success+'}') as JSON
	}

	def cancelAppointment(){
		println "\n" + new Date()
		println "params: " + params
		Boolean success = false
		if (params.c){ // params.c = appointment.code
			def appointment = Appointment.findByCode(params.c.trim())
			println "appointment(${appointment.id}): " + appointment.client?.getFullName() + " | " + appointment.service?.description + " on " + appointment.appointmentDate.format('MM/dd/yy @ hh:mm a [E]')
			if (appointment){
				appointment.delete()
				if (!appointment.hasErrors()){
					emailService.sendCancellationNotices(appointment)
					success = true
				}
			}
		}
		render ('{"success":'+success+'}') as JSON
	}

}
