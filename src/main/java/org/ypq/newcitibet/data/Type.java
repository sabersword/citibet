package org.ypq.newcitibet.data;

/**
 * 
* @ClassName: Type 
* @Description: 表示了各种不同形式的下单,这里包括了6种
* @author god
* @date 2017年2月22日 下午12:31:51
 */
public enum Type {

    WIN_BET("WinBet"),
    WIN_EAT("WinEat"),
    PLACE_BET("PlaceBet"),
    PLACE_EAT("PlaceEat"),
    FOLLOW_WIN_BET("FollowWinBet"),
    FOLLOW_WIN_EAT("FollowWinEat");
    
    /**
     * 根据不同形式对应的名字,找到相应的前台控件条件
     */
    private String name;
    
    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
