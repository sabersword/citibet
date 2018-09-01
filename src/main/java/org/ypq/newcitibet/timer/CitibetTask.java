package org.ypq.newcitibet.timer;

import java.util.List;
import java.util.TimerTask;

import org.ypq.newcitibet.task.BaseCitibet;

/**
 * 
* @ClassName: GetDataTask 
* @Description: 定时任务,执行任务队列中的doCitibet方法
* @author god
* @date 2017年2月22日 下午12:38:54
 */
public class CitibetTask extends TimerTask{

    private List<BaseCitibet> taskQueue;
    
    public CitibetTask() {
    }
    
    @Override
    public void run() {
        for (BaseCitibet bc : taskQueue) {
            bc.doCitibet();
        }
    }

    public List<BaseCitibet> getTaskQueue() {
        return taskQueue;
    }

    public void setTaskQueue(List<BaseCitibet> taskQueue) {
        this.taskQueue = taskQueue;
    }

}
