# Reet
This is an android app that allows users to login and post images and texts. This can be emergencies, accidents or any other thing that one feels like the public should know.

> The app is continuously receiving new features and UI changes. The is the first version of the app.

[Download App in the latest release here](https://github.com/lokified/Reet/releases)

#### The app utilizes firebase features.
* Firebase authentication - this includes login and signup with an email and password.
* Firebase firestore - all the data is stored in the database.
* Firebase Cloud Messaging - App uses push notification from the firebase sdk that can be used for advertisement and passing information about the app.
* Firebase performance - one can monitor the performance of the app where it has traces attached to main functions of the app.
* Firebase crashlytics - one can view crash logs of the app.

## Demo/screenshots
<p align="center">
<img src="https://github.com/lokified/Reet/assets/87479198/0429cbcc-37a0-4663-bc5b-60b4b929e0e3" width=20% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/fb26acbf-f2cf-4ddf-883d-d0454dc4693f" width=20% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/b3d55e9f-bdc9-465a-a54c-0f46f3b7ab48" width=20% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/6d729df0-43a2-4c52-9b8f-5a5ae26f858d" width=20% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/22d9a89e-bf59-4474-bd83-b5e4213a451e" width=20% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/159f07fd-cf80-42b5-8ee5-2d49d5f2e062" width=20% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/d6651c5a-a1c3-440a-89eb-3531324c92de" width=20% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/0335005d-ff32-450f-9142-9268127b4b1b" width=20% height=30% >
</p>


## App Architecture

The app uses multimodule mvvm clean architecture.

The app has a news API.

> The News API is built with **NodeJs**. The news data is scraped from a Kenyan media station (nation.africa).

## Technologies

The app uses these technologies;

- Kotlin - App is built with the language.
- Firebase - for storing data and authentication.
- Jetpack compose - Ui uses compose with material3
- Jetpack components;
  - navigation component - Navigating through different screens in the app.
  - Hilt - For dependency injection.
  - CameraX - a library to capture images and record videos.
  - Datastore - for data persistence in the app.
- Coroutines - Used to make asynchronous calls.
- Coil - for loading images.
- Splash screen Api - For creating a splash screen on app starting.
- Retrofit - make network calls to apis.
- Mockk - Framework for unit testing on different layers in the app.
- Exo Player - A library that offers a layout to play videos.

## Known Bugs

If the app has any bug. Please make contact below or open an issue
> lsheldon645@gmail.com
