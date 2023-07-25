<title>How To Use</title>

<h2>Set-up:</h2>
    1.) Download the mc server software provided by mojang. (https://www.minecraft.net/en-us/download/server)
    2.) Move the "Server.jar" into a folder somewhere on your pc where you wanna keep the server stored.
      2a.) The `server folder` will be the repo folder -> Contains the Minecraft_Java_Launcher.bat and MinecraftServerHandler.java
            Those Files and others are used with your Server.
    3.) Find your world folder %appdata%/roaming/.minecraft/saves/{world name} : Copy and paste that into the server folder
    
    4.) Run the bat file until it stops, it will generate needed files in the folder.
    5.) Find "Eula.txt" and open it changing false to true
    6.) Open server.properties and change any of the server properties that you want.
    7.) Set the "level-name" property in the server property file to the EXACT NAME OF THE WORLD FOLDER
    8.) Port forward port 25565 --> Log into your router to port forward
    
    9.) 
    10.) Run the shortcut for Minecraft_Java_Launcher.bat
    11.) El Fin!

<h3>Logging into Router:</h3>
    1.) Type ipconfig in cmd.exe
    2.) Locate the Default Gateway --> Look for the 0.0.0.0 [IPv4] format
      2a.) If you have a spectrum router use the mobile app to port forward
      2b.) If you have something custom you should be good to login through https://x.x.x.x/