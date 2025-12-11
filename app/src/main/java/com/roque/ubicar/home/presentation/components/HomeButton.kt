package com.roque.ubicar.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roque.ubicar.R

@Composable
fun HomeButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    contentDescription: String? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            imageVector?.let {
                Icon(
                    imageVector = it,
                    contentDescription = contentDescription,
                )
                Spacer(modifier = Modifier.size(12.dp))
            }
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun HomeButtonPreview() {
    // To visualize the theme in preview, you might want to wrap this in UbiCarTheme
    // But for a simple component preview, standard is fine.
    HomeButton(
        onClick = {},
        text = stringResource(R.string.get_directions),
        imageVector = Icons.Default.Directions,
        modifier = Modifier.fillMaxWidth(),
        contentDescription = "Get directions icon"
    )
}