# 								第十小组 Lab2 设计思路

UML类图如下：

![image-20240405160509190](C:\Users\HP\AppData\Roaming\Typora\typora-user-images\image-20240405160509190.png)

由UML类图可见，Attendee为一抽象类，有子类Player（玩家）与Banker（庄家）。Main为主控类，Constants为常量类，Printer为工具类，用于和用户的交互。另有一测试类Test1（使用JUnit5），UML类图中不再给出。

以下就几个重点类的设计思路进行分析。

## Attendee：

Attendee类代表牌局参与者，有手牌cardCollection、总点数totalPoints、胜负情况判断winFlag（winFlag有四种模式：UNJUDGED、WIN、LOSE、DRAW）三个成员变量。

calcTotalPoints方法计算手牌点数和，其中A的点数先以11计算，若totalPoints大于上限，将若干张A点数改为以1计算，直至totalPoints不大于上限。

getCards为一抽象方法，从Dealer手上反复取牌，并判定是否有权限继续，子类Player（玩家）与Banker（庄家）各自提供了自己的实现。（庄家大于17点，不可再取牌，玩家无此限制）

## Dealer：

Deal类代表牌局的荷官（发牌机器+其他游戏流程控制），其手牌cardCollection代表的是这局游戏的总牌堆，发牌时（start为初始发牌、dealCards为后续发牌）从打乱的牌堆最上方取一张牌，加入牌局参与者的手牌。showAllCardPoints方法负责终局的胜负判断。dealer的实现中，我们考虑了多玩家情况，不过在目前的要求中（1v1），设置PLAYERAMOUNT = 1即可。game方法控制整个游戏流程，包括胜负判断后的继续游戏功能。

