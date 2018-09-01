package org.ypq.newcitibet.event;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.regex.Pattern;

import javax.swing.JTextField;

/**
 * 
* @ClassName: MultiHorseAdapter 
* @Description: 校验文本框的马是否1+2+3+14的形式 
* @author god
* @date 2017年5月7日 下午12:05:16
 */
public class MultiHorseAdapter extends FocusAdapter{

    @Override
    public void focusLost(FocusEvent e) {
        JTextField tf = (JTextField) e.getSource();
        if (!Pattern.matches("^(1[0-4]|[1-9])(\\+(1[0-4]|[1-9]))*$", tf.getText()))
            tf.setText("");
    }
}
