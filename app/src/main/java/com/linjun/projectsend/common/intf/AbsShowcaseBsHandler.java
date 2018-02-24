package com.linjun.projectsend.common.intf;

import com.linjun.projectsend.common.Const;
import com.linjun.projectsend.common.ShowcasePacket;
import com.linjun.projectsend.common.packets.BaseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * Created by linjun on 2018/2/24.
 */

public abstract class AbsShowcaseBsHandler<T extends BaseBody> implements ShowcaseBsHandlerIntf {
    private static Logger log = LoggerFactory.getLogger(AbsShowcaseBsHandler.class);

    /**
     *
     * @author tanyaowu
     */
    public AbsShowcaseBsHandler() {
    }

    public abstract Class<T> bodyClass();

    @Override
    public Object handler(ShowcasePacket packet, ChannelContext channelContext) throws Exception {
        String jsonStr = null;
        T bsBody = null;
        if (packet.getBody() != null) {
            jsonStr = new String(packet.getBody(), Const.CHARSET);
            bsBody = Json.toBean(jsonStr, bodyClass());
        }

        return handler(packet, bsBody, channelContext);
    }

    public abstract Object handler(ShowcasePacket packet, T bsBody, ChannelContext channelContext) throws Exception;

}
