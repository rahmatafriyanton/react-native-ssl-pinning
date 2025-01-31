import UIKit
import React
import React_RCTAppDelegate
import ReactAppDependencyProvider
import TrustKit

@main
class AppDelegate: RCTAppDelegate {
  override func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    self.moduleName = "SSLPinningExample"
    self.dependencyProvider = RCTAppDependencyProvider()

    // You can add your custom initial props in the dictionary below.
    // They will be passed down to the ViewController used by React Native.
    self.initialProps = [:]
    
    let trustKitConfig: [String: Any] = [
            kTSKSwizzleNetworkDelegates: true,
            kTSKPinnedDomains: [
                "jsonplaceholder.typicode.com": [
                    kTSKEnforcePinning: true,
                    kTSKIncludeSubdomains: true,
                    kTSKPublicKeyAlgorithms: [kTSKAlgorithmRsa2048],
                    kTSKPublicKeyHashes: [
                      "Cl7dc6nofBuxRWuGgnZc9Fi/VYDPg608JSN91g/wQXA=",
                      "kIdp6NNEd8wsugYyyIYFsi1ylMCED3hZbSR8ZFsa/A4="
                    ],
                    kTSKReportUris: ["https://your-report-endpoint.com"]
                ]
            ]
        ]

        TrustKit.initSharedInstance(withConfiguration: trustKitConfig)

    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }

  override func sourceURL(for bridge: RCTBridge) -> URL? {
    self.bundleURL()
  }

  override func bundleURL() -> URL? {
#if DEBUG
    RCTBundleURLProvider.sharedSettings().jsBundleURL(forBundleRoot: "index")
#else
    Bundle.main.url(forResource: "main", withExtension: "jsbundle")
#endif
  }
}
