package com.sam.yoga.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sam.yoga.domain.navigation.Route

@Composable
fun BottomBar(
    navController: NavHostController,
    currentRoute: Route?,
    modifier: Modifier = Modifier
) {
    val routes = Route.routes

    if (currentRoute != Route.Scan() && currentRoute != Route.EditProfile &&
        currentRoute != Route.RecentActivity
    ) {
        Row(
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            routes.forEach { item ->
                BottomBarItem(
                    icon = item.icon,
                    label = item::class.java.name,
                    onClick = { navController.navigate(item) },
                    modifier = modifier,
                    selected = currentRoute == item
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    @DrawableRes icon: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    Box(
        modifier = modifier
            .padding(horizontal = 5.dp)
            .size(80.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember(::MutableInteractionSource)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = if (selected) Color.White else Color.Gray
        )
    }
}

@Preview
@Composable
private fun NavBarPreview() {
    BottomBar(
        navController = rememberNavController(),
        currentRoute = Route.Home
    )
}