package com.example.hw_3.core


sealed class Routes(val route: String) {
    object Welcome : Routes("welcome")
    object Screen1 : Routes("screen1")
    object Screen2 : Routes("screen2")
    object Screen3 : Routes("screen3")
    object Profile : Routes("profile")
    object EditProfile : Routes("edit_profile")
    object Filter : Routes("filter")
    object ClassFilter : Routes("class_filter")
    object Favorites : Routes("favorites/{type}") {
        fun createRoute(type: String) = "favorites/$type"
    }
    object ClassDetail : Routes("class_detail/{classIndex}") {
        fun createRoute(classIndex: String) = "class_detail/$classIndex"
    }
    object SpellDetail : Routes("spell_detail/{spellIndex}") {
        fun createRoute(spellIndex: String) = "spell_detail/$spellIndex"
    }
    object MonsterDetail : Routes("monster_detail/{monsterIndex}") {
        fun createRoute(monsterIndex: String) = "monster_detail/$monsterIndex"
    }
}

