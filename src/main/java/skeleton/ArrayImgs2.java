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

import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.basictypeaccess.array.LongArray;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BitType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class ArrayImgs2 {
	final static public <T extends NativeType<T>> RandomAccessibleInterval<T> img(final long[] dim, final long[] offset,
			final T type) {
		return Views.translate(new ArrayImgFactory<>(type).create(dim), offset);
	}

	final static public <T extends NativeType<T>> RandomAccessibleInterval<T> img(final Interval interval,
			final T type) {
		return Views.translate(new ArrayImgFactory<>(type).create(Intervals.dimensionsAsLongArray(interval)),
				Intervals.minAsLongArray(interval));
	}
}
