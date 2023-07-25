import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class MinecraftServerHandler {

    public static void main(String[] args) {
        MinecraftServerHandler ControlPannel = new MinecraftServerHandler();

        File f = new File(".\\paths.txt");
        if (f.exists())
        {
            boolean emptyString = (ControlPannel.ReadPathsFile(".\\paths.txt", 0).isBlank() || ControlPannel.ReadPathsFile(".\\paths.txt", 1).isBlank());
            boolean execEnding = ControlPannel.ReadPathsFile(".\\paths.txt", 0).endsWith("\\Minecraft Launcher\\MinecraftLauncher.exe");
            boolean nullStrings = (ControlPannel.ReadPathsFile(".\\paths.txt", 0) == null || ControlPannel.ReadPathsFile(".\\paths.txt", 1) == null);

            if (emptyString || execEnding || nullStrings)
            {
                ControlPannel.RepairPathsFile(".\\paths.txt");
            } else
                {
                    ControlPannel.StartMinecraft();
                }
        } else {
            ControlPannel.MakePathsFile();
        }
    }

    void StartMinecraft()
    {
        boolean isOnline = false;
        String launcherPath = ReadPathsFile(".\\paths.txt", 0);

        Scanner serverStartInput = new Scanner(System.in);
        System.out.println("Do You Want To Start Your Server? [y/n]:");
        String resp = serverStartInput.nextLine();

        if (resp.equals("y")) {
            isOnline = true;
        }

        serverStartInput.close();

        MinecraftWorldDataManagement(isOnline, ReadPathsFile(".\\paths.txt", 1));

        try {

            Thread.sleep(5000);

            if (isOnline) {
                System.out.println("[*] Launching Server!");
                StartUpPrompt("cmd /c start .\\layeredMinecraftStarter.bat");
            }

            System.out.println("[*] Launching Minecraft Java Launcher");

            StartUpPrompt(launcherPath);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void StartUpPrompt(String givenPath) {
        System.out.println("[*] Creating New Process");

        try {
            // Command to create an external process
            String execProg = givenPath;

            // Running the above command
            Runtime.getRuntime().exec(execProg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String ReadPathsFile(String path, int id) {
        try {
            if (Files.readAllLines(Paths.get(path)).size() == 2)
            {
                String line = Files.readAllLines(Paths.get(path)).get(id);
                return line;
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        return "";
    }

    void MakePathsFile() {
        Scanner pathInput = new Scanner(System.in);
        System.out.println("Enter in PATH of Minecraft Launcher.exe\nEx.) \"C:\\Program Files (x86)\\Minecraft Launcher\\MinecraftLauncher.exe\"\n Launcher Path: ");
        String launcherPath = pathInput.nextLine();
        
            // Various Rules to Ensure correct Path String Format
            launcherPath = launcherPath.replace("\\", "\\\\");
            launcherPath = launcherPath.replace("/", "\\\\");

            if (launcherPath.indexOf(0) == '\"')
            {
                launcherPath = "\"" + launcherPath;
            }

            if (launcherPath.indexOf(launcherPath.length() - 1) == '\"')
            {
                launcherPath = launcherPath + "\"";
            }

        System.out.print("\n");

        System.out.println("Enter in PATH of Local World Folder\nEx.) \"C:\\Users\\USER\\AppData\\Roaming\\.minecraft\\saves\\WORLD_NAME\"\n World Save Path: ");
        String worldFolderPath = pathInput.nextLine();

            // Various Rules to Ensure correct Path String Format
            worldFolderPath = worldFolderPath.replace("\\.", "\\\\.");
            worldFolderPath = worldFolderPath.replace("\\.", ".");
            worldFolderPath = worldFolderPath.replace("//.", "\\\\.");
            worldFolderPath = worldFolderPath.replace("/.", "\\\\.");
            worldFolderPath = worldFolderPath.replace("\\.", ".");
            worldFolderPath = worldFolderPath.replace("Roaming.minecraft", "Roaming\\.minecraft");


            worldFolderPath = worldFolderPath.replace("\\", "\\\\");
            worldFolderPath = worldFolderPath.replace("/", "\\");
            worldFolderPath = worldFolderPath.replace("//", "\\");
            

            if (worldFolderPath.indexOf(0) == '\"')
            {
                worldFolderPath = "\"" + worldFolderPath;
            }

            if (worldFolderPath.indexOf(worldFolderPath.length() - 1) == '\"')
            {
                worldFolderPath = worldFolderPath + "\"";
            }
        System.out.print("\n");

        BufferedWriter f_writer;
        try {
            f_writer = new BufferedWriter(new FileWriter("./paths.txt"));

            f_writer.write(launcherPath);
            f_writer.newLine();
            f_writer.write(worldFolderPath);

            f_writer.close();

            System.out.println("[+] Paths File Created! Run the Launcher Again to Continue! [Press Enter] ");
            pathInput.nextLine();
            pathInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void RepairPathsFile(String filePath)
    {
        File pathFile = new File(filePath);
        if (pathFile.delete()) {
            System.out.println("[*] Detected File Tamperment!");
            System.out.println("[*] Deleted the file: " + pathFile.getName());
            System.out.println("[+] Building New paths.txt. . .\n");
            MakePathsFile();
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    String GetServerPath()
    {
        String ServerFolder = "";

        // Creating a File object for directory
        File activeDir = new File(".\\");
        // List of all files and directories
        File contents[] = activeDir.listFiles();

        for (int i = 0; i < contents.length; i++) {
            String pathString = contents[i].getPath().replace("\\", "\\\\");

            File directoryPath = new File(pathString);
            // List of all files and directories
            String dirContent[] = directoryPath.list();

            try {
                if (Arrays.asList(dirContent).contains("level.dat")) {
                    ServerFolder = "\"" + pathString + "\"";
                    System.out.println("[+] Server Folder Found! : " + ServerFolder);
                    return ServerFolder;
                }
            } catch (Exception e) {
                System.out.println("[*] Searching Next Path. . .");
            }
        }

        return "";
    }

    void MinecraftWorldDataManagement(boolean b, String localPath)
    {
        String sourceFolder = GetServerPath();
        String destinationFolder = localPath;

        if (b) {
            System.out.println("[*] Updating Server World Data. . .");

            // Copy Local to Server
            String cmd = "xcopy " + destinationFolder + " " + sourceFolder + " /E /I /H /K";
            System.out.println(cmd);

            try {
                // Running the above command
                Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("[*] Updating Local World Data. . .");

            // Copy Server to Local
            String cmd = "xcopy " + sourceFolder + " " + destinationFolder + " /E /I /H /K";
            System.out.println(cmd);

            try {
                // Running the above command
                Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}