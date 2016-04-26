# Intilery Android SDK Example Application

### Prerequisites
Before you start your integration you will need the following.

* Google Cloud Messaging API Key
* Intilery API Key
* Google Services JSON file

### Setting up your GCM Key
1. Log into your [Google API Console](https://console.developers.google.com/)
2. Navigate to the Google Cloud Messaging page, it's found under the Mobile APIs which will take you to the GCM settings page. Click the blue enable button to enable GCM for your account.
3. On the left hand menu there should be an option called `Credentials`, this will take you to a page to generate your keys. *If you already have an API Key for your app click skip to step 6*
4. Click `Create Credentials > API key > Android key`, enter your application's name, click `Add package name and fingerprint`, enter your Package Name and your SHA-1 certificate fingerprint which must be from the key you sign your applications with. When debugging you will need to use your debug keystore, but you must remember to update this when you're publishing your app to the market. You can generate the fingerprint with:
   
   ```
   $ keytool -list -v -keystore mystore.keystore
   ```
   When debugging you will need your debug keystore (`debug.keystore`) file is found in one of the following directories:
   *   ~/.android (Linux,Mac OS),
   *   C:\Documents and Settings\[User Name]\.android in Windows XP or 
   *   C:\Users\.android in Windows Vista or Windows 7 (password is `android`). 
   
   For more information please read the [Google support page](https://support.google.com/cloud/answer/6158862?hl=en#creating-android-api-keys)
5. Now you should have a Google API Key with GCM support (this can take a few minutes to be activated). Make note of your Google API Key which is needed for setting up your Intilery API Key.
6. You are also going to need your Project Number which can be found in the Google API console by clicking on the three dots in the top right of the page and click on `Project Information`. Make note of your Project Number as this will be required the integration.

### Setting up your Intilery API Key
1. Log into the [Intilery app dashboard](https://www.intilery.com/app) with your account details. 
2. Navigate to the Mobile Apps page by clicking `Settings` (the cog icon) and then `Mobile Apps`.
3. At the bottom of the page click the `Add Application` button, enter an application name (we will need this for later), select `Android Push Notifications` from the application type and click `add`.
4. You will need to add your Google API Key to the application.
5. Click on the `New Key` button to generate your Intilery API KEY and now you're ready to start the integration.

### Google Services JSON file
You will need a `google-services.json` file in the example folder which can be generated from the [Google Support Site](
https://developers.google.com/cloud-messaging/android/client#get-config). The file should look something like this:
```
{
  "project_info": {
    "project_id": "PROJECT_ID",
    "project_number": "PROJECT_NUMBER",
    "name": "PROJECT_NAME"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "MOBILE_SDK_KEY_HERE",
        "client_id": "android:com.intilery.android.example",
        "client_type": 1,
        "android_client_info": {
          "package_name": "com.intilery.android.example",
          "certificate_hash": []
        }
      },
      "oauth_client": [],
      "api_key": [],
      "services": {
        "analytics_service": {
          "status": 1
        },
        "cloud_messaging_service": {
          "status": 2,
          "apns_config": []
        },
        "appinvite_service": {
          "status": 1,
          "other_platform_oauth_client": []
        },
        "google_signin_service": {
          "status": 1
        },
        "ads_service": {
          "status": 1
        }
      }
    }
  ],
  "client_info": [],
  "ARTIFACT_VERSION": "1"
}
```

### Adding the SDK Support to an application
Go into your build.gradle and add the following to the list of dependencies
```
compile 'com.intilery.android:sdk:0.1.1'
```

## Usage
Before use, create an instance of the Intilery API.
```java
new Intilery(IntileryConfig.builder()
    .url("https://www.intilery-analytics.com/api") //Changes if you are on your own AWS cluster.
    .intileryToken("*********") //Token found in Intilery App
    .appName("zed-test1") //As found in the Intilery App
    .rootContext(someContext) // Context used to query android APIs
    .userAgent("Android App") //Optional
    .gcmToken(someRegistrationID) //Required for receiving push notifications
    .build());
    
```
### Sending an event
Event sending is achieved through an API
```java
Intilery.i().getIo().track(
    IntileryEvent.builder()
        .eventData(
            EventData.builder("Event Action", "Event Name") //Event name is optional.
                .data("ItemData", 
                    new MapBuilder<String, Object>()
                        .put("Name", "Red K Trainers")
                        .put("Price", "13.99")
                    .build()
                )
            .build()
        )
    .build()
);
```

### Managing User Details
To attach data to a visitor, e.g City, Language or any other property available within the Intilery Platform on your account - there is an API.
```java
    Intilery.i().getIo()
        .setVisitorProperties(
            PropertyUpdate.builder()
                .property("First Name", "Tom")
                .property("Last Name", "Jones")
                .property("Sex","Male")
            .build()
        );
```
Additionally if you want to get properties of a customer which Intilery is storing then you can use this API:
```java

    Intilery.i().getIo().getVisitorProperties(
        // We now declare the receiver once we get the response from the Intilery API
        new IntileryIO.PropertyReceiver() {
            @Override
            public void receive(Properties properties) {
                // You can use properties.get(PROPERTY_NAME) to get the received property
                System.out.println("The first name is: " + properties.get("First Name"));
            }
        }
        // Next we list the properties we want to fetch
        , "First Name", "Last Name");
```
### Managing the unique device ID
On first initialisation the API generates as a UUID. This UUID acts as a unique identifier for the device and is stored persistently.
Once created the API will contact Intilery servers to register with the platform as a device.
You can get the UUID we are using through:
```java
Intilery.i().getUserInfo().uuid();
```
If you ever want to reset this device ID then you can use:
```java
Intilery.i().getUserInfo().reset()
```

##  Push Events
The Intilery Platform supports sending Push Notifications as part of a campaign. To analyse how engaging your push notifications are you may want to send an event when someone acts upon a notification (e.g opening app). In the future a set of APIs will be available to deal with this however until then please contact your account manager to find out which events you need to send to allow them to integrate with the platform.

