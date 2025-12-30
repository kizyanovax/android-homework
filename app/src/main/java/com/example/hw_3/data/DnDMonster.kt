package com.example.hw_3.data

import com.google.gson.annotations.SerializedName

data class DnDMonster(
    @SerializedName("index") val index: String,
    @SerializedName("name") val name: String,
    @SerializedName("size") val size: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("subtype") val subtype: String?,
    @SerializedName("alignment") val alignment: String?,
    @SerializedName("armor_class") val armorClass: List<ArmorClass>?,
    @SerializedName("hit_points") val hitPoints: Int?,
    @SerializedName("hit_dice") val hitDice: String?,
    @SerializedName("hit_points_roll") val hitPointsRoll: String?,
    @SerializedName("speed") val speed: Speed?,
    @SerializedName("strength") val strength: Int?,
    @SerializedName("dexterity") val dexterity: Int?,
    @SerializedName("constitution") val constitution: Int?,
    @SerializedName("intelligence") val intelligence: Int?,
    @SerializedName("wisdom") val wisdom: Int?,
    @SerializedName("charisma") val charisma: Int?,
    @SerializedName("proficiencies") val proficiencies: List<MonsterProficiency>?,
    @SerializedName("damage_vulnerabilities") val damageVulnerabilities: List<String>?,
    @SerializedName("damage_resistances") val damageResistances: List<String>?,
    @SerializedName("damage_immunities") val damageImmunities: List<String>?,
    @SerializedName("condition_immunities") val conditionImmunities: List<ApiReference>?,
    @SerializedName("senses") val senses: Senses?,
    @SerializedName("languages") val languages: String?,
    @SerializedName("challenge_rating") val challengeRating: Double?,
    @SerializedName("proficiency_bonus") val proficiencyBonus: Int?,
    @SerializedName("xp") val xp: Int?,
    @SerializedName("special_abilities") val specialAbilities: List<MonsterAbility>?,
    @SerializedName("actions") val actions: List<MonsterAction>?,
    @SerializedName("legendary_actions") val legendaryActions: List<MonsterAction>?,
    @SerializedName("url") val url: String
)

data class ArmorClass(
    @SerializedName("type") val type: String?,
    @SerializedName("value") val value: Int?
)

data class Speed(
    @SerializedName("walk") val walk: String?,
    @SerializedName("swim") val swim: String?,
    @SerializedName("fly") val fly: String?,
    @SerializedName("burrow") val burrow: String?
)

data class MonsterProficiency(
    @SerializedName("proficiency") val proficiency: ApiReference?,
    @SerializedName("value") val value: Int?
)

data class Senses(
    @SerializedName("blindsight") val blindsight: String?,
    @SerializedName("darkvision") val darkvision: String?,
    @SerializedName("passive_perception") val passivePerception: Int?
)

data class MonsterAbility(
    @SerializedName("name") val name: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("attack_bonus") val attackBonus: Int?,
    @SerializedName("damage") val damage: List<MonsterDamage>?
)

data class MonsterAction(
    @SerializedName("name") val name: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("attack_bonus") val attackBonus: Int?,
    @SerializedName("damage") val damage: List<MonsterDamage>?,
    @SerializedName("dc") val dc: ActionDC?,
    @SerializedName("usage") val usage: ActionUsage?
)

data class MonsterDamage(
    @SerializedName("damage_type") val damageType: ApiReference?,
    @SerializedName("damage_dice") val damageDice: String?
)

data class ActionDC(
    @SerializedName("dc_type") val dcType: ApiReference?,
    @SerializedName("dc_value") val dcValue: Int?,
    @SerializedName("success_type") val successType: String?
)

data class ActionUsage(
    @SerializedName("type") val type: String?,
    @SerializedName("times") val times: Int?
)


