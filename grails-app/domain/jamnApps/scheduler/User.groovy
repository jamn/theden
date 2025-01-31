package jamnApps.scheduler

import java.text.SimpleDateFormat

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
    String passwordResetCode
    Date passwordResetCodeDateCreated
    String notes

	static hasMany = [services:Service, daysOfTheWeek:DayOfTheWeek, logins:LoginLog]

    static constraints = {
    	username(nullable:true)
		password(nullable:true)
		firstName(nullable:true)
		lastName(nullable:true)
        email(nullable:true)
		phone(nullable:true)
		startTime(nullable:true)
        endTime(nullable:true)
        passwordResetCode(nullable:true)
        passwordResetCodeDateCreated(nullable:true)
        notes(nullable:true)
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

    Boolean isNewUser(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy")
        Date dateNewUsersWereEnabled = sdf.parse("07/16/2015")
        return dateCreated > dateNewUsersWereEnabled
    }

}	