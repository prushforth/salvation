package com.shapesecurity.salvation.directiveValues;

import com.shapesecurity.salvation.data.Base64Value;
import com.shapesecurity.salvation.interfaces.MatchesHash;
import com.shapesecurity.salvation.interfaces.Show;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HashSource implements SourceExpression, MatchesHash {
	@Nonnull
	private final HashAlgorithm algorithm;
	@Nonnull
	private final Base64Value value;

	public HashSource(@Nonnull HashAlgorithm algorithm, @Nonnull Base64Value value) {
		this.algorithm = algorithm;
		this.value = value;
	}

	public void validationErrors() {
		switch (this.algorithm) {
			case SHA256:
				if (this.value.size() != 32) {
					throw new IllegalArgumentException("Invalid SHA-256 value (wrong length): " + this.value.size() + ".");
				}
				break;
			case SHA384:
				if (this.value.size() != 48) {
					throw new IllegalArgumentException("Invalid SHA-384 value (wrong length): " + this.value.size() + ".");
				}
				break;
			case SHA512:
				if (this.value.size() != 64) {
					throw new IllegalArgumentException("Invalid SHA-512 value (wrong length): " + this.value.size() + ".");
				}
				break;
			default:
				throw new RuntimeException("Not reached.");
		}
	}

	@Nonnull
	@Override
	public String show() {
		return "'" + this.algorithm.show() + "-" + this.value.show() + "'";
	}


	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null || !(other instanceof HashSource)) {
			return false;
		}
		return this.algorithm.equals(((HashSource) other).algorithm) && this.value.equals(((HashSource) other).value);
	}

	@Override
	public int hashCode() {
		return (this.algorithm.hashCode() ^ 0xFE608B8F) ^ (this.value.hashCode() ^ 0x01D77E94);
	}

	public boolean matchesHash(@Nonnull HashAlgorithm algorithm, @Nonnull Base64Value value) {
		return this.algorithm == algorithm && this.value.equals(value);
	}


	public enum HashAlgorithm implements Show {
		SHA256("sha256"),
		SHA384("sha384"),
		SHA512("sha512");

		@Nonnull
		private final String value;

		HashAlgorithm(@Nonnull String value) {
			this.value = value;
		}

		@Nonnull
		@Override
		public String show() {
			return this.value;
		}
	}
}
