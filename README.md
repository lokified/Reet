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
<img src="https://github.com/lokified/Reet/assets/87479198/8c7bdb84-7b5e-4734-b626-9f913a2d960e" width=20% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/7c6e6001-0164-4e5e-b9e9-dc0f8fe440458" width=20% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/b50206c2-0bf0-436b-bef4-5c5860848cf6" width=20% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/159f07fd-cf80-42b5-8ee5-2d49d5f2e062" width=20% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/5664e578-d339-43e2-9ae4-9621673b7b23" width=20% height=30% >
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
- Hilt - For dependency injection.
- Jetpack components;
  - navigation component - Navigating through different screens in the app.
- Coroutines - Used to make asynchronous calls.
- Coil - for loading images.
- Splash screen Api - For creating a splash screen on app starting.
- Datastore - for data persistence in the app
- Retrofit - make network calls to apis.
- Mockk - Framework for unit testing on different layers in the app.

## Known Bugs

If the app has any bug. Please make contact below or open an issue
> lsheldon645@gmail.com
