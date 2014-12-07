class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:"site", action:"index")
        "/admin"(controller:"admin", action:"index")
		"500"(controller:"error", action:"index")
		"404"(controller:"error", action:"index")
	}
}
