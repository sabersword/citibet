package org.ypq.newcitibet.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.ypq.newcitibet.data.Type;

import okhttp3.OkHttpClient;



/**
 * 
* @ClassName: BaseCitibet 
* @Description: 所有关于citibet业务处理的抽象类,定义了httpclient,界面frame,日期,场次,比赛类型等共用变量
* @author god
* @date 2017年2月20日 下午6:41:28
 */
public abstract class BaseCitibet {

    /**
     * 共用的httpclient
     */
    @Autowired
    protected OkHttpClient client;

    /**
     * 基本上所有http定时任务都需要用到登陆获取的JSESSIONID,用来作为http请求的标识
     */
    protected static String jsessionid = "";
    
    protected String date;
    protected String raceType;
    protected String race;
    
    /**
     * 用来标识这个http任务属于什么类型,目前总共有6种
     */
    protected Type type;
    
    public BaseCitibet() {

    }
    
    /**
     * 
    * @Title: doCitibet 
    * @Description: 用来处理citibet的相关业务,包括获取数据,订单,赌,吃等
    * @return void   
    * @throws
     */
    public abstract void doCitibet();

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static String getJsessionid() {
        return jsessionid;
    }

    public static void setJsessionid(String jsessionid) {
        BaseCitibet.jsessionid = jsessionid;
    }
    
}
