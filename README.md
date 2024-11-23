# This module requires LSposed

# LSPosed Plugin: Manage Your Android ID

With the evolution of Android, privacy and user tracking restrictions have tightened significantly. Since **Android 10 (API level 29)**, developers face increased challenges when attempting to track users through unique device identifiers. This plugin aims to simplify the management of Android IDs while respecting user control and privacy.

---

## Understanding Unique Identifiers in Android

### Key Identifiers in Android
To identify users or devices, Android relies primarily on two unique identifiers:
- **Android ID**
- **Advertising ID**

### Changes Introduced in Android 10+
Android 10 introduced significant restrictions on access to device identifiers:

1. **Non-Resettable Identifiers**  
   Access to identifiers like IMEI, serial numbers, or device MAC addresses (e.g., Wi-Fi or Bluetooth) is no longer available to standard apps. Only the following apps can access these identifiers:
   - Device or profile owner apps.
   - Apps with special carrier permissions.
   - Apps with the `READ_PRIVILEGED_PHONE_STATE` permission

2. **Randomization and Privacy by Design**  
   Android 10+ ensures that developers cannot access Wi-Fi or Bluetooth MAC addresses through traditional APIs.

None of the `Non-Resettable Identifiers` are available for normal user apps from the PlayStore! As such, there is no need to "fake" these identifiers anymore.

### Android ID: A Closer Look
The **Android ID** is a unique identifier that combines:
- A base key generated when the device first boots.
- A unique key per developer (tied to the app’s signing key).

**How it works:**
- Each app installed on the device sees a **different Android ID**, even if they are running on the same device.
- If you **reinstall an app**, the Android ID remains the same, as the base key does not change.
- If you **factory reset the device**, the base key is regenerated, and all apps will see a completely new Android ID.

These behaviors make the Android ID device-specific but app-specific as well, enhancing privacy.

---

## LSPosed Plugin: Enhanced Control Over Your Android ID

The LSPosed plugin allows you to reset your **Android ID** without the need for a full factory reset. This provides a convenient, runtime method for managing your Android ID without wiping your device data.

### Key Features
- **Runtime Reset**: Reset your Android ID directly within the plugin interface.
- **Factory Reset Alternative**: Avoid the hassle of completely resetting your device to generate a new Android ID.
- **Developer-Oriented Control**: Enables developers and Android experts to test apps with varying Android IDs in different scenarios.

### Why Use This Plugin?
1. **Testing and Debugging**: Simulate different Android IDs to test user-specific or device-specific behavior in your app.
2. **Privacy Management**: Gain control over identifiers shared with apps to enhance personal privacy.
3. **No Device Wipe**: Save time by avoiding a full factory reset whenever you need a new Android ID.

---

## How to Use the LSPosed Plugin

1. **Install LSPosed Framework**  
   Ensure the LSPosed framework is installed and configured on your device. Refer to the LSPosed documentation for setup instructions.

2. **Install the Plugin**  
   Download and install the LSPosed plugin APK.

3. **Reset Android ID**  
   - Open the LSPosed plugin.
   - Navigate to the Android ID reset feature.
   - Follow the prompts to generate a new Android ID at runtime.

4. **Test the Changes**  
   Verify the new Android ID using a debugging tool or your app's log to ensure the reset was successful.

---

## Important Notes
- **No Play Store Apps**: Apps downloaded from the Google Play Store do not have access to restricted identifiers like the IMEI or serial number. This ensures a baseline level of privacy and security.
- **Designed for Developers and Experts**: While this tool provides advanced capabilities, it is intended for Android developers and pow
