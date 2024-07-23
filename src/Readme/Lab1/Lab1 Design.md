# 第十小组 Lab1 设计思路

## 类介绍 

在lab1中，我们设计了3个类，完成了21点游戏的洗牌功能，以下将对各类逐一介绍：

### Card 类：

用于表示一张牌的类，有以下四个成员变量,其意义见注释：

~~~java
    private int point;             //点数（1~13）
    private int suit;			   //花色（0~3表示4种花色 0:spade♠ 1:heart♥ 2:diamond♦ 3:club♣）
    private String pointName;	   //该卡牌上的点数名（"A","2","3"..."K"）
    private String suitName;	   //该卡牌上的花色名
~~~

Card类中，有点数、花色与其名称的转换方法，并且在此类中定义了一些常量以提高代码可读性。

### CardCollection类

用于表示一套牌的类，有以下两个成员变量,其意义见注释：

~~~java
    private ArrayList<Card> cards;  //这套牌中所有牌的集合
    private int totalCards;			//总牌数
~~~

一套牌默认有52张，当然，我们也添加了向牌组中再增加牌的方法。

CardCollection类自带洗牌方法（使用Collection自带的 Collections.shuffle方法实现）与按排列顺序打印各张牌信息的方法。

### Main类

主控类，用于控制初始化、洗牌、打印信息逐一进行。

## 运行结果

程序的运行结果如下：


    Before shuffle:
    | A of Spade    | 2 of Spade    | 3 of Spade    | 4 of Spade    | 5 of Spade    | 6 of Spade    | 7 of Spade    | 8 of Spade    | 9 of Spade    | 10 of Spade   | J of Spade    | Q of Spade    | K of Spade    | A of Heart     | 2 of Heart    | 3 of Heart    | 4 of Heart    | 5 of Heart    | 6 of Heart    | 7 of Heart    | 8 of Heart    | 9 of Heart    | 10 of Heart   | J of Heart    | Q of Heart    | K of Heart    | A of Diamond  | 2 of Diamond   | 3 of Diamond  | 4 of Diamond  | 5 of Diamond  | 6 of Diamond  | 7 of Diamond  | 8 of Diamond  | 9 of Diamond  | 10 of Diamond | J of Diamond  | Q of Diamond  | K of Diamond  | A of Club     | 2 of Club      | 3 of Club     | 4 of Club     | 5 of Club     | 6 of Club     | 7 of Club     | 8 of Club     | 9 of Club     | 10 of Club    | J of Club     | Q of Club     | K of Club     |
    After shuffle:
    | 10 of Club    | 3 of Spade    | 3 of Heart    | 2 of Heart    | 3 of Club     | A of Club     | 2 of Spade    | Q of Heart    | 10 of Spade   | 4 of Spade    | 8 of Heart    | A of Spade    | Q of Club     | 6 of Heart     | J of Diamond  | 9 of Heart    | 7 of Heart    | 5 of Diamond  | A of Diamond  | 5 of Heart    | 6 of Spade    | 7 of Club     | 5 of Club     | 4 of Diamond  | K of Heart    | K of Spade    | 9 of Diamond  | Q of Spade     | 9 of Spade    | J of Club     | 4 of Heart    | 7 of Spade    | 6 of Diamond  | 9 of Club     | K of Club     | J of Spade    | 8 of Diamond  | 2 of Club     | 2 of Diamond  | J of Heart    | K of Diamond   | 8 of Club     | 3 of Diamond  | 10 of Heart   | 8 of Spade    | A of Heart    | Q of Diamond  | 4 of Club     | 7 of Diamond  | 5 of Spade    | 10 of Diamond | 6 of Club     |

## 重点更新过程记录

2.28 - 初步实现洗牌功能、向牌组中增加牌的方法、打印牌组信息的方法

2.29 - 增加“总牌数”变量、增加注释、将生成初始牌组从Main移动到CardCollection类中

3.1  - 增加常量用于替代一些数字提升代码可读性，如将13定义为K_POINT、将2定义为DIAMOND

