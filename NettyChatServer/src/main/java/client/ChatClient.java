/*
  @author Tom Milman
 * Distributed Systems Spring 2020
 * Netty presentation
 */

package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient {
    public static void main(String[] args) {
        int port = 8080;
        String host = "localhost";

        switch (args.length){
            case 1:
                port = Integer.parseInt(args[0]);
                new ChatClient(host, port).run();
                break;
            case 2:
                host = args[0];
                port = Integer.parseInt(args[1]);
                new ChatClient(host, port).run();
                break;
            default:
                new ChatClient(host, port).run();
                break;
        }
    }

    private final String host;
    private final int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run () {
        System.out.printf("Trying to connect to %s %d...\n", host, port);
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChatClientInitializer());

        Channel channel = null;

        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port);
            channel = channelFuture.sync().channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String input = in.readLine();

                ByteBuf buf = channel.alloc().buffer(input.length());
                buf.writeBytes(input.getBytes());
                channel.writeAndFlush(buf);

            }
        } catch (Exception e) {
            String str = e.getCause().toString();
            if (str.contains("Connection refused"))
                System.out.printf("-- Couldn't connect to %s %d --\n", host, port);
            else
                System.out.println("-- You were disconnected from server --");

            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
