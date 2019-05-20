Android Auto for IDrive
=======================

[![Build Status](https://travis-ci.org/hufman/AndroidAutoIdrive.svg?branch=master)](https://travis-ci.org/hufman/AndroidAutoIdrive)
[![Coverage Status](https://coveralls.io/repos/github/hufman/AndroidAutoIdrive/badge.svg?branch=master)](https://coveralls.io/github/hufman/AndroidAutoIdrive?branch=master)

The BMW/Mini IDrive does not offer native Android Auto integration, but does provide a much more powerful Connected Apps connectivity option, which offers many exciting integration points into the car.
This project is an effort to implement most of the features of Android Auto as an unofficial IDrive Connected App.

Overview
--------

As part of the Connected Apps feature, when the phone connects over USB (or Bluetooth in 2017+ models), a tunnel to the car is created to allow other apps on the phone to interact with the car.
A Connected App uses this connection to upload its widget layout to the car, receive event callbacks from user selections, and update widget contents in response.
This remote UI framework effectively creates a custom application in the car, while enabling tight user integration and excellent data availability due to actually running all logic on the phone.

Android Auto for IDrive, combined with the safety benefits of the tactile IDrive controller, allows the user to safely interact with their incoming notifications and control their phone's music, while the phone is tucked out of reach.

Implemented Features
--------------------

  - Phone Notifications
    - Popup about new notifications
  - Google Maps
    - Basic search and routing
    - Not enabled by default, showing Google Maps in a car is against the API EULA
  - Control of Android Auto compatible music apps
    - Supports browsing and searching apps' music libraries
    - Supports selecting from a list of currently-queued songs, as well as basic back/next control
    - Integrates into the car's audio context, for automatic resume and hardware button control
    - Tested working apps:
      - [Black Player](https://play.google.com/store/apps/details?id=com.kodarkooperativet.blackplayerfree)
      - [Dash Player](https://play.google.com/store/apps/details?id=com.dashradio.dash)
      - [DSub](https://play.google.com/store/apps/details?id=github.daneren2005.dsub)
      - [Media Monkey](https://play.google.com/store/apps/details?id=com.ventismedia.android.mediamonkey)
      - [Player FM](https://play.google.com/store/apps/details?id=fm.player)
      - [Plex](https://play.google.com/store/apps/details?id=com.plexapp.android) (only music)
      - [PowerAmp](https://play.google.com/store/apps/details?id=com.maxmpz.audioplayer)
      - [Rocket Player](https://play.google.com/store/apps/details?id=com.jrtstudio.AnotherMusicPlayer)
      - [Scanner Radio](https://play.google.com/store/apps/details?id=com.scannerradio)
      - [SoundCloud](https://play.google.com/store/apps/details?id=com.soundcloud.android)
      - Spotify (up to [version 8.4.96.953](https://www.apkhere.com/down/com.spotify.music_8.4.96.953_free))
      - [Tidal](https://play.google.com/store/apps/details?id=com.aspiro.tidal)
      - [YouTube](https://play.google.com/store/apps/details?id=com.google.android.youtube) (only music previous/next control, no video)

Integration Points
------------------

Besides showing a self-contained remote UI, the IDrive system offers many exciting integration points. Here are a few that this project supports:

  - The Map View, Notification List, and Music Playback, can be assigned to the physical shortcut keys in the dashboard
  - New notifications trigger a popup from anywhere in IDrive, not just in the main list
  - New notification popups can be disabled if a passenger is detected in the seat
  - The currently-playing app is displayed along the top of the IDrive screen
  - The currently-playing song is shown in the Multimedia side panel of the IDrive
  - Audio focus can be optionally enabled, which enables playback resuming when reconnecting on startup, pausing playback when pushing the mute button or during calls, handling physical back/next buttons including seeking support, and using the steering wheel controls to skip back/next from the instrument cluster

Limitations
-----------

Due to the unofficial reverse-engineered nature of this project, it has some limitations:

  - The main menu entries' icons and text can't be altered, and so do not look exactly correct
  - The individual music app icons are not fully functional, but they do switch the active music source
  - Android Oreo disabled Android Open Accessory Protocol 2 audio output, which is required to play audio over USB in IDrive 4++ used by Mini 2014-2017 and BMW i3 2014-2017. Please disable the app option "Request Audio Focus" and use Bluetooth audio
  - Some Android Auto music apps enforce a whitelist of clients, preventing this app from connecting and controlling them. Some tested unavailable apps are:
    - Audible
    - Bandcamp
    - CloudPlayer
    - Deezer
    - doubleTwist
    - Google Play Music
    - iHeartAuto
    - TuneIn Radio
    - Pandora
    - Smart Audiobook Player
    - YouTube Music

Requirements
------------

To communicate to the car, this project relies on the proxy connection that is created by the main Connected app on the phone. Both of the brand-specific Connected and the Connected Classic apps have been tested as compatible for this purpose.

Additionally, the car proposes a security challenge during the connection process, and this project asks the Security Service provided by the Connected apps for the correct response.
The Connected Classic app can be installed to easily provide this response. However, the newer Connected app disabled this ability in version 5.1, which means a workaround needs to be found if the user wants the features of the newer app.
For example, the user can install BMW Connected for the cloud connectivity and better robustness with the Android memory manager, and install Mini Connected Classic to provide the Security Service for this project.

It is not recommended to install both the Connected and Connected Classic apps of the same brand, they will fight over the connection to the car and undefined results may happen. Instead, install the Connected Classic app of the other brand that is not intended to be used regularly.

Build Instructions
------------------

  - (Optional) Add a [Google Maps API key](https://developers.google.com/maps/documentation/android-sdk/signup) to `~/.gradle/gradle.properties` as a property named `AndroidAutoIdrive_GmapsApiKey`
  - Check out the project in Android Studio, then `Build > Make Project`
  - From the commandline, with an Android build environment set up, `./gradlew assemble` should work too

Screenshots
-----------

![Phone App Interface](https://hufman.github.io/AndroidAutoIdrive/screenshot-app.png)

![Music App List](https://hufman.github.io/AndroidAutoIdrive/screenshot-musicapplist.jpg)

![Music Playback Interface](https://hufman.github.io/AndroidAutoIdrive/screenshot-musicplayback.jpg)

![Music Browse](https://hufman.github.io/AndroidAutoIdrive/screenshot-musicbrowse.jpg)