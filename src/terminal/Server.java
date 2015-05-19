/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package terminal;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server extends Thread {

    String status;
    int portNumber = 80;
    boolean isBound = false;

    public Server() {
        System.out.println("Setting status to off");
        status = "off";
    }

    public String getStatus() {
        return status;
    }

    public String getPortNumber() {
        return "" + portNumber;
    }

    /*public boolean getIsBound() {
     return isBound;
     }*/
    public void run() {

        status = "okay";
        String temp;

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
            isBound = serverSocket.isBound();
            if (isBound) {
                String ip = processIp(clientSocket.getRemoteSocketAddress().toString());
                TerminalGui.terminalHistory.append("Client connected: " + ip + "\n");
            }
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String time = sdf.format(new Date());
                TerminalGui.terminalHistory.append("[" + time + "]: " + inputLine + "\n");
            }
        } catch (Exception e) {
            TerminalGui.terminalHistory.append("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection, or Client disconnected");
            System.out.println(e.getMessage());
        }
    }

    private String processIp(String ipUnprocessed) {
        String ip;
        StringBuffer sb = new StringBuffer();
        String ipAndPort[] = ipUnprocessed.split(":");
        ipUnprocessed = ipAndPort[0];
        char slashAndIp[] = ipUnprocessed.toCharArray();
        for (int i = 1; i < slashAndIp.length; i++) {
            sb.append(slashAndIp[i]);
        }
        ip = sb.toString();
        return ip;
    }
}