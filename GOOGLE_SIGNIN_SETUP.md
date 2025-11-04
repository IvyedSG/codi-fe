# Configuración de Google Sign-In para Android

## ✅ CONFIGURACIÓN AUTOMÁTICA

**¡Todo está listo!** El proyecto ya está configurado para que Google Sign-In funcione automáticamente.

### ✨ Qué está configurado:
- ✅ Keystore compartido incluido en el proyecto (`debug.keystore`)
- ✅ SHA-1 ya registrado en Firebase/Google Cloud Console
- ✅ Gradle configurado para usar automáticamente el keystore correcto
- ✅ Funciona en cualquier dispositivo (emulador o físico) sin configuración adicional

### 🚀 Para empezar a desarrollar:

1. **Clonar el proyecto**
2. **Hacer build** (debug o release)
3. **¡Listo!** Google Sign-In funcionará automáticamente

```bash
# Build debug
./gradlew :composeApp:assembleDebug

# Build release
./gradlew :composeApp:assembleRelease

# Instalar en dispositivo
adb install composeApp/build/outputs/apk/debug/composeApp-debug.apk
```

## 🔑 Detalles Técnicos

### Keystore Compartido:
- **Archivo**: `debug.keystore` (en la raíz del proyecto, incluido en git)
- **Alias**: `androiddebugkey`
- **Password Store**: `android`
- **Password Key**: `android`
- **SHA-1**: `EB:49:51:24:33:65:05:4D:CB:23:A7:EA:26:AE:40:7B:CC:D4:37:AF`
- **SHA-256**: `BD:F9:E6:D9:37:39:31:0E:2A:8E:B0:6D:EF:05:81:F6:95:67:C0:9F:82:30:07:1B:67:CE:E1:17:5A:E4:FC:2D`

### Configuración en Firebase/Google Cloud Console:
- **Package name**: `org.codi.app`
- **SHA-1**: `EB:49:51:24:33:65:05:4D:CB:23:A7:EA:26:AE:40:7B:CC:D4:37:AF` ✅ (ya registrado)
- **Web Client ID**: `275267069227-vlak55jq1fht1t7cuc90mad9j591bs7f.apps.googleusercontent.com`

## 🔒 Seguridad

**Nota sobre el keystore compartido:**
- El `debug.keystore` está incluido en el repositorio para facilitar el desarrollo en equipo
- Este keystore solo debe usarse para **desarrollo y testing**
- Para producción (Google Play), se debe usar un keystore de release privado y seguro
- El keystore de release NO está incluido en el repositorio por seguridad

## 🐛 Troubleshooting

### Si Google Sign-In no funciona:

1. **Verifica que estés usando el build correcto**
   ```bash
   # Ver SHA-1 de un APK instalado
   keytool -printcert -jarfile composeApp/build/outputs/apk/debug/composeApp-debug.apk
   ```
   Debe mostrar: `EB:49:51:24:33:65:05:4D:CB:23:A7:EA:26:AE:40:7B:CC:D4:37:AF`

2. **Verifica la conexión al backend**
   - Emulador: usa `http://10.0.2.2:8000`
   - Dispositivo físico: asegúrate de que el backend sea accesible

3. **Revisa los logs de Logcat**
   ```bash
   adb logcat | grep -i "GoogleSignIn\|ApiException"
   ```

### Ver SHA-1 del keystore del proyecto
```bash
keytool -list -v -keystore debug.keystore -alias androiddebugkey -storepass android
```

## 📚 Referencias

- [Google Sign-In Android](https://developers.google.com/identity/sign-in/android/start)
- [Firebase Authentication](https://firebase.google.com/docs/auth/android/google-signin)

