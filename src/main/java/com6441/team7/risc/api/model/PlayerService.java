package com6441.team7.risc.api.model;

import com6441.team7.risc.api.wrapperview.PlayerChangeWrapper;
import com6441.team7.risc.api.wrapperview.PlayerDominationWrapper;
import com6441.team7.risc.api.wrapperview.PlayerEditWrapper;
import com6441.team7.risc.api.wrapperview.ReinforcedArmyWrapper;

import java.util.*;

public class PlayerService extends Observable {

	/**
	 * a reference to mapService
	 */
    private MapService mapService;

    //to store newly added player to the list
    //private CircularFifoQueue<Player> playerList;
    
    //Keshav Refactoring part
    
    //Jenny I am continuing using arraylist because many functions use arraylist and it'il be tedious to refactor
    //It's gonna be hard to change everything.
    //Also we need to keep track of current player even for queue.
    //Very tedious to remove player from front and add to back of queue everytime we switch player
    //So let's stick with arraylist.
    
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
	 * constructor of playerService
	 * @param mapService
	 */
    public PlayerService(MapService mapService){

        this.mapService = mapService;
        //playerList = new CircularFifoQueue<>();
        
        this.listPlayers=new ArrayList<Player>();
        
        //Because no players added when PlayerService object is being instantiated in App.class
        this.currentPlayerIndex=-1;
        
    }

	/**
	 * get mapService
	 * @return the reference of mapService
	 */
	public MapService getMapService() {
        return mapService;
    }

	/**
	 * extends addObserver() from java
	 * @param observer
	 */
	@Override
    public void addObserver(Observer observer) {

        super.addObserver(observer);
    }

	/**
	 * set current Player, notify the observers when player has been changed
	 * @param num the index of player in PlayerList
	 */
	public void setCurrentPlayerIndex(int num) {
		this.currentPlayerIndex=num;
		
		Player currentPlayer=listPlayers.get(currentPlayerIndex);
		PlayerChangeWrapper playerChangeWrapper=new PlayerChangeWrapper(currentPlayer);
		
		setChanged();
		notifyObservers(playerChangeWrapper);
	}

	/**
	 * add a Player
	 * @param name of the player
	 * @return player
	 */
    public Player addPlayer(String name){
    	Player newPlayer=new Player(name);
    	listPlayers.add(newPlayer);
    	
		//Add Player to Wrapper function and send wrapper function to observers
		PlayerEditWrapper playerEditWrapper=new PlayerEditWrapper();
		playerEditWrapper.setAddedPlayer(newPlayer);
    	
    	setChanged();
        notifyObservers(playerEditWrapper);
        
    	return newPlayer;
    }

	/**
	 * remove the player by player name
	 * @param playerName
	 * @return true if player been removed successfully and notify the observers
	 * 		   false if player has not been removed successfully
	 */
	public boolean removePlayer(String playerName){
    	
		for(int i=0;i<listPlayers.size();i++) {
			
			if(listPlayers.get(i).getName().equals(playerName)) {
				
				Player removedPlayer=listPlayers.remove(i);
				
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
	 * return the list of players
	 * @return
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
	 * @param name
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
	 * @return player
	 */
	public Player getCurrentPlayer() {
    	
    	if(currentPlayerIndex<0) return null;
    	
    	return listPlayers.get(currentPlayerIndex);
    }


	/**
	 * get current player index
	 * @return int
	 */
	public int getCurrentPlayerIndex() {
    	
    	return currentPlayerIndex;
    }



	/**
	 * set current player
	 * @param player
	 */
    public void setCurrentPlayer(Player player){
        currentPlayer = player;
    }

   // public boolean isPlayerValid(){ return false; }

	/**
	 * check if the player exist
	 * @param playerName
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
	 * get total number of countries conquered by the player
	 * @param player
	 * @return
	 */
    public long getConqueredCountriesNumber(Player player){

        return mapService.getConqueredCountriesNumber(player);
    }



	/**
	 * get total number of reinforcedArmy if player has conquered the whole continents
	 * @param player
	 * @return
	 */
    public long getReinforcedArmyByConqueredContinents(Player player){
        return mapService.getReinforceArmyByConqueredContinents(player);
    }


	/**
	 * get countries names conquered by the player
	 * @param player
	 * @return
	 */
    public List<String> getConqueredContries(Player player){
        return mapService.getConqueredCountriesNameByPlayer(player);}




	/**
	 * reinforce army to the player of its country occupied
	 * @param player
	 * @param country
	 * @param armyNum
	 */
	public void reinforceArmy(Player player, String country, int armyNum){
		player.reinforceArmy(country, armyNum, mapService);
		ReinforcedArmyWrapper reinforcedArmyWrapper = new ReinforcedArmyWrapper(player, country, armyNum);
		setChanged();
		notifyObservers(reinforcedArmyWrapper);
	}


	/**
	 * show cards information of the player
	 * @param player
	 * @return
	 */
    public List<String> showCardsInfo(Player player){
        return player.getCardList();
    }



	/**
	 * check if the trade-in cards meet the trade-in condition
	 * @param player
	 * @param cardList
	 * @return
	 */
    public boolean isTradeInCardsValid(Player player, List<String> cardList){
        return player.meetTradeInCondition(cardList);
    }


	/**
	 * remove cards from cardList of the player
	 * @param player
	 * @param cardList
	 */
    public void removeCards(Player player, List<String> cardList){
        player.removeCards(cardList);
        notifyObservers(player);
    }

    
	/**
	 * check if Player is valid
	 * @return always return false?
	 */
	public boolean isPlayerValid(){ return false; }

    
    
	/**
	 * This method keeps track of the currentPlayerIndex and switches to the next player as soon as a player's
	 * turn is over.
	 *Uses Atomic Boolean boolFortificationPhaseOver to take decisions.
	 */
	public void switchNextPlayer() {


			if(currentPlayerIndex==listPlayers.size()-1) {
				this.setCurrentPlayerIndex(0);
			}

			else setCurrentPlayerIndex(this.currentPlayerIndex+1);

	}

	/**
	 * get the next player index.
	 * if next index points to the last element in list, return 0
	 * else return currentIndex + 1
	 * @return int
	 */
	public int getNextPlayerIndex() {
		
		if((currentPlayerIndex+1)<=listPlayers.size()-1) return currentPlayerIndex+1;
		
		else return 0;
	}

	/**
	 * get next Player
	 * @return player
	 */
	public Player getNextPlayer() {
		return listPlayers.get(getNextPlayerIndex());
	}


	/**
	 * Function that notifies all playerService observers that it has been changed and then sends an object to the observers
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
	 * @return map of <continent id, player name>, if any player owns the respective continent
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
	 *
	 */
	public void evaluateWorldDomination() {
		
		Map<Integer, String> continentOwnerMap=checkContinentOwners();
		
		int numCountries=mapService.getCountries().size();
		
		ArrayList<PlayerDominationWrapper> listPlayerDomination=new ArrayList<>();
		
		for(Player p: listPlayers) {
			
			String playerName=p.getName();
			
			int numPlayerCountries=p.getCountryList().size();
			
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
	 * caculate armies get from the player by the number of occupied countries
	 * @param player
	 * @return number of armies received
	 */
	public int calculateTotalPlayerArmies(Player player) {
		int counter=0;
		for(Country c:player.getCountryList()) counter+=c.getSoldiers().intValue();
		
		return counter;
	}
	
	/*
	public void fortifyCurrentPlayer(PlayerFortificationWrapper playerFortificationWrapper) {
		getCurrentPlayer().fortify(this, playerFortificationWrapper);
	}
	*/

}
