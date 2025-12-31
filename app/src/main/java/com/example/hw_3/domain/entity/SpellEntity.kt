package com.example.hw_3.domain.entity

data class SpellEntity(
    val index: String,
    val name: String,
    val desc: List<String>?,
    val higherLevel: List<String>?,
    val range: String?,
    val components: List<String>?,
    val material: String?,
    val ritual: Boolean?,
    val duration: String?,
    val concentration: Boolean?,
    val castingTime: String?,
    val level: Int?,
    val attackType: String?,
    val damage: SpellDamageEntity?,
    val school: ReferenceEntity?,
    val classes: List<ReferenceEntity>?,
    val subclasses: List<ReferenceEntity>?
)

data class SpellDamageEntity(
    val damageType: ReferenceEntity?,
    val damageAtSlotLevel: Map<String, String>?
)

