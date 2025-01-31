package com.sslpinningexample

import android.util.Log
import com.facebook.react.modules.network.OkHttpClientFactory
import com.facebook.react.modules.network.OkHttpClientProvider
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class SSLPinningFactory : OkHttpClientFactory {
  companion object {
	private const val hostname = "jsonplaceholder.typicode.com"
	private val sha256Keys = listOf(
	  "sha256/Cl7dc6nofBuxRWuGgnZc9Fi/VYDPg608JSN91g/wQXA=",
	)
  }

  override fun createNewNetworkModuleClient(): OkHttpClient {
	val certificatePinnerBuilder = CertificatePinner.Builder()
	for (key in sha256Keys) {
	  certificatePinnerBuilder.add(hostname, key)
	}
	val certificatePinner = certificatePinnerBuilder.build()

	// Create a logging interceptor
	val loggingInterceptor = HttpLoggingInterceptor { message ->
	  Log.e("NetworkError", message)
	}.apply {
	  level = HttpLoggingInterceptor.Level.BODY
	}

	val clientBuilder = OkHttpClientProvider.createClientBuilder()
	return clientBuilder
	  .certificatePinner(certificatePinner)
	  .addInterceptor(loggingInterceptor)
	  .build()
  }
}
