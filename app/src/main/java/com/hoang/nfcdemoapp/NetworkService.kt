package com.hoang.nfcdemoapp

import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object RetrofitService {
    val NFC_URL = "http://demo.tueminh.tech:8085/DaNangWS.svc/"
    private val okhttp = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).callTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build()
    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
    private val nfcRetrofit = Retrofit.Builder().baseUrl(NFC_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build()

    val nfcNetworkService = nfcRetrofit.create(NfcNetworkService::class.java)
}

interface NfcNetworkService {
    @GET("GetTravelerByCardUID")
    fun getNfcTransaction(@Query("cardUID") cardUid: String): Single<NfcTransactionData>
}