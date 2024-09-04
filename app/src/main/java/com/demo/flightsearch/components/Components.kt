package com.demo.flightsearch.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.demo.flightsearch.model.Airport


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightTopBar(title: String) {
    CenterAlignedTopAppBar(title = { Text(text = title) })
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector = Icons.Default.Search,
    trailingIcon: ImageVector = Icons.Filled.Mic,
    keyboardType: KeyboardType = KeyboardType.Text,
    onMicClicked: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = valueState,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        shape = RoundedCornerShape(15.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        keyboardActions = KeyboardActions { onSearchClicked.invoke() },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = "Search icon") },
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable { onMicClicked.invoke() },
                imageVector = trailingIcon,
                contentDescription = "Microphone icon"
            )
        })
    }

