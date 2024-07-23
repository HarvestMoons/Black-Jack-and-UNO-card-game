package utility;

public class UNO_Constants {
    // 当前处于UNO的游戏流程
    public static final int UNO_MODE = 2;
    // 依次为 普通卡、功能卡、道具卡
    public static final int NORMAL = 0;
    public static final int ITEM = 1;
    public static final int UNIVERSAL = 2;
    // 初始手牌数
    public static final int INIT_CARD_NUM = 7;
    // 花色
    public static final int RED = 0;
    public static final int YELLOW = 1;
    public static final int BLUE = 2;
    public static final int GREEN = 3;
    public static final int BLACK = 4;
    // 功能牌数值
    public static final int SKIP = 10;
    public static final int REVERSE = 11;
    public static final int PLUS2 = 12;
    // 万能牌数值
    public static final int CHANGECOLOR = 13;
    public static final int PLUS4 = 14;
    // 需要玩家选择时的打印信息
    public static final String START_OR_NOT = "Start the game?(Y/N)";
    // 最大、最小玩家数量
    public static final int MAX_PLAYER_AMOUNT = 4;
    public static final int MIN_PLAYER_AMOUNT = 2;
    // 可输入卡牌名
    public static final Character[] NORMAL_COLORS = { 'r', 'y', 'b', 'g' };
    public static final String[] NORMAL_COLORS_NAME = { "红色", "黄色", "蓝色", "绿色" };
    public static final Character[] UNIVERSAL_COLORS = { 'k' };
    public static final Character[] NORMAL_NAMES = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    public static final Character[] ITEM_NAMES = { 's', 'r', 'p' };
    public static final Character[] UNIVERSAL_NAMES = { 'c', 'p' };
    // 出牌顺序
    public static final int CLOCKWISE = 1;
    public static final int COUNTERCLOCKWISE = -1;
}
