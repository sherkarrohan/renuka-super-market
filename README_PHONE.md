# Renuka Super Market — Build the APK from your phone

This project is prepared for an Android APK build using GitHub Actions, so you do NOT need a laptop.

1. On your Android phone, create/sign in to a GitHub account.
2. Create a new repository, e.g. `renuka-super-market`.
3. Upload all files/folders from this project into the repository.
4. Open the repository's **Actions** tab.
5. Select **Build Renuka Super Market APK**.
6. Tap **Run workflow**.
7. Wait for the build to finish.
8. Open the completed workflow and download the artifact named `RenukaSuperMarket-debug-apk`.
9. Extract the artifact and install `app-debug.apk` on the shop phone.

The app database is local SQLite storage. The GitHub workflow is only used to compile the APK; the installed app does not use GitHub or a cloud database.

For a production release, add encrypted local backup/export, barcode camera scanning, GST invoice/thermal printer support, returns, customer/supplier ledgers, weighted-average costing, and a PIN/biometric lock.
