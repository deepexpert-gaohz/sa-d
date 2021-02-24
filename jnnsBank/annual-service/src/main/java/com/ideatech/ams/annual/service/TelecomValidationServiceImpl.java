package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.TelecomValidationDao;
import com.ideatech.ams.annual.dao.spec.TelecomValidationSpec;
import com.ideatech.ams.annual.dto.TelecomValidationDto;
import com.ideatech.ams.annual.dto.TelecomValidationSearchDto;
import com.ideatech.ams.annual.entity.TelecomValidationPo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class TelecomValidationServiceImpl extends BaseServiceImpl<TelecomValidationDao, TelecomValidationPo, TelecomValidationDto> implements TelecomValidationSerivce {

    @PersistenceContext
    private EntityManager em; //注入EntityManager

    @Override
    public List<TelecomValidationDto> findByBatchNo(String batchNo) {
        List<TelecomValidationPo> byBatchNo = getBaseDao().findByBatchNo(batchNo);
        return ConverterService.convertToList(byBatchNo, TelecomValidationDto.class);
    }

    @Override
    public PagingDto<TelecomValidationDto> query(TelecomValidationSearchDto searchDto, PagingDto pagingDto) {
//        Pageable pageable = new PageRequest(Math.max(pagingDto.getOffset() , 0), pagingDto.getLimit());
//        Page<TelecomValidationPo> byBatchNo = getBaseDao().findAll(new TelecomValidationSpec(searchDto), pageable);
//        List<TelecomValidationDto> saicStockHolderDtos = ConverterService.convertToList(byBatchNo.getContent(), TelecomValidationDto.class);
//        pagingDto.setList(saicStockHolderDtos);
//        pagingDto.setTotalRecord(byBatchNo.getTotalElements());
//        pagingDto.setTotalPages(byBatchNo.getTotalPages());
//        return pagingDto;

        String sql = "select t1 from TelecomValidationPo t1, OrganizationPo t2 where t1.bankCode=t2.code and t2.fullId like ?1 ";
        String countSql = "select count(*) from TelecomValidationPo t1, OrganizationPo t2 where t1.bankCode=t2.code and t2.fullId like ?1 ";
        if (StringUtils.isNotBlank(searchDto.getBatchNo())) {
            sql += " and t1.batchNo = ?2 ";
            countSql += " and t1.batchNo = ?2 ";
        }
        if (StringUtils.isNotBlank(searchDto.getCustomerNo())) {
            sql += " and t1.customerNo = ?3 ";
            countSql += " and t1.customerNo = ?3 ";
        }
        if (StringUtils.isNotBlank(searchDto.getCustomerName())) {
            sql += " and t1.customerName like ?4 ";
            countSql += " and t1.customerName like ?4 ";
        }
        if (StringUtils.isNotBlank(searchDto.getBankCode())) {
            sql += " and t1.bankCode like ?5 ";
            countSql += " and t1.bankCode like ?5 ";
        }
        if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
            sql += " and t1.acctNo = ?6 ";
            countSql += " and t1.acctNo = ?6 ";
        }
        Query query = em.createQuery(sql);
        Query queryCount = em.createQuery(countSql);

        query.setParameter(1, SecurityUtils.getCurrentOrgFullId() + "%");
        queryCount.setParameter(1, SecurityUtils.getCurrentOrgFullId() + "%");

        if (StringUtils.isNotBlank(searchDto.getBatchNo())) {
            query.setParameter(2, searchDto.getBatchNo());
            queryCount.setParameter(2, searchDto.getBatchNo());
        }
        if (StringUtils.isNotBlank(searchDto.getCustomerNo())) {
            query.setParameter(3, searchDto.getCustomerNo());
            queryCount.setParameter(3, searchDto.getCustomerNo());
        }
        if (StringUtils.isNotBlank(searchDto.getCustomerName())) {
            query.setParameter(4, "%" + searchDto.getCustomerName() + "%");
            queryCount.setParameter(4, "%" + searchDto.getCustomerName() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getBankCode())) {
            query.setParameter(5, "%" + searchDto.getBankCode() + "%");
            queryCount.setParameter(5, "%" + searchDto.getBankCode() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
            query.setParameter(6, searchDto.getAcctNo());
            queryCount.setParameter(6, searchDto.getAcctNo());
        }

        query.setFirstResult(pagingDto.getOffset() * pagingDto.getLimit());
        query.setMaxResults(pagingDto.getLimit());
        //查询
        List<TelecomValidationPo> list = query.getResultList();
        Long count = (Long) queryCount.getSingleResult();

        List<TelecomValidationDto> dtoList = new ArrayList<>();

        for(TelecomValidationPo po :list){
            TelecomValidationDto dto = new TelecomValidationDto();
            BeanUtils.copyProperties(po, dto);
            dtoList.add(dto);
        }

        pagingDto.setList(dtoList);
        pagingDto.setTotalRecord(count);
        pagingDto.setTotalPages((int) Math.ceil(count.intValue()/pagingDto.getLimit()));
        return pagingDto;
    }

    @Override
    public List<TelecomValidationDto> queryAll(TelecomValidationSearchDto searchDto) {
        String sql = "select t1 from TelecomValidationPo t1, OrganizationPo t2 where t1.bankCode=t2.code and t2.fullId like ?1 ";
        if (StringUtils.isNotBlank(searchDto.getBatchNo())) {
            sql += " and t1.batchNo = ?2 ";
        }
        if (StringUtils.isNotBlank(searchDto.getCustomerNo())) {
            sql += " and t1.customerNo = ?3 ";
        }
        if (StringUtils.isNotBlank(searchDto.getCustomerName())) {
            sql += " and t1.customerName like ?4 ";
        }
        if (StringUtils.isNotBlank(searchDto.getBankCode())) {
            sql += " and t1.bankCode like ?5 ";
        }
        if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
            sql += " and t1.acctNo = ?6 ";
        }
        Query query = em.createQuery(sql);

        query.setParameter(1, SecurityUtils.getCurrentOrgFullId() + "%");

        if (StringUtils.isNotBlank(searchDto.getBatchNo())) {
            query.setParameter(2, searchDto.getBatchNo());
        }
        if (StringUtils.isNotBlank(searchDto.getCustomerNo())) {
            query.setParameter(3, searchDto.getCustomerNo());
        }
        if (StringUtils.isNotBlank(searchDto.getCustomerName())) {
            query.setParameter(4, "%" + searchDto.getCustomerName() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getBankCode())) {
            query.setParameter(5, "%" + searchDto.getBankCode() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
            query.setParameter(6, searchDto.getAcctNo());
        }

        //查询
        List<TelecomValidationPo> list = query.getResultList();

        List<TelecomValidationDto> dtoList = new ArrayList<>();

        for(TelecomValidationPo po :list){
            TelecomValidationDto dto = new TelecomValidationDto();
            BeanUtils.copyProperties(po, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public HSSFWorkbook exportExcel(List<TelecomValidationDto> tvdList) {
        HSSFWorkbook wb = new HSSFWorkbook();//创建Excel工作簿对象

        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);//字号

        HSSFFont jcFont = wb.createFont();
        jcFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗

        HSSFFont redFont = wb.createFont();
        redFont.setColor(HSSFColor.RED.index);// 红色
        //文本格式
        HSSFCellStyle textStyle = wb.createCellStyle();
        HSSFDataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));//设置格式为文本格式
        //标题样式
        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setFont(font);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style1.setWrapText(true);//自动换行
        //表格内容样式
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
        style2.setWrapText(true);//自动换行
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        HSSFSheet sheet = wb.createSheet("Sheet1");//创建Excel工作表对象
        //设置列宽
        double[] columnWidthArr = {10, 15, 15, 15, 10, 20, 14, 30};
        for (int i = 0; i < columnWidthArr.length; i++) {
            sheet.setColumnWidth(i, (short) (256 * columnWidthArr[i]));//设置列宽
            sheet.setDefaultColumnStyle(i, textStyle);//设置格式为文本格式
        }

        //设置表头
        HSSFRow row1 = sheet.createRow((short) 0); //创建Excel工作表的第一行
        HSSFCell cell = row1.createCell(0);
        cell.setCellStyle(style1); //设置Excel指定单元格的样式
        HSSFRichTextString row1String = new HSSFRichTextString("电信运营商校验表");
        row1String.applyFont(jcFont);
        cell.setCellValue(row1String); //设置Excel指定单元格的值

        HSSFRow row2 = sheet.createRow((short) 1);
        row2.setHeight((short) 540);//设置行高
        String[] row2String = {"客户号", "客户名称", "账号", "核心机构号", "姓名", "身份证号", "手机号", "是否一致"};
        for (int i = 0; i < row2String.length; i++) {
            this.setCell(row2, i, style1, row2String[i]);
        }
        //插入内容
        for (int i = 0; i < tvdList.size(); i++) {
            TelecomValidationDto tvd = tvdList.get(i);
            HSSFRow row = sheet.createRow((short) (2 + i));
            this.setCell(row, 0, style1, tvd.getCustomerNo());//客户号
            this.setCell(row, 1, style1, tvd.getCustomerName());//客户名称
            this.setCell(row, 2, style1, tvd.getAcctNo());//账号
            this.setCell(row, 3, style1, tvd.getBankCode());//核心机构号
            this.setCell(row, 4, style1, tvd.getName());//姓名
            this.setCell(row, 5, style1, tvd.getIdNo());//身份证号
            this.setCell(row, 6, style1, tvd.getMobile());//手机号
            this.setCell(row, 7, style1, tvd.getResult());//校验结果
        }

        //合并单元格
        CellRangeAddress cra = CellRangeAddress.valueOf("$A$1:$H$1");
        sheet.addMergedRegion(cra);
        //额外设置合并单元格后的边框
        RegionUtil.setBorderBottom(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
        RegionUtil.setBorderLeft(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
        RegionUtil.setBorderTop(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
        RegionUtil.setBorderRight(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
        return wb;
    }

    @Override
    public List<TelecomValidationDto> findSameDto(String name, String tel, String idCardNo,String batchNo) {
        return ConverterService.convertToList(getBaseDao().findByNameAndIdNoAndMobileAndBatchNo(name,tel,idCardNo,batchNo),TelecomValidationDto.class);
    }

    private void setCell(HSSFRow row, int column, HSSFCellStyle style, Object value) {
        HSSFCell cell = row.createCell(column);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(value == null ? "" : String.valueOf(value)));
    }
}
