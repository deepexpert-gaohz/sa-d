package com.ideatech.ams.ws.api.restful.test;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.ws.api.service.PbcApiService;
import com.ideatech.ams.ws.dto.ImageBatchInfo;
import com.ideatech.common.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author vantoo
 * @date 2018-11-30 10:25
 */

@RestController
@RequestMapping("/api/test/pbc")
public class PbcApiTestController {

    @Autowired
    private PbcApiService pbcApiService;

    @PostMapping("/reenterablePbcSync")
    public ResultDto reenterablePbcSync(String organCode, AllBillsPublicDTO billsPublic, @RequestParam(required = false) Boolean syncAms, @RequestParam(required = false) Boolean syncEccs) {
        if (syncAms == null && syncEccs == null) {
            return pbcApiService.reenterablePbcSync(organCode, billsPublic);
        } else {
            return pbcApiService.reenterablePbcSync(organCode, billsPublic, syncAms, syncEccs);
        }

    }

    @PostMapping("/reenterablePbcSync1")
    public ResultDto reenterablePbcSync1(String organCode, AllBillsPublicDTO billsPublic, @RequestParam(required = false) Boolean syncAms, @RequestParam(required = false) Boolean syncEccs) {
        return pbcApiService.reenterablePbcSync(organCode, billsPublic,"0");
    }

    @PostMapping("/syncPbc")
    public ResultDto syncPbc(String organCode, AllBillsPublicDTO billsPublic, @RequestParam(required = false) Boolean needSync, @RequestParam(required = false) Boolean reenterable) {
        if (reenterable == null) {
            if (needSync == null) {
                return pbcApiService.syncPbc(organCode, billsPublic);
            } else {
                return pbcApiService.syncPbc(organCode, billsPublic, needSync);
            }
        } else {
            return pbcApiService.syncPbc(organCode, billsPublic, needSync, reenterable);
        }
    }

    @GetMapping("/submitPbc")
    public ResultDto submitPbc(String billNo) {
        return pbcApiService.submitPbc(billNo, false);
    }
    @PostMapping("/reenterablePbcSyncImage")
    public ResultDto reenterablePbcSyncImage(String organCode, AllBillsPublicDTO billsPublic, ImageBatchInfo batchInfo) {
        return pbcApiService.reenterablePbcSync(organCode, billsPublic,batchInfo);
    }
}
