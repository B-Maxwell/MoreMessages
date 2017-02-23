import java.util.ArrayList;

/**
 * Created by Ben Maxwell on 2/22/17.
 */
public class User {
    String name;
    String password;
    ArrayList<String> messages = new ArrayList<>();


    public User(String name, String password) {
        this.name = name;
        this.password = password;

    }
}

