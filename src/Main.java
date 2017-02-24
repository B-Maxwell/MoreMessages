import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.HashMap;

public class Main {


    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {

        Spark.init();
        //<editor-fold desc="Get /">
        Spark.get("/", (request, response) -> {

                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = users.get(name);

                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        m.put("messages", user.messages);
                        return new ModelAndView(user, "messages.html");
                    }

                },
                new MustacheTemplateEngine()
        );
        //</editor-fold>

        //<editor-fold desc="Create-User">
        Spark.post("/create-user", (request, response) -> {
                    String name = request.queryParams("userName");
                    String password = request.queryParams("password");

                    User user = users.get(name);

                    if (user == null) {
                        user = new User(name, password);
                        users.put(name, user);
                        Session session = request.session();
                        session.attribute("userName", name);
                        response.redirect("/");
                    }

                    else if (password.equals(user.password)) {
                        Session session = request.session();
                        session.attribute("userName", name);
                        response.redirect("/");

                    } else {
                        response.redirect("/");
                    }

                    return "";
                }
        );
        //</editor-fold>

        //<editor-fold desc="Create-Message">
        Spark.post("/create-message", (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = users.get(name);
                    user.messages.add(request.queryParams("messagePost"));
                    response.redirect("/");
                    return "";
                }
        );
        //</editor-fold>

        //<editor-fold desc="Update-Message">
        Spark.post("/update-message", (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    int messageNumber = Integer.parseInt(request.queryParams("updateNumber"));
                    User user = users.get(name);
                    user.messages.set(messageNumber - 1, request.queryParams("messageUpdate"));
                    response.redirect("/");
                    return "";
                }
        );
        //</editor-fold>

        //<editor-fold desc="Delete-Message">
        Spark.post("/delete-message", (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    int messageNumber = Integer.parseInt(request.queryParams("deleteNumber"));
                    User user = users.get(name);
                    user.messages.remove(messageNumber - 1);
                    response.redirect("/");
                    return "";
                }
        );
        //</editor-fold>

        //<editor-fold desc="Logout">
        Spark.post("/logout", (request, response) -> {
            Session session = request.session();
            session.invalidate();
            response.redirect("/");
            return "";
        });
        //</editor-fold>

    }
}
