package org.ypq.newcitibet.task;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.ypq.newcitibet.data.BetEatStatus;
import org.ypq.newcitibet.data.MaxDiscount;
import org.ypq.newcitibet.data.TableData;
import org.ypq.newcitibet.data.Type;
import org.ypq.newcitibet.utils.CitibetUtils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 
* @ClassName: GetAndShowData 
* @Description: 继承自BaseCitibet,该任务用于获取和显示数据
* @author god
* @date 2017年2月22日 下午12:35:53
 */
public class GetAndShowData extends BaseCitibet {

    @Autowired
    @Qualifier(value="winBetStatus")
    private BetEatStatus winBetStatus;
    
    @Autowired
    @Qualifier(value="winEatStatus")
    private BetEatStatus winEatStatus;
    
    @Autowired
    @Qualifier(value="placeBetStatus")
    private BetEatStatus placeBetStatus;
    
    @Autowired
    @Qualifier(value="placeEatStatus")
    private BetEatStatus placeEatStatus;
    
    @Autowired
    @Qualifier(value="followWinBetStatus")
    private BetEatStatus followWinBetStatus;
    
    @Autowired
    @Qualifier(value="followWinEatStatus")
    private BetEatStatus followWinEatStatus;
    
    /**
     * 获取后的数据要显示的数据表名
     */
    private String tableName;
    
    private static final Logger logger = LoggerFactory.getLogger(GetAndShowData.class);
    
    /**
     * 定时任务队列里就是执行该函数
     */
    @Override
    public void doCitibet() {
        Request request = new Request.Builder().url(CitibetUtils.getUrl4GetAndShow(type)).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //1.将数据进行解析,根据showTableMapping映射关系显示到表格上
                TableData tableData = CitibetUtils.ParseData(body);
                tableData.showOnTable(tableName);
                //2.对于两种赌数据而言,还需要获取最大折扣
                if (type == Type.WIN_BET || type == Type.PLACE_BET) {
                    MaxDiscount maxDiscount = new MaxDiscount();
                    tableData.findMaxDiscount(maxDiscount);
                    maxDiscount.toWriteFile(type, "记录数据.txt");
//                    logger.error("\n type={}, \n maxdiscount={}", type.getName(), maxDiscount.toString());
                    //3.对于win赌,分别计算是否符合win的'赌','吃'和跟'赌'的条件
                    if (type == Type.WIN_BET) {
                        maxDiscount.calStatus(winBetStatus);
                        maxDiscount.calStatus(winEatStatus);
                        maxDiscount.calStatus(followWinBetStatus);
                        maxDiscount.calStatus(followWinEatStatus);
//                        logger.error("\n winBet={}", winBetStatus.toString());
//                        logger.error("\n winEat={}", winEatStatus.toString());
//                        logger.error("\n followWinBet={}", followWinBetStatus.toString());
//                        logger.error("\n followWinEat={}", followWinEatStatus.toString());
                    } else {
                        //4.对于place赌,分别计算是否符合place的'赌','吃'和跟'吃'的条件
                        maxDiscount.calStatus(placeBetStatus);
                        maxDiscount.calStatus(placeEatStatus);
//                        logger.error("\n placeBet={}", placeBetStatus.toString());
//                        logger.error("\n placeEat={}", placeEatStatus.toString());
                        
                    }
                }
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("doCitibet tpye={} 失败 !", type);
            }

        });

    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
