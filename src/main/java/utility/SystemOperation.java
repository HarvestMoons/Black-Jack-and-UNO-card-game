package utility;

import java.util.Scanner;

public class SystemOperation {
    private final Scanner scanner;

    public SystemOperation(Scanner scanner) {
        this.scanner = scanner;
    }

    // 读取系统输入的指令
    public String readOperation() {
        return scanner.nextLine();
    }
}
