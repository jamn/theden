package salonManager

class SendAppointmentReminderEmailJob {

    def emailService

    def concurrent = false

    static triggers = {
        cron name: 'SendAppointmentReminderEmailTrigger', cronExpression: "0 0/15 * * * ?"
    }

    def execute() {
        def active = ApplicationProperty.findByName("JOBS__SEND_APPOINTMENT_REMINDER_EMAIL_JOB__ACTIVE")?.value ?: "0"
        def date = new Date()
        if (active == "1"){
            println("${date.format('MM/dd/yy HH:mm:ss')} -- Starting SendAppointmentReminderEmailJob")
            emailService.sendReminderEmails()
            date = new Date()
            println("${date.format('MM/dd/yy HH:mm:ss')} -- Ending SendAppointmentReminderEmailJob")
        }
        else{
            println "${date.format('MM/dd/yy HH:mm:ss')} -- SendAppointmentReminderEmailJob IS DISABLED"
        }
    }

}
