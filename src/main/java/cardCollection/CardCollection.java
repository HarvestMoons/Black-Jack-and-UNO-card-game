package cardCollection;

import java.util.*;

public abstract class CardCollection<T> {
    protected ArrayList<T> cards;
    // 总牌数
    protected int totalCards;

    public CardCollection() {
        cards = new ArrayList<>();
    }

    // 向牌组中增加一张牌
    public void addCard(T card) {
        cards.add(card);
        totalCards++;
    };

    // 向牌组中增加一张牌(已知卡的属性)
    public abstract void addCard(int point, int suit, int type);

    // 显示所有卡牌信息
    public abstract String stringOfAllCards();

    // 取顶牌
    public T getTopCard() {
        totalCards--;
        return cards.remove(0);
    }

    // 洗牌
    public void shuffle() {
        if (cards.isEmpty()) {
            System.out.println("The cardCollection is empty.");
        } else {
            Collections.shuffle(cards);
        }
    }

    public int getTotalCards() {
        return totalCards;
    }

    public ArrayList<T> getCards() {
        return cards;
    }
}
