package culture.africa.learn.ground.java.enums;

public sealed interface SealedType permits PersonSimple, PersonComplexe {
    String name();
    int age();
}
