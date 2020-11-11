package aima.core.probability.example;

import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.util.RandVar;

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class BayesNetExampleFactory {
	public static BayesianNetwork construct2FairDiceNetwor() {
		FiniteNode dice1 = new FullCPTNode(ExampleRV.DICE_1_RV, new double[] {
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0 });
		FiniteNode dice2 = new FullCPTNode(ExampleRV.DICE_2_RV, new double[] {
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0 });

		return new BayesNet(dice1, dice2);
	}

	public static BayesianNetwork constructToothacheCavityCatchNetwork() {
		FiniteNode cavity = new FullCPTNode(ExampleRV.CAVITY_RV, new double[] {
				0.2, 0.8 });
		@SuppressWarnings("unused")
		FiniteNode toothache = new FullCPTNode(ExampleRV.TOOTHACHE_RV,
				new double[] {
						// C=true, T=true
						0.6,
						// C=true, T=false
						0.4,
						// C=false, T=true
						0.1,
						// C=false, T=false
						0.9

				}, cavity);
		@SuppressWarnings("unused")
		FiniteNode catchN = new FullCPTNode(ExampleRV.CATCH_RV, new double[] {
				// C=true, Catch=true
				0.9,
				// C=true, Catch=false
				0.1,
				// C=false, Catch=true
				0.2,
				// C=false, Catch=false
				0.8 }, cavity);

		return new BayesNet(cavity);
	}

	public static BayesianNetwork constructToothacheCavityCatchWeatherNetwork() {
		FiniteNode cavity = new FullCPTNode(ExampleRV.CAVITY_RV, new double[] {
				0.2, 0.8 });
		@SuppressWarnings("unused")
		FiniteNode toothache = new FullCPTNode(ExampleRV.TOOTHACHE_RV,
				new double[] {
						// C=true, T=true
						0.6,
						// C=true, T=false
						0.4,
						// C=false, T=true
						0.1,
						// C=false, T=false
						0.9

				}, cavity);
		@SuppressWarnings("unused")
		FiniteNode catchN = new FullCPTNode(ExampleRV.CATCH_RV, new double[] {
				// C=true, Catch=true
				0.9,
				// C=true, Catch=false
				0.1,
				// C=false, Catch=true
				0.2,
				// C=false, Catch=false
				0.8 }, cavity);
		FiniteNode weather = new FullCPTNode(ExampleRV.WEATHER_RV,
				new double[] {
						// sunny
						0.6,
						// rain
						0.1,
						// cloudy
						0.29,
						// snow
						0.01 });

		return new BayesNet(cavity, weather);
	}

	public static BayesianNetwork constructMeningitisStiffNeckNetwork() {
		FiniteNode meningitis = new FullCPTNode(ExampleRV.MENINGITIS_RV,
				new double[] { 1.0 / 50000.0, 1.0 - (1.0 / 50000.0) });
		@SuppressWarnings("unused")
		FiniteNode stiffneck = new FullCPTNode(ExampleRV.STIFF_NECK_RV,
				new double[] {
						// M=true, S=true
						0.7,
						// M=true, S=false
						0.3,
						// M=false, S=true
						0.009986199723994478,
						// M=false, S=false
						0.9900138002760055

				}, meningitis);
		return new BayesNet(meningitis);
	}

	public static BayesianNetwork constructBurglaryAlarmNetwork() {
		FiniteNode burglary = new FullCPTNode(ExampleRV.BURGLARY_RV,
				new double[] { 0.001, 0.999 });
		FiniteNode earthquake = new FullCPTNode(ExampleRV.EARTHQUAKE_RV,
				new double[] { 0.002, 0.998 });
		FiniteNode alarm = new FullCPTNode(ExampleRV.ALARM_RV, new double[] {
				// B=true, E=true, A=true
				0.95,
				// B=true, E=true, A=false
				0.05,
				// B=true, E=false, A=true
				0.94,
				// B=true, E=false, A=false
				0.06,
				// B=false, E=true, A=true
				0.29,
				// B=false, E=true, A=false
				0.71,
				// B=false, E=false, A=true
				0.001,
				// B=false, E=false, A=false
				0.999 }, burglary, earthquake);

		@SuppressWarnings("unused")
		FiniteNode johnCalls = new FullCPTNode(ExampleRV.JOHN_CALLS_RV,
				new double[] {
						// A=true, J=true
						0.90,
						// A=true, J=false
						0.10,
						// A=false, J=true
						0.05,
						// A=false, J=false
						0.95 }, alarm);
		@SuppressWarnings("unused")
		FiniteNode maryCalls = new FullCPTNode(ExampleRV.MARY_CALLS_RV,
				new double[] {
						// A=true, M=true
						0.70,
						// A=true, M=false
						0.30,
						// A=false, M=true
						0.01,
						// A=false, M=false
						0.99 }, alarm);


		return new BayesNet(burglary, earthquake);
	}


	public static BayesianNetwork constructCloudySprinklerRainWetGrassNetwork() {
		FiniteNode cloudy = new FullCPTNode(ExampleRV.CLOUDY_RV, new double[] {
				0.5, 0.5 });
		FiniteNode sprinkler = new FullCPTNode(ExampleRV.SPRINKLER_RV,
				new double[] {
						// Cloudy=true, Sprinkler=true
						0.1,
						// Cloudy=true, Sprinkler=false
						0.9,
						// Cloudy=false, Sprinkler=true
						0.5,
						// Cloudy=false, Sprinkler=false
						0.5 }, cloudy);
		FiniteNode rain = new FullCPTNode(ExampleRV.RAIN_RV, new double[] {
				// Cloudy=true, Rain=true
				0.8,
				// Cloudy=true, Rain=false
				0.2,
				// Cloudy=false, Rain=true
				0.2,
				// Cloudy=false, Rain=false
				0.8 }, cloudy);
		@SuppressWarnings("unused")
		FiniteNode wetGrass = new FullCPTNode(ExampleRV.WET_GRASS_RV,
				new double[] {
						// Sprinkler=true, Rain=true, WetGrass=true
						.99,
						// Sprinkler=true, Rain=true, WetGrass=false
						.01,
						// Sprinkler=true, Rain=false, WetGrass=true
						.9,
						// Sprinkler=true, Rain=false, WetGrass=false
						.1,
						// Sprinkler=false, Rain=true, WetGrass=true
						.9,
						// Sprinkler=false, Rain=true, WetGrass=false
						.1,
						// Sprinkler=false, Rain=false, WetGrass=true
						0.0,
						// Sprinkler=false, Rain=false, WetGrass=false
						1.0 }, sprinkler, rain);

		return new BayesNet(cloudy);
	}

	public static BayesianNetwork prova1() {
		FiniteNode cloudy = new FullCPTNode(ExampleRV.CLOUDY_RV, new double[] {
				0.5, 0.5 });
		FiniteNode cloudy2 = new FullCPTNode(ExampleRV.CLOUDY_RV2, new double[] {
				0.4, 0.6 });
		FiniteNode sprinkler = new FullCPTNode(ExampleRV.SPRINKLER_RV,
				new double[] {
						// Cloudy=true, Sprinkler=true
						0.1,
						// Cloudy=true, Sprinkler=false
						0.9,
						// Cloudy=false, Sprinkler=true
						0.5,
						// Cloudy=false, Sprinkler=false
						0.5 }, cloudy);
		FiniteNode rain = new FullCPTNode(ExampleRV.RAIN_RV, new double[] {
				// Cloudy=true, Rain=true
				0.8,
				// Cloudy=true, Rain=false
				0.2,
				// Cloudy=false, Rain=true
				0.2,
				// Cloudy=false, Rain=false
				0.8,
				// Cloudy=true, Rain=true
				0.8,
				// Cloudy=true, Rain=false
				0.2,
				// Cloudy=false, Rain=true
				0.2,
				// Cloudy=false, Rain=false
				0.8}, cloudy,cloudy2);
		@SuppressWarnings("unused")
		FiniteNode wetGrass = new FullCPTNode(ExampleRV.WET_GRASS_RV,
				new double[] {
						// Sprinkler=true, Rain=true, WetGrass=true
						.99,
						// Sprinkler=true, Rain=true, WetGrass=false
						.01,
						// Sprinkler=true, Rain=false, WetGrass=true
						.9,
						// Sprinkler=true, Rain=false, WetGrass=false
						.1,
						// Sprinkler=false, Rain=true, WetGrass=true
						.9,
						// Sprinkler=false, Rain=true, WetGrass=false
						.1,
						// Sprinkler=false, Rain=false, WetGrass=true
						0.0,
						// Sprinkler=false, Rain=false, WetGrass=false
						1.0 }, sprinkler, rain);

		return new BayesNet(cloudy,cloudy2);
	}

	public static BayesianNetwork constructBurglaryAlarmNetworkTornado() {
		FiniteNode burglary = new FullCPTNode(ExampleRV.BURGLARY_RV,
				new double[] { 0.001, 0.999 });
		FiniteNode earthquake = new FullCPTNode(ExampleRV.EARTHQUAKE_RV,
				new double[] { 0.002, 0.998 });
		FiniteNode tornado = new FullCPTNode(ExampleRV.TORNADO_RV,
				new double[] { 0.005, 0.995 });
		FiniteNode alarm = new FullCPTNode(ExampleRV.ALARM_RV, new double[] {
				// B=true, E=true, A=true
				0.95,
				// B=true, E=true, A=false
				0.05,
				// B=true, E=false, A=true
				0.94,
				// B=true, E=false, A=false
				0.06,
				// B=false, E=true, A=true
				0.29,
				// B=false, E=true, A=false
				0.71,
				// B=false, E=false, A=true
				0.001,
				// B=false, E=false, A=false
				0.999
			}, burglary, earthquake);

		@SuppressWarnings("unused")
		FiniteNode johnCalls = new FullCPTNode(ExampleRV.JOHN_CALLS_RV,
				new double[] {
						// A=true, J=true, Tornado=false
						0.90,
						// A=true, J=false Tornado=false
						0.10,
						// A=false, J=true Tornado=false
						0.05,
						// A=false, J=false Tornado=false
						0.95,
						// A=true, J=true, Tornado=true
						0.95,
						// A=true, J=false Tornado=true
						0.05,
						// A=false, J=true Tornado=false
						0.01,
						// A=false, J=false Tornado=false
						0.99

				}, alarm,tornado);
		@SuppressWarnings("unused")
		FiniteNode maryCalls = new FullCPTNode(ExampleRV.MARY_CALLS_RV,
				new double[] {
						// A=true, M=true
						0.70,
						// A=true, M=false
						0.30,
						// A=false, M=true
						0.01,
						// A=false, M=false
						0.99 }, alarm);
		FiniteNode sleep = new FullCPTNode(ExampleRV.SLEEP_RV,
				new double[] { 0.80, 0.20 });
		FiniteNode johnJuniorCalls = new FullCPTNode(ExampleRV.JOHN_JUNIOR_CALLS_RV,
				new double[] {
						// W=true, //J=true  JJ=true
						0.90,
						// W=true, //J=true  JJ=false
						0.10,
						// W=true,  //J=false  JJ=true
						0.80,
						// W=true, J=false //JJ=false
						0.20,
						// W=false, //J=true JJ=true
						0.70,
						// W=false, //J=true JJ=false
						0.30,
						// W=false,  //J=false JJ=true
						0.20,
						// W=false, //J=false JJ=false
						0.80,




				}, sleep, johnCalls);



		return new BayesNet(burglary, earthquake,tornado,sleep);
	}

	/*public static BayesianNetwork constructJailDomains() {
		FiniteNode kill = new FullCPTNode(ExampleRV.KILL_RV,
				new double[] { 0.001, 0.999 });

		FiniteNode steal = new FullCPTNode(ExampleRV.STEAL_RV,
				new double[] { 0.001, 0.999 });




		FiniteNode alarm = new FullCPTNode(ExampleRV.ALARM_RV, new double[] {
				// B=true, E=true, A=true
				0.95,
				// B=true, E=true, A=false
				0.05,
				// B=true, E=false, A=true
				0.94,
				// B=true, E=false, A=false
				0.06,
				// B=false, E=true, A=true
				0.29,
				// B=false, E=true, A=false
				0.71,
				// B=false, E=false, A=true
				0.001,
				// B=false, E=false, A=false
				0.999 }, burglary, earthquake);

		@SuppressWarnings("unused")
		FiniteNode johnCalls = new FullCPTNode(ExampleRV.JOHN_CALLS_RV,
				new double[] {
						// A=true, J=true, Tornado=false
						0.90,
						// A=true, J=false Tornado=false
						0.10,
						// A=false, J=true Tornado=false
						0.05,
						// A=false, J=false Tornado=false
						0.95,
						// A=true, J=true, Tornado=true
						0.95,
						// A=true, J=false Tornado=true
						0.05,
						// A=false, J=true Tornado=false
						0.01,
						// A=false, J=false Tornado=false
						0.99

				}, alarm,tornado);
		@SuppressWarnings("unused")
		FiniteNode maryCalls = new FullCPTNode(ExampleRV.MARY_CALLS_RV,
				new double[] {
						// A=true, M=true
						0.70,
						// A=true, M=false
						0.30,
						// A=false, M=true
						0.01,
						// A=false, M=false
						0.99 }, alarm);
		FiniteNode weed = new FullCPTNode(ExampleRV.WEED_RV,
				new double[] { 0.80, 0.20 });
		FiniteNode johnJuniorCalls = new FullCPTNode(ExampleRV.JOHN_JUNIOR_CALLS_RV,
				new double[] {
						// J=true, //W=true  JJ=true
						0.90,
						// J=true, //W=true  JJ=false
						0.10,
						// J=true,  //W=false  JJ=true
						0.80,
						// J=true, W=false //JJ=false
						0.20,
						// J=false, //W=true JJ=true
						0.70,
						// J=false, //W=true JJ=false
						0.30,
						// J=false,  //W=false JJ=true
						0.20,
						// J=false, //W=false JJ=false
						0.80,




				}, weed, johnCalls);



		return new BayesNet(burglary, earthquake,tornado,weed);
	} */

	public static BayesianNetwork constructFlySafeNetwork(){
		FiniteNode wind = new FullCPTNode(new RandVar("Wind",
				new BooleanDomain()),
				new double[] { 0.40, 0.60 });
		FiniteNode rain = new FullCPTNode(new RandVar("Rain",
				new BooleanDomain()),
				new double[] { 0.002, 0.998 });
		FiniteNode turbulence = new FullCPTNode(new RandVar("Turbulence",
				new BooleanDomain()),
				new double[] { 0.005, 0.995 });
		FiniteNode fly = new FullCPTNode(new RandVar("Fly",
				new BooleanDomain()), new double[] {
				// B=true, E=true, A=true
				0.95,
				// B=true, E=true, A=false
				0.05,
				// B=true, E=false, A=true
				0.94,
				// B=true, E=false, A=false
				0.06,
				// B=false, E=true, A=true
				0.29,
				// B=false, E=true, A=false
				0.71,
				// B=false, E=false, A=true
				0.001,
				// B=false, E=false, A=false
				0.999
		}, wind, rain);

		@SuppressWarnings("unused")
		FiniteNode panic = new FullCPTNode(new RandVar("Panic",
				new BooleanDomain()),
				new double[] {
						// A=true, J=true, Tornado=false
						0.90,
						// A=true, J=false Tornado=false
						0.10,
						// A=false, J=true Tornado=false
						0.05,
						// A=false, J=false Tornado=false
						0.95,
						// A=true, J=true, Tornado=true
						0.95,
						// A=true, J=false Tornado=true
						0.05,
						// A=false, J=true Tornado=false
						0.01,
						// A=false, J=false Tornado=false
						0.99

				}, turbulence,fly);
		@SuppressWarnings("unused")
		FiniteNode delay = new FullCPTNode(new RandVar("Delay",
				new BooleanDomain()),
				new double[] {
						// A=true, M=true
						0.001,
						// A=true, M=false
						0.999,
						// A=false, M=true
						0.99,
						// A=false, M=false
						0.01 }, fly);
		FiniteNode health = new FullCPTNode(new RandVar("Health",
				new BooleanDomain()),
				new double[] { 0.90, 0.10 });
		FiniteNode stroke = new FullCPTNode(new RandVar("Stroke",
				new BooleanDomain()),
				new double[] {
						// W=true, //J=true  JJ=true
						0.90,
						// W=true, //J=true  JJ=false
						0.10,
						// W=true,  //J=false  JJ=true
						0.80,
						// W=true, J=false //JJ=false
						0.20,
						// W=false, //J=true JJ=true
						0.70,
						// W=false, //J=true JJ=false
						0.30,
						// W=false,  //J=false JJ=true
						0.20,
						// W=false, //J=false JJ=false
						0.80,




				}, health, panic);
		FiniteNode death = new FullCPTNode(new RandVar("Death",
				new BooleanDomain()),
				new double[] {
						// W=true, //J=true  JJ=true
						0.90,
						// W=true, //J=true  JJ=false
						0.10,
						// W=true,  //J=false  JJ=true
						0.80,
						// W=true, J=false //JJ=false
						0.20,
				}, stroke);

		FiniteNode cry = new FullCPTNode(new RandVar("Cry",
				new BooleanDomain()),
				new double[] {
						// W=true, //J=true  JJ=true
						0.89,
						// W=true, //J=true  JJ=false
						0.11,
						// W=true,  //J=false  JJ=true
						0.80,
						// W=true, J=false //JJ=false
						0.20,
				}, death);
		FiniteNode funeral = new FullCPTNode(new RandVar("Funeral",
				new BooleanDomain()),
				new double[] {
						// W=true, //J=true  JJ=true
						0.89,
						// W=true, //J=true  JJ=false
						0.11,
						// W=true,  //J=false  JJ=true
						0.80,
						// W=true, J=false //JJ=false
						0.20,
				}, death);
		FiniteNode heredity = new FullCPTNode(new RandVar("Heredity",
				new BooleanDomain()),
				new double[] {
						// W=true, //J=true  JJ=true
						0.6,
						// W=true, //J=true  JJ=false
						0.4,
						// W=true,  //J=false  JJ=true
						0.80,
						// W=true, J=false //JJ=false
						0.20,
						// W=true, //J=true  JJ=true
						0.7,
						// W=true, //J=true  JJ=false
						0.3,
						// W=true,  //J=false  JJ=true
						0.80,
						// W=true, J=false //JJ=false
						0.20,
				},cry, funeral);
		FiniteNode happy = new FullCPTNode(new RandVar("Happy",
				new BooleanDomain()),
				new double[] {
						// W=true, //J=true  JJ=true
						0.9,
						// W=true, //J=true  JJ=false
						0.1,
						// W=true,  //J=false  JJ=true
						0.20,
						// W=true, J=false //JJ=false
						0.80,
				},heredity);
		FiniteNode optimist = new FullCPTNode(new RandVar("Optimism",
				new BooleanDomain()),
				new double[] {
						// W=true, //J=true  JJ=true
						0.9,
						// W=true, //J=true  JJ=false
						0.1,
						// W=true,  //J=false  JJ=true
						0.20,
						// W=true, J=false //JJ=false
						0.80,
				},happy);
		FiniteNode buy = new FullCPTNode(new RandVar("Buy",
				new BooleanDomain()),
				new double[] {
						// W=true, //J=true  JJ=true
						0.95,
						// W=true, //J=true  JJ=false
						0.05,
						// W=true,  //J=false  JJ=true
						0.20,
						// W=true, J=false //JJ=false
						0.80,
				},optimist);
		FiniteNode gamble = new FullCPTNode(new RandVar("Gamble",
				new BooleanDomain()),
				new double[] {
						// W=true, //J=true  JJ=true
						0.3,
						// W=true, //J=true  JJ=false
						0.7,
						// W=true,  //J=false  JJ=true
						0.20,
						// W=true, J=false //JJ=false
						0.80,
				},optimist);
		FiniteNode poor = new FullCPTNode(new RandVar("Poor",
				new BooleanDomain()),
				new double[] {
						// W=true, //J=true  JJ=true
						0.98,
						// W=true, //J=true  JJ=false
						0.02,
						// W=true,  //J=false  JJ=true
						0.30,
						// W=true, J=false //JJ=false
						0.70,
						// W=true, //J=true  JJ=true
						0.6,
						// W=true, //J=true  JJ=false
						0.4,
						// W=true,  //J=false  JJ=true
						0.30,
						// W=true, J=false //JJ=false
						0.70,
				},buy,gamble);


		return new BayesNet(wind, rain,turbulence,health);

	}

	public static BayesianNetwork constructSingleGetRichNetwork() {
		FiniteNode job = new FullCPTNode(ExampleRV.JOB_RV,
				new double[] {
						//doctor
						0.20,
						//programmer
						0.80

				});
		FiniteNode money = new FullCPTNode(ExampleRV.MONEY_RV,
				new double[] {
						//doctor 5000
						0.90,
						//docotr 4000
						0.10,
						//programmer 5000
						0.20,
						//programmer 4000
						0.80
				},job);
		FiniteNode investment = new FullCPTNode(ExampleRV.INVEST_RV,
				new double[] {
						//5000 good investment
						0.90,
						// 5000 bad investment
						0.10,
						//4000 good investment
						0.60,
						//4000 bad investment
						0.40
				}, money);
		FiniteNode rich = new FullCPTNode(ExampleRV.RICH_RV, new double[] {
				// Good investment and rich
				0.95,
				// Good investment and poor
				0.05,
				// Bad investment and rich
				0.20,
				// Bad investment and poor
				0.80

				 }, investment);





		return new BayesNet(job);
	}
}
