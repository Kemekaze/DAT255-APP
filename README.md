# Group: Super Friends

## APK
https://github.com/Kemekaze/DAT255-APP/tree/dev/APK
## Dokumentationen
https://github.com/Kemekaze/DAT255-APP/wiki/Dokumentation
## Post Mortem Report
https://github.com/Kemekaze/DAT255-APP/wiki/Post-mortem-report

***

## Getting Started
```bash
git clone git://github.com/Kemekaze/DAT255-APP.git
```
## Dependencies
* Nodejs
* MongoDB
* Android SDK
* Android device or use Android emulator

## SDK targets
* Target SDK: 21
* Minimun SDK: 21

## Installation
### Server side 
Install Mongodb and Nodejs
Run mongodb (mongod.exe)
Open any shell and cd to Server directory off where you cloned git.

Run:
```bash
  npm install
```
Start the server:
```bash
  node server.js
```
(in order for bus specificoperations to work you need to add some buses )
### Client side 
Run the app Busster in Klient folder
#### optional
Open the webgui at http://localhost:3000 in a webbrowser to edit and see posts, buses etc


## Massor med UIS
* https://github.com/wasabeef/awesome-android-ui
