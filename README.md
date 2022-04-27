# picshare
# Build
This project is using standard Android Studio directory structure. And the gradle is the compile tool. Make sure the java version is 11 for building.
## 1.Change the server URL
Change the file `/app/build.gradle`. In the `buildConfigField("String", "ServerUrl", '"http://3.88.236.75:8080/"')`, we can change it to the server that we deploy. Or just leave it default.
## 2.Build the Apk File
```
gradlew.bat assembleDebug
```
The apk file is located at `app/build/outputs/apk/debug/hk.hku.cs.picshare-debug.apk`
