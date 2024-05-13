package lat.jack.etl.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentVariables {

    private static final Dotenv dotenv = Dotenv.configure().load();

    public static String getVariable(String variableName) {
        return dotenv.get(variableName);
    }
}
