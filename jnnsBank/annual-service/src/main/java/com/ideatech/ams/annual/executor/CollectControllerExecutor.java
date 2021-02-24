package com.ideatech.ams.annual.executor;

import com.ideatech.ams.annual.enums.CollectType;
import com.ideatech.ams.annual.service.CoreCollectionService;
import com.ideatech.ams.annual.service.PbcCollectionService;
import com.ideatech.ams.annual.service.SaicCollectionService;
import com.ideatech.ams.annual.vo.CoreCollectionExcelRowVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description 采集的控制层线程
 * @Author wanghongjie
 * @Date 2018/8/13
 **/
@Data
@Slf4j
public class CollectControllerExecutor implements Runnable {
    private PbcCollectionService pbcCollectionService;

    private SaicCollectionService saicCollectionService;

    private CoreCollectionService coreCollectionService;

    private List<CoreCollectionExcelRowVo> dataList;

    private CollectType collectType;

    private Long taskId;

    private int saicType;

    private int pbcType;

    private int coreType;

    @Override
    public void run() {
        if(pbcCollectionService != null){
            if(collectType == CollectType.CONTINUE){
                if(pbcType == 0){//Excel重新下载
                    pbcCollectionService.collect(collectType,taskId);
                }else{
                    pbcCollectionService.collectReset(taskId);
                }
            }else{
                pbcCollectionService.collect(collectType,taskId);
            }
        }else if(saicCollectionService != null){
            if(collectType == CollectType.CONTINUE){//失败重新采集
                saicCollectionService.collectReset(collectType,taskId);
            }else{//初始采集
                if(saicType ==0){//在线采集
                    saicCollectionService.collect(collectType,taskId);
                }else{//文件导入采集
                    saicCollectionService.collectByFile(collectType,taskId);
                }
            }
        } else if(coreCollectionService != null){

            if(coreType ==0){
                if(collectType == CollectType.CONTINUE){
                    coreCollectionService.collectReset(taskId);
                }else{
                    coreCollectionService.collect(taskId);
                }
            }else{
                coreCollectionService.collect(dataList,taskId, collectType);
            }
        }
    }
}
