package salonManager

import java.text.SimpleDateFormat
import org.apache.commons.lang.RandomStringUtils
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class SchedulerService {

	static Long HOUR = 3600000
	static Long MINUTE = 60000

	def dateService
	def emailService

	SimpleDateFormat dateFormatter3 = new SimpleDateFormat("MM/dd/yyyy")

	public deleteAppointment(id, sql = null){
		println "Deleting appointment: " + id
		if (!sql){
			def con = AH.application.mainContext.sessionFactory.currentSession.connection()
			sql = new groovy.sql.Sql(con)
		}
		def query1 = "DELETE FROM appointment WHERE id = ${id};"
		def query2 = "DELETE FROM core_object WHERE id = ${id};"
		try {
			sql.executeUpdate(query1)
		}
		catch(Exception e) {
			println "ERROR: " + e
		}
		try {
			sql.executeUpdate(query2)
		}
		catch(Exception e) {
			println "ERROR: " + e
		}
	}

	public Map getTimeSlotsAvailableMap(Date requestedDate, User stylist, Service service){
		def timeSlotsMap = [:]

		println "requestedDate: " + requestedDate
		println "stylist: " + stylist
		println "service: " + service

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

		def appointments = Appointment.executeQuery("FROM Appointment a WHERE a.stylist = :stylist AND a.appointmentDate >= :startDate AND a.appointmentDate <= :endDate ORDER BY appointmentDate", [stylist:stylist, startDate:startDate.getTime(), endDate:endDate.getTime()])

		Calendar currentTimeMarker = new GregorianCalendar()
		currentTimeMarker.setTime(startDate.getTime())
		
		def durationInMinutes = service.duration / MINUTE
		println "durationInMinutes: " + durationInMinutes
		
		def count = 0

		while(currentTimeMarker < endDate) {
			count++
			Calendar timeSlotStart = new GregorianCalendar()
			timeSlotStart.setTime(currentTimeMarker.getTime())
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
			def now = new Date()
			
			if (timeSlotEnd <= endDate && stylistDayOfTheWeek.available && timeSlotStart.getTime() > now){ // BLOCK OFF SATURDAY AND SUNDAY
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

			currentTimeMarker.setTime(timeSlotStart.getTime())
			currentTimeMarker.add(Calendar.MINUTE, durationInMinutes.intValue())
		}

		return timeSlotsMap
	}

	public Boolean bookForClient(Map params = [:]){
		Boolean success = false
		def client = User.get(new Long(params.cId))
		def stylist = User.findByCode("kp")
		def service = Service.get(new Long(params.sId))

		def startTimeString = params.sTime
		def amIndex = startTimeString.indexOf("AM")
		def pmIndex = startTimeString.indexOf("PM")
		def minutesIndex = startTimeString.indexOf(":")
		Long hour = 0
		Long minute = 0

		if (amIndex > -1){
			startTimeString = startTimeString.substring(0,amIndex)
		}
		else if (pmIndex > -1){
			startTimeString = startTimeString.substring(0,pmIndex)
		}
		
		if (minutesIndex > -1){
			minute = new Long(startTimeString.substring(minutesIndex+1))
		}
		def tempHour
		if (minutesIndex > -1){
			tempHour = startTimeString.substring(0,minutesIndex)?.padLeft(2, "0")
		}
		else if (amIndex > -1){
			tempHour = startTimeString.substring(0,amIndex)?.padLeft(2, "0")
		}else if (pmIndex > -1){
			tempHour = startTimeString.substring(0,pmIndex)?.padLeft(2, "0")
		}
		hour = new Long(tempHour)
		if (pmIndex > -1 && hour != 12){
			hour = hour + 12
		}

		Calendar tempDate = new GregorianCalendar()
		tempDate.setTime(dateFormatter3.parse(params.aDate))
		tempDate.set(Calendar.HOUR_OF_DAY, hour.intValue())
		tempDate.set(Calendar.MINUTE, minute.intValue())
		tempDate.set(Calendar.SECOND, 0)
		tempDate.set(Calendar.MILLISECOND, 0)

		def appointmentDate = tempDate.getTime()

		if (client && stylist && service && appointmentDate){
			def appointment = new Appointment()
			appointment.appointmentDate = appointmentDate
			appointment.stylist = stylist
			appointment.service = service
			appointment.client = client
			appointment.code = RandomStringUtils.random(14, true, true)
			appointment.booked = true
			appointment.save(flush:true)
			if (appointment.hasErrors()){
				println "ERROR!"
				println appointment.errors
			}
			else{
				success = true
				if (params?.rescheduledAppointment?.toString()?.toUpperCase() == "TRUE"){
					runAsync {
						emailService.sendRescheduledConfirmation(appointment)
					}
				}
				else{
					runAsync {
						emailService.sendEmailConfirmation([appointment])
					}
				}
			}
		}
		return success
	}

}