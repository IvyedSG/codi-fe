// Kotlin
package org.codi.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import codi.composeapp.generated.resources.Res
import codi.composeapp.generated.resources.logo
import org.codi.theme.CodiThemeValues

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val buttonEnabled = !isLoading && email.isNotEmpty() && password.isNotEmpty()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CodiThemeValues.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "Logo CODI",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "\"",
                style = CodiThemeValues.typography.displaySmall,
                color = CodiThemeValues.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Cada recibo deja una huella.",
                style = CodiThemeValues.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                color = CodiThemeValues.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Hazla verde.",
                style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        "correo@gmail.com",
                        color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = CodiThemeValues.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = CodiThemeValues.colorScheme.surface,
                    focusedContainerColor = CodiThemeValues.colorScheme.surface,
                    unfocusedBorderColor = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.2f),
                    focusedBorderColor = CodiThemeValues.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text(
                        "contraseña",
                        color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = CodiThemeValues.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = CodiThemeValues.colorScheme.surface,
                    focusedContainerColor = CodiThemeValues.colorScheme.surface,
                    unfocusedBorderColor = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.2f),
                    focusedBorderColor = CodiThemeValues.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    isLoading = true
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        onLoginSuccess()
                    }
                    isLoading = false
                },
                enabled = buttonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CodiThemeValues.colorScheme.tertiary,
                    contentColor = CodiThemeValues.colorScheme.onTertiary,
                    disabledContainerColor = CodiThemeValues.colorScheme.tertiary.copy(alpha = 0.6f),
                    disabledContentColor = CodiThemeValues.colorScheme.onTertiary.copy(alpha = 0.6f)
                ),
                shape = CodiThemeValues.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = CodiThemeValues.colorScheme.onTertiary,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Ingresar",
                        style = CodiThemeValues.typography.labelLarge,
                        color = CodiThemeValues.colorScheme.onTertiary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Todavía no tienes una cuenta? ",
                    style = CodiThemeValues.typography.bodySmall,
                    color = CodiThemeValues.colorScheme.onBackground
                )
                TextButton(
                    onClick = { /* navegar a registro */ },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Crear Cuenta",
                        style = CodiThemeValues.typography.labelMedium,
                        color = CodiThemeValues.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}