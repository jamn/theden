package salonManager

class SchedulerService {

	static Long HOUR = 3600000
	static Long MINUTE = 60000

	def dateService

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

}