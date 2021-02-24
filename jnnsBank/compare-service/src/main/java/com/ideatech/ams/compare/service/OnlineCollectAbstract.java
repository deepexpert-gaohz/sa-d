package com.ideatech.ams.compare.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Description 在线采集虚拟类
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
public abstract class OnlineCollectAbstract implements OnlineCollectService{

    private List<Future<Long>> futureList;

    @Override
    public void clearFuture() {
        if(futureList !=null && futureList.size()>0){
            for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Long> future = iterator.next();
                if(future.isDone()){
                    iterator.remove();
                }else{
                    future.cancel(true);
                    iterator.remove();
                }
            }
        }
        futureList = new ArrayList<Future<Long>>();
    }


    /**
     * 判断采集是否完成
     *
     * @param taskId
     * @throws Exception
     */
    @Override
    public void valiCollectCompleted(final Long taskId) throws Exception {
        while(futureList.size()>0){
            for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Long> future = iterator.next();
                if(future.isDone()){
                    iterator.remove();
                }
            }
            // 暂停1分钟
            TimeUnit.MINUTES.sleep(1);
        }
    }
}
