package org.ypq.newcitibet.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JSpinner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ypq.newcitibet.event.ButtonHandler;
import org.ypq.newcitibet.task.GetAndShowData;
import org.ypq.newcitibet.utils.CitibetUtils;

/**
 * 
* @ClassName: MaxDiscount 
* @Description: 记录了所有马组合的最大折扣 
* @author god
* @date 2017年2月17日 下午4:04:57
 */
public class MaxDiscount {

    /**
     * 马组合映射到该组合的最大折扣
     */
    private Map<String, Double> maxDiscount = new HashMap<String, Double>();
    private static String fileData = "";
    private static final Logger logger = LoggerFactory.getLogger(MaxDiscount.class);
    
    public MaxDiscount() {
        clear();
    }
    
    public void clear() {
        for (String horseCombination : CitibetUtils.getAllHorse()) {
            maxDiscount.put(horseCombination, 0d);     //初始折扣初始化为0
        }
    }
    
    public void setMaxDiscountByHorse(String horse, Double discount) {
        maxDiscount.put(horse, discount);
    }
    
    @Override
    public String toString() {
        String result = System.lineSeparator();
        for (String horse : maxDiscount.keySet()) {
            if (maxDiscount.get(horse) != 0d)
            result += horse + "---" + maxDiscount.get(horse) + System.lineSeparator();
        }
        return result;
    }
    
    /**
     * 
    * @Title: calStatus 
    * @Description: 根据容器提供的条件,找到哪些形式是符合下单的
    * @param  bes 针对(6种)形式中一种的记录下单状态
    * @param  container   容器,需要提供容器去找到设定的条件
    * @return void    返回类型 
    * @throws
     */
    public void calStatus(BetEatStatus bes) {
        double bottom = (double) ((JSpinner) CitibetUtils.getComponentByName("spinner" + bes.getType() + "1")).getValue();
        double top = (double) ((JSpinner) CitibetUtils.getComponentByName("spinner" + bes.getType() + "2")).getValue();
//        AtomicBoolean isAllow = ButtonHandler.getAllowByName("btn" + bes.getType());
        for (String s : CitibetUtils.getAllHorse()) {       //判断是否在折扣的区间中
            if (bes.getStatus(s) == Status.UNSATISFIED
                    && bes.getAllow() == true
                    && maxDiscount.get(s) >= bottom
                    && maxDiscount.get(s) <= top) {
                bes.setStatus(s, Status.SATISFIED);
            }
        }
        
    }
    

    public void toWriteFile(Type type, String fileName) {
        toFileFormat(type);
        if (!fileData.contains(type.PLACE_BET.getName()))
            return;
        File file =new File(fileName);
        BufferedWriter bufferWritter = null;
        try {
                if (!file.exists()) {
                    file.createNewFile();
            }
            
            FileWriter fileWritter;
            //文件的记录是先WIN后PLACE,如果是WIN,则清空内容. 如果是PLACE,则在后面添加
//            if (type == Type.WIN_BET)
                fileWritter = new FileWriter(file.getName());
//            else
//                fileWritter = new FileWriter(file.getName(), true);
            bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(fileData);
            fileData = "";
            
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (bufferWritter != null)
                try {
                    bufferWritter.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
        }
        
    }
    
    
    public void toFileFormat(Type type) {
        if (fileData.contains(type.WIN_BET.getName()) && fileData.contains(type.PLACE_BET.getName())) 
            return;
        if (!fileData.contains(type.WIN_BET.getName()) && type == type.WIN_BET) {
            fileData = type.WIN_BET.getName() + System.lineSeparator();
            for (String horse : CitibetUtils.getAllHorse()) {
                if (maxDiscount.get(horse) != 0d) {
                    fileData += horse;
                    if (horse.length() <= 3)
                        fileData += "\t\t";
                    else
                        fileData += "\t";
                    fileData += maxDiscount.get(horse) + System.lineSeparator();
                }
            }
            fileData += System.lineSeparator();
        } else if (fileData.contains(type.WIN_BET.getName()) && type == type.PLACE_BET){
            fileData += type.PLACE_BET.getName() + System.lineSeparator();
            for (String horse : CitibetUtils.getAllHorse()) {
                if (maxDiscount.get(horse) != 0d) {
                    fileData += horse;
                    if (horse.length() <= 3)
                        fileData += "\t\t";
                    else
                        fileData += "\t";
                    fileData += maxDiscount.get(horse) + System.lineSeparator();
                }
            }
        }
        
    }
    
}
