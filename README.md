# SamYoga

AI-powered Yoga Pose Detection and Correction App for Android  
Built with Jetpack Compose & TensorFlow Lite (Custom trained model included)

[![Made with Kotlin](https://img.shields.io/badge/Made%20with-Kotlin-orange?style=flat-square&logo=kotlin)](https://kotlinlang.org/)  [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-%F0%9F%A7%96-blue?style=flat-square&logo=android)](https://developer.android.com/jetpack/compose)

## Overview

**SamYoga** is an intelligent Yoga assistant app that helps users perfect their yoga poses in real time.  
It uses AI-based pose detection through the phone’s front camera, providing corrections and guidance — much like a personal yoga instructor!

## Features

- AI-based Yoga Pose Detection
- Real-time posture correction and feedback
- Audio feedback with suggested improvements
- Pose descriptions and instructions
- Track your progress over time
- Offline support (runs on-device with TensorFlow Lite)
- Inspired by Kemtai, built for mobile users

## Tech Stack

- **Jetpack Compose:** modern Android UI toolkit
- **Kotlin**: language of choice
- **TensorFlow Lite**: on-device pose estimation
- **CameraX**: front-camera integration
- **MediaPlayer**: text-to-speech for audio feedback
- **MVVM clean architecture**
- **Hilt**: for Dependency Injection
- **Coil**: for Image parsing

## Snapshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/3974a52b-36c7-4d81-b11b-58b41ff1410a" alt="Home Screen" width="30%" height="570px" />
  <img src="https://github.com/user-attachments/assets/8e82ae33-ca83-4748-b287-17acf52d2ca6" alt="Session Screen" width="30%" height="570px" />
  <img src="https://github.com/user-attachments/assets/870d276f-a553-4e3f-a184-808fe9cc3936" alt="Explore Screen" width="30%" height="570px" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/7377f1b0-6765-41c1-8b78-1e9b6a3141c7" alt="Profile Screen" width="30%" height="570px" />
  <img src="https://github.com/user-attachments/assets/b1ffc7a2-6b01-41a4-be14-2362e10b88e0" alt="Sign Up Screen" width="30%" height="570px" />
  <img src="https://github.com/user-attachments/assets/8d39c7db-e1a6-437c-a8c1-2407b12e66ef" alt="Login Screen" width="30%" height="570px" />
</p>

## Getting Started

1. Download or Clone project

```bash
git clone https://github.com/prasidhanchan/SamYoga.git
cd SamYoga
```

2. Add Secrets
   Paste your Gemini API key and Supabase storage url in local.properties file
```
apiKey=YOUR_GEMINI_API_KEY
storageUrl=SUPABASE_STORAGE_URL
```
```
Ex:
apiKey=AIzaSy...
storageUrl=https://samyoga.supabase.co/storage/v1/object/public/images/
```

3. Sync Gradle and Run on device or emulator

 ---

⭐ **If you like this project or find it useful, please give it a star! It helps to support my work and encourages me to create more.** 😊