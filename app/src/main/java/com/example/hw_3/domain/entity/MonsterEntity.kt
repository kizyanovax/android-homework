package com.example.hw_3.domain.entity

data class MonsterEntity(
    val index: String,
    val name: String,
    val size: String?,
    val type: String?,
    val subtype: String?,
    val alignment: String?,
    val armorClass: List<ArmorClassEntity>?,
    val hitPoints: Int?,
    val hitDice: String?,
    val hitPointsRoll: String?,
    val speed: SpeedEntity?,
    val strength: Int?,
    val dexterity: Int?,
    val constitution: Int?,
    val intelligence: Int?,
    val wisdom: Int?,
    val charisma: Int?,
    val damageVulnerabilities: List<String>?,
    val damageResistances: List<String>?,
    val damageImmunities: List<String>?,
    val languages: String?,
    val challengeRating: Double?,
    val proficiencyBonus: Int?,
    val xp: Int?,
    val specialAbilities: List<MonsterAbilityEntity>?,
    val actions: List<MonsterActionEntity>?,
    val legendaryActions: List<MonsterActionEntity>?
)

data class ArmorClassEntity(
    val type: String?,
    val value: Int?
)

data class SpeedEntity(
    val walk: String?,
    val swim: String?,
    val fly: String?,
    val burrow: String?
)

data class MonsterAbilityEntity(
    val name: String?,
    val desc: String?,
    val attackBonus: Int?,
    val damage: List<MonsterDamageEntity>?
)

data class MonsterActionEntity(
    val name: String?,
    val desc: String?,
    val attackBonus: Int?,
    val damage: List<MonsterDamageEntity>?,
    val dc: ActionDCEntity?,
    val usage: ActionUsageEntity?
)

data class ActionDCEntity(
    val dcType: ReferenceEntity?,
    val dcValue: Int?,
    val successType: String?
)

data class ActionUsageEntity(
    val type: String?,
    val times: Int?
)

data class MonsterDamageEntity(
    val damageType: ReferenceEntity?,
    val damageDice: String?
)

