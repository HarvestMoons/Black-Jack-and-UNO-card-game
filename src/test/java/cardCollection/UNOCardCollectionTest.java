package cardCollection;

import card.UNO_Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import utility.UNO_Constants;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UNOCardCollectionTest {
    @Spy
    UNO_CardCollection cardCollection = new UNO_CardCollection();
    @BeforeEach
    void setUP(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void setWholeCards(){
        cardCollection.setWholeCards();
        //验证设置牌组后牌组中是否恰好为108张牌
        assertEquals(108, cardCollection.cards.size());
    }

    @Test
    void stringOfALLCards(){
        UNO_Card card = Mockito.mock(UNO_Card.class);
        ArrayList<UNO_Card> cards = new ArrayList<>();
        cards.add(card);
        cardCollection.cards = cards;
        Mockito.when(card.getName()).thenReturn("card's name");
        String result = cardCollection.stringOfAllCards();
        assertEquals("|  card's name" + "\t" + "|", result);
    }

    @Test
    void  removeCard(){
        UNO_Card card1 = Mockito.mock(UNO_Card.class);
        UNO_Card card2 = Mockito.mock(UNO_Card.class);
        ArrayList<UNO_Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        cardCollection.cards = cards;
        Mockito.when(card1.getColor()).thenReturn(UNO_Constants.BLUE);
        Mockito.when(card1.getType()).thenReturn(UNO_Constants.SKIP);
        Mockito.when(card2.getColor()).thenReturn(UNO_Constants.RED);
        Mockito.when(card2.getType()).thenReturn(UNO_Constants.SKIP);
        UNO_Card card = Mockito.mock(UNO_Card.class);
        Mockito.when(card.getColor()).thenReturn(UNO_Constants.BLUE);
        Mockito.when(card.getType()).thenReturn(UNO_Constants.SKIP);
        boolean result = cardCollection.removeCard(card);
        assertEquals(true,result);
        assertEquals(1,cardCollection.cards.size());
    }
}
