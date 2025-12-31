package com.example.hw_3.data.mapper

import com.example.hw_3.data.*
import com.example.hw_3.domain.entity.*

fun ReferenceEntity.toDataModel(): ApiReference {
    return ApiReference(
        index = index,
        name = name,
        url = url
    )
}

fun ClassEntity.toDataModel(): DnDClass {
    return DnDClass(
        index = index,
        name = name,
        hitDie = hitDie,
        proficiencyChoices = null, // Simplified for backward compatibility
        proficiencies = proficiencies?.map { it.toDataModel() },
        savingThrows = savingThrows?.map { it.toDataModel() },
        startingEquipment = null, // Simplified
        startingEquipmentOptions = null, // Simplified
        subclasses = subclasses?.map { it.toDataModel() },
        spellcasting = spellcasting?.toDataModel(),
        url = "" // Will be set if needed
    )
}

fun SpellcastingEntity.toDataModel(): Spellcasting {
    return Spellcasting(
        level = level,
        spellcastingAbility = spellcastingAbility?.toDataModel(),
        info = info?.map { it.toDataModel() }
    )
}

fun SpellcastingInfoEntity.toDataModel(): SpellcastingInfo {
    return SpellcastingInfo(
        name = name,
        desc = desc
    )
}

fun SpellEntity.toDataModel(): DnDSpell {
    return DnDSpell(
        index = index,
        name = name,
        desc = desc,
        higherLevel = higherLevel,
        range = range,
        components = components,
        material = material,
        ritual = ritual,
        duration = duration,
        concentration = concentration,
        castingTime = castingTime,
        level = level,
        attackType = attackType,
        damage = damage?.toDataModel(),
        school = school?.toDataModel(),
        classes = classes?.map { it.toDataModel() },
        subclasses = subclasses?.map { it.toDataModel() },
        url = "" // Will be set if needed
    )
}

fun SpellDamageEntity.toDataModel(): SpellDamage {
    return SpellDamage(
        damageType = damageType?.toDataModel(),
        damageAtSlotLevel = damageAtSlotLevel
    )
}

fun MonsterEntity.toDataModel(): DnDMonster {
    return DnDMonster(
        index = index,
        name = name,
        size = size,
        type = type,
        subtype = subtype,
        alignment = alignment,
        armorClass = armorClass?.map { it.toDataModel() },
        hitPoints = hitPoints,
        hitDice = hitDice,
        hitPointsRoll = hitPointsRoll,
        speed = speed?.toDataModel(),
        strength = strength,
        dexterity = dexterity,
        constitution = constitution,
        intelligence = intelligence,
        wisdom = wisdom,
        charisma = charisma,
        proficiencies = null, // Not used in UI
        damageVulnerabilities = damageVulnerabilities,
        damageResistances = damageResistances,
        damageImmunities = damageImmunities,
        conditionImmunities = null, // Not used in UI
        senses = null, // Not used in UI
        languages = languages,
        challengeRating = challengeRating,
        proficiencyBonus = proficiencyBonus,
        xp = xp,
        specialAbilities = specialAbilities?.map { it.toDataModel() },
        actions = actions?.map { it.toDataModel() },
        legendaryActions = legendaryActions?.map { it.toDataModel() },
        url = "" // Will be set if needed
    )
}

fun ArmorClassEntity.toDataModel(): ArmorClass {
    return ArmorClass(
        type = type,
        value = value
    )
}

fun SpeedEntity.toDataModel(): Speed {
    return Speed(
        walk = walk,
        swim = swim,
        fly = fly,
        burrow = burrow
    )
}

fun MonsterAbilityEntity.toDataModel(): MonsterAbility {
    return MonsterAbility(
        name = name,
        desc = desc,
        attackBonus = attackBonus,
        damage = damage?.map { it.toDataModel() }
    )
}

fun MonsterActionEntity.toDataModel(): MonsterAction {
    return MonsterAction(
        name = name,
        desc = desc,
        attackBonus = attackBonus,
        damage = damage?.map { it.toDataModel() },
        dc = dc?.toDataModel(),
        usage = usage?.toDataModel()
    )
}

fun com.example.hw_3.domain.entity.ActionDCEntity.toDataModel(): ActionDC {
    return ActionDC(
        dcType = dcType?.toDataModel(),
        dcValue = dcValue,
        successType = successType
    )
}

fun com.example.hw_3.domain.entity.ActionUsageEntity.toDataModel(): ActionUsage {
    return ActionUsage(
        type = type,
        times = times
    )
}

fun MonsterDamageEntity.toDataModel(): MonsterDamage {
    return MonsterDamage(
        damageType = damageType?.toDataModel(),
        damageDice = damageDice
    )
}

