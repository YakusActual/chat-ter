/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terminal;

import java.io.File;

/**
 *
 * @author Yakus
 */
public class CommandsManager {

    private String command;
    private String complexCommands[];
    static protected TerminalClient tc;

    public CommandsManager(String args) {
        command = args;
        complexCommands = command.split(" ");
    }

    private String displayHelp() {
        String help = "/connect <ipaddress> - Connects to partner under the given IP addres\n"
                + "/echo <text> - Echoes back the text\n"
                + "/exit - Exits terminal\n"
                // + "/ip - Displays your IP, give it to your partner for them to connect"
                + "/hdd - Displays info about your drives\n"
                + "/help - Displays information about all available commands\n"
                + "/jvm - Displays info about your JVM\n";
        return help;
    }
    
    public String executeCommand() {
        String result = "Unknown command, please type '/help' for available commands\n";
        System.out.println(command);

        if (command.equals("/help")) {
            result = displayHelp();
        } else if (command.equals("/jvm")) {
            result = displayJvmInfo();
        } else if (command.equals("/hdd")) {
            result = displayHddInfo();
        } else if (command.equals("/ip")) {
            result = "here goes your IP: ";
        } else if (complexCommands.length > 1) {
            if (command.equals("/echo " + complexCommands[1])) {
                result = "'" + complexCommands[1] + "'\n";
            } else if (command.equals("/connect " + complexCommands[1])) {
                result = "Connecting to " + complexCommands[1] + "\n";
                boolean clientRuns = startClient(complexCommands[1]);
                if (clientRuns){
                    result = result + "Client online\n";
                    tc.start();
                } else {
                    result = result + "";
                }
            } else {
                result = "Unknown command, please type '/help' for available commands\n";
            }
        } else {
            result = "Unknown command, please type '/help' for available commands\n";
        }

        return result;
    }
    
    private boolean startClient(String ipAddress){  
        tc = new TerminalClient(ipAddress);
        tc.start();
        return tc.getIsClientRunning();
    }

    private String displayJvmInfo() {
        String jvmVersion = System.getProperty("java.jvm.version");
        String jvmName = System.getProperty("java.jvm.name");
        Runtime r = Runtime.getRuntime();
        double maxMemory = r.maxMemory();
        double totalMemory = r.totalMemory();
        double freeMemory = r.freeMemory();
        double usedMemory = (totalMemory - freeMemory);
        double totalFreeMemory = (freeMemory + (maxMemory - totalMemory));
        String fMemory = String.format("%5.2f", (freeMemory) / 1024 / 1024);
        String tMemory = String.format("%5.2f", (totalMemory) / 1024 / 1024);
        String mMemory = String.format("%5.2f", (maxMemory) / 1024 / 1024);
        String uMemory = String.format("%5.2f", (usedMemory) / 1024 / 1024);
        String tFMemory = String.format("%5.2f", (totalFreeMemory) / 1024 / 1024);

        String info = "Java Virtual Machine name: " + jvmName + "\n"
                + "JavaVirtualMachine version: " + jvmVersion + "\n"
                + "Memory stats for JVM\n"
                + "Max memory: " + mMemory + "MB\n"
                + "Total  memory: " + tMemory + "MB\n"
                + "Used memory: " + uMemory + "MB\n"
                + "Free memory: " + fMemory + "MB\n"
                + "Total free memory: " + tFMemory + "MB\n";
        return info;
    }

    public String displayHddInfo() {

        File[] roots = File.listRoots();
        StringBuffer infoDisk = new StringBuffer();
        String diskInfo;

        for (File root : roots) {
            if (root.getTotalSpace() == 0) {
                continue;
            } else {
                infoDisk.append("Disk: ").append(root.getAbsolutePath())
                        .append(" space allocation:\n");
                infoDisk.append("Total space :");
                infoDisk.append(String.format(
                        "%5.2f",
                        Double.valueOf(root.getTotalSpace()) / 1024 / 1024 / 1024));
                infoDisk.append(" Gb\n");
                infoDisk.append("Free space : ");
                infoDisk.append(String.format(
                        "%5.2f",
                        Double.valueOf(root.getFreeSpace()) / 1024 / 1024 / 1024));
                infoDisk.append(" Gb\n");
                infoDisk.append("Occupied disk space : ");
                infoDisk.append(String.format(
                        "%5.2f",
                        Double.valueOf(root.getTotalSpace()
                        - root.getFreeSpace()) / 1024 / 1024 / 1024));
                infoDisk.append(" Gb\n\n");

            }

        }

        diskInfo = new String(infoDisk);

        return diskInfo;

    }
}