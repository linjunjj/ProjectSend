package com.linjun.projectsend.handler;

import com.linjun.projectsend.common.ShowcaseAbsAioHandler;
import com.linjun.projectsend.common.ShowcasePacket;
import com.linjun.projectsend.common.Type;
import com.linjun.projectsend.common.intf.AbsShowcaseBsHandler;

import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linjun on 2018/2/24.
 */

public class ShowcaseClientAioHandler extends ShowcaseAbsAioHandler implements ClientAioHandler {

    private static Map<Byte, AbsShowcaseBsHandler<?>> handlerMap = new HashMap<>();
    static {
        handlerMap.put(Type.LOGIN_RESP, new LoginRespHandler());
    }

    private static ShowcasePacket heartbeatPacket = new ShowcasePacket(Type.HEART_BEAT_REQ, null);

    /**
     * 处理消息
     */
    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        ShowcasePacket showcasePacket = (ShowcasePacket) packet;
        Byte type = showcasePacket.getType();
        AbsShowcaseBsHandler<?> showcaseBsHandler = handlerMap.get(type);
        showcaseBsHandler.handler(showcasePacket, channelContext);
        return;
    }

    /**
     * 此方法如果返回null，框架层面则不会发心跳；如果返回非null，框架层面会定时发本方法返回的消息包
     */
    @Override
    public ShowcasePacket heartbeatPacket() {
        return heartbeatPacket;
    }
}
