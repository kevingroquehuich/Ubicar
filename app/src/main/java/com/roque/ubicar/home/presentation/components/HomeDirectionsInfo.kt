package com.roque.ubicar.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeDirectionsInfo(
    onClick: () -> Unit,
    distance: String?,
    modifier: Modifier
) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            distance?.let {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Your Vehicle",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row {
                        Icon(
                            imageVector = Icons.Outlined.Route,
                            contentDescription = "",
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "$it away",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }


            HomeButton(
                onClick = { onClick() },
                text = "Stop Searching"
            )
        }
    }
}

@Preview
@Composable
fun HomeDirectionsInfoPreview() {
    HomeDirectionsInfo(
        onClick = {},
        distance = "10.5 km away",
        modifier = Modifier.fillMaxWidth()
    )
}