package com.ideatech.ams.permissions;

import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class IdeaAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private PasswordEncoder passwordEncoder;
//	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	//账管登录密码校验
	@Value("${amsLoginPwdRule.enabled:false}")
	private Boolean amsLoginPwdRuleEnabled;

	//是否启用用户锁定功能
	@Value("${amsLoginLocked.enabled:false}")
	private Boolean amsLoginLockedEnabled;

	//用户锁定时间设置
	@Value("${amsLoginLocked.lockedTime}")
	private int amsLoginLockedLockedTime;

	static Map map;

	private SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		try {
			username = desEncrypt(username, "ideatech20180223", "ideatech20180223");
			password = desEncrypt(password, "ideatech20180223", "ideatech20180223");
		} catch (Exception e) {
			throw new UsernameNotFoundException("用户名或密码不正确");
		}
		UserDto userDto = userService.findByUsername(username);
		if (null == userDto) {
			throw new BadCredentialsException("用户不存在");
		}

		if(!userDto.getEnabled()){
			throw new BadCredentialsException("用户已禁用");
		}

		log.info("是否启用用户上锁机制:{}",amsLoginLockedEnabled);
		log.info("上锁时间配置:{}分钟",amsLoginLockedLockedTime);
		//是否启用用户上锁机制
		if(amsLoginLockedEnabled){
			//1:上锁状态
			if(StringUtils.isNotBlank(userDto.getLockedStatus()) && StringUtils.equals("1",userDto.getLockedStatus())){
				int minutes = 0;
				try{
					Date nowDate = new Date();
					Date lockDate = simpleFormat.parse(userDto.getLockedTime());
					long now = nowDate.getTime();
					long lock = lockDate.getTime();
					minutes = (int) ((now - lock) / (1000 * 60));
				}catch (Exception e){
					log.info("时间转换错误！",e);
				}
				log.info("用户锁定时间为{}分钟",amsLoginLockedLockedTime);
				log.info("用户已锁定{}分钟",minutes);
				if(minutes >= amsLoginLockedLockedTime){
					log.info("用户锁定时间解除,重置状态");
					userDto.setLockedTime("");
					userDto.setLockedStatus("");
					userDto.setPwFailCount(0);
					userService.save(userDto);
				}else{
					throw new BadCredentialsException("该用户为锁定状态,请等待" + (amsLoginLockedLockedTime - minutes) + "分钟！");
				}
			}
		}


		ConfigDto pwdExpireCheckConfig = configService.findOneByConfigKey("pwdExpireCheck");
		ConfigDto pwdExpireDayConfig = configService.findOneByConfigKey("pwdExpireDay");
		if(pwdExpireCheckConfig != null && pwdExpireDayConfig != null) {
			String pwdExpireDay = "";
			Boolean flag = false;
			if("true".equals(pwdExpireCheckConfig.getConfigValue())) { //启用密码有效期控制
				if (!"virtual".equals(userDto.getUsername()) && !"admin".equals(userDto.getUsername())) {
					pwdExpireDay = pwdExpireDayConfig.getConfigValue();
					if (userDto.getPwdUpdateDate() != null) {
						flag = DateUtils.isMinuteAfter(DateUtils.dayAfter(userDto.getPwdUpdateDate(), Long.parseLong(pwdExpireDay)));
						if(flag) {
							throw new BadCredentialsException("密码超过有效期");
						}
					}
				}
			}
		}

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		SecurityUtils.UserInfo user = new SecurityUtils.UserInfo(username, password, authorities);

		if(userDto.getOrgId() != null) {
			OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
			user.setId(userDto.getId());
			user.setOrgFullId(organizationDto.getFullId());
			user.setOrgId(organizationDto.getId());
			user.setRoleId(userDto.getRoleId());
		} else {
			throw new BadCredentialsException("用户不属于任何机构，无法登陆");
		}
		// 加密过程在这里体现
		if (!passwordEncoder.matches(password.trim(), userDto.getPassword())) {

			//根据配置是否增加用户锁定功能
			//用户输入错误密码4次  锁定半小时  时间可以配置文件进行配置
			if(amsLoginLockedEnabled){
				if(userDto.getPwFailCount() == null){
					userDto.setPwFailCount(0);
				}
				userDto.setPwFailCount(userDto.getPwFailCount() + 1);
				if(userDto.getPwFailCount() == 4){
					log.info("用户密码错误次数打到上限，进行锁定");
					userDto.setLockedStatus("1");
					userDto.setLockedTime(DateUtils.DateToStr(new Date(),"yyyy-MM-dd HH:mm"));
					userService.save(userDto);
					throw new BadCredentialsException("密码输入错误次数已达上限，请等待" + amsLoginLockedLockedTime + "分钟后重试");
				}
				userService.save(userDto);
				throw new BadCredentialsException("密码错误,当前还有" + map.get(userDto.getPwFailCount()) + "次输入机会！");
			}
			throw new BadCredentialsException("密码错误");
		}

		//如果是启用的密码校验机制 初始密码提示修改
		if(amsLoginPwdRuleEnabled){
			//初始密码为123456，增加密码校验机制后Aa123456
			if(StringUtils.equals("123456",password) || StringUtils.equals("Aa123456",password)){
				throw new BadCredentialsException("当前登录密码为初始密码，请修改！");
			}
		}

		//走到这一步说明是密码正确的  启用了用户锁定功能在这里进行重置
		if(amsLoginLockedEnabled){
			log.info("启动用户锁定功能，密码输入正确，重置状态");
			userDto.setLockedTime("");
			userDto.setLockedStatus("");
			userDto.setPwFailCount(0);
			userService.save(userDto);
		}
//		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		return new UsernamePasswordAuthenticationToken(user, password, authorities);
	}
	public static String desEncrypt(String data, String key, String iv) throws Exception {
        try {
            byte[] encrypted1 = new Base64().decode(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            return null;
        }
    }
	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	static {
		map = new HashMap();
		map.put(1,3);
		map.put(2,2);
		map.put(3,1);
	}

}
