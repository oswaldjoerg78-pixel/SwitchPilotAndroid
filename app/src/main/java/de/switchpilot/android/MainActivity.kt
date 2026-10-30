package de.switchpilot.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContent { SwitchPilotApp() }
}
}

private data class Agent(val name: String, val description: String)

private val agents = listOf(
Agent("Setup-Agent", "Hilfe bei der Grundeinrichtung"),
Agent("Fehler-Agent", "Checkliste zur Fehlersuche"),
Agent("Bibliothek-Agent", "Verwaltung deiner Spieleliste"),
Agent("Performance-Agent", "Tipps für Grafik und FPS")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwitchPilotApp() {
var tab by remember { mutableIntStateOf(0) }
var gameName by remember { mutableStateOf("") }
var games by remember { mutableStateOf(listOf<String>()) }

Scaffold(
    topBar = {
        TopAppBar(title = {
            Column {
                Text("SwitchPilot", fontWeight = FontWeight.Bold)
                Text("Dein privater Gaming-Assistent",
                    style = MaterialTheme.typography.labelSmall)
            }
        })
    },
    bottomBar = {
        NavigationBar {
            NavigationBarItem(
                selected = tab == 0,
                onClick = { tab = 0 },
                icon = { Icon(Icons.Default.Home, null) },
                label = { Text("Start") }
            )
            NavigationBarItem(
                selected = tab == 1,
                onClick = { tab = 1 },
                icon = { Icon(Icons.Default.SmartToy, null) },
                label = { Text("Agenten") }
            )
            NavigationBarItem(
                selected = tab == 2,
                onClick = { tab = 2 },
                icon = { Icon(Icons.Default.SportsEsports, null) },
                label = { Text("Spiele") }
            )
        }
    }
) { padding ->
    when (tab) {
        0 -> HomeScreen(Modifier.padding(padding))
        1 -> AgentsScreen(Modifier.padding(padding))
        else -> GamesScreen(
            games = games,
            gameName = gameName,
            onNameChange = { gameName = it },
            onAdd = {
                if (gameName.isNotBlank()) {
                    games = games + gameName.trim()
                    gameName = ""
                }
            },
            onDelete = { game -> games = games - game },
            modifier = Modifier.padding(padding)
        )
    }
}

}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
Column(
modifier.fillMaxSize().padding(20.dp),
verticalArrangement = Arrangement.spacedBy(14.dp)
) {
Text("Willkommen!", style = MaterialTheme.typography.headlineMedium)
Text("Dein Dashboard für Einrichtung, Fehlersuche und Spieleverwaltung.")
Card {
Column(Modifier.padding(16.dp)) {
Text("Projektstatus", style = MaterialTheme.typography.titleMedium)
Text("Grundoberfläche vorbereitet.")
Text("Eine Switch-Emulator-Engine ist noch nicht integriert oder getestet.")
}
}
Text("Deine vier Helfer", style = MaterialTheme.typography.titleLarge)
agents.forEach {
ListItem(
headlineContent = { Text(it.name) },
supportingContent = { Text(it.description) }
)
HorizontalDivider()
}
}
}

@Composable
private fun AgentsScreen(modifier: Modifier = Modifier) {
LazyColumn(modifier.fillMaxSize().padding(12.dp)) {
item {
Text("Helfer-Agenten", style = MaterialTheme.typography.headlineMedium)
}
items(agents) {
Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
ListItem(
headlineContent = { Text(it.name, fontWeight = FontWeight.SemiBold) },
supportingContent = { Text(it.description) }
)
}
}
item {
Text("Die Helfer sind derzeit einfache Info- und Checklistenfunktionen.")
}
}
}

@Composable
private fun GamesScreen(
games: List<String>,
gameName: String,
onNameChange: (String) -> Unit,
onAdd: () -> Unit,
onDelete: (String) -> Unit,
modifier: Modifier = Modifier
) {
Column(
modifier.fillMaxSize().padding(16.dp),
verticalArrangement = Arrangement.spacedBy(12.dp)
) {
Text("Meine Spiele", style = MaterialTheme.typography.headlineMedium)
Text("Bitte trage nur Spiele ein, die du rechtmäßig besitzt.")
OutlinedTextField(
value = gameName,
onValueChange = onNameChange,
label = { Text("Spielname") },
modifier = Modifier.fillMaxWidth(),
singleLine = true
)
Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) {
Text("Zur Liste hinzufügen")
}
if (games.isEmpty()) {
Text("Noch keine Spiele eingetragen.")
} else {
LazyColumn {
items(games) { game ->
ListItem(
headlineContent = { Text(game) },
trailingContent = {
IconButton(onClick = { onDelete(game) }) {
Icon(Icons.Default.Delete, contentDescription = "Entfernen")
}
}
)
}
}
}
}
}
