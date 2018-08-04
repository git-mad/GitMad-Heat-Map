# GitMad-Heat-Map

<h3>Directions for setting up api-key</h3>
  <p>For setting up your google maps API key that is authorized to use the Maps SDK for Android and the Places SDK for Android you need to do the following:</p>
  <p>1) Follow the guide located <a href="https://developers.google.com/maps/documentation/android-sdk/signup">here</a> to get your api key.</p>
  <p>2) Follow the guide located <a href="https://medium.com/code-better/hiding-api-keys-from-your-android-repository-b23f5598b906">here</a> to store and access your api key so that it remains safe and out of the hands of potential baddies.</p>
  
<div>
  <h3>These are things that I think we could add quickly that would help out a lot:</h3>
  <div>
    <h4>Making sign in optional. Users should be able to see the map without creating an account.</h6>
    <p>If we do this we should look to find some way to link an anonymous user to locations so that they only have one piece of data associated with them. My thought is we create a preference that is used when the user is not signed in, and if they create an account we just use their username as normal.</p>
  </div>
  <div>
    <h4>Add a hamburger on the top left for options like sign out and view account information.</h6>
    <p>As of right now I don't really know what all would be inlcuded in this area. It may be a little more difficult to try and include that now, but I think it is worth a shot. You could have paths to account info, signing out, and maybe something like viewing friends.</p>
  </div>
</div>
 
