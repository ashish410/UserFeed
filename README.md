# UserFeed
This app has a simple feed example and also supports a details screen for the same. <br>
It is built using Koltin language. <br>
MVVM is used as the architecture of this app. <br> 
The network calls were done using the Retrofit library. <br>
The network response is handled on the IO thread. This is possible by the Kotlin Coroutines. <br>
Also, I have used Hilt as the dependency injection making it easier and efficient to access the dependencies at compile time.<br> <br> 
Package Description:
 |- data: Contains all the required data classes <br>
 |- di: Contains the dependency injection classes<br>
 |- network: Contains the classes responsible for handling network connectivity. Also contains the network request classes and interfaces<br>
 |- ui: All the Activity and Fragment are present here <br>
 |- util: Some helper classes to ease the development<br>
 |- Constants.kt: All app-related constants are present here<br>
 |- MainApp.kt: The application class of app. Required to initialise hilt library<br>
 
The app demonstrate the api available here https://jsonplaceholder.typicode.com/posts
