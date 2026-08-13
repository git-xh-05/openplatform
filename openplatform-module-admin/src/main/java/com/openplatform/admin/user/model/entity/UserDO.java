package com.openplatform.admin.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.openplatform.common.base.model.entity.BaseDO;
import com.openplatform.common.enums.DisEnableStatusEnum;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class UserDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String nickname;
    private String password;
    private String gender;
    private String email;
    private String phone;
    private String avatar;
    private String description;
    private DisEnableStatusEnum status;
    private Boolean isSystem;
    private LocalDateTime pwdResetTime;
}