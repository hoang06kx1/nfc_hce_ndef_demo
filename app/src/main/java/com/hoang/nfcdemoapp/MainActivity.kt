package com.hoang.nfcdemoapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private var mNfcAdapter: NfcAdapter? = null
    private val sharedPref: SharedPrefManager = SharedPrefManager(this)
    private lateinit var mTurnNfcDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (!sharedPref.getString("NFC_UID","").isBlank()) {
            bt_nfc.visibility = View.INVISIBLE
        }
        bt_nfc.setOnClickListener {
            if (checkNfcEnabled() && packageManager.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)) {
                initNfcService()
                bt_nfc.visibility = View.INVISIBLE
            } else {
                showTurnOnNfcDialog()
            }
        }
        bt_transaction.setOnClickListener {
            val i = Intent(this, NfcTransactionActivity::class.java)
            i.putExtra("NFC_UID", getNfcUid())
            startActivity(i)
        }
    }

    private fun checkNfcEnabled(): Boolean {
        return if (mNfcAdapter == null) {
            false
        } else {
            mNfcAdapter!!.isEnabled
        }
    }

    private fun initNfcService() {
        val intent = Intent(this, KHostApduService::class.java)
        intent.putExtra("ndefMessage", getNfcUid())
        startService(intent)
    }

    private fun getNfcUid(): String {
        return if (sharedPref.getString("NFC_UID", "").isBlank()) {
            var result = ""
            val random = Random()
            for (i in 0 until 14) {
                val nextInt = random.nextInt(16)
                result += Integer.toHexString(nextInt).toUpperCase()
            }
            Log.d("H.NH", "Generated card id:$result")
            sharedPref.putString("NFC_UID", result)
            result
        } else {
            sharedPref.getString("NFC_UID", "")
        }
    }

    private fun showTurnOnNfcDialog() {
        mTurnNfcDialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.ad_nfcTurnOn_title))
                .setMessage(getString(R.string.ad_nfcTurnOn_message))
                .setPositiveButton(
                        getString(R.string.ad_nfcTurnOn_pos)
                ) { _, _ ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(Intent(android.provider.Settings.ACTION_NFC_SETTINGS))
                    } else {
                        startActivity(Intent(android.provider.Settings.ACTION_NFC_SETTINGS))
                    }
                }.setNegativeButton(getString(R.string.ad_nfcTurnOn_neg)) { _, _ ->
                    onBackPressed()
                }
                .create()
        mTurnNfcDialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (!sharedPref.getString("NFC_UID","").isBlank()) {
            Handler().postDelayed({
                initNfcService()
            }, 3000)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        val nfcIntent = Intent(this, KHostApduService::class.java)
        stopService(nfcIntent)
    }
}