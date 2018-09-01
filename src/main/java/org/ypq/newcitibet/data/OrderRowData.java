package org.ypq.newcitibet.data;

import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderRowData extends RowData {

    /**
     * 订单比普通数据多出的一列,指明该订单是赌还是吃
     */
    protected String betOrEat;
    /**
     * 每一行订单为6列,比普通数据多一列
     */
    protected static final int COUNT = 6;
    protected static final Logger logger = LoggerFactory.getLogger(OrderRowData.class);
    
    /**
     * 将解析得到的字符串数组构造成一个rowdata
     * @param row  解析得到的字符串数组
     */
    public OrderRowData(String[] row) {
        if (row.length != COUNT) {
            logger.error("{} length != {}", row, COUNT);    //如果是取订单的话有6列,先去除该判断
        }
        this.betOrEat = row[0];
        this.race = row[1];
        this.horse = row[2];                               
        this.amount = row[3];
        this.discount = row[4];
        this.limit = row[5];
    }

    /**
     * 将一个rowdata转换成Vector,以便在JTable上显示(JTable可以用Vector来设置某一行数据)
     */
    @Override
    public Vector<String> toVector() {
        Vector<String> v = new Vector<String>();
        v.addElement(betOrEat);
        v.addElement(race);
        v.addElement(horse);
        v.addElement(amount);
        v.addElement(discount);
        v.addElement(limit);
        return v;
    }
    
    

}
