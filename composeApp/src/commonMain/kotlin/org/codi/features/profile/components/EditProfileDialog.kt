package org.codi.features.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.features.profile.ProfileViewModel
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen

@Composable
fun EditProfileDialog(viewModel: ProfileViewModel) {
    if (viewModel.showEditDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.closeEditDialog() },
            title = {
                Text(
                    text = "Editar Perfil",
                    style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = viewModel.tempName.value,
                        onValueChange = { viewModel.updateTempName(it) },
                        label = { Text("Nombre") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = PrimaryGreen
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            focusedLabelColor = PrimaryGreen
                        )
                    )

                    OutlinedTextField(
                        value = viewModel.tempLastName.value,
                        onValueChange = { viewModel.updateTempLastName(it) },
                        label = { Text("Apellido") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = PrimaryGreen
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            focusedLabelColor = PrimaryGreen
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.updateProfile() },
                    enabled = !viewModel.isUpdating.value &&
                            viewModel.tempName.value.isNotBlank() &&
                            viewModel.tempLastName.value.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        disabledContainerColor = PrimaryGreen.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (viewModel.isUpdating.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (viewModel.isUpdating.value) "Guardando..." else "Guardar",
                        style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.closeEditDialog() },
                    enabled = !viewModel.isUpdating.value
                ) {
                    Text(
                        text = "Cancelar",
                        color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                        style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

