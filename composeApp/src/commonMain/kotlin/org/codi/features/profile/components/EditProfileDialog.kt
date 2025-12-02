package org.codi.features.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.features.profile.ProfileViewModel
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen

@Composable
fun EditProfileDialog(viewModel: ProfileViewModel) {
    if (viewModel.showEditDialog.value) {
        var nameError by remember { mutableStateOf(false) }
        var lastNameError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = {
                if (!viewModel.isUpdating.value) {
                    viewModel.closeEditDialog()
                    nameError = false
                    lastNameError = false
                }
            },
            title = {
                Text(
                    text = "Editar Perfil",
                    style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Nombre
                    OutlinedTextField(
                        value = viewModel.tempName.value,
                        onValueChange = {
                            viewModel.updateTempName(it)
                            nameError = it.isBlank()
                        },
                        label = { Text("Nombre") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = if (nameError) MaterialTheme.colorScheme.error else PrimaryGreen
                            )
                        },
                        trailingIcon = {
                            if (nameError) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        isError = nameError,
                        supportingText = {
                            if (nameError) {
                                Text(
                                    text = "El nombre es obligatorio",
                                    color = MaterialTheme.colorScheme.error,
                                    style = CodiThemeValues.typography.bodySmall
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (nameError) MaterialTheme.colorScheme.error else PrimaryGreen,
                            focusedLabelColor = if (nameError) MaterialTheme.colorScheme.error else PrimaryGreen,
                            errorBorderColor = MaterialTheme.colorScheme.error
                        ),
                        enabled = !viewModel.isUpdating.value
                    )

                    // Apellido
                    OutlinedTextField(
                        value = viewModel.tempLastName.value,
                        onValueChange = {
                            viewModel.updateTempLastName(it)
                            lastNameError = it.isBlank()
                        },
                        label = { Text("Apellido") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = if (lastNameError) MaterialTheme.colorScheme.error else PrimaryGreen
                            )
                        },
                        trailingIcon = {
                            if (lastNameError) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        isError = lastNameError,
                        supportingText = {
                            if (lastNameError) {
                                Text(
                                    text = "El apellido es obligatorio",
                                    color = MaterialTheme.colorScheme.error,
                                    style = CodiThemeValues.typography.bodySmall
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (lastNameError) MaterialTheme.colorScheme.error else PrimaryGreen,
                            focusedLabelColor = if (lastNameError) MaterialTheme.colorScheme.error else PrimaryGreen,
                            errorBorderColor = MaterialTheme.colorScheme.error
                        ),
                        enabled = !viewModel.isUpdating.value
                    )

                    // Información adicional
                    if (!viewModel.isUpdating.value) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Completa ambos campos para continuar",
                                style = CodiThemeValues.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        nameError = viewModel.tempName.value.isBlank()
                        lastNameError = viewModel.tempLastName.value.isBlank()

                        if (!nameError && !lastNameError) {
                            viewModel.updateProfile()
                        }
                    },
                    enabled = !viewModel.isUpdating.value &&
                            viewModel.tempName.value.isNotBlank() &&
                            viewModel.tempLastName.value.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        disabledContainerColor = PrimaryGreen.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (viewModel.isUpdating.value) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        }
                        Text(
                            text = if (viewModel.isUpdating.value) "Guardando..." else "Guardar",
                            style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.closeEditDialog()
                        nameError = false
                        lastNameError = false
                    },
                    enabled = !viewModel.isUpdating.value
                ) {
                    Text(
                        text = "Cancelar",
                        color = if (viewModel.isUpdating.value)
                            Color.Gray.copy(alpha = 0.5f)
                        else
                            CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                        style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

