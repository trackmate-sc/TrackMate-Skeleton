package skeleton;

/*
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

//package net.imglib2.realtransform;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.RealPositionable;
import net.imglib2.concatenate.Concatenable;
import net.imglib2.concatenate.PreConcatenable;
import net.imglib2.realtransform.AbstractTranslation;
import net.imglib2.realtransform.TranslationGet;

/**
 * 1-dimensional translation.
 *
 * @author Stephan Saalfeld, Christian Tischer
 */

// TODO: pull request!

public class Translation1D extends AbstractTranslation
		implements Concatenable<TranslationGet>, PreConcatenable<TranslationGet> {
	final static protected RealPoint[] constDs = new RealPoint[1];
	{
		constDs[0] = new RealPoint(1.0);
	}

	final protected Translation1D inverse;

	protected Translation1D(final double[] t, final Translation1D inverse) {
		super(t, constDs);

		assert t.length == numDimensions() : "Input dimensions do not match or are not 1.";

		this.inverse = inverse;
	}

	public Translation1D() {
		super(new double[1], constDs);
		inverse = new Translation1D(new double[1], this);
	}

	public Translation1D(final double tx) {
		this();
		set(tx);
	}

	public Translation1D(final double... t) {
		this();
		set(t);
	}

	public void set(final double tx) {
		t[0] = tx;

		inverse.t[0] = -tx;
	}

	/**
	 * Set the translation vector.
	 *
	 * @param t t.length &lt;= the number of dimensions of this
	 *          {@link net.imglib2.realtransform.Translation2D}
	 */
	@Override
	public void set(final double... t) {
		assert t.length <= 1 : "Too many inputs.";

		try {
			this.t[0] = t[0];

			inverse.t[0] = -t[0];
		} catch (final ArrayIndexOutOfBoundsException e) {
		}
	}

	@Override
	public void set(final double t, final int d) {
		assert d >= 0 && d < numDimensions() : "Dimensions index out of bounds.";

		this.t[d] = t;
		inverse.t[d] = -t;
	}

	@Override
	public void apply(final double[] source, final double[] target) {
		assert source.length >= numDimensions() && target.length >= numDimensions() : "Input dimensions too small.";

		target[0] = source[0] + t[0];
	}

	@Override
	public void apply(final float[] source, final float[] target) {
		assert source.length >= numDimensions() && target.length >= numDimensions() : "Input dimensions too small.";

		target[0] = (float) (source[0] + t[0]);
	}

	@Override
	public void apply(final RealLocalizable source, final RealPositionable target) {
		assert source.numDimensions() >= numDimensions() && target.numDimensions() >= numDimensions()
				: "Input dimensions too small.";

		target.setPosition(source.getDoublePosition(0) + t[0], 0);
	}

	@Override
	public void applyInverse(final double[] source, final double[] target) {
		assert source.length >= numDimensions() && target.length >= numDimensions() : "Input dimensions too small.";

		source[0] = target[0] - t[0];
		;
	}

	@Override
	public void applyInverse(final float[] source, final float[] target) {
		assert source.length >= numDimensions() && target.length >= numDimensions() : "Input dimensions too small.";

		source[0] = (float) (target[0] - t[0]);
	}

	@Override
	public void applyInverse(final RealPositionable source, final RealLocalizable target) {
		assert source.numDimensions() >= numDimensions() && target.numDimensions() >= numDimensions()
				: "Input dimensions too small.";

		source.setPosition(target.getDoublePosition(0) - t[0], 0);
	}

	@Override
	public Translation1D copy() {
		return new Translation1D(t);
	}

	@Override
	public double[] getRowPackedCopy() {
		final double[] matrix = new double[2];

		matrix[0] = 1;

		matrix[1] = t[0];

		return matrix;
	}

	@Override
	public Translation1D inverse() {
		return inverse;
	}

	@Override
	public Translation1D preConcatenate(final TranslationGet a) {
		set(t[0] + a.getTranslation(0));

		return this;
	}

	@Override
	public Class<TranslationGet> getPreConcatenableClass() {
		return TranslationGet.class;
	}

	@Override
	public Translation1D concatenate(final TranslationGet a) {
		return preConcatenate(a);
	}

	@Override
	public Class<TranslationGet> getConcatenableClass() {
		return TranslationGet.class;
	}

	@Override
	public String toString() {
		return "1d-translation: (" + t[0] + ")";
	}
}
