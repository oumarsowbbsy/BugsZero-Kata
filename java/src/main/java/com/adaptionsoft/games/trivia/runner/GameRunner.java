
package com.adaptionsoft.games.trivia.runner;
import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;


public class GameRunner {

	private static boolean notAWinner;

	public static void playGame(Random rand) {
		Game game = initializeGame();
		playRounds(game, rand);
	}

	/**
	 * Initialise le jeu en ajoutant les joueurs.
	 * @return Retourne une instance du jeu initialisée.
	 */
	private static Game initializeGame() {
		Game game = new Game();
		game.add("Chet");
		game.add("Pat");
		game.add("Sue");
		return game;
	}

	/**
	 * Exécute les tours de jeu jusqu'à ce qu'un gagnant soit trouvé.
	 * @param game L'instance du jeu.
	 * @param rand L'instance de Random utilisée pour générer des valeurs aléatoires.
	 */
	private static void playRounds(Game game, Random rand) {
		boolean notAWinner;
		do {
			int rollValue = rand.nextInt(5) + 1;
			game.roll(rollValue);

			if (isTimeForWrongAnswer(rand)) {
				notAWinner = game.wrongAnswer();
			} else {
				notAWinner = game.wasCorrectlyAnswered();
			}
		} while (notAWinner);
	}

	/**
	 * Détermine aléatoirement si le prochain événement doit être une mauvaise réponse.
	 * @param rand L'instance de Random utilisée pour générer des valeurs aléatoires.
	 * @return Vrai si le moment est choisi pour une mauvaise réponse, faux sinon.
	 */
	private static boolean isTimeForWrongAnswer(Random rand) {
		return rand.nextInt(9) == 7;
	}

}
