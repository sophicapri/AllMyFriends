#### Technical test

All My Friends
=================

An app that displays a list of people and shows their profile.
Available in offline mode, and in landscape mode.

<img src="https://github.com/sophicapri/AllMyFriends/blob/dev/screenshots/app_tour.gif" align="right" width="40%">

Technical Stack
--------------
  * MVVM Architecture
  * [AndroidX][1] 
  * [Android KTX][2] 
  * [Navigation][14] 
  * [View Binding][11]
  * [Kotlin Flow][13]
  * [Paging 3][7]
  * [Retrofit][5] - REST client.
  * [Room][16] - For caching remote data. In this projet I did not use [RemoteMediator][77] to cache data, breaking the Single Source of Truth rule. 
    Once I'll manage to finally make it work or find an alternative, I will use that instead in the futur. 
  * [Hilt][92] - For dependency injection
  * [Glide][32] - To display remote images.
  * [Kotlin Coroutines][91] - For managing background threads.
  * [Moshi][9] - JSON library to parse data.
  * [MockK][20] - Mocking library.
  * [LeakCanary][33] - A memory leak detection library.

[1]: https://developer.android.com/jetpack/androidx
[2]: https://developer.android.com/kotlin/ktx
[13]: https://developer.android.com/kotlin/flow
[11]: https://developer.android.com/topic/libraries/view-binding
[14]: https://developer.android.com/topic/libraries/architecture/navigation/
[16]: https://developer.android.com/topic/libraries/architecture/room
[17]: https://developer.android.com/topic/libraries/architecture/viewmodel
[30]: https://developer.android.com/guide/topics/ui
[34]: https://developer.android.com/guide/components/fragments
[91]: https://kotlinlang.org/docs/reference/coroutines-overview.html
[92]: https://developer.android.com/training/dependency-injection/hilt-android
[5]: https://github.com/square/retrofit
[7]: https://developer.android.com/topic/libraries/architecture/paging/v3-overview
[8]: https://developer.android.com/topic/libraries/architecture/datastore
[9]: https://github.com/square/moshi
[10]: https://github.com/google/gson
[20]: https://github.com/mockk/mockk
[21]: https://github.com/airbnb/lottie-android
[33]: https://square.github.io/leakcanary/
[77]: https://developer.android.com/reference/androidx/paging/RemoteMediator
[32]: https://github.com/bumptech/glide
