package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.SaicBeneficiaryDao;
import com.ideatech.ams.annual.dao.spec.SaicBeneficiarySpec;
import com.ideatech.ams.annual.dto.SaicBeneficiaryDto;
import com.ideatech.ams.annual.dto.SaicBeneficiarySearchDto;
import com.ideatech.ams.annual.entity.SaicBeneficiaryPo;
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
public class SaicBeneficiaryServiceImpl extends BaseServiceImpl<SaicBeneficiaryDao, SaicBeneficiaryPo, SaicBeneficiaryDto> implements SaicBeneficiarySerivce {

    @PersistenceContext
    private EntityManager em; //注入EntityManager

    @Override
    public PagingDto<SaicBeneficiaryDto> query(SaicBeneficiarySearchDto searchDto, PagingDto pagingDto) {
//        Pageable pageable = new PageRequest(Math.max(pagingDto.getOffset(), 0), pagingDto.getLimit());
//        Page<SaicBeneficiaryPo> byBatchNo = getBaseDao().findAll(new SaicBeneficiarySpec(searchDto), pageable);
//        List<SaicBeneficiaryDto> saicBeneficiaryDtos = ConverterService.convertToList(byBatchNo.getContent(), SaicBeneficiaryDto.class);
//        pagingDto.setList(saicBeneficiaryDtos);
//        pagingDto.setTotalRecord(byBatchNo.getTotalElements());
//        pagingDto.setTotalPages(byBatchNo.getTotalPages());
//        return pagingDto;


        String sql = "select t1 from SaicBeneficiaryPo t1, OrganizationPo t2 where t1.organCode=t2.code and t2.fullId like ?1 ";
        String countSql = "select count(*) from SaicBeneficiaryPo t1, OrganizationPo t2 where t1.organCode=t2.code and t2.fullId like ?1 ";
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
        Query query = em.createQuery(sql + "order by t1.beneficiaryRatio1 desc nulls last, t1.id");
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
        List<SaicBeneficiaryPo> list = query.getResultList();
        Long count = (Long) queryCount.getSingleResult();

        List<SaicBeneficiaryDto> dtoList = new ArrayList<>();

        for(SaicBeneficiaryPo po :list){
            SaicBeneficiaryDto dto = new SaicBeneficiaryDto();
            BeanUtils.copyProperties(po, dto);
            dtoList.add(dto);
        }

        pagingDto.setList(dtoList);
        pagingDto.setTotalRecord(count);
        pagingDto.setTotalPages((int) Math.ceil(count.intValue()/pagingDto.getLimit()));
        return pagingDto;
    }

    @Override
    public List<SaicBeneficiaryDto> queryAll(SaicBeneficiarySearchDto searchDto) {
        String sql = "select t1 from SaicBeneficiaryPo t1, OrganizationPo t2 where t1.organCode=t2.code and t2.fullId like ?1 ";
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
        Query query = em.createQuery(sql + "order by t1.beneficiaryRatio1 desc");

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
        List<SaicBeneficiaryPo> list = query.getResultList();

        List<SaicBeneficiaryDto> dtoList = new ArrayList<>();

        for(SaicBeneficiaryPo po :list){
            SaicBeneficiaryDto dto = new SaicBeneficiaryDto();
            BeanUtils.copyProperties(po, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public HSSFWorkbook exportExcel(List<SaicBeneficiaryDto> sbdList) {

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
        double[] columnWidthArr = {10, 15, 15, 15, 7, 9, 7, 9, 7, 9, 7, 9, 35, 7, 9, 7, 9, 7, 9, 7, 9, 35, 9};
        for (int i = 0; i < columnWidthArr.length; i++) {
            sheet.setColumnWidth(i, (short) (256 * columnWidthArr[i]));//设置列宽
            sheet.setDefaultColumnStyle(i, textStyle);//设置格式为文本格式
        }

        //设置表头
        HSSFRow row1 = sheet.createRow((short) 0); //创建Excel工作表的第一行
        HSSFCell cell = row1.createCell(0);
        cell.setCellStyle(style1); //设置Excel指定单元格的样式
        HSSFRichTextString row1String = new HSSFRichTextString("受益所有人比对表");
        row1String.applyFont(jcFont);
        cell.setCellValue(row1String); //设置Excel指定单元格的值

        HSSFRow row2 = sheet.createRow((short) 1);
        this.setCell(row2, 0, style1, "核心数据");
        this.setCell(row2, 13, style1, "工商数据");

        HSSFRow row3 = sheet.createRow((short) 2);
        row3.setHeight((short) 540);//设置行高
        String[] row3String = {"客户号", "客户名称", "账号", "核心机构号", "受益所有人1", "", "受益所有人2", "", "受益所有人3", "", "受益所有人4", "", "法定代表人\r\n（若无持股比例超过25%自然人显示）",
                "受益所有人1", "", "受益所有人2", "", "受益所有人3", "", "受益所有人4", "", "法定代表人\r\n（若无持股比例超过25%自然人显示）", "是否一致"};
        for (int i = 0; i < row3String.length; i++) {
            HSSFCell cel3 = row3.createCell(i);
            cel3.setCellStyle(style1);
            HSSFRichTextString richString = new HSSFRichTextString(row3String[i]);
            if (row3String[i].startsWith("法定代表人")) {
                richString.applyFont(6, richString.length(), redFont);
            }
            cel3.setCellValue(richString);
        }
        HSSFRow row4 = sheet.createRow((short) 3);
        String[] row4String = {"", "", "", "", "姓名", "持股比例", "姓名", "持股比例", "姓名", "持股比例", "姓名", "持股比例", "", "姓名", "持股比例", "姓名", "持股比例", "姓名", "持股比例", "姓名", "持股比例", "", ""};
        for (int i = 0; i < row4String.length; i++) {
            this.setCell(row4, i, style1, row4String[i]);
        }

        //插入内容
        for (int i = 0; i < sbdList.size(); i++) {
            SaicBeneficiaryDto sbd = sbdList.get(i);
            HSSFRow row = sheet.createRow((short) (4 + i));
            this.setCell(row, 0, style1, sbd.getCustomerNo());//客户号
            this.setCell(row, 1, style1, sbd.getCustomerName());//客户名称
            this.setCell(row, 2, style1, sbd.getAcctNo());//账号
            this.setCell(row, 3, style1, sbd.getOrganCode());//核心机构号
            this.setCell(row, 4, style1, sbd.getCoreBeneficiaryName1());//核心受益人名称1
            this.setCell(row, 5, style1, this.doubleToPercent(sbd.getCoreBeneficiaryRatio1()));//核心受益人比例1
            this.setCell(row, 6, style1, sbd.getCoreBeneficiaryName2());//核心受益人名称2
            this.setCell(row, 7, style1, this.doubleToPercent(sbd.getCoreBeneficiaryRatio2()));//核心受益人比例2
            this.setCell(row, 8, style1, sbd.getCoreBeneficiaryName3());//核心受益人名称3
            this.setCell(row, 9, style1, this.doubleToPercent(sbd.getCoreBeneficiaryRatio3()));//核心受益人比例3
            this.setCell(row, 10, style1, sbd.getCoreBeneficiaryName4());//核心受益人名称4
            this.setCell(row, 11, style1, this.doubleToPercent(sbd.getCoreBeneficiaryRatio4()));//核心受益人比例4
            this.setCell(row, 12, style1, sbd.getCoreLegalName());//核心法人
            this.setCell(row, 13, style1, sbd.getBeneficiaryName1());//受益人名称1
            this.setCell(row, 14, style1, this.doubleToPercent(sbd.getBeneficiaryRatio1()));//受益人比例1
            this.setCell(row, 15, style1, sbd.getBeneficiaryName2());//受益人名称2
            this.setCell(row, 16, style1, this.doubleToPercent(sbd.getBeneficiaryRatio2()));//受益人比例2
            this.setCell(row, 17, style1, sbd.getBeneficiaryName3());//受益人名称3
            this.setCell(row, 18, style1, this.doubleToPercent(sbd.getBeneficiaryRatio3()));//受益人比例3
            this.setCell(row, 19, style1, sbd.getBeneficiaryName4());//受益人名称4
            this.setCell(row, 20, style1, this.doubleToPercent(sbd.getBeneficiaryRatio4()));//受益人比例4
            this.setCell(row, 21, style1, sbd.getLegalName());//法人
            this.setCell(row, 22, style1, sbd.getIsSame() ? "一致" : "不一致");//工商与核心是否一致
        }

        //合并单元格
        String[] mergedRegionArr = {"$A$1:$W$1","$A$2:$M$2","$N$2:$W$2","$A$3:$A$4","$B$3:$B$4",
                "$C$3:$C$4","$D$3:$D$4","$M$3:$M$4","$V$3:$V$4","$E$3:$F$3","$G$3:$H$3",
                "$I$3:$J$3","$K$3:$L$3","$N$3:$O$3","$P$3:$Q$3","$R$3:$S$3","$T$3:$U$3"};
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

    @Override
    public HSSFWorkbook exportCollectExcel(List<SaicBeneficiaryDto> sbdList) {

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
        double[] columnWidthArr = {20, 20, 20, 20, 10, 10, 10, 10, 10, 10, 10, 10, 35};
        for (int i = 0; i < columnWidthArr.length; i++) {
            sheet.setColumnWidth(i, (short) (256 * columnWidthArr[i]));//设置列宽
            sheet.setDefaultColumnStyle(i, textStyle);//设置格式为文本格式
        }

        //设置表头
        HSSFRow row1 = sheet.createRow((short) 0); //创建Excel工作表的第一行
        HSSFCell cell = row1.createCell(0);
        cell.setCellStyle(style1); //设置Excel指定单元格的样式
        HSSFRichTextString row1String = new HSSFRichTextString("受益所有人比对表");
        row1String.applyFont(jcFont);
        cell.setCellValue(row1String); //设置Excel指定单元格的值

        HSSFRow row2 = sheet.createRow((short) 1);
        this.setCell(row2, 0, style1, "");
        this.setCell(row2, 4, style1, "工商数据");

        HSSFRow row3 = sheet.createRow((short) 2);
        row3.setHeight((short) 540);//设置行高
        String[] row3String = {"客户号", "客户名称", "账号", "核心机构号",
                "受益所有人1", "", "受益所有人2", "", "受益所有人3", "", "受益所有人4", "", "法定代表人\r\n（若无持股比例超过25%自然人显示）"};
        for (int i = 0; i < row3String.length; i++) {
            HSSFCell cel3 = row3.createCell(i);
            cel3.setCellStyle(style1);
            HSSFRichTextString richString = new HSSFRichTextString(row3String[i]);
            if (row3String[i].startsWith("法定代表人")) {
                richString.applyFont(6, richString.length(), redFont);
            }
            cel3.setCellValue(richString);
        }
        HSSFRow row4 = sheet.createRow((short) 3);
        String[] row4String = {"", "", "", "", "姓名", "持股比例", "姓名", "持股比例", "姓名", "持股比例", "姓名", "持股比例", ""};
        for (int i = 0; i < row4String.length; i++) {
            this.setCell(row4, i, style1, row4String[i]);
        }

        //插入内容
        for (int i = 0; i < sbdList.size(); i++) {
            SaicBeneficiaryDto sbd = sbdList.get(i);
            HSSFRow row = sheet.createRow((short) (4 + i));
            this.setCell(row, 0, style1, sbd.getCustomerNo());//客户号
            this.setCell(row, 1, style1, sbd.getCustomerName());//客户名称
            this.setCell(row, 2, style1, sbd.getAcctNo());//账号
            this.setCell(row, 3, style1, sbd.getOrganCode());//核心机构号
            this.setCell(row, 4, style1, sbd.getBeneficiaryName1());//受益人名称1
            this.setCell(row, 5, style1, this.doubleToPercent(sbd.getBeneficiaryRatio1()));//受益人比例1
            this.setCell(row, 6, style1, sbd.getBeneficiaryName2());//受益人名称2
            this.setCell(row, 7, style1, this.doubleToPercent(sbd.getBeneficiaryRatio2()));//受益人比例2
            this.setCell(row, 8, style1, sbd.getBeneficiaryName3());//受益人名称3
            this.setCell(row, 9, style1, this.doubleToPercent(sbd.getBeneficiaryRatio3()));//受益人比例3
            this.setCell(row, 10, style1, sbd.getBeneficiaryName4());//受益人名称4
            this.setCell(row, 11, style1, this.doubleToPercent(sbd.getBeneficiaryRatio4()));//受益人比例4
            this.setCell(row, 12, style1, sbd.getLegalName());//法人
        }

        //合并单元格
        String[] mergedRegionArr = {"$A$1:$M$1","$A$2:$D$2","$E$2:$M$2",
                "$A$3:$A$4","$B$3:$B$4","$C$3:$C$4","$D$3:$D$4","$M$3:$M$4",
                "$E$3:$F$3","$G$3:$H$3","$I$3:$J$3","$K$3:$L$3"};
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

    @Override
    public HSSFWorkbook exportBeneficiaryNameExcel(List<SaicBeneficiaryDto> sbdList) {

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
        double[] columnWidthArr = {10, 15, 15, 15, 7, 9, 7, 9, 7, 9, 7, 9, 35, 7, 9, 7, 9, 7, 9, 7, 9, 35, 9};
        for (int i = 0; i < columnWidthArr.length; i++) {
            sheet.setColumnWidth(i, (short) (256 * columnWidthArr[i]));//设置列宽
            sheet.setDefaultColumnStyle(i, textStyle);//设置格式为文本格式
        }

        //设置表头
        HSSFRow row1 = sheet.createRow((short) 0); //创建Excel工作表的第一行
        HSSFCell cell = row1.createCell(0);
        cell.setCellStyle(style1); //设置Excel指定单元格的样式
        HSSFRichTextString row1String = new HSSFRichTextString("受益所有人比对表");
        row1String.applyFont(jcFont);
        cell.setCellValue(row1String); //设置Excel指定单元格的值

        HSSFRow row2 = sheet.createRow((short) 1);
        this.setCell(row2, 0, style1, "核心数据");
        this.setCell(row2, 9, style1, "工商数据");

        HSSFRow row3 = sheet.createRow((short) 2);
        row3.setHeight((short) 540);//设置行高
        String[] row3String = {"客户号", "客户名称", "账号", "核心机构号", "受益所有人1", "受益所有人2", "受益所有人3", "受益所有人4", "法定代表人\r\n（若无持股比例超过25%自然人显示）",
                "受益所有人1", "受益所有人2", "受益所有人3", "受益所有人4", "法定代表人\r\n（若无持股比例超过25%自然人显示）", "是否一致"};
        for (int i = 0; i < row3String.length; i++) {
            HSSFCell cel3 = row3.createCell(i);
            cel3.setCellStyle(style1);
            HSSFRichTextString richString = new HSSFRichTextString(row3String[i]);
            if (row3String[i].startsWith("法定代表人")) {
                richString.applyFont(6, richString.length(), redFont);
            }
            cel3.setCellValue(richString);
        }
        HSSFRow row4 = sheet.createRow((short) 3);
        String[] row4String = {"", "", "", "", "姓名", "姓名", "姓名", "姓名", "", "姓名", "姓名", "姓名", "姓名", "", ""};
        for (int i = 0; i < row4String.length; i++) {
            this.setCell(row4, i, style1, row4String[i]);
        }

        //插入内容
        for (int i = 0; i < sbdList.size(); i++) {
            SaicBeneficiaryDto sbd = sbdList.get(i);
            HSSFRow row = sheet.createRow((short) (4 + i));
            this.setCell(row, 0, style1, sbd.getCustomerNo());//客户号
            this.setCell(row, 1, style1, sbd.getCustomerName());//客户名称
            this.setCell(row, 2, style1, sbd.getAcctNo());//账号
            this.setCell(row, 3, style1, sbd.getOrganCode());//核心机构号
            this.setCell(row, 4, style1, sbd.getCoreBeneficiaryName1());//核心受益人名称1
            this.setCell(row, 5, style1, sbd.getCoreBeneficiaryName2());//核心受益人名称2
            this.setCell(row, 6, style1, sbd.getCoreBeneficiaryName3());//核心受益人名称3
            this.setCell(row, 7, style1, sbd.getCoreBeneficiaryName4());//核心受益人名称4
            this.setCell(row, 8, style1, sbd.getCoreLegalName());//核心法人
            this.setCell(row, 9, style1, sbd.getBeneficiaryName1());//受益人名称1
            this.setCell(row, 10, style1, sbd.getBeneficiaryName2());//受益人名称2
            this.setCell(row, 11, style1, sbd.getBeneficiaryName3());//受益人名称3
            this.setCell(row, 12, style1, sbd.getBeneficiaryName4());//受益人名称4
            this.setCell(row, 13, style1, sbd.getLegalName());//法人
            this.setCell(row, 14, style1, sbd.getIsSame() ? "一致" : "不一致");//工商与核心是否一致
        }

        //合并单元格
        String[] mergedRegionArr = {"$A$1:$O$1","$A$2:$I$2","$J$2:$O$2","$A$3:$A$4","$B$3:$B$4",
                "$C$3:$C$4","$D$3:$D$4","$M$3:$M$4"};
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
