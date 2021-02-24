/**
 *
 */
package com.ideatech.ams.controller;

import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhailiang
 */
@RestController
public class SystemController {
    /**
     * 登录失败的返回
     *
     * @return
     */
    @RequestMapping("/login/fail")
    public ResultDto loginFail() {
        return ResultDtoFactory.toValidationError("验证码错误或者超时,请重新刷新验证码", null);
    }

}
