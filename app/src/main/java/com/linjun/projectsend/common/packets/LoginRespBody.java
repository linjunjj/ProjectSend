package com.linjun.projectsend.common.packets;

/**
 * Created by linjun on 2018/2/24.
 */

public class LoginRespBody extends BaseBody {
    public static interface Code {
        Integer SUCCESS = 1;
        Integer FAIL = 2;
    }
    private String token;
    private Integer code;

    private String msg;

    /**
     *
     * @author tanyaowu
     */
    public LoginRespBody() {

    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
}
