# Reet
This is an android app to report emergencies, accidents or any other thing that one feels like the public should know.

> The app is continuously receiving new features and UI changes. The is the first version of the app.

[Download App](https://drive.google.com/file/d/1BS9H3fnDHzI3K6ONkbuO0BoUGM08nPSr/view?usp=sharing)

#### The app utilizes firebase features.
* Firebase authentication - this includes login and signup with an email.
* Firebase firestore - all the data is stored in the database.
* Firebase Cloud Messaging - App uses push notification from the firebase sdk that can be used for advertisement and passing information about the app
* Firebase perfomance - one can monitor the perfomance of the app where it has traces attached to main functions of the app
* Firebase crashlytics - one can view crash logs of the app
  
## Demo/screenshots
<p align="center">
<img src="https://github.com/lokified/Reet/assets/87479198/62fb6c54-6a0b-4ad1-8100-0b518426f275" width=30% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/3db3a3f4-e05d-4211-a3af-eafb609dc944" width=30% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/8cd0f7ba-8502-47a2-9ad8-b01d65ebb795" width=30% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/92062344-c8f1-48d3-971f-41f8ce9af438" width=30% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/d21e1394-f1c8-4a48-acaa-f0d26da23156" width=30% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/0baacc80-0b01-4e67-9b3d-f92edcfb29d2" width=30% height=30% >
<img src="https://github.com/lokified/Reet/assets/87479198/4c6b522d-c92f-4261-978e-ccadf5bb8c5a" width=30% height=30% >
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

## Known Bugs

If the app has any bug. Please make contact below or open an issue
> lsheldon645@gmail.com

## Contacts

Have anything to contribute to the app, send an email to:

> lsheldon645@gmail.com
