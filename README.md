# GitMad-Heat-Map

<h3>Directions for setting up api-key</h3>
  <p>For setting up your google maps API key that is authorized to use the Maps SDK for Android and the Places SDK for Android you need to do the following:</p>
  <ol>
    <li>Follow the guide located <a href="https://developers.google.com/maps/documentation/android-sdk/signup">here</a> to get your api key.</li>
    <li>Follow the guide located <a href="https://medium.com/code-better/hiding-api-keys-from-your-android-repository-b23f5598b906">here</a> to store and access your api key so that it remains safe and out of the hands of potential baddies.</li>
  </ol>
  <p>After doing the above we will have done two things: created our own maps api key and created a way to store that key without anyone else being able to see or touch it. Howeve,r now we need to link these two things to get our app off and running.
  As of right now in our manifest we have a code snippet that attempts to link to google services. The code looks something like this:
  
  ```xml
  <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_API_KEY"/>
  ```
  Earlier in the second article above we saw that we created a new way to access our key from a file not being tracked by github. IT IS IMPORTANT that you name the key in the file to `GitmadApp_ApiKey`. We then access that key through our build.gradle file under our `BuildTypes`. It should look something like this:
  
  ```java
      buildTypes {
        debug {
            buildConfigField 'String', "maps_api_key", GitmadApp_ApiKey
            resValue 'string', "maps_api_key", GitmadApp_ApiKey
        }
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            buildConfigField 'String', "maps_api_key", GitmadApp_ApiKey
            resValue 'string', "maps_api_key", GitmadApp_ApiKey
        }
    }
```
The above snippet sets a string value in our app labeled `maps_api_key` to the value of our actual api key. From here we can then change the snippet above where we access the google maps services to look like this:
  
```xml
<meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="@string/google_maps_key" />
```

After doing this your api key should be linked and you should be able to use the services provided by that key.
  
<div>
  <h3>Connecting your app to Firebase</h3>
  <p>This app uses Firebase as a way to store and maintain information. We do not give away any credentials when it comes to our database and the information within it (and you shouldn't either), thus you should create your own Firebase project to store information in.</p>
  <ol>
    <li>If you do not already have a user account of Firebase, create one. Easy, short, simple.</li>
    <li>Once you are logged in, create a new Firebase project. Feel free to name it whatever you want.</li>
    <li>Enter into the project. You will know when you do this once you see "Authentication", "Database", "Storage", and more on the left side of the screen. If you are having trouble entering into the project you can try clicking the Firebase logo in the top left and then clicking on the project.</li>
    <li>Click and enter the "Authentication" section. Click on the "Sign-In tab" (within the authentication section) and then enable email and password.</li>
    <li>Click and enter the "Database" section. Create a new database. Select test mode.</li>
    <p>That should be everything we need to do on Firebase. Now we are going to actually link it to the project.</p>
    <li>In Android Studio click on "Tools" in the toolbar and within the dropdown select "Firebase". A sidebar should appear on the right side of the screen.</li>
    <li>Click on the dropdown arrow for "Realtime Database" and then "Save and retrieve data".</li>
    <li>Click on "Connect to Firebase".</li>
    <li>If you are not signed in, sign in, and then choose the project you have created for this app.</li>
    <li>If the "Add the Realtime Database to your app" button is present in step 2. Click it and accept changes.</li>
    <li>Now do the same thing under the "Authentication" section in the Firebase side tab.</li>
  </ol>
  <p>Cool, you should be set up to work with Firebase and can now write and read data from your Firebase account.</p>
</div>
  
<div>
  <h3>These are things that I think we could add quickly that would help out a lot:</h3>
    <h4>Add user account information to the nagivation drawer.</h4>
    <p>This one is pretty straight to the point. Add a fragment for this and add a link in the navigation drawer.</p>
  </div>
  <div>
    <h4>Remove log out button from the home screen and place it in the navigation drawer.</h4>
    <p>UI wise this may look better being at the bottom of the drawer, but I am not sure if material design has anything to say about this. I would look through the material design standards first and do what it says. If it does not have anything then I would see if you can implement one at the bottom and on in the menu item list and just see which looks better.</p>
    <p>Remember there are two instances of the nav drawer (one in the UserLoggedIn activity and one in the HeatMap activity) so add them in both instances.</p>
  </div>
   <div>
    <h4>Write a script to auto-remove location instances.</h4>
    <p>This will most likely be ran from some hosting service like heroku. Ask Lucas if you have any questions.</p>
  </div>
</div>
 
