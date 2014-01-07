package database.scripts

import salonManager.*

class version0_3 {

	def init = { servletContext ->

		if (DayOfTheWeek.count() < 1){

			def sunday = new DayOfTheWeek()
			sunday.dayOfTheWeek = 1
			sunday.available = false
			sunday.startTime = 36000000 //10AM
			sunday.endTime  = 68400000 //7PM
			sunday.save(flush:true)

			def monday = new DayOfTheWeek()
			monday.dayOfTheWeek = 2
			monday.available = true
			monday.startTime = 36000000 //10AM
			monday.endTime  = 64800000 //6PM
			monday.save(flush:true)

			def tuesday = new DayOfTheWeek()
			tuesday.dayOfTheWeek = 3
			tuesday.available = true
			tuesday.startTime = 36000000 //10AM
			tuesday.endTime  = 68400000 //7PM
			tuesday.save(flush:true)

			def wednesday = new DayOfTheWeek()
			wednesday.dayOfTheWeek = 4
			wednesday.available = true
			wednesday.startTime = 36000000 //10AM
			wednesday.endTime  = 68400000 //7PM
			wednesday.save(flush:true)

			def thursday = new DayOfTheWeek()
			thursday.dayOfTheWeek = 5
			thursday.available = true
			thursday.startTime = 36000000 //10AM
			thursday.endTime  = 68400000 //7PM
			thursday.save(flush:true)

			def friday = new DayOfTheWeek()
			friday.dayOfTheWeek = 6
			friday.available = true
			friday.startTime = 36000000 //10AM
			friday.endTime  = 63000000 //5:30PM
			friday.save(flush:true)

			def saturday = new DayOfTheWeek()
			saturday.dayOfTheWeek = 7
			saturday.available = false
			saturday.startTime = 36000000 //10AM
			saturday.endTime  = 68400000 //7PM
			saturday.save(flush:true)

			def user = User.findByCode("kp")
			user.addToDaysOfTheWeek(sunday)
			user.addToDaysOfTheWeek(monday)
			user.addToDaysOfTheWeek(tuesday)
			user.addToDaysOfTheWeek(wednesday)
			user.addToDaysOfTheWeek(thursday)
			user.addToDaysOfTheWeek(friday)
			user.addToDaysOfTheWeek(saturday)
			user.save()
		}
	}
}