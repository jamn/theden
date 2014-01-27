dataSource {
	pooled = true
	maxActive = 20
	initialSize = 5
	driverClassName = "org.hsqldb.jdbcDriver"
	username = "sa"
	password = ""
}
hibernate {
	cache.use_second_level_cache=true
	cache.use_query_cache=true
	cache.provider_class='org.hibernate.cache.EhCacheProvider'
}
environments {

	development {
		dataSource {
			//logSql = true;
			dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:mysql://localhost/salon_manager?useOldAliasMetadataBehavior=true&autoReconnect=true"
			driverClassName = "com.mysql.jdbc.Driver"
			username = "root"
			password = ""
		}
	}

	test {
		dataSource {
			//logSql = true;
			dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:mysql://localhost/salon_manager?useOldAliasMetadataBehavior=true&autoReconnect=true"
			driverClassName = "com.mysql.jdbc.Driver"
			username = "sm1013"
			password = "654uxF8Wdd0!"
		}
	}

	production {
		dataSource {
			//logSql = true;
			dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:mysql://localhost/salon_manager?useOldAliasMetadataBehavior=true&autoReconnect=true"
			driverClassName = "com.mysql.jdbc.Driver"
			username = "sm1013"
			password = "654uxF8Wdd0!"
		}
	}
}