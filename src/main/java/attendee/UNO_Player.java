package attendee;

import card.*;
import cardCollection.*;
import utility.*;

public class UNO_Player {
    protected UNO_CardCollection cards;
    // 玩家是否喊了UNO
    private boolean isUNO = false;

    public UNO_Player() {
        cards = new UNO_CardCollection();
    }

    /** 向玩家手牌中添加一张牌 */
    public void addCard(UNO_Card card) {
        cards.addCard(card);
        isUNO = false;
    }

    /** 从玩家手牌中移除一张牌，若移除失败（无此牌），返回false */
    public boolean removeCard(UNO_Card card) {
        return cards.removeCard(card);
    }

    /** 获取玩家手牌数 */
    public int getCardsNum() {
        return cards.getCards().size();
    }

    /** 获取玩家是否喊过UNO */
    public boolean getIsUNO() {
        return isUNO;
    }

    /** 设置玩家UNO状态 */
    public void setIsUNO(boolean isUNO) {
        this.isUNO = isUNO;
    }

    /** 获取玩家剩余手牌总点数 */
    public int getScore() {
        int result = 0;
        for (UNO_Card card : cards.getCards()) {
            switch (card.getType()) {
                case UNO_Constants.NORMAL:
                    result += card.getPoint();
                    break;
                case UNO_Constants.ITEM:
                    result += 20;
                    break;
                case UNO_Constants.UNIVERSAL:
                    result += 50;
            }
        }
        return result;
    }

    /** 清空手牌 */
    public void clearCard() {
        cards.clearCard();
    }

    /** 获取用户侧牌组显示的字符串 */
    public String stringOfAllCards() {
        return cards.stringOfAllCards();
    }
}