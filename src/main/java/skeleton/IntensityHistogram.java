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

import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

public class IntensityHistogram<T extends RealType<T> & NativeType<T>> {
	public double[] binCenters;
	public double[] frequencies;
	final public double binWidth;
	final public int numBins;
	final RandomAccessibleInterval<T> rai;

	public IntensityHistogram(RandomAccessibleInterval<T> rai, double maxValue, double binWidth) {
		this.binWidth = binWidth;
		this.numBins = (int) (maxValue / binWidth);
		this.rai = rai;

		initializeHistogram(numBins, binWidth);
		computeFrequencies();
	}

	public void initializeHistogram(int numBins, double binWidth) {
		this.binCenters = new double[numBins];
		this.frequencies = new double[numBins];

		for (int i = 0; i < numBins; ++i) {
			binCenters[i] = i * binWidth + binWidth * 0.5;
		}
	}

	public CoordinateAndValue getMode() {
		final CoordinateAndValue coordinateAndValue = new CoordinateAndValue(0, 0);

		for (int i = 0; i < numBins - 1; ++i) // numBins - 1 avoids the last bin containing saturated pixels
		{
			if (frequencies[i] > coordinateAndValue.value) {
				coordinateAndValue.value = frequencies[i];
				coordinateAndValue.coordinate = binCenters[i];
			}
		}

		return coordinateAndValue;

	}

	public CoordinateAndValue getRightHandHalfMaximum() {
		final CoordinateAndValue maximum = getMode();

		final CoordinateAndValue coordinateAndValue = new CoordinateAndValue();

		for (int i = 0; i < numBins; ++i) {
			if (binCenters[i] > maximum.coordinate) {
				if (frequencies[i] <= maximum.value / 2.0) {
					coordinateAndValue.coordinate = binCenters[i];
					coordinateAndValue.value = frequencies[i];
					return coordinateAndValue;
				}
			}
		}

		return coordinateAndValue;

	}

	private void computeFrequencies() {
		final Cursor<T> cursor = Views.iterable(rai).cursor();

		while (cursor.hasNext()) {
			increment(cursor.next().getRealDouble());
		}
	}

	public void increment(double value) {
		int bin = (int) (value / binWidth);

		if (bin >= numBins) {
			bin = numBins - 1;
		}

		frequencies[bin]++;
	}

}
