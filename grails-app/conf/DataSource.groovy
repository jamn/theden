dataSource {
	pooled = true
	jmxExport = true
	//driverClassName = "org.hsqldb.jdbcDriver"
	//username = "sa"
	//password = ""
	properties {
       jmxEnabled = true
       initialSize = 5
       maxActive = 50
       minIdle = 5
       maxIdle = 25
       maxWait = 10000
       maxAge = 10 * 60000
       timeBetweenEvictionRunsMillis = 5000
       minEvictableIdleTimeMillis = 60000
       validationQuery = "SELECT 1"
       validationQueryTimeout = 3
       validationInterval = 15000
       testOnBorrow = true
       testWhileIdle = true
       testOnReturn = false
       jdbcInterceptors = "ConnectionState;StatementCache(max=200)"
       defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
    }
}
hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
	cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
	singleSession = true // configure OSIV singleSession mode
	flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
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
