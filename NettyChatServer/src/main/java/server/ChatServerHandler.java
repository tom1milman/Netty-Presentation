package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends ChannelInboundHandlerAdapter {
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incomingChannel = ctx.channel();

        String outgoingMsg = String.format("[SERVER] you have joined the chat as %s", incomingChannel.remoteAddress());
        String outgoingMsgAll = String.format("[SERVER] %s has joined", incomingChannel.remoteAddress());

        printToServerConsole(outgoingMsg);
        sendOutMessage(ctx.channel(), outgoingMsg);
        broadcastMessage(ctx.channel(), outgoingMsgAll);
        channels.add(incomingChannel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;

        StringBuilder msgStr = new StringBuilder();
        msgStr.append("[");
        msgStr.append(ctx.channel().remoteAddress());
        msgStr.append("] ");

        try {
            while(in.isReadable()) {
                msgStr.append((char) in.readByte());
            }
        }
        finally {
            ReferenceCountUtil.release(msg);
        }

        String finalMsg = msgStr.toString();
        printToServerConsole(finalMsg);

        broadcastMessage(ctx.channel(), finalMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }


    ////
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incomingChannel = ctx.channel();
        channels.remove(incomingChannel);

        String outgoingMsg = String.format("[SERVER] %s has left", incomingChannel.remoteAddress());

        printToServerConsole(outgoingMsg);
        broadcastMessage(ctx.channel(), outgoingMsg);
    }

    private void broadcastMessage(Channel incomingChannel, String msg) {
        for (Channel channel : channels) {
            if (channel != incomingChannel)
                sendOutMessage(channel, msg);
        }
    }

    private void sendOutMessage(Channel channel, String msg) {
        final ByteBuf buf = channel.alloc().buffer(msg.length());

        buf.writeBytes(msg.getBytes());

        final ChannelFuture channelFuture = channel.writeAndFlush(buf);
    }

    private void printToServerConsole(String msg) {
        System.out.println(msg);
    }
}
