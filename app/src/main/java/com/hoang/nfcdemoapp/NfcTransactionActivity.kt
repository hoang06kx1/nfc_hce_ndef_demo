package com.hoang.nfcdemoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NfcTransactionActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nfcUid = intent.getStringExtra("NFC_UID")
        setContentView(R.layout.activity_nfc_transactions)
        findViewById<TextView>(R.id.tv_uid).text = "UID: ${nfcUid}"
        val tableLayout = findViewById<TableLayout>(R.id.nfc_tb_layout)
        RetrofitService.nfcNetworkService.getNfcTransaction(nfcUid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    it.getTravelerByCardUIDResult.firstOrNull()?.AccTransactionInfo?.forEachIndexed {index, data ->
                        val rowLayout = layoutInflater.inflate(R.layout.layout_nfc_transaction_row, null)
                        rowLayout.findViewById<TextView>(R.id.tv_no).text = index.toString()
                        rowLayout.findViewById<TextView>(R.id.tv_date).text = data.TransactionDate
                        rowLayout.findViewById<TextView>(R.id.tv_amount).text = data.Amount.toString()
                        rowLayout.findViewById<TextView>(R.id.tv_info).text = data.Content + "/" + data.MerchantName
                        tableLayout.addView(rowLayout)
                    }
                }, {
                    Toast.makeText(this, "Không thể tải thông tin giao dịch của ID: ${nfcUid}.", Toast.LENGTH_LONG).show()
                    it.printStackTrace()
                })
    }
}

data class NfcTransactionData(
    val getTravelerByCardUIDResult: List<GetTravelerByCardUIDResult> = listOf()
)

data class GetTravelerByCardUIDResult(
    val AccTransactionInfo: List<AccTransactionInfo> = listOf(),
    val AccountID: Int = 0,
    val AccountNumber: Any? = Any(),
    val Balance: Int = 0,
    val CardNumber: Any? = Any(),
    val CardType: Int = 0,
    val CardUID: String = "",
    val Description: Any? = Any(),
    val ExpirationDate: Any? = Any(),
    val IssueDate: Any? = Any(),
    val MerchantID: Int = 0,
    val Name: String = "",
    val Status: Int = 0,
    val TravelerID: Int = 0
)

data class AccTransactionInfo(
    val AccountID: Any? = Any(),
    val AccountNumber: Any? = Any(),
    val Amount: Int = 0,
    val Balance: Any? = Any(),
    val CardNumber: Any? = Any(),
    val Code: Any? = Any(),
    val Content: String = "",
    val CreatDate: Any? = Any(),
    val ID: Int = 0,
    val Merchant: Any? = Any(),
    val MerchantID: Int = 0,
    val MerchantName: String = "",
    val Name: Any? = Any(),
    val SumAmount: Any? = Any(),
    val Terminal: Any? = Any(),
    val TerminalID: Int = 0,
    val TransactionDate: String = "",
    val Type: Any? = Any(),
    val UserID: Any? = Any()
)