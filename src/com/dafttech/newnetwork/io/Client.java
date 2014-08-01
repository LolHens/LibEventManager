package com.dafttech.newnetwork.io;

import com.dafttech.newnetwork.AbstractClient;
import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;

public class Client<P extends Packet> extends AbstractClient<P> {

    public Client(Protocol<P> protocol) {
        super(protocol);
    }

}