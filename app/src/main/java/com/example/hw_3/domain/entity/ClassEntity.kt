package com.example.hw_3.domain.entity

data class ClassEntity(
    val index: String,
    val name: String,
    val hitDie: Int?,
    val proficiencies: List<ReferenceEntity>?,
    val savingThrows: List<ReferenceEntity>?,
    val subclasses: List<ReferenceEntity>?,
    val spellcasting: SpellcastingEntity?
)

data class ReferenceEntity(
    val index: String,
    val name: String,
    val url: String
)

data class SpellcastingEntity(
    val level: Int?,
    val spellcastingAbility: ReferenceEntity?,
    val info: List<SpellcastingInfoEntity>?
)

data class SpellcastingInfoEntity(
    val name: String?,
    val desc: List<String>?
)

