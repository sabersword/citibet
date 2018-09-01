package org.ypq.newcitibet.event;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 
* @ClassName: Space2PlusDocument 
* @Description: 将输入文本框输入的空格改成+
* @author god
* @date 2017年5月7日 下午12:06:50
 */
public class Space2PlusDocument extends PlainDocument {

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str.equals(" ")) 
            str = "+";
        super.insertString(offs, str, a);
    }
}
