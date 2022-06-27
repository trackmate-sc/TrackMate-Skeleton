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