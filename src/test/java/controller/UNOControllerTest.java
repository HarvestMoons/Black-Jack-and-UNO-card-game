package controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import utility.SystemOperation;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UNOControllerTest {
    @Spy
    UNO_Controller controller = new UNO_Controller(new Scanner(System.in));

    @Test
    void initialize(){
        controller.systemOperation = Mockito.mock(SystemOperation.class);
        Mockito.when(controller.systemOperation.readOperation()).thenReturn("3");
        controller.initialize();
        assertEquals(3,controller.playerAmount);
        assertEquals(3,controller.players.length);
    }

    @Test
    void starterConfirm(){
        //有上局获胜玩家的情况
        controller.winAmount = 1;
        controller.winIndex = 1;
        controller.starterConfirm();
        assertEquals(1, controller.playerIndex);

        //无上局获胜玩家的情况
        controller.winAmount = -1;
        controller.systemOperation = Mockito.mock(SystemOperation.class);
        Mockito.when(controller.systemOperation.readOperation()).thenReturn("3");
        controller.initialize();
        controller.starterConfirm();
    }

    @Test
    void uno(){
        controller.systemOperation = Mockito.mock(SystemOperation.class);
        Mockito.when(controller.systemOperation.readOperation()).thenReturn("3");
        controller.initialize();
        controller.starterConfirm();
        controller.initialCardDealing();
        controller.getFirstCard();
        Mockito.when(controller.systemOperation.readOperation()).thenReturn("","-d","-p r7","-o");
        controller.playerRound();
    }
}
