package org.example;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextInputControlMatchers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
@ExtendWith(ApplicationExtension.class)
public class AppTest {
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new
                FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Start
    private void start(Stage stage) throws IOException {
        Scene scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    void clearCalculator(FxRobot robot) {
        robot.clickOn("#clearBtn"); // Press clear button before each test
    }

    private void selectBase(FxRobot robot, String base) {
        robot.clickOn("#baseSelector"); // Open base selection
        robot.clickOn(base); // Select the correct base
    }



    @Test
    void test_1(FxRobot robot) {
        selectBase(robot, "Hexadecimal");

        robot.clickOn("#minus");
        robot.clickOn("#A");
        robot.clickOn("#Zero");
        robot.clickOn("#Zero");
        robot.clickOn("#minus");
        robot.clickOn("#one");
        robot.clickOn("#B");
        robot.clickOn("#mul");
        robot.clickOn("#one");
        robot.clickOn("#one");
        robot.clickOn("#div");
        robot.clickOn("#nine");
        robot.clickOn("#plus");
        robot.clickOn("#three");
        robot.clickOn("#D");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("-9F6"));
    }

    @Test
    void test_2(FxRobot robot) {
        selectBase(robot, "Hexadecimal");

        robot.clickOn("#A");
        robot.clickOn("#Zero");
        robot.clickOn("#Zero");
        robot.clickOn("#div");
        robot.clickOn("#Zero");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText(
                "Error: trying to divide by 0 (evaluated: \"0\")"));
    }

    @Test
    void test_3(FxRobot robot) {
        selectBase(robot, "Hexadecimal");

        robot.clickOn("#one");
        robot.clickOn("#C");
        robot.clickOn("#plus");
        robot.clickOn("#two");
        robot.clickOn("#D");
        robot.clickOn("#plus");
        robot.clickOn("#three");
        robot.clickOn("#F");
        robot.clickOn("#minus");
        robot.clickOn("#four");
        robot.clickOn("#E");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("3A"));
    }


    @Test
    void test_4(FxRobot robot) {
        selectBase(robot, "Octal");

        robot.clickOn("#seven");
        robot.clickOn("#Zero");
        robot.clickOn("#Zero");
        robot.clickOn("#minus");
        robot.clickOn("#one");
        robot.clickOn("#two");
        robot.clickOn("#mul");
        robot.clickOn("#one");
        robot.clickOn("#one");
        robot.clickOn("#div");
        robot.clickOn("#six");
        robot.clickOn("#plus");
        robot.clickOn("#three");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("664"));
    }

    @Test
    void test_5(FxRobot robot) {
        selectBase(robot, "Binary");

        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero");
        robot.clickOn("#one"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#one");
        robot.clickOn("#and");
        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero");
        robot.clickOn("#one"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero");
        robot.clickOn("#or");
        robot.clickOn("#not");
        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero");
        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#one");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("11111110"));
    }

    @Test
    void test_6(FxRobot robot) {
        selectBase(robot, "Decimal");

        robot.clickOn("#five");
        robot.clickOn("#nine");
        robot.clickOn("#plus");
        robot.clickOn("#plus");
        robot.clickOn("#one");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText(
                "Error: invalid expression"));
    }

    @Test
    void test_7(FxRobot robot) {
        selectBase(robot, "Decimal");

        robot.clickOn("#five");
        robot.clickOn("#eight");
        robot.clickOn("#plus");
        robot.clickOn("#one");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("59"));
    }

    @Test
    void test_8(FxRobot robot) {
        selectBase(robot, "Decimal");

        robot.clickOn("#minus");
        robot.clickOn("#one");
        robot.clickOn("#Zero");
        robot.clickOn("#Zero");
        robot.clickOn("#plus");
        robot.clickOn("#five");
        robot.clickOn("#Zero");
        robot.clickOn("#mul");
        robot.clickOn("#three");
        robot.clickOn("#div");
        robot.clickOn("#two");
        robot.clickOn("#minus");
        robot.clickOn("#five");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("-30"));
    }

    @Test
    void test_9(FxRobot robot) {
        selectBase(robot, "Octal");

        robot.clickOn("#seven");
        robot.clickOn("#one");
        robot.clickOn("#Zero");
        robot.clickOn("#minus");
        robot.clickOn("#one");
        robot.clickOn("#four");
        robot.clickOn("#mul");
        robot.clickOn("#one");
        robot.clickOn("#Zero");
        robot.clickOn("#div");
        robot.clickOn("#four");
        robot.clickOn("#plus");
        robot.clickOn("#five");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("665"));
    }



    @Test
    void test_10(FxRobot robot) {
        selectBase(robot, "Hexadecimal");
        robot.clickOn("#B");
        robot.clickOn("#two");
        robot.clickOn("#minus");
        robot.clickOn("#three");
        robot.clickOn("#C");
        robot.clickOn("#mul");
        robot.clickOn("#two");
        robot.clickOn("#plus");
        robot.clickOn("#A");
        robot.clickOn("#Zero");
        robot.clickOn("#div");
        robot.clickOn("#five");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("5A"));
    }

    @Test
    void test_11(FxRobot robot) {
        selectBase(robot, "Binary");
        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero");
        robot.clickOn("#one"); robot.clickOn("#Zero"); robot.clickOn("#one"); robot.clickOn("#Zero");
        robot.clickOn("#and");
        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero");
        robot.clickOn("#one"); robot.clickOn("#one"); robot.clickOn("#Zero"); robot.clickOn("#Zero");
        robot.clickOn("#or");
        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero");
        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#one"); robot.clickOn("#one");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("00001011"));
    }

    @Test
    void test_12(FxRobot robot) {
        selectBase(robot, "Octal");
        robot.clickOn("#seven");
        robot.clickOn("#seven");
        robot.clickOn("#seven");
        robot.clickOn("#plus");
        robot.clickOn("#one");
        robot.clickOn("#Zero");
        robot.clickOn("#minus");
        robot.clickOn("#five");
        robot.clickOn("#mul");
        robot.clickOn("#one");
        robot.clickOn("#Zero");
        robot.clickOn("#div");
        robot.clickOn("#four");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("775"));
    }

    @Test
    void test_13(FxRobot robot) {
        selectBase(robot, "Binary");

        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero");
        robot.clickOn("#one"); robot.clickOn("#one"); robot.clickOn("#Zero"); robot.clickOn("#one");
        robot.clickOn("#xor");
        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero");
        robot.clickOn("#one"); robot.clickOn("#Zero"); robot.clickOn("#one"); robot.clickOn("#one");
        robot.clickOn("#or");
        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#Zero");
        robot.clickOn("#Zero"); robot.clickOn("#Zero"); robot.clickOn("#one"); robot.clickOn("#Zero");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("00000110"));
    }

    @Test
    void test_14(FxRobot robot) {
        selectBase(robot, "Octal");
        robot.clickOn("#minus");
        robot.clickOn("#seven");
        robot.clickOn("#plus");
        robot.clickOn("#five");
        robot.clickOn("#equal");

        FxAssert.verifyThat("#result", TextInputControlMatchers.hasText("-2"));
    }



}
