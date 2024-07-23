package attendee;

import card.BJ_Card;
import cardCollection.BJ_CardCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BJAttendeeTest {

    @InjectMocks
    @Spy
    BJ_Attendee attendee = new BJ_Attendee() {
        @Override
        public String showCards(boolean isShowWithStars) {
            return null;
        }
        @Override
        public void getCards(BJ_Card card, int index) {
        }
    } ;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTotalPoints() {
        attendee.totalPoints[0] = 12;
        attendee.totalPoints[1] = 10;
        assertEquals(12 , attendee.getTotalPoints()[0]);
        assertEquals(10,attendee.getTotalPoints()[1]);
    }

    @Test
    void clearCards() {
        attendee.clearCards();
        assertEquals(1,attendee.cardCollections.size());
    }

    @Test
    void getCardCollection() {
        ArrayList<BJ_CardCollection> cardCollections = new ArrayList<>();
        BJ_CardCollection cardCollection1 = Mockito.mock(BJ_CardCollection.class);
        BJ_CardCollection cardCollection2 = Mockito.mock(BJ_CardCollection.class);
        cardCollections.add(cardCollection1);
        cardCollections.add(cardCollection2);
        attendee.cardCollections = cardCollections;
        assertEquals(cardCollection2,attendee.getCardCollection(1));
    }
}