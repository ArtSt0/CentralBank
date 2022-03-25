package ru.artsto.centralbank.di.modules

import ru.artsto.centralbank.retrofit.RetrofitApiInterfaces
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {

    private val host = "192.168.42.23"
    private val port = "60300"

    @Provides
    @Singleton
    fun providesRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient,
        //rxJava3CallAdapterFactory: RxJava3CallAdapterFactory
    ): Retrofit {

        return Retrofit.Builder()
            //.baseUrl("https://${host}:${port}/")
            .baseUrl("https://cbr-xml-daily.ru")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            //.addCallAdapterFactory(rxJava3CallAdapterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        /*
        val mCertificateFactory = CertificateFactory.getInstance("X.509")
        val cert = "-----BEGIN CERTIFICATE-----\n" +
                "MIIEAzCCAuugAwIBAgIJAPaStOfwcKdxMA0GCSqGSIb3DQEBCwUAMIGWMQswCQYD\n" +
                "VQQGEwJSVTESMBAGA1UECAwJS1JBU05PREFSMRIwEAYDVQQHDAlLUkFTTk9EQVIx\n" +
                "DzANBgNVBAoMBkFSVFNUTzEPMA0GA1UECwwGQVJUU1RPMRYwFAYDVQQDDA04MC44\n" +
                "Ny4yMDEuMjI5MSUwIwYJKoZIhvcNAQkBFhZzdG9yb3poZWZmYXZAZ21haWwuY29t\n" +
                "MCAXDTIxMDIyNzAwMzAwMVoYDzIxMjEwMjAzMDAzMDAxWjCBljELMAkGA1UEBhMC\n" +
                "UlUxEjAQBgNVBAgMCUtSQVNOT0RBUjESMBAGA1UEBwwJS1JBU05PREFSMQ8wDQYD\n" +
                "VQQKDAZBUlRTVE8xDzANBgNVBAsMBkFSVFNUTzEWMBQGA1UEAwwNODAuODcuMjAx\n" +
                "LjIyOTElMCMGCSqGSIb3DQEJARYWc3Rvcm96aGVmZmF2QGdtYWlsLmNvbTCCASIw\n" +
                "DQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMh05K/A7Ad7LYpq3M0+2LYGkOEQ\n" +
                "47LOqUgX+uPyrPC6mCfBTj+GHG5QxRBps4Lpa96juZdEGt92SihWLblHFZCrdFw0\n" +
                "NWI9cN+l7Rzk/CnK/MOu8KFadDMXPHahjfCU3g2yYlpMV5BLkgblvndVgE6V+Clm\n" +
                "H3GOJHAeZJ52cVmKh2mIaTyYvle0poGp1J7WkMuP6Z5vZ1mL/UskXAEs6SfS8eW6\n" +
                "zJuIv7zyBmhVWwAQll6SL3kR1HdLjjjsQZi5lIFttQ7DzXsgrf+JCYxTrAEt7NM8\n" +
                "V9zU0+aoMR6Rmt9UnDfecsAQA/XpGKwQtheLX/AoGWDRPJuSieBMjgvbaL0CAwEA\n" +
                "AaNQME4wHQYDVR0OBBYEFDMDDbGXzo18DHlU7kJ/WD3QWRGTMB8GA1UdIwQYMBaA\n" +
                "FDMDDbGXzo18DHlU7kJ/WD3QWRGTMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEL\n" +
                "BQADggEBABLExsRXN5nMFI5qExkCiYXRrnBYO74fimf1K7XcdEMqEiIeB3RjvSoX\n" +
                "MmXnjgxaOxghOtWzz4LfDMNVwbi7YcQ5r5x/UuZ8ifQR60Z+rE7AEiefA5g0/9is\n" +
                "cDIz3uOuqDmA995PgSvChrCVoCwE4CTxY1HC0EzpzDXvTgp/U3cx3owlXyaL6YIw\n" +
                "qI5f8NQc2ug+RLOWsq+nTOnf8FFO9ILeoteTWjxcHG4Jfje6rtF+54BxWczq4yr2\n" +
                "XrLnD/u4mD285FFPTCTEpXJhnY5msz1qESV3Vp5Rjz5y58f6pFiiLGRbNppqFUGy\n" +
                "HLgnhqY2xD63knk1V1Y5BbcAcDDcBBI=\n" +
                "-----END CERTIFICATE-----"

        //AppCore.resourses?.assets?.open("acert_core.pem")

        val mInputStream = cert.byteInputStream()
        val mCertificate : Certificate = mCertificateFactory.generateCertificate(mInputStream)
        mInputStream.close()

        val mKeyStoreType = KeyStore.getDefaultType()
        val mKeyStore = KeyStore.getInstance(mKeyStoreType)
        mKeyStore.load(null, null)
        mKeyStore.setCertificateEntry("ca", mCertificate)

        val mTmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val mTrustManagerFactory = TrustManagerFactory.getInstance(mTmfAlgorithm)
        mTrustManagerFactory.init(mKeyStore)

        val mTrustManagers = mTrustManagerFactory.trustManagers

        val mSslContext = SSLContext.getInstance("SSL")
        mSslContext.init(null, mTrustManagers, null)
        val mSslSocketFactory = mSslContext.socketFactory

        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2) //handshake
            .allEnabledCipherSuites()
            .build()
        */

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor) //далее закрыть
            //.connectionSpecs(Collections.singletonList(spec))
            .connectTimeout(10, TimeUnit.SECONDS) // connect timeout
            .readTimeout(10, TimeUnit.SECONDS) // read timeout
            .writeTimeout(10, TimeUnit.SECONDS)

        //okHttpClient.sslSocketFactory(mSslSocketFactory, mTrustManagers[0] as X509TrustManager)
        //okHttpClient.hostnameVerifier { _, _ -> true }
        return okHttpClient.build()
    }


    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit): RetrofitApiInterfaces {
        return retrofit.create(RetrofitApiInterfaces::class.java)
    }
    /*

    @Provides
    @Singleton
    fun providesRxJava3CallAdapterFactory(): RxJava3CallAdapterFactory {
        return RxJava3CallAdapterFactory.create()
    }
    */
}
