package top.zengarden.joinbook.core.manager.storage.impl

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import top.zengarden.joinbook.config.MainConfig
import top.zengarden.joinbook.core.manager.DataManager
import top.zengarden.joinbook.core.manager.storage.StorageEngine
import top.zengarden.joinbook.log

class MySQL : StorageEngine {

    private lateinit var hikari: HikariDataSource

    override fun init() {
        hikari = HikariDataSource()
        val config = HikariConfig()
        config.poolName = "JoinBook-Hikari"
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
        conn.createStatement()
        val stmt = conn.createStatement()
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS `joinbook`(" +
                    "`uuid`             VARCHAR(36)        NOT NULL," +
                    "`joinedBefore`     TINYINT(1)         NOT NULL," +
                    " PRIMARY KEY (`uuid`));")
        stmt.close()
        conn.close()
    }

    override fun load(data: DataManager) {
        val conn = hikari.connection
        val stmt = conn.prepareStatement("SELECT * FROM `joinbook` WHERE `uuid` = ?")
        stmt.setString(1, data.uuid.toString())
        val rs = stmt.executeQuery()
        if (rs.next()) {
            data.hasJoinedBefore = rs.getBoolean("joinedBefore")
        }
        rs.close()
        conn.close()
    }

    override fun upsertData(data: DataManager) {
        val conn = hikari.connection
        val stmt = conn.prepareStatement(
            "INSERT INTO joinbook (`uuid`, `joinedBefore`) VALUES (?, ?)" +
                    " ON DUPLICATE KEY UPDATE `joinedBefore` = VALUES (joinedBefore)"
        )
        stmt.setString(1, data.uuid.toString())
        stmt.setBoolean(2, data.hasJoinedBefore)
        stmt.execute()
        stmt.close()
        conn.close()
    }

    override fun close() {
        hikari.close()
    }


}