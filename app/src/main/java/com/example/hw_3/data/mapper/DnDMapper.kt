package com.example.hw_3.data.mapper

import com.example.hw_3.data.*
import com.example.hw_3.domain.entity.*

fun ApiReference.toEntity(): ReferenceEntity {
    return ReferenceEntity(
        index = index,
        name = name,
        url = url
    )
}

fun DnDClass.toEntity(): ClassEntity {
    return ClassEntity(
        index = index,
        name = name,
        hitDie = hitDie,
        proficiencies = proficiencies?.map { it.toEntity() },
        savingThrows = savingThrows?.map { it.toEntity() },
        subclasses = subclasses?.map { it.toEntity() },
        spellcasting = spellcasting?.toEntity()
    )
}

fun Spellcasting.toEntity(): SpellcastingEntity {
    return SpellcastingEntity(
        level = level,
        spellcastingAbility = spellcastingAbility?.toEntity(),
        info = info?.map { it.toEntity() }
    )
}

fun SpellcastingInfo.toEntity(): SpellcastingInfoEntity {
    return SpellcastingInfoEntity(
        name = name,
        desc = desc
    )
}

fun DnDSpell.toEntity(): SpellEntity {
    return SpellEntity(
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
        damage = damage?.toEntity(),
        school = school?.toEntity(),
        classes = classes?.map { it.toEntity() },
        subclasses = subclasses?.map { it.toEntity() }
    )
}

fun SpellDamage.toEntity(): SpellDamageEntity {
    return SpellDamageEntity(
        damageType = damageType?.toEntity(),
        damageAtSlotLevel = damageAtSlotLevel
    )
}

fun DnDMonster.toEntity(): MonsterEntity {
    return MonsterEntity(
        index = index,
        name = name,
        size = size,
        type = type,
        subtype = subtype,
        alignment = alignment,
        armorClass = armorClass?.map { it.toEntity() },
        hitPoints = hitPoints,
        hitDice = hitDice,
        hitPointsRoll = hitPointsRoll,
        speed = speed?.toEntity(),
        strength = strength,
        dexterity = dexterity,
        constitution = constitution,
        intelligence = intelligence,
        wisdom = wisdom,
        charisma = charisma,
        damageVulnerabilities = damageVulnerabilities,
        damageResistances = damageResistances,
        damageImmunities = damageImmunities,
        languages = languages,
        challengeRating = challengeRating,
        proficiencyBonus = proficiencyBonus,
        xp = xp,
        specialAbilities = specialAbilities?.map { it.toEntity() },
        actions = actions?.map { it.toEntity() },
        legendaryActions = legendaryActions?.map { it.toEntity() }
    )
}

fun ArmorClass.toEntity(): ArmorClassEntity {
    return ArmorClassEntity(
        type = type,
        value = value
    )
}

fun Speed.toEntity(): SpeedEntity {
    return SpeedEntity(
        walk = walk,
        swim = swim,
        fly = fly,
        burrow = burrow
    )
}

fun MonsterAbility.toEntity(): MonsterAbilityEntity {
    return MonsterAbilityEntity(
        name = name,
        desc = desc,
        attackBonus = attackBonus,
        damage = damage?.map { it.toEntity() }
    )
}

fun MonsterAction.toEntity(): MonsterActionEntity {
    return MonsterActionEntity(
        name = name,
        desc = desc,
        attackBonus = attackBonus,
        damage = damage?.map { it.toEntity() },
        dc = dc?.toEntity(),
        usage = usage?.toEntity()
    )
}

fun ActionDC.toEntity(): com.example.hw_3.domain.entity.ActionDCEntity {
    return com.example.hw_3.domain.entity.ActionDCEntity(
        dcType = dcType?.toEntity(),
        dcValue = dcValue,
        successType = successType
    )
}

fun ActionUsage.toEntity(): com.example.hw_3.domain.entity.ActionUsageEntity {
    return com.example.hw_3.domain.entity.ActionUsageEntity(
        type = type,
        times = times
    )
}

fun MonsterDamage.toEntity(): MonsterDamageEntity {
    return MonsterDamageEntity(
        damageType = damageType?.toEntity(),
        damageDice = damageDice
    )
}

