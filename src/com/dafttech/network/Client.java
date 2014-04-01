package com.dafttech.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private volatile Socket socket;
    private ClientThread thread;

    public Client(String host, int port) throws UnknownHostException, IOException {
        this(InetAddress.getByName(host), port);
    }

    public Client(InetAddress address, int port) throws IOException {
        this(new Socket(address, port));
    }

    public Client(Socket socket) {
        this.socket = socket;
        thread = new ClientThread();
        thread.start();
    }

    public final void close() {
        thread.closed = true;
    }

    public final void send(byte[] data) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(BigInteger.valueOf(data.length).toByteArray(), 0, 4);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive(byte[] data) {

    }

    public final InputStream getInputStream() {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final OutputStream getOutputStream() {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class ClientThread extends Thread {
        private volatile boolean closed;

        @Override
        public void run() {
            while (!closed) {
                try {
                    InputStream inputStream = socket.getInputStream();
                    byte[] size = new byte[4];
                    inputStream.read(size);
                    byte[] data = new byte[new BigInteger(size).intValue()];
                    inputStream.read(data);
                    receive(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}