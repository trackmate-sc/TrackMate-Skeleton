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

public class VectorUtils {
	static double[] getPerpendicularVector(double[] vector) {
		final double[] perpendicularVector = new double[3];
		if (vector[2] != 0) {
			perpendicularVector[1] = 1;
			perpendicularVector[2] = -vector[1] / vector[2];
		} else if (vector[1] != 0) {
			perpendicularVector[0] = 1;
			perpendicularVector[1] = -vector[0] / vector[1];
		} else if (vector[0] != 0) {
			perpendicularVector[2] = 1;
			perpendicularVector[0] = -vector[2] / vector[0];
		}

		return perpendicularVector;
	}
}
