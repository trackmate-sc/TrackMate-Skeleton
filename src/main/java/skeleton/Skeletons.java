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

import java.util.ArrayList;

import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.labeling.ConnectedComponents;
import net.imglib2.algorithm.morphology.table2d.Branchpoints;
import net.imglib2.algorithm.morphology.table2d.Endpoints;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.logic.BitType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class Skeletons {

	public static RandomAccessibleInterval<BitType> longestBranch(RandomAccessibleInterval<BitType> skeleton) {

		final RandomAccessibleInterval<BitType> longestBranch = Utils.copyAsArrayImg(skeleton);

		removeBranchpoints(longestBranch);

		Regions.onlyKeepLargestRegion(longestBranch, ConnectedComponents.StructuringElement.EIGHT_CONNECTED);

		return longestBranch;
	}

	public static ArrayList<Long> branchLengths(RandomAccessibleInterval<BitType> skeleton) {
		final RandomAccessibleInterval<BitType> branches = Utils.copyAsArrayImg(skeleton);

		removeBranchpoints(branches);

		final ArrayList<RegionAndSize> regions = Regions.getSizeSortedRegions(branches,
				ConnectedComponents.StructuringElement.EIGHT_CONNECTED);

		final ArrayList<Long> branchLengthPixels = new ArrayList<>();

		for (RegionAndSize regionAndSize : regions)
			branchLengthPixels.add(regionAndSize.getSize());

		return branchLengthPixels;
	}

	public static RandomAccessibleInterval<BitType> branchPoints(RandomAccessibleInterval<BitType> skeleton) {
		RandomAccessibleInterval<BitType> branchPoints = ArrayImgs.bits(Intervals.dimensionsAsLongArray(skeleton));

		Branchpoints.branchpoints(Views.extendBorder(Views.zeroMin(skeleton)), Views.flatIterable(branchPoints));

		Views.translate(branchPoints, Intervals.minAsLongArray(skeleton));

		return branchPoints;
	}

	public static RandomAccessibleInterval<BitType> endPoints(RandomAccessibleInterval<BitType> skeleton) {
		RandomAccessibleInterval<BitType> endPoints = ArrayImgs.bits(Intervals.dimensionsAsLongArray(skeleton));

		Endpoints.endpoints(Views.extendBorder(Views.zeroMin(skeleton)), Views.flatIterable(endPoints));

		Views.translate(endPoints, Intervals.minAsLongArray(skeleton));

		return endPoints;
	}

	static void removeBranchpoints(RandomAccessibleInterval<BitType> skeleton) {
		final RandomAccessibleInterval<BitType> branchpoints = branchPoints(skeleton);

		LoopBuilder.setImages(branchpoints, skeleton).forEachPixel((b, s) -> {
			if (b.get())
				s.set(false);
		});
	}

	public static RandomAccessibleInterval<BitType> skeleton(RandomAccessibleInterval<BitType> input,
			OpService opService) {
		final RandomAccessibleInterval<BitType> thin = Utils.createEmptyCopy(input);
		opService.morphology().thinGuoHall(thin, input);
		return thin;
	}

}
