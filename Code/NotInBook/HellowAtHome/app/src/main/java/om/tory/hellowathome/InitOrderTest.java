package om.tory.hellowathome;

public class InitOrderTest {

    // 静态方法块1
    static {
        System.out.println("静态方法块1初始化");
    }
    // 静态属性
    private static String staticField = getStaticField();

    // 静态方法块2
    static {
        System.out.println("静态方法块2初始化");
    }

    // 普通属性
    private String field = getField();
    // 普通方法块
    {
        System.out.println(field);
        System.out.println("普通方法块初始化");
        System.out.println("Field Patch Initial");
    }
    // 构造函数
    public InitOrderTest() {
        System.out.println("构造函数初始化");
        System.out.println("Structure Initial ");
    }

    public static String getStaticField() {
        String statiFiled = "Static Field Initial";
        System.out.println("静态属性初始化");
        return statiFiled;
    }

    public static String getField() {
        String filed = "Field Initial";
        System.out.println("普通属性初始化");
        return filed;
    }
    // 主函数
    public static void main(String[] argc) {
        new InitOrderTest();
    }
}
