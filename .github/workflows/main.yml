name: Build APK

on:
  push:
    branches: [ main, master ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Setup Android SDK
        uses: android-actions/setup-android@v3

      - name: Install Gradle 8.2
        run: |
          wget -q https://services.gradle.org/distributions/gradle-8.2-bin.zip
          unzip -q gradle-8.2-bin.zip -d $HOME/gradle
          echo "$HOME/gradle/gradle-8.2/bin" >> $GITHUB_PATH

      - name: Build Debug APK
        run: gradle assembleDebug --stacktrace
        env:
          ANDROID_HOME: /usr/local/lib/android/sdk

      - name: Upload Debug APK
        uses: actions/upload-artifact@v4
        with:
          name: MyThoughts-debug-apk
          path: app/build/outputs/apk/debug/app-debug.apk
          retention-days: 30
