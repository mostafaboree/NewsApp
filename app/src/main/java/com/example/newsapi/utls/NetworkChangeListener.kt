package com.contacts.getphonecontacts.broadcastReceiver.networkState

interface NetworkChangeListener {
    fun onNetworkChanged(isConnected: Boolean)
}