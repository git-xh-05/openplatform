package com.openplatform.admin.auth.service;

import com.openplatform.admin.auth.model.req.LoginReq;
import com.openplatform.admin.auth.model.resp.LoginResp;
import com.openplatform.admin.auth.model.resp.RouteResp;
import com.openplatform.admin.auth.model.resp.UserInfoResp;

import java.util.List;

public interface AuthService {

    LoginResp login(LoginReq req);

    void logout();

    UserInfoResp getUserInfo();

    List<RouteResp> buildRouteTree();
}