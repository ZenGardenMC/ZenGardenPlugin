package top.zengarden.joinbook.config

object MainConfig : Config("config") {

    var url by value("sql.url", "localhost:3306")
    var user by value("sql.user", "root")
    var passwd by value("sql.passwd", "passwd")
    var dbName by value("sql.database", "database")

    var title by value("book.title", "&b阿巴阿巴")

    var content by value("book.content", listOf("&b阿巴阿巴\n&c阿巴阿巴", "&b阿巴阿巴\n&c阿巴阿巴"))

}