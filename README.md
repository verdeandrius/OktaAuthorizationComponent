# Okta Authorization Component

**Last version:**
[![](https://jitpack.io/v/verdeandrius/OktaAuthorizationComponent.svg)](https://jitpack.io/#verdeandrius/OktaAuthorizationComponent)

## Description
This component is an object (Kotlin) or static (Java) method that requires to be initialized and then is possible to uses in the views required.

## How to use it
To use this component follow the next steps: 

**1. Add to build.gradle(Project):**
```Gradle
allprojects {
  repositories {
     ...
	maven { url 'https://jitpack.io' }
	       }
	    }
```  

**2. Add to build.gradle(app):**
```Gradle
dependencies {
	        implementation 'com.github.verdeandrius:OktaAuthorizationComponent:0.1.0'
		implementation 'com.okta.android:oidc-androidx:1.0.11'
	     }
``` 

**3. For each enviroment, add the buildConfigField espected:**
* *CLIENT_ID* 
* *REDIRECT_URI* 
* *END_SESSION_REDIRECT_URI* 
* *DISCOVERY_URI* 

```Gradle
staging {

        //Okta constants
        buildConfigField 'String', 'OKTA_CLIENT_ID', '"XXXjg4rnuyNU834TAXXX"'
        buildConfigField 'String', 'OKTA_REDIRECT_URI', '"com.oktapreview.cargillcustomer-uat:/callback"'
        buildConfigField 'String', 'OKTA_END_SESSION_URI', '"com.oktapreview.cargillcustomer-uat:/callback"'
        buildConfigField 'String', 'OKTA_DISCOVERY_URI', '"https://cargillcustomer-uat.oktapreview.com/oauth2/XXXf5swlrsgiHV3z7XXX/.well-known/openid-configuration"'
 ...
        }
```

**4. Place in your first activity the Okta configuration params and start the component initialization:** 
```Kotlin
private fun initializeOkta(){
        OktaAuthorization.setOktaConfigParams(
            BuildConfig.OKTA_CLIENT_ID,
            BuildConfig.OKTA_REDIRECT_URI,
            BuildConfig.OKTA_END_SESSION_URI,
            BuildConfig.OKTA_DISCOVERY_URI,
            this
        )
        OktaAuthorization.initialize()
    }
```    

**5. Inside onCreate method set the result callback for each authorization action (Login and Logout):**
```Kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_login_view)
	this.initView()
    }

private fun initView() {
   
    //Okta
    this.setOktaLoginResult()

    //Buttons
    ... 
}

private fun setOktaLoginResult(){

        OktaAuthorization.onLoginResult =
            object :
                OnLoginResultListener {
                override fun onLoginResult(client: WebAuthClient) {
                    //TODO Perform here your on login success actions, example the navigation:
                    startActivity(Intent(context, HostActivity::class.java))
                }

                override fun onErrorResult(msg: String?, error: AuthorizationException?) {
                    //TODO Perform action to handle error scenario 
                }
            }
}
```

**6. Inside onClickListener of login button, make the call to "doLogin":**
```Kotlin
	
    //Buttons
    this.binding.buttonLogin.setOnClickListener {
    
        //Call Okta Login
        OktaAuthorization.doLogin(this)
    }
``` 

**The webView should be open and displayed the Okta login page**
