package com.openplatform.admin.auth.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Data
@Schema(description = "用户信息")
public class UserInfoResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "权限码集合")
    private Set<String> permissions;

    @Schema(description = "角色编码集合")
    private Set<String> roles;

    @Schema(description = "密码修改时间")
    private String pwdResetTime;
}