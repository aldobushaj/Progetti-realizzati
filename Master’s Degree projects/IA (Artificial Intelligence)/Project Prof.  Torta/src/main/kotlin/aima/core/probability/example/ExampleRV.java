package aima.core.probability.example;

import aima.core.probability.domain.ArbitraryTokenDomain;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.domain.FiniteIntegerDomain;
import aima.core.probability.util.RandVar;

/**
 * Predefined example Random Variables from AIMA3e.
 * 
 * @author Ciaran O'Reilly
 */
public class ExampleRV {
	//
	public static final RandVar DICE_1_RV = new RandVar("Dice1",
			new FiniteIntegerDomain(1, 2, 3, 4, 5, 6));
	public static final RandVar DICE_2_RV = new RandVar("Dice2",
			new FiniteIntegerDomain(1, 2, 3, 4, 5, 6));
	//
	public static final RandVar TOOTHACHE_RV = new RandVar("Toothache",
			new BooleanDomain());
	public static final RandVar CAVITY_RV = new RandVar("Cavity",
			new BooleanDomain());
	public static final RandVar CATCH_RV = new RandVar("Catch",
			new BooleanDomain());
	//
	public static final RandVar WEATHER_RV = new RandVar("Weather",
			new ArbitraryTokenDomain("sunny", "rain", "cloudy", "snow"));
	//
	public static final RandVar MENINGITIS_RV = new RandVar("Meningitis",
			new BooleanDomain());
	public static final RandVar STIFF_NECK_RV = new RandVar("StiffNeck",
			new BooleanDomain());
	//
	public static final RandVar BURGLARY_RV = new RandVar("Burglary",
			new BooleanDomain());
	public static final RandVar EARTHQUAKE_RV = new RandVar("Earthquake",
			new BooleanDomain());
	public static final RandVar ALARM_RV = new RandVar("Alarm",
			new BooleanDomain());
	public static final RandVar JOHN_CALLS_RV = new RandVar("JohnCalls",
			new BooleanDomain());
	public static final RandVar MARY_CALLS_RV = new RandVar("MaryCalls",
			new BooleanDomain());
	public static final RandVar TORNADO_RV = new RandVar("Tornado",
			new BooleanDomain());
	public static final RandVar JOHN_JUNIOR_CALLS_RV = new RandVar("JohnJuniorCalls",
			new BooleanDomain());
	public static final RandVar SLEEP_RV = new RandVar("Sleep",
			new BooleanDomain());
	//
	public static final RandVar CLOUDY_RV = new RandVar("Cloudy",
			new BooleanDomain());
	public static final RandVar CLOUDY_RV2 = new RandVar("Cloudy2",
			new BooleanDomain());
	public static final RandVar SPRINKLER_RV = new RandVar("Sprinkler",
			new BooleanDomain());
	public static final RandVar RAIN_RV = new RandVar("Rain",
			new BooleanDomain());
	public static final RandVar WET_GRASS_RV = new RandVar("WetGrass",
			new BooleanDomain());
	//
	public static final RandVar RAIN_tm1_RV = new RandVar("Rain_t-1",
			new BooleanDomain());
	public static final RandVar RAIN_t_RV = new RandVar("Rain_t",
			new BooleanDomain());
	public static final RandVar UMBREALLA_t_RV = new RandVar("Umbrella_t",
			new BooleanDomain());


	/*
	* VARIABLES FOR GET RICH NETWORK
	* */
	public static final RandVar JOB_RV = new RandVar("Job",
			new BooleanDomain());
	public static final RandVar MONEY_RV = new RandVar("Money",
			new BooleanDomain());
	public static final RandVar INVEST_RV = new RandVar("Invest",
			new BooleanDomain());
	public static final RandVar RICH_RV = new RandVar("Rich",
			new BooleanDomain());

	/*
	 * VARIABLES FOR Jail Network
	 * */

	public static final RandVar KILL_RV = new RandVar("Kill",
			new ArbitraryTokenDomain("One","Many","No"));

	public static final RandVar STEAL_RV = new RandVar("Steal",
			new ArbitraryTokenDomain("Million","Thousand","No"));

	public static final RandVar JAIL_RV = new RandVar("Jail",
			new ArbitraryTokenDomain("Long","Middle","Short"));

	public static final RandVar MAD_RV = new RandVar("Mad",
			new ArbitraryTokenDomain("Yes","No"));

	public static final RandVar SANE_RV = new RandVar("Sane",
			new ArbitraryTokenDomain("Yes","No"));

	public static final RandVar FRIENDS_RV = new RandVar("Friends",
			new ArbitraryTokenDomain("Many","One","Nobody"));



}
