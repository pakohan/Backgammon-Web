package controllers;

import com.google.inject.Inject;
import controllers.de.htwg.upfaz.backgammon.controller.Core;
import controllers.de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

public class Application extends Controller {

    @Inject
    private Core currentGame;

    public static Result index() {
        return redirect(routes.Application.click());
    }

    public Result click() {

        if (currentGame.getWinner() == -1) {
            return ok(views.html.game.render(Json.toJson(currentGame.getGameMap()).toString(), currentGame.toString()));
        } else {
            return ok(views.html.game.render("", "game finished"));
        }
    }

    public Result json() {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        int x = Integer.parseInt(values.get("x")[0]);
        int y = Integer.parseInt(values.get("y")[0]);

        System.out.println(x + ":" + y);

        currentGame.click(BackgammonFrame.getClickedField(x, y));

        return ok(Json.toJson(currentGame.getGameMap()));
    }
}
