package chess.controller;

import chess.domain.ChessGame;
import chess.domain.position.Positions;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.dto.ChessDTO;
import chess.service.ChessService;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class ChessWebController {

    private static final String VIEW = "game.html";
    private static final String INIT_TURN = "blank";
    private static final String WHITE = "white";
    private static final String BLACK = "black";
    private static final String GAME_ID = ":id";
    private static final String ERROR_ANNOUNCE_MESSAGE = "<br> 뒤로 가기를 눌러주세요. ";
    private static final int NOT_EXIST_USER = 0;
    private static final String INIT_STATE = "init";
    private static final String STATE = "state";

    private final ChessService chessService;

    public ChessWebController(ChessService chessService) {
        this.chessService = chessService;
    }

    public void run() {
        staticFileLocation("/static");
        ChessGame chessGame = new ChessGame();

        get("/", (req, res) -> modelAndView(new LinkedHashMap<>(), VIEW), new HandlebarsTemplateEngine());

        post("/input-user", (req, res) -> {
            String whiteName = req.queryParams(WHITE);
            String blackName = req.queryParams(BLACK);

            if (chessService.findGameIdByUser(whiteName, blackName) == NOT_EXIST_USER) {
                chessService.saveGame(whiteName, blackName, chessGame.getTurn());
            }
            int gameId = chessService.findGameIdByUser(whiteName, blackName);
            res.redirect("/start/" + gameId + "/init");
            return null;
        });

        get("/start/:id/:state", (req, res) -> {
            int gameId = Integer.parseInt(req.params(GAME_ID));
            if (!chessGame.isRunning() || req.params(STATE).equals(INIT_STATE)) {
                initGame(chessGame, gameId);
            }

            chessGame.loadBoard(chessService.findPieces(gameId));
            Map<String, Object> model = new LinkedHashMap<>(chessService.findPieces(gameId));
            model.put("id", gameId);

            return modelAndView(model, VIEW);
        }, new HandlebarsTemplateEngine());

        post("/move/:id", (req, res) -> {
            int gameId = Integer.parseInt(req.params(GAME_ID));
            movePiece(chessGame, req, gameId);

            if (chessGame.isFinished()) {
                chessService.deleteGame(gameId);
                return modelAndView(createScore(chessGame, new HashMap<>()), VIEW);
            }

            res.redirect("/start/" + gameId + "/running");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/status/:id", (req, res) -> {
            int gameId = Integer.parseInt(req.params(GAME_ID));
            Map<String, Object> model = new HashMap<>(chessGame.toBoardModel());;
            model.put("id", gameId);
            return modelAndView(createScore(chessGame, model), VIEW);
        }, new HandlebarsTemplateEngine());

        get("/end/:id", (req, res) -> {
            int gameId = Integer.parseInt(req.params(GAME_ID));
            chessGame.end();
            chessService.deleteGame(gameId);

            Map<String, Object> model = new HashMap<>(chessGame.toBoardModel());
            createScore(chessGame, model);
            return modelAndView(model, VIEW);
        }, new HandlebarsTemplateEngine());


        exception(Exception.class, (exception, request, response) -> {
            response.status(400);
            response.body(exception.getMessage() + ERROR_ANNOUNCE_MESSAGE);
        });
    }

    private void initGame(ChessGame chessGame, int gameId) {
        chessGame.start();
        String turn = chessService.findTurn(gameId);
        loadTurn(chessGame, turn);
        initBoard(chessGame, gameId, turn);
    }

    private void movePiece(ChessGame chessGame, Request req, int gameId) {
        String from = req.queryParams("from");
        String to = req.queryParams("to");

        Piece piece = chessGame.findPiece(from);
        chessGame.move(Positions.findPosition(from), Positions.findPosition(to));
        chessService.savePieces(List.of(new ChessDTO(piece.getColor(), piece.getPiece(), to)), gameId);
        chessService.deletePiece(from, gameId);
        chessService.updateTurn(gameId, chessGame.getTurn());
    }

    private Map<String, Object> createScore(ChessGame chessGame, Map<String, Object> model) {
        model.put(WHITE, chessGame.computeScore(Color.WHITE));
        model.put(BLACK, chessGame.computeScore(Color.BLACK));
        return model;
    }

    private void loadTurn(ChessGame chessGame, String turn) {
        if (!turn.equals(INIT_TURN) && !chessGame.isRightTurn(turn)) {
            chessGame.loadTurn();
        }
    }

    private void initBoard(ChessGame chessGame, int gameId, String turn) {
        if (turn.equals(INIT_TURN)) {
            chessService.initBoard(chessGame.toBoardModel(), gameId);
        }
    }
}
