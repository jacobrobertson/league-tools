package com.jacobrobertson.leaguetools.util.riot;

public class Constants {

	private static final int COST_1_POOL_SIZE = 29;
	private static final int COST_2_POOL_SIZE = 22;
	private static final int COST_3_POOL_SIZE = 16;
	private static final int COST_4_POOL_SIZE = 12;
	private static final int COST_5_POOL_SIZE = 10;
	
	private static final int[] COST_POOL_SIZES = {
			COST_1_POOL_SIZE,
			COST_2_POOL_SIZE,
			COST_3_POOL_SIZE,
			COST_4_POOL_SIZE,
			COST_5_POOL_SIZE,
	};
	
	public static int getPoolSize(int cost) {
		return COST_POOL_SIZES[cost - 1];
	}
}
