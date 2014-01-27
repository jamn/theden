class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller:"site", action:"index")
		//"500"(view:'/error')
		"500"(controller:"error", action:"index")
		"404"(controller:"error", action:"index")
	}
}
