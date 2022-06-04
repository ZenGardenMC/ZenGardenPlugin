package top.zengarden.navigator.config

object MainConfig : Config("config") {

    var url by value("sql.url", "localhost:3306")
    var user by value("sql.user", "root")
    var passwd by value("sql.passwd", "passwd")
    var dbName by value("sql.database", "database")

}