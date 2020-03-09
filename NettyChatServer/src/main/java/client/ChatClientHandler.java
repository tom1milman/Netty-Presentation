/*
  @author Tom Milman
 * Distributed Systems Spring 2020
 * Netty presentation
 */

package client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChatClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf in = (ByteBuf) msg;
        try {
            while(in.isReadable()) {
                System.out.print((char) in.readByte());
                System.out.flush();
            }
            System.out.println();
        }
        finally{
            in.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        System.out.println("-- Server was closed --");
    }
}
