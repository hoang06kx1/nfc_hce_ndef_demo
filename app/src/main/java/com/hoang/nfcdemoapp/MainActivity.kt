package com.hoang.nfcdemoapp

import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
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
        bt_nfc.setOnClickListener {
            if (checkNfcEnabled() && packageManager.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)) {
                initNfcService()
            } else {
                showTurnOnNfcDialog()
            }
        }
        bt_transaction.setOnClickListener {

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
        Log.d("H.NH", "Card UID:" + getCardUid())
        intent.putExtra("ndefMessage", getCardUid())
        startService(intent)
    }

    private fun getCardUid(): String {
        return if (sharedPref.getString("NFC_UID", "").isBlank()) {
            var result = ""
            val random = Random()
            for (i in 0..14) {
                val nextInt = random.nextInt(16)
                result = result + Integer.toHexString(nextInt).toUpperCase()
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

}