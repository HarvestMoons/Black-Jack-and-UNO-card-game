//牌局参与者
package attendee;

import java.util.*;

import card.*;
import cardCollection.*;
import utility.*;

public abstract class BJ_Attendee {
	public ArrayList<BJ_CardCollection> cardCollections;
	public int[] totalPoints;
	public int[] winflag;

	public BJ_Attendee() {
		cardCollections = new ArrayList<>();
		cardCollections.add(new BJ_CardCollection());
		winflag = new int[] { BJ_Constants.UNJUDGED, BJ_Constants.UNJUDGED };
		totalPoints = new int[2];
	}

	/** 向外调用总点数 */
	public int[] getTotalPoints() {
		return totalPoints;
	}

	/** 计算总点数 */
	public void calcTotalPoints() {
		// 将总点数清零，重头开始计算
		totalPoints = new int[] { 0, 0 };
		for (int i = 0; i < cardCollections.size(); i++) {
			totalPoints[i] = cardCollections.get(i).calcTotalPoints();
		}
	}

	/** 清空手牌 */
	public void clearCards() {
		cardCollections = new ArrayList<>();
		cardCollections.add(new BJ_CardCollection());
	}

	/** 展示所有牌 */
	public abstract String showCards(boolean isShowWithStars);

	/** 从Dealer手上反复取牌 并判定是否结束 */
	public abstract void getCards(BJ_Card card, int index);

	/** 向外调取牌组中索引为index的牌 */
	public BJ_CardCollection getCardCollection(int index) {
		return cardCollections.get(index);
	}
}
