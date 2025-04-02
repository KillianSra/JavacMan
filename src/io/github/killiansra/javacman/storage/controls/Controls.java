package io.github.killiansra.javacman.storage.controls;

import java.io.Serializable;

/**
 * A record that stores the control values.
 * <p>
 *     This record is immutable and implements {@link Serializable}, allowing it
 *     to be saved and loaded from a file.
 * </p>
 *
 * @param upCode The up code value to be stored.
 * @param downCode The down code value to be stored.
 * @param leftCode The left code value to be stored.
 * @param rightCode The right code value to be stored.
 * @param pauseCode The pause code value to be stored.
 */
public record Controls(int upCode, int downCode, int leftCode, int rightCode, int pauseCode) implements Serializable {}