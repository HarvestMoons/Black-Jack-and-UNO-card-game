package attendee;

import card.UNO_Card;
import cardCollection.UNO_CardCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import utility.UNO_Constants;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UNO_PlayerTest {
    @InjectMocks
    @Spy
    UNO_Player player = new UNO_Player();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getScore(){
        UNO_Card card1 = Mockito.mock(UNO_Card.class);
        UNO_Card card2 = Mockito.mock(UNO_Card.class);
        UNO_Card card3 = Mockito.mock(UNO_Card.class);
        player.cards.addCard(card1);
        player.cards.addCard(card2);
        player.cards.addCard(card3);
        Mockito.when(card1.getType()).thenReturn(UNO_Constants.NORMAL);
        Mockito.when(card1.getPoint()).thenReturn(9);
        Mockito.when(card2.getType()).thenReturn(UNO_Constants.ITEM);
        Mockito.when(card3.getType()).thenReturn(UNO_Constants.UNIVERSAL);
        //score = 9 + 20（工能牌） + 50（万能牌） = 79分
        int result = player.getScore();
        assertEquals(79, result);
    }
}