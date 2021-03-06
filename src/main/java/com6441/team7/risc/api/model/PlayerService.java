package com6441.team7.risc.api.model;

import com6441.team7.risc.api.wrapperview.*;
import com6441.team7.risc.controller.TournamentController;
import com6441.team7.risc.utils.builder.AbstractPlayerServiceBuilder;
import com6441.team7.risc.utils.builder.ConcretePlayerServiceBuilder;

import java.util.*;

/**
 * Contains state of players
 * It is observed by different views and hence extends observable
 * @author Keshav
 *
 */
public class PlayerService extends Observable {

	/**
	 * the builder to build the data in PlayerService
	 */
	private final AbstractPlayerServiceBuilder builder;
	
	/**
	 * a reference to mapService
	 */
	private MapService mapService;

	/**
	 * List of players playing the game
	 */
	private ArrayList<Player> listPlayers;

	/**
	 * Keeps track of current player index to access current player from list of players.
	 * Used to switch to next player in list as well by incrementing index
	 */
	int currentPlayerIndex;


	/**
	 * the reference of current player
	 */
	private Player currentPlayer;

	/**
	 * Deck of cards implemented as stack
	 */
	private Stack<Card> deckCards;


	/**
	 * boolean value to check if the country conquered during the attacking phase
	 */
	private boolean countryConqueredDuringAttackPhase;

	/**
	 * the game command for the current game phase
	 */
	private String command;


	/**
	 * set the command
	 * @param command game command
	 */
	public void setCommand(String command){
		this.command = command;
	}

	/**
	 * return the command
	 * @return command name
	 */
	public String getCommand() {
		return command;
	}


	/**
	 * constructor of playerService
	 * @param mapService reference
	 */
	public PlayerService(MapService mapService){

		this.mapService = mapService;

		this.listPlayers=new ArrayList<Player>();

		initialiseDeckCards();

		//Because no players added when PlayerService object is being instantiated in App.class
		this.currentPlayerIndex=-1;
		
		this.boolTournamentMode=false;
		
		builder = new ConcretePlayerServiceBuilder();

	}	
	
	/**
	 * return the list of players
	 * @return list of players
	 */
	public ArrayList<Player> getPlayerList(){
		return listPlayers;
	}

	/**
	 * get current player name.
	 * if the playerIndex is valid, return current player name.
	 * if not, return empty string
	 * @return player name
	 */
	public String getCurrentPlayerName(){

		if(currentPlayerIndex<0) return "";

		Player currentPlayer=listPlayers.get(currentPlayerIndex);
		return currentPlayer.getName();
	}

	/**
	 * get Player by name.
	 * if the name does not exist, return null
	 * if the name exist, return the player
	 * @param name reference
	 * @return the player
	 */
	public Player getPlayerByName(String name) {

		for(Player p:listPlayers) {
			if(p.getName().equals(name)) return p;
		}

		return null;
	}

	/**
	 * get current player.
	 * if the playerIndex is less than 0, return null
	 * else return the player by index
	 * @return returns current player
	 */
	public Player getCurrentPlayer() {

		if(currentPlayerIndex<0) return null;

		return listPlayers.get(currentPlayerIndex);
	}


	/**
	 * get current player index
	 * @return returns current index of player
	 */
	public int getCurrentPlayerIndex() {
		
		return currentPlayerIndex;
	}
	
	
	/**
	 * get mapService
	 * @return the reference of mapService
	 */
	public MapService getMapService() {
		return mapService;
	}
	
	/**
	 * get the next player index.
	 * if next index points to the last element in list, return 0
	 * else return currentIndex + 1
	 * @return next player index
	 */
	public int getNextPlayerIndex() {

		if((currentPlayerIndex+1)<=listPlayers.size()-1) return currentPlayerIndex+1;

		else return 0;
	}

	/**
	 * get next Player
	 * @return next player
	 */
	public Player getNextPlayer() {
		return listPlayers.get(getNextPlayerIndex());
	}

	/**
	 * extends addObserver() from java
	 * @param observer Observer
	 */
	@Override
	public void addObserver(Observer observer) {

		super.addObserver(observer);
	}
	
	
	
	

	//--------------------------------------------DECK OF CARDS METHODS---------------------------------------

	
	
	/**
	 * Method initialised Deck of cards: 14 cards of each type
	 */
	public void initialiseDeckCards() {

		this.deckCards=new Stack();

		Card[] arrCard=new Card[] {Card.INFANTRY,Card.CAVALRY,Card.ARTILLERY};

		for(int j=0;j<3;j++) {
			for(int i=0;i<14;i++) {
				deckCards.push(arrCard[j]);
			}
		}

		//shuffle deck of cards thrice for randomisation
		shuffleDeckCards();
	}

	/**
	 * Method to shuffle deck
	 */
	public void shuffleDeckCards() {
		Collections.shuffle(deckCards);
		Collections.shuffle(deckCards);
		Collections.shuffle(deckCards);
	}

	/**
	 * Card is drawn from deck by just popping the stacl
	 * @return drawn card
	 */
	public Card drawFromDeck() {
		if(deckCards.isEmpty()) {
			notifyPlayerServiceObservers("No cards available on the deck right now.");
			return null;
		}
		return deckCards.pop();
	}

	/**
	 * Adds list of cards returned to deck and shuffles the deck
	 * @param cards list
	 */
	public void returnToDeck(List<Card> cards) {

		cards.forEach(card -> deckCards.push(card));
		shuffleDeckCards();
	}

	/**
	 * Pushes card returned to stack and shuffles the deck
	 * @param card returned
	 */
	public void returnToDeck(Card card){
		deckCards.push(card);
		shuffleDeckCards();
	}
	
	

//-----------------------------------------END OF DECK OF CARD METHODS------------------------------------------
	
	
	

//*****************************************STARTUP GAME CONTROLLER METHODS**************************************

	/**
	 * set current Player, notify the observers when player has been changed
	 * @param num the index of player in PlayerList
	 * NOTIFIES OBSERVERS OF CURRENT PLAYER BY SENDING PlayerChangeWrapper.class
	 */
	public void setCurrentPlayerIndex(int num) {
		this.currentPlayerIndex=num;
		
		currentPlayer=listPlayers.get(currentPlayerIndex);
		PlayerChangeWrapper playerChangeWrapper=new PlayerChangeWrapper(currentPlayer);

		setChanged();
		notifyObservers(playerChangeWrapper);
	}
	

	/**
	 * set current player
	 * @param player reference player
	 */
	public void setCurrentPlayer(Player player){
		currentPlayer = player;
	}

	/**
	 * check if the player exist
	 * @param playerName reference player name
	 * @return true if the player name exist, false if does not exist
	 */
	public boolean checkPlayerExistance(String playerName) {

		for(int i=0;i<listPlayers.size();i++) {
			if(listPlayers.get(i).getName().equals(playerName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * add a Player. Notifies players when player added
	 * @param type add different strategy type of player
	 * @param name of the player
	 * @return player added
	 * 
	 */
	public Player addPlayer(String name, String type){
		Player newPlayer=new Player(name);
		newPlayer.generatePlayerCategory(type);
		listPlayers.add(newPlayer);

		//Add Player to Wrapper function and send wrapper function to observers
		PlayerEditWrapper playerEditWrapper=new PlayerEditWrapper();
		playerEditWrapper.setAddedPlayer(newPlayer);

		setChanged();
		notifyObservers(playerEditWrapper);

		return newPlayer;
	}

	/**
	 * remove the player by player name. Notifies observers when player removed
	 * @param playerName reference player name
	 * @return true if player been removed successfully and notify the observers, false if player has not been removed successfully
	 * 
	 */
	public boolean removePlayer(String playerName){

		for(int i=0;i<listPlayers.size();i++) {

			if(listPlayers.get(i).getName().equals(playerName)) {

				Player removedPlayer=listPlayers.remove(i);

				if(currentPlayerIndex>i) {
					
					//setCurrentPlayerIndex((currentPlayerIndex-1));
					this.currentPlayerIndex=(currentPlayerIndex-1);
					
					currentPlayer=listPlayers.get(currentPlayerIndex);
					
					
				}
				
				//Add Player to Wrapper function and send wrapper function to observers
				PlayerEditWrapper playerEditWrapper=new PlayerEditWrapper();
				playerEditWrapper.setRemovedPlayer(removedPlayer);
				
				setChanged();
				//NOTIFY BEFORE RETURN
				notifyObservers(playerEditWrapper);
				
				return true;
			}
		}

		return false;
	}
	
	/**
	 * This method keeps track of the currentPlayerIndex and switches to the next player as soon as a player's
	 * turn is over.
	 * Uses Atomic Boolean boolFortificationPhaseOver to take decisions.
	 */
	public void switchNextPlayer() {	
		
		if(currentPlayerIndex>=listPlayers.size()-1) {
			this.setCurrentPlayerIndex(0);
			turnNum++;
		}

		else setCurrentPlayerIndex(this.currentPlayerIndex+1);

	}
	
	
	//---------------------------------Utils for autoplaying game------------------------------------------
	
	/**
	 * boolean for tournament mode
	 */
	private boolean boolTournamentMode;
	
	/**
	 * boolean for player if they are winner
	 */
	private boolean boolPlayerWinner;
	
	/**
	 * Player object 
	 */
	private Player winner;
	
	/**
	 * counter to keep track of games
	 */
	private int counterGame=0;
	
	/**
	 * counter to keep track of total turns
	 */
	private int numTurnsCombined=1000;
	
	/**
	 * tournament controller 
	 */
	private TournamentController tournamentController;
	
	/**
	 * turn at start. 
	 */
	private double turnNum=1;
	
	/**
	 * method to automate the game. 
	 */
	public void automateGame() {

			// 3 Random: 1268/3 turns until stack overflow when stack is 256Kb
			
			if(boolPlayerWinner) {
				
				//send winner
				//turnNum - 1 for result because winner checked at the start of next turn
				
				tournamentController.setResult(winner.getPlayerCategory().getName()+
						"("+winner.getName()+", numTurns: "+(turnNum-1)+")");
				return;
			}
			
			notifyPlayerServiceObservers("Turn: " + Math.ceil(turnNum));
				
				if(turnNum>numTurnsCombined) {
				 if(boolTournamentMode) {
					 //draw
					 tournamentController.setResult("DRAW");
					 return;
				 }				 
	
				 
			 notifyPlayerServiceObservers("Exited automated game as turn limit reached.");
			 System.exit(0);
			 }		

			// Does not affect tournament as no humans in tournament
			if (currentPlayer.getPlayerCategory() == PlayerCategory.HUMAN) {
				return;
			}

			else {

				switch (currentPlayer.getPlayerCategory()) {

				case RANDOM:
					currentPlayer.setStrategy(new RandomStrategy(this));
					break;
				case AGGRESSIVE:
					currentPlayer.setStrategy(new AggressiveStrategy(this));
					break;
				case BENEVOLENT:
					currentPlayer.setStrategy(new BenevolentStrategy(this));
					break;
				case CHEATER:
					currentPlayer.setStrategy(new CheaterStrategy(this));
					break;
				default:

				}

				currentPlayer.getStrategy().reinforce();
				//this.mapService.setState(GameState.ATTACK);
				currentPlayer.getStrategy().attack();
				currentPlayer.getStrategy().fortify();

			}
	}
	
	/**
	 * setter for numTurnsCombined
	 * @param turns total turns
	 */
	public void setNumTurns(int turns) {

		this.numTurnsCombined=turns;
		
		//reset numturns
		this.turnNum=1;
	}
	
	/**
	 * setter for counterGame
	 * @param counter move counter
	 */
	public void setMoveCounter(int counter) {
		this.counterGame=0;
	}
	
	/**
	 * setter for boolTournamentMode
	 * @param bool boolean value
	 */
	public void setBoolTournamentMode(boolean bool) {
		this.boolTournamentMode=true;
	}
	
	/**
	 * getter for boolTournamentMode
	 * @return returns boolean of boolTournamentMode
	 */
	public boolean getBoolTournamentMode() {
		return boolTournamentMode;
	}
	
	/**
	 * setter for tournamentController
	 * @param tournamentController to be set
	 */
	public void setTournamentController(TournamentController tournamentController) {
		this.tournamentController=tournamentController;
	}
	
	/**
	 * setter for boolPlayerWinner
	 * @param bool boolean value
	 */
	public void setBoolPlayerWinner(boolean bool) {
		this.boolPlayerWinner=bool;
	}
	
	/**
	 * getter for boolPlayerWinner
	 * @return returns boolean value
	 */
	public boolean getBoolPlayerWinner() {
		return boolPlayerWinner;
	}
	
	/**
	 * setter for winner
	 * @param player player as winner
	 */
	public void setPlayerWinner(Player player) {
		this.winner=player;
	}
	
	/**
	 * getter for winner
	 * @return returns winner player
	 */
	public Player getPlayerWinner() {
		return winner;
	}
	

	//------------------------------------REINFORCEMENT UTILS -----------------------------------------------
	
	/**
	 * get total number of countries conquered by the player
	 * @param player reference player
	 * @return player's conquered countries
	 */
	public long getConqueredCountriesNumber(Player player){

		return mapService.getConqueredCountriesNumber(player);
	}



	/**
	 * get total number of reinforcedArmy if player has conquered the whole continents
	 * @param player reference player
	 * @return player's army
	 */
	public long getReinforcedArmyByConqueredContinents(Player player){
		return mapService.getReinforceArmyByConqueredContinents(player);
	}


	/**
	 * get countries names conquered by the player
	 * @param player reference
	 * @return list of country names conquered by player
	 */
	public List<String> getConqueredContries(Player player){
		return mapService.getConqueredCountriesNameByPlayer(player);
		}




	/**
	 * reinforce army to the player of its country occupied
	 * @param player reference
	 * @param country name to be reinforced
	 * @param armyNum number of armies to be reinforced
	 */
	public void reinforceArmy(Player player, String country, int armyNum){
		player.reinforceArmy(country, armyNum, mapService);
		ReinforcedArmyWrapper reinforcedArmyWrapper = new ReinforcedArmyWrapper(player, country, armyNum);
		setChanged();
		notifyObservers(reinforcedArmyWrapper);
	}
	
	/**
	 * calculate armies get from the player by the number of occupied countries
	 * @param player reference
	 * @return number of armies received
	 */
	public int calculateTotalPlayerArmies(Player player) {
		int counter=0;
		for(Country c:player.getCountryPlayerList()) counter+=c.getSoldiers().intValue();

		return counter;
	}


	/**
	 * Show cards information of the player
	 * Sends notification to observers
	 * @param player passed as parameter
	 * 
	 */
	public void showCardsInfo(Player player){

		ReinforcedCardWrapper cardWrapper = new ReinforcedCardWrapper(player, player.getCardList());
		setChanged();
		notifyObservers(cardWrapper);
	}

	/**
	 * caculate number of reinforced armies after trading in cards
	 * after receiving reinforced army, it will notify cardExchangeView with ReinforceArmyAfterTradingCard wrapper
	 * to display current player and number of reinforced armies
	 * @param player reference player
	 * @return number of reinforced army
	 */
	public int calculateReinforcedArmyByTradingCards(Player player){
		int number = player.calculateReinforcedArmyByTradingCards();
		ReinforcedArmyAfterTradingCardWrapper wrapper = new ReinforcedArmyAfterTradingCardWrapper(player, number);
		setChanged();
		notifyObservers(wrapper);
		return number;
	}

	/**
	 * check if the trade-in cards meet the trade-in condition
	 * @param player reference player
	 * @param cardList reference list of cards
	 * @return player's card
	 */
	public boolean isTradeInCardsValid(Player player, List<Card> cardList){
		return player.meetTradeInCondition(cardList);
	}


	/**
	 * remove cards from cardList of the player
	 * @param player reference player
	 * @param cardList reference list of cards
	 */
	public void removeCards(Player player, List<Card> cardList){

		player.removeCards(cardList);
		returnToDeck(cardList);
		notifyObservers(player);
	}
	
	
	//****************************** NOTIFY OBSERVERS + WORLD DOMINATION ******************************************


	/**
	 * Function that notifies all playerService observers that it has been changed and then sends an object to
	 *  the observers
	 *  Wraps the setChanged and notify observers method so that they can be called by methods in game controllers
	 *  or by methods in other models (such as player.class) when the latter perform some actions and want to notify
	 *  observers.
	 * @param object that can be of different classes (different wrapper classes)
	 */
	public void notifyPlayerServiceObservers(Object object) {

		setChanged();
		notifyObservers(object);
	}

	/**
	 * This method checks if any player owns any continent.
	 * It loops through all countries in each continent and check if they have the same owner.
	 * Used for domination view and attack phase
	 * @return map of (continent id, player name), if any player owns the respective continent
	 */
	public Map<Integer, String> checkContinentOwners() {

		Map<Integer, Set<Integer>> continentCountriesMap = mapService.getContinentCountriesMap();

		Map<Integer, String> continentOwnerMap=new HashMap<>();

		//Loop through every continent's countries
		//check if owner is same for all countries of the continent

		for(Map.Entry<Integer, Set<Integer>> item :
				mapService.getContinentCountriesMap().entrySet()) {

			int key=(int) item.getKey();

			Optional<Continent> optionalContinent=mapService.getContinentById(key);
			Continent currentContinent= (Continent) optionalContinent.get();

			Set<Integer> value=item.getValue();

			//If continent empty, move to next continent
			if(value.size()==0) continue;

			//Only 1 country in continent: player of that country therefore owns continent
			if(value.size()==1) {

				int countryId=-1; //initialising variable

				for(Integer i:value) countryId=i;

				Optional<Country> optionalCountry=mapService.getCountryById(countryId);
				Country currentCountry=optionalCountry.get();
				String currentCountryOwnerName=currentCountry.getPlayer().getName();

				continentOwnerMap.put(key, currentCountryOwnerName);
				continue;
			}

			boolean boolSameOwner=true;

			String ownerName="";

			int counter=0;

			for(Integer i:value) {
				//For Each Country In Continent, Get owner
				Optional<Country> optionalCountry=mapService.getCountryById(i);

				Country currentCountry=optionalCountry.get();
				String currentCountryOwnerName=currentCountry.getPlayer().getName();

				//Set owner of first country as ownerName with which all other country owner names will be compared
				if(counter==0) {
					ownerName=currentCountryOwnerName;
					counter++;
					continue;
				}

				if(!currentCountryOwnerName.equals(ownerName)) {
					boolSameOwner=false;
					break;
				}

				counter++;
			}    //End of looping through all countries of 1 continent

			if(boolSameOwner) continentOwnerMap.put(key, ownerName);

		}

		return continentOwnerMap;

	}  //End of method


	/**
	 *Determines percentage controlled by every player, ownership of continents by every player,
	 *number of soldiers controller by every player and then notifies observers of playerservice.
	 *Notifies Domination view by sending list of players and their corresponding info using PlayerDominationWrapper
	 */
	public void evaluateWorldDomination() {

		//Check if any player owns any continents
		Map<Integer, String> continentOwnerMap=checkContinentOwners();

		int numCountries=mapService.getCountries().size();

		ArrayList<PlayerDominationWrapper> listPlayerDomination=new ArrayList<>();
		
		//Loop through every player, calculate percentage and other info
		for(Player p: listPlayers) {

			String playerName=p.getName();

			int numPlayerCountries=p.getCountryPlayerList().size();

			float percentageMap= (numPlayerCountries*100.0f) / numCountries;

			int numPlayerArmies=calculateTotalPlayerArmies(p);

			PlayerDominationWrapper playerDominationWrapper=new PlayerDominationWrapper(playerName,
					percentageMap, numPlayerArmies);

			//Check if player owns any continent and add to list

			for(Map.Entry<Integer, String> item: continentOwnerMap.entrySet()) {

				int key= (int) item.getKey();
				String strValue= item.getValue().toString();

				if(strValue.equals(playerName)) {
					String continentName=mapService.getContinentById(key).get().getName();
					playerDominationWrapper.addContinentNameToWrapperList(continentName);
				}

			}
			listPlayerDomination.add(playerDominationWrapper);
		}

		//NOTIFY TO OBSERVERS

		setChanged();
		notifyObservers(listPlayerDomination);

	}

	/**
	 * set list of players
	 * @param listPlayers the list of players
	 */
	public void setListPlayers(ArrayList<Player> listPlayers) {
		this.listPlayers = listPlayers;
	}

	/**
	 * Construct playerServiceEntity
	 * by making the builder create new PlayerServiceEntity,
	 * build list of players, build command, build current player,
	 * and build current player index.
	 */
	public void constructPlayerServiceEntity(){
		builder.createNewPlayerServiceEntity();
		builder.buildListPlayers(listPlayers);
		builder.buildCommand(command);
		builder.buildCurrentPlayer(currentPlayer);
		builder.buildCurrentPlayerIndex(currentPlayerIndex);
	}


	/**
	 * get player status entity to build playerStatusEntity
	 * @return playerStatusEntity
	 */
	public PlayerStatusEntity getPlayerStatusEntity(){
		constructPlayerServiceEntity();
		return builder.getPlayerStatusEntity();
	}
}