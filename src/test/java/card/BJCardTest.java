package card;

import org.mockito.Mockito;
import utility.BJ_Constants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;

class BJCardTest {
    @Spy
    BJ_Card card = new BJ_Card(1, 1);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generatePointName() {
        card = new BJ_Card(BJ_Constants.A_POINT, 1);
        String pointName = card.generatePointName();
        assertEquals(pointName, "A");

        card = new BJ_Card(BJ_Constants.J_POINT, 1);
        pointName = card.generatePointName();
        assertEquals(pointName, "J");

        card = new BJ_Card(BJ_Constants.Q_POINT, 1);
        pointName = card.generatePointName();
        assertEquals(pointName, "Q");

        card = new BJ_Card(BJ_Constants.K_POINT, 1);
        pointName = card.generatePointName();
        assertEquals(pointName, "K");

        card = new BJ_Card(10, 1);
        pointName = card.generatePointName();
        assertEquals(pointName, "10");
    }

    @Test
    void generateSuitName() {
        card = new BJ_Card(10, BJ_Constants.SPADE);
        String suitName = card.generateSuitName();
        assertEquals(suitName, "Spade");

        card = new BJ_Card(10, BJ_Constants.HEART);
        suitName = card.generateSuitName();
        assertEquals(suitName, "Heart");

        card = new BJ_Card(10, BJ_Constants.DIAMOND);
        suitName = card.generateSuitName();
        assertEquals(suitName, "Diamond");

        card = new BJ_Card(10, BJ_Constants.CLUB);
        suitName = card.generateSuitName();
        assertEquals(suitName, "Club");
    }

    @Test
    void getPoint() {
        int point = card.getPoint();
        assertEquals(point, 1);
    }

    @Test
    void getSuit() {
        int suit = card.getSuit();
        assertEquals(suit, 1);
    }

    @Test
    void getPointName() {
        String pointName = card.getPointName();
        assertEquals(pointName, "A");
    }

    @Test
    void getSuitName() {
        String suitName = card.getSuitName();
        assertEquals(suitName, "Heart");
    }

    @Test
    void Card() {
        try {
            new BJ_Card(0, 0);
        } catch (Exception e) {
            assertEquals("Invalid card point.", e.getMessage());
        }

        try {
            new BJ_Card(1, -1);
        } catch (Exception e) {
            assertEquals("Invalid card suit.", e.getMessage());
        }
    }

    @Test
    void checkA() {
        BJ_Card card1 = new BJ_Card(1,1);
        assertEquals(true, card1.checkA());
    }
}