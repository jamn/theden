package salonManager

class DayOfTheWeek extends CoreObject {

	Long dayOfTheWeek // java.Calendar -- 1=Sunday, 2=Monday, etc.
	Boolean available = false
	Long startTime
	Long endTime

}