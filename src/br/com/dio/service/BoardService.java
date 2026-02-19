package br.com.dio.service;

import br.com.dio.model.Board;
import br.com.dio.model.GameStatusEnum;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardService {

    private final static int BOARD_LIMIT = 9;

    private final Board board;

    public BoardService(final Map<String, String> gameConfig) {
        this.board = new Board(initBoard(gameConfig));
    }

    public List<List<Space>> getSpaces(){
        return board.getSpaces();
    }

    public void reset(){
        board.reset();
    }

    public boolean hasErrors(){
        return board.hasErrors();
    }

    public GameStatusEnum getStatus(){
        return board.getStatus();
    }

    public boolean gameIsFinished(){
        return board.gameIsFinished();
    }


    private List<List<Space>> initBoard(final Map<String, String> gameConfig) {
        List<List<Space>> spaces = new ArrayList<>();

        for (int col = 0; col < BOARD_LIMIT; col++) {
            List<Space> column = new ArrayList<>();

            for (int row = 0; row < BOARD_LIMIT; row++) {
                String key = "%d,%d".formatted(col, row);
                String positionConfig = gameConfig.get(key);

                if (positionConfig == null) {
                    throw new IllegalArgumentException("Configuração ausente para a posição: " + key);
                }

                // valor vem no formato "4,false"
                String[] parts = positionConfig.trim().split("\\s*,\\s*", 2);
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Config inválida para " + key + ": " + positionConfig);
                }

                int expected = Integer.parseInt(parts[0]);
                boolean fixed = Boolean.parseBoolean(parts[1]);

                column.add(new Space(expected, fixed));
            }

            spaces.add(column);
        }

        if (spaces.size() != BOARD_LIMIT || spaces.get(0).size() != BOARD_LIMIT) {
            throw new IllegalStateException("Board mal inicializado: " +
                    "colunas=" + spaces.size() + ", col0=" + spaces.get(0).size());
        }

        return spaces;
    }
}