/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.core.audio;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * This class consists exclusively of static methods that operate on or return {@link AudioStream}s.
 *
 * @author Andreas Brenk - Initial contribution
 */
@NonNullByDefault
public class AudioStreams {

    private AudioStreams() {
    }

    public static AudioFormat getAudioFormat(InputStream audioStream) throws AudioException {
        try {
            return AudioStreams.getAudioFormat(AudioSystem.getAudioFileFormat(audioStream));
        } catch (UnsupportedAudioFileException | IOException e) {
            // throw new UnsupportedAudioStreamException("Cannot determine AudioFormat from InputStream",
            // audioStream.getClass(), e);
            throw new AudioException("Unable to determine AudioFormat from InputStream", e);
        }
    }

    // TODO rename determineAudioFormat
    public static AudioFormat getAudioFormat(AudioStream audioStream) {
        try {
            return AudioStreams.getAudioFormat(AudioSystem.getAudioFileFormat(audioStream));
        } catch (UnsupportedAudioFileException | IOException e) {
            return audioStream.getFormat();
        }
    }

    // TODO rename determineAudioFormat
    public static AudioFormat getAudioFormat(URLAudioStream audioStream) {
        try {
            return AudioStreams.getAudioFormat(AudioSystem.getAudioFileFormat(new URL(audioStream.getURL())));
        } catch (UnsupportedAudioFileException | IOException e) {
            return audioStream.getFormat();
        }
    }

    public static AudioFormat getAudioFormat(AudioFileFormat audioFileFormat) {
        final String container = audioFileFormat.getType().toString();
        final String codec = audioFileFormat.getFormat().getEncoding().toString();
        final boolean bigEndian = audioFileFormat.getFormat().isBigEndian();
        final int bitDepth = audioFileFormat.getFormat().getSampleSizeInBits();
        final int bitRate = Float.valueOf(audioFileFormat.getFormat().getFrameRate() * bitDepth).intValue();
        final long frequency = Float.valueOf(audioFileFormat.getFormat().getSampleRate()).longValue();

        return new AudioFormat(container, codec, bigEndian, bitDepth, bitRate, frequency);
    }
}
