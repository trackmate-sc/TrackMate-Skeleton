/*-
 * #%L
 * TrackMate detector based on Skeletonization.
 * %%
 * Copyright (C) 2022 - 2023 TrackMate developers.
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

import net.imglib2.Point;
import net.imglib2.RealPoint;

public abstract class Vectors {
	public static double[] asDoubles(RealPoint point) {
		final double[] vector = new double[point.numDimensions()];
		for (int d = 0; d < point.numDimensions(); d++) {
			vector[d] = point.getDoublePosition(d);
		}
		return vector;
	}
}
