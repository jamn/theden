package salonManager

class Appointment extends CoreObject {

	Date appointmentDate
	User stylist
	User client
	Service service
	String notes
	String code
	Boolean booked = false

	static constraints = {
    	client(nullable:true)
    	notes(nullable:true)
    }

}