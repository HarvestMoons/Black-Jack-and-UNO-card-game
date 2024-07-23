package attendee;

import java.util.*;

import card.*;
import utility.*;

public class BJ_Player extends BJ_Attendee {
	private final Wallet wallet;
	// 赌注,初始化为0
	private int wager;
	private boolean isAbleToSplit;
	private final Scanner scanner;
	private boolean isInsurance = false;
	private int insurancePayment;
	private boolean whetherSplit = false;
	private int gamblingState;
	protected SystemOperation systemOperation;

	private static class Wallet {
		private int balance;

		public Wallet(int balance) {
			this.balance = balance;
		}

		// 钱包加钱
		public void addMoney(int amount) {
			balance += amount;
		}

		// 钱包取钱
		public void deductMoney(int amount) {
			balance -= amount;
		}

	}

	public BJ_Player(Scanner scanner, int balance) {
		super();
		// 初始化钱包
		wallet = new Wallet(balance);
		wager = 0;
		isAbleToSplit = false;
		this.scanner = scanner;
		gamblingState = BJ_Constants.NORMAL;
		systemOperation = new SystemOperation(scanner);
	}

	/** 展示所有牌 不论该玩家有多少堆 */
	@Override
	public String showCards(boolean isShowWithStars) {
		StringBuilder result = new StringBuilder();
		for (cardCollection.BJ_CardCollection cardCollection : cardCollections) {
			result.append("\n").append(cardCollection.stringOfAllCards());
		}
		return result.toString();
	}

	/** 下注操作 */
	public void bet() {
		System.out.println(
				"You now have " + wallet.balance + " bucks.Please make a bet(It has to be a multiple of 10): ");
		String userInput = systemOperation.readOperation();
		while (!userInput.matches("\\d+") || Integer.parseInt(userInput) % 10 != 0
				|| Integer.parseInt(userInput) > wallet.balance || Integer.parseInt(userInput) < 10) {
			System.out.println("Invalid wager!Please make your bet again: ");
			userInput = systemOperation.readOperation();
		}
		wager = Integer.parseInt(userInput);
	}

	/** 赌注翻倍 */
	public void doubleWagerChoice() {
		// 玩家若同意翻倍赌注，且有足够的余额，进行赌注翻倍操作
		if (UserInterface.yesOrNo(systemOperation, BJ_Constants.DOUBLE_OR_NOT)) {
			if (wager * 2 > wallet.balance) {
				System.out.println("Oops! You don't have enough money! You now have " + wallet.balance + " bucks.");
			} else {
				gamblingState = BJ_Constants.DOUBLE;
				wager *= 2;
				System.out.println("You have doubled your wager. You wager now is " + wager + " bucks.");
			}
		}
	}

	/** 赌注清零 */
	public void clearWager() {
		wager = 0;
	}

	/** 获取玩家余额 */
	public int getBalance() {
		return wallet.balance;
	}

	/** 获取赌注金额 */
	public int getWager() {
		return wager;
	}

	/** 获取保险金额 */
	public int getInsurancePayment() {
		return insurancePayment;
	}

	/** 要牌操作 */
	@Override
	public void getCards(BJ_Card card, int index) {
		cardCollections.get(index).addCard(card);
		calcTotalPoints();
		if (totalPoints[index] > BJ_Constants.MAXPOINT) {
			winflag[index] = BJ_Constants.LOSE;
		}
	}

	/** 询问是否投降 */
	public void surrenderChoice() {
		if (UserInterface.yesOrNo(systemOperation, BJ_Constants.SURRENDER_OR_NOT)) {
			gamblingState = BJ_Constants.SURRENDER;
			winflag[0] = BJ_Constants.LOSE;
			wager /= 2;
		}
	}

	/** 检查分牌条件 */
	public boolean getIsAbleToSplit() {
		return isAbleToSplit;
	}

	/** 询问是否分牌 */
	public void splitChoice() {
		if (isAbleToSplit) {
			whetherSplit = UserInterface.yesOrNo(systemOperation, BJ_Constants.SPLIT_OR_NOT);
		}
	}

	/** 设置分牌状态 */
	public void setIsAbleToSplit(boolean isAbleToSplit) {
		this.isAbleToSplit = isAbleToSplit;
	}

	/** 获取分牌状态 */
	public boolean getWhetherSplit() {
		return whetherSplit;
	}

	/** 询问是否保险 */
	public void insuranceChoice() {
		if (UserInterface.yesOrNo(systemOperation, BJ_Constants.INSURANCE_OR_NOT)) {
			isInsurance = true;
			insurancePayment = wager / 2;
			wallet.deductMoney(insurancePayment);
			System.out.println("You have paid " + insurancePayment + " bucks for insurance.");
		}
	}

	/** 更新余额 */
	public void renewMoney(boolean bankerIsBlackJack) {
		for (int i = 0; i < cardCollections.size(); i++) {
			if (winflag[i] == BJ_Constants.LOSE) {
				wallet.deductMoney(wager);
			} else if (winflag[i] == BJ_Constants.WIN) {
				if (cardCollections.get(i).isBlackJack()) {
					wallet.addMoney((int) (wager * 1.5));
				} else {
					wallet.addMoney(wager);
				}
			}
		}
		if (isInsurance && bankerIsBlackJack) {
			wallet.addMoney(2 * insurancePayment);
		}
	}

	/** 获取牌组数 */
	public int getCardCollectionAmount() {
		return cardCollections.size();
	}

	/** 获取玩家注状态 普通 翻倍 投降 */
	public int getGamblingState() {
		return gamblingState;
	}

	/** 玩家状态初始化 不改变钱包 */
	public void initExceptBalance() {
		clearCards();
		winflag = new int[] { BJ_Constants.UNJUDGED, BJ_Constants.UNJUDGED };
		totalPoints = new int[2];
		gamblingState = BJ_Constants.NORMAL;
		whetherSplit = false;
		isInsurance = false;
	}

}
