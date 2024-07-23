package main;

import java.util.*;

import controller.*;
import utility.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int gameMode = UserInterface.getInt(scanner, 2,
                "What game do you want to play today?(" + BJ_Constants.BJ_MODE + ": BlackJack; " +
                        UNO_Constants.UNO_MODE + ": UNO)");
        switch (gameMode) {
            case BJ_Constants.BJ_MODE:
                Dealer dealer = Dealer.getDealerInstance(scanner);
                dealer.game();
                break;
            case UNO_Constants.UNO_MODE:
                UNO_Controller unoController = UNO_Controller.getDealerInstance(scanner);
                unoController.game();
                break;
            default:
                System.out.println("Undefined game type!");
        }
        scanner.close();
    }
}
