//荷官
package controller;

import java.util.*;

import attendee.*;
import card.*;
import cardCollection.*;
import utility.*;

public class Dealer {
    private static volatile Dealer dealer = null;
    private BJ_CardCollection dealersCardCollection;
    // 0:庄家 1:初始阶段 2:结算 3:玩家
    private int state;
    // 游戏模式
    private int gameMode;
    // 玩家数量
    private int playerAmount;
    // 一共有几套牌
    private int setOfCards;
    private BJ_Player[] player;
    private int playerIndex;
    private BJ_Banker banker;
    private final Scanner scanner;
    private int initBalance;
    private boolean allLose = true;
    protected SystemOperation systemOperation;

    private Dealer(Scanner scanner) {
        this.scanner = scanner;
        gameMode = 0;
        playerAmount = 0;
        initBalance = 0;
        setOfCards = 0;
        systemOperation = new SystemOperation(scanner);
        // 初始化为0，意为“待选择”
    }

    /**
     * 单例模式
     */
    public static Dealer getDealerInstance(Scanner scanner) {
        // 如果实例为空
        if (dealer == null) {
            // 同步块，保证只有一个线程可以进入
            synchronized (Dealer.class) {
                // 双重检查，再次确认实例为空
                if (dealer == null) {
                    // 创建单例实例
                    dealer = new Dealer(scanner);
                }
            }
        }
        // 返回单例实例
        return dealer;
    }

    /**
     * 总流程 包括开始 退出 继续
     */
    public void game() {
        boolean quitGame = false;
        do {
            initialize();
            while (state != BJ_Constants.ENDROUND) {
                dealCards();
            }
            dealCards();
            quitGame = !UserInterface.yesOrNo(systemOperation, BJ_Constants.START_OR_NOT);

        } while (!quitGame);
        System.out.println("Thank you for playing!");
    }

    /**
     * 发牌 根据state决定调用谁的getCards方法 若state是0则一人发两张等等
     */
    private void dealCards() {
        switch (state) {
            case BJ_Constants.BANKERROUND:
                if (allLose) {
                    state = BJ_Constants.ENDROUND;
                    break;
                }
                while (banker.getIsNeedCard()) {
                    banker.getCards(dealersCardCollection.getTopCard(), 0);
                }
                state = BJ_Constants.ENDROUND;
                break;
            case BJ_Constants.STARTROUND:
                // 初始发牌
                initialCardDealing();
                state = BJ_Constants.PLAYERROUND;
                break;
            case BJ_Constants.ENDROUND:
                // 结算 判定玩家的输赢情况并显示结算信息
                end();
                break;
            default:
                System.out.println("\nplayer" + (playerIndex + 1) + ": ");
                playerRound(player[playerIndex]);
                state = (state + 1) % (playerAmount + 3);
                playerIndex++;
        }
    }

    /**
     * 玩家回合 分为简单 赌徒 安全模式
     */
    private void playerRound(BJ_Player player) {
        if (gameMode == BJ_Constants.EASY_MODE) {
            showPlayerCards(player);
            while (player.winflag[0] != BJ_Constants.LOSE && UserInterface.yesOrNo(systemOperation, BJ_Constants.HIT_OR_NOT)) {
                player.getCards(dealersCardCollection.getTopCard(), 0);
                showPlayerCards(player);
            }
            if (player.winflag[0] == BJ_Constants.LOSE) {
                System.out.println("You lose, move to the next player or banker.");
            } else {
                System.out.println("You stood, move to the next player or banker.");
            }
            allLose &= player.winflag[0] == BJ_Constants.LOSE;
        } else if (gameMode == BJ_Constants.GAMBLER_MODE) {
            player.bet();
            showPlayerCards(player);
            player.doubleWagerChoice();
            if (player.getGamblingState() == BJ_Constants.DOUBLE) {
                player.getCards(dealersCardCollection.getTopCard(), 0);
                System.out.println("player" + (playerIndex + 1) + ": ");
                showPlayerCards(player);
                if (player.winflag[0] != BJ_Constants.LOSE) {
                    System.out.println(
                            "You have doubled your wager and got your card, move to the next player or banker.");
                } else {
                    System.out.println("You lose, move to the next player or banker.");
                }
                return;
            }
            if (player.getIsAbleToSplit()) {
                player.splitChoice();
            }
            if (player.getWhetherSplit()) {
                player.cardCollections.add(new BJ_CardCollection());
                player.cardCollections.get(1).addCard(player.cardCollections.get(0).getTopCard());
                player.cardCollections.get(0).addCard(dealersCardCollection.getTopCard());
                player.cardCollections.get(1).addCard(dealersCardCollection.getTopCard());
                showPlayerCards(player);
            }
            int cardCollectionAmount = player.getCardCollectionAmount();
            for (int j = 0; j < cardCollectionAmount; j++) {
                UserInterface.printSplitMessage(cardCollectionAmount, j);
                while (player.winflag[j] != BJ_Constants.LOSE
                        && UserInterface.yesOrNo(systemOperation, BJ_Constants.HIT_OR_NOT)) {
                    player.getCards(dealersCardCollection.getTopCard(), j);
                    showPlayerCards(player);
                }
                if (player.winflag[j] == BJ_Constants.LOSE) {
                    System.out.println("You lose, move to the next "
                            + (j == cardCollectionAmount - 1 ? "player or banker." : "card collection."));
                } else {
                    System.out.println("You stood, move to the next "
                            + (j == cardCollectionAmount - 1 ? "player or banker." : "card collection."));
                }
                allLose &= player.winflag[j] == BJ_Constants.LOSE;
            }
        } else if (gameMode == BJ_Constants.SAFE_MODE) {
            player.bet();
            showPlayerCards(player);
            player.surrenderChoice();
            if (player.getGamblingState() == BJ_Constants.SURRENDER) {
                System.out.println("You have surrendered. Your round is over.");
                allLose &= player.winflag[0] == BJ_Constants.LOSE;
                return;
            }
            player.doubleWagerChoice();
            if (player.getGamblingState() == BJ_Constants.DOUBLE) {
                player.getCards(dealersCardCollection.getTopCard(), 0);
                System.out.println("player" + (playerIndex + 1) + ": ");
                showPlayerCards(player);
                if (player.winflag[0] != BJ_Constants.LOSE) {
                    System.out.println(
                            "You have doubled your wager and got your card, move to the next player or banker.");
                } else {
                    System.out.println("You lose, move to the next player or banker.");
                }
                return;
            }
            if (player.getIsAbleToSplit()) {
                player.splitChoice();
            }
            if (player.getWhetherSplit()) {
                player.cardCollections.add(new BJ_CardCollection());
                player.cardCollections.get(1).addCard(player.cardCollections.get(0).getTopCard());
                player.cardCollections.get(0).addCard(dealersCardCollection.getTopCard());
                player.cardCollections.get(1).addCard(dealersCardCollection.getTopCard());
                showPlayerCards(player);
            }
            // 检测banker是否明牌为A，保险的前提
            if (banker.getIsHavingInitA()) {
                player.insuranceChoice();
            }
            int cardCollectionAmount = player.getCardCollectionAmount();
            for (int j = 0; j < cardCollectionAmount; j++) {
                UserInterface.printSplitMessage(cardCollectionAmount, j);
                while (player.winflag[j] != BJ_Constants.LOSE
                        && UserInterface.yesOrNo(systemOperation, BJ_Constants.HIT_OR_NOT)) {
                    player.getCards(dealersCardCollection.getTopCard(), j);
                    showPlayerCards(player);
                }
                if (player.winflag[j] == BJ_Constants.LOSE) {
                    System.out.println("You lose, move to the next "
                            + (j == cardCollectionAmount - 1 ? "player or banker." : "card collection."));
                } else {
                    System.out.println("You stood, move to the next "
                            + (j == cardCollectionAmount - 1 ? "player or banker." : "card collection."));
                }
                allLose &= player.winflag[j] == BJ_Constants.LOSE;
            }
        }
    }

    /**
     * 结算 判定玩家的输赢情况并显示结算信息
     */
    private void end() {
        System.out.println("The game is over.");
        for (int i = 0; i < playerAmount; i++) {
            for (int j = 0; j < player[i].getCardCollectionAmount(); j++) {
                player[i].calcTotalPoints();
                if (player[i].getTotalPoints()[j] > BJ_Constants.MAXPOINT) {
                    player[i].winflag[j] = BJ_Constants.LOSE;
                }
                if (banker.winflag[0] == BJ_Constants.LOSE && player[i].winflag[j] != BJ_Constants.LOSE) {
                    player[i].winflag[j] = BJ_Constants.WIN;
                } else if (player[i].winflag[j] != BJ_Constants.LOSE) {
                    if (player[i].getCardCollection(j).isBlackJack()
                            && !banker.getCardCollection(0).isBlackJack()) {
                        player[i].winflag[j] = BJ_Constants.WIN;
                    } else if (!player[i].getCardCollection(j).isBlackJack()
                            && banker.getCardCollection(0).isBlackJack()) {
                        player[i].winflag[j] = BJ_Constants.LOSE;
                    } else if (player[i].getTotalPoints()[j] > banker.getTotalPoints()[0]) {
                        player[i].winflag[j] = BJ_Constants.WIN;
                    } else if (player[i].getTotalPoints()[j] == banker.getTotalPoints()[0]) {
                        player[i].winflag[j] = BJ_Constants.DRAW;
                    } else if (player[i].getTotalPoints()[j] < banker.getTotalPoints()[0]) {
                        player[i].winflag[j] = BJ_Constants.LOSE;
                    }
                }
            }
            player[i].renewMoney(banker.getCardCollection(0).isBlackJack());
        }
        showAllCardPoints();
        if (gameMode != BJ_Constants.EASY_MODE) {
            showAllMoneyChange();
        }
    }

    /**
     * 初始发牌
     */
    private void initialCardDealing() {
        dealersCardCollection.shuffle();
        // 给庄家两张初始牌
        BJ_Card banksFirstCard = dealersCardCollection.getTopCard();
        BJ_Card banksSecondCard = dealersCardCollection.getTopCard();
        banker.getCards(banksFirstCard, 0);
        banker.getCards(banksSecondCard, 0);
        // 初始发牌，（明牌的那张）是否为A？
        boolean isHavingInitA = banksFirstCard.getPoint() == BJ_Constants.A_POINT;
        banker.setIsHavingInitA(isHavingInitA);
        // 展示庄家手牌（带星号）
        System.out.println("The Banker's cards are: " + banker.showCards(true));

        // 给每位玩家初始牌
        for (int j = 0; j < playerAmount; j++) {
            BJ_Card playersFirstCard = dealersCardCollection.getTopCard();
            BJ_Card playersSecondCard = dealersCardCollection.getTopCard();
            player[j].getCards(playersFirstCard, 0);
            player[j].getCards(playersSecondCard, 0);
            // 初始发牌，是否两牌相同？
            boolean isCardTheSame = playersFirstCard.getPoint() == playersSecondCard.getPoint()
                    && playersFirstCard.getSuit() == playersSecondCard.getSuit();

            // boolean isCardTheSame = playersFirstCard.getSuit() ==
            // playersSecondCard.getSuit();

            // 本句为测试用句
            player[j].setIsAbleToSplit(isCardTheSame);
        }

    }

    /**
     * 展示目前玩家的手牌（不带星号的展示）
     */
    public void showPlayerCards(BJ_Player player) {
        System.out.println("Your cards now are: " + player.showCards(false));
    }

    /**
     * 展示所有人手牌结算数值
     */
    public void showAllCardPoints() {
        System.out
                .println("The Banker's cards are: " + banker.cardCollections.get(0).stringOfAllCards()
                        + ", total score is "
                        + banker.getTotalPoints()[0]);
        for (int i = 0; i < playerAmount; i++) {
            int cardCollectionAmount = player[i].getCardCollectionAmount();
            for (int j = 0; j < cardCollectionAmount; j++) {
                if (player[i].getGamblingState() == BJ_Constants.SURRENDER) {
                    System.out.println("Player " + (i + 1) + " surrendered.");
                } else {
                    if (player[i].winflag[j] == BJ_Constants.LOSE) {
                        System.out.println("Player " + (i + 1) +
                                (cardCollectionAmount == 1 ? "" : ("'s cards " + (j + 1))) + " lose.");
                    } else if (banker.winflag[j] == BJ_Constants.LOSE || player[i].winflag[j] == BJ_Constants.WIN) {
                        System.out.println("Player " + (i + 1) +
                                (cardCollectionAmount == 1 ? "" : ("'s cards " + (j + 1))) + " win.");
                    } else if (player[i].winflag[j] == BJ_Constants.DRAW) {
                        System.out.println("The game result for Player" + (i + 1) +
                                (cardCollectionAmount == 1 ? "" : ("'s cards " + (j + 1))) + " is a draw.");
                    }
                    System.out.println(
                            "Player " + (i + 1) + "'s cards" + (cardCollectionAmount == 1 ? ""
                                    : (" " + (j + 1))) + " are: " + player[i].cardCollections.get(j).stringOfAllCards()
                                    + ", total score is " + player[i].getTotalPoints()[j]);
                }

            }
        }
    }

    /**
     * 重置
     */
    public void initialize() {
        // 若还未选择模式，提供模式选择
        if (gameMode == 0) {
            gameMode = UserInterface.getInt(scanner, 3, "Choose game mode: 1-easy mode, 2-gambler mode, 3-safe mode: ");
        }
        if (gameMode != 1) {
            initBalance = BJ_Constants.INIT_BALANCE;
        }
        // 若还未选择玩家人数，提供人数选择
        if (playerAmount == 0) {
            playerAmount = UserInterface.getPlayerAmount(systemOperation, BJ_Constants.MAX_PLAYER_AMOUNT,
                    BJ_Constants.MIN_PLAYER_AMOUNT);
            player = new BJ_Player[playerAmount];
            for (int i = 0; i < playerAmount; i++) {
                player[i] = new BJ_Player(scanner, initBalance);
            }
        } else {
            for (int i = 0; i < playerAmount; i++) {
                player[i].initExceptBalance();
            }
        }
        calcSetOfCards();// 根据玩家数量，计算牌的套数
        dealersCardCollection = new BJ_CardCollection();
        for (int i = 0; i < setOfCards; i++) {
            dealersCardCollection.getWholeCards();
        }
        banker = new BJ_Banker();
        playerIndex = 0;
        state = BJ_Constants.STARTROUND;
    }

    /**
     * 根据人数计算牌堆总副数
     */
    private void calcSetOfCards() {
        if (playerAmount > 1 && playerAmount <= 5) {
            setOfCards = 2;
        } else {
            setOfCards = 4;
        }
    }

    /**
     * 展示所有玩家余额
     */
    private void showAllMoneyChange() {
        for (int i = 0; i < playerAmount; i++) {
            System.out.println("Player " + (i + 1) + " has " + player[i].getBalance() + " bucks now.");
        }
    }
}