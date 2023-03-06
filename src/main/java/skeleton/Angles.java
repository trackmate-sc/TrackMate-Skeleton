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
import net.imglib2.util.LinAlgHelpers;

import static java.lang.Math.acos;
import static java.lang.Math.atan;
import static java.lang.Math.toDegrees;

public abstract class Angles {
	public static double angle2DToCoordinateSystemsAxisInDegrees(RealPoint point) {
		final double[] vector = Vectors.asDoubles(point);

		return angle2DToCoordinateSystemsAxisInDegrees(vector);
	}

	public static double angle2DToCoordinateSystemsAxisInDegrees(double[] vector) {

		double angleToZAxisInDegrees;

		if (vector[Constants.Y] == 0) {
			angleToZAxisInDegrees = Math.signum(vector[Constants.X]) * 90;
		} else {
			angleToZAxisInDegrees = toDegrees(atan(vector[Constants.X] / vector[Constants.Y]));

			if (vector[Constants.Y] < 0) {
				angleToZAxisInDegrees += 180;
			}
		}

		return angleToZAxisInDegrees;
	}

	public static double angleOfSpindleAxisToXAxisInRadians(final double[] vector) {
		double[] xAxis = new double[] { 1, 0, 0 };

		double angleInRadians = Transforms.getAngle(vector, xAxis);

		return angleInRadians;
	}

}
