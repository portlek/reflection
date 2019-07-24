package io.github.portlek.reflection.clazz;

import io.github.portlek.reflection.RefClass;
import io.github.portlek.reflection.constructor.ConstructorOf;
import io.github.portlek.reflection.field.FieldOf;
import io.github.portlek.reflection.method.MethodOf;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsTrue;

class ClassOfTest {

    private static final RefClass CLASS = new ClassOf(TestClass.class);

    @Test
    void getRealClass() {
        new Assertion<>(
            "Class is not equal to real class!",
            CLASS.getRealClass(),
            new IsEqual<>(TestClass.class)
        ).affirm();;
    }

    @Test
    void isInstance() {
        new Assertion<>(
            "Class is not instance of the object",
            CLASS.isInstance(new TestTestClass("Hasan", 21)),
            new IsTrue()
        ).affirm();
    }

    private static class TestTestClass extends TestClass {

        TestTestClass(String text, int age) {
            super(text, age);
        }
    }

    @Test
    void getMethod() {
        new Assertion<>(
            "Cannot find method",
            CLASS.getMethod("voidMethod"),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.getMethod("returnStringMethod"),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.getMethod("voidParameterMethod", String.class, Integer.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.getMethod("returnIntegerParameterMethod", String.class, Integer.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.getPrimitiveMethod("voidPrimitiveParameterMethod", String.class, Integer.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.getPrimitiveMethod("returnIntPrimitiveParameterMethod", String.class, Integer.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();
    }

    @Test
    void findMethodByParameter() {
        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByParameter(),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByParameter(String.class, Integer.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByParameter(String.class, int.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();
    }

    @Test
    void findPrimitiveMethodByParameter() {
        new Assertion<>(
            "Cannot find method",
            CLASS.findPrimitiveMethodByParameter(),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findPrimitiveMethodByParameter(String.class, Integer.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findPrimitiveMethodByParameter(String.class, int.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();
    }

    @Test
    void findMethodByName() {
        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByName("voidMethod"),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByName("returnStringMethod"),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByName("voidParameterMethod"),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByName("returnIntegerParameterMethod"),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByName("voidPrimitiveParameterMethod"),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByName("returnIntPrimitiveParameterMethod"),
            new IsInstanceOf(MethodOf.class)
        ).affirm();
    }

    @Test
    void findMethodByReturnType() {
        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByReturnType(void.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByReturnType(String.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByReturnType(Integer.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByReturnType(int.class),
            new IsInstanceOf(MethodOf.class)
        ).affirm();
    }

    @Test
    void findMethodByReturnTypeAsRefClass() {
        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByReturnType(
                new ClassOf(void.class)
            ),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByReturnType(
                new ClassOf(String.class)
            ),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByReturnType(
                new ClassOf(Integer.class)
            ),
            new IsInstanceOf(MethodOf.class)
        ).affirm();

        new Assertion<>(
            "Cannot find method",
            CLASS.findMethodByReturnType(
                new ClassOf(int.class)
            ),
            new IsInstanceOf(MethodOf.class)
        ).affirm();
    }

    @Test
    void getPrimitiveConstructor() {
        new Assertion<>(
            "Cannot find constructor!",
            CLASS.getPrimitiveConstructor(String.class, Integer.class),
            new IsInstanceOf(ConstructorOf.class)
        ).affirm();
    }

    @Test
    void findConstructor() {
        new Assertion<>(
            "Cannot find constructor!",
            CLASS.findConstructor(2),
            new IsInstanceOf(ConstructorOf.class)
        ).affirm();
    }

    @Test
    void getConstructor() {
        new Assertion<>(
            "Cannot find constructor!",
            CLASS.getConstructor(String.class, int.class),
            new IsInstanceOf(ConstructorOf.class)
        ).affirm();


        new Assertion<>(
            "Cannot find constructor!",
            CLASS.getConstructor(),
            new IsInstanceOf(ConstructorOf.class)
        ).affirm();
    }

    @Test
    void getField() {
        new Assertion<>(
            "Cannot find field!",
            CLASS.getField("text"),
            new IsInstanceOf(FieldOf.class)
        ).affirm();
    }

    @Test
    void findFieldFromClass() {
        new Assertion<>(
            "Cannot find field!",
            CLASS.findField(String.class),
            new IsInstanceOf(FieldOf.class)
        ).affirm();
    }

    @Test
    void findFieldFromRefClass() {
        new Assertion<>(
            "Cannot find field!",
            CLASS.findField(
                new ClassOf(String.class)
            ),
            new IsInstanceOf(FieldOf.class)
        ).affirm();
    }

    private interface ITest {
    }

    private static class TestClass implements ITest {

        private final String text;
        private final int age;

        TestClass(String text, int age) {
            this.text = text;
            this.age = age;
        }

        TestClass() {
            this("Hasan", 21);
        }

        private void voidMethod() {

        }

        private String returnStringMethod() {
            return "Called Return Method!";
        }

        private void voidParameterMethod(String param1, Integer param2) {

        }

        private Integer returnIntegerParameterMethod(String param1, Integer param2) {
            return 1;
        }

        private void voidPrimitiveParameterMethod(String param1, int param2) {

        }

        private int returnIntPrimitiveParameterMethod(String param1, int param2) {
            return 1;
        }

    }

}