package utility;

import java.util.*;

public class UserInterface {
	public static boolean yesOrNo(SystemOperation systemOperation, String choiceInfo) {
		System.out.println(choiceInfo);
		String userInput = systemOperation.readOperation().toUpperCase();
		while (!"Y".equals(userInput) && !"N".equals(userInput)) {
			System.out.println("Invalid command! Please enter Y(y) or N(n):");
			userInput = systemOperation.readOperation().toUpperCase();
		}
		return "Y".equals(userInput);
	}

	/** 要求用户输入1-max间的数字，返回该数字 */
	public static int getInt(Scanner scanner, int max, String text) {
		System.out.println(text);
		boolean inputSuccess = false;
		Integer result = null;
		while (!inputSuccess) {
			try {
				String userInput = scanner.nextLine();
				result = Integer.parseInt(userInput);
				if (result <= 0 || result > max) {
					throw new Exception();
				}
				inputSuccess = true;
			} catch (Exception e) {
				StringBuilder output = new StringBuilder("Invalid command! Please enter ");
				for (int i = 0; i < max - 1; i++) {
					output.append((i + 1)).append(' ');
				}
				output.append("or ").append(max).append(':');
				System.out.println(output);
			}
		}
		return result;
	}

	public static int getPlayerAmount(SystemOperation systemOperation, int maxPlayerAmount, int minPlayerAmount) {
		System.out.println("Please enter player amount(" + minPlayerAmount + "~" + maxPlayerAmount + "):");
		String userInput = systemOperation.readOperation();
		while (!userInput.matches("\\d+") || Integer.parseInt(userInput) < minPlayerAmount
				|| Integer.parseInt(userInput) > maxPlayerAmount) {
			System.out
					.println("Invalid command! Please enter player amount(" + minPlayerAmount + "~" + maxPlayerAmount
							+ "):");
			userInput = systemOperation.readOperation();
		}
		return Integer.parseInt(userInput);
	}

	public static int gameChoice(Scanner scanner) {
		System.out.println("What game do you want to play today?(" + BJ_Constants.BJ_MODE + ": BlackJack; " +
				UNO_Constants.UNO_MODE + ": UNO)");
		System.out.println("Enter your choice here:\t");
		String userInput = scanner.next();
		while (!Objects.equals(userInput, "1") && !Objects.equals(userInput, "2")) {
			System.out.println("Invalid input! Please enter 1 or 2 to choose game type.");
			userInput = scanner.nextLine();
		}
		return Integer.parseInt(userInput);
	}

	public static void printSplitMessage(int cardCollectionNum, int currentCollectionNum) {
		System.out.println();
		if (cardCollectionNum != 2) {
			return;
		}
		switch (currentCollectionNum) {
			case 0:
				System.out.println("For your first card collection:");
				break;
			case 1:
				System.out.println("For your second card collection:");
				break;
			default:
				System.err.println("错误的牌组数量：" + currentCollectionNum);
		}

	}

}
