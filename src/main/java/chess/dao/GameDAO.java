package chess.dao;

import chess.dto.GameDTO;
import chess.dto.GameIdDTO;
import chess.dto.TurnDTO;

public interface GameDAO {

    void saveGame(GameDTO gameDTO, TurnDTO turnDTO);

    GameIdDTO findGameIdByUser(GameDTO gameDTO);

    TurnDTO findTurn(GameIdDTO gameIdDTO);

    void updateTurn(GameIdDTO gameIdDTO, String turn);

    void deleteGame(GameIdDTO gameIdDTO);
}
