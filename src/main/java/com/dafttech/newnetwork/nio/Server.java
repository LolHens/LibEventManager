package com.dafttech.newnetwork.nio;

import com.dafttech.autoselector.SelectorManager;
import com.dafttech.newnetwork.AbstractClient;
import com.dafttech.newnetwork.AbstractProtocol;
import com.dafttech.newnetwork.AbstractServer;
import com.dafttech.newnetwork.ProtocolProvider;
import com.dafttech.newnetwork.disconnect.DisconnectReason;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public class Server<P> extends AbstractServer<P> {
    protected final ServerSocketChannel socketChannel;

    public Server(Class<? extends AbstractProtocol> protocolClazz, InetSocketAddress socketAddress, BiConsumer<AbstractClient<P>, P> receiveHandler, BiConsumer<ProtocolProvider<P>, DisconnectReason> disconnectHandler) throws IOException {
        super(protocolClazz, receiveHandler, disconnectHandler);

        clients = new CopyOnWriteArrayList<>();

        setExceptionHandler(new ExceptionHandler());

        socketChannel = ServerSocketChannel.open();
        socketChannel.bind(socketAddress);

        SelectorManager.instance.register(socketChannel, SelectionKey.OP_ACCEPT, (selectionKey) -> {
            if (!isAlive()) return;

            if (selectionKey.isAcceptable()) {
                try {
                    AbstractClient<P> client = new Client<P>(protocolClazz, socketChannel.accept(), receiveHandler, disconnectHandler);
                    client.setConnectHandler(getConnectHandler());
                    onAccept(client);
                    clients.add(client);
                } catch (IOException e) {
                    onException(e);
                }
            }
        });
    }

    public Server(Class<? extends AbstractProtocol> protocolClazz, int port, BiConsumer<AbstractClient<P>, P> receiveHandler, BiConsumer<ProtocolProvider<P>, DisconnectReason> disconnectHandler) throws IOException {
        this(protocolClazz, new InetSocketAddress(port), receiveHandler, disconnectHandler);
    }

    @Override
    protected void onClose() throws IOException {
        super.onClose();
        socketChannel.close();
    }
}
