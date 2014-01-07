package salonManager

import org.apache.commons.lang.RandomStringUtils
import java.text.SimpleDateFormat
import grails.converters.JSON

class AdminController {

	SimpleDateFormat dateFormatter = new SimpleDateFormat("EE dd MMM yyyy @ hh:mm a")
	SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MM/dd/yyyyhh:mma")
	SimpleDateFormat dateFormatter3 = new SimpleDateFormat("MM/dd/yyyy")

	def dateService

	def testEmail = {
		println "sending email"
		try {	
			sendMail {     
				to "bjacobi@gmail.com"     
				subject "New Appointment [Tomorrow]"     
				html "<p><img style='height:120px;width:120px;' src='http://thedenbarbershop-kc.com/new/images/logo.png'></p><p><b>Client:</b> Me</p>"
			}
		}
		catch(Exception e) {
			println "ERROR"
			println e
		}
		println "finished"
		
	}

    def index() { 
    	if (session.adminUser){
    		Calendar now = new GregorianCalendar()
    		now.set(Calendar.HOUR_OF_DAY, 0)
			now.set(Calendar.MINUTE, 0)
			now.set(Calendar.SECOND, 0)
			now.set(Calendar.MILLISECOND, 0)
	    	def appointments = Appointment.findAllWhere(booked:true)?.findAll{it.appointmentDate > now.getTime()}?.sort{it.appointmentDate}
	    	def homepageText = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "ERROR: HOMEPAGE_MESSAGE record not found in the database. Tell Ben."
	    	def stylist = User.findByCode("kp")
	    	homepageText = homepageText.replace("<br>","\r")

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

	    	return [appointments:appointments, homepageText:homepageText, stylist:stylist, startTime:startTime, endTime:endTime]
    	}
    }

    def addExistingAppointments(){
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
		}
	}

	def saveHomepageMessage(){
		if (session.adminUser){
			println "params: " + params
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
		}
	}

	def blockOffTime(){
		if (session.adminUser){
			println "params: " + params
			def from = dateFormatter2.parse(params?.date+params?.from)
			def to = dateFormatter2.parse(params?.date+params?.to)

			def stylist = User.findByCode("kp")
			def service = Service.findByDescription("Blocked Off Time")
			Calendar currentDate = new GregorianCalendar()
			currentDate.setTime(from)

			while (currentDate.getTime() <= to){
				def appointment = new Appointment()
				appointment.appointmentDate = currentDate.getTime()
				appointment.stylist = stylist
				appointment.service = service
				appointment.code = RandomStringUtils.random(14, true, true)
				appointment.client = stylist
				appointment.booked = true
				appointment.save(flush:true)
				println "appointment: " + appointment
				currentDate.add(Calendar.MINUTE, 15)
			}
			render ('{"success":true}') as JSON
		}
	}

	def blockOffWholeDay(){
		if (session.adminUser){
			println "params: " + params
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
				println "dayOff: " + dayOff
				currentDate.add(Calendar.DAY_OF_YEAR, 1)
			}
			render ('{"success":true}') as JSON
		}
	}

}
