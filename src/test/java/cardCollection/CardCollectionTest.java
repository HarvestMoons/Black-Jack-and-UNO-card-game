package cardCollection;

import card.BJ_Card;
import cardCollection.BJ_CardCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardCollectionTest {
    @InjectMocks
    @Spy
    BJ_CardCollection cardCollection = new BJ_CardCollection();
    @Mock
    BJ_Card card = new BJ_Card(1, 1);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCard() {
        cardCollection.addCard(card);
        assertEquals(cardCollection.getTotalCards(), 1);
        assertEquals(cardCollection.getTopCard(), card);
    }

    @Test
    void shuffle() {
        try {
            cardCollection.shuffle();
        } catch (IllegalArgumentException e) {
            assertEquals("The cardcollection is empty.", e.getMessage());
        }

        cardCollection.getWholeCards();
        BJ_Card old_top_card = cardCollection.getTopCard();
        cardCollection.shuffle();
        BJ_Card new_top_card = cardCollection.getTopCard();
        assertNotEquals(old_top_card, new_top_card);
    }

    @Test
    void stringOfAllCards() {
        try {
            cardCollection.stringOfAllCards();
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "The cardcollection is empty.");
        }

        cardCollection.addCard(new BJ_Card(1, 1));
        String result = "| " + "A of Heart" + "\t| ";
        assertEquals(result, cardCollection.stringOfAllCards());
    }

    @Test
    void getTotalCards() {
        assertEquals(cardCollection.getTotalCards(), 0);

        cardCollection.addCard(new BJ_Card(1, 1));
        assertEquals(cardCollection.getTotalCards(), 1);
    }

    @Test
    void getTopCard() {
        card = new BJ_Card(1, 1);
        cardCollection.addCard(card);
        assertEquals(cardCollection.getTotalCards(), 1);
        assertEquals(card, cardCollection.getTopCard());
        assertEquals(cardCollection.getTotalCards(), 0);
    }

    @Test
    void getCards() {
        BJ_Card card1 = new BJ_Card(1, 1);
        BJ_Card card2 = new BJ_Card(2, 2);
        cardCollection.addCard(card1);
        cardCollection.addCard(card2);
        ArrayList<BJ_Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        assertEquals(cards, cardCollection.getCards());
    }
}