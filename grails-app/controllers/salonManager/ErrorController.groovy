package salonManager

class ErrorController {

    def index() {
        def message = ApplicationProperty.findByName("HOMEPAGE_MESSAGE")?.value ?: "No messages found."
        return [message:message]
    }

    
}
