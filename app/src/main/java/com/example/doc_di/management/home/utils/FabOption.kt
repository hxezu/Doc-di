package com.example.doc_di.management.home.utils

import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.example.doc_di.ui.theme.MainBlue

@Immutable
interface FabOption {
    @Stable
    val iconTint: Color

    @Stable
    val backgroundTint: Color

    @Stable
    val showLabels: Boolean
}

private class FabOptionImpl(
    override val iconTint: Color,
    override val backgroundTint: Color,
    override val showLabels: Boolean
) : FabOption

@Composable
fun FabOption(
    backgroundTint: Color = MainBlue,
    iconTint: Color = contentColorFor(backgroundTint),
    showLabels: Boolean = false
): FabOption =
    FabOptionImpl(iconTint = iconTint, backgroundTint = backgroundTint, showLabels = showLabels)