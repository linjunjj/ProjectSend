package com.linjun.projectsend.common.intf;

import com.linjun.projectsend.common.ShowcasePacket;

import org.tio.core.ChannelContext;

/**
 * Created by linjun on 2018/2/24.
 */

public interface ShowcaseBsHandlerIntf {
    public Object handler(ShowcasePacket packet, ChannelContext channelContext) throws Exception;
}
