package controller;

import java.util.*;

import card.*;
import attendee.*;
import cardCollection.*;
import utility.*;

public class UNO_Controller {
    private static volatile UNO_Controller unoController = null;
    protected UNO_CardCollection cards;
    private final Scanner scanner;
    protected int playerAmount;
    protected UNO_Player[] players;
    protected int playerIndex;
    private UNO_Card lastCard;
    private int[] playersForStarterConfirm;
    protected int winIndex = -1;
    protected int winAmount = 0;
    private int order;
    protected SystemOperation systemOperation = null;

    public UNO_Controller(Scanner scanner) {
        this.scanner = scanner;
        playerAmount = 0;
        cards = new UNO_CardCollection();
        systemOperation = new SystemOperation(scanner);
    }

    /** 单例模式 */
    public static UNO_Controller getDealerInstance(Scanner scanner) {
        if (unoController == null) {
            synchronized (UNO_Controller.class) {
                if (unoController == null) {
                    unoController = new UNO_Controller(scanner);
                }
            }
        }
        return unoController;
    }

    /** 整体UNO游戏流程 */
    public void game() {
        boolean isQuitGame = false;
        do {
            initialize();
            dealCards();
            isQuitGame = !UserInterface.yesOrNo(systemOperation, UNO_Constants.START_OR_NOT);
        } while (!isQuitGame);
        System.out.println("Thank you for playing!");
    }

    /** 单局UNO游戏流程 */
    private void dealCards() {
        starterConfirm();
        initialCardDealing();
        getFirstCard();
        showInstructions();
        playerRound();
        end();
    }

    /** 重置 */
    public void initialize() {
        lastCard = null;
        playerIndex = 0;
        cards = new UNO_CardCollection();
        cards.setWholeCards();
        order = UNO_Constants.CLOCKWISE;
        // 若还未选择玩家人数，提供人数选择
        if (playerAmount == 0) {
            playerAmount = UserInterface.getPlayerAmount(systemOperation, UNO_Constants.MAX_PLAYER_AMOUNT,
                    UNO_Constants.MIN_PLAYER_AMOUNT);
            players = new UNO_Player[playerAmount];
            for (int i = 0; i < playerAmount; i++) {
                players[i] = new UNO_Player();
            }
        }
        if (playersForStarterConfirm == null) {
            playersForStarterConfirm = new int[playerAmount];
            Arrays.fill(playersForStarterConfirm, 0);
        }
    }

    /** 首发确认，如果有前一局，根据前一局第一名首发，否则抽牌确认，抽完牌需重置牌堆 */
    protected void starterConfirm() {
        int firstPlayer = 0;
        boolean isDone = false;
        if (winAmount == 1) {
            isDone = true;
            firstPlayer = winIndex;
            System.out.println("\n上一局有唯一赢家");
        } else {
            System.out.println("\n首发确认开始");
        }
        while (!isDone) {
            isDone = true;
            cards.setWholeCards();
            for (int i = 0; i < playerAmount; i++) {
                if (playersForStarterConfirm[i] == -1) {
                    continue;
                }
                UNO_Card card = cards.getTopCard();
                if (card.getType() != UNO_Constants.NORMAL) {
                    isDone = false;
                    break;
                }
                if (card.getPoint() > playersForStarterConfirm[firstPlayer]) {
                    isDone = true;
                    firstPlayer = i;
                } else if (card.getPoint() == playersForStarterConfirm[firstPlayer]) {
                    isDone = false;
                }
                playersForStarterConfirm[i] = card.getPoint();
            }
        }
        for (int i = 0; i < playerAmount && winAmount != 1; i++) {
            System.out.print("玩家" + (i + 1));
            if (playersForStarterConfirm[i] == -1) {
                System.out.println("不参与首发确认");
            } else {
                System.out.println("抽到了数字" + playersForStarterConfirm[i]);
            }
        }
        System.out.println("第一个出牌的为玩家" + (firstPlayer + 1));
        cards.setWholeCards();
        winIndex = -1;
        playerIndex = firstPlayer;
    }

    /** 发牌，每人拿到初始手牌 (一次性发7张) */
    protected void initialCardDealing() {
        for (UNO_Player player : players) {
            player.clearCard();
            for (int i = 0; i < UNO_Constants.INIT_CARD_NUM; i++) {
                player.addCard(cards.getTopCard());
            }
        }

    }

    /** 开牌，即将抽牌堆顶部牌拿出，赋给lastCard */
    protected void getFirstCard() {
        do {
            cards.setWholeCards();
            lastCard = cards.getTopCard();
        } while (lastCard.getType() != UNO_Constants.NORMAL);
        System.out.println("\n开牌：\n第一张牌为" + lastCard.getName());
    }

    /** 告知全体玩家可输入的指令格式 */
    private void showInstructions() {
        System.out.println();
        System.out.println("您可输入的指令格式如下：");
        System.out.println("-h\t显示本列表");
        System.out.println("-p card\t出牌\n\t其中card可输入的格式有：\n\t\tcolor+number\n\t\tcolor+function");
        System.out.println("\t\tcolor: r - 红色, y - 黄色, b - 蓝色, g - 绿色, k - 万能牌");
        // 此处暂定的出牌方式可以根据CYX实际实现的效果进行更改
        System.out.println("\t\tfunction: s - 跳过, r - 反转, c - 换色, p - +2/+4(视卡牌颜色而定)");
        System.out.println("\t\t如r2代表红色的2, kp代表万能牌的+4");
        System.out.println("-d\t摸牌");
        System.out.println("-s\t喊UNO");
        System.out.println("-q\t质疑");
        System.out.println("-a\t展示手牌");
        System.out.println("-o\t直接进行结算结束比赛");
    }

    /** 在此阶段内玩家可以输入指令进行各种操作 */
    protected void playerRound() {
        boolean isGameOver = false;
        while (!isGameOver) {
            System.out.println();
            System.out.println("玩家" + (playerIndex + 1) + "回合");
            System.out.println("牌堆顶部的牌为" + lastCard.getName());
            System.out.println("玩家" + (playerIndex + 1) + "现有手牌：");
            System.out.println(players[playerIndex].stringOfAllCards());
            System.out.println("请输入指令：");
            // 指令拆分
            ArrayList<ArrayList<String>> instructions = dealInstruction();
            ArrayList<String> playOrDrawInstruction = instructions.get(0);
            ArrayList<String> elseInstructions = instructions.get(1);

            // 指令执行
            isGameOver = runInstruction(playOrDrawInstruction, elseInstructions);

        }

    }

    /** 展示手牌 */
    private void showCards(UNO_Player player) {
        System.out.println("您的现有手牌：");
        System.out.println(player.stringOfAllCards());
    }

    /** 指令拆分，判定格式正误（此处不执行） */
    private ArrayList<ArrayList<String>> dealInstruction() {
        boolean isInstructionLegal = false;
        ArrayList<String> playOrDrawInstruction = new ArrayList<>();
        ArrayList<String> elseInstructions = new ArrayList<>();
        while (!isInstructionLegal) {
            String instruction = systemOperation.readOperation();
            isInstructionLegal = instruction.startsWith("-");
            String[] splitInstruction = instruction.split(" ");
            playOrDrawInstruction = new ArrayList<>();
            elseInstructions = new ArrayList<>();
            ArrayList<String> tempInstruction = new ArrayList<>();
            for (int i = 0; i <= splitInstruction.length && isInstructionLegal; i++) {
                if (i == splitInstruction.length
                        || (!tempInstruction.isEmpty() && splitInstruction[i].startsWith("-"))) {
                    switch (tempInstruction.get(0)) {
                        case "-p":
                            if (tempInstruction.size() != 2 || !playOrDrawInstruction.isEmpty()) {
                                isInstructionLegal = false;
                                break;
                            }
                            UNO_Card cardToPlay = getCard(tempInstruction.get(1));
                            if (cardToPlay == null) {
                                System.out.print("这不是一张卡牌，");
                                isInstructionLegal = false;
                                break;
                            }
                            playOrDrawInstruction = tempInstruction;
                            break;
                        case "-d":
                            if (tempInstruction.size() != 1 || !playOrDrawInstruction.isEmpty()) {
                                isInstructionLegal = false;
                                break;
                            }
                            playOrDrawInstruction = tempInstruction;
                            break;
                        case "-h":
                        case "-u":
                        case "-q":
                        case "-a":
                        case "-o":
                            if (tempInstruction.size() != 1) {
                                isInstructionLegal = false;
                                break;
                            }
                            elseInstructions.add(tempInstruction.get(0));
                            break;
                        default:
                            isInstructionLegal = false;
                            break;
                    }
                    tempInstruction = new ArrayList<>();
                }
                if (i < splitInstruction.length) {
                    tempInstruction.add(splitInstruction[i]);
                }
            }

            if (!isInstructionLegal) {
                System.out.println("错误的指令格式，请重新输入，输入-h获取所有合法指令");
            }
        }

        ArrayList<ArrayList<String>> result = new ArrayList<>();
        result.add(playOrDrawInstruction);
        result.add(elseInstructions);
        return result;
    }

    /** 将字符串转为UNO_Card */
    private UNO_Card getCard(String cardString) {
        if (cardString.length() != 2) {
            return null;
        }
        int color, point, type;
        if (Arrays.asList(UNO_Constants.NORMAL_COLORS).contains(cardString.charAt(0))) {
            color = Arrays.asList(UNO_Constants.NORMAL_COLORS).indexOf(cardString.charAt(0));
            if (Arrays.asList(UNO_Constants.NORMAL_NAMES).contains(cardString.charAt(1))) {
                point = Arrays.asList(UNO_Constants.NORMAL_NAMES).indexOf(cardString.charAt(1));
                type = UNO_Constants.NORMAL;
            } else if (Arrays.asList(UNO_Constants.ITEM_NAMES).contains(cardString.charAt(1))) {
                point = Arrays.asList(UNO_Constants.ITEM_NAMES).indexOf(cardString.charAt(1)) + 10;
                type = UNO_Constants.ITEM;
            } else {
                return null;
            }
        } else if (Arrays.asList(UNO_Constants.UNIVERSAL_COLORS).contains(cardString.charAt(0))) {
            color = Arrays.asList(UNO_Constants.UNIVERSAL_COLORS).indexOf(cardString.charAt(0)) + 4;
            type = UNO_Constants.UNIVERSAL;
            if (Arrays.asList(UNO_Constants.UNIVERSAL_NAMES).contains(cardString.charAt(1))) {
                point = Arrays.asList(UNO_Constants.UNIVERSAL_NAMES).indexOf(cardString.charAt(1)) + 13;
            } else {
                return null;
            }
        } else {
            return null;
        }
        return new UNO_Card(color, point, type);
    }

    /** 指令执行，返回是否结束游戏 */
    private boolean runInstruction(ArrayList<String> playOrDrawInstruction, ArrayList<String> elseInstructions) {
        boolean isGameOver = false;
        try {
            for (String elseInstruction : elseInstructions) {
                switch (elseInstruction) {
                    case "-u":
                        shout(players[playerIndex]);
                        break;
                    case "-q":
                        question(players[playerIndex], playerIndex);
                        break;
                    case "-h":
                        showInstructions();
                        break;
                    case "-a":
                        showCards(players[playerIndex]);
                        break;
                    case "-o":
                        isGameOver = true;
                        break;
                    default:
                        break;
                }
            }
            if (playOrDrawInstruction.isEmpty()) {
                return isGameOver;
            }
            switch (playOrDrawInstruction.get(0)) {
                case "-p":
                    isGameOver = playCard(getCard(playOrDrawInstruction.get(1)), players[playerIndex]);
                    break;
                case "-d":
                    drawCard(players[playerIndex]);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            isGameOver = true;
        }
        if (cards.getCardsNum() == 0) {
            isGameOver = true;
        }
        return isGameOver;
    }

    /** 出牌，并做出对应效果 */
    private boolean playCard(UNO_Card card, UNO_Player player) {
        if (!player.removeCard(card)) {
            System.out.println("你没有这张牌，请重新出牌");
            return false;
        }
        if (card.getType() != UNO_Constants.UNIVERSAL &&
                lastCard.getColor() != card.getColor() && lastCard.getPoint() != card.getPoint()) {
            System.out.println("你不能出这张牌，你出的牌需要与前一张牌颜色或数字相同");
            player.addCard(card);
            return false;
        }
        System.out.println("你出了一张牌：" + card.getName());
        if (player.getCardsNum() == 0) {
            winIndex = playerIndex;
            return true;
        }
        switch (card.getType()) {
            case UNO_Constants.ITEM:
                switch (card.getPoint()) {
                    case UNO_Constants.SKIP:
                        playerIndex = (playerIndex + playerAmount + order) % playerAmount;
                        System.out.println("下家跳过回合");
                        break;
                    case UNO_Constants.REVERSE:
                        order *= (-1);
                        System.out.println("出牌顺序反转");
                        break;
                    case UNO_Constants.PLUS2:
                        int nextPlayerIndex = (playerIndex + playerAmount + order) % playerAmount;
                        players[nextPlayerIndex].addCard(cards.getTopCard());
                        players[nextPlayerIndex].addCard(cards.getTopCard());
                        playerIndex = (playerIndex + playerAmount + order) % playerAmount;
                        System.out.println("下家摸取两张牌，并跳过回合");
                        break;
                }
                break;
            case UNO_Constants.UNIVERSAL:
                switch (card.getPoint()) {
                    case UNO_Constants.PLUS4:
                        int nextPlayerIndex = (playerIndex + playerAmount + order) % playerAmount;
                        players[nextPlayerIndex].addCard(cards.getTopCard());
                        players[nextPlayerIndex].addCard(cards.getTopCard());
                        players[nextPlayerIndex].addCard(cards.getTopCard());
                        players[nextPlayerIndex].addCard(cards.getTopCard());
                        playerIndex = (playerIndex + playerAmount + order) % playerAmount;
                        System.out.println("下家摸取四张牌，并跳过回合");
                    case UNO_Constants.CHANGECOLOR:
                        boolean isLegal = false;
                        System.out.println("请你选择万能牌颜色(r, y, b, g)：");
                        while (!isLegal) {
                            String input = scanner.nextLine();
                            if (input.length() == 1
                                    && Arrays.asList(UNO_Constants.NORMAL_COLORS).contains(input.charAt(0))) {
                                card.setColor(Arrays.asList(UNO_Constants.NORMAL_COLORS).indexOf(input.charAt(0)));
                                isLegal = true;
                            } else {
                                System.out.println("格式错误，请重新输入");
                            }
                        }
                        break;
                }
                System.out.println("你将万能牌颜色调为" + UNO_Constants.NORMAL_COLORS_NAME[card.getColor()]);
                break;
        }
        lastCard = card;
        playerIndex = (playerIndex + playerAmount + order) % playerAmount;
        return false;
    }

    /** 摸牌，判定摸到的牌能不能出 */
    private void drawCard(UNO_Player player) {
        UNO_Card card = cards.getTopCard();
        player.addCard(card);
        System.out.println("你摸到了" + card.getName());
        if (card.getType() == UNO_Constants.UNIVERSAL ||
                lastCard.getColor() == card.getColor() || lastCard.getPoint() == card.getPoint()) {
            boolean playOrNOt = UserInterface.yesOrNo(systemOperation, "这张牌符合出牌条件，请问是否出牌");
            if (playOrNOt) {
                playCard(card, player);
                return;
            }
        }
        showCards(player);
        playerIndex = (playerIndex + playerAmount + order) % playerAmount;
    }

    /** 喊UNO */
    private void shout(UNO_Player player) {
        player.setIsUNO(true);
        if (player.getCardsNum() != 2) {
            player.setIsUNO(false);
            player.addCard(cards.getTopCard());
            player.addCard(cards.getTopCard());
            player.setIsUNO(true);
            System.out.println("你不能喊UNO，罚两张牌");
            showCards(player);
        } else {
            System.out.println("你成功喊了UNO");
        }
    }

    /** 质疑前一名玩家 */
    private void question(UNO_Player player, int playerIndex) {
        int lastPlayerIndex = (playerIndex + players.length - order) % players.length;
        if (!players[lastPlayerIndex].getIsUNO() && players[lastPlayerIndex].getCardsNum() == 1) {
            players[lastPlayerIndex].addCard(cards.getTopCard());
            players[lastPlayerIndex].addCard(cards.getTopCard());
            System.out.println("质疑成功，上家罚摸两张牌");
        } else {
            player.addCard(cards.getTopCard());
            player.addCard(cards.getTopCard());
            System.out.println("质疑失败，你罚摸两张牌");
            showCards(player);
        }
    }

    /** 每局游戏后结算，记录并显示排名 */
    private void end() {
        if (winIndex == -1) {
            winAmount = 0;
            if (cards.getCardsNum() == 0) {
                System.out.println("抽牌堆已抽完，无人出完牌，开始结算手牌");
            } else {
                System.out.println("游戏中断，开始结算手牌");
            }
            int min = -1;
            int[] scores = new int[playerAmount];
            for (int i = 0; i < playerAmount; i++) {
                UNO_Player player = players[i];
                scores[i] = player.getScore();
                if (scores[i] < min || min == -1) {
                    min = scores[i];
                }
                System.out.println("玩家" + (i + 1) + "剩余手牌点数为" + scores[i]);
            }
            Arrays.fill(playersForStarterConfirm, -1);
            for (int i = 0; i < playerAmount; i++) {
                if (scores[i] == min) {
                    playersForStarterConfirm[i] = 0;
                    System.out.print("玩家" + (i + 1) + ", ");
                    winIndex = i;
                    winAmount++;
                }
            }
            System.out.println("\b\b手牌点数最小，赢得比赛");
        } else {
            System.out.println("玩家" + (winIndex + 1) + "出完了手牌，赢得比赛");
            winAmount = 1;
        }
    }
}