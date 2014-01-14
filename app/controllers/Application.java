package controllers;

import controllers.de.htwg.upfaz.backgammon.controller.Core;
import controllers.de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.libs.OpenID;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Application extends Controller {

    public static Result index() {
        return redirect(routes.Application.click());
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public Result click() {
        Core currentGame = BackGammonGlobal.INJECTOR.getInstance(Core.class);
        currentGame.createGame();
        String color = session("color").equals("0") ? "White" : "Black";
        return ok(views.html.game.render(Json.toJson(currentGame.getGameMap()).toString(), currentGame.toString(), currentGame.getGameMap().get_id().toString(), color));
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public Result clicks(String id) {
        Core currentGame = BackGammonGlobal.INJECTOR.getInstance(Core.class);
        currentGame.loadGame(UUID.fromString(id));
        String color = session("color").equals("0") ? "White" : "Black";
        return ok(views.html.game.render(Json.toJson(currentGame.getGameMap()).toString(), currentGame.toString(), currentGame.getGameMap().get_id().toString(), color));
    }

    @play.mvc.Security.Authenticated(Secured.class)
    public Result json() {

        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        int x = (int) Double.parseDouble(values.get("x")[0]);
        int y = (int) Double.parseDouble(values.get("y")[0]);

        Core currentGame = BackGammonGlobal.INJECTOR.getInstance(Core.class);
        currentGame.loadGame(UUID.fromString(values.get("id")[0]));

        if (currentGame.getCurrentPlayer().getColor() == Integer.parseInt(session("color"))) {
            currentGame.click(BackgammonFrame.getClickedField(x, y));
        }

        return ok(Json.toJson(currentGame.getGameMap()));
    }

    public static Result login() {
        return ok(views.html.login.render(Form.form(User.class)));
    }

    public static Result logout() {
        session().clear();
        return redirect(routes.Application.index());
    }

    private static String player1;
    private static String player2;

    public Result authenticate() {
        Form<User> loginform = DynamicForm.form(User.class).bindFromRequest();

        User user = User.authenticate(loginform.get());

        int color;


        if (loginform.hasErrors() || user == null) {

            return badRequest(views.html.login.render(loginform));
        } else {
            if (player1 == null) {
                player1 = user.email;
                color = 0;
            } else if (player2 == null) {
                player2 = user.email;
                color = 1;
            } else {
                return badRequest(views.html.login.render(loginform));
            }
            session().clear();
            session("email", user.email);
            session("color", String.valueOf(color));
            return redirect(
                    controllers.routes.Application.index()
            );
        }
    }

    public static class User {

        private final static String defaultUser = "test@123.de";
        private final static String defaultPasswort = "nsa";

        public String email;
        public String password;

        public User() {
        }

        private User(final String email, final String password) {
            this.email = email;
            this.password = password;
        }

        public static User authenticate(User user) {
            if (user.email.equals(defaultUser) && user.password.equals(defaultPasswort)) {
                return new User(user.email, user.password);
            }

            return null;
        }
    }

    public static class Secured extends Security.Authenticator {

        @Override
        public String getUsername(Context ctx) {
            return ctx.session().get("email");
        }

        @Override
        public Result onUnauthorized(Context ctx) {
            return redirect(routes.Application.login());
        }
    }

    public static Result auth() {
        String providerUrl = "https://www.google.com/accounts/o8/id";
        String returnToUrl = "http://localhost:9000/openID/verify";

        Map<String, String> attributes = new HashMap<>();
        attributes.put("Email", "http://schema.openid.net/contact/email");
        attributes.put("FirstName", "http://schema.openid.net/namePerson/first");
        attributes.put("LastName", "http://schema.openid.net/namePerson/last");

        F.Promise<String> redirectUrl = OpenID.redirectURL(providerUrl, returnToUrl, attributes);
        return redirect(redirectUrl.get());
    }

    public Result verify() {
        F.Promise<OpenID.UserInfo> userInfoPromise = OpenID.verifiedId();
        OpenID.UserInfo userInfo = userInfoPromise.get();
        int color;
        if (player1 == null) {
            player1 = userInfo.attributes.get("Email");
            color = 0;
        } else if (player2 == null) {
            player2 = userInfo.attributes.get("Email");
            color = 1;
        } else {
            return badRequest(views.html.login.render(DynamicForm.form(User.class).bindFromRequest()));
        }
        session().clear();
        session("email", userInfo.attributes.get("Email"));
        session("color", String.valueOf(color));
        return redirect(
                routes.Application.index()
        );
    }
}
