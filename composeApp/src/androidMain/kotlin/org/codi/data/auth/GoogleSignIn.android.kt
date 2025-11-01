package org.codi.data.auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import org.codi.app.MainActivity

private const val TAG = "GoogleSignIn"

@Composable
actual fun rememberGoogleSignInLauncher(
    onResult: (GoogleSignInResult) -> Unit
): () -> Unit {
    val context = LocalContext.current

    // Configuración de Google Sign-In
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(MainActivity.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    // Launcher para manejar el resultado del sign-in
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "Result code: ${result.resultCode}")

        when (result.resultCode) {
            Activity.RESULT_OK -> {
                Log.d(TAG, "RESULT_OK recibido")
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account = task.getResult(ApiException::class.java)

                    Log.d(TAG, "Account: ${account?.email}")
                    Log.d(TAG, "IdToken presente: ${account?.idToken != null}")

                    val idToken = account?.idToken

                    if (idToken != null) {
                        Log.d(TAG, "Token obtenido exitosamente")
                        onResult(
                            GoogleSignInResult(
                                success = true,
                                idToken = idToken
                            )
                        )
                    } else {
                        Log.e(TAG, "Token es null")
                        onResult(
                            GoogleSignInResult(
                                success = false,
                                error = "No se obtuvo el token de Google. Verifica que el Web Client ID esté configurado correctamente."
                            )
                        )
                    }
                } catch (e: ApiException) {
                    Log.e(TAG, "ApiException: ${e.statusCode} - ${e.message}", e)
                    onResult(
                        GoogleSignInResult(
                            success = false,
                            error = "Error de Google (${e.statusCode}): ${e.localizedMessage ?: "Error desconocido"}"
                        )
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: ${e.message}", e)
                    onResult(
                        GoogleSignInResult(
                            success = false,
                            error = "Error inesperado: ${e.message}"
                        )
                    )
                }
            }
            Activity.RESULT_CANCELED -> {
                Log.d(TAG, "RESULT_CANCELED - Usuario canceló")
                onResult(
                    GoogleSignInResult(
                        success = false,
                        error = "Inicio de sesión cancelado por el usuario"
                    )
                )
            }
            else -> {
                Log.d(TAG, "Código de resultado desconocido: ${result.resultCode}")
                onResult(
                    GoogleSignInResult(
                        success = false,
                        error = "Resultado inesperado del inicio de sesión (código: ${result.resultCode})"
                    )
                )
            }
        }
    }

    // Retornar la función que inicia el sign-in
    return {
        Log.d(TAG, "Iniciando Google Sign-In")
        Log.d(TAG, "Web Client ID: ${MainActivity.GOOGLE_WEB_CLIENT_ID}")

        // Lanzar directamente sin signOut previo
        // El signOut puede causar que el flujo se cancele
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
}

