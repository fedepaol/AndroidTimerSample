### Sample timer app
This sample timer app shows the correct way to implement a long running timer in Android. This is intended to be a sample to the approach described in [my blogpost here.](http://fedepaol.github.io/blog/2016/06/20/how-to-a-timer/)


#### From the blogpost:
The idea is to run the countdown timer as long as the app is foregrounded, showing the progress to the user _one second at the time_, but set a system alarm whenever the app goes in background. Whenever the user gets back to the app, you'll cancel the system alarm and restart the timer from where it is supposed to start.


From the user's perspective, the timer is running even if the app is in background, because whenever he returns to the app he sees what he is expecting to see (the time passed). On the other hand, if the timer expires when the app is in background, a friendly notification will remind him that he has to take the plum cake out of the oven.

