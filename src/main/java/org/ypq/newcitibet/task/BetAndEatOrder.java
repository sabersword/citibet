package org.ypq.newcitibet.task;

import java.io.IOException;
import java.util.Map;

import javax.swing.JSpinner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ypq.newcitibet.data.BetEatStatus;
import org.ypq.newcitibet.data.Status;
import org.ypq.newcitibet.data.Type;
import org.ypq.newcitibet.utils.CitibetUtils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 
* @ClassName: BetAndEatOrder 
* @Description: 继承自BaseCitibet,专门用于赌和吃的任务
* @author god
* @date 2017年2月22日 下午12:34:12
 */
public class BetAndEatOrder extends BaseCitibet {
    
    /**
     * 根据type(4种)映射到要下单的status
     */
    private static Map<Type, BetEatStatus> statusMapping;
    
    /**
     * 下单的status
     */
    private BetEatStatus status;
    
    /**
     * 对于下订单的http任务,需要查找下订单金额
     */
    private int amount;
    
    private static final Logger logger = LoggerFactory.getLogger(BetAndEatOrder.class);
    
    /**
     * 定时任务队列里就是执行该函数
     */
    @Override
    public void doCitibet() {
        for (String horse : CitibetUtils.getAllHorse()) {
            if (status.getStatus(horse) == Status.SATISFIED) {
                status.setStatus(horse, Status.FINISHED);
                amount = (Integer) ((JSpinner) CitibetUtils.getComponentByName("spinner" + type.getName() + "Amount")).getValue();
                String hss = horse.replace('-', '_');   //下单的马竟然是下划线
                logger.error("下单的网址是{}", CitibetUtils.getUrl4BetAndEat(type) + "&Tix=" + amount + "&Hss=" + hss);
                Request request = new Request.Builder().url(CitibetUtils.getUrl4BetAndEat(type) + "&Tix=" + amount + "&Hss=" + hss).addHeader("Cookie", "JSESSIONID=" + jsessionid).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        logger.error(body);
                    }
                    
                    @Override
                    public void onFailure(Call call, IOException e) {
                        logger.error("下单{} 失败 !", type);
                    }
                });
            }
        }
    }

    public BetEatStatus getStatus() {
        return status;
    }

    public void setStatus(BetEatStatus status) {
        this.status = status;
    }

}
