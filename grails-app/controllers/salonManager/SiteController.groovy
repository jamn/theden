package salonManager

import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import org.apache.commons.lang.RandomStringUtils
import java.text.SimpleDateFormat
import grails.converters.JSON

class SiteController {

	static Long HOUR = 3600000
	static Long MINUTE = 60000

	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd")
	SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MM/dd/yyyy HH:mm")
	SimpleDateFormat dateFormatter3 = new SimpleDateFormat("MM/dd/yyyy")

	def dateService
	def emailService
	def schedulerService
	def userService

	def index() {
		resetSession()
		def message = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "No messages found."
		return [message:message]
	}

	

	def getServices(){
		println "\n---- GET SERVICES ----"
		println new Date()
		println "params.u: " + params.u
		def stylist = User.findByCode(params?.u)
		if (stylist){
			session.stylistId = stylist.id
			println "stylist: " + stylist?.getFullName()
		}
		def serviceList = getServicesForStylist(stylist)
		if (serviceList.size() > 0){
			println "serviceList: " + serviceList
			render (template: "services", model: [services:serviceList])
		}
		else
		{
			println "ERROR: unable to locate services for stylist: " + params
			render ('{"success":false}') as JSON
		}
	}

	def getAvailableTimes() {
		println "\n---- GET AVAILABLE TIMES ----"
		println new Date()
		def timeSlotsMap = [:]
		def requestedDate
		def service
		def stylist
		Boolean dontRenderTemplate = false
		try {
			if (params?.d){
				requestedDate = dateFormatter3.parse(params?.d)
			}
			else if (session?.requestedDate){
				requestedDate = session.requestedDate
				dontRenderTemplate = true
			}
			else{
				requestedDate =  new Date()
			}
			println "requestedDate: " + requestedDate
			if (params?.stylist){
				stylist = params.stylist
			}else{
				stylist = User.get(session.stylistId)
			}
			println "stylist: " + stylist?.getFullName()
			if (params?.s){
				service = Service.findWhere(stylist:stylist, description:params?.s)
				session.serviceId = service.id
			}
			else{
				service = Service.get(session?.serviceId)
			}
			println "service: " + service?.description
		}
		catch(Exception e) {
			println "ERROR: " + e
		}

		if (requestedDate && stylist && service){
			timeSlotsMap = schedulerService.getTimeSlotsAvailableMap(requestedDate, stylist, service)
		}
		else {
			println "ERROR: unable to process params -> " + params
		}

		if (dontRenderTemplate){
			return timeSlotsMap
		}
		else{
			render (template: "timeSlots", model: [timeSlotsMap:timeSlotsMap])
		}

	}

	def saveDate(){
		println "\n---- SAVE DATE ----"
		println new Date()

		def appointmentDate = dateFormatter2.parse(params?.d)
		def repeatDuration = new Integer(1)
		def repeatNumberOfAppointments = 1

		if (params?.r?.toLowerCase() == "true"){ // r = recurringAppointment
			println "Recurring Appointment"
			repeatDuration = new Integer(params?.dur)
			repeatNumberOfAppointments = new Integer(params?.num)
		}
		Calendar startDate = new GregorianCalendar()
		startDate.setTime(appointmentDate)
		def count = 1
		List existingAppointments = []
		def nextAppointment
		while (count <= repeatNumberOfAppointments){
			def existingAppointment = Appointment.findByAppointmentDate(appointmentDate)
			if (existingAppointment && count == 1){
				println "existingAppointment(${existingAppointment.id}): " + existingAppointment.client?.getFullName() + " | " + existingAppointment.service?.description + " on " + existingAppointment.appointmentDate.format('MM/dd/yy @ hh:mm a [E]')
				render ('{"success":false}') as JSON
			}
			else if (existingAppointment && count > 1){
				println "existingAppointment(${existingAppointment.id}): " + existingAppointment.client?.getFullName() + " | " + existingAppointment.service?.description + " on " + existingAppointment.appointmentDate.format('MM/dd/yy @ hh:mm a [E]')
				existingAppointments.add(existingAppointment)
			}

			if (appointmentDate && !existingAppointment){
				def stylist = User.get(session.stylistId)
				def service = Service.get(session.serviceId)
				def appointment = new Appointment()
				appointment.appointmentDate = appointmentDate
				appointment.stylist = stylist
				appointment.service = service
				appointment.code = RandomStringUtils.random(14, true, true)
				appointment.save(flush:true)
				if (count == 1){
					nextAppointment = appointment
				}else{
					session.multipleAppointmentsScheduled = true
				}
				println "saved appointment(${appointment.id}): " + appointment.service?.description + " on " + appointment.appointmentDate.format('MM/dd/yy @ hh:mm a [E]')
			}
			startDate.add(Calendar.WEEK_OF_YEAR, repeatDuration)
			appointmentDate = startDate.getTime()
			count++
		}
		println "existingAppointments: " + existingAppointments
		session.appointmentId = nextAppointment.id
		session.existingAppointments = existingAppointments
		render (template: "login", model: [appointment:nextAppointment])
	}

	def sendPasswordResetEmail(){
		println "\n---- PASSWORD RESET EMAIL REQUESTED ----"
		println new Date()
		println "params: " + params
		Boolean errorOccurred = false
		def errorMessage = ''

		if (params?.e?.size() > 0){
			def email = params?.e
			def client = User.findByEmail(email)
			if (client){
				try {
					client.passwordResetCode = RandomStringUtils.random(14, true, true)
					client.passwordResetCodeDateCreated = new Date()
					client.save(flush:true)
					runAsync {
						emailService.sendPasswordResetLink(client)
					}
				}
				catch(Exception e) {
					println "ERROR: " + e
					errorOccurred = true
					errorMessage = "Oops, something didn't work right. Try again please."
				}
				
			}
			else{
				errorOccurred = true
				errorMessage = "Email not found."
			}
		}else{
			errorOccurred = true
			errorMessage = "Email required."
		}

		if (errorOccurred){
			println "AN ERROR OCCURRED: " + errorMessage
			def jsonString = '{"success":false,"errorMessage":"'+errorMessage+'"}'
			render(jsonString)
		}else{
			render(template: "confirmation", model: [passwordReset:true])
		}
	}

	def resetPasswordForm(){
		println "\n---- GETTING PASSWORD RESET FORM ----"
		println new Date()
		println "params: " + params

		if (params?.rc?.size() > 0 && params?.cc?.size() > 0){
			def user = User.findWhere(passwordResetCode:params.rc, code:params.cc)
			if (user){
				session.userUpdatingPassword = user
				def message = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "No messages found."
				render(template: "resetPassword", model: [message:message])
			}
		}else{
			println "ERROR: required params not passed."
		}
	}

	def attemptPasswordReset(){
		println "\n---- ATTEMPTING TO RESET PASSWORD ----"
		println new Date()
		println "params: " + params
		Boolean errorOccurred = false
		def errorMessage = ''
		if (params?.p1?.size() > 0 && params?.p2?.size() > 0){
			def password1 = params.p1
			def password2 = params.p2
			if (password1 == password2){
				session.userUpdatingPassword.password = password1
				session.userUpdatingPassword.passwordResetCode = null
				session.userUpdatingPassword.passwordResetCodeDateCreated = null
				session.userUpdatingPassword.save(flush:true)
				if (session.userUpdatingPassword.hasErrors()){
					println "ERROR: " + session.userUpdatingPassword.errors
					errorOccurred = true
					errorMessage = 'Something really weird happened. Please try again.'
				}
			}else{
				errorOccurred = true
				errorMessage = 'Password fields do not match.'
			}
		}
		else{
			errorOccurred = true
			errorMessage = 'Please enter a new password.'
		}

		if (errorOccurred){
			println "AN ERROR OCCURRED: " + errorMessage
			def jsonString = '{"success":false,"errorMessage":"'+errorMessage+'"}'
			render(jsonString)
		}else{
			render(template: "confirmation", model: [passwordReset:true, success:true])
		}
	}


	def bookAppointment(){
		println "\n---- BOOK APPOINTMENT ----"
		println new Date()
		println "params: " + params
		Boolean errorOccurred = false
		def errorMessage = ''
		def appointments = []
		if (params?.hp?.size() > 0){ // HONEYPOT -- check value of hidden field to see if a spambot is submitting the form
			errorOccurred = true
			errorMessage = "bear found the honey"
		}
		else{
			def loginResults = userService.loginClient(params)
			println "loginResults: " + loginResults
			if (loginResults.error == true){
				errorOccurred = true
				errorMessage = loginResults.errorDetails
			}else{
				def client = loginResults.client
				def service = Service.get(session.serviceId)
				def stylist = User.get(session.stylistId)
				
				println "client: " + client?.getFullName()
				println "service: " + service?.description
				println "stylist: " + stylist?.getFullName()

				println "session: " + session

				def tempAppointment = Appointment.get(session.appointmentId)
				
				if (session.multipleAppointmentsScheduled && tempAppointment){
					println "here"
					def now = new Date()
					Appointment.findAllWhere(client:tempAppointment.client)?.each(){
						if (it.booked == false && it.appointmentDate > now){
							println "adding appointment"
							appointments.add(it)
						}
					}
				}else if (tempAppointment){
					appointments.add(tempAppointment)
				}

				println "appointments: " + appointments

				if (client && service && stylist && appointments.size() > 0){
					appointments.each(){ appointment ->
						appointment.client = client
						appointment.booked = true
						appointment.save(flush:true)
						if (appointment.hasErrors() || appointment.booked == false){
							println "ERROR: " + appointment?.errors
							errorOccurred = true
							errorMessage = "A really weird error occured trying to save your appointment. We'll get to the bottom of it. In the meantime please try booking again from the start. Sorry about that."
						}
						else{
							println "saved appointment(${appointment.id}): " + appointment.client?.getFullName() + " | " + appointment.service?.description + " on " + appointment.appointmentDate.format('MM/dd/yy @ hh:mm a')
						}
					}
					runAsync {
						emailService.sendEmailConfirmation(appointments)
					}

					if (session.existingAppointmentId){
						def existingAppointment = Appointment.get(session.existingAppointmentId)
						if (existingAppointment.client == client){
							println "Deleting existing appointment..."
							schedulerService.deleteAppointment(existingAppointment.id)
							emailService.sendCancellationNotices(existingAppointment)
						}
					}
				}else{
					errorOccurred = true
					errorMessage = "Email address not recognized."
				}
			}
		}
		if (errorOccurred){
			println "AN ERROR OCCURRED: " + errorMessage
			def jsonString = '{"success":false,"errorMessage":"'+errorMessage+'"}'
			render(jsonString)
		}
		else{
			render(template: "confirmation", model: [appointments:appointments, existingAppointments:session.existingAppointments])
		}
	}

	def modifyAppointment(){
		println "\n---- MODIFY APPOINTMENT ----"
		println new Date()
		println "params: " + params
		if (params.a && params.cc){ // params.a = appointmentId | params.cc = clientCode
			def appointment = Appointment.get(params.a)
			if (appointment){
				session.appointmentId = null
				session.requestedDate = appointment.appointmentDate
				session.serviceId = appointment?.service?.id ?: null
				session.stylistId = appointment?.stylist?.id ?: null
				if (appointment?.client?.code?.toUpperCase() == params.cc.toString().toUpperCase().trim()){
					session.existingAppointmentId = appointment.id
					println "session: " + session
					def timeSlotsMap = getAvailableTimes()
					def message = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "No messages found."
					render (template: "modifyAppointment", model: [timeSlotsMap:timeSlotsMap, message:message, appointment:appointment])
				}
			}
			else{
				println "ERROR: appointment doesn't exist"
			}
		}
		else{
			println "ERROR: required params not passed"
		}
	}

	def cancelAppointment(){
		println "\n---- CANCEL APPOINTMENT ----"
		println new Date()
		println "params: " + params
		if (params.c){ // params.c = appointment.code
			def appointment = Appointment.findByCode(params.c.trim())
			println "appointment(${appointment.id}): " + appointment.client?.getFullName() + " | " + appointment.service?.description + " on " + appointment.appointmentDate.format('MM/dd/yy @ hh:mm a [E]')
			if (appointment){
				appointment.delete()
				runAsync {
					emailService.sendCancellationNotices(appointment)
				}
				def message = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "No messages found."
				render (template: "cancelAppointment", model: [message: message])
			}
			else{
				render ('{"success":false}') as JSON
			}
		}
	}

	private deleteStaleAppointments(){
		println "\n---- DELETING STALE APPOINTMENTS ----"
		println new Date()
		def con = AH.application.mainContext.sessionFactory.currentSession.connection()
		def sql = new groovy.sql.Sql(con)
		Calendar now = new GregorianCalendar()
		now.add(Calendar.MINUTE, -10)
		def tenMinutesAgo = now.getTime().format('yyyy-MM-dd HH:mm:ss')
		List idsToDelete = []
		def query1 = [
			"SELECT id FROM appointment WHERE booked = 0 AND id IN (",
				"SELECT id FROM core_object WHERE date_created < '"+tenMinutesAgo+"'",
			");"
		].join('\n')
		try {
			sql.eachRow(query1){ row ->
				idsToDelete.add(row.id)
			}
		}
		catch(Exception e) {
			println "ERROR: " + e
		}
		idsToDelete.each(){
			schedulerService.deleteAppointment(it, sql)
		}
		
	}

	private getServicesForStylist(User stylist){
		def services = Service.executeQuery("FROM Service s WHERE s.stylist = :stylist AND display = true ORDER BY s.displayOrder", [stylist:stylist])
		def serviceList = []
		services?.each(){
			//def duration = dateService.getTimeString(it.duration)
			def price = it.price
			def description = it.description
			serviceList.add([price:price, description:description, id:it.id])
		}
		return serviceList
	}


	private resetSession(){
		def appointment = Appointment.get(session?.appointmentId)
		if (appointment && !appointment?.booked){
			appointment.delete()
		}
		deleteStaleAppointments()
		session.invalidate()
		def now = new Date()
		println "** SESSION RESET ${now} **"
	}
}
