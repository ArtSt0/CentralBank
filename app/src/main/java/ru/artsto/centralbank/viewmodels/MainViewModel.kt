package ru.artsto.centralbank.viewmodels

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.artsto.centralbank.retrofit.Currency
import ru.artsto.centralbank.retrofit.ExchangeRate
import ru.artsto.centralbank.retrofit.ExchangeRateRetrofit
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val exchangeRateRetrofit: ExchangeRateRetrofit
):ViewModel() {

    //список валют
    var exchangeRate =  mutableStateOf(ExchangeRate())
    val textTimer = mutableStateOf("")
    private var countDownTimer: CountDownTimer? = null

    val kol1 = mutableStateOf("1.0")
    val kol2 = mutableStateOf("1.0")
    val key1 = mutableStateOf("RUB")
    val key2 = mutableStateOf("USD")

    init {
        getExchangeRate()
    }

    //поля1 валют
    fun onDropDownMenu1(){ kol2.value = convertCurrent(true) }
    fun onValueChange1(){ kol2.value = convertCurrent(true) }
    //поля2 валют
    fun onDropDownMenu2(){ kol2.value = convertCurrent(true) }
    fun onValueChange2(){ kol1.value = convertCurrent(false) }

    //расчет валют
    private fun convertCurrent(
        curFlag: Boolean //true - изменить значение1 false - изменить значение2
    ): String {

        val cur1 = exchangeRate.value.currency?.get(key1.value)
        val cur2 = exchangeRate.value.currency?.get(key2.value)
        var res = 0.0

        val c1 = cur1?.nominal
        val c2 = cur2?.nominal
        var x1 = 0.0
        var x2 = 0.0

        if (c1!=null && c2!=null){
            x1 = cur1.value /c1
            x2 = cur2.value /c2
        }

        if (curFlag){
            if (kol1.value!="") {
                val k1 = kol1.value.toDouble()
                res = (k1 * x1)/x2
            }

        }else{
            if (kol2.value!="") {
                val k1 = kol2.value.toDouble()
                res = (k1 * x2)/x1
            }
        }

        val ds = DecimalFormatSymbols(Locale.getDefault())
        ds.decimalSeparator = '.'
        return DecimalFormat("#.######", ds).format(res)
    }

    //запуск таймера
    private fun startTimer(){
            countDownTimer=object :CountDownTimer(30000, 1000){
                override fun onTick(p0: Long) {
                    textTimer.value = "${p0/1000}"
                }
                override fun onFinish() {
                    onClickUpdate()
                }
            }.start()
    }

    //прерывание таймера
    private fun cancelTimer(){
        countDownTimer?.cancel()
    }

    //прервать таймер, обновить данные
    fun onClickUpdate(){
        cancelTimer()
        getExchangeRate()
    }

    //получить валютный курс
    private fun getExchangeRate(){
        viewModelScope.launch {

            val res = withContext(Dispatchers.IO){
                exchangeRateRetrofit.getExchange()
            }
            res.let{
                exchangeRate.value = it
                insertRUB()
                startTimer()
                onValueChange1()
            }
        }
    }

    //добавление РУБЛЯ
    private fun insertRUB(){
        val currency = Currency(
            id = "1",
            numCode = "023",
            charCode = "RUB",
            nominal = 1.0,
            name = "Российский рубль",
            value = 1.0,
            previous = 1.0
        )
        exchangeRate.value.currency?.put("RUB", currency)
    }
}