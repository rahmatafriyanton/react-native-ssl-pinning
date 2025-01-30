package com.sslpinningexample

import android.util.Log
import com.facebook.react.modules.network.OkHttpClientFactory
import com.facebook.react.modules.network.OkHttpClientProvider
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class SSLPinningFactory : OkHttpClientFactory {
  companion object {
	private const val hostname = "dummyjson.com"
	private val sha256Keys = listOf(
	  "sha256/RBYNVi5e63XJHyeE6aRR2vcHNc92JOP+CRGgw/8nAi0=",
	  "sha256/bdrBhpj38ffhxpubzkINl0rG+UyossdhcBYj+Zx2fcc="
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
