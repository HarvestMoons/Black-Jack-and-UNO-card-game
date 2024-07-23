package attendee;

import card.*;
import utility.*;

public class BJ_Banker extends BJ_Attendee {
	private boolean isNeedCard = true;
	private boolean isHavingInitA;

	/** 要牌操作 */
	@Override
	public void getCards(BJ_Card card, int index) {
		cardCollections.get(0).addCard(card);
		calcTotalPoints();

		if (totalPoints[0] >= BJ_Constants.BANKER_CHECKPOINT) {
			isNeedCard = false;
		}
		if (totalPoints[0] > BJ_Constants.MAXPOINT) {
			winflag[0] = BJ_Constants.LOSE;
		}
	}

	/** 获取是否需要继续要牌 */
	public boolean getIsNeedCard() {
		return isNeedCard;
	}

	/** 获取第一张牌是否为A */
	public boolean getIsHavingInitA() {
		return isHavingInitA;
	}

	/** 设置第一张牌是否为A */
	public void setIsHavingInitA(boolean isHavingInitA) {
		this.isHavingInitA = isHavingInitA;
	}

	/** 展示所有牌 */
	@Override
	public String showCards(boolean isShowWithStars) {
		if (isShowWithStars) {
			return cardCollections.get(0).stringOfAllCardsWithStar();
		} else {
			return cardCollections.get(0).stringOfAllCards();
		}
	}
}
