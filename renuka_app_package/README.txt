RENUKA SUPER MARKET - Billing & Inventory App
================================================

WHAT'S IN THIS ZIP
- index.html  -> the entire app (HTML + CSS + JavaScript in one file).
                 This is the only file you need.

HOW TO USE ON YOUR PHONE
1. Copy index.html onto your Android phone (email it to yourself,
   send via WhatsApp "Document", or copy over USB cable).
2. Open it with Chrome (tap the file -> Open with -> Chrome).
3. Once it opens, tap Chrome's menu (three dots, top right)
   -> "Add to Home screen". This creates an app icon.
4. From then on, open it from that icon like a normal app.
   It works fully offline - no internet connection needed.

HOW IT STORES DATA
- All products and bills are saved in the phone's browser storage
  (IndexedDB) - nothing is uploaded anywhere, no login, no cloud.
- IMPORTANT: go to the "Backup" tab in the app and tap
  "Export backup (.json)" regularly (e.g. every evening). This
  downloads a file with all your data, in case the phone's storage
  ever gets cleared.

TURNING THIS INTO A REAL INSTALLABLE .APK (optional)
This zip is a web app, not a compiled .apk. If you want an actual
.apk file, you have three options:
1. PWABuilder (pwabuilder.com) - free, no-code, upload index.html
   (hosted online) and it generates a signed .apk for you.
2. Median.co - paid, easiest, wraps any website into an .apk.
3. Android Studio - build a WebView wrapper app yourself (free,
   needs a computer, most control). Ask for this project's Kotlin
   source code if you want to go this route.

EDITING THE APP
Everything is in index.html - open it in any text editor. The
<style> section controls appearance, the <script> section
controls behavior. It's plain HTML/CSS/JavaScript, no build step,
no dependencies, no internet required to edit or run it.
