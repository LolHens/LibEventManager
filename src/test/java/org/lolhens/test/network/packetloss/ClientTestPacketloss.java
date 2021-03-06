package org.lolhens.test.network.packetloss;

import org.lolhens.network.nio.Client;
import org.lolhens.network.packet.SimplePacket;
import org.lolhens.network.protocol.SimpleProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ClientTestPacketloss {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Client<SimplePacket> client = new Client<>(SimpleProtocol.class);

        client.setReceiveHandler((c, packet) -> {
            c.send(new SimplePacket(packet.channel + 1, new byte[0]));
            if (packet.channel % 1000 == 0) System.out.println(packet.channel);
            //System.out.println(packet);
        });
        client.setDisconnectHandler((pp, r) -> System.out.println(pp + ": " + r));

        try {
            client.connect(input.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (input != null) {
            String in = input.readLine();
            if (in != null) client.send(new SimplePacket(0, in.getBytes()));
        }
    }
}
