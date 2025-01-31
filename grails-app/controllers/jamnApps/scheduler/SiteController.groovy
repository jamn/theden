package jamnApps.scheduler

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.apache.commons.lang.RandomStringUtils
import java.text.SimpleDateFormat
import grails.converters.JSON
import groovy.sql.Sql

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
	def sessionFactory

	def index() {
		def message = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "No messages found."
		return [message:message]
	}

	def getServices(){
		deleteStaleAppointments()
		session.invalidate()
		GrailsWebRequest.lookup(request).session = null

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

	def getTimeSlots() {
		println "\n---- GET AVAILABLE TIMES ----"
		println new Date()
		def timeSlotsMap = [:]
		def requestedDate
		def service
		def stylist
		Boolean dontRenderTemplate = false
		Boolean renderDatePicker = false

		if (params?.d){
			requestedDate = dateFormatter3.parse(params?.d)
		}
		else if (session?.requestedDate){
			requestedDate = session.requestedDate
			dontRenderTemplate = true
		}
		else{
			renderDatePicker = true
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

		if (requestedDate && stylist && service){
			timeSlotsMap = schedulerService.getTimeSlotsAvailableMap(requestedDate, stylist, service)
			if (dontRenderTemplate){
				return timeSlotsMap
			}
			else{
				def datePickerHTML = g.render(template: "datePicker")
				def timeSlotsHTML = g.render(template: "timeSlots", model: [timeSlotsMap:timeSlotsMap])
				def returnHTML = renderDatePicker ? (datePickerHTML + "\n" + timeSlotsHTML) : timeSlotsHTML
				render (returnHTML)
			}
		}
		else {
			println "ERROR: unable to process params -> " + params
			return false
		}
	}

	def getLoginForm() {
		println "\n---- GET LOGIN FORM ----"
		println new Date()
		saveDate(params)
		def loggedInCookieId = request.getCookie('den2')
		def loginResults = userService.loginClient([loggedInCookieId:loggedInCookieId])
		println "loginResults: " + loginResults
		if (!loginResults.error){
			session.client = loginResults.client
		}
		render(template: "login")
		
	}

	private saveDate(Map params = [:]){
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
		List bookedAppointments = []
		def nextAppointment
		while (count <= repeatNumberOfAppointments){
			def existingAppointment = Appointment.findWhere(appointmentDate:appointmentDate, deleted:false)
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
				bookedAppointments.add(appointment)
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
		session.bookedAppointments = bookedAppointments
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
					def appointment = Appointment.get(session.appointmentId)
					println "deleting appointment: " + appointment
					appointment.deleted = true
					appointment.save()
					emailService.sendPasswordResetLink(client)
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
			freeHeldTimeslots()
			resetSessionVariables()
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

		if (params?.loggedIn != "logged-in") {
			session.client = null
		}

		def loginResults = session?.client ? [client:session.client] : userService.loginClient(params)
		println "*** loginResults: " + loginResults
		if (loginResults.error == true || !loginResults.client){
			errorOccurred = true
			errorMessage = loginResults.errorDetails ?: "Unable to login. Please try resetting your password."
		}else{

			def client = loginResults.client
			def service = Service.get(session.serviceId)
			def stylist = User.get(session.stylistId)
			
			println "client: " + client?.getFullName()
			println "service: " + service?.description
			println "stylist: " + stylist?.getFullName()

			if (params?.remember == "true") {
				def loggedInCookieId = RandomStringUtils.random(20, true, true)
				println "NEW loggedInCookieId: " + loggedInCookieId
				new LoginLog(
					user:client,
					loggedInCookieId: loggedInCookieId
				).save(flush:true)
				response.setCookie('den2', loggedInCookieId)
			}

			println "session: " + session

			def phone = params?.ph?.replaceAll("-","")?.replaceAll(" ","")?.replaceAll("___-___-____","")
			if (params?.tRmndr && phone?.size() == 10 && !phone?.contains('0000000000')){
				println "saving client phone number: " + phone
				client.phone = phone
				client.save()
			}

			def tempAppointment = Appointment.get(session.appointmentId)
			
			if (session.multipleAppointmentsScheduled && tempAppointment){
				println "...multiple appointments scheduled and appointment in session..."
				def now = new Date()
				Appointment.findAllWhere(client:tempAppointment.client, deleted:false)?.each(){
					if (it.booked == false && it.appointmentDate > now){
						println "adding appointment."
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
					appointment.sendEmailReminder = params?.eRmndr == "true" ? true : false
					appointment.sendTextReminder = params?.tRmndr == "true" ? true : false
					appointment.booked = true
					appointment.save(flush:true)
					if (appointment.hasErrors() || appointment.booked == false){
						println "ERROR: " + appointment?.errors
						errorOccurred = true
						errorMessage = "An error occured trying to save your appointment. Sorry about that, we'll get to the bottom of it. In the meantime please try booking again from the start."
					}
					else{
						println "saved appointment(${appointment.id}): " + appointment.client?.getFullName() + " | " + appointment.service?.description + " on " + appointment.appointmentDate.format('MM/dd/yy @ hh:mm a')
					}
				}
				emailService.sendEmailConfirmation(appointments)

				if (session.existingAppointmentId){
					def existingAppointment = Appointment.get(session.existingAppointmentId)
					if (existingAppointment.client == client){
						println "Deleting existing appointment..."
						existingAppointment.deleted = true
						existingAppointment.save(flush:true)
						session.existingAppointmentId = null
						emailService.sendCancellationNotices(existingAppointment)
					}
				}
			}else{
				errorOccurred = true
				errorMessage = "Email address not recognized."
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
			def appointment = Appointment.findWhere(id:params.a, deleted:false)
			if (appointment){
				session.appointmentId = null
				session.requestedDate = appointment.appointmentDate
				session.serviceId = appointment?.service?.id ?: null
				session.stylistId = appointment?.stylist?.id ?: null
				if (appointment?.client?.code?.toUpperCase() == params.cc.toString().toUpperCase().trim()){
					session.existingAppointmentId = appointment.id
					println "session: " + session
					def timeSlotsMap = getTimeSlots()
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
		println "\n---- CANCEL APPOINTMENT? ----"
		println new Date()
		println "params: " + params
		
		if (params?.c){ // params.c = appointment.code
			def appointment = Appointment.findWhere(code:params.c.trim(), deleted:false)
			if (appointment){
				session.appointmentToDelete = appointment
				println "appointment(${appointment?.id}): " + appointment?.client?.getFullName() + " | " + appointment?.service?.description + " on " + appointment?.appointmentDate?.format('MM/dd/yy @ hh:mm a [E]')
			}
		}

		println "session: " + session

		def message = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "No messages found."
		render (template: "cancelAppointment", model: [message: message])
	}

	def confirmAppointmentCancellation(){
		println "\n---- CANCEL APPOINTMENT CONFIRMED ----"
		println new Date()
		println "params: " + params
		Boolean appointmentDeleted = false
		if (session?.appointmentToDelete){
			def appointment = session.appointmentToDelete
			appointment.deleted = true
			appointment.save(flush:true)
			if (appointment.hasErrors()){
				println "ERROR: " + appointment.error
			}
			else{
				appointmentDeleted = true
				session.appointmentToDelete = null
				emailService.sendCancellationNotices(appointment)
			}
		}
		def message = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "No messages found."
		render(template: "cancelAppointmentConfirmation", model: [message: message, appointmentDeleted:appointmentDeleted])
	}

	private deleteStaleAppointments(){
		println "\n---- DELETING STALE APPOINTMENTS ----"
		println new Date()
		def numberOfTimeSlotsFreed = 0
		def lastAppointmentUserAttemptedToBook = Appointment.get(session?.appointmentId)
		if (lastAppointmentUserAttemptedToBook && lastAppointmentUserAttemptedToBook.booked == false){
			lastAppointmentUserAttemptedToBook.deleted = true
			lastAppointmentUserAttemptedToBook.save(flush:true)
			numberOfTimeSlotsFreed++
		}
		Calendar calendarObject = new GregorianCalendar()
		calendarObject.add(Calendar.MINUTE, -1)
		def fiveMinutesAgo = calendarObject.getTime()
		numberOfTimeSlotsFreed += Appointment.executeUpdate("update Appointment a set a.deleted = true where a.booked = false and a.dateCreated < :fiveMinutesAgo", [fiveMinutesAgo:fiveMinutesAgo])	
		sessionFactory.currentSession.flush()
		println "Deleted ${numberOfTimeSlotsFreed} stale appointments"
	}

	private freeHeldTimeslots(){
		session?.bookedAppointments?.each(){
			def appointment = Appointment.get(it.id)
			if (appointment && appointment.booked == false){
				appointment.deleted = true
				appointment.save(flush:true)
			}
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

	private resetSessionVariables() {
		session?.bookedAppointments = null
		session?.stylistId = null
		session?.serviceId = null
		session?.userUpdatingPassword = null
		session?.existingAppointments = null
		session?.appointmentId = null
	}

}
