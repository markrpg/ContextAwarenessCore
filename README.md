# ContextAwarenessCore
A context awareness platform for android, which I developed whilst attending Edinburgh Napier University in Scotland for my honours dissertation.

Instructions:

The current context awareness platform contains an example application, which showcases context awareness in a fitness use-case scenario, In order to use the context awareness features in your own usecase scenario, do the following steps.

- Step 1: Clone the repository in Android Studio.
- Step 2: Create a new Activity and extend the interface "ContextClientActivity".
- Step 3: Implement the method "updateContexts" associated with the interface.
- Step 4: The Activity is now ready to receive contextual information, simply create new instance of the primary context types (IdentityContext, LocationContext, TimeContext and ActivityContext), directly within your Activity or create a Secondary Context type and aggregate these types in any order you want.
- Step 5: Update the UI through the "updateContexts" method as it runs every 10 seconds.


![ScreenShot](https://github.com/markrpg/ContextAwarenessCore/blob/master/PosterPDF.png)
