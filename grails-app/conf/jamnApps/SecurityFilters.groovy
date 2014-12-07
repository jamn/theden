package jamnApps

class SecurityFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {

                // so we don't get stuck in a loop
                if (controllerName == "access" || controllerName == "site" || controllerName == "error" || controllerName == "assets"){
                    return true
                }

                if (!session.adminUser){
                    redirect(controller:'access', action:'login')
                    return false
                }

            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
