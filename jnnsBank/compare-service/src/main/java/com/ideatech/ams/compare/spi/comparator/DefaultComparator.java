/**
 *
 */
package com.ideatech.ams.compare.spi.comparator;

import com.ideatech.ams.compare.dto.CompareDefineDto;
import com.ideatech.ams.compare.dto.CompareFieldDto;
import com.ideatech.ams.compare.dto.CompareRuleFieldsDto;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailsCell;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailsField;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.enums.CompareFieldEnum;
import com.ideatech.common.util.BeanValueUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 比对默认实现
 */
@Slf4j
public class DefaultComparator extends AbstractComparator {

    @Override
    protected CompareResultDetailsField doCompare(List<CompareDefineDto> define, CompareRuleFieldsDto compareRuleFieldsDto, Map<Long, CompareDataDto> compareDatas) {
        CompareResultDetailsField result = new CompareResultDetailsField();

        result.setFieldName(compareRuleFieldsDto.getCompareFieldDto().getName());
        result.setFieldCode(compareRuleFieldsDto.getCompareFieldDto().getField());





        if (compareRuleFieldsDto.getActive()) {

            for (CompareDefineDto compareDefineDto : define) {
                CompareResultDetailsCell cell = new CompareResultDetailsCell();
                cell.setValue(BeanValueUtils.getValueString(compareDatas.get(compareDefineDto.getDataSourceId()), compareRuleFieldsDto.getCompareFieldDto().getField()));
                cell.setMatch(true);
                result.getContent().put(compareDefineDto.getDataSourceDto().getName(), cell);
            }

            for (CompareDefineDto compareDefineDto1 : define) {
                for (CompareDefineDto compareDefineDto2 : define) {
                    if (compareDefineDto1.getDataSourceId().equals(compareDefineDto2.getDataSourceId())) {
                        continue;
                    }
                    //比对
                    Boolean compareResult = compare(compareRuleFieldsDto.getCompareFieldDto(), compareDefineDto1, compareDefineDto2, compareDatas.get(compareDefineDto1.getDataSourceId()), compareDatas.get(compareDefineDto2.getDataSourceId()));

                    //如果有这个数据源的结果，直接设置2个数据源的结果为false
                    CompareResultDetailsCell cell1 = result.getContent().get(compareDefineDto1.getDataSourceDto().getName());
                    CompareResultDetailsCell cell2 = result.getContent().get(compareDefineDto2.getDataSourceDto().getName());

                    if (cell1.isMatch()) {
                        cell1.setMatch(compareResult);
                    }
                    if (cell2.isMatch()) {
                        cell2.setMatch(compareResult);
                    }


                    if (!compareResult) {
                        //不一致直接返回
                        break;
                    }
                }

                //只循环一次
                break;
            }
        }

        return result;
    }

    private Boolean compare(CompareFieldDto compareFieldDto, CompareDefineDto defineDto1, CompareDefineDto defineDto2, CompareDataDto data1, CompareDataDto data2) {

        String name = compareFieldDto.getName();
        String field = compareFieldDto.getField();

        //无此规则也跳过
        if (defineDto1 == null || defineDto2 == null) {
            return true;
        }

        //如果该字段不需要比对则直接跳过
        if (!defineDto1.getActive() || !defineDto2.getActive()) {
            return true;
        }

        String fieldValue1 = BeanValueUtils.getValueString(data1, field);
        String fieldValue2 = BeanValueUtils.getValueString(data2, field);

        if (CompareFieldEnum.REG_CAPITAL.getField().equalsIgnoreCase(field)) {
            fieldValue1 = StringUtils.isBlank(fieldValue1) ? "0" : fieldValue1;
            fieldValue2 = StringUtils.isBlank(fieldValue2) ? "0" : fieldValue2;
        }

        //客户异动比较时，营业执照到期日是9999年的，跳过。
        if (CompareFieldEnum.ENDDATE.getField().equalsIgnoreCase(field)){
            if (fieldValue1.contains("9999")||fieldValue2.contains("9999")){
                return true;
            }
        }

        //如果空算对且是空的，则直接跳过
        boolean nullPass = (defineDto1.getNullpass() && StringUtils.isEmpty(fieldValue1)) || (defineDto2.getNullpass() && StringUtils.isEmpty(fieldValue2));
        if (nullPass) {
            return true;
        }

        if (CompareFieldEnum.BUSINESS_SCOPE.getField().equalsIgnoreCase(field)) {
            log.info("比对经营范围针对取消开户许可证进行特殊处理前：fieldValue1={},fieldValue2={}",fieldValue1,fieldValue2);

            //全角字符集转化成半角字符集
            fieldValue1 = Q2BChange(fieldValue1,true);
            fieldValue2 = Q2BChange(fieldValue2,true);

            if(fieldValue1.contains("(取消开户许可证核发)")){
                fieldValue1 = fieldValue1.replace("(取消开户许可证核发)","");
            }else if(fieldValue1.contains("（取消开户许可证核发）")){
                fieldValue1 = fieldValue1.replace("（取消开户许可证核发）","");
            }
            if(fieldValue2.contains("(取消开户许可证核发)")){
                fieldValue2 = fieldValue2.replace("(取消开户许可证核发)","");
            }else if(fieldValue2.contains("（取消开户许可证核发）")){
                fieldValue2 = fieldValue2.replace("（取消开户许可证核发）","");
            }
//            fieldValue1 = fieldValue1.contains("（取消开户许可证核发）") ? fieldValue1.replace("（取消开户许可证核发）","") : fieldValue1;
//            fieldValue2 = fieldValue1.contains("（取消开户许可证核发）") ? fieldValue2.replace("（取消开户许可证核发）","") : fieldValue2;
            log.info("比对经营范围针对取消开户许可证进行特殊处理后：fieldValue1={},fieldValue2={}",fieldValue1,fieldValue2);
        }

        if(StringUtils.equals("registeredCapital",field)){
            //注册资金特殊处理
            log.info("注册资金特殊处理");
            if(!fieldValue1.contains(".")){
                fieldValue1=fieldValue1+".00";
            }
            if(!fieldValue2.contains(".")){
                fieldValue2 = fieldValue2+".00";
            }
            log.info("注册资金特殊处理结果：fieldValue1={},fieldValue2={}",fieldValue1,fieldValue2);
        }
        //比较
        if (!ObjectUtils.equals(fieldValue1, fieldValue2)) {
            log.info(name + "字段比对不一致");
            return false;
        }

        return true;
    }


    /**
     * 全角字符转化成半角字符
     * @param input 所需要转化的字符串
     * @param flag 标识
     * @return
     */
    private static String Q2BChange(String input,boolean flag) {
        String result = "";
        if (StringUtils.isNotBlank(input)){
            char[] str = input.toCharArray();
            for (int i = 0; i < str.length; i++) {
                int code = str[i];//获取当前字符的unicode编码
                if (code >= 65281 && code <= 65373)//在这个unicode编码范围中的是所有的英文字母以及各种字符
                {
                    result += (char) (str[i] - 65248);//把全角字符的unicode编码转换为对应半角字符的unicode码
                } else if (code == 12288)//空格
                {
                    result += (char) (str[i] - 12288 + 32);
                } else if (code == 65377) {
                    result += (char) (12290);
                } else if (code == 12539) {
                    result += (char) (183);
                } else if (code == 8482 && flag == true) {//如果是特殊字符TM 并且是需要转换的所作操作

                } else if (code == 8226) { //特殊字符 ‘·’的转化
                    result += (char) (183);
                } else {
                    result += str[i];
                }
            }
            result.replace(" ","");
        }

        return result;
    }
}
