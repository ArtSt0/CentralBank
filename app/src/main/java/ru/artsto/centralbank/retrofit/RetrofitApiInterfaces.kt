package ru.artsto.centralbank.retrofit

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import java.io.Serializable

interface RetrofitApiInterfaces {

    //https://www.cbr-xml-daily.ru/daily_json.js
    @GET("/daily_json.js")
    fun getExchangeRate(): Call<ExchangeRate>
}

//валютный курс
data class ExchangeRate(
    @SerializedName("Date")
    val date:String?="",
    @SerializedName("PreviousDate")
    val previousDate:String?="",
    @SerializedName("PreviousURL")
    val previousURL:String="",
    @SerializedName("Timestamp")
    val timestamp: String="",
    @SerializedName("Valute")
    val currency:MutableMap<String, Currency>?=null,
):Serializable

//единица валюты
data class Currency(
    @SerializedName("ID")
    val id:String,
    @SerializedName("NumCode")
    val numCode:String,
    @SerializedName("CharCode")
    val charCode:String,
    @SerializedName("Nominal")
    val nominal:Double,
    @SerializedName("Name")
    val name:String,
    @SerializedName("Value")
    val value:Double,
    @SerializedName("Previous")
    val previous:Double,
):Serializable