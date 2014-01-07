package salonManager

public abstract class CoreObject implements java.io.Serializable {

	Date dateCreated = new Date()
	Date dateLastUpdated
	Long createdBy
	Long updatedBy
	Boolean deleted = false

    static mapping = {
        tablePerHierarchy false
    }

    static constraints = {
    	dateCreated(nullable:true)
		dateLastUpdated(nullable:true)
		createdBy(nullable:true)
		updatedBy(nullable:true)
		deleted(nullable:true)
    }

    def beforeInsert = {
    	createdBy = -1
    }

    def afterUpdate = {
    	//updatedBy = session.user.id
    	dateLastUpdated = new Date()
    }
}
