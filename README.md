# Eye Saver - hands-free reminder for quick breaks
A light desktop app featuring a customizable reminder to relax your eyes every now and then. You can make the reminders as unintrusive or obnoxious as you please, as frequent or sparse as you need. 

## To run:
System requisites: At least JRE version 9
Download prototype.jar and run it.

## Latest updates:
#### 3/17/2023: 
Adds exception possibility if ImageIO.read() failed BUT File(path) did not -  when iconURL path in settings points to vaild file but not an image.
- in try { }:  if (bad condition) throw new NullPointerException();  //or IOException or ArrayIndexOutofBound or IllegalArgumentException,etc.etc.
- catch (Exception e)  will catch all exceptions
     - then to check what kind: if (e instanceof IOException) { }

#### 3/16/2023:
Saves user chosen settings in settings.txt upon user choosing icon file or user mouse released on one of the duration sliders. Added code to deal with some unexpected edge cases (user chosen image moved, settings.txt not found, extra long filename, out of bounds time settings)
- JFileChooser filter incorrectly set up, should be:
     - new FileNameExtensionFilter("Images", ImageIO.getReaderFileSuffixes())
- JSlider: slider.getValueIsAdjusting() check if user actively dragging slider
- Integer.valueOf()  exception if string not integer,  -->  surround try-catch

#### 3/14/2023:
Saves user chosen settings in settings.txt upon user choosing icon file.
- "editing" file:
     1. create new file ~settings.txt (cs 111 if exists already? temp file? pipe? rename as timestamp of user? possible issues here
     2. write to new file (editing $$$$ ?  just want add one line, costly?)
     3. CLOSE THE FILE    cannot delete/rename  without close.....like C++
     4. delete original
     5. rename new as original
     6. update original file var = new File(original);
- File x = new File("settings.txt");
     - x.delete();  
     - y.renameTo(x);
- Scanner (to read file)
     - new Scanner(File)   --> surround try catch
     - .nextLine()   vs    .next()     vs    .hasNext()
- Formatter (to write to file)
     - new Formatter(newFilename)
     - .format(%s\n", str); to write to file
     - .close()  !!!!ESSENTIAL TO LATER MODIFY FILE (much hours wasted)
- Image from File (not path)
     - ImageIO.read(file); 
- Strings
     - Integer.valueOf()  string->integer
     - String.valueOf()   int -> string
     - buf.indexOf('c') or buf.indexOf("str")   good deliminator

#### 3/13/2023:
Update default window icon to match system tray icon. Allows user to pick a file on computer as new icon for both window and tray, and has no effect if picked nonimage file. 
- JFileChooser
     - jfc.showDialog(null, "Select new icon") will popup new picker window
          - return val == JFileChooser.APPROVE_OPTION if user picked file
     - jfc.getSelectedFile().getAbsolutePath())
- ImageIO.read(new File(string abs path);
- setIconImage(); to set window icon

#### 3/12/2023:
Formatting using GridBagLayout() instead of default FlowLayout(). Separates break, home, and settings pages into separate JPanels. Adds settings tab with two sliders that preview and adjust duration of work and break cycles. 
- GridBagConstraints
     - .WEST, .HORIZONTAL, .NONE, etc.
- GridBagLayout()
     - .fill , .gridwidth, .insets (padding), .gridx, .gridy, c.anchor
- JTabbed Pane
     - .addTab("label", JPanel), .setVisible(false); 
- JPanel
- JSlider
     - setValue(), addChangeListener(), getValue()
     - handler: stateChanged(ChangeEvent e) {}
- SwingConstants.CENTER, .SOUTH, etc. for JLabel


#### 3/11/2023:
Adds itself in the system tray on button click. This hides it from the screen and taskbar while still working and showing reminders. Reminders dismiss themselves after 20 seconds. To reopen the app, click on the icon in the system tray. 
- System.exit(0);
- dispose();  //hides window entirely
- TrayIcon
     - new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("path")));
- SystemTray
     - .isSupported()
     - .getSystemTray()
     - .add(Icon) and .remove(Icon)
- setLocationRelativeTo(null); to center window on startup
- .setVisible(false);
- .addMouseListener(), class extends MouseAdapter
- mouseClicked(MouseEvent e)
- .setToolTip("show on hover");


#### 3/10/2023:
Release prototype 1 for JDK 19, 20 minute worktime + 20 second break.
Release prototype 2 w/ JDK 9 compliance, 1 minute worktime + 20 second break (for testing)
Maximizes window on break to block everything else.
- setExtendedState(JFrame.MAXIMIZED_BOTH);


#### 3/9/2023: 
On startup: display current, start of alarm, and end of alarm time.  Sets start as now and alarm as +workMin minutes from now. 
Updates current clock every second. While updating, check if current time passes alarm time. 
If yes and computer awake AT alarm time (not minutes after alarm time passed), say "HOORAY"
if not, computer slept through it and reset alarm. 
- LocalDateTime (better alt to Date)
     - .now(), .plusMinutes(), .getHour(), isBefore()
- String.format("%02d", int); 
- Thread.sleep(miliseconds); 
- from hackyToFront():
     - setAlwaysOnTop(true);
     - setExtendedState(JFrame.NORMAL);
     - toFront();  and toBack();


#### 3/8/2023: 
Simply displays time when opened + button.
If you click the button, the time updates to current time. 
- Date()
- JButton + explicit ActionListener handler
- JFrame
- JLabel
- FlowLayout

## Others' Code Snippets used:
for hackyToFront(): 
https://stackoverflow.com/questions/34637597/bring-jframe-window-to-the-front

for initializing and adding system tray icon:
https://www.youtube.com/watch?v=NZ_fhAIOxfI
