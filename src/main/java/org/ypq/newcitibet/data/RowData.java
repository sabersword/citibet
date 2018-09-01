package org.ypq.newcitibet.data;

import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RowData {

    protected String race;
    protected String horse;
    protected String amount;
    protected String discount;
    protected String limit;
    
    /**
     * 每一行赌吃数据为5列
     */
    protected static final int COUNT = 5;
    protected static final Logger logger = LoggerFactory.getLogger(RowData.class);
    public RowData() {
        
    }
    
    /**
     * 将解析得到的字符串数组构造成一个rowdata
     * @param row  解析得到的字符串数组
     */
    public RowData(String[] row) {
        if (row.length != COUNT) {
            logger.error("{} length != {}", row, COUNT);    //如果是取订单的话有6列,先去除该判断
        }
        this.race = row[0];
        this.horse = row[1].replaceAll("\\(|\\)", "");      //去掉前后括号不要了
        this.amount = row[2];
        this.discount = row[3];
        this.limit = row[4];
    }
    
    /**
     * 
    * @Title: toVector 
    * @Description: 将一个rowdata转换成Vector,以便在JTable上显示(JTable可以用Vector来设置某一行数据)
    * @return Vector<String>    
    * @throws
     */
    public Vector<String> toVector() {
        Vector<String> v = new Vector<String>();
        v.addElement(race);
        v.addElement(horse);
        v.addElement(amount);
        v.addElement(discount);
        v.addElement(limit);
        return v;
    }
    
    public String getAmount() {
        return amount;
    }
    public String getDiscount() {
        return discount;
    }
    public String getHorse() {
        return horse;
    }
    public String getLimit() {
        return limit;
    }
    public String getRace() {
        return race;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public void setDiscount(String discount) {
        this.discount = discount;
    }
    public void setHorse(String horse) {
        this.horse = horse;
    }
    public void setLimit(String limit) {
        this.limit = limit;
    }
    public void setRace(String race) {
        this.race = race;
    }
    
}
