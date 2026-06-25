# My Thoughts — Android App 📖

Personal blog app by **Vivek Shah** — wrapping [blog.ervivekshah.com.np](https://blog.ervivekshah.com.np) into a native Android experience.

[![Build APK](https://github.com/YOUR_USERNAME/MyThoughtsApp/actions/workflows/build-apk.yml/badge.svg)](https://github.com/YOUR_USERNAME/MyThoughtsApp/actions/workflows/build-apk.yml)

---

## ✨ Features

- 🌐 Full WebView of your blog with smooth loading
- 🔄 Pull-to-refresh
- 📶 Offline detection with Retry button
- ↩️ Back navigation within the blog
- 🔗 External links open in browser
- 📤 Share current post
- ⭐ In-app rating prompt (after 5 page reads)
- 🎨 Beautiful splash screen with animation
- 🏠 Home / Share / Open in Browser / About menu

---

## 🚀 Get APK via GitHub Actions (No Setup Needed!)

### Step 1 — Upload this project to GitHub

1. Go to [github.com](https://github.com) and sign in (or create a free account)
2. Click **"New repository"** (green button top right)
3. Name it `MyThoughtsApp`, set it **Public**, click **Create repository**
4. On your computer, extract this ZIP
5. Open terminal/command prompt in the extracted folder and run:

```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/MyThoughtsApp.git
git push -u origin main
```

> Replace `YOUR_USERNAME` with your actual GitHub username.

### Step 2 — Wait for the build (~5 minutes)

- Go to your repository on GitHub
- Click the **"Actions"** tab
- You'll see **"Build APK"** workflow running
- Wait for the green ✅ checkmark

### Step 3 — Download your APK

**Option A — From Actions (Debug APK):**
1. Click on the completed workflow run
2. Scroll down to **"Artifacts"**
3. Click **"MyThoughts-debug-apk"** to download

**Option B — From Releases (auto-created on each push to main):**
1. Click **"Releases"** on the right sidebar
2. Download `app-debug.apk` from the latest release

### Step 4 — Install on your phone

1. Transfer the APK to your Android phone
2. Open the APK file
3. If prompted, go to **Settings → Install unknown apps → Allow**
4. Install and enjoy! 🎉

---

## 🛠️ Build Locally (Optional)

If you have Android Studio installed:

```bash
# Debug APK
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk

# Release APK (unsigned)
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

---

## 🎨 Customization

| File | What to change |
|------|----------------|
| `app/src/main/java/.../MainActivity.kt` line ~22 | `BLOG_URL` — change the blog URL |
| `app/src/main/res/values/colors.xml` | App brand colors |
| `app/src/main/res/values/strings.xml` | App name |
| `app/src/main/res/drawable/ic_logo.xml` | App logo (vector) |
| `app/src/main/res/mipmap-*/` | Launcher icons (PNG) |

---

## 📦 Tech Stack

- **Language:** Kotlin
- **Min SDK:** Android 5.0 (API 21) — covers 99%+ of devices
- **Target SDK:** Android 14 (API 34)
- **UI:** Material Design 3
- **Build:** Gradle 8.2 + AGP 8.2.0

---

## 📄 License

Personal project by Vivek Shah. All rights reserved.
