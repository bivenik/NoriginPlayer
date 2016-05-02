# NoriginPlayer

Demo app for Norigin Media which plays one of 3 given movies.

##Project structure##

In folder apks you can find apk for each variant of application (sintel, elephants, bunny).
Library folder contains exoPlayer source code.

##Frameworks##

1) Playing video - exoPlayer. It support DRM from android version 4.3. http://google.github.io/ExoPlayer/guide.html <br>
2) Parsing json - LoganSquare. https://github.com/bluelinelabs/LoganSquare<br>
3) Asynchronous work - RxJava, RxAndroid. https://github.com/ReactiveX/RxJava  https://github.com/ReactiveX/RxAndroid<br>
4) Retrolambda for using lambdas in Android. https://github.com/evant/gradle-retrolambda

##How it works##

Application has 1 activity which holds 2 fragments. One fragment is for video and other for loading screen. Activity adds both fragment on the start of application. Loading screen shows from 1 to 10 seconds (depends on video loading status).

