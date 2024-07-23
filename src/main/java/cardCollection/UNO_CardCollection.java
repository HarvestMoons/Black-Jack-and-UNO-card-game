package cardCollection;

import java.util.*;

import card.*;
import utility.*;

public class UNO_CardCollection extends CardCollection<UNO_Card> {

    public UNO_CardCollection() {
        super();
    }

    /** 添加一整套牌 */
    public void setWholeCards() {
        cards = new ArrayList<>();
        for (int color = UNO_Constants.RED; color < UNO_Constants.GREEN + 1; color++) {
            addCard(color, 0, UNO_Constants.NORMAL);
            for (int point = 1; point < UNO_Constants.SKIP; point++) {
                addCard(color, point, UNO_Constants.NORMAL);
                addCard(color, point, UNO_Constants.NORMAL);
            }
            for (int point = UNO_Constants.SKIP; point < UNO_Constants.PLUS2 + 1; point++) {
                addCard(color, point, UNO_Constants.ITEM);
                addCard(color, point, UNO_Constants.ITEM);
            }
        }
        for (int i = 0; i < 4; i++) {
            addCard(UNO_Constants.BLACK, UNO_Constants.CHANGECOLOR, UNO_Constants.UNIVERSAL);
            addCard(UNO_Constants.BLACK, UNO_Constants.PLUS4, UNO_Constants.UNIVERSAL);
        }
        shuffle();
    }

    @Override
    public void addCard(int color, int point, int type) {
        cards.add(new UNO_Card(color, point, type));
    }

    @Override
    public String stringOfAllCards() {
        StringBuilder result = new StringBuilder();
        for (UNO_Card card : cards) {
            result.append("|  ").append(card.getName()).append("\t");
        }
        result.append("|");
        return result.toString();
    }

    /** 获得牌组牌数 */
    public int getCardsNum() {
        return cards.size();
    }

    /** 清除牌组 */
    public void clearCard() {
        cards.clear();
    }

    /** 移除某张牌 */
    public boolean removeCard(UNO_Card card) {
        boolean result = false;
        for (UNO_Card cardInCollection : cards) {
            if (cardInCollection.getColor() == card.getColor() && cardInCollection.getPoint() == card.getPoint()) {
                result = true;
                cards.remove(cardInCollection);
                break;
            }
        }
        return result;
    }

}
