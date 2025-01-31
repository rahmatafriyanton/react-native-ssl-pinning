# SSL Pinning in React Native (Android & iOS)

This guide provides step-by-step instructions for implementing SSL Pinning in a React Native project using native code for both Android (Kotlin) and iOS (Swift).

## Prerequisites

- React Native installed
- Xcode (for iOS)
- Android Studio (for Android)
- CocoaPods installed (for iOS dependencies)

---

## Android (Using OkHttp & CertificatePinner)

### Dependencies

Add the required dependencies to your `android/app/build.gradle`:

```gradle
dependencies {
    implementation 'com.squareup.okhttp3:okhttp:4.9.3'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.3'
}
```

### Implementation

Create a custom `OkHttpClientFactory` class in `android/app/src/main/java/com/yourapp/SSLPinningFactory.kt`:

```kotlin
package com.yourapp

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
            "sha256/Cl7dc6nofBuxRWuGgnZc9Fi/VYDPg608JSN91g/wQXA=" // Replace with your actual pin
        )
    }

    override fun createNewNetworkModuleClient(): OkHttpClient {
        val certificatePinner = CertificatePinner.Builder().apply {
            sha256Keys.forEach { add(hostname, it) }
        }.build()

        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.e("Network", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClientProvider.createClientBuilder()
            .certificatePinner(certificatePinner)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}
```

### Register Custom SSL Pinning Factory

Modify `MainApplication.java` to register the custom SSL Pinning factory:

```java
@Override
public void onCreate() {
    super.onCreate();
    OkHttpClientProvider.setOkHttpClientFactory(new SSLPinningFactory());
}
```

---

## iOS (Using TrustKit in Swift)

### Dependencies

Install TrustKit via CocoaPods by adding this to your `ios/Podfile`:

```ruby
pod 'TrustKit'
```

Run:

```sh
cd ios && pod install
```

### Implementation

Modify `AppDelegate.swift` to configure SSL Pinning:

```swift
import TrustKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {

        let trustKitConfig: [String: Any] = [
            kTSKSwizzleNetworkDelegates: true,
            kTSKPinnedDomains: [
                "jsonplaceholder.typicode.com": [
                    kTSKIncludeSubdomains: true,
                    kTSKEnforcePinning: true,
                    kTSKDisableDefaultReportUri: true,
                    kTSKPublicKeyHashes: [
                        "Cl7dc6nofBuxRWuGgnZc9Fi/VYDPg608JSN91g/wQXA=" // Replace with actual pin
                    ]
                ]
            ]
        ]

        TrustKit.initSharedInstance(withConfiguration: trustKitConfig)

        return true
    }
}
```

### Verifying SSL Pinning

To test SSL Pinning, use a proxy tool like Charles Proxy or mitmproxy and attempt to intercept requests. If SSL Pinning is correctly configured, requests will fail when a proxy is enabled.

---

## Troubleshooting

### Common Issues & Fixes

- **App crashes on iOS with TrustKit:** Ensure you have at least **two different** pinned keys (primary and backup).
- **SSL Pinning still allows intercepted requests:** Make sure the certificate hashes match the actual server certificates. Use `openssl` to verify:
  ```sh
  openssl s_client -connect jsonplaceholder.typicode.com:443 -servername jsonplaceholder.typicode.com | openssl x509 -noout -pubkey | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | base64
  ```
- **Pod install issues:** Run `pod repo update && pod install` to refresh dependencies.

---

## Conclusion

This guide sets up SSL Pinning in React Native using native implementations for both Android (OkHttp) and iOS (TrustKit in Swift). Make sure to test SSL Pinning to confirm it is working correctly before releasing your app.

---

### References

- [OkHttp SSL Pinning](https://square.github.io/okhttp/features/certificate_pinning/)
- [TrustKit Documentation](https://github.com/datatheorem/TrustKit)
