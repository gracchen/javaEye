# Eye Saver - hands-free reminder for quick breaks
A light desktop app featuring a customizable reminder to relax your eyes every now and then. You can make the reminders as unintrusive or obnoxious as you please, as frequent or sparse as you need. 

## To run:
System requisites: At least JRE version 9
Download prototype.jar and run it.

## Latest updates:
3/11/2032:
Automatically adds itself in the system tray if supported. Features a hide button that hides it from the screen and taskbar while still working and showing reminders. Reminders dismiss themselves after 20 seconds. To reopen the app, click on the icon in the system tray. 

3/10/2023:
Release prototype 1 for JDK 19, 20 minute worktime + 20 second break.
Release prototype 2 w/ JDK 9 compliance, 1 minute worktime + 20 second break (for testing)

3/9/2023: 
On startup: display current, start of alarm, and end of alarm time.  Sets start as now and alarm as +workMin minutes from now. 
Updates current clock every second. While updating, check if current time passes alarm time. 
If yes and computer awake AT alarm time (not minutes after alarm time passed), say "HOORAY"
if not, computer slept through it and reset alarm.  

3/8/2023: 
Simply displays time when opened + button.
If you click the button, the time updates to current time. 
