package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.SaicStockHolderDao;
import com.ideatech.ams.annual.dao.spec.SaicStockHolderSpec;
import com.ideatech.ams.annual.dto.SaicStockHolderDto;
import com.ideatech.ams.annual.dto.SaicStockHolderSearchDto;
import com.ideatech.ams.annual.entity.SaicStockHolderPo;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaicStockHolderServiceImpl extends BaseServiceImpl<SaicStockHolderDao, SaicStockHolderPo, SaicStockHolderDto> implements SaicStockHolderService {

    @PersistenceContext
    private EntityManager em; //注入EntityManager

    @Override
    public PagingDto<SaicStockHolderDto> query(SaicStockHolderSearchDto searchDto, PagingDto pagingDto) {
//        Pageable pageable = new PageRequest(Math.max(pagingDto.getOffset(), 0), pagingDto.getLimit());
//        Page<SaicStockHolderPo> byBatchNo = getBaseDao().findAll(new SaicStockHolderSpec(searchDto), pageable);
//        List<SaicStockHolderDto> saicStockHolderDtos = ConverterService.convertToList(byBatchNo.getContent(), SaicStockHolderDto.class);
//        pagingDto.setList(saicStockHolderDtos);
//        pagingDto.setTotalRecord(byBatchNo.getTotalElements());
//        pagingDto.setTotalPages(byBatchNo.getTotalPages());
//        return pagingDto;

        String sql = "select t1 from SaicStockHolderPo t1, OrganizationPo t2 where t1.organCode=t2.code and t2.fullId like ?1 ";
        String countSql = "select count(*) from SaicStockHolderPo t1, OrganizationPo t2 where t1.organCode=t2.code and t2.fullId like ?1 ";
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
        if (StringUtils.isNotBlank(searchDto.getOrganCode())) {
            sql += " and t1.organCode like ?5 ";
            countSql += " and t1.organCode like ?5 ";
        }
        if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
            sql += " and t1.acctNo = ?6 ";
            countSql += " and t1.acctNo = ?6 ";
        }
        if (searchDto.getIsSame() != null) {
            sql += " and t1.isSame = ?7 ";
            countSql += " and t1.isSame = ?7 ";
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
        if (StringUtils.isNotBlank(searchDto.getOrganCode())) {
            query.setParameter(5, "%" + searchDto.getOrganCode() + "%");
            queryCount.setParameter(5, "%" + searchDto.getOrganCode() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
            query.setParameter(6, searchDto.getAcctNo());
            queryCount.setParameter(6, searchDto.getAcctNo());
        }
        if (searchDto.getIsSame() != null) {
            query.setParameter(7, searchDto.getIsSame());
            queryCount.setParameter(7, searchDto.getIsSame());
        }

        query.setFirstResult(pagingDto.getOffset() * pagingDto.getLimit());
        query.setMaxResults(pagingDto.getLimit());
        //查询
        List<SaicStockHolderPo> list = query.getResultList();
        Long count = (Long) queryCount.getSingleResult();

        List<SaicStockHolderDto> dtoList = new ArrayList<>();

        for(SaicStockHolderPo po :list){
            SaicStockHolderDto dto = new SaicStockHolderDto();
            BeanUtils.copyProperties(po, dto);
            dtoList.add(dto);
        }

        pagingDto.setList(dtoList);
        pagingDto.setTotalRecord(count);
        pagingDto.setTotalPages((int) Math.ceil(count.intValue()/pagingDto.getLimit()));
        return pagingDto;
    }

    @Override
    public List<SaicStockHolderDto> queryAll(SaicStockHolderSearchDto searchDto) {
        String sql = "select t1 from SaicStockHolderPo t1, OrganizationPo t2 where t1.organCode=t2.code and t2.fullId like ?1 ";
        if (StringUtils.isNotBlank(searchDto.getBatchNo())) {
            sql += " and t1.batchNo = ?2 ";
        }
        if (StringUtils.isNotBlank(searchDto.getCustomerNo())) {
            sql += " and t1.customerNo = ?3 ";
        }
        if (StringUtils.isNotBlank(searchDto.getCustomerName())) {
            sql += " and t1.customerName like ?4 ";
        }
        if (StringUtils.isNotBlank(searchDto.getOrganCode())) {
            sql += " and t1.organCode like ?5 ";
        }
        if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
            sql += " and t1.acctNo = ?6 ";
        }
        if (searchDto.getIsSame() != null) {
            sql += " and t1.isSame = ?7 ";
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
        if (StringUtils.isNotBlank(searchDto.getOrganCode())) {
            query.setParameter(5, "%" + searchDto.getOrganCode() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
            query.setParameter(6, searchDto.getAcctNo());
        }
        if (searchDto.getIsSame() != null) {
            query.setParameter(7, searchDto.getIsSame());
        }

        //查询
        List<SaicStockHolderPo> list = query.getResultList();

        List<SaicStockHolderDto> dtoList = new ArrayList<>();

        for(SaicStockHolderPo po :list){
            SaicStockHolderDto dto = new SaicStockHolderDto();
            BeanUtils.copyProperties(po, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public HSSFWorkbook exportExcel(List<SaicStockHolderDto> sshdList) {


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
        double[] columnWidthArr = {10, 15, 15, 15, 10, 20, 10, 20, 9};
        for (int i = 0; i < columnWidthArr.length; i++) {
            sheet.setColumnWidth(i, (short) (256 * columnWidthArr[i]));//设置列宽
            sheet.setDefaultColumnStyle(i, textStyle);//设置格式为文本格式
        }

        //设置表头
        HSSFRow row1 = sheet.createRow((short) 0); //创建Excel工作表的第一行
        HSSFCell cell = row1.createCell(0);
        cell.setCellStyle(style1); //设置Excel指定单元格的样式
        HSSFRichTextString row1String = new HSSFRichTextString("控股股东比对表");
        row1String.applyFont(jcFont);
        cell.setCellValue(row1String); //设置Excel指定单元格的值

        HSSFRow row2 = sheet.createRow((short) 1);
        this.setCell(row2, 0, style1, "核心数据");
        this.setCell(row2, 6, style1, "工商数据");

        HSSFRow row3 = sheet.createRow((short) 2);
        row3.setHeight((short) 540);//设置行高
        String[] row3String = {"客户号", "客户名称", "账号", "核心机构号", "控股股东\r\n（若无持股比例超过50%，则显示无）",
                "", "控股股东\r\n（若无持股比例超过50%，则显示无）", "", "是否一致"};
        for (int i = 0; i < row3String.length; i++) {
            this.setCell(row3, i, style1, row3String[i]);
        }
        HSSFRow row4 = sheet.createRow((short) 3);
        String[] row4String = {"", "", "", "", "姓名", "持股比例", "姓名", "持股比例", ""};
        for (int i = 0; i < row4String.length; i++) {
            this.setCell(row4, i, style1, row4String[i]);
        }
        //插入内容
        for (int i = 0; i < sshdList.size(); i++) {
            SaicStockHolderDto sshd = sshdList.get(i);
            HSSFRow row = sheet.createRow((short) (4 + i));
            this.setCell(row, 0, style1, sshd.getCustomerNo());//客户号
            this.setCell(row, 1, style1, sshd.getCustomerName());//客户名称
            this.setCell(row, 2, style1, sshd.getAcctNo());//账号
            this.setCell(row, 3, style1, sshd.getOrganCode());//核心机构号
            this.setCell(row, 4, style1, sshd.getCoreStockHolderName());//核心控股股东名称
            this.setCell(row, 5, style1, this.doubleToPercent(sshd.getCoreStockHolderRatio()));//核心控股股东持股比例
            this.setCell(row, 6, style1, sshd.getStockHolderName());//控股股东名称
            this.setCell(row, 7, style1, this.doubleToPercent(sshd.getStockHolderRatio()));//控股股东持股比例
            this.setCell(row, 8, style1, sshd.getIsSame() ? "一致" : "不一致");//工商与核心是否一致
        }

        //合并单元格
        String[] mergedRegionArr = {"$A$1:$I$1","$A$2:$F$2","$G$2:$I$2","$A$3:$A$4","$B$3:$B$4",
                "$C$3:$C$4","$D$3:$D$4","$E$3:$F$3","$G$3:$H$3"};
        for (int i = 0; i < mergedRegionArr.length; i++) {
            CellRangeAddress cra = CellRangeAddress.valueOf(mergedRegionArr[i]);
            sheet.addMergedRegion(cra);
            //额外设置合并单元格后的边框
            RegionUtil.setBorderBottom(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
            RegionUtil.setBorderLeft(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
            RegionUtil.setBorderTop(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
            RegionUtil.setBorderRight(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
        }
        return wb;
    }

    private void setCell(HSSFRow row, int column, HSSFCellStyle style, Object value) {
        HSSFCell cell = row.createCell(column);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(value == null ? "" : String.valueOf(value)));
    }

    private String doubleToPercent(Double d) {
        if (d != null) {
            NumberFormat nFromat = NumberFormat.getPercentInstance();
            return nFromat.format(d);
        } else {
            return "";
        }
    }

}
