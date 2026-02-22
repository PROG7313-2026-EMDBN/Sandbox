import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prog7313.sandbox.util.shareGadget
import com.prog7313.sandbox.viewmodel.GadgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GadgetsScreen(
    gadgetVm: GadgetViewModel,
    onBack: () -> Unit,
    onAdd: () -> Unit
) {
    val context = LocalContext.current

    val gadgetsState = gadgetVm.gadgets.collectAsStateWithLifecycle()
    val gadgets = gadgetsState.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(
            items = gadgets,
            key = { it.id }
        ) { g ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { shareGadget(context, g) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(g.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text("${g.brand} • ${g.category}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(6.dp))
                    Text("R${"%.2f".format(g.price)}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(6.dp))
                    Text("Tap to share", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
