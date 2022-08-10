package wang.bottom.community.entity;

import lombok.Data;

@Data
public class AccessTokenParam {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
    // 省略getter和setter方法
}
