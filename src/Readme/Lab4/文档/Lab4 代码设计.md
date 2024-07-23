# Lab4 代码设计

## 一、设计类图

我们使用了Idea的PlantUML插件绘制了整个项目（Lab1~Lab4）的UML类图，源代码见Readme\Lab4\图表\UML Diagram.puml，导出的PNG图片如下：

![UML类图](./../图表/UML%20Diagram.png)

## 二、代码复用说明

- 通过构建抽象类CardCollection（参看上面给出的设计类图），我们实现了对此前设计的卡组类的属性和方法的复用，属性如总牌数、卡牌集合等，方法如取牌、加牌、洗牌等。
- 通过共同协商和需求分析，游戏的各个流程被合理拆分，starterConfirm()、initialCardDealing()、getFirstCard()等方法被分别调用，简化了dealCards()的实现，使代码更加清晰、模块化。
- 我们通过使用常量避免了硬编码，便于维护和修改。我们建立了常量类，常量如UNO_Constants.CLOCKWISE和UNO_Constants.INIT_CARD_NUM等在代码中多次使用，提高了代码复用性和可读性。
- 将游戏的不同功能模块解耦成独立的方法，如玩家操作playerRound()、指令处理dealInstruction()、出牌playCard()、摸牌drawCard()等，使得每个方法关注一个具体的功能，从而提高了代码的复用性和可测试性。
- 使用工具类方法来处理用户输入和游戏逻辑中的一些常见操作。例如UserInterface.yesOrNo()和UserInterface.getPlayerAmount()，这些工具类方法可以在BJ和UNO中均可调用，从而提高了代码复用性。

## 三、代码重构说明
- 方法拆分
  - 在`UNO_Controller.java`中的`playerRound()`方法进行了从开牌后到游戏结束前所有玩家操作相关的动作，通过while语句持续获取玩家指令，并重构拆分为`dealInstruction()`与`runInstruction()`。先进行指令处理，确认格式等无误后根据处理后的指令进行执行，其中在`runInstruction()`中，通过switch语句进入各个细分方法分块处理。
~~~java
for (String elseInstruction : elseInstructions) {
    switch (elseInstruction) {
        case "-u":
            shout(players[playerIndex]);
            break;
        case "-q":
            question(players[playerIndex], playerIndex);
            break;
        case "-h":
            showInstructions();
            break;
        case "-a":
            showCards(players[playerIndex]);
            break;
        case "-o":
            gameOver = true;
            break;
        default:
            break;
    }
}
if (playOrDrawInstruction.isEmpty()) {
    return gameOver;
}
switch (playOrDrawInstruction.get(0)) {
    case "-p":
        gameOver = playCard(getCard(playOrDrawInstruction.get(1)), players[playerIndex]);
        break;
    case "-d":
        drawCard(players[playerIndex]);
        break;
    default:
        break;
}
~~~

- 代码规范
  - 在重构过程中进行了代码变量命名规范，如boolean变量统一以`is`开头命名，如`isUNO`，`isInstructionLegal`等。
  - 在重构过程中进行了注释规范，如方法注释统一采用`/** */`格式，所有行尾注释调整为单起一行的注释。
- 字符串相关重构
  - 优化BJ_Card中方法为了调用toString分配了一个基本包装类型的问题，由((Integer) point).toString()调整为Integer.toString(point)，减少了不必要的装箱和拆箱操作，提升性能，提高代码可读性
  - 将长度为1的String常量优化为字符，提升性能。
  - 将字符串拼接由字符串相加替换为StringBuilder类内置的append方法，减少内存开销的同时简化了代码维护。调整后的代码如下：
~~~java
public String stringOfAllCards() {
    StringBuilder result = new StringBuilder();
    for (UNO_Card card : cards) {
        result.append("|  ").append(card.getName()).append("\t");
    }
    result.append("|");
    return result.toString();
}
~~~

- 代码抽象化
  - 将较为重复的代码根据用户输入类型抽象重构为能够进行多次复用的方法，如UNO的摸牌后是否出牌、BlackJack的是否投降等均抽象为`UserInterface.yesOrNo(scanner, choiceInfo)`方法，游戏选择BlackJack或UNO、BlackJack选择游戏模式等均抽象为`UserInterface.getInt(scanner,, max, text)`方法等等。
~~~java
public static boolean yesOrNo(Scanner scanner, String choiceInfo) {
	System.out.println(choiceInfo);
	String userInput = scanner.nextLine().toUpperCase();
	while (!"Y".equals(userInput) && !"N".equals(userInput)) {
		System.out.println("Invalid command! Please enter Y(y) or N(n):");
		userInput = scanner.nextLine().toUpperCase();
	}
	return "Y".equals(userInput);
}
~~~