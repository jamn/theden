package salonManager

class DayOfTheWeek extends CoreObject {

	Long dayOfTheWeek // java.Calendar -- 1=Sunday, 2=Monday, etc.
	Boolean available = false
	Long startTime // 1 hour = 3,600,000
	Long endTime

}