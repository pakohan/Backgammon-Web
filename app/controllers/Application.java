package controllers;

import controllers.de.htwg.upfaz.backgammon.controller.Core;
import controllers.de.htwg.upfaz.backgammon.gui.BackgammonFrame;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;
import java.util.UUID;

public class Application extends Controller {

    public static Result index() {
        return redirect(routes.Application.click());
    }

    public Result click() {
        Core currentGame = BackGammonGlobal.INJECTOR.getInstance(Core.class);
        currentGame.createGame();
        return ok(views.html.game.render(Json.toJson(currentGame.getGameMap()).toString(), currentGame.toString()));
    }

    public Result json() {

        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        int x = Integer.parseInt(values.get("x")[0]);
        int y = Integer.parseInt(values.get("y")[0]);

        Core currentGame = BackGammonGlobal.INJECTOR.getInstance(Core.class);
        currentGame.loadGame(UUID.fromString(values.get("id")[0]));

        currentGame.click(BackgammonFrame.getClickedField(x, y));

        return ok(Json.toJson(currentGame.getGameMap()));
    }
}
