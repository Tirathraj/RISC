package com6441.team7.risc.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.view.CommandPromptView;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.api.model.MapService;
//import com6441.team7.risc.controller.StateContext;

/**
 * 
 * MapEditorTest class tests cases relevant with the controller components of the map editor.
 * @author Binsar Hutapea
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapEditorControllerTest {

	static private CommandPromptView testCmdView;
	static private MapService testMapService;
	static private MapLoaderController testMapLoader;
	static private GameController testGameController;
	String test_map;
	
	//define test params
	URI uri;
	String file, inputcommand, editorcommand1, editorcommand2, editorcommand3, editorcommand4, 
		editorcommand5, editorcommand6, editorcommand7, editorcommand8,	editorcommand9, 
		editorcommand10, editorcommand11, editorcommand12, editorcommand13, editorcommand14,
		editorcommand15, editorcommand16, editorcommand17, editorcommand18, editorcommand19, 
		editorcommand20;
	String[] editorcommands1, editorcommands2, editorcommands3, editorcommands4, editorcommands5,
		editorcommands6, editorcommands7, editorcommands8, editorcommands9, editorcommands10,
		editorcommands11, editorcommands12, editorcommands13, editorcommands14, editorcommands15,
		editorcommands16, editorcommands17, editorcommands18, editorcommands19, editorcommands20;
	int initcontinentsize, initcountrysize, expectedcontinentsize1, expectedcontinentsize2,
		expectedcontinentsize3, expectedcontinentsize4, expectedcontinentsize5
		, expectedcontinentsize6, expectedcountrysize1, expectedcountrysize2,
		expectedcountrysize3, expectedcountrysize4, expectedcountrysize5
		, expectedcountrysize6;
	Optional<String> inputmap;

	String message;
	@BeforeClass
	public static void beginClass() {
		testCmdView = new CommandPromptView(testMapLoader, testGameController);
//		testState = new StateContext();
		testMapService = new MapService();
		testMapLoader = new MapLoaderController(testMapService);
		testMapLoader.setView(testCmdView);
		
	}
	
	/**
	 * beginMethod() is called before every method is performed.
	 */
	@Before
	public void beginMethod() throws Exception{
		System.out.printf("==========%nBeginning of method%n==========%n");
		System.out.println(testMapLoader.getMapService().getContinentCountriesMap());
		System.out.println(testMapLoader.getMapService().getCountries());
		testMapLoader.getMapService().printNeighboringCountryInfo();
		System.out.println("Number of continents before test: "+testMapLoader.getMapService().getContinents().size());
		System.out.println("Number of countries before test: "+testMapLoader.getMapService().getCountries().size());
		/**
		 * URI variable uri is assigned URI parameter for reading file and executing editmap command
		 */
		URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI(); 
		/**
		 * file reads the file retrieved from the uri as string.
		 * it uses UTF-8 charsets.
		 */
		file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
		/**
		 * The followings are for test case 002 : executing editmap
		 */
		inputcommand = "editmap "+uri;
		inputmap = testMapLoader.editMap(inputcommand);
		//size of continent list before one continent is added
		/**
		 * The followings are for test cases on editing continent
		 * <li>initcontinentsize defines the size of continent before continent edit 
		 * operation is performed.
		 */
		initcontinentsize = testMapLoader.getMapService().getContinents().size();
		/**
		 * The followings are for the test cases of editing continent
		 * expectedcontinentsize is the expected size of continent list
		 * after edit operation
		 * <li>editorcommand contains the editcontinent commands.
		 * <li>editorcommands is an array that contains the split string for parameters
		 */
		expectedcontinentsize1 = initcontinentsize + 1;
		editorcommand1 = "editcontinent -add Nord_Asia 1";
		editorcommand1 = StringUtils.substringAfter(editorcommand1, "-");
		editorcommands1 = StringUtils.split(editorcommand1, "-");
		expectedcontinentsize2 = initcontinentsize + 2;
		editorcommand2 = "editcontinent -add Southeast_Asia 1 -add Northeast_Asia 1";
		editorcommand2 = StringUtils.substringAfter(editorcommand2, "-");
		editorcommands2 = StringUtils.split(editorcommand2, "-");
		expectedcontinentsize3 = initcontinentsize - 1;
		editorcommand3 = "editcontinent -remove ulstrailia";
		editorcommand3 = StringUtils.substringAfter(editorcommand3, "-");
		editorcommands3 = StringUtils.split(editorcommand3, "-");
		expectedcontinentsize4 = initcontinentsize - 2;
		editorcommand4 = "editcontinent -remove ameroki 1 -remove amerpoll 1";
		editorcommand4 = StringUtils.substringAfter(editorcommand4, "-");
		editorcommands4 = StringUtils.split(editorcommand4, "-");
		expectedcontinentsize5 = initcontinentsize;
		editorcommand5 = "editcontinent -add NordWest_Asia 9 -remove Southeast_Asia";
		editorcommand5 = StringUtils.substringAfter(editorcommand5, "-");
		editorcommands5 = StringUtils.split(editorcommand5, "-");
		expectedcontinentsize6 = initcontinentsize;
		editorcommand6 = "editcountry -add Sky_Republic Nord_Asia -add Ocean_Republic Northeast_Asia -remove vinenlant -remove heal";
		editorcommand6 = StringUtils.substringAfter(editorcommand6, "-");
		editorcommands6 = StringUtils.split(editorcommand6, "-");
		/**
		 * The followings are for the test cases of editing countries
		 * expectedcountrysize is the expected size of country list
		 * after edit operation
		 * <li>editorcommand contains the editcountry commands.
		 * <li>editorcommands is an array that contains the split string for parameters
		 */
		initcountrysize = testMapLoader.getMapService().getCountries().size();
		expectedcountrysize1 = initcountrysize+1;
		editorcommand7 = "editcountry -add Nordenstan Nord_Asia";
		editorcommand7 = StringUtils.substringAfter(editorcommand7, "-");
		editorcommands7 = StringUtils.split(editorcommand7, "-");
		expectedcountrysize2 = initcountrysize+2;
		editorcommand8 = "editcountry -add Nordennavic NordEast_Europe -add United_Islands Northeast_Asia";
		editorcommand8 = StringUtils.substringAfter(editorcommand8, "-");
		editorcommands8 = StringUtils.split(editorcommand8, "-");
		expectedcountrysize3 = initcountrysize-1;
		editorcommand9 = "editcountry -remove united_islands";
		editorcommand9 = StringUtils.substringAfter(editorcommand9, "-");
		editorcommands9 = StringUtils.split(editorcommand9, "-");
		expectedcountrysize4 = initcountrysize-2;
		editorcommand10 = "editcountry -remove maganar -remove pero";
		editorcommand10 = StringUtils.substringAfter(editorcommand10, "-");
		editorcommands10 = StringUtils.split(editorcommand10, "-");
		expectedcountrysize5 = initcountrysize;
		editorcommand11 = "editcountry -add Fiji azio -remove Nordenstan";
		editorcommand11 = StringUtils.substringAfter(editorcommand11, "-");
		editorcommands11 = StringUtils.split(editorcommand11, "-");
		expectedcountrysize6 = initcountrysize;
		editorcommand12 = "editcountry -add Sky_Republic Nord_Asia -add Ocean_Republic Northeast_Asia -remove vinenlant -remove heal";
		editorcommand12 = StringUtils.substringAfter(editorcommand12, "-");
		editorcommands12 = StringUtils.split(editorcommand12, "-");
	}
	
	/**
	 * endMethod() is called after every method is performed.
	 */
	@After
	public void endMethod() {
		System.out.printf("%n%n==========%nEnd of method%n==========%n");
		System.out.println(testMapLoader.getMapService().getContinentCountriesMap());
		System.out.println(testMapLoader.getMapService().getCountries());
		testMapLoader.getMapService().printNeighboringCountryInfo();
		System.out.println("Number of continents after test: "+testMapLoader.getMapService().getContinents().size());
		System.out.println("Number of countries after test: "+testMapLoader.getMapService().getCountries().size());
	}
	
	

	/**
	 * test1_readFile() tests command to load map from file.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test001_readFile() throws Exception{
		message = "The map is not valid";
		assertTrue(message, testMapLoader.parseFile(file));
	}
	
	/**
	 * test2_editMap() checks if the editmap command is valid.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test002_editMap() throws Exception{
		System.out.printf("Testing editmap command.%n");
		message = "Invalid command.";
		assertTrue(inputmap.isPresent());		
	}
	
	/**
	 * test3_addOneContinent() tests adding one continent to the continent list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test003_addOneContinent() throws Exception{
		System.out.printf("Adding one continent%n------------%n");
		System.out.println(editorcommand1);
		testMapLoader.editContinents(editorcommands1);
		assertSame(expectedcontinentsize1, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test4_addTwoContinents() tests adding two continents to the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test004_addTwoContinents() throws Exception{
		System.out.printf("Adding two continents%n------------%n");
		System.out.println(editorcommand2);
		testMapLoader.editContinents(editorcommands2);
		assertSame(expectedcontinentsize2, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test5_removeOneContinent() tests deleting one continent.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test005_removeOneContinent() throws Exception{
		System.out.printf("Removing one continent%n------------%n");
		System.out.println(editorcommand3);
		testMapLoader.editContinents(editorcommands3);
		assertSame(expectedcontinentsize3, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test6_removeTwoContinents() tests removing continents from the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test006_removeTwoContinents() throws Exception{
		System.out.printf("Removing two continents%n------------%n");
		System.out.println(editorcommand4);
		testMapLoader.editContinents(editorcommands4);
		assertSame(expectedcontinentsize4, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test7_addOneContinentRemoveOneContinent() tests adding and removing one continent from the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test007_addOneContinentRemoveOneContinent() throws Exception{
		System.out.printf("Adding and removing one continent%n------------%n");
		System.out.println(editorcommand5);
		testMapLoader.editContinents(editorcommands5);
		assertSame(expectedcontinentsize5, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test8_addTwoContinentsRemoveTwoContinents() tests adding and removing two continents from the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test008_addTwoContinentsRemoveTwosContinents() throws Exception{
		System.out.printf("Adding and removing two continents%n------------%n");
		System.out.println(editorcommand6);
		testMapLoader.editContinents(editorcommands6);
		assertSame(expectedcontinentsize6, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test9_addOneCountry() tests adding one country to the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test009_addOneCountry() throws Exception{
		System.out.printf("Adding one country%n------------%n");
		System.out.println(editorcommand7);
		testMapLoader.editCountries(editorcommands7);
		assertSame(expectedcountrysize1, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test10_addTwoCountries() tests adding one country to the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test010_addTwoCountries() throws Exception{
		System.out.printf("Adding two countries%n------------%n");
		String editorcommand = "editcountry -add Nordennavic NordEast_Europe -add United_Islands Northeast_Asia";
		System.out.println(editorcommand8);
		testMapLoader.editCountries(editorcommands8);
		assertSame(expectedcountrysize2, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test011_removeOneCountry() tests removing one country from the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test011_removeOneCountry() throws Exception{
		System.out.printf("Removing one country%n------------%n");
		//size of country list before one country is removed
		System.out.println(editorcommand9);
		testMapLoader.editCountries(editorcommands9);
		assertSame(expectedcountrysize3, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test012_removeTwoCountries() tests removing two countries from the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test012_removeTwoCountries() throws Exception{
		System.out.printf("Removing two countries%n------------%n");
		System.out.println(editorcommand10);
		testMapLoader.editCountries(editorcommands10);
		assertSame(expectedcountrysize4, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test013_addOneCountryRemoveOneCountry() tests adding and removing one country from the country list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test013_addOneCountryRemoveOneCountry() throws Exception{
		System.out.printf("Adding and removing one country%n------------%n");
		System.out.println(editorcommand11);
		testMapLoader.editCountries(editorcommands11);
		assertSame(expectedcountrysize5, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test014_addTwoCountriesRemoveTwoCountries() tests adding and removing two countries from the country list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test014_addTwoCountriesRemoveTwoCountries() throws Exception{
		System.out.printf("Adding and removing two countries%n------------%n");
		System.out.println(editorcommand12);
		testMapLoader.editCountries(editorcommands12);
		assertSame(expectedcountrysize6, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test015_addOneNeighbor() tests adding one neighbor of a country.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test015_addOneNeighbor() throws Exception{
		System.out.printf("Adding one neighbor to a country%n------------%n");
		//Set the command string to add one neighbor
		String editorcommand = "editneighbor -add nordennavic northern_utropa";
		System.out.println(editorcommand);
		//Retrieve substring(s) after every dash(es)
		editorcommand = StringUtils.substringAfter(editorcommand, "-");
		//Create an array of substrings for param.
		String[] editorcommands = StringUtils.split(editorcommand, "-");
		testMapLoader.editNeighbor(editorcommands);
		//create map object from adjacency list
		Map<Integer, Set<Integer>> borders = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get country ID
		Optional<Integer> countryid = testMapLoader.getMapService().findCorrespondingIdByCountryName("nordennavic");
		//get neighbor ID
		Optional<Integer> neighborid = testMapLoader.getMapService().findCorrespondingIdByCountryName("northern_utropa");
		//get pair of country and neighbor
		Set<Integer> pair = borders.get(countryid.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders.containsKey(countryid.get()));
		assertTrue("Neighbor country is not found", pair.contains(neighborid.get()));
	}
	
	/**
	 * test016_addTwoNeighbors() tests adding two neighbors for one or two countries in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test016_addTwoNeighbors() throws Exception{
		System.out.printf("Adding two neighbors to one country%n------------%n");
		//Set the command string to add two neighbors
		String editorcommand = "editneighbor -add fiji japan -add fiji western_united_states";
		System.out.println(editorcommand);
		//Retrieve substring(s) after every dash(es)
		editorcommand = StringUtils.substringAfter(editorcommand, "-");
		//Create an array of substrings for param.
		String[] editorcommands = StringUtils.split(editorcommand, "-");
		testMapLoader.editNeighbor(editorcommands);
		//create map object from adjacency list
		Map<Integer, Set<Integer>> borders = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get country ID
		Optional<Integer> countryid = testMapLoader.getMapService().findCorrespondingIdByCountryName("fiji");
		//get neighbor ID
		Optional<Integer> firstneighbor = testMapLoader.getMapService().findCorrespondingIdByCountryName("japan");
		Optional<Integer> secondneighbor = testMapLoader.getMapService().findCorrespondingIdByCountryName("western_united_states");
		//get pair of country and neighbor
		Set<Integer> pair = borders.get(countryid.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders.containsKey(countryid.get()));
		assertTrue("First neighbor country is not found", pair.contains(firstneighbor.get()));
		assertTrue("Second neighbor country is not found", pair.contains(secondneighbor.get()));
	}
	
	/**
	 * test017_removeOneNeighbor() tests removing one neighbor from a country.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test017_removeOneNeighbor() throws Exception{
		System.out.printf("Removing one neighbor from a country%n------------%n");
		//Set the command string to remove one neighbor
		String editorcommand = "editneighbor -remove tungu south_afrori";
		System.out.println(editorcommand);
		//Retrieve substring(s) after every dash(es)
		editorcommand = StringUtils.substringAfter(editorcommand, "-");
		//Create an array of substrings for param.
		String[] editorcommands = StringUtils.split(editorcommand, "-");
		testMapLoader.editNeighbor(editorcommands);
		//create map object from adjacency list
		Map<Integer, Set<Integer>> borders = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get country ID
		Optional<Integer> countryid = testMapLoader.getMapService().findCorrespondingIdByCountryName("tungu");
		//get neighbor ID
		Optional<Integer> neighborid = testMapLoader.getMapService().findCorrespondingIdByCountryName("south_afrori");
		//get pair of country and neighbor
		Set<Integer> pair = borders.get(countryid.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders.containsKey(countryid.get()));
		assertFalse("Neighbor country is found", pair.contains(neighborid.get()));
	}
	
	/**
	 * test018_removeTwoNeighbors() tests removing two neighbors from one or two countries in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test018_removeTwoNeighbors() throws Exception{
		System.out.printf("Removing two neighbors from one country%n------------%n");
		//Set the command string to remove two neighbors
		String editorcommand = "editneighbor -remove western_united_states great_britain -remove western_united_states teramar";
		System.out.println(editorcommand);
		//Retrieve substring(s) after every dash(es)
		editorcommand = StringUtils.substringAfter(editorcommand, "-");
		//Create an array of substrings for param.
		String[] editorcommands = StringUtils.split(editorcommand, "-");
		testMapLoader.editNeighbor(editorcommands);
		//create map object from adjacency list
		Map<Integer, Set<Integer>> borders = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get country ID
		Optional<Integer> countryid = testMapLoader.getMapService().findCorrespondingIdByCountryName("western_united_states");
		//get neighbor ID
		Optional<Integer> firstneighborid = testMapLoader.getMapService().findCorrespondingIdByCountryName("great_britain");
		Optional<Integer> secondneighborid = testMapLoader.getMapService().findCorrespondingIdByCountryName("teramar");
		//get pair of country and neighbor
		Set<Integer> pair = borders.get(countryid.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders.containsKey(countryid.get()));
		assertFalse("First neighbor country is found", pair.contains(firstneighborid.get()));
		assertFalse("Second neighbor country is found", pair.contains(secondneighborid.get()));
	}
	
	/**
	 * test019_addOneNeighborRemoveOneNeighbor() tests adding and removing one neighbor in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test019_addOneNeighborRemoveOneNeighbor() throws Exception{
		System.out.printf("Adding and removing one neighbor from one country%n------------%n");
		//Set the command string to remove two neighbors
		String editorcommand = "editneighbor -add western_united_states japan -remove western_united_states central_ameroki";
		System.out.println(editorcommand);
		//Retrieve substring(s) after every dash(es)
		editorcommand = StringUtils.substringAfter(editorcommand, "-");
		//Create an array of substrings for param.
		String[] editorcommands = StringUtils.split(editorcommand, "-");
		testMapLoader.editNeighbor(editorcommands);
		//create map object from adjacency list
		Map<Integer, Set<Integer>> borders = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get country ID
		Optional<Integer> countryid = testMapLoader.getMapService().findCorrespondingIdByCountryName("western_united_states");
		//get neighbor ID
		Optional<Integer> addedneighbor = testMapLoader.getMapService().findCorrespondingIdByCountryName("japan");
		Optional<Integer> removedneighbor = testMapLoader.getMapService().findCorrespondingIdByCountryName("central_ameroki");
		//get pair of country and neighbor
		Set<Integer> pair = borders.get(countryid.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders.containsKey(countryid.get()));
		assertTrue("First neighbor country is not found", pair.contains(addedneighbor.get()));
		assertFalse("Second neighbor country is found", pair.contains(removedneighbor.get()));
	}
	
	/**
	 * test020_addTwoNeighborsRemoveTwoNeighbors() tests adding and removing two neighbors in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test020_addTwoNeighborsRemoveTwoNeighbors() throws Exception{
		System.out.printf("Adding and removing two neighbors from one country%n------------%n");
		//Set the command string to remove two neighbors
		String editorcommand = "editneighbor -add senadlavin japan -add senadlavin argentina -remove senadlavin ireland -remove senadlavin western_united_states";
		System.out.println(editorcommand);
		//Retrieve substring(s) after every dash(es)
		editorcommand = StringUtils.substringAfter(editorcommand, "-");
		//Create an array of substrings for param.
		String[] editorcommands = StringUtils.split(editorcommand, "-");
		testMapLoader.editNeighbor(editorcommands);
		//create map object from adjacency list
		Map<Integer, Set<Integer>> borders = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get country ID
		Optional<Integer> countryid = testMapLoader.getMapService().findCorrespondingIdByCountryName("senadlavin");
		//get neighbor ID
		Optional<Integer> firstnewneighbor = testMapLoader.getMapService().findCorrespondingIdByCountryName("japan");
		Optional<Integer> secondnewneighbor = testMapLoader.getMapService().findCorrespondingIdByCountryName("argentina");
		Optional<Integer> firstremovedneighbor = testMapLoader.getMapService().findCorrespondingIdByCountryName("ireland");
		Optional<Integer> secondremovedneighbor = testMapLoader.getMapService().findCorrespondingIdByCountryName("western_united_states");
		//get pair of country and neighbor
		Set<Integer> pair = borders.get(countryid.get());
		System.out.println("Array is"+pair);
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders.containsKey(countryid.get()));
		assertTrue("First added neighbor country is not found", pair.contains(firstnewneighbor.get()));
		assertTrue("Second added neighbor country is not found", pair.contains(secondnewneighbor.get()));
		assertFalse("First removed neighbor country is found", pair.contains(firstremovedneighbor.get()));
		assertFalse("Second removed neighbor country is found", pair.contains(secondremovedneighbor.get()));
	}
	
	/**
	 * test021_validateMap() tests if map is valid.
	 */
	@Test
	public void test021_validateMap() {
		System.out.printf("%nTesting map validation%n");
		assertTrue("This map is invalid", testMapLoader.getMapService().isMapValid());
	}
	
	/**
	 * test022_checkGraphConnection() tests if map is a connected graph.
	 */
	@Test
	public void test022_checkGraphConnection() {
		System.out.printf("%nTesting if map is a connected graph%n");
		//returns true if map is a connected graph.
		assertTrue("This map is not a connected graph", testMapLoader.getMapService().isStronglyConnected());
	}
	
	/**
	 * test4_saveMap() tests if map can be saved.
	 */
	@Ignore
	@Test
	public void test024_saveMap() {
		System.out.printf("%nTesting map saving%n");
		try {
			testMapLoader.saveMap("savemap");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}