/*-
 * #%L
 * TrackMate detector based on Skeletonization.
 * %%
 * Copyright (C) 2022 TrackMate developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package skeleton;

import ij.gui.Plot;

import java.util.ArrayList;

public class Plots {
	public static void plot(double[] xValues, double[] yValues) {
		Plot plot = new Plot("title", "x", "y", xValues, yValues);
		plot.show();
	}

	public static void plot(ArrayList<Double> xValues, ArrayList<Double> yValues, String xLab, String yLab) {
		Plot plot = new Plot("", xLab, yLab, xValues.stream().mapToDouble(d -> d).toArray(),
				yValues.stream().mapToDouble(d -> d).toArray());
		plot.show();
	}

	public static void plot(CoordinatesAndValues cv, String xLab, String yLab) {
		Plot plot = new Plot("", xLab, yLab, cv.coordinates.stream().mapToDouble(d -> d).toArray(),
				cv.values.stream().mapToDouble(d -> d).toArray());

		plot.show();
	}

	public static void plot(CoordinateToValue cv, String xLab, String yLab) {
		Plot plot = new Plot("", xLab, yLab, cv.keySet().stream().mapToDouble(d -> d).toArray(),
				cv.values().stream().mapToDouble(d -> d).toArray());

		plot.show();
	}

}
