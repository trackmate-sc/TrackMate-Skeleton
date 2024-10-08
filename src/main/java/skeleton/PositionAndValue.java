/*-
 * #%L
 * TrackMate detector based on Skeletonization.
 * %%
 * Copyright (C) 2022 - 2024 TrackMate developers.
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

public class PositionAndValue implements Comparable<PositionAndValue> {
	double[] position;

	public double[] getPosition() {
		return position;
	}

	public double getValue() {
		return value;
	}

	double value;

	public int compareTo(PositionAndValue positionAndValue) {

		double difference = this.value - positionAndValue.value;

		if (difference > 0)
			return 1;
		if (difference < 0)
			return -1;
		else
			return 0;

	}
}
