package ru.artsto.centralbank.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ExchangeRateRetrofit @Inject constructor(
    private val retrofitApiInterfaces: RetrofitApiInterfaces
) {
    //запрос данных
    suspend fun getExchange():ExchangeRate{
        return suspendCoroutine {cont->
            val call = retrofitApiInterfaces.getExchangeRate()

            call.enqueue(object : Callback<ExchangeRate> {
                override fun onResponse(
                    call: Call<ExchangeRate>,
                    response: Response<ExchangeRate>
                ) {
                    if (response.isSuccessful) {
                            cont.resume(response.body()!!)
                    }else{
                        //ошибка сервера
                    }
                }
                override fun onFailure(call: Call<ExchangeRate>, t: Throwable) {
                    cont.resumeWithException(t)
                    //Log.i("central", "Error = ${t.message}")
                }
            })
        }
    }
}