package card;

import utility.*;

public class BJ_Card {
    // 点数（BJ:1~13,UNO:0-9）
    private int point;
    // 花色（0~3表示4种花色 0:spade♠ 1:heart♥ 2:diamond♦ 3:club♣）
    private int suit;
    // 该卡牌上的点数名（"A","2","3"..."K"）
    private String pointName;
    // 该卡牌上的花色名
    private String suitName;

    public BJ_Card(Integer point, Integer suit) {
        try {
            if (point < BJ_Constants.A_POINT || point > BJ_Constants.K_POINT) {
                throw new Exception("Invalid card point.");
            }
            if (suit < BJ_Constants.SPADE || suit > BJ_Constants.CLUB) {
                throw new Exception("Invalid card suit.");
            }
            this.point = point;
            this.suit = suit;
            pointName = generatePointName();
            suitName = generateSuitName();
        } catch (Exception e) {
            System.out.println("Error creating card: " + e.getMessage());
        }
    }

    public String generatePointName() {
        switch (point) {
            case BJ_Constants.A_POINT:
                pointName = "A";
                break;
            case BJ_Constants.J_POINT:
                pointName = "J";
                break;
            case BJ_Constants.Q_POINT:
                pointName = "Q";
                break;
            case BJ_Constants.K_POINT:
                pointName = "K";
                break;
            default:
                pointName = Integer.toString(point);
                break;
        }
        return pointName;
    }

    public String generateSuitName() {
        switch (suit) {
            case BJ_Constants.SPADE:
                suitName = "Spade";
                break;
            case BJ_Constants.HEART:
                suitName = "Heart";
                break;
            case BJ_Constants.DIAMOND:
                suitName = "Diamond";
                break;
            case BJ_Constants.CLUB:
                suitName = "Club";
                break;
            default:
                System.err.println("Undefined suit:" + suit);
        }
        return suitName;
    }

    public int getPoint() {
        return point;
    }

    public int getSuit() {
        return suit;
    }

    public String getPointName() {
        return pointName;
    }

    public String getSuitName() {
        return suitName;
    }

    public boolean checkA() {
        return point == 1;
    }
}
