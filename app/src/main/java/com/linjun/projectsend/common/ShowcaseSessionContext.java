package com.linjun.projectsend.common;

/**
 * Created by linjun on 2018/2/24.
 */

public class ShowcaseSessionContext {
    private String token = null;

    private String userid = null;

    /**
     *
     * @author tanyaowu
     */
    public ShowcaseSessionContext() {
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }
}
