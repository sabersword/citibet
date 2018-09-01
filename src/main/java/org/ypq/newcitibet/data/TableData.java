package org.ypq.newcitibet.data;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ypq.newcitibet.utils.CitibetUtils;

/**
 * 
* @ClassName: TableData 
* @Description: 对应着JTable的内容 
* @author god
* @date 2017年2月22日 下午12:25:35
 */
public class TableData {

    private List<RowData> tableData = new ArrayList<RowData>();
    
    private static final Logger logger = LoggerFactory.getLogger(TableData.class);
    
    public TableData() {
        
    }
    
    public void addRow(RowData rowDate) {
        tableData.add(rowDate);
    }
    
    public List<RowData> getData() {
        return tableData;
    }

    /**
     * 
    * @Title: showOnTable 
    * @Description: 将表数据显示到表格上
    * @param  tableName 要显示到的表格Name
    * @param  container 要显示到的表格所在的容器
    * @return void    返回类型 
    * @throws
     */
    public void showOnTable(String tableName) {
        Component component = CitibetUtils.getComponentByName(tableName);
        if (component == null) {
            logger.error("can not find {}", tableName);
            return;
        }
        JTable table = (JTable) component;
        
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);
        for (RowData rd : tableData) {
            tableModel.addRow(rd.toVector());
        }
    }
    
    /**
     * 
    * @Title: findMaxDiscount 
    * @Description: 根据表格的数据,将各种马组合的最大折扣放回最大折扣类
    * @param md   最大折扣类
    * @return void     
    * @throws
     */
    public void findMaxDiscount(MaxDiscount md) {
        for (String horseCombination : CitibetUtils.getAllHorse()) {
            md.setMaxDiscountByHorse(horseCombination, findMaxDiscount(horseCombination));
        }

    }
    
    /**
     * 
    * @Title: findMaxDiscount 
    * @Description: 根据马组合在这张表格数据里找到折扣最大的
    * @param horseCombination 马组合
    * @return Double   最大折扣
    * @throws
     */
    public Double findMaxDiscount(String horseCombination) {
        double max = 0;
        for (RowData rd : tableData) {
            if (rd.getHorse().equals(horseCombination) && Double.valueOf(rd.getDiscount()) > max) 
                max = Double.valueOf(rd.getDiscount());
        }
        return max;
    }
}
