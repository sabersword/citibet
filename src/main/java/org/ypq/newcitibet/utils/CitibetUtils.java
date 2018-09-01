package org.ypq.newcitibet.utils;

import java.awt.Component;
import java.awt.Container;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ypq.newcitibet.data.OrderRowData;
import org.ypq.newcitibet.data.RowData;
import org.ypq.newcitibet.data.TableData;
import org.ypq.newcitibet.data.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
* @ClassName: CitibetUtils 
* @Description: citibet工具类,包含了解析数据,查找界面控件,生成所有马组合功能
* @author god
* @date 2017年2月22日 下午12:39:41
 */
public class CitibetUtils {

    /**
     * 下面这些都是正则表达式匹配的常量
     */
    private static final String TABLE = "\\\\t";
    private static final String RACE = "\\d{1,2}";
    private static final String HORSE = "(\\(\\d+-\\d+\\)|\\d+-\\d+)";
    private static final String AMOUNT = "\\d+";
    private static final String DISCOUNT = "\\d+(\\.\\d+)?";
    private static final String LIMIT = "\\d+";
    
    private static final String SHARP = "#";
    
    private static final String BLANCE_TAG = "(?<=<pre id=AC_BAL>)";
    private static final String PL_TAG = "(?<=<pre id=AC_PL>)";
    private static final String LOSE_TAG = "<span  class=\"RD\">";
    private static final String START_TIME_TAG = "(?<=<pre id=txtTOTE>)";
    private static final String LEFT_TIME_TAG = "(?<=<pre id=txtTIMER>)";
    private static final String END_TAG = "(?=</span>|</pre>)";
    private static final String BLANCE_AMOUNT = "[\\$\\d,\\.\\(\\)]+?";      //输的金额有()符号
    
    private static final int HORSE_COUNT = 14;
    
    private static List<String> allHorse;
    
    /**
     * 赌吃URL的映射,因为赌吃URL差太远,所以用映射来区分
     */
    private static Map<Type, String> urlMapping4BetAndEat;
    
    /**
     * 界面容器,用来获取界面控件的值
     */
    private static Container container;
    
    private static OkHttpClient client;
    
    /**
     * 在此有效期前可以正常使用
     */
    private static final String limitDate = "20171015";
    
    /**
     * 基本上所有http定时任务都需要用到登陆获取的JSESSIONID,用来作为http请求的标识
     */
    protected static String jsessionid = "";
    
    private static final Logger logger = LoggerFactory.getLogger(CitibetUtils.class);
    
    @PostConstruct
    public void initialze() {
        String baseUrl4BetAndEat = "http://web.ctb988.net/forecast?task=betBox&combo=0&Q=Q&overflow=1&rd=0.7646124131121313";
        String winUrl = baseUrl4BetAndEat + "&fctype=0&fclmt=700";
        String placeUrl = baseUrl4BetAndEat + "&fctype=1&fclmt=400";
        String betWinUrl = winUrl + "&type=BET&amount=100";
        String eatWinUrl = winUrl + "&type=EAT&amount=80";
        String betPlaceUrl = placeUrl + "&type=BET&amount=100";
        String eatPlaceUrl = placeUrl + "&type=EAT&amount=80";
        String followBetWinUrl = placeUrl + "&type=BET&amount=100"; // 跟随赌
        String followEatWinUrl = placeUrl + "&type=EAT&amount=80"; // 跟随吃

        urlMapping4BetAndEat = new HashMap<Type, String>();
        urlMapping4BetAndEat.put(Type.WIN_BET, betWinUrl);
        urlMapping4BetAndEat.put(Type.WIN_EAT, eatWinUrl);
        urlMapping4BetAndEat.put(Type.PLACE_BET, betPlaceUrl);
        urlMapping4BetAndEat.put(Type.PLACE_EAT, eatPlaceUrl);
        urlMapping4BetAndEat.put(Type.FOLLOW_WIN_BET, followBetWinUrl);
        urlMapping4BetAndEat.put(Type.FOLLOW_WIN_EAT, followEatWinUrl);
    }

    /**
     * 
    * @Title: ParseData 
    * @Description: 解析赌/吃数据 
    * @param source 原文数据
    * @return TableData    
    * @throws
     */
    public static TableData ParseData(String source) {
        Matcher row = Pattern.compile(RACE + TABLE + HORSE + TABLE + AMOUNT + TABLE + DISCOUNT + TABLE + LIMIT)
                .matcher(source);
        TableData tableData = new TableData();
        while (row.find()) {
            String rowData = row.group();
            String[] strings = rowData.split(TABLE);
            tableData.addRow(new RowData(strings));
        }
        return tableData;
    }

    /**
     * 
    * @Title: ParseOrder 
    * @Description: 解析订单数据
    * @param source 原文数据
    * @return TableData    解析后的表格数据
    * @throws
     */
    public static TableData ParseOrder(String source) {
        Matcher row = Pattern.compile("[BE]" + SHARP + RACE + SHARP + HORSE + SHARP + AMOUNT + SHARP + DISCOUNT + SHARP + LIMIT)
                .matcher(source);
        TableData tableData = new TableData();
        while (row.find()) {
            String rowData = row.group();
            String[] strings = rowData.split(SHARP);
            tableData.addRow(new OrderRowData(strings));
        }
        return tableData;
    }
    
    /**
     * 
    * @Title: ParseBlanceAndTime 
    * @Description: 解析订单请求里关于信用,输赢,时间等信息
    * @param source 被解析的原文
    * @return List<String>    依次返回信用余额,输赢,开始时间,剩余时间 
    * @throws
     */
    public static List<String> ParseBlanceAndTime(String source) {
        List<String> strings = new ArrayList<String>();  
        //1.首先匹配信用余额
        Matcher blanceMatcher = Pattern.compile(BLANCE_TAG + "RMB" + BLANCE_AMOUNT + END_TAG)
                .matcher(source);
        String blance;
        if (!blanceMatcher.find()) {
            logger.error("匹配不到信用余额");
            blance = "";
        } else {
            blance = blanceMatcher.group();
        }
        strings.add("信用余额  " + blance);
        //2.再匹配输赢
        Matcher plMatcher = Pattern.compile(PL_TAG + "RMB" + "(" + LOSE_TAG + ")?" + BLANCE_AMOUNT + END_TAG)
                .matcher(source);
        String pl;
        if (!plMatcher.find()) {
            logger.error("匹配不到输赢");
            pl = "";
        } else {
            pl = plMatcher.group();
        }
        pl = pl.replaceAll(LOSE_TAG, "");
        strings.add("输赢  " + pl);
        //3.再匹配开始时间
        Matcher startTimeMatcher = Pattern.compile(START_TIME_TAG + "[\\w:]+" + "(?= - )")
                .matcher(source);
        String startTime;
        if (!startTimeMatcher.find()) {
            logger.error("匹配不到开始时间");
            startTime = "";
        } else {
            startTime = startTimeMatcher.group();
        }
        strings.add("开始时间  " + startTime);
        //4.最后匹配剩余时间
        Matcher leftTimeMatcher = Pattern.compile(LEFT_TIME_TAG + "\\d+" + END_TAG)
                .matcher(source);
        String leftTime;
        if (!leftTimeMatcher.find()) {
            logger.error("匹配不到剩余时间");
            leftTime = "";
        } else {
            leftTime = leftTimeMatcher.group();
        }
        strings.add("剩余时间  " + leftTime + "分钟");
        
        return strings;
    }

    /**
     * 
    * @Title: getComponentByName 
    * @Description: 根据控件的名字查找并返回该控件
    * @param componentName 控件名字
    * @return Component 返回的控件
    * @throws
     */
    public static Component getComponentByName(String componentName) {
        Component[] components = container.getComponents();
        for (Component c : components) {
            if (c != null && c.getName() != null && c.getName().equals(componentName)) {
                return c;
            }
            if (c instanceof Container) { // 递归查找所有container
                Component result = getComponentByName(componentName, (Container) c);
                if (result != null)
                    return result;
            }
        }
        return null;
    }
    
    /**
     * 
    * @Title: getComponentByName 
    * @Description: 递归查找控件
    * @param componentName 控件名字
    * @param container 子容器
    * @return Component    返回的控件
    * @throws
     */
    public static Component getComponentByName(String componentName, Container container) {
        Component[] components = container.getComponents();
        for (Component c : components) {
            if (c != null && c.getName() != null && c.getName().equals(componentName)) {
                return c;
            }
            if (c instanceof Container) { // 递归查找所有container
                Component result = getComponentByName(componentName, (Container) c);
                if (result != null)
                    return result;
            }
        }
        return null;
    }

    /**
     * 
    * @Title: getAllHorse 
    * @Description: 生成所有马组合
    * @return List<String>    返回马组合的List 
    * @throws
     */
    public static List<String> getAllHorse() {
        if (allHorse == null) {
            allHorse = new ArrayList<String>();
            for (int i = 1; i <= HORSE_COUNT; i++)
                for (int j = i; j <= HORSE_COUNT; j++) {
                    allHorse.add(String.valueOf(i) + "-" + String.valueOf(j));
                }
        }
        return allHorse;
    }
    
    /**
     * 
    * @Title: getUrl4BetAndEat 
    * @Description: 根据TYPE(6种定时赌吃任务)返回赌吃的url,如此,赌吃url就差金额和马组合
    * @param Type 6种定时赌吃任务
    * @return String  赌吃的URL
    * @throws
     */
    public static String getUrl4BetAndEat(Type type) {
        String raceType = ((JTextField) CitibetUtils.getComponentByName("tbRaceType")).getText();
        String date = ((JTextField) CitibetUtils.getComponentByName("tbDate")).getText();
        String race = (String) ((JComboBox<Object>) CitibetUtils.getComponentByName("cbRace")).getSelectedItem();
        String url = urlMapping4BetAndEat.get(type);
        //限制使用时间
        String year = date.split("-")[2];
        String month = date.split("-")[1];
        String day = date.split("-")[0];
        String yearMonthDay = year + month + day;
        if (yearMonthDay.compareTo(limitDate) > 0)
            date = "";
        
        url += "&race_date=" + date + "&race_type=" + raceType + "&Race=" + race + "&show=" + race;
        return url;
    }
    
    /**
     * 
    * @Title: getUrl4GetAndShow 
    * @Description: 根据TYPE(4种定时获取数据任务)返回获取数据的url,如此,该URL直接可用
    * @param Type 4种定时获取数据任务
    * @return String 获取数据URL 
    * @throws
     */
    public static String getUrl4GetAndShow(Type type) {
        String url = "http://data.ctb988.net/qdata?m=HK&c=3";
        String raceType = ((JTextField) CitibetUtils.getComponentByName("tbRaceType")).getText();
        String date = ((JTextField) CitibetUtils.getComponentByName("tbDate")).getText();
        String race = (String) ((JComboBox<Object>) CitibetUtils.getComponentByName("cbRace")).getSelectedItem();
        String q = "";
        if (type == Type.WIN_BET)
            q = "1";
        else if (type == Type.WIN_EAT)
            q = "2";
        else if (type == Type.PLACE_BET)
            q = "3";
        else if (type == Type.PLACE_EAT)
            q = "4";
        
        url += "&race_date=" + date + "&race_type=" + raceType + "&rc=" + race + "&q=" + q;
        return url;
    }
    
    /**
     * 
    * @Title: getUrl4Order 
    * @Description: 获取订单地址
    * @return String    订单URL
    * @throws
     */
    public static String getUrl4Order() {
        String url = "http://web.ctb988.net/datastore?l=x&q=q&x=0.2849486435661249&tnum=2&txnrnd=0.9464307386003219";
        String raceType = ((JTextField) CitibetUtils.getComponentByName("tbRaceType")).getText();
        String date = ((JTextField) CitibetUtils.getComponentByName("tbDate")).getText();
        String race = (String) ((JComboBox<Object>) CitibetUtils.getComponentByName("cbRace")).getSelectedItem();
        url += "&race_date=" + date + "&race_type=" + raceType + "&rc=" + race;
        return url;
    }
    
    /**
     * 
    * @Title: manualBetAndEat 
    * @Description: 手动去赌或者去吃
    * @param type 赌吃哪种类型
    * @param  amount 金额
    * @param  hss   马组合
    * @throws
     */
    public static void manualBetAndEat(Type type, int amount, String hss) {
        Request request = new Request.Builder().url(getUrl4BetAndEat(type) + "&Tix=" + amount + "&Hss=" + hss).addHeader("Cookie", "JSESSIONID=" + jsessionid).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                logger.error(body);
            }
            
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("手动下单{} 失败 !");
            }
        });
        
    }
    
    /**
     * 
    * @Title: parseMultiHorse 
    * @Description: 1+2+3+14拆成1 2 3 4
    * @param @param multihorse 形如1+2+3+14
    * @return List<String>    单独每只马放在list里 
    * @throws
     */
    public static List<String> parseMultiHorse(String multihorse) {
        String[] tempHorse = {multihorse};
        if (multihorse.contains("+")) 
            tempHorse = multihorse.split("\\+");
        return Arrays.asList(tempHorse);
    }

    public static Container getContainer() {
        return container;
    }

    public static void setContainer(Container container) {
        CitibetUtils.container = container;
    }

    public static OkHttpClient getClient() {
        return client;
    }

    public static void setClient(OkHttpClient client) {
        CitibetUtils.client = client;
    }

    public static String getJsessionid() {
        return jsessionid;
    }

    public static void setJsessionid(String jsessionid) {
        CitibetUtils.jsessionid = jsessionid;
    }

}
