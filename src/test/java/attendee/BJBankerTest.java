package attendee;

import card.BJ_Card;
import cardCollection.BJ_CardCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import utility.BJ_Constants;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BJBankerTest {
    @InjectMocks
    @Spy
    BJ_Banker banker = new BJ_Banker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCards() {
        BJ_Card card1 = new BJ_Card(10, 1);
        BJ_Card card2 = new BJ_Card(7, 2);
        BJ_Card card3 = new BJ_Card(4, 1);
        BJ_Card card4 = new BJ_Card(1, 1);

        int index = 0;
        banker.getCards(card1, index);
        assertEquals(true, banker.getIsNeedCard());

        banker.getCards(card2, index);
        assertEquals(false, banker.getIsNeedCard());

        banker.getCards(card3,index);
        assertEquals(banker.winflag[index], BJ_Constants.UNJUDGED);

        banker.getCards(card4,index);
        assertEquals(banker.winflag[index], BJ_Constants.LOSE);
    }

    @Test
    void getIsNeedCard() {
        boolean isNeedCard = banker.getIsNeedCard();
        assertEquals(true, isNeedCard);
    }

    @Test
    void setAndGetIsHavingInitA() {
        boolean isHavingInitA = true;
        banker.setIsHavingInitA(isHavingInitA);
        assertEquals(isHavingInitA, banker.getIsHavingInitA());
    }

    @Test
    void showCards(){
        BJ_CardCollection cardCollection1 = Mockito.mock(BJ_CardCollection.class);
        BJ_CardCollection cardCollection2 = Mockito.mock(BJ_CardCollection.class);
        ArrayList<BJ_CardCollection> cards = new ArrayList<>();
        cards.add(cardCollection1);
        cards.add(cardCollection2);
        banker.cardCollections = cards;
        Mockito.when(cardCollection1.stringOfAllCards()).thenReturn("stringOfAllCards");
        Mockito.when(cardCollection1.stringOfAllCardsWithStar()).thenReturn("stringOfAllCardsWithStar");
        assertEquals("stringOfAllCards", banker.showCards(false));
        assertEquals("stringOfAllCardsWithStar", banker.showCards(true));
    }
}