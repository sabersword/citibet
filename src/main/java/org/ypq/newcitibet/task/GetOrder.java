package org.ypq.newcitibet.task;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ypq.newcitibet.data.TableData;
import org.ypq.newcitibet.utils.CitibetUtils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 
* @ClassName: GetOrder 
* @Description: 继承自BaseCitibet,用于获取订单信息(顺带获取余额,输赢等辅助信息)
* @author god
* @date 2017年2月22日 下午12:36:48
 */
public class GetOrder extends BaseCitibet {

    /**
     * 依次保存辅助信息:信用余额,输赢,开始时间,剩余时间
     */
    private List<String> blanceAndTime;
    /**
     * 标签的对应关系
     */
    private static List<String> labelNameMapping;
    private static final Logger logger = LoggerFactory.getLogger(GetOrder.class);
    
    public GetOrder() {
        labelNameMapping = new ArrayList<String>();
        labelNameMapping.add("lblBlance");
        labelNameMapping.add("lblPL");
        labelNameMapping.add("lblStartTime");
        labelNameMapping.add("lblLeftTime");
    }
    
    /**
     * 定时任务队列里就是执行该函数
     */
    @Override
    public void doCitibet() {
        Request request = new Request.Builder().url(CitibetUtils.getUrl4Order()).addHeader("Cookie", "JSESSIONID=" + jsessionid).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //将订单数据显示在订单表上
                TableData qOrderData = CitibetUtils.ParseOrder(body);
                qOrderData.showOnTable("qOrderTable");
                //抓取输赢和时间,显示到label上
                blanceAndTime = CitibetUtils.ParseBlanceAndTime(body);
                showBlanceAndTime();
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("getOrder 失败 !");
            }
        });
    }
    
    public void showBlanceAndTime() {
        Iterator<String> bats = blanceAndTime.iterator();
        Iterator<String> lnm = labelNameMapping.iterator();
        while (bats.hasNext() && lnm.hasNext()) {
            Component component = CitibetUtils.getComponentByName(lnm.next());
            if (component == null) {
                logger.error("can not find {}", lnm.next());
                return;
            }
            JLabel l = (JLabel) component;
            l.setText(bats.next());
            //如果是输赢的话还要改变颜色
            if (l.getName().equals("lblPL") && l.getText().contains(")"))
                l.setForeground(Color.RED);
            else
                l.setForeground(Color.BLACK);
        }
    }

}
