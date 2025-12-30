package com.example.hw_3.data

import com.google.gson.annotations.SerializedName

data class DnDSpell(
    @SerializedName("index") val index: String,
    @SerializedName("name") val name: String,
    @SerializedName("desc") val desc: List<String>?,
    @SerializedName("higher_level") val higherLevel: List<String>?,
    @SerializedName("range") val range: String?,
    @SerializedName("components") val components: List<String>?,
    @SerializedName("material") val material: String?,
    @SerializedName("ritual") val ritual: Boolean?,
    @SerializedName("duration") val duration: String?,
    @SerializedName("concentration") val concentration: Boolean?,
    @SerializedName("casting_time") val castingTime: String?,
    @SerializedName("level") val level: Int?,
    @SerializedName("attack_type") val attackType: String?,
    @SerializedName("damage") val damage: SpellDamage?,
    @SerializedName("school") val school: ApiReference?,
    @SerializedName("classes") val classes: List<ApiReference>?,
    @SerializedName("subclasses") val subclasses: List<ApiReference>?,
    @SerializedName("url") val url: String
)

data class SpellDamage(
    @SerializedName("damage_type") val damageType: ApiReference?,
    @SerializedName("damage_at_slot_level") val damageAtSlotLevel: Map<String, String>?
)

