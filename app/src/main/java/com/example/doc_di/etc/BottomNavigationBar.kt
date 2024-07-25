package com.example.doc_di.etc

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.home.BtmBarViewModel

@Composable
fun BottomNavigationBar(navController: NavController, btmBarViewModel: BtmBarViewModel) {
    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier.height(90.dp)
    ) {
        btmBarViewModel.btmNavBarItems.forEach { navItem ->
            val selected = navItem.selected
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    navController.navigate(navItem.route){
                        restoreState = true
                    }
                    btmBarViewModel.btmNavBarItems.forEach{ afterRecomposeBtmBarItem ->
                        afterRecomposeBtmBarItem.selected = afterRecomposeBtmBarItem.route == navItem.route
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = navItem.iconResourceId) ,
                        contentDescription = navItem.title,
                        tint = if(navItem.selected) Color(0xFF007AEB) else Color(0xFF95CCFF),
                        modifier = if(navItem.title == "챗봇 화면") Modifier.size(36.dp) else Modifier.size(28.dp)
                    )
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}