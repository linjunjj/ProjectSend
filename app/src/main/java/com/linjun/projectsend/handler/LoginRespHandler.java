package com.linjun.projectsend.handler;

import com.linjun.projectsend.common.ShowcasePacket;
import com.linjun.projectsend.common.ShowcaseSessionContext;
import com.linjun.projectsend.common.intf.AbsShowcaseBsHandler;
import com.linjun.projectsend.common.packets.LoginRespBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * Created by linjun on 2018/2/24.
 */
public  class LoginRespHandler extends AbsShowcaseBsHandler<LoginRespBody> {


    private static Logger log = LoggerFactory.getLogger(LoginRespHandler.class);

    /**
     * @param args
     * @author tanyaowu
     */
    public static void main(String[] args) {

    }

    /**
     *
     * @author tanyaowu
     */
    public LoginRespHandler() {
    }

    /**
     * @return
     * @author tanyaowu
     */
    @Override
    public Class<LoginRespBody> bodyClass() {
        return LoginRespBody.class;
    }

    /**
     * @param packet
     * @param bsBody
     * @param channelContext
     * @return
     * @throws Exception
     * @author tanyaowu
     */
    static LoginRespBody loginRespBody=null;

    @Override
    public Object handler(ShowcasePacket packet, LoginRespBody bsBody, ChannelContext channelContext) throws Exception {
        System.out.println("收到登录响应消息:" + Json.toJson(bsBody));
        if (LoginRespBody.Code.SUCCESS.equals(bsBody.getCode())) {
            ShowcaseSessionContext showcaseSessionContext = (ShowcaseSessionContext) channelContext.getAttribute();
            showcaseSessionContext.setToken(bsBody.getToken());
            System.out.println("登录成功，token是:" + bsBody.getToken());
        }
        loginRespBody=bsBody;

        return null;
    }


    public  static  LoginRespBody getBody(){
        return loginRespBody;
    }

}
