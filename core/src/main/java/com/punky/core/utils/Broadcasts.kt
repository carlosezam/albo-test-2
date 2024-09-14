package com.punky.core.utils

import android.content.Context
import android.content.Intent

/**
 * Crea un nombre de acción utilizando el nombre del paquete de la aplicación y el
 * nombre de la acción proporcionado.
 *
 * El nombre de la acción se convierte a mayúsculas y se concatena con el nombre
 * del paquete utilizando un punto como separador.
 *
 *   Ejemplo de uso:
 *   ```
 *   val action = createAction("CUSTOM_ACTION")
 *   val broadcastIntent = context.nonExportedBroadcastIntent(action)
 *   context.sendBroadcast(broadcastIntent)
 *   ```
 *
 *   o
 *
 *   ```
 *   val action = createAction("CUSTOM_ACTION")
 *   val filter = IntentFilter(action)
 *   ContextCompat.registerReceiver(context, receiver, filter, flags)
 *   ```
 * @param actionName El nombre de la acción.
 * @return El nombre de la acción completo, incluyendo el nombre del paquete.
 * @receiver Context utilizado para obtener el nombre del paquete de la aplicación.
 */
fun Context.createAction(actionName: String): String {
    return "${packageName}.${actionName.uppercase()}"
}


/**
 * Crea un Intent para broadcasts no exportados.
 *
 * A partir de Android 13 [android.os.Build.VERSION_CODES.TIRAMISU], es necesario
 * incluir el paquete de la aplicación para que los receivers no exportados
 * puedan recibir el broadcast.
 *
 *   Ejemplo de uso:
 *   ```
 *   val action = createAction("CUSTOM_ACTION")
 *   val broadcastIntent = context.nonExportedBroadcastIntent(action)
 *   context.sendBroadcast(broadcastIntent)
 *   ```
 * @param action La acción del Intent.
 * @return Un nuevo Intent para broadcasts no exportados, con el paquete de la
 *         aplicación establecido para compatibilidad con Android 13 y versiones
 *         posteriores.
 */
fun Context.nonExportedBroadcastIntent(action: String): Intent {
    return Intent(action).setPackage(packageName)
}