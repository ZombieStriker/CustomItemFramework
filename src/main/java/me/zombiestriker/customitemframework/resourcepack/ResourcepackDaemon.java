package me.zombiestriker.customitemframework.resourcepack;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourcepackDaemon extends Thread {

    private int port;
    private ServerSocket socket;
    private File resourcepack;

    public boolean running = true;

    public ResourcepackDaemon(int port, File resourcepackFile) throws IOException {
        this.port = port;
        this.resourcepack = resourcepackFile;
        this.socket = new ServerSocket(port);
        socket.setReuseAddress(true);
    }

    @Override
    public void run() {
        while (running) {
            try {
                new Thread(new ResourcepackConnection(this, socket.accept())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ResourcepackConnection implements Runnable {
        protected final ResourcepackDaemon server;
        protected final Socket client;

        public ResourcepackConnection(ResourcepackDaemon server, Socket client) {
            this.server = server;
            this.client = client;
        }

        public Socket getClient() {
            return client;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), "8859_1"));
                OutputStream out = client.getOutputStream();
                PrintWriter pout = new PrintWriter(new OutputStreamWriter(out, "8859_1"), true);
                String request = in.readLine();
                System.out.println("REQUEST= " +request);

                Matcher get = Pattern.compile("GET /?(\\S*).*").matcher(request);
                if (get.matches()) {
                    request = get.group(1);
                    File result = server.resourcepack;
                    if (result == null) {
                        pout.println("HTTP/1.0 400 Bad Request");
                    } else {
                        try {
                            // Writes zip files specifically; Designed for resource pack hosting
                            out.write("HTTP/1.0 200 OK\r\n".getBytes());
                            out.write("Content-Type: application/zip\r\n".getBytes());
                            out.write(("Content-Length: " + result.length() + "\r\n").getBytes());
                            out.write(("Date: " + new Date().toGMTString() + "\r\n").getBytes());
                            out.write("Server: MineHttpd\r\n\r\n".getBytes());
                            FileInputStream fis = new FileInputStream(result);
                            byte[] data = new byte[64 * 1024];
                            for (int read; (read = fis.read(data)) > -1; ) {
                                out.write(data, 0, read);
                            }
                            out.flush();
                            fis.close();
                            System.out.println("Sent resourcepack.");
                        } catch (FileNotFoundException e) {
                            pout.println("HTTP/1.0 404 Object Not Found");
                        }
                    }
                } else {
                    pout.println("HTTP/1.0 400 Bad Request");
                }
                client.close();
            } catch (IOException e) {
                System.out.println("I/O error " + e);
            }
        }
    }
}
