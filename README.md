# GitMad-Heat-Map

<h3>Directions for setting up api-key</h3>
  <p>For setting up your google maps API key that is authorized to use the Maps SDK for Android and the Places SDK for Android you need to do the following:</p>
  <p>1) Follow the guide located <a href="https://developers.google.com/maps/documentation/android-sdk/signup">here</a> to get your api key.</p>
  <p>2) Follow the guide located <a href="https://medium.com/code-better/hiding-api-keys-from-your-android-repository-b23f5598b906">here</a> to store and access your api key so that it remains safe and out of the hands of potential baddies.</p>
  <p>After doing the above we will have done two things: created our own maps api key and created a way to store that key without anyone else being able to see or touch it. Howeve,r now we need to link these two things to get our app off and running.
  As of right now in our manifest we have a code snippet that attempts to link to google services. The code looks something like this:
  
  ```xml
  <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_API_KEY"/>
  ```
  Earlier in the second article above we saw that we created a new way to access our key from a file not being tracked by github. IT IS IMPORTANT that you name the key in the file to `GitmadApp_Key`. We then access that key through our build.gradle file under our `BuildTypes`. It should look something like this:
  
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
 
