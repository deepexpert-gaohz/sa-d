package com.ideatech.ams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author wangqingan
 * @company ydrx
 * @date 3/13/2018
 * @description
 */
@Controller
@RequestMapping("/account")
public class AccountController {
  @RequestMapping(value = "detail",method = RequestMethod.GET)
  public String detail(){
    return "account/detail";
  }

  @RequestMapping(value = "account",method = RequestMethod.GET)
  public String account(){
    return "account/account";
  }

  @RequestMapping(value = "contact",method = RequestMethod.GET)
  public String contact(){
    return "account/contact";
  }

  @RequestMapping(value = "legal",method = RequestMethod.GET)
  public String legal(){
    return "account/legal";
  }

  @RequestMapping(value = "org",method = RequestMethod.GET)
  public String org(){
    return "account/org";
  }

  @RequestMapping(value = "other",method = RequestMethod.GET)
  public String other(){
    return "account/other";
  }

  @RequestMapping(value = "/saic",method = RequestMethod.GET)
  public String saic(){
    return "account/saic";
  }

  @RequestMapping(value = "superviser",method = RequestMethod.GET)
  public String superviser(){
    return "account/superviser";
  }

  @RequestMapping(value = "tax",method = RequestMethod.GET)
  public String tax(){
    return "account/tax";
  }

  @RequestMapping(value = "print",method = RequestMethod.GET)
  public String print(){
    return "account/print";
  }

  @RequestMapping(value = "view",method = RequestMethod.GET)
  public String view(){
    return "account/view";
  }
}
