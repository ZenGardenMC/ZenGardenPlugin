package top.zengarden.navigator.config

import top.zengarden.navigator.core.navigator.PathNavigator

object NavigatorConfig : Config("navigators") {

    val navigators by value("navigators", mutableListOf<PathNavigator>())

}