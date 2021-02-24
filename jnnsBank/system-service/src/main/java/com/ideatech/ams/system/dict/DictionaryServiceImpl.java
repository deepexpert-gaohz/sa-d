package com.ideatech.ams.system.dict;

import com.ideatech.ams.system.dict.dao.DictionaryDao;
import com.ideatech.ams.system.dict.dao.OptionDao;
import com.ideatech.ams.system.dict.dto.DictionaryDto;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.entity.DictionaryPo;
import com.ideatech.ams.system.dict.entity.OptionPo;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.common.converter.ConverterService;

import com.ideatech.common.util.BeanCopierUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author liangding
 * @create 2018-05-24 下午5:03
 **/
@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryDao dictionaryDao;

    @Autowired
    private OptionDao optionDao;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void save(DictionaryDto dictionaryDto) {
        DictionaryPo dictionaryPo = new DictionaryPo();
        if (dictionaryDto.getId() != null) {
            dictionaryPo = dictionaryDao.findOne(dictionaryDto.getId());
            if (dictionaryPo == null) {
                dictionaryPo = new DictionaryPo();
            }
        }
        ConverterService.convert(dictionaryDto, dictionaryPo);
        dictionaryDao.save(dictionaryPo);
        if(dictionaryDto.getOptions() != null) {
            for (OptionDto optionDto : dictionaryDto.getOptions()) {
                OptionPo optionPo = new OptionPo();
                if (optionDto.getId() != null) {
                    optionPo = optionDao.findOne(optionDto.getId());
                    if (optionPo == null) {
                        optionPo = new OptionPo();
                    }
                }
                ConverterService.convert(optionDto, optionPo);
                optionPo.setDictionaryId(dictionaryPo.getId());
                optionDao.save(optionPo);
            }
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteDictionary(Long id) {
        dictionaryDao.delete(id);
        List<OptionPo> options = optionDao.findByDictionaryIdOrderByName(id);
        optionDao.delete(options);
    }

    @Override
    public List<DictionaryDto> listWithoutCascade() {
        List<DictionaryPo> all = dictionaryDao.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "name")));
        return ConverterService.convertToList(all, DictionaryDto.class);
    }

    @Override
    public List<OptionDto> listOptionByDictId(Long id) {
        List<OptionPo> byDictionaryId = optionDao.findByDictionaryIdOrderByName(id);
        return ConverterService.convertToList(byDictionaryId, OptionDto.class);
    }

    @Override
    public List<OptionDto> findOptionsByDictionaryName(String name) {
        List<OptionPo> opList = new ArrayList<>();
        DictionaryPo dictionaryPo = dictionaryDao.findByName(name);
        if (dictionaryPo != null) {
            opList = optionDao.findByDictionaryIdOrderByName(dictionaryPo.getId());
        }
        List<OptionDto> odList = new ArrayList<>();
        for (OptionPo op : opList) {
            OptionDto od = new OptionDto();
            BeanCopierUtils.copyProperties(op, od);
            odList.add(od);
        }
        return odList;
    }

    @Override
    public List<OptionDto> findOptionsByDictionaryNameStartWith(String name) {
        List<OptionPo> opList = new ArrayList<>();
        List<Long> dictionaryIdList = (List<Long>) CollectionUtils.collect(dictionaryDao.findByNameStartingWith(name), new Transformer() {
            @Override
            public Long transform(Object dictionary) {
                return ((DictionaryPo) dictionary).getId();
            }
        });
        if (dictionaryIdList.size() > 0) {
            opList = optionDao.findByDictionaryIdInOrderByName(dictionaryIdList);
        }
        List<OptionDto> odList = new ArrayList<>();
        Set<String> nameSet = new HashSet<>();
        for (OptionPo op : opList) {
            if (!nameSet.contains(op.getName())) {//去重复并转换泛型
                nameSet.add(op.getName());
                OptionDto od = new OptionDto();
                BeanCopierUtils.copyProperties(op, od);
                odList.add(od);
            }
        }
        return odList;
    }

    @Override
    public void saveOption(OptionDto optionDto) {
        OptionPo optionPo = new OptionPo();
        if (optionDto.getId() != null) {
            optionPo = optionDao.findOne(optionDto.getId());
            if (optionPo == null) {
                optionPo = new OptionPo();
            }
        }
        ConverterService.convert(optionDto, optionPo);
        optionDao.save(optionPo);
    }

    @Override
    public void deleteOption(Long optionId) {
        optionDao.delete(optionId);
    }

    @Override
    public OptionDto findOptionById(Long optionId) {
        OptionPo one = optionDao.findOne(optionId);
        return ConverterService.convert(one, OptionDto.class);
    }

    @Override
    public String transalte(String dictionaryName, String optionName) {
        DictionaryPo dictionaryPo = dictionaryDao.findByName(dictionaryName);
        if (dictionaryPo == null) {
            return optionName;
        }
        OptionPo optionPo = optionDao.findByDictionaryIdAndName(dictionaryPo.getId(), optionName);
        if(optionPo == null) {
            return optionName;
        }
        return optionPo.getValue();
    }

    @Override
    public String transalteNull2Empty(String dictionaryName, String optionName) {
        DictionaryPo dictionaryPo = dictionaryDao.findByName(dictionaryName);
        if (dictionaryPo == null) {
            return optionName;
        }
        //先查看
        OptionPo optionPo = optionDao.findByDictionaryIdAndName(dictionaryPo.getId(), optionName);
        if (optionPo == null) {
            //如果为空，再次查询是否已经转换过，如果转换过则直接返回原值
            if (optionDao.findByDictionaryIdAndValue(dictionaryPo.getId(), optionName) != null) {
                return optionName;
            } else {
                return "";
            }
        }
        return optionPo.getValue();
    }

    @Override
    public String transalteLike(String dictionaryName, String optionName) {

        DictionaryPo dictionaryPo = dictionaryDao.findByName(dictionaryName);
        if (dictionaryPo == null) {
            return optionName;
        }
        List<OptionPo> optionPos = optionDao.findByDictionaryIdAndNameEndingWith(dictionaryPo.getId(), optionName);
        if(CollectionUtils.isEmpty(optionPos)){
            return optionName;
        }
//        if(optionPo == null) {
//            return optionName;
//        }
        return optionPos.get(0).getValue();
    }

    @Override
    public DictionaryDto findDictionaryById(Long id) {
        return ConverterService.convert(dictionaryDao.findOne(id), DictionaryDto.class);
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<DictionaryDto> getDictionaryLikeName(String name) {
		return (List<DictionaryDto>) CollectionUtils.collect(dictionaryDao.findByNameStartingWith(name), new Transformer() {
			@Override
			public Object transform(Object dictionary) {
				DictionaryDto dictionaryDto = new DictionaryDto();
				BeanUtils.copyProperties(dictionary, dictionaryDto);
				return dictionaryDto;
			}
		});
	}
	
	@Override
	public List<OptionDto> listOptionByDictName(String name){
		DictionaryPo dictionaryPo = dictionaryDao.findByName(name);
		if(dictionaryPo != null) {
			return listOptionByDictId(dictionaryPo.getId());
		}else {
			return null;
		}
	}

    @Override
    public String getNameByValue(String dictionaryName, String optionValue) {
        DictionaryPo dictionaryPo = dictionaryDao.findByName(dictionaryName);
        if (dictionaryPo == null) {
            return optionValue;
        }
        OptionPo optionPo = optionDao.findByDictionaryIdAndValue(dictionaryPo.getId(),optionValue);
        if(optionPo==null){
            return optionValue;
        }
        return optionPo.getName();
    }
}
