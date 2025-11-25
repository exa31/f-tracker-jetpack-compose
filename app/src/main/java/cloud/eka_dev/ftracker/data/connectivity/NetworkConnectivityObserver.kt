package cloud.eka_dev.ftracker.data.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkConnectivityObserver(
    context: Context
) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {

        // 🔥 INITIAL STATE — penting biar saat pertama buka langsung dapat info
        val currentStatus = getCurrentStatus()
        trySend(currentStatus)

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(ConnectivityObserver.Status.Available)
            }

            override fun onLost(network: Network) {
                trySend(ConnectivityObserver.Status.Lost)
            }

            override fun onUnavailable() {
                trySend(ConnectivityObserver.Status.Unavailable)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    private fun getCurrentStatus(): ConnectivityObserver.Status {
        val network =
            connectivityManager.activeNetwork ?: return ConnectivityObserver.Status.Unavailable
        val capabilities = connectivityManager.getNetworkCapabilities(network)
            ?: return ConnectivityObserver.Status.Unavailable

        return if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            ConnectivityObserver.Status.Available
        } else ConnectivityObserver.Status.Unavailable
    }
}
