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
			println "stylist: " + stylist
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
		def count = 0
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
			println "stylist: " + stylist
			if (params?.s){
				service = Service.findWhere(stylist:stylist, description:params?.s)
				session.serviceId = service.id
			}
			else{
				service = Service.get(session?.serviceId)
			}
			println "service: " + service
		}
		catch(Exception e) {
			println "ERROR: " + e
		}

		if (requestedDate && stylist && service){

			Calendar startDate = new GregorianCalendar()
			startDate.setTime(requestedDate)
			Calendar endDate = new GregorianCalendar()
			endDate.setTime(requestedDate)
			
			def stylistDayOfTheWeek = stylist?.daysOfTheWeek?.find{it.dayOfTheWeek == startDate.get(Calendar.DAY_OF_WEEK)}

			def stylistStartTime = dateService.get24HourTimeValues(stylistDayOfTheWeek.startTime)
			def stylistEndTime = dateService.get24HourTimeValues(stylistDayOfTheWeek.endTime)

			startDate.set(Calendar.HOUR_OF_DAY, stylistStartTime.hour.intValue())
			startDate.set(Calendar.MINUTE, stylistStartTime.minute.intValue())
			startDate.set(Calendar.SECOND, 0)
			startDate.set(Calendar.MILLISECOND, 0)

			endDate.set(Calendar.HOUR_OF_DAY, stylistEndTime.hour.intValue())
			endDate.set(Calendar.MINUTE, stylistEndTime.minute.intValue())
			endDate.set(Calendar.SECOND, 0)
			endDate.set(Calendar.MILLISECOND, 0)

			println "startDate: " + startDate.getTime()
			println "endDate: " + endDate.getTime()

			//def daysOff = DayOff.list()
			//Calendar requestedDateCalObject = new GregorianCalendar()
			//requestedDateCalObject.setTime(requestedDate)
			//requestedDateCalObject.set(Calendar.HOUR_OF_DAY, 0)
			//requestedDateCalObject.set(Calendar.MINUTE, 0)
			//requestedDateCalObject.set(Calendar.SECOND, 0)
			//requestedDateCalObject.set(Calendar.MILLISECOND, 0)
			//Boolean requestedDateIsOnADayOff = daysOff.find{it.dayOffDate == requestedDateCalObject.getTime()} ? true : false
			//if (!requestedDateIsOnADayOff){
				def appointments = Appointment.executeQuery("FROM Appointment a WHERE a.stylist = :stylist AND a.appointmentDate >= :startDate AND a.appointmentDate <= :endDate ORDER BY appointmentDate", [stylist:stylist, startDate:startDate.getTime(), endDate:endDate.getTime()])

				println "appointments: " + appointments

				Calendar currentTime = new GregorianCalendar()
				currentTime.setTime(startDate.getTime())
				
				def durationInMinutes = service.duration / MINUTE
				println "durationInMinutes: " + durationInMinutes

				while(currentTime < endDate) {
					count++
					Calendar timeSlotStart = new GregorianCalendar()
					timeSlotStart.setTime(currentTime.getTime())
					Calendar timeSlotEnd = new GregorianCalendar()
					timeSlotEnd.setTime(timeSlotStart.getTime())
					timeSlotEnd.add(Calendar.MINUTE, durationInMinutes.intValue())

					// DOES AN EXISTING APPOINTMENT FALL IN THIS TIME RANGE?
					def existingAppointment = appointments.find{ it.appointmentDate >= timeSlotStart.getTime() && it.appointmentDate < timeSlotEnd.getTime() }
					//println "timeSlotStart: " + timeSlotStart.getTime().format("yyyy-MM-dd HH:mm:ss")
					//println "existingAppointment: " + existingAppointment?.appointmentDate?.format("yyyy-MM-dd HH:mm:ss")
					while (existingAppointment){
						timeSlotStart.setTime(existingAppointment.appointmentDate)
						def existingAppointmentDurationInMinutes = existingAppointment.service.duration / MINUTE
						timeSlotStart.add(Calendar.MINUTE, existingAppointmentDurationInMinutes.intValue())
						timeSlotEnd.setTime(timeSlotStart.getTime())
						timeSlotEnd.add(Calendar.MINUTE, durationInMinutes.intValue())
						existingAppointment = appointments.find{ it.appointmentDate >= timeSlotStart.getTime() && it.appointmentDate < timeSlotEnd.getTime() }
					}


					def dayOfWeek = timeSlotEnd.get(Calendar.DAY_OF_WEEK)
					if (timeSlotEnd <= endDate && stylistDayOfTheWeek.available){ // BLOCK OFF SATURDAY AND SUNDAY
					//if (timeSlotEnd <= endDate && dayOfWeek != 1 && dayOfWeek != 7){ // BLOCK OFF SATURDAY AND SUNDAY
					//if (timeSlotEnd <= endDate){
						def timeSlot = timeSlotStart.getTime().format('h:mma').replace(':00', '') + " / " + timeSlotEnd.getTime().format('h:mma').replace(':00', '')
						if (timeSlotStart.get(Calendar.HOUR_OF_DAY) < 11){
							List morning = timeSlotsMap.get("morning") ?: []
							morning.add([startTime:timeSlotStart.getTime().format('MM/dd/yyyy HH:mm'), timeSlot: timeSlot, id:count])
							timeSlotsMap.put("morning", morning)
						}
						else if (timeSlotStart.get(Calendar.HOUR_OF_DAY) < 14){
							List lunch = timeSlotsMap.get("lunch") ?: []
							lunch.add([startTime:timeSlotStart.getTime().format('MM/dd/yyyy HH:mm'), timeSlot: timeSlot, id:count])
							timeSlotsMap.put("lunch", lunch)
						}
						else{
							List afternoon = timeSlotsMap.get("afternoon") ?: []
							afternoon.add([startTime:timeSlotStart.getTime().format('MM/dd/yyyy HH:mm'), timeSlot: timeSlot, id:count])
							timeSlotsMap.put("afternoon", afternoon)
							timeSlotsMap.put("afternoon", afternoon)
						}
					}

					currentTime.setTime(timeSlotStart.getTime())
					currentTime.add(Calendar.MINUTE, 15)
				}

				println "timeSlotsMap.size(): " + timeSlotsMap.size()
			//}
			//else{
				//println "REQUESTED DATE IS ON A DAY OFF!"
			//}

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
			println "existingAppointment: " + existingAppointment
			if (existingAppointment && count == 1){
				println "existingAppointment: " + existingAppointment
				render ('{"success":false}') as JSON
			}
			else if (existingAppointment && count > 1){
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
				}
				println "saved appointment: " + appointment
			}
			startDate.add(Calendar.WEEK_OF_YEAR, repeatDuration)
			appointmentDate = startDate.getTime()
			count++
		}
		println "existingAppointments: " + existingAppointments
		session.appointmentId = nextAppointment.id
		session.existingAppointments = existingAppointments
		render (template: "login", model: [appointment:nextAppointment])
			

		/*def existingAppointment = Appointment.findByAppointmentDate(appointmentDate)
		println "existingAppointment: " + existingAppointment
		if (appointmentDate && !existingAppointment){
			def stylist = User.get(session.stylistId)
			def service = Service.get(session.serviceId)
			def appointment = new Appointment()
			appointment.appointmentDate = appointmentDate
			appointment.stylist = stylist
			appointment.service = service
			appointment.code = RandomStringUtils.random(14, true, true)
			appointment.save(flush:true)
			session.appointmentId = appointment.id
			println "session: " + session
			render (template: "login", model: [appointment:appointment])
		}else{
			println "existingAppointment: " + existingAppointment
			render ('{"success":false}') as JSON
		}*/
	}


	def bookAppointment(){
		println "\n---- BOOK APPOINTMENT ----"
		println new Date()
		println "params: " + params
		if (params?.hp?.size() > 0){ // HONEYPOT -- check value of hidden field to see if a spambot is submitting the form
			render ('{"success":false}') as JSON
		}
		else{
			def client
			def service = Service.get(session.serviceId)
			def stylist = User.get(session.stylistId)
			def appointment = Appointment.get(session.appointmentId)
			if (session.existingAppointmentId && params?.e?.size() > 1 && params?.p?.size() > 1){
				println "Attempting to log in user (existing appointment)..."
				def tempClient = User.findWhere(email:params.e, password:params.p) ?: null
				def existingAppointment = Appointment.get(session.existingAppointmentId)
				if (tempClient && existingAppointment.client == tempClient){
					client = tempClient
				}
			}
			else if (params?.f?.size() > 1 && params?.l?.size() > 1 && params?.e?.size() > 1 && params?.p?.size() > 1){
				def existingUser = User.findByEmail(params.e)
				if (existingUser && existingUser.password == params.p){
					println "USER LOGGED IN CORRECTLY BY ACCIDENT"
					client = existingUser
				}
				else if (existingUser){
					println "ERROR: Email already in use: " + existingUser
				}
				else{
					println "CREATING NEW USER"
					client = new User()
					client.firstName = params.f
					client.lastName = params.l
					client.email = params.e
					client.password = params.p
					client.phone = params.ph
					client.code = client.firstName.substring(0,1).toLowerCase() + client.lastName.substring(0,1).toLowerCase() + new Date().getTime()
					client.save(flush:true)
					if (client.hasErrors()){
						println "ERROR: " + client.errors
						render ('{"success":false}') as JSON
					}
				}
			}
			else if (params?.e?.size() > 1 && params?.p?.size() > 1){
				println "Attempting to log in user..."
				client = User.findWhere(email:params.e, password:params.p) ?: null
			}

			println "client: " + client
			println "service: " + service
			println "stylist: " + stylist
			println "appointment: " + appointment

			if (client && service && stylist && appointment){
				appointment.client = client
				appointment.booked = true
				appointment.save(flush:true)

				if (session.existingAppointmentId){
					def existingAppointment = Appointment.get(session.existingAppointmentId)
					existingAppointment?.delete()
					existingAppointment?.save()
				}
			}
			if (appointment.hasErrors() || appointment.booked == false){
				println "ERROR: appointment.booked("+appointment.booked+") | " + appointment?.errors
				render ('{"success":false}') as JSON
			}
			else{
				println "saved appointment"
				runAsync {
					emailService.sendEmailConfirmation(appointment)
				}
				render (template: "confirmation", model: [appointment:appointment, existingAppointments:session.existingAppointments])
			}
		}
	}

	def modifyAppointment(){
		println "\n---- MODIFY APPOINTMENT ----"
		println new Date()
		println "params: " + params
		if (params.a && params.cc){ // params.a = appointmentId | params.cc = clientCode
			def appointment = Appointment.get(params.a)
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
			else{
				render ('{"success":false}') as JSON
			}
		}
		else{
			render ('{"success":false}') as JSON
		}
	}

	def cancelAppointment(){
		println "\n---- CANCEL APPOINTMENT ----"
		println new Date()
		println "params: " + params
		if (params.c){ // params.c = appointment.code
			def appointment = Appointment.findByCode(params.c.trim())
			println "appointment: " + appointment
			if (appointment){
				appointment.booked = false
				appointment.save(flush:true)
				emailService.sendCancellationNotice(appointment)
				resetSession()
				def message = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "No messages found."
				render (template: "cancelAppointment", model: [message: message])
			}
			else{
				render ('{"success":false}') as JSON
			}
		}
	}

	private deleteStaleAppointments(){
		def con = AH.application.mainContext.sessionFactory.currentSession.connection();
		def sql = new groovy.sql.Sql(con);
		Calendar now = new GregorianCalendar()
		now.add(Calendar.MINUTE, -10)
		def tenMinutesAgo = now.getTime().format('yyyy-MM-dd HH:mm:ss')
		def query = [
			"DELETE FROM appointment WHERE booked = 0 AND id IN (",
				"SELECT id FROM core_object WHERE date_created < '"+tenMinutesAgo+"'",
			");"
		].join('\n')
		sql.executeUpdate(query)
	}

	private getServicesForStylist(User stylist){
		def services = Service.executeQuery("FROM Service s WHERE s.stylist = :stylist AND display = true ORDER BY s.displayOrder", [stylist:stylist])
		def serviceList = []
		services?.each(){
			def duration = dateService.getTimeString(it.duration)
			def description = it.description
			serviceList.add([duration:duration, description:description, id:it.id])
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
