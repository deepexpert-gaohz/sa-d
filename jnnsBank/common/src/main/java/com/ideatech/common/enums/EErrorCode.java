/*
 * Project Name: standard-code-base-trunk
 * File Name: EBizError.java
 * Class Name: EBizError
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ideatech.common.enums;

import com.ideatech.common.exception.DisplayableError;

import java.util.Arrays;



/**
 * Class Name: EBizError Description: business errors which may be recoverable, or should be displayed on web page.
 *
 * @author SC
 */
public enum EErrorCode implements DisplayableError {
    // Default error
    COMM_INTERNAL_ERROR("COMM0001"),
    TECH_PARAM_REQUIRED("TECH0001","TECH0001"),
    TECH_DATA_NOT_EXIST("TECH0002","TECH0002"),
    TECH_DATA_INVALID("TECH0003","TECH0003"),
    TECH_OPTIMISTIC_LOCK("TECH0004","TECH0004"),
    VERSION_ERROR("TECH0005","TECH0005"),


    /**
     * 机构异常
     */
    //机构未配置
    ORGAN_NOTCONFIG("ORG0001"),
    //机构未配置人行用户秘密吗
    ORGAN_AMS_USER_NOTCONFIG("ORG0002"),
    ORGAN_AMS_USER_EMPTY("ORG0003"),
    ORGAN_AMS_USER_MUST2LEVEL("ORG0004"),
    ORGAN_AMS_USER_IP_EMPTY("ORG0005"),

    ORGAN_ECCS_USER_NOTCONFIG("ORG0101"),
    ORGAN_ECCS_USER_EMPTY("ORG0102"),
    ORGAN_ECCS_USER_MUST2LEVEL("ORG0104"),
    ORGAN_ECCS_USER_IP_EMPTY("ORG0105"),

    //存在未完成流水
    BILL_UN_FINISHED("BILL0001"),
    BILL_UPDATE_FINALSTATUS_ERROR("BILL0002"),
    BILL_UPDATE_APPROVESTATUS_ERROR("BILL0003"),

    ACCT_DATA_NOT_EXIST("ACCT0001"),

    /**
     * 人行相关异常
     */
    //人行查询失败
    PBC_QUERY_ERROR("PBC0001"),
    PBC_QUERY_PARAM_EMPTY("PBC0002"),
    PBC_SYNC_ECCS_NONSUPPORT("PBC0003"),
    PBC_SYNC_ACCTTYPE_ERROR("PBC0004"),
    PBC_SYNC_FAILURE("PBC0005"),//上报人行失败
    PBC_ECCS_SYNC_FAILURE("PBC0006"),//上报人行和上报信用代码都失败
    ECCS_SYNC_FAILURE("PBC0007"),//上报信用代码失败

    /**
     * 核心相关异常
     */
    CORE_SYNC_FAILURE("CORE0001"),

    SYSTEM_ERROR("TECH9999","TECH9999");
    private String errorCode;

    // this field is only for display, do not set it if it is not needed.
    private String displayMsg;

    private Object[] args;

    private static final String DEFAULT_ERROR_MSG = "error.common.unknown";

    EErrorCode(String errorCode, String displayMsg) {
        this.errorCode = errorCode;
        this.displayMsg = displayMsg;
    }

    EErrorCode(String errorCode, String displayMsg, String[] args) {
        this.errorCode = errorCode;
        this.displayMsg = displayMsg;
        if (args != null) {
            this.args = Arrays.copyOf(args, args.length);
        }
    }

    EErrorCode(String errorCode) {
        this.errorCode = errorCode;
        this.displayMsg = DEFAULT_ERROR_MSG;
    }

    @Override
    public boolean isBizError() {
        return !displayMsg.equals(DEFAULT_ERROR_MSG);
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getDisplayMsg() {
        return displayMsg;
    }

    public void setDisplayMsg(String displayMsg) {
        this.displayMsg = displayMsg;
    }

    /**
     * @return return the value of the var args
     */
    @Override
    public Object[] getArgs() {
        return args;
    }

    /**
     * set dynamic args for the message template
     *
     * @param args Set args value
     */
    public DisplayableError setArgs(Object... args) {
        if (args != null) {
            this.args = Arrays.copyOf(args, args.length);
        }
        return this;
    }

}
