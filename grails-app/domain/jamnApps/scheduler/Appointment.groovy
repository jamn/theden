package jamnApps.scheduler

class Appointment extends CoreObject {

	Date appointmentDate
	User stylist
	User client
	Service service
	String notes
	String code
	Boolean booked = false
	Boolean reminderEmailSent = false

	static constraints = {
    	client(nullable:true)
    	notes(nullable:true)
    }

}