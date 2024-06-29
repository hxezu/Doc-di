package com.example.doc_di.etc

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.doc_di.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier.height(90.dp)
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        val btmNavBarItems = listOf<BtmBarItem>(
            BtmBarItem(
                title = "홈 화면",
                icon = painterResource(id = R.drawable.home),
                route = Routes.HomeScreen.route
            ),
            BtmBarItem(
                title = "검색 화면",
                icon = painterResource(id = R.drawable.pill),
                route = Routes.searchScreen.route
            ),
            BtmBarItem(
                title = "챗봇 화면",
                icon = painterResource(id = R.drawable.chatbot),
                route = Routes.chatbotScreen.route
            ),
            BtmBarItem(
                title = "복용 관리 화면",
                icon = painterResource(id = R.drawable.management),
                route = Routes.managementScreen.route
            )
        )

        btmNavBarItems.forEach { navItem ->
            val selected = currentRoute == navItem.route
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    navController.navigate(navItem.route){
                        restoreState=true
                    }
                },
                icon = {
                    Icon(
                        painter = navItem.icon,
                        contentDescription = navItem.title,
                        tint = if(selected) Color(0xFF007AEB) else Color(0xFF95CCFF),
                        modifier = if(navItem.title == "챗봇 화면") Modifier.size(36.dp) else Modifier.size(28.dp)
                    )
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}