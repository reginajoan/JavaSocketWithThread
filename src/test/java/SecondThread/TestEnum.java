package SecondThread;

import org.junit.Test;

enum Color{
    Red("Red"), Blue("Blue"), Green("Green");
    private String col;
    Color(String col) {
        this.col = col;
    }

    public String getColor(){
        return col;
    }
};

public class TestEnum {
    @Test
    public void TestData(){
        Color c = Color.Green;
        c = Color.Blue;
        System.out.println(c);
        System.out.println(Color.Red.getColor());
    }
}


