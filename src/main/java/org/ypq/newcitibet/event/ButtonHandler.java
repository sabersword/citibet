package org.ypq.newcitibet.event;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ypq.newcitibet.data.BetEatStatus;

/**
 * 
* @ClassName: ButtonHandler 
* @Description: button的管理类,控制button的显示,button和其状态的映射关系(暂时放在这,但是感觉不太好) 
* @author god
* @date 2017年2月21日 上午11:06:33
 */
public class ButtonHandler extends MouseAdapter {

    private BetEatStatus bes;
    private static final Logger logger = LoggerFactory.getLogger(ButtonHandler.class);
    
    @Override
    public void mouseClicked(MouseEvent e) {
        JButton button = (JButton) e.getComponent();
        try {
            if (bes.getAllow() == false) {
                bes.setAllow(true);
                button.setBackground(Color.RED);
                button.setText("停止" + button.getText().substring(2));
            } else {
                bes.setAllow(false);
                if (button.getName().endsWith("Bet"))       //如果是赌的按钮,按键没按下的情况下是蓝色,吃的按钮没按下情况下是橙色
                    button.setBackground(Color.CYAN);
                else
                    button.setBackground(Color.ORANGE);
                button.setText("开始" + button.getText().substring(2));
            }
        } catch (IndexOutOfBoundsException exception) {
            logger.error(exception.getMessage());
        }
    }

    public BetEatStatus getBes() {
        return bes;
    }

    public void setBes(BetEatStatus bes) {
        this.bes = bes;
    }
    
}
