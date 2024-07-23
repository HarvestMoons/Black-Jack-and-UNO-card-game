package card;

import utility.UNO_Constants;

public class UNO_Card {
    // 点数（UNO:0-9）
    private int point;
    private int color;
    private int type;

    public UNO_Card(int color, int point, int type) {
        this.color = color;
        this.point = point;
        this.type = type;
    }

    public int getPoint() {
        return point;
    }

    public int getColor() {
        return color;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        String colorName;
        String pointName;
        switch (getColor()) {
            case UNO_Constants.RED:
                colorName = "红色的";
                break;
            case UNO_Constants.YELLOW:
                colorName = "黄色的";
                break;
            case UNO_Constants.BLUE:
                colorName = "蓝色的";
                break;
            case UNO_Constants.GREEN:
                colorName = "绿色的";
                break;
            case UNO_Constants.BLACK:
                colorName = "万能牌";
                break;
            default:
                colorName = "";
                break;
        }
        switch (getPoint()) {
            case UNO_Constants.SKIP:
                pointName = "跳过";
                break;
            case UNO_Constants.REVERSE:
                pointName = "反转";
                break;
            case UNO_Constants.PLUS2:
                pointName = "+2";
                break;
            case UNO_Constants.CHANGECOLOR:
                pointName = "调色";
                break;
            case UNO_Constants.PLUS4:
                pointName = "+4";
                break;

            default:
                pointName = Integer.toString(getPoint());
                break;
        }
        return colorName + pointName;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
