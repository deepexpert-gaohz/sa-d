package com.ideatech.ams.system.Initializer;

/**
 * @author van
 * @date 11:02 2018/7/5
 */
public interface DataInitializerService {

    /**
     * 打印字典
     * @return
     */
    StringBuffer printDictionaryList();

    /**
     * 打印字典详细项
     * @return
     */
    StringBuffer printOptionList();

    /**
     * 打印机构
     * @return
     */
    StringBuffer printOrganList();

    /**
     * 打印用户
     * @return
     */
    StringBuffer printUserList();

}
