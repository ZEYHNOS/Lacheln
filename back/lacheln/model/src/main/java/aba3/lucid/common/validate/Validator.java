package aba3.lucid.common.validate;

public class Validator {

    public static boolean isInvalidId(long id) {
        return id <= 0;
    }

    public static boolean isInvalidId(Long id) {
        return id == null || id <= 0;
    }

    public static boolean isInvalidId(long id1, long id2) {
        return id1 <= 0 || id2 <= 0;
    }

}
