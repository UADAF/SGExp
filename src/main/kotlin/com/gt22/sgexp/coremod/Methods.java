package com.gt22.sgexp.coremod;

import gcewing.sg.SGBaseTE;

public class Methods {

	public static int getAcceleratedDialingTime(SGBaseTE te) {
		return ClassTransformer.Companion.getAcceleratedDialingTime(te);
	}

	public static double getAcceleratedEnergyUse(double energy, SGBaseTE te) {
		return ClassTransformer.Companion.getAcceleratedEnergyUse(te, energy);
	}

}
