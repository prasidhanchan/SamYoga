package com.sam.yoga.domain.mappers

import com.sam.yoga.domain.models.YogaPose
import com.sam.yoga.domain.models.YogaPoseData

fun YogaPoseData.toYogaPose(): YogaPose {
    return YogaPose(
        name = this.name,
        description = this.description,
        level = this.level,
        time = this.time,
        image = this.image
    )
}

fun YogaPose.toYogaPoseData(): YogaPoseData {
    return YogaPoseData(
        name = this.name,
        description = this.description,
        level = this.level,
        time = this.time,
        image = this.image
    )
}