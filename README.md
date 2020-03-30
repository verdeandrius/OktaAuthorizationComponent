# OktaAuthorizationComponent
[![](https://jitpack.io/v/verdeandrius/OktaAuthorizationComponent.svg)](https://jitpack.io/#verdeandrius/OktaAuthorizationComponent)

To use this component follow the next steps: 

1. **Add to build.gradle(Project):**
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```  
2. **Add to build.gradle(app):**
```
dependencies {
	        implementation 'com.github.verdeandrius:OktaAuthorizationComponent:0.1.0'
		implementation 'com.okta.android:oidc-androidx:1.0.11'
	}
```  
3. **Place in your first activity the Okta configuration params and start the component initialization:** 
```
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
