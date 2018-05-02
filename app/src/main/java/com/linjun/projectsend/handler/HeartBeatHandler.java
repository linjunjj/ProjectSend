package com.linjun.projectsend.handler;

import android.os.Handler;
import android.os.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HeartBeatHandler extends SimpleChannelInboundHandler<String> {
    final Handler handler;
    public HeartBeatHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Message message = handler.obtainMessage(1);
        message.obj = "服务端:" + s+"\n";
        message.sendToTarget();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Message message = handler.obtainMessage(1);
        message.obj = "当前链路被连接\n";
        message.sendToTarget();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Message message = handler.obtainMessage(1);
        message.obj = "当前链路被关闭\n";
        message.sendToTarget();
        super.channelInactive(ctx);
    }
}
