package salonManager

import grails.converters.JSON

class AccessController {

    def index() {
        render(view:'login')
    }

    def login = {
        println "params: " + params
    	def user = User.findWhere(
    		username: params.u,
    		password: params.p,
    		deleted: false
    	)

        println 'admin user: ' + user

    	if (user?.hasPermission('admin')){
    		session.adminUser = user
            session.setMaxInactiveInterval(1080) // set timeout to one week
            redirect (controller:'admin', action:'index')
    	}
    	else{
    		render(view:'login')
    	}
    }

    def logout = {
    	if (session.user){
    		session.invalidate()
    	}
        if (session.adminUser){
            session.invalidate()
        }
        render(view:'login')
    }
}
