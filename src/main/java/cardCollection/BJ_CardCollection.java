package cardCollection;

import card.*;
import utility.*;

//一套牌
public class BJ_CardCollection extends CardCollection<BJ_Card> {
    // 总牌数
    private int totalCards;
    // 总点数
    private int totalPoints;

    public BJ_CardCollection() {
        super();
    }

    // 添加一整套牌
    public void getWholeCards() {
        for (int suit = BJ_Constants.SPADE; suit < BJ_Constants.CLUB + 1; suit++) {
            // 所有花色
            for (int point = BJ_Constants.A_POINT; point < BJ_Constants.K_POINT + 1; point++) {
                // 所有点数
                addCard(point, suit, BJ_Constants.NORMALTYPE);
            }
        }
    }

    @Override
    // 向牌组中增加一张扑克牌(已知点数与花色)
    public void addCard(int point, int suit, int type) {
        cards.add(new BJ_Card(point, suit));
        totalCards++;
    }

    @Override
    // 打印所有卡牌
    public String stringOfAllCards() {
        String result = "| ";
        for (BJ_Card card : cards) {
            try {
                if (cards.isEmpty()) {
                    throw new IllegalArgumentException("The cardCollection is empty.");
                } else {
                    result += card.getPointName() + " of " + card.getSuitName() + "\t| ";
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    // 打印带星卡牌
    public String stringOfAllCardsWithStar() {
        StringBuilder result = null;
        try {
            if (cards.isEmpty()) {
                throw new IllegalArgumentException("The cardCollection is empty.");
            } else {
                result = new StringBuilder("| ");
                result.append(cards.get(0).getPointName()).append(" of ").append(cards.get(0).getSuitName())
                        .append("\t|");
                for (int i = 1; i < cards.size(); i++) {
                    result.append("\t*\t|");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        assert result != null;
        return result.toString();
    }

    // 判断黑杰克
    public boolean isBlackJack() {
        calcTotalPoints();
        return totalCards == 2 && totalPoints == 21;
    }

    public int calcTotalPoints() {
        totalPoints = 0;
        // 点数作为11计算的A牌的数量
        int A_ElevenNums = 0;
        // A的点数默认为11点加入totalPoints，如最终totalPoints大于21，再作为1计算
        // JQK的点数记为10加入totalPoints
        for (BJ_Card card : cards) {

            switch (card.getPointName()) {
                case "A":
                    totalPoints += 11;
                    A_ElevenNums++;
                    break;
                case "J":
                case "K":
                case "Q":
                    totalPoints += 10;
                    break;
                default:
                    totalPoints += card.getPoint();
                    break;
            }
        }
        while (totalPoints > 21 && A_ElevenNums > 0) {
            A_ElevenNums--;
            totalPoints -= 10;
        }
        return totalPoints;
    }

}
