package hiof.gruppe1.Estivate.SQLParsers.TextConcatenation;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import java.util.ArrayList;

public class StringUtils {

    static StringBuilder createCommaValues(ArrayList<String> values) {
        StringBuilder sb = new StringBuilder();
        values.forEach((v) -> sb.append(String.format("\"%s\", ", v)));
        sb.deleteCharAt(sb.length() - 1);
        return sb;
    }

    static String createValuesInParenthesis(StringBuilder keyString) {
        StringBuilder finalString = new StringBuilder();
        finalString.append("(");
        finalString.append(keyString);
        finalString.deleteCharAt(finalString.length() - 1);
        finalString.append(")");
        return finalString.toString();
    }

    static String createWritableValue(SQLAttribute sqlAttr) {
        if (sqlAttr.getData().getClass().getSimpleName().equals("String")) {
            return String.format("\"%s\"", sqlAttr.getData().toString());
        }
        return sqlAttr.getData().toString();
    }
}
