package cat.uab.tqs;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

class GamePanelTest implements Test {

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}