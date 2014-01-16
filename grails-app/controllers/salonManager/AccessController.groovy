package salonManager

import grails.converters.JSON
import org.apache.commons.lang.RandomStringUtils


class AccessController {

    def index() {
        render(view:'login')
    }

    def login = {
        println "params: " + params
    	def user

        def loggedInCookieId = request.getCookie('den1')
        println "loggedInCookieId: " + loggedInCookieId
        def loggedIn = false
        if (loggedInCookieId){
            loggedIn = true
            user = User.findByLoggedInCookieId(loggedInCookieId)
        }
        if (!user){
            user = User.findWhere(
            	username: params.u,
            	password: params.p,
            	deleted: false
            )
        }

        println 'admin user: ' + user

    	if (user?.hasPermission('admin')){
    		session.adminUser = user
            if (!loggedIn){
                loggedInCookieId = RandomStringUtils.random(20, true, true)
                user.loggedInCookieId = loggedInCookieId
                user.save()
                response.setCookie('den1', loggedInCookieId)
            }
            redirect (controller:'admin', action:'index')
    	}
    	else{
    		render(view:'login')
    	}
    }

    def logout = {
    	if (session.user){
    		session.invalidate()
            response.deleteCookie('den1')
    	}
        if (session.adminUser){
            session.invalidate()
            response.deleteCookie('den1')
        }
        render(view:'login')
    }
}
