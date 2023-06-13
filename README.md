# Suara Kita
Suara Kita was an native android application that created using Kotlin. All the database was deployed online and accessed with API.

## Tech 
- Android Studio
- Kotlin
- Retrofit
- CameraX

## API
Suara kita use 3 API in the process :
- Database (https://capstone-386111.et.r.appspot.com/)
- Machine Learning OCR (https://flask-ocr-rkg6stvtla-uc.a.run.app/)
- Machine Learning Siamese (https://flask-siamese-rkg6stvtla-uc.a.run.app/)

## Library and Resources 
- jitpack.io (usage will be mentioned in the feature list)
- m2.material.io
- m3.material.io
- Glide

## Features
### 1. Sign Up
Here are the flow when user create Suara Kita account:
1. Welcome page. User can chosse to register(daftar) or login(masuk) when already have an accout.
2. Terms and Condition.
3. ID Card Scan. First step of register is to capture user's ID Card or KTP using CameraX provided by application. There will be line or guide where user should position the camera and ID Card before capture. This ID Card data will be extracted using OCR feature.
4. Data Verification Page. User's data from ID Card will be retreived and if there are any mistake, user can revise them. On this page, user also create their username and password.
5. Face Training Page. User required to take two pictures for the purpose of data training in siamese model. 


### 2. Login
After signing up and create Suara Kita account, user will be directed to login page and input their credential (email and password) before start using the application.

### 3. Home
Home page created by using 1 activity for the navigation and two fragment (home fragment and profile fragment). For the home landing page, was shown the user's name , slider or slideshow for banner and encouragement for people to use their voting change. Below the slider, displayed pie chart to see the current vote down between the candidates. Both slider and pie chart was created using jitpack.io library by adding nescessary implementation in the dependencies.
```sh
implementation "com.github.denzcoskun:ImageSlideshow:0.1.0" //slider library on jitpack.io
implementation 'com.github.intrudershanky:scatter-piechart:1.0.0' //pie chart library on jitpack.io
```

### 4. Profile
Profile page was connected to navigation on home activity and accessed through profile fragment. The page show user data, such as name, date of birth, email, and voting status. The data are view only and can not be changed. System will always request new data through API by using retrofit) to get latest data of the voting status. Below the data, displayed logout button for user to signout of the application and go to the welcome page.

### 5. Vote
Here are the flow when user want to cast their vote:
1. ID Number Validation
2. Face Verification.
3. Vote Guide.
4. Vote Page.

> Jitpack Library/Dependency: 



## References
- [Android Developer](https://developer.android.com/)
- [Dicoding](https://www.dicoding.com/)
- [Material Design]((https://m3.material.io/))
- jitpack.io Documentation
