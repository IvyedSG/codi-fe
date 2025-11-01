// Kotlin
package org.codi.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import codi.composeapp.generated.resources.Res
import codi.composeapp.generated.resources.logo
import org.codi.theme.CodiThemeValues
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.data.api.ApiClient
import org.codi.data.api.ApiException
import io.ktor.http.HttpStatusCode
import org.codi.data.api.models.LoginRequest
import org.codi.data.storage.TokenStorage
import org.codi.ui.ToastManager
import org.codi.data.auth.rememberGoogleSignInLauncher
import org.codi.data.api.models.GoogleSignInRequest

object LoginScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()

        // Configurar el launcher de Google Sign-In
        val launchGoogleSignIn = rememberGoogleSignInLauncher { result ->
            scope.launch {
                isLoading = true
                try {
                    if (result.success && result.idToken != null) {
                        // Enviar el idToken al backend
                        val googleReq = GoogleSignInRequest(idToken = result.idToken)
                        val resp = ApiClient.router.googleSignIn(googleReq)

                        if (resp.success && resp.data != null) {
                            resp.data.token.takeIf { it.isNotBlank() }?.let { token ->
                                try {
                                    TokenStorage.saveToken(token)
                                } catch (_: Throwable) {}
                            }
                            navigator.replace(HomeTabNavigator)
                        } else {
                            val msg = resp.message.takeIf { it.isNotBlank() } ?: resp.error ?: "Error al iniciar sesión con Google"
                            ToastManager.show(msg)
                        }
                    } else {
                        ToastManager.show(result.error ?: "Error al iniciar sesión con Google")
                    }
                } catch (ae: ApiException) {
                    val bodyText = ae.body?.takeIf { it.isNotBlank() }
                    val friendly = when (ae.status) {
                        HttpStatusCode.BadRequest -> bodyText ?: "Token de Google inválido"
                        HttpStatusCode.Unauthorized -> bodyText ?: "No autorizado"
                        else -> if (ae.status.value >= 500) "Error interno" else (bodyText ?: "Error al iniciar sesión")
                    }
                    ToastManager.show(friendly)
                } catch (t: Throwable) {
                    ToastManager.show(t.message ?: "Error en la conexión")
                } finally {
                    isLoading = false
                }
            }
        }

        val buttonEnabled = !isLoading && email.isNotEmpty() && password.isNotEmpty()

        Scaffold {
            innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars),
                color = CodiThemeValues.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
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
                            scope.launch {
                                isLoading = true
                                try {
                                    val req = LoginRequest(email = email, password = password)
                                    val resp = ApiClient.router.login(req)
                                    if (resp.success && resp.data != null) {
                                        resp.data.token.takeIf { it.isNotBlank() }?.let { token ->
                                            try {
                                                TokenStorage.saveToken(token)
                                            } catch (_: Throwable) {}
                                        }
                                        navigator.replace(HomeTabNavigator)
                                    } else {
                                        val msg = resp.message.takeIf { it.isNotBlank() } ?: resp.error ?: "Error al iniciar sesión"
                                        ToastManager.show(msg)
                                    }
                                } catch (ae: ApiException) {
                                    val bodyText = ae.body?.takeIf { it.isNotBlank() }
                                    val friendly = when (ae.status) {
                                        HttpStatusCode.BadRequest -> bodyText ?: "Datos inválidos"
                                        HttpStatusCode.Unauthorized -> bodyText ?: "Credenciales inválidas"
                                        else -> if (ae.status.value >= 500) "Error interno" else (bodyText ?: "Error al iniciar sesión")
                                    }
                                    ToastManager.show(friendly)
                                } catch (t: Throwable) {
                                    ToastManager.show(t.message ?: "Error en la conexión")
                                } finally {
                                    isLoading = false
                                }
                            }
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Divisor "o"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.2f)
                        )
                        Text(
                            text = " o ",
                            style = CodiThemeValues.typography.bodySmall,
                            color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.2f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de Google Sign-In
                    OutlinedButton(
                        onClick = {
                            if (!isLoading) {
                                launchGoogleSignIn()
                            }
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = CodiThemeValues.colorScheme.surface,
                            contentColor = CodiThemeValues.colorScheme.onSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.3f)
                        ),
                        shape = CodiThemeValues.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Icono de Google (usando una aproximación con texto)
                            Text(
                                text = "G",
                                style = CodiThemeValues.typography.headlineSmall,
                                color = CodiThemeValues.colorScheme.primary,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Text(
                                text = "Continuar con Google",
                                style = CodiThemeValues.typography.labelLarge
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
                            onClick = { navigator.push(RegisterScreen) },
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
    }
}