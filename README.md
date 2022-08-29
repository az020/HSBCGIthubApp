A Simple Github App
===========================================================

* This is a simple GitHub app that allows users to search GitHub repositories and has a simple profile page.
* The app is composed of 3 screens: search, profile and login.
* The app requires storage permission at first launch, but it does not actually access your storage.

### Libraries/Frameworks used
* The app presents data using MVVM pattern with LiveData nad ViewModel from Jetpack Library.
* The network requests are performed through Retrofit.
* The profile page's avatar view uses Picasso.
* Async tasks are executed using Kotlin coroutine.

### Login and Security
The app uses GitHub's personal access token to login. You can generate it here: https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token.
After user logs in, the personal access token is stored locally in encrypted shared preference until user logs out

### Testing
The project uses Mockito to unit test. Due to time limit, only view models and AccountManger is tested.

### Test Cases
* User logs in successfully: App navigates to Profile page. User info is displayed. When user opens the app next time, he/she does not
need to login again until the token expires
* User fails to login: Error message are presented. App is still in Login page, not navigating. When user opens the app next time, he/she still needs to login.
* User token expires: Profile page cannot display user's info. User can choose to logout and login again.
* User logout: Navigates to search page. Profile page displays login button. When user opens the app next time, remained logged out.

* search with results: display the result list
* search with 0 result: not displaying the list, total result should be 0
* search fail: display error message

* configuration change: app preserves its data and state through configuration change
