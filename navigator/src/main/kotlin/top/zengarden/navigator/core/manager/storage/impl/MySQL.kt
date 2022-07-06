package top.zengarden.navigator.core.manager.storage.impl

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import top.zengarden.navigator.config.MainConfig
import top.zengarden.navigator.config.NavigatorConfig
import top.zengarden.navigator.core.manager.DataManager
import top.zengarden.navigator.core.manager.storage.StorageEngine
import top.zengarden.navigator.log

class MySQL : StorageEngine {

    private lateinit var hikari: HikariDataSource

    override fun init() {
        hikari = HikariDataSource()
        val config = HikariConfig()
        config.poolName = "Navigator-Hikari"
        config.username = MainConfig.user
        config.password = MainConfig.passwd
        config.jdbcUrl = "jdbc:mysql://${MainConfig.url}/${MainConfig.dbName}"
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        config.addDataSourceProperty("useServerPrepStmts", "true")
        config.addDataSourceProperty("useLocalSessionState", "true")
        config.addDataSourceProperty("rewriteBatchedStatements", "true")
        config.addDataSourceProperty("cacheResultSetMetadata", "true")
        config.addDataSourceProperty("cacheServerConfiguration", "true")
        config.addDataSourceProperty("elideSetAutoCommits", "true")
        config.addDataSourceProperty("maintainTimeStats", "false")
        config.addDataSourceProperty("alwaysSendSetIsolation", "false")
        config.addDataSourceProperty("cacheCallableStmts", "true")
        config.addDataSourceProperty("useUnicode", "true")
        config.addDataSourceProperty("characterEncoding", "utf-8")
        config.addDataSourceProperty("useSSL", "false")
        config.maximumPoolSize = 10
        config.minimumIdle = 10
        config.maxLifetime = 1800000
        config.connectionTimeout = 5000
        this.hikari = HikariDataSource(config)
        createTable()
        log("§a已连接到 MySQL")
    }

    private fun createTable() {
        val conn = hikari.connection
        val stmt = conn.createStatement()
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS `navigator`(" +
                    "`uuid`             VARCHAR(36)        NOT NULL," +
                    "`targeting`        VARCHAR(36)," +
                    "`nav_index`            INT(50)," +
                    " PRIMARY KEY (`uuid`));")
        stmt.close()
        conn.close()
    }

    override fun load(data: DataManager) {
        val conn = hikari.connection
        val stmt = conn.prepareStatement("SELECT * FROM `navigator` WHERE `uuid` = ?")
        stmt.setString(1, data.uuid.toString())
        val rs = stmt.executeQuery()
        if (rs.next()) {
            val targetingId = rs.getString("targeting")
            val index = rs.getInt("nav_index")
            data.nav = NavigatorConfig.navigators.find { it.id == targetingId }
            data.index = index
        }
        rs.close()
        conn.close()
    }

    override fun upsertData(data: DataManager) {
        val conn = hikari.connection
        val stmt = conn.prepareStatement(
            "INSERT INTO navigator (`uuid`, `targeting`, `nav_index`) VALUES (?, ?, ?)" +
                    " ON DUPLICATE KEY UPDATE " +
                    "`targeting` = VALUES (targeting), " +
                    "`nav_index` = VALUES (nav_index)"
        )
        stmt.setString(1, data.uuid.toString())
        stmt.setString(2, data.nav?.id)
        stmt.setInt(3, data.index)
        stmt.execute()
        stmt.close()
        conn.close()
    }

    override fun close() {
        hikari.close()
    }


}