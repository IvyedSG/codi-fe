package org.codi.data.auth

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.AuthenticationServices.ASWebAuthenticationSession
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem
import platform.Foundation.NSCharacterSet
import platform.Foundation.NSURLComponentsURLKey
import platform.Foundation.NSString
import platform.Foundation.NSStringEncoding
import platform.Foundation.NSURLQueryItemName
import platform.darwin.NSObject
import platform.Foundation.NSStringFromClass
import platform.Foundation.NSURLComponentsCompatible
import kotlin.random.Random
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

/**
 * Implementación de Google Sign-In para iOS usando ASWebAuthenticationSession + PKCE.
 *
 * Requisitos (debe configurar en el proyecto iOS):
 * 1) Crear una credencial OAuth 2.0 Client ID de tipo iOS en Google Cloud Console.
 *    - Registrar el bundle identifier de la app.
 * 2) Obtener el iOS Client ID (por ejemplo: 12345-abc.apps.googleusercontent.com) y
 *    asignarlo a la constante `IOS_GOOGLE_CLIENT_ID` más abajo.
 * 3) Configurar el URL scheme en Info.plist: agregar CFBundleURLTypes con CFBundleURLSchemes
 *    que contemple "com.googleusercontent.apps.<IOS_GOOGLE_CLIENT_ID>" (sin espacios).
 *    Ejemplo:
 *    <key>CFBundleURLTypes</key>
 *    <array>
 *      <dict>
 *        <key>CFBundleURLSchemes</key>
 *        <array>
 *          <string>com.googleusercontent.apps.12345-abc.apps.googleusercontent.com</string>
 *        </array>
 *      </dict>
 *    </array>
 *
 * NOTA: Reemplace IOS_GOOGLE_CLIENT_ID por el Client ID de iOS. También puede exponer
 * ese valor desde la capa iOS si lo desea (por ejemplo, leyendo Info.plist desde código nativo).
 */

// --- CONFIGURAR AQUÍ: reemplazar por su iOS Client ID ---
private const val IOS_GOOGLE_CLIENT_ID = "REPLACE_WITH_IOS_CLIENT_ID.apps.googleusercontent.com"
// -------------------------------------------------------

@Serializable
private data class TokenResponse(
    val access_token: String? = null,
    val expires_in: Int? = null,
    val scope: String? = null,
    val token_type: String? = null,
    val id_token: String? = null,
    val refresh_token: String? = null,
    val error: String? = null,
    val error_description: String? = null
)

// Pequeña implementación PKCE utils
private fun createCodeVerifier(length: Int = 64): String {
    val allowed = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~"
    return (1..length).map { allowed[Random.nextInt(allowed.length)] }.joinToString(separator = "")
}

private fun sha256(input: ByteArray): ByteArray {
    // Implementación mínima SHA-256 en Kotlin puro.
    // Basada en un compact port. Suficiente para PKCE code_challenge.
    val h = intArrayOf(
        0x6a09e667, 0xbb67ae85.toInt(), 0x3c6ef372, 0xa54ff53a.toInt(),
        0x510e527f, 0x9b05688c.toInt(), 0x1f83d9ab, 0x5be0cd19.toInt()
    )
    val k = intArrayOf(
        0x428a2f98, 0x71374491, 0xb5c0fbcf.toInt(), 0xe9b5dba5.toInt(), 0x3956c25b,
        0x59f111f1, 0x923f82a4.toInt(), 0xab1c5ed5.toInt(), 0xd807aa98.toInt(), 0x12835b01,
        0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe.toInt(), 0x9bdc06a7.toInt(),
        0xc19bf174.toInt(), 0xe49b69c1.toInt(), 0xefbe4786.toInt(), 0x0fc19dc6, 0x240ca1cc,
        0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da.toInt(), 0x983e5152.toInt(),
        0xa831c66d.toInt(), 0xb00327c8.toInt(), 0xbf597fc7.toInt(), 0xc6e00bf3.toInt(),
        0xd5a79147.toInt(), 0x06ca6351, 0x14292967, 0x27b70a85, 0x2e1b2138,
        0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e.toInt(),
        0x92722c85.toInt(), 0xa2bfe8a1.toInt(), 0xa81a664b.toInt(), 0xc24b8b70.toInt(),
        0xc76c51a3.toInt(), 0xd192e819.toInt(), 0xd6990624.toInt(), 0xf40e3585.toInt(),
        0x106aa070, 0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3,
        0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 0x748f82ee.toInt(), 0x78a5636f, 0x84c87814.toInt(),
        0x8cc70208.toInt(), 0x90befffa.toInt(), 0xa4506ceb.toInt(), 0xbef9a3f7.toInt(),
        0xc67178f2.toInt()
    )

    fun rotr(x: Int, n: Int) = (x ushr n) or (x shl (32 - n))
    fun ch(x: Int, y: Int, z: Int) = (x and y) xor (x.inv() and z)
    fun maj(x: Int, y: Int, z: Int) = (x and y) xor (x and z) xor (y and z)
    fun bigSigma0(x: Int) = rotr(x, 2) xor rotr(x, 13) xor rotr(x, 22)
    fun bigSigma1(x: Int) = rotr(x, 6) xor rotr(x, 11) xor rotr(x, 25)
    fun smallSigma0(x: Int) = rotr(x, 7) xor rotr(x, 18) xor (x ushr 3)
    fun smallSigma1(x: Int) = rotr(x, 17) xor rotr(x, 19) xor (x ushr 10)

    val bytes = input.toMutableList()
    val bitLen = input.size * 8L
    bytes.add(0x80.toByte())
    while ((bytes.size % 64) != 56) bytes.add(0)
    // append 64-bit big-endian length
    for (i in 7 downTo 0) {
        bytes.add(((bitLen ushr (i * 8)) and 0xFF).toByte())
    }

    val w = IntArray(64)
    val chunkCount = bytes.size / 64

    for (iChunk in 0 until chunkCount) {
        val chunk = bytes.subList(iChunk * 64, iChunk * 64 + 64)
        for (i in 0 until 16) {
            val j = i * 4
            w[i] = ((chunk[j].toInt() and 0xFF) shl 24) or
                    ((chunk[j + 1].toInt() and 0xFF) shl 16) or
                    ((chunk[j + 2].toInt() and 0xFF) shl 8) or
                    (chunk[j + 3].toInt() and 0xFF)
        }
        for (i in 16 until 64) {
            w[i] = (smallSigma1(w[i - 2]) + w[i - 7] + smallSigma0(w[i - 15]) + w[i - 16])
        }
        var a = h[0]
        var b = h[1]
        var c = h[2]
        var d = h[3]
        var e = h[4]
        var f = h[5]
        var g = h[6]
        var hh = h[7]
        for (i in 0 until 64) {
            val t1 = hh + bigSigma1(e) + ch(e, f, g) + k[i] + w[i]
            val t2 = bigSigma0(a) + maj(a, b, c)
            hh = g
            g = f
            f = e
            e = d + t1
            d = c
            c = b
            b = a
            a = t1 + t2
        }
        h[0] = h[0] + a
        h[1] = h[1] + b
        h[2] = h[2] + c
        h[3] = h[3] + d
        h[4] = h[4] + e
        h[5] = h[5] + f
        h[6] = h[6] + g
        h[7] = h[7] + hh
    }

    val out = ByteArray(32)
    for (i in 0 until 8) {
        out[i * 4] = ((h[i] ushr 24) and 0xFF).toByte()
        out[i * 4 + 1] = ((h[i] ushr 16) and 0xFF).toByte()
        out[i * 4 + 2] = ((h[i] ushr 8) and 0xFF).toByte()
        out[i * 4 + 3] = (h[i] and 0xFF).toByte()
    }
    return out
}

private fun base64UrlEncode(bytes: ByteArray): String {
    val base64 = kotlin.coroutines.jvm.internal.Base64.getEncoder()?.encodeToString(bytes)
        ?: kotlin.native.concurrent.ThreadLocal.get()?.toString() // fallback (shouldn't happen)
    // replace +/ with -_ and remove padding
    return base64.replace('+', '-').replace('/', '_').trimEnd('=' )
}

// Because the above attempt to use JVM Base64 won't work on native,
// implement a small Base64URL encoder for our byte arrays.
private fun base64UrlEncodeNative(bytes: ByteArray): String {
    val table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    val sb = StringBuilder()
    var i = 0
    while (i < bytes.size) {
        val b0 = bytes[i].toInt() and 0xFF
        val b1 = if (i + 1 < bytes.size) bytes[i + 1].toInt() and 0xFF else 0
        val b2 = if (i + 2 < bytes.size) bytes[i + 2].toInt() and 0xFF else 0
        val out0 = b0 shr 2
        val out1 = ((b0 and 0x3) shl 4) or (b1 shr 4)
        val out2 = ((b1 and 0xF) shl 2) or (b2 shr 6)
        val out3 = b2 and 0x3F
        sb.append(table[out0])
        sb.append(table[out1])
        if (i + 1 < bytes.size) sb.append(table[out2]) else sb.append('=')
        if (i + 2 < bytes.size) sb.append(table[out3]) else sb.append('=')
        i += 3
    }
    // convert to URL-safe
    return sb.toString().replace('+', '-').replace('/', '_').trimEnd('=')
}

@Composable
actual fun rememberGoogleSignInLauncher(
    onResult: (GoogleSignInResult) -> Unit
): () -> Unit {
    return {
        // Lanzar en background coroutine
        CoroutineScope(Dispatchers.Main).launch {
            // Verificar que el developer haya configurado el client id
            if (IOS_GOOGLE_CLIENT_ID.startsWith("REPLACE_WITH")) {
                onResult(GoogleSignInResult(success = false, error = "Configure IOS_GOOGLE_CLIENT_ID en GoogleSignIn.ios.kt"))
                return@launch
            }

            val clientId = IOS_GOOGLE_CLIENT_ID
            val redirectScheme = "com.googleusercontent.apps.$clientId"
            val redirectUri = "$redirectScheme:/oauth2redirect"

            val codeVerifier = createCodeVerifier()
            val hashed = sha256(codeVerifier.encodeToByteArray())
            val codeChallenge = base64UrlEncodeNative(hashed)

            val authUrl = NSURL("https://accounts.google.com/o/oauth2/v2/auth" +
                    "?client_id=${clientId.encodeURLParameter()}" +
                    "&redirect_uri=${redirectUri.encodeURLParameter()}" +
                    "&response_type=code" +
                    "&scope=${"openid email profile".encodeURLParameter()}" +
                    "&code_challenge=${codeChallenge.encodeURLParameter()}" +
                    "&code_challenge_method=S256" +
                    "&prompt=select_account")

            val session = ASWebAuthenticationSession(authUrl, redirectScheme) { callbackUrl, error ->
                if (callbackUrl != null) {
                    // Extraer code de la query
                    val urlString = callbackUrl.absoluteString ?: ""
                    val components = NSURLComponents("$urlString")
                    val items = components.queryItems
                    var code: String? = null
                    if (items != null) {
                        for (i in 0 until items.size) {
                            val it = items[i] as NSURLQueryItem
                            if (it.name == "code") {
                                code = it.value
                                break
                            }
                        }
                    }
                    if (code != null) {
                        // Intercambiar el code por tokens
                        CoroutineScope(Dispatchers.Default).launch {
                            try {
                                val client = HttpClient(Darwin)
                                val response: HttpResponse = client.post("https://oauth2.googleapis.com/token") {
                                    contentType(ContentType.Application.FormUrlEncoded)
                                    setBody("grant_type=authorization_code" +
                                            "&code=${code.encodeURLParameter()}" +
                                            "&redirect_uri=${redirectUri.encodeURLParameter()}" +
                                            "&client_id=${clientId.encodeURLParameter()}" +
                                            "&code_verifier=${codeVerifier.encodeURLParameter()}")
                                }
                                val text = response.bodyAsText()
                                val tokenResp = Json.decodeFromString<TokenResponse>(text)
                                if (tokenResp.id_token != null) {
                                    withContext(Dispatchers.Main) {
                                        onResult(GoogleSignInResult(success = true, idToken = tokenResp.id_token))
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        onResult(GoogleSignInResult(success = false, error = tokenResp.error_description ?: tokenResp.error ?: "No id_token returned"))
                                    }
                                }
                            } catch (t: Throwable) {
                                withContext(Dispatchers.Main) {
                                    onResult(GoogleSignInResult(success = false, error = t.message ?: "Error intercambio token"))
                                }
                            }
                        }
                    } else {
                        onResult(GoogleSignInResult(success = false, error = "No se recibió código de autorización"))
                    }
                } else {
                    onResult(GoogleSignInResult(success = false, error = error?.localizedDescription ?: "Autenticación cancelada"))
                }
            }
            // start the session
            session.start()
        }
    }
}

// --- Helpers ---
private fun String.encodeURLParameter(): String =
    NSURL.Companion.URLWithString("http://example.com/?q=${this}")?.absoluteString?.substringAfter("?q=") ?: this

// Extension to construct NSURL more easily
private fun NSURL(string: String): NSURL = NSURL.URLWithString(string)

// Kotlin/Native safe way to access queryItems: helper constructor
private class NSURLComponents(private val urlString: String) {
    val queryItems: List<Any>? by lazy {
        val nsurl = NSURL.URLWithString(urlString)
        val comps = platform.Foundation.NSURLComponents.componentsWithURL(nsurl, null)
        comps?.queryItems
    }
}
