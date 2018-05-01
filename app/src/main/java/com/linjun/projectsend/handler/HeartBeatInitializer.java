package com.linjun.projectsend.handler;

import android.os.Handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class HeartBeatInitializer extends ChannelInitializer<SocketChannel> {
    final Handler handler;

    public HeartBeatInitializer(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
//
////        //pipeline.addLast("framer", new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()));
////        pipeline.addLast("framer", new LineBasedFrameDecoder(Integer.MAX_VALUE));
////        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
////        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
////        //pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
////
//        pipeline.addLast("handler", new LetsChatHandler(handler));
//
////        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
////        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
//        pipeline.addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE));
//
//        pipeline.addLast(new ByteArrayEncoder());
//        pipeline.addLast(new ByteArrayDecoder());
//        //pipeline.addLast(new ChunkedWriteHandler());//2

//        pipeline.addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE));
//        pipeline.addLast(new ByteArrayDecoder());
//        pipeline.addLast(new ByteArrayEncoder());
//
//        pipeline.addLast(new StringEncoder());
//        pipeline.addLast(new StringDecoder());

//        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,2,0,2));
//        pipeline.addLast(new LengthFieldPrepender(2));

        ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));
        ch.pipeline().addLast(new ObjectEncoder());

        pipeline.addLast(new HeartBeatHandler(handler));
    }
}
