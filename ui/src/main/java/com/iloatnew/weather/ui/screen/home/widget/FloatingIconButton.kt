package com.iloatnew.weather.ui.screen.home.widget

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iloatnew.weather.R

@Composable
fun FloatingIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    shape: Shape = FloatingActionButtonDefaults.smallShape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = 0.dp
    ),
    interactionSource: MutableInteractionSource? = null,
) {
    SmallFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}

@Composable
fun FloatingLocationButton(
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit,
) {
    FloatingIconButton(
        onClick = onClick,
        imageVector = Icons.Default.LocationOn,
        contentDescription = stringResource(R.string.home_floating_location_button_description),
        tint = tint,
    )
}

@Composable
fun FloatingAddButton(
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit,
) {
    FloatingIconButton(
        onClick = onClick,
        imageVector = Icons.Default.Add,
        contentDescription = stringResource(R.string.home_floating_add_button_description),
        tint = tint,
    )
}

@Composable
fun FloatingRefreshButton(
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit,
) {
    FloatingIconButton(
        onClick = onClick,
        imageVector = Icons.Default.Refresh,
        contentDescription = stringResource(R.string.home_floating_refresh_button_description),
        tint = tint,
    )
}
