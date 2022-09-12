package culture.africa.learn.ground.java.enums;

import culture.africa.learn.ground.java.enums.SealedType;

public record PersonSimple(String name, int age, String job) implements SealedType {
}
