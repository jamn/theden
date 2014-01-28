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
            println("Starting SendAppointmentReminderEmailJob: ${date.format('MM/dd/yy @ HH:mm:ss')}")
            emailService.sendReminderEmails()
            date = new Date()
            println("Ending SendAppointmentReminderEmailJob: ${date.format('MM/dd/yy @ HH:mm:ss')}")
        }
        else{
            println "SendAppointmentReminderEmailJob IS DISABLED | ${date.format('MM/dd/yy @ HH:mm:ss')}"
        }
    }

}
