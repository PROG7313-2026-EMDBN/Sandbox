import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.prog7313.sandbox.model.Gadget
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGadgetScreen(
    onBack: () -> Unit,
    onSave: (Gadget) -> Unit
) {
    val categories = listOf("Audio", "Wearables", "Storage", "Accessories", "Smart Home", "Other")

    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }

    var category by remember { mutableStateOf(categories.first()) }
    var categoryExpanded by remember { mutableStateOf(false) }

    var priceText by remember { mutableStateOf("") }
    val price = priceText.toDoubleOrNull()
    val priceIsValid = price != null && price > 0.0

    val canSave = name.isNotBlank() && brand.isNotBlank() && category.isNotBlank() && priceIsValid

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Gadget") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Brand") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier
                        .menuAnchor(
                            type = MenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categories.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                category = option
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = priceText,
                onValueChange = { input ->
                    priceText = sanitizeMoneyInput(input)
                },
                label = { Text("Price (R)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = priceText.isNotBlank() && !priceIsValid,
                supportingText = {
                    if (priceText.isNotBlank() && !priceIsValid) {
                        Text("Enter a valid amount (e.g., 1499.99)")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    onSave(
                        Gadget(
                            id = UUID.randomUUID().toString(),
                            name = name.trim(),
                            brand = brand.trim(),
                            category = category.trim(),
                            price = price!!
                        )
                    )
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}

private fun sanitizeMoneyInput(input: String): String {
    val filtered = input.filter { it.isDigit() || it == '.' }

    val firstDot = filtered.indexOf('.')
    if (firstDot == -1) return filtered

    val before = filtered.substring(0, firstDot)
    val afterRaw = filtered.substring(firstDot + 1).replace(".", "")
    val after = afterRaw.take(2)

    return "$before.$after"
}