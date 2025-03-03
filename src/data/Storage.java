package data;

import java.io.Serializable;

/**
 * A record that stores a score value.
 * <p>
 * This record is immutable and implements {@link Serializable}, allowing it
 * to be saved and loaded from a file.
 * </p>
 *
 * @param score The score value to be stored.
 */
public record Storage(int score) implements Serializable {}
