package card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import utility.UNO_Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UNOCardTest {
    @Spy
    UNO_Card card = new UNO_Card(1,5,1);

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getName(){
        Mockito.when(card.getColor()).thenReturn(UNO_Constants.RED);
        Mockito.when(card.getPoint()).thenReturn(UNO_Constants.SKIP);
        assertEquals("红色的跳过", card.getName());

        Mockito.when(card.getColor()).thenReturn(UNO_Constants.YELLOW);
        Mockito.when(card.getPoint()).thenReturn(UNO_Constants.REVERSE);
        assertEquals("黄色的反转", card.getName());

        Mockito.when(card.getColor()).thenReturn(UNO_Constants.BLUE);
        Mockito.when(card.getPoint()).thenReturn(UNO_Constants.PLUS2);
        assertEquals("蓝色的+2", card.getName());

        Mockito.when(card.getColor()).thenReturn(UNO_Constants.GREEN);
        Mockito.when(card.getPoint()).thenReturn(8);
        assertEquals("绿色的8", card.getName());

        Mockito.when(card.getColor()).thenReturn(UNO_Constants.BLACK);
        Mockito.when(card.getPoint()).thenReturn(UNO_Constants.PLUS4);
        assertEquals("万能牌+4", card.getName());

        Mockito.when(card.getColor()).thenReturn(UNO_Constants.BLACK);
        Mockito.when(card.getPoint()).thenReturn(UNO_Constants.CHANGECOLOR);
        assertEquals("万能牌调色", card.getName());
    }
}
