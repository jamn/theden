import org.codehaus.groovy.grails.commons.ApplicationHolder
import salonManager.*

class BootStrap {

	def context
	def databaseVersion
	def applicationVersion
	def loader = new GroovyClassLoader()

	def init = { servletContext ->

		context = servletContext
		getApplicationVersion()
		getDatabaseVersion()

		def appVersionNumber
		def dbVersionNumber

		try {
			appVersionNumber = new Double(applicationVersion)
			dbVersionNumber = new Double(databaseVersion.value)
		}
		catch(Exception e) {
			println "ERROR: " + e
			println "APP VERSION OR DATABASE VERSION NOT SET CORRECTLY!"
			println "    Application Version: " + applicationVersion
			println "    Database Version: " + databaseVersion.value
		}

		while (dbVersionNumber <= appVersionNumber){
			runVersionScript(dbVersionNumber)
			dbVersionNumber = dbVersionNumber + 0.1
		}
	}

	private getDatabaseVersion() {
		databaseVersion = ApplicationProperty.findByName("DATABASE_VERSION") ?: new ApplicationProperty(name:"DATABASE_VERSION", value:"0.1").save()
	}

	private getApplicationVersion() {
		applicationVersion = ApplicationHolder.application.metadata['app.version']
	}

	private runVersionScript(Double version){
		try{
			def script = loader.loadClass("database.scripts.version"+version.toString().substring(0,3).replace(".","_"))
			println "Running version script " + version + " ..."
			def scriptInstance = script.newInstance()
			scriptInstance.init(context)


			databaseVersion.value = version.toString()
			databaseVersion.save(flush:true)
		}catch(ClassNotFoundException e){
			println "ERROR: " + e
		}
	}

	def destroy = {
		println "SALON MANAGER HAS BEEN SHUTDOWN"
	}

}