package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RegexpsEnum {

    REGEXP_FOR_CUT_STRING_BY_N_LEADING_CHARACTERS("(?<=[^\0]{%d})[^\0]*", "Выбирает все символы после N-ого для обрезания строки");

    @Getter
    private String regexp;
    private String description;

    public <T> String getRegexp(T symbols) {
        return String.format(regexp, symbols);
    }
}
