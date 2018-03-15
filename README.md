FixService Android App
======

> Android App for fix service job post, bid and payment

## Overview

### 1. Main Features
- User management  
Signup, Login, Profile, Setting, ...

#### 1.1 Admin Version
- User management  
User list, Ban/Unban User ...
- Report management  
Report list, Delete Report

#### 1.2 Client Version
- Job management  
Post Job, Posted Job list
- Bid management  
Bid list, Choose bid ...  
- Payment & User rate

#### 1.3 Contractor Version
- Job & Bid  
Available job list, filter, bid, ...
 
### 2. Techniques 
#### 2.1 UI Implementation
- Subdirectories in Resource Layout  
- Toolbar  
```app\_bar\_normal.xml```  
Custom Title, Back Button  
```app\_bar\_admin.xml```  
```app\_bar\_serviceman.xml```  
- Material Buttons  
Buttons with simple color background or transparent background are made as material buttons  
RaisedButton: ```PrimaryButtonStyle```  
FlatButton: ```PrimaryFlatButtonStyle```  
- Material clickable background  
RecyclerView Items  
CardView: foreground
- Custom view with Class and xml attributes  
```res\values\attrs.xml```  
```context.obtainStyleAttributes()```  
```views.serviceman.ViewToolbar``` -> ```rightImage```  
```views.serviceman.ViewRateStar``` -> ```starValue```  
- Padding on recyclerview but not clip to edge  
```android:clipToPadding="false"```

#### 2.2 Function Implementation
- Getting location using [```LocationManager```](https://developer.android.com/reference/android/location/LocationManager.html) & [```LocationListener```](https://developer.android.com/reference/android/location/LocationListener.html)
- Used Google Firebase for backend  
[```GeoFire```](https://github.com/firebase/geofire-java) for location saving & querying  
- Implemented Parcelable for all model for passing between activities

##### 2.2.1 Payment from client to contractor through Stripe
###### 2.2.1.1 Client
- Create client when sign up  
Stripe API: [https://stripe.com/docs/api#create_customer](https://stripe.com/docs/api#create_customer)  
- Add card as payment information -> Making Source  
Android SDK:  
```CardMultilineWidget``` for input card information  ```Stripe.createSource()```  
- Attach source to client  
Stripe API: [https://stripe.com/docs/api#attach_source](https://stripe.com/docs/api#attach_source)
- Create charge for payment
Stripe API: [https://stripe.com/docs/api#create_charge](https://stripe.com/docs/api#create_charge)  

###### 2.2.1.2 Contractor
- Setting Account Id to get payment  
[Using Connect with Express Accounts](https://stripe.com/docs/connect/express-accounts)  
Oauth connection in WebView to authenticate and get account information  

#### 2.2.3 Code tricks  
- Common module for input image
```PhotoActivityHelper``` & ```E2FUpdateImageListener```  
```AdminBanUserHelper```  
```GeoLocationHelper```  
```JobDetailHelper```  
```UserDetailHelper```  
- Excluding fields in model  
```@get:Exclude``` excludes the field when getting, avoids redundant saving in db  
- <s>Fetching data from firebase synchronously</s>  
Implemented using [Play services Task API](https://developers.google.com/android/guides/tasks)  
```User.readFromDatabase```  
- All Api calls are defined in ApiManager with respone callback  
- Save sensitive Information in different string resource file  
```app/src/main/res/values/secret.xml```, gitignored

#### 2.2.4 Third-Party Libraries
- [BottomSheet](https://github.com/soarcn/BottomSheet) v1.x  
Showing options when selecting photo  
- [Glide](https://github.com/bumptech/glide) v4.6.1    
Seekbar with numeric value label in Filter page for serviceman
- [AndPermission](https://github.com/yanzhenjie/AndPermission) v2.0.0-rc2    
Real Time permission checking module
- [Android RateThisApp](https://github.com/kobakei/Android-RateThisApp) v1.2.0   
Rate app in Google Play
- [Strip Android SDK](https://github.com/stripe/stripe-android)    
- [OkHttp](https://github.com/square/okhttp) v3.10.0  
Calling & Fetching data from Rest Api  
\* Payment process using Stripe & Firebase API

## Need to Improve
- Add and improve features