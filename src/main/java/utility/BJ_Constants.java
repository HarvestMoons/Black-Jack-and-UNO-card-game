//常量集合
package utility;

public class BJ_Constants {
	// 当前处于BJ的游戏流程
	public static final int BJ_MODE = 1;
	// 用于Attendee的winflag判断（UNJUDGE表示胜负未定的状态）
	public static final int UNJUDGED = 0;
	public static final int WIN = 1;
	public static final int LOSE = 2;
	public static final int DRAW = 3;
	// 庄家手牌点数和大于等于17点，不可继续要牌
	public static final int BANKER_CHECKPOINT = 17;
	// 任一参与者手牌点数和大于21点，判负
	public static final int MAXPOINT = 21;
	// 0~3表示4种花色 0:spade♠ 1:heart♥ 2:diamond♦ 3:club♣
	public static final int SPADE = 0;
	public static final int HEART = 1;
	public static final int DIAMOND = 2;
	public static final int CLUB = 3;
	// A,J,Q,K的数值
	public static final int A_POINT = 1;
	public static final int J_POINT = 11;
	public static final int Q_POINT = 12;
	public static final int K_POINT = 13;
	// card type
	public static final int NORMALTYPE = 0;
	// 当前游戏进程
	public static final int BANKERROUND = 0;
	public static final int STARTROUND = 1;
	public static final int ENDROUND = 2;
	public static final int PLAYERROUND = 3;
	// 最多玩家人数
	public static final int MAX_PLAYER_AMOUNT = 10;
	// 最少玩家人数
	public static final int MIN_PLAYER_AMOUNT = 1;
	// 最多有几套牌
	public static final int MAX_SET_OF_CARDS = 10;
	// 需要玩家选择时的打印信息
	public static final String START_OR_NOT = "Start the game?(Y/N)";
	public static final String CONTINUE_OR_NOT = "Continue the game?(Y/N)";
	public static final String HIT_OR_NOT = "Wanna hit(Get one more card)?(Y/N):";
	public static final String DOUBLE_OR_NOT = "Wanna double your bet? (You only get one change, "
			+ "you will and only will get one more card if you double)(Y/N):";
	public static final String SURRENDER_OR_NOT = "Wanna surrender? (You will pay half of you wager and end this round)(Y/N)";
	public static final String INSURANCE_OR_NOT = "The banker got an A, wanna pay for insurance? "
			+ "(You will pay half of you wager and get insurance, if the banker got blackjack then you gain double of the insurance)(Y/N)";
	public static final String SPLIT_OR_NOT = "Wanna split your cards?(You will get another two cards and bet the same wager)(Y/N)";
	// 初始牌数
	public static final int ORIGIN_CARD_AMOUNT = 2;

	// 三种游戏模式
	public static final int EASY_MODE = 1;
	public static final int GAMBLER_MODE = 2;
	public static final int SAFE_MODE = 3;

	// 玩家状态（正常、投降、翻倍）（投降、翻倍为金钱模式的特有状态）
	public static final int NORMAL = 1;
	public static final int SURRENDER = 2;
	public static final int DOUBLE = 3;

	// 初始资金（cash mode特有）
	public static final int INIT_BALANCE = 500;
}
