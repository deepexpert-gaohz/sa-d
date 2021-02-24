package com.ideatech.ams.system.pbc.service;

import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dao.PbcAccountDao;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.entity.PbcAccountPo;
import com.ideatech.ams.system.pbc.enums.EAccountStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.sm4.service.EncryptPwdService;
import com.ideatech.common.converter.ConverterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author liangding
 * @create 2018-05-17 上午12:04
 **/
@Service
@Transactional
@Slf4j
public class PbcAccountServiceImpl implements PbcAccountService {
    @Autowired
    private PbcAccountDao pbcAccountDao;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EncryptPwdService encryptPwdService;

    @Override
    public void save(PbcAccountDto pbcAccountDto) {
        PbcAccountPo pbcAccountPo = new PbcAccountPo();
        if (pbcAccountDto.getId() != null) {
            pbcAccountPo = pbcAccountDao.findOne(pbcAccountDto.getId());
            if (pbcAccountPo == null) {
                pbcAccountPo = new PbcAccountPo();
            }
        } else {
            pbcAccountPo = pbcAccountDao.findByOrgIdAndAccountTypeAndAccount(pbcAccountDto.getOrgId(), pbcAccountDto.getAccountType(), pbcAccountDto.getAccount());
            if (pbcAccountPo == null) {
                pbcAccountPo = new PbcAccountPo();
            } else {
                pbcAccountDto.setId(pbcAccountPo.getId());
            }
        }
        ConverterService.convert(pbcAccountDto, pbcAccountPo);
        pbcAccountDao.save(pbcAccountPo);
        //根据IP和保存类型,账号找出保存在数据库中相同账号的
        List<PbcAccountPo> accountPos = pbcAccountDao.findByIpAndAccountTypeAndAccount(pbcAccountDto.getIp(),pbcAccountDto.getAccountType(),pbcAccountDto.getAccount());
        for(PbcAccountPo pbcAccount : accountPos){
            pbcAccount.setPassword(pbcAccountDto.getPassword());
            pbcAccountDao.save(pbcAccount);
        }
    }

    @Override
    public void uploadSave(PbcAccountDto pbcAccountDto) {
        PbcAccountPo pbcAccountPo = pbcAccountDao.findByOrgIdAndAccountTypeAndAccount(pbcAccountDto.getOrgId(), pbcAccountDto.getAccountType(), pbcAccountDto.getAccount());
        if (pbcAccountPo == null) {
            pbcAccountPo = new PbcAccountPo();
        } else {
            pbcAccountDto.setId(pbcAccountPo.getId());
            pbcAccountDto.setEnabled(pbcAccountPo.getEnabled());
        }
        if (pbcAccountPo.getPassword() != null && pbcAccountPo.getPassword().equals(pbcAccountDto.getPassword())
                && pbcAccountPo.getIp() != null && pbcAccountPo.getIp().equals(pbcAccountDto.getIp())) { //账号密码相同 并且 ip相同，则不修改验证状态
            pbcAccountDto.setAccountStatus(pbcAccountPo.getAccountStatus());
        }
        ConverterService.convert(pbcAccountDto, pbcAccountPo);
        pbcAccountDao.save(pbcAccountPo);
    }

    @Override
    public List<PbcAccountDto> listByOrgId(Long orgId) {
        List<PbcAccountPo> byOrOrgId = pbcAccountDao.findByOrOrgId(orgId);
        List<PbcAccountDto> pbcAccountDtos = ConverterService.convertToList(byOrOrgId, PbcAccountDto.class);

        return pbcAccountsDecrypt(pbcAccountDtos);
    }

    @Override
    public void delete(Long id) {
        pbcAccountDao.delete(id);
    }

    @Override
    public List<PbcAccountDto> listByOrgIdAndType(Long orgId, EAccountType accountType) {
        List<PbcAccountPo> byOrgIdAndAccountType = pbcAccountDao.findByOrgIdAndAccountType(orgId, accountType);
        List<PbcAccountDto> pbcAccountDtos = ConverterService.convertToList(byOrgIdAndAccountType, PbcAccountDto.class);

        return pbcAccountsDecrypt(pbcAccountDtos);
    }

    @Override
    public List<PbcAccountDto> listByOrgCodeAndType(String orgCode, EAccountType accountType) {
        OrganizationDto organizationDto = organizationService.findByCode(orgCode);
        if (organizationDto != null) {
            return listByOrgIdAndType(organizationDto.getId(), accountType);
        } else {
            return null;
        }
    }

    @Override
    public PbcAccountDto getAnnualPbcAccountByOrganId(Long organId, EAccountType accountType) {
        List<PbcAccountDto> pbcAccountDtoList = listByOrgIdAndType(organId, accountType);
        if (CollectionUtils.isNotEmpty(pbcAccountDtoList)) {
            for (PbcAccountDto pbcAccountDto : pbcAccountDtoList) {
                boolean isAms2Level = (accountType == EAccountType.AMS && pbcAccountDto.getAccount().startsWith("2"));
                if (isAms2Level) {
                    return pbcAccountDto;
                }

            }
        }
        return null;
    }

    @Override
    public PbcAccountDto getPbcAccountByOrganId(Long organId, EAccountType accountType) {
        List<PbcAccountDto> pbcAccountDtoList = listByOrgIdAndType(organId, accountType);
        if (CollectionUtils.isNotEmpty(pbcAccountDtoList)) {
            for (PbcAccountDto pbcAccountDto : pbcAccountDtoList) {
                if (pbcAccountDto.getAccountStatus() == EAccountStatus.VALID && pbcAccountDto.getEnabled()) {
                    boolean isAms2LevelOrEccs = (accountType == EAccountType.AMS && pbcAccountDto.getAccount().startsWith("2")) || accountType == EAccountType.ECCS;
                    if (isAms2LevelOrEccs) {
                        return pbcAccountDto;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public PbcAccountDto getPbcAccountByOrganCode(String organCode, EAccountType accountType) {
        OrganizationDto organizationDto = organizationService.findByCode(organCode);
        if (organizationDto != null) {
            return getPbcAccountByOrganId(organizationDto.getId(), accountType);
        } else {
            return null;
        }
    }

    @Override
    public PbcAccountDto getPbcAccountByOrganFullId(String organFullId, EAccountType accountType) {
        OrganizationDto organizationDto = organizationService.findByOrganFullId(organFullId);
        if (organizationDto != null) {
            return getPbcAccountByOrganId(organizationDto.getId(), accountType);
        } else {
            return null;
        }
    }

    @Override
    public PbcAccountDto getPbcAccountByOrganFullIdByCancelHeZhun(String organFullId, EAccountType accountType) {
        OrganizationDto organizationDto = organizationService.findByOrganFullId(organFullId);
        if (organizationDto != null) {
            return getPbcAccountByOrganIdByCancelHeZhun(organizationDto.getId(), accountType);
        } else {
            return null;
        }
    }

    @Override
    public PbcAccountDto getPbcAccountByOrganCodeByCancelHeZhun(String organCode, EAccountType accountType) {
        OrganizationDto organizationDto = organizationService.findByCode(organCode);
        if (organizationDto != null) {
            return getPbcAccountByOrganIdByCancelHeZhun(organizationDto.getId(), accountType);
        } else {
            return null;
        }
    }

    @Override
    public PbcAccountDto getById(Long id) {
        PbcAccountPo one = pbcAccountDao.findOne(id);
        PbcAccountDto dto = ConverterService.convert(one, PbcAccountDto.class);

        try {
            decryptEcbPwd(dto);
        } catch (Exception e) {
            log.error("方法getById根据id获取PbcAccountDto后SM4密码解密出错", e);
        }
        return dto;
    }

    @Override
    public void enable(Long id) {
        PbcAccountPo one = pbcAccountDao.findOne(id);
        one.setEnabled(Boolean.TRUE);
        pbcAccountDao.save(one);
    }

    @Override
    public void disable(Long id) {
        PbcAccountPo one = pbcAccountDao.findOne(id);
        one.setEnabled(Boolean.FALSE);
        pbcAccountDao.save(one);
    }

    @Override
    public List<PbcAccountDto> listByType(EAccountType type) {
        List<PbcAccountPo> byAccountType = pbcAccountDao.findByAccountType(type);
        List<PbcAccountDto> pbcAccountDtos = ConverterService.convertToList(byAccountType, PbcAccountDto.class);

        return pbcAccountsDecrypt(pbcAccountDtos);
    }

    @Override
    public List<PbcAccountDto> listAllNewAccount() {
        List<PbcAccountPo> byAccountStatusAndEnabled = pbcAccountDao.findByAccountStatusAndEnabled(EAccountStatus.NEW, Boolean.TRUE);
        List<PbcAccountDto> pbcAccountDtos = ConverterService.convertToList(byAccountStatusAndEnabled, PbcAccountDto.class);

//        return pbcAccountsDecrypt(pbcAccountDtos);
        //这个方法只是在定时任务中会用到，不需要返回解密的密码，解密后面的代码逻辑中去处理
        return pbcAccountDtos;
    }

    @Override
    public PbcAccountDto getAdjacentByCode(String organCode, EAccountType accountType) {
        OrganizationDto organ = organizationService.findByCode(organCode);
        if (organ.getParentId() != null) {
            Long parentId = organ.getParentId();
            //父节点不为空则寻找相同父节点的相邻机构
            if (parentId == -1) {
                parentId = organ.getId();
            }
            List<Long> adjacentOrgans = new LinkedList<>();
            List<OrganizationDto> organList = organizationService.searchChild(parentId, null);
            for (OrganizationDto dto : organList) {
                if (StringUtils.isNotBlank(dto.getPbcCode())) {
                    adjacentOrgans.add(dto.getId());
                }
            }
            if (CollectionUtils.isNotEmpty(adjacentOrgans)) {
                //随机取一个机构
                Collections.shuffle(adjacentOrgans);
                for (Long adjacentOrgan : adjacentOrgans) {
                    List<PbcAccountPo> pbcAccountPos = pbcAccountDao.findByOrgIdAndAccountType(adjacentOrgan, accountType);
                    for (PbcAccountPo pbcAccountPo : pbcAccountPos) {
                        if (pbcAccountPo != null && pbcAccountPo.getEnabled() && pbcAccountPo.getAccountStatus() == EAccountStatus.VALID) {
                            PbcAccountDto pbcAccountDto = ConverterService.convert(pbcAccountPo, PbcAccountDto.class);
                            try {
                                return decryptEcbPwd(pbcAccountDto);
                            } catch (Exception e) {
                                log.error("getAdjacentByCode方法SM4解密异常", e);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<PbcAccountDto> listByIpAndAccountType(EAccountType accountType) {
        PbcAccountDto dto = null;
        List<PbcAccountDto> list = new ArrayList<>();
        List<PbcAccountPo> pbcAccounts = pbcAccountDao.findByAccountTypeAndAccountStatusAndEnabledAndDeletedAndAccountStartsWith(
                accountType, EAccountStatus.VALID, true, false, "2");

        return ConverterService.convertToList(pbcAccounts, PbcAccountDto.class);
    }

    @Override
    public List<PbcAccountDto> listByIp(String ip) {
        List<PbcAccountPo> byIp = pbcAccountDao.findByIp(ip);
        return ConverterService.convertToList(byIp, PbcAccountDto.class);
    }

    public PbcAccountDto getPbcAccountByOrganIdByCancelHeZhun(Long organId, EAccountType accountType) {
        List<PbcAccountDto> pbcAccountDtoList = listByOrgIdAndType(organId, accountType);
        if (CollectionUtils.isNotEmpty(pbcAccountDtoList)) {
            for (PbcAccountDto pbcAccountDto : pbcAccountDtoList) {
                if (pbcAccountDto.getAccount().startsWith("4") && pbcAccountDto.getEnabled() && pbcAccountDto.getAccountStatus() == EAccountStatus.VALID) {
                    return pbcAccountDto;
                }
            }
        }
        return null;
    }

    private List<PbcAccountDto> pbcAccountsDecrypt(List<PbcAccountDto> pbcAccountDtos) {
        if(CollectionUtils.isNotEmpty(pbcAccountDtos)) {
            for(PbcAccountDto dto : pbcAccountDtos) {
                try {
                    decryptEcbPwd(dto);
                } catch (Exception e) {
                    log.error("获取人行账户列表SM4解密出错", e);
                }
            }
        }

        return pbcAccountDtos;
    }

    /**
     * sm4解密封装
     * @param pbcAccountDto
     * @throws Exception
     */
    private PbcAccountDto decryptEcbPwd(PbcAccountDto pbcAccountDto) throws Exception {
        pbcAccountDto.setPassword(encryptPwdService.decryptEcbPwd(pbcAccountDto.getPassword()));
        return pbcAccountDto;
    }
}
