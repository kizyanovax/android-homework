package com.example.hw_3.data

import com.google.gson.annotations.SerializedName

data class DnDClass(
    @SerializedName("index") val index: String,
    @SerializedName("name") val name: String,
    @SerializedName("hit_die") val hitDie: Int?,
    @SerializedName("proficiency_choices") val proficiencyChoices: List<ProficiencyChoice>?,
    @SerializedName("proficiencies") val proficiencies: List<ApiReference>?,
    @SerializedName("saving_throws") val savingThrows: List<ApiReference>?,
    @SerializedName("starting_equipment") val startingEquipment: List<StartingEquipment>?,
    @SerializedName("starting_equipment_options") val startingEquipmentOptions: List<StartingEquipmentOption>?,
    @SerializedName("subclasses") val subclasses: List<ApiReference>?,
    @SerializedName("spellcasting") val spellcasting: Spellcasting?,
    @SerializedName("url") val url: String
)

data class ProficiencyChoice(
    @SerializedName("desc") val desc: String?,
    @SerializedName("choose") val choose: Int?,
    @SerializedName("type") val type: String?,
    @SerializedName("from") val from: ProficiencyFrom?
)

data class ProficiencyFrom(
    @SerializedName("option_set_type") val optionSetType: String?,
    @SerializedName("options") val options: List<ProficiencyOption>?
)

data class ProficiencyOption(
    @SerializedName("item") val item: ApiReference?,
    @SerializedName("option_type") val optionType: String?
)

data class StartingEquipment(
    @SerializedName("equipment") val equipment: ApiReference?,
    @SerializedName("quantity") val quantity: Int?
)

data class StartingEquipmentOption(
    @SerializedName("desc") val desc: String?,
    @SerializedName("choose") val choose: Int?,
    @SerializedName("type") val type: String?,
    @SerializedName("from") val from: EquipmentFrom?
)

data class EquipmentFrom(
    @SerializedName("option_set_type") val optionSetType: String?,
    @SerializedName("options") val options: List<EquipmentOption>?
)

data class EquipmentOption(
    @SerializedName("item") val item: ApiReference?,
    @SerializedName("option_type") val optionType: String?
)

data class Spellcasting(
    @SerializedName("level") val level: Int?,
    @SerializedName("spellcasting_ability") val spellcastingAbility: ApiReference?,
    @SerializedName("info") val info: List<SpellcastingInfo>?
)

data class SpellcastingInfo(
    @SerializedName("name") val name: String?,
    @SerializedName("desc") val desc: List<String>?
)

