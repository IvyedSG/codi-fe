package org.codi.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import codi.composeapp.generated.resources.Res
import codi.composeapp.generated.resources.logo
import org.codi.theme.CodiThemeValues
import org.codi.ui.HomeTabNavigator
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.data.api.ApiClient
import org.codi.data.api.ApiException
import io.ktor.http.HttpStatusCode
import org.codi.data.api.models.RegisterRequest
import org.codi.data.api.models.DniResponse
import org.codi.data.storage.TokenStorage
import org.codi.ui.ToastManager
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.window.Dialog

object RegisterScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var nombre by remember { mutableStateOf("") }
        var apellido by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        // Estado para dialogo DNI (movido arriba para ser accesible desde la lambda de registro)
        var showDniDialog by remember { mutableStateOf(false) }
        var dniValue by remember { mutableStateOf("") }
        var dniLoading by remember { mutableStateOf(false) }
        var dniResult by remember { mutableStateOf<DniResponse?>(null) }

        val buttonEnabled = !isLoading &&
                           nombre.isNotBlank() &&
                           apellido.isNotBlank() &&
                           email.isNotBlank() &&
                           password.isNotBlank()

        Scaffold(
            // Usar SnackbarHost personalizado para asegurar contraste y visibilidad del texto
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    // Fondo oscuro y texto blanco para visibilidad
                    Snackbar(
                        containerColor = Color(0xFF333333),
                        contentColor = Color.White,
                        action = {
                            data.visuals.actionLabel?.let { label ->
                                TextButton(onClick = { /* no-op */ }) { Text(label, color = Color.White) }
                            }
                        }
                    ) {
                        Text(text = data.visuals.message)
                    }
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.systemBars), // Respetar las barras del sistema
                color = CodiThemeValues.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    Image(
                        painter = painterResource(Res.drawable.logo),
                        contentDescription = "Logo CODI",
                        modifier = Modifier.size(160.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Crear Cuenta",
                        style = CodiThemeValues.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = CodiThemeValues.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Campo de Nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        placeholder = {
                            Text(
                                "Nombre",
                                color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Nombre",
                                tint = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
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

                    // Campo de Apellido
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { apellido = it },
                        placeholder = {
                            Text(
                                "Apellido",
                                color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Apellido",
                                tint = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        },
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

                    // Campo de Email
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

                    // Campo de Contraseña
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

                    // Botón de Registrarse
                    Button(
                        onClick = {
                            // Ejecutar llamada al endpoint en coroutine
                            scope.launch {
                                isLoading = true
                                try {
                                    val req = RegisterRequest(
                                        nombre = nombre,
                                        apellido = apellido,
                                        email = email,
                                        password = password
                                    )
                                    val resp = ApiClient.router.register(req)

                                    if (resp.success && resp.data != null) {
                                        // Guardar token seguro y reemplazar la pantalla actual
                                        resp.data.token.takeIf { it.isNotBlank() }?.let { token ->
                                            try {
                                                TokenStorage.saveToken(token)
                                                // Guardar también el userId
                                                resp.data.user.id.takeIf { it.isNotBlank() }?.let { userId ->
                                                    TokenStorage.saveUserId(userId)
                                                }
                                            } catch (_: Throwable) {
                                                // No bloquear navegación por fallo al guardar
                                            }
                                        }

                                        // En vez de navegar inmediatamente, mostramos el diálogo para asociar DNI
                                        showDniDialog = true
                                    } else {
                                        val msg = resp.message.takeIf { it.isNotBlank() }
                                            ?: resp.error.takeIf { it?.isNotBlank() == true }
                                            ?: "Error en el registro"

                                        ToastManager.show(msg)
                                    }
                                } catch (ae: ApiException) {
                                    // Log raw body for debugging
                                    try {
                                        println("ApiException body=${ae.body}")
                                    } catch (_: Throwable) {}

                                    // Manejo fino según el status
                                    val bodyText = ae.body?.takeIf { it.isNotBlank() }
                                    val friendly = when (ae.status) {
                                        HttpStatusCode.BadRequest -> bodyText ?: "Datos inválidos. Revisa los campos."
                                        HttpStatusCode.Conflict -> bodyText ?: "El correo ya está registrado."
                                        else -> {
                                            if (ae.status.value >= 500) "Error interno del servidor. Intenta más tarde." else (bodyText ?: "Error en el registro (${ae.status.value})")
                                        }
                                    }
                                    // Asegurar mensaje no vacío
                                    val messageToShow = friendly.takeIf { it.isNotBlank() } ?: "Error desconocido"
                                    ToastManager.show(messageToShow)
                                } catch (t: Throwable) {
                                    // Error de red / parseo
                                    val fallback = t.message?.takeIf { it.isNotBlank() } ?: "Error en la conexión"
                                    ToastManager.show(fallback)
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
                                text = "Registrarse",
                                style = CodiThemeValues.typography.labelLarge,
                                color = CodiThemeValues.colorScheme.onTertiary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Enlace para ir a login
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿Ya tienes una cuenta? ",
                            style = CodiThemeValues.typography.bodySmall,
                            color = CodiThemeValues.colorScheme.onBackground
                        )
                        TextButton(
                            onClick = { navigator.pop() },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Iniciar Sesión",
                                style = CodiThemeValues.typography.labelMedium,
                                color = CodiThemeValues.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        // --- DIALOGO para DNI ---
        if (showDniDialog) {
            // Dialog personalizado para adaptarlo al theme
            Dialog(onDismissRequest = { showDniDialog = false }) {
                Surface(
                    shape = CodiThemeValues.shapes.medium,
                    color = CodiThemeValues.colorScheme.surface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)) {

                        // Header con icono
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = CodiThemeValues.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Asociar DNI",
                                style = CodiThemeValues.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = CodiThemeValues.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Ingresa tu DNI para confirmar tu identidad. Al completar 8 dígitos se buscará automáticamente.",
                            style = CodiThemeValues.typography.bodyMedium,
                            color = CodiThemeValues.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = dniValue,
                            onValueChange = { v ->
                                val clean = v.filter { it.isDigit() }.take(8)
                                dniValue = clean

                                if (clean.length == 8) {
                                    dniLoading = true
                                    dniResult = null
                                    scope.launch {
                                        try {
                                            val resp = ApiClient.router.getUserByDni(clean)
                                            dniResult = resp
                                        } catch (t: Throwable) {
                                            ToastManager.show(t.message ?: "Error al buscar DNI")
                                        } finally {
                                            dniLoading = false
                                        }
                                    }
                                } else {
                                    dniResult = null
                                }
                            },
                            placeholder = { Text("Ej: 98765432", color = CodiThemeValues.colorScheme.onSurfaceVariant) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CodiThemeValues.colorScheme.primary,
                                unfocusedBorderColor = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.12f),
                                focusedLabelColor = CodiThemeValues.colorScheme.primary,
                                cursorColor = CodiThemeValues.colorScheme.primary,
                                focusedLeadingIconColor = CodiThemeValues.colorScheme.primary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (dniLoading) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = CodiThemeValues.colorScheme.primary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Buscando...", color = CodiThemeValues.colorScheme.onSurface)
                            }
                        }

                        dniResult?.let { res ->
                            Spacer(modifier = Modifier.height(12.dp))

                            if (res.success && res.data != null) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = CodiThemeValues.colorScheme.secondaryContainer),
                                    shape = CodiThemeValues.shapes.small,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        // Forzar color negro para asegurar legibilidad sobre el container
                                        Text(text = "Nombre: ${res.data.nombre} ${res.data.apellido}", style = CodiThemeValues.typography.bodyLarge, color = Color.Black)
                                        Text(text = "DNI: ${res.data.dni}", style = CodiThemeValues.typography.bodyMedium, color = Color.Black)
                                    }
                                }
                            } else {
                                // Mensaje de error/estado del backend
                                Text(
                                    text = res.message,
                                    style = CodiThemeValues.typography.bodyMedium,
                                    color = CodiThemeValues.colorScheme.error
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { showDniDialog = false }) {
                                Text("Cancelar", color = CodiThemeValues.colorScheme.onSurfaceVariant)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            val confirmEnabled = dniResult?.success == true && dniResult?.data != null && !dniLoading

                            Button(
                                onClick = {
                                    // Confirmar y navegar si hay resultado válido
                                    showDniDialog = false
                                    navigator.replace(HomeTabNavigator)
                                },
                                enabled = confirmEnabled,
                                colors = ButtonDefaults.buttonColors(containerColor = CodiThemeValues.colorScheme.primary)
                            ) {
                                Text("Confirmar", color = CodiThemeValues.colorScheme.onPrimary)
                            }
                        }

                    }
                }
            }
        }
    }
}
