package com.ledge.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledge.core.CurrencyType
import com.ledge.ui.components.core.LedgeCard
import com.ledge.ui.components.core.LedgeScaffold
import com.ledge.ui.components.core.LedgeScreenTitle
import com.ledge.ui.theme.LedgeTheme
import com.ledge.ui.theme.textSecondary
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    onOpenBudgetSettings: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    // FILE SYSTEM EXTRACTION ACTIONS (BACKUP EXPORT/IMPORT CONTRACTS)
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            coroutineScope.launch {
                val file = viewModel.exportBackup(context)
                val json = file.readText()
                context.contentResolver.openOutputStream(it)?.bufferedWriter()?.use { writer ->
                    writer.write(json)
                }
                snackBarHostState.showSnackbar("Backup exported")
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            val json = context.contentResolver.openInputStream(it)?.bufferedReader()?.readText() ?: return@let
            viewModel.restoreBackup(json)
            coroutineScope.launch {
                snackBarHostState.showSnackbar("Backup restored")
            }
        }
    }

    // CENTRALIZED SCAFFOLDING - Replaces structural scaffold boilerplate
    LedgeScaffold(snackbarHostState = snackBarHostState) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                LedgeScreenTitle(title = "Settings")
            }

            item {
                SettingsSectionTitle(title = "Finance")
            }

            // BUDGET CONFIGURATION SWITCH BLOCK
            item {
                LedgeCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = LedgeTheme.surfaces.surfaceHigh
                ) {
                    SettingsSwitchRow(
                        title = "Enable Budget Mode",
                        subtitle = if (uiState.budgetMode) {
                            "Track spending against category budgets"
                        } else {
                            "Show relative category spending"
                        },
                        checked = uiState.budgetMode,
                        onCheckedChange = { viewModel.setBudgetMode(it) }
                    )
                }
            }

            // DEBT INTEREST INTEGRATION SWITCH BLOCK
            item {
                LedgeCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = LedgeTheme.surfaces.surfaceHigh
                ) {
                    SettingsSwitchRow(
                        title = "Include Debt & Lending",
                        subtitle = "Show net balance instead of cash only",
                        checked = uiState.includeDebtInBalance,
                        onCheckedChange = { viewModel.setIncludeDebtInBalance(it) }
                    )
                }
            }

            // REDIRECT MANAGEMENT ACTION BLOCK
            item {
                LedgeCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = LedgeTheme.surfaces.surfaceHigh
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Budget Settings",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Configure monthly spending budgets for categories.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.textSecondary
                        )
                        FilledTonalButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onOpenBudgetSettings() },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = LedgeTheme.surfaces.surfaceHighest
                            )
                        ) {
                            Text("Manage Budgets")
                        }
                    }
                }
            }

            // SELECTION CURRENCY ROW BLOCK
            item {
                LedgeCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = LedgeTheme.surfaces.surfaceHigh
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "Preferred Currency",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(CurrencyType.INR, CurrencyType.USD).forEach { type ->
                                val selected = uiState.currency == type
                                FilledTonalButton(
                                    onClick = { viewModel.setCurrency(type) },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = if (selected) LedgeTheme.surfaces.surfaceHighest else LedgeTheme.surfaces.surface,
                                        contentColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                ) {
                                    Text(text = type.name)
                                }
                            }
                        }
                        Text(
                            text = "Current: ${uiState.currency.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // BIOMETRICS REACTION BLOCK
            item {
                LedgeCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = LedgeTheme.surfaces.surfaceHigh
                ) {
                    SettingsSwitchRow(
                        title = "Biometric Lock",
                        subtitle = "Protect the app using device biometrics",
                        checked = uiState.biometricLock,
                        onCheckedChange = { viewModel.setBiometricLock(it) }
                    )
                }
            }

            // DATA STORE BACKUP MANAGEMENT ACTION GROUP
            item {
                LedgeCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = LedgeTheme.surfaces.surfaceHigh
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Backup",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Export or restore your finance data.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.textSecondary
                        )
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { exportLauncher.launch("ledge_backup.json") },
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Export Backup")
                        }
                        FilledTonalButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { importLauncher.launch(arrayOf("application/json")) },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = LedgeTheme.surfaces.surfaceHighest
                            )
                        ) {
                            Text("Restore Backup")
                        }
                    }
                }
            }

            item {
                SettingsSectionTitle(title = "About")
            }

            // APPLICATION DETAIL BLOCK
            item {
                LedgeCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = LedgeTheme.surfaces.surfaceHigh
                ) {
                    SettingsInfoRow(
                        title = "Ledge",
                        subtitle = "Personal Finance Tracker"
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = LedgeTheme.surfaces.surfaceHighest
                    )
                    SettingsInfoRow(
                        title = "Version",
                        subtitle = "1.0"
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textSecondary
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingsInfoRow(
    title: String,
    subtitle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.textSecondary
        )
    }
}