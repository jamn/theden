package salonManager

class User extends CoreObject {

	String username
	String password
	String firstName
	String lastName
	String email
    String phone
	String type = "001"		// [admin/stylist/client] -- 1 or 0 for each field, fields can be combined
							// 001 = client
							// 010 = sylist
							// 100 = admin

	String code // users initials or some short code to pass as a URL param
	Long startTime
	Long endTime
    String loggedInCookieId

	static hasMany = [services:Service, daysOfTheWeek:DayOfTheWeek]

    static constraints = {
    	username(nullable:true)
		password(nullable:true)
		firstName(nullable:true)
		lastName(nullable:true)
        email(nullable:true)
		phone(nullable:true)
		startTime(nullable:true)
        endTime(nullable:true)
		loggedInCookieId(nullable:true)
    }


    public hasPermission(String permission){
    	Boolean hasPermission = false
    	if (permission.toLowerCase() == "admin" && this.type.substring(0,1) == "1"){
    		hasPermission = true
    	}
    	if (permission.toLowerCase() == "stylist" && this.type.substring(1,2) == "1"){
    		hasPermission = true
    	}
    	if (permission.toLowerCase() == "client" && this.type.substring(2,3) == "1"){
    		hasPermission = true
    	}
    	return hasPermission
    }

    def transients = [
    	'fullName'
    ]

    String getFullName(){
    	return firstName + " " + lastName
    }

}	