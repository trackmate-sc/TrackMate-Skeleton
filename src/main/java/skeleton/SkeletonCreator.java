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

import java.util.ArrayList;

import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.labeling.ConnectedComponents;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;

public class SkeletonCreator<T extends RealType<T> & NativeType<T>> {

	final ImgLabeling< Integer, IntType > imgLabeling;
	private final OpService opService;

	private ArrayList<RandomAccessibleInterval<BitType>> skeletons;
	private int closingRadius = 0;

	public SkeletonCreator(ImgLabeling< Integer, IntType > imgLabeling, OpService opService) {
		this.imgLabeling = imgLabeling;
		this.opService = opService;
	}

	public void setClosingRadius(int closingRadius) {
		this.closingRadius = closingRadius;
	}

	public void run() {

		skeletons = new ArrayList<>();

		

		final RandomAccessibleInterval<BitType> skeletons = Algorithms.createObjectSkeletons(imgLabeling, closingRadius, 
				opService);
		this.skeletons.add(skeletons);
	}

	public ArrayList<RandomAccessibleInterval<BitType>> getSkeletons() {
		return skeletons;
	}

}
