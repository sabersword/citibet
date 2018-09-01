package org.ypq.newcitibet.data;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.ypq.newcitibet.utils.CitibetUtils;

/**
 * 
* @ClassName: BetEatStatus 
* @Description: 这个类是对不同形式的下单(目前6种)进行记录,判断该种形式的下单是否符合条件?是否已经下? 
* @author god
* @date 2017年2月22日 上午11:32:34
 */
public class BetEatStatus {

    /**
     * 用来分辨不同形式的下单
     */
    private Type type;
    
    /**
     * 这个类除了记录下单是否符合条件?是否已经下?,还要记录用户有没按下允许下单按钮
     */
    private AtomicBoolean isAllow;
    
    /**
     * 马组合映射到马组合的下单状态,采用HashTable保证线程安全
     */
    private Map<String, Status> betEatStatus = new Hashtable<String, Status>();
    
    public BetEatStatus() {
        
    }
    
    @PostConstruct
    public void initialize() {
        clear();
    }
    
    public Status getStatus(String horse) {
        return betEatStatus.get(horse);
    }
    
    public void setStatus(String horse, Status status) {
        betEatStatus.put(horse, status);
    }
    
    @Override
    public String toString() {
        String result = System.lineSeparator();
        for (String s : CitibetUtils.getAllHorse()) { 
            if (getStatus(s) == Status.SATISFIED) {
                result += s + "应该要下单" + System.lineSeparator();
            }
            if (getStatus(s) == Status.FINISHED) {
                result += s + "已经下了单" + System.lineSeparator();
            }
        }
        return result;
    }
    
    public void clear() {
        for (String s : CitibetUtils.getAllHorse()) { 
            setStatus(s, Status.UNSATISFIED);
        }
        isAllow.set(false);
    }
    
    /**
     * 
    * @Title: getAllow 
    * @Description: 返回用户有没按下按钮允许下单
    * @return boolean    true:允许    false:不允许
    * @throws
     */
    public boolean getAllow() {
        return isAllow.get();
    }
    
    /**
     * 
    * @Title: setAllow 
    * @Description: 根据用户的按钮时间设置 是否允许下单
    * @param  allow    true:允许    false:不允许
    * @throws
     */
    public void setAllow(boolean allow) {
        isAllow.set(allow);
    }
    
    public String getType() {
        return type.getName();
    }
    public void setType(Type type) {
        this.type = type;
    }

    public AtomicBoolean getIsAllow() {
        return isAllow;
    }

    public void setIsAllow(AtomicBoolean isAllow) {
        this.isAllow = isAllow;
    }

    
    
}
