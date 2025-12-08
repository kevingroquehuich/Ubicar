package com.roque.ubicar.authentication.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roque.ubicar.R

@Composable
fun LoginWithGoogleButton(
    onClick: () -> Unit = {},
    text: String,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(70.dp))
            .background(Color.Yellow)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google_logo),
            contentDescription = "Google Logo"
        )

        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_google_logo),
            contentDescription = "Google Logo",
            tint = Color.Yellow
        )
    }
}

@Preview
@Composable
fun LoginWithGoogleButtonPreview() {
    LoginWithGoogleButton(
        onClick = {},
        text = "Continue with Google",
        modifier = Modifier.fillMaxWidth()
    )
}