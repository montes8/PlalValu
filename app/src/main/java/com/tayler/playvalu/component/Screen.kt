package com.tayler.playvalu.component

const val ROOT_GRAPH_ROUTE = "root"

sealed class Screen (open val route: String) {
    object HomeScreen : Screen("home_screen")
    object SplashScreen : Screen("splash_screen")


    fun withArgs(vararg args: String = emptyArray(), optional: Map<String, String?> = emptyMap()): String {
    return buildString {
            append(route)
            args.forEach { append("/$it") }

            if(optional.isNotEmpty()) {
                append("?")
            }

            optional.entries.forEachIndexed { index, map ->
                map.value?.let {
                    if(index != 0) append("&")

                    append("${map.key}=${map.value}")
                }
            }
        }
    }


}


