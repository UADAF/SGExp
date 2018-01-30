package com.gt22.sgexp.coremod;

import gcewing.sg.SGBaseTE;

public class Methods {

	public static int getAcceleratedDialingTime(int defaultTime, SGBaseTE te) {
		return ClassTransformer.Companion.getAcceleratedDialingTime(defaultTime, te);
	}

	public static double getAcceleratedEnergyUse(double energy, SGBaseTE te) {
		return ClassTransformer.Companion.getAcceleratedEnergyUse(te, energy);
	}

}
