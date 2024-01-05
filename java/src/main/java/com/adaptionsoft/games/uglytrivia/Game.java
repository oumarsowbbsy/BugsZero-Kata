package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class Game {
    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses  = new int[6];
    boolean[] inPenaltyBox  = new boolean[6];

	private LinkedList<String> popQuestions = new LinkedList<>();
	private LinkedList<String> scienceQuestions = new LinkedList<>();
	private LinkedList<String> sportsQuestions = new LinkedList<>();
	private LinkedList<String> rockQuestions = new LinkedList<>();

	private int currentPlayer = 0;
	private int numberOfQuestions = 50; // Nombre de questions par catégorie
	private boolean isGettingOutOfPenaltyBox;

	public Game() {
		initializeQuestions(numberOfQuestions);
	}

	/**
	 * Initialise les questions pour chaque catégorie.
	 * @param numberOfQuestions Le nombre de questions à générer pour chaque catégorie.
	 */
	private void initializeQuestions(int numberOfQuestions) {

		IntStream.range(0, numberOfQuestions).forEach(i -> {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast("Science Question " + i);
			sportsQuestions.addLast("Sports Question " + i);
			rockQuestions.addLast(createRockQuestion(i));
		});
	}

	/**
	 * Crée une question pour la catégorie "Rock".
	 *
	 * @param index L'index de la question dans la catégorie "Rock".
	 * @return La question générée pour la catégorie "Rock".
	 */
	public String createRockQuestion(int index) {
		return "Rock Question " + index;
	}


	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	/**
	 * Ajoute un nouveau joueur au jeu.
	 *
	 * Vérifie d'abord si le nom du joueur n'est ni null ni vide. Ensuite, ajoute le joueur à la liste des joueurs
	 * et initialise ses attributs (position, montant en monnaie du jeu, et statut dans la penalty box).
	 * Affiche des informations sur le joueur ajouté.
	 *
	 * @param playerName Le nom du joueur à ajouter.
	 * @return Vrai si le joueur a été ajouté avec succès, faux si le nom du joueur est invalide.
	 */
	public boolean add(String playerName) {
		if (playerName == null || playerName.trim().isEmpty()) {
			System.out.println("Player name cannot be null or empty.");
			return false;
		}

		int currentPlayerIndex = players.size();

		players.add(playerName);
		places[currentPlayerIndex] = 0;
		purses[currentPlayerIndex] = 0;
		inPenaltyBox[currentPlayerIndex] = false;

		System.out.println(playerName + " was added");
		System.out.println("They are player number " + players.size());
		return true;
	}


	public int howManyPlayers() {
		return players.size();
	}


	/**
	 * Effectue le lancer de dé pour le joueur actuel et détermine les actions suivantes.
	 * Si le joueur est dans la penalty box, vérifie s'il peut en sortir en fonction du résultat du dé.
	 * Si le joueur n'est pas dans la penalty box ou s'il sort de la penalty box, déplace le joueur et pose une question.
	 * @param roll Le résultat du lancer de dé.
	 */
	public void roll(int roll) {
		System.out.println(players.get(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);

		if (inPenaltyBox[currentPlayer]) {
			if (roll % 2 != 0) {
				System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
				isGettingOutOfPenaltyBox = true;
			} else {
				System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
				return; // Évite un mouvement inutile si le joueur reste dans la penalty box
			}
		}

		movePlayerAndAskQuestion(roll);
	}


	private void movePlayerAndAskQuestion(int roll) {
		places[currentPlayer] = places[currentPlayer] + roll;
		if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

		System.out.println(players.get(currentPlayer)
                + "'s new location is "
                + places[currentPlayer]);
		System.out.println("The category is " + currentCategory());
		askQuestion();
	}

	private void askQuestion() {
		String category = currentCategory();

		switch (category) {
			case "Pop":
				System.out.println(popQuestions.removeFirst());
				break;
			case "Science":
				System.out.println(scienceQuestions.removeFirst());
				break;
			case "Sports":
				System.out.println(sportsQuestions.removeFirst());
				break;
			case "Rock":
				System.out.println(rockQuestions.removeFirst());
				break;
		}
	}



	private String currentCategory() {
		switch (places[currentPlayer] % 4) {
			case 0: return "Pop";
			case 1: return "Science";
			case 2: return "Sports";
			default: return "Rock";
		}
	}


	public boolean wasCorrectlyAnswered() {
		boolean isCorrectAnswer = !inPenaltyBox[currentPlayer] || isGettingOutOfPenaltyBox;

		if (isCorrectAnswer) {
			System.out.println("Answer was correct!!!!");
			purses[currentPlayer]++;
			System.out.println(players.get(currentPlayer)
					+ " now has "
					+ purses[currentPlayer]
					+ " Gold Coins.");

			boolean winner = didPlayerWin();
			advanceToNextPlayer();
			return winner;
		} else {
			advanceToNextPlayer();
			return true;
		}
	}

	private void advanceToNextPlayer() {
		currentPlayer = (currentPlayer + 1) % players.size();
	}


	public boolean wrongAnswer(){
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer)+ " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;
		
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
		return true;
	}


	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}
}
