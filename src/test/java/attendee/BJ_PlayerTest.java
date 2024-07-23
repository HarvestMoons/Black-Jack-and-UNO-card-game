package attendee;

import card.BJ_Card;
import cardCollection.BJ_CardCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import utility.BJ_Constants;
import utility.SystemOperation;

import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class BJ_PlayerTest {
    Scanner sc = new Scanner(System.in);
    @Spy
    BJ_Player player = new BJ_Player(sc, 1000);
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        player.systemOperation = Mockito.mock(SystemOperation.class);
    }

    @Test
    void  showCards(){
        ArrayList<BJ_CardCollection> testCardCollections = new ArrayList<>();
        BJ_CardCollection cardCollection1 = Mockito.mock(BJ_CardCollection.class);
        BJ_CardCollection cardCollection2 = Mockito.mock(BJ_CardCollection.class);
        testCardCollections.add(cardCollection1);
        testCardCollections.add(cardCollection2);
        player.cardCollections = testCardCollections;
        Mockito.when(cardCollection1.stringOfAllCards()).thenReturn("cardCollection1_stringOfAllCards");
        Mockito.when(cardCollection2.stringOfAllCards()).thenReturn("cardCollection2_stringOfAllCards");
        String result = player.showCards(false);
        String expectedResult = "\n"+"cardCollection1_stringOfAllCards" + "\n" + "cardCollection2_stringOfAllCards";
        assertEquals(expectedResult, result);
    }

    @Test
    void splitChoice(){
        player.setIsAbleToSplit(true);
        //设置输入”Y“来选择分牌操作
        Mockito.when(player.systemOperation.readOperation()).thenReturn("Y");
        player.splitChoice();
        assertEquals(true, player.getWhetherSplit());
    }

    @Test
    void bet() {
        //输入下注金额，13，abc,10000,0均是错误的输入，最终下注金额为100
        Mockito.when(player.systemOperation.readOperation()).thenReturn("13","abc","10000","0","100");
        player.bet();
        //验证最终的下注值wager为正确输入100
        assertEquals(100, player.getWager());
    }

    @Test
    void doubleWagerChoice() {
        //正常加倍形况
        Mockito.when(player.systemOperation.readOperation()).thenReturn("200");
        player.bet();
        //m为异常输入
        Mockito.when(player.systemOperation.readOperation()).thenReturn("m","Y");
        player.doubleWagerChoice();
        assertEquals(player.getWager(),400);
        assertEquals(player.getGamblingState(), BJ_Constants.DOUBLE);

        //加倍后超过钱包总额
        Mockito.when(player.systemOperation.readOperation()).thenReturn("600");
        player.bet();
        Mockito.when(player.systemOperation.readOperation()).thenReturn("y");
        player.doubleWagerChoice();
    }

    @Test
    void clearWager() {
        Mockito.when(player.systemOperation.readOperation()).thenReturn("200");
        player.bet();
        assertEquals(player.getWager(),200);
        player.clearWager();
        assertEquals(player.getWager(),0);
    }

    @Test
    void getCards() {
        player.getCards(new BJ_Card(11,1),0);
        player.getCards(new BJ_Card(11,2),0);
        player.getCards(new BJ_Card(12,1),0);
        assertEquals( BJ_Constants.LOSE, player.winflag[0]);
        assertNotEquals(BJ_Constants.LOSE, player.winflag[1]);
    }

    @Test
    void surrenderChoice() {
        Mockito.when(player.systemOperation.readOperation()).thenReturn("1000");
        player.bet();
        Mockito.when(player.systemOperation.readOperation()).thenReturn("Y");
        player.surrenderChoice();
        assertEquals(player.getWager(),500);
        assertEquals(player.getGamblingState(), BJ_Constants.SURRENDER);
    }

    @Test
    void insuranceChoice(){
        Mockito.when(player.systemOperation.readOperation()).thenReturn("1000");
        player.bet();
        Mockito.when(player.systemOperation.readOperation()).thenReturn("Y");
        player.insuranceChoice();
        //验证保险费是否为500
        assertEquals(500, player.getInsurancePayment());
        //验证钱包余额是否为1000 - 500（保险费） = 500
        assertEquals(500, player.getBalance());
    }

    @Test
    void setIsAbleToSplit(){
        boolean isAbleToSplit = true;
        player.setIsAbleToSplit(isAbleToSplit);
        assertEquals(isAbleToSplit, player.getIsAbleToSplit());
    }

    @Test
    void renewMoney(){
        ArrayList<BJ_CardCollection> cardCollections = new ArrayList<>();
        BJ_CardCollection bj_cardCollection = Mockito.mock(BJ_CardCollection.class);
        cardCollections.add(bj_cardCollection);
        player.cardCollections = cardCollections;

        //1.玩家输了且无保险的情况
        player.winflag[0] = BJ_Constants.LOSE;
        //设置下注金额100
        Mockito.when(player.systemOperation.readOperation()).thenReturn("100");
        player.bet();
        //结算
        player.renewMoney(false);
        //验证钱包剩余金额
        assertEquals(900, player.getBalance());
        System.out.println("s");

        //2.玩家赢了且是黑杰克而且买了保险且庄家是黑杰克的情况
        player.winflag[0] = BJ_Constants.WIN;
        Mockito.when(player.systemOperation.readOperation()).thenReturn("100");
        player.bet();
        //选择投保险
        Mockito.when(player.systemOperation.readOperation()).thenReturn("Y");
        player.insuranceChoice();
        Mockito.when(bj_cardCollection.isBlackJack()).thenReturn(true);
        player.renewMoney(true);
        //900 - 50(交保险费) + 150 + 100（收保险费） = 1100
        assertEquals(1100,player.getBalance());

        //3.玩家赢了不是黑杰克无保险
        player.winflag[0] = BJ_Constants.WIN;
        Mockito.when(player.systemOperation.readOperation()).thenReturn("100");
        player.bet();
        Mockito.when(bj_cardCollection.isBlackJack()).thenReturn(false);
        player.renewMoney(false);
        //1100 + 100 = 1200
        assertEquals(1200,player.getBalance());
    }

    @Test
    void getCardCollectionAmount(){
        ArrayList<BJ_CardCollection> cardCollections = new ArrayList<>();
        BJ_CardCollection bj_cardCollection1 = Mockito.mock(BJ_CardCollection.class);
        BJ_CardCollection bj_cardCollection2 = Mockito.mock(BJ_CardCollection.class);
        cardCollections.add(bj_cardCollection1);
        cardCollections.add(bj_cardCollection2);
        player.cardCollections = cardCollections;
        assertEquals(2, player.getCardCollectionAmount());
    }

    @Test
    void initExceptBalance(){
        player.initExceptBalance();
        assertEquals(BJ_Constants.UNJUDGED, player.winflag[0]);
        assertEquals(BJ_Constants.UNJUDGED, player.winflag[1]);
        assertEquals(BJ_Constants.NORMAL, player.getGamblingState());
    }
}