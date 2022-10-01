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

import net.imglib2.Interval;

import java.util.ArrayList;
import java.util.List;

public class Corners {
	public static final int MIN = 0;
	public static final int MAX = 1;
	public static int[] MIN_MAX = new int[] { MIN, MAX };

	public static long[] corner(int[] minMax, Interval interval) {
		assert minMax.length == interval.numDimensions();

		long[] corner = new long[minMax.length];

		for (int d = 0; d < corner.length; ++d) {
			if (minMax[d] == MIN) {
				corner[d] = interval.min(d);
			} else if (minMax[d] == MAX) {
				corner[d] = interval.max(d);
			}
		}

		return corner;
	}

	public static List<long[]> corners(Interval interval) {
		int[] minMaxArray = new int[interval.numDimensions()];
		ArrayList<long[]> corners = new ArrayList<>();
		setCorners(corners, interval, minMaxArray, -1);
		return corners;
	}

	public static void setCorners(ArrayList<long[]> corners, Interval interval, int[] minMaxArray, int d) {
		++d;

		for (int minMax : MIN_MAX) {
			minMaxArray[d] = minMax;

			if (d == minMaxArray.length - 1) {
				corners.add(corner(minMaxArray, interval));
			} else {
				setCorners(corners, interval, minMaxArray, d);
			}
		}

	}

}
