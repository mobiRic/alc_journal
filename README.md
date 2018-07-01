Dayli - An Every Day Journal for your Pocket
============================================

Dayli is a journal app for Android, created for the ALC3.0 #7DaysofCodeChallenge.

Dayli has the following features:
- Register and Sign In with Google Authentication
- View all thoughts in a list, sorted most recent first
- Add new thoughts quickly and easily
- View, edit, or delete previously captured thoughts
- Displays user name, email, and profile picture
- View Open Source Licence credits
- Fabric Crashlytics automatic crash reporting

Dayli has the following system requirements in order to run:
-  Android 4.1 Jellybean (API 16)
-  Google Play Services

In addition, a valid Google account and internet connection are required to sign in and use the app.


Getting Started
---------------

To build a copy of Dayli on your own system, clone this repo and import the project into Android Studio.

### Prerequisites

Dayli requires the following:
- Android Studio v3+
- Firebase project to be configured for Google Authentication

### Keystore Configuration

This Dayli repo already contains a debug keystore and signing configuration. Release signing requires further steps.

Create `extras/key/keystore_release.properties` with the following properties:
```gradle
storeFile=../extras/key/release.jks
storePassword=...
keyPassword=...
keyAlias=...
```

For security reasons, it is recommended to use the following filenames, which have been added to the `.gitignore` configuration:
- `/extras/key/keystore_release.properties`
- `/extras/key/release.jks`

### Firebase Configuration

Firebase project needs to be configured for Google Authentication. The downloaded `google-services.json` configuration file must be placed in `/app/google-services.json`.

This file has also been added to the `.gitignore` and will not be added to the repo by default.

### Fabric Configuration

Fabric Crashlytics project configuration should be stored in:
- `/app/fabric.properties`

Fabric Beta distribution list and release notes are stored in:
- `/extras/fabric/distribution.txt`
- `/extras/fabric/release_notes.txt`

**Note:** All Fabric configuration files are stored in the repository by default.

Screenshots
-----------

![Sign In](/extras/screenshots/Sign%20In.png)
![Home](/extras/screenshots/Home.png)
![Navigation Drawer](/extras/screenshots/Navigation%20Drawer.png)
![Read Thought](/extras/screenshots/Read%20Thought.png)
![Add Thought](/extras/screenshots/Add%20Thought.png)
![About and Open Source Licences](/extras/screenshots/About%20Dayli.png)

Download
--------

Dayli can be downloaded and installed from this Github repo:
- [Install Dayli](/extras/app-release.apk)

Author
------

- **Richard Le Mesurier** - [mobiRic](https://github.com/mobiRic)

License
=======

    Copyright 2018 Glowworm Software

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.









