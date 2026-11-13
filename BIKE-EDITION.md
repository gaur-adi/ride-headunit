# Ride Headunit implementation

This fork is based on upstream Open Headunit commit `5511654d0d01`. The current app version is `4.0.0-bike.4`.

## Receiver and display

The existing native wireless Android Auto handshake, transport recovery, video decoder and audio implementation are retained. The bike defaults apply once on the first unlocked launch, preserving later user choices. They select native wireless, hardware H.264, SurfaceView, 720p/30 FPS, a dark theme and automatic orientation. Widgets, favorites and perpetual connection shimmer start disabled.

`BikeProfile` and `BikeDefaults` provide Smooth, Sharp and Compatibility presets. The compatibility preset restores TextureView and removes optional low-latency requests. The existing decoder configure ladder handles unsupported codec keys. Changing a preset requires reconnecting the projection session.

The custom home header and palette reuse the upstream connection controls and state reporting. The launcher name, adaptive icon, package ID and signature-protected navigation contract are distinct from the upstream app.

## Rotation

`Settings.effectiveScreenOrientation` reads Android's system Auto-rotate toggle in Auto mode:

- Enabled: request USER orientation.
- Disabled: request LANDSCAPE.
- Explicit app portrait/landscape selections remain fixed choices.

The dashboard/settings and projection observe the system toggle while in the foreground. The old projection's sticky orientation lock is removed. A changed portrait/landscape aspect held for 1.2 seconds uses the existing settings-restart recovery path to negotiate a matching video stream. Matching, reversed same-aspect, square and unknown dimensions do not request an extra session. A turn made while another screen covered projection is also checked on resume.

The system setting itself is never changed by the app. Changing aspect briefly interrupts projection; this is not a seamless video resize.

## Device configuration versus source defaults

The test device was configured separately for a lower 5 GHz Wi-Fi Direct channel, a battery optimization exemption and display-over-other-apps permission. These are device settings, not credentials or hardcoded phone identifiers in this fork. Neither personal preferences nor Wi-Fi/Bluetooth pairing data is stored in the repository.

The stock Android launcher is currently preferred on the test device. Dedicated Home, boot and Bluetooth opening remain optional upstream features. Users can set them in the app and Android settings. Android normally requires the first unlock after a restart.

## Validation

Build and targeted tests:

```sh
./gradlew :app:assembleGithubDebug
./gradlew :app:testGithubDebugUnitTest --tests '*Bike*Test'
```

Nine tests cover persistent profile choices, fallback recovery, phone identity preservation, dashboard layout, rotation edge cases and system toggle behavior. The app was installed and visually checked on a OnePlus 10T with Android 15; native wireless projection, portrait layout and a rotation-triggered reconnect to landscape were observed. The system-toggle-off landscape request was verified on both home and projection activities. The user reported smooth performance after tuning. A controlled A/B benchmark, full reboot validation and long-duration motorcycle testing have not been performed.

## Signing and rollback

The supplied development builds use the developer machine's local Android debug identity. That private signing material is not part of the repository. A fresh CI build will have a different debug identity unless a consistent key is configured; Android will not accept it as an update to an app signed by another key.

To stop using the receiver as Home, select the system launcher in Android's Default apps settings and disable the optional boot/screen-on/Bluetooth opening in the app. The OS has not been replaced.

Final publication validation: the GitHub debug APK assembled successfully, and the full local unit suite passed (4,821 tests, zero failures or errors).
