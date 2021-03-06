/*
 * Copyright 2018 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.consensys.cava.ssz;

import net.consensys.cava.bytes.Bytes;
import net.consensys.cava.units.bigints.UInt256;

import java.math.BigInteger;
import java.util.List;

/**
 * A writer for encoding values to SSZ.
 */
public interface SSZWriter {

  /**
   * Append an already SSZ encoded value.
   *
   * Note that this method <b>may not</b> validate that {@code value} is a valid SSZ sequence. Appending an invalid SSZ
   * sequence will cause the entire SSZ encoding produced by this writer to also be invalid.
   *
   * @param value The SSZ encoded bytes to append.
   */
  void writeSSZ(Bytes value);

  /**
   * Append an already SSZ encoded value.
   *
   * Note that this method <b>may not</b> validate that {@code value} is a valid SSZ sequence. Appending an invalid SSZ
   * sequence will cause the entire SSZ encoding produced by this writer to also be invalid.
   *
   * @param value The SSZ encoded bytes to append.
   */
  default void writeSSZ(byte[] value) {
    writeSSZ(Bytes.wrap(value));
  }

  /**
   * Encode a {@link Bytes} value to SSZ.
   *
   * @param value The byte array to encode.
   */
  default void writeBytes(Bytes value) {
    SSZ.encodeBytesTo(value, this::writeSSZ);
  }

  /**
   * Encode a byte array to SSZ.
   *
   * @param value The byte array to encode.
   */
  default void writeBytes(byte[] value) {
    SSZ.encodeByteArrayTo(value, this::writeSSZ);
  }

  /**
   * Write a string to the output.
   *
   * @param str The string to write.
   */
  default void writeString(String str) {
    SSZ.encodeStringTo(str, this::writeSSZ);
  }

  /**
   * Write a two's-compliment integer to the output.
   *
   * @param value The integer to write.
   * @param bitLength The bit length of the integer value.
   * @throws IllegalArgumentException If the value is too large for the specified bit length.
   */
  default void writeInt(int value, int bitLength) {
    writeSSZ(SSZ.encodeLongToByteArray(value, bitLength));
  }

  /**
   * Write a two's-compliment long to the output.
   *
   * @param value The long value to write.
   * @param bitLength The bit length of the integer value.
   * @throws IllegalArgumentException If the value is too large for the specified bit length.
   */
  default void writeLong(long value, int bitLength) {
    writeSSZ(SSZ.encodeLongToByteArray(value, bitLength));
  }

  /**
   * Write an unsigned big integer to the output.
   *
   * @param value The integer to write.
   * @param bitLength The bit length of the integer value.
   * @throws IllegalArgumentException If the value is too large for the specified bit length.
   * @throws IllegalArgumentException If the value is negative
   */
  default void writeUBigInteger(BigInteger value, int bitLength) {
    writeSSZ(SSZ.encodeUnsignedBigIntegerToByteArray(value, bitLength));
  }

  /**
   * Write a big integer to the output.
   *
   * @param value The integer to write.
   * @param bitLength The bit length of the integer value.
   * @throws IllegalArgumentException If the value is too large for the specified bit length.
   */
  default void writeBigInteger(BigInteger value, int bitLength) {
    writeSSZ(SSZ.encodeBigIntegerToByteArray(value, bitLength));
  }

  /**
   * Write an 8-bit two's-compliment integer to the output.
   *
   * @param value The integer to write.
   * @throws IllegalArgumentException If the value is too large for the specified bit length.
   */
  default void writeInt8(int value) {
    writeInt(value, 8);
  }

  /**
   * Write a 16-bit two's-compliment integer to the output.
   *
   * @param value The integer to write.
   * @throws IllegalArgumentException If the value is too large for the specified bit length.
   */
  default void writeInt16(int value) {
    writeInt(value, 16);
  }

  /**
   * Write a 32-bit two's-compliment integer to the output.
   *
   * @param value The integer to write.
   */
  default void writeInt32(int value) {
    writeInt(value, 32);
  }

  /**
   * Write a 64-bit two's-compliment integer to the output.
   *
   * @param value The long to write.
   */
  default void writeInt64(long value) {
    writeLong(value, 64);
  }

  /**
   * Write an unsigned integer to the output.
   *
   * @param value The integer to write.
   * @param bitLength The bit length of the integer value.
   * @throws IllegalArgumentException If the value is too large for the specified bit length.
   * @throws IllegalArgumentException If the value is negative
   */
  default void writeUInt(int value, int bitLength) {
    writeSSZ(SSZ.encodeULongToByteArray(value, bitLength));
  }

  /**
   * Write an unsigned long to the output.
   *
   * @param value The long value to write.
   * @param bitLength The bit length of the integer value.
   * @throws IllegalArgumentException If the value is too large for the specified bit length.
   * @throws IllegalArgumentException If the value is negative
   */
  default void writeULong(long value, int bitLength) {
    writeSSZ(SSZ.encodeULongToByteArray(value, bitLength));
  }

  /**
   * Write an 8-bit unsigned integer to the output.
   *
   * @param value The integer to write.
   * @throws IllegalArgumentException If the value is too large for the specified bit length.
   */
  default void writeUInt8(int value) {
    writeUInt(value, 8);
  }

  /**
   * Write a 16-bit unsigned integer to the output.
   *
   * @param value The integer to write.
   * @throws IllegalArgumentException If the value is too large for the specified bit length.
   */
  default void writeUInt16(int value) {
    writeUInt(value, 16);
  }

  /**
   * Write a 32-bit unsigned integer to the output.
   *
   * @param value The integer to write.
   */
  default void writeUInt32(long value) {
    writeULong(value, 32);
  }

  /**
   * Write a 64-bit unsigned integer to the output.
   *
   * @param value The long to write.
   */
  default void writeUInt64(long value) {
    writeULong(value, 64);
  }

  /**
   * Write a {@link UInt256} to the output.
   *
   * @param value The {@link UInt256} to write.
   */
  default void writeUInt256(UInt256 value) {
    writeSSZ(SSZ.encodeUInt256(value));
  }

  /**
   * Write a boolean to the output.
   *
   * @param value The boolean value.
   */
  default void writeBoolean(boolean value) {
    writeSSZ(SSZ.encodeBoolean(value));
  }

  /**
   * Write an address.
   *
   * @param address The address (must be exactly 20 bytes).
   * @throws IllegalArgumentException If {@code address.size != 20}.
   */
  default void writeAddress(Bytes address) {
    writeSSZ(SSZ.encodeAddress(address));
  }

  /**
   * Write a hash.
   *
   * @param hash The hash.
   */
  default void writeHash(Bytes hash) {
    writeSSZ(SSZ.encodeHash(hash));
  }

  /**
   * Write a list of bytes.
   *
   * @param elements The bytes to write as a list.
   */
  default void writeBytesList(Bytes... elements) {
    SSZ.encodeBytesListTo(elements, this::writeSSZ);
  }

  /**
   * Write a java.util.List of bytes.
   *
   * @param elements The bytes to write as a java.util.List.
   */
  default void writeBytesList(List<? extends Bytes> elements) {
    SSZ.encodeBytesListTo(elements, this::writeSSZ);
  }

  /**
   * Write a list of strings, which must be of the same length
   *
   * @param elements The strings to write as a list.
   */
  default void writeStringList(String... elements) {
    SSZ.encodeStringListTo(elements, this::writeSSZ);
  }

  /**
   * Write a java.util.List of strings, which must be of the same length
   *
   * @param elements The java.util.List of String elements to write.
   */
  default void writeStringList(List<String> elements) {
    SSZ.encodeStringListTo(elements, this::writeSSZ);
  }

  /**
   * Write a list of two's compliment integers.
   *
   * @param bitLength The bit length of the encoded integers (must be a multiple of 8).
   * @param elements The integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeIntList(int bitLength, int... elements) {
    SSZ.encodeIntListTo(bitLength, elements, this::writeSSZ);
  }

  /**
   * Write a java.util.List of two's compliment integers.
   *
   * @param bitLength The bit length of the encoded integers (must be a multiple of 8).
   * @param elements The java.util.List of Integer elements to write.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeIntList(int bitLength, List<Integer> elements) {
    SSZ.encodeIntListTo(bitLength, elements, this::writeSSZ);
  }

  /**
   * Write a list of two's compliment long integers.
   *
   * @param bitLength The bit length of the encoded integers (must be a multiple of 8).
   * @param elements The long integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeLongIntList(int bitLength, long... elements) {
    SSZ.encodeLongIntListTo(bitLength, elements, this::writeSSZ);
  }

  /**
   * Write a java.util.List of two's compliment long integers.
   *
   * @param bitLength The bit length of the encoded integers (must be a multiple of 8).
   * @param elements The java.util.List of Long elements to write.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeLongIntList(int bitLength, List<Long> elements) {
    SSZ.encodeLongIntListTo(bitLength, elements, this::writeSSZ);
  }

  /**
   * Write a list of big integers.
   *
   * @param bitLength The bit length of each integer.
   * @param elements The integers to write as a list.
   * @throws IllegalArgumentException if an integer cannot be stored in the number of bytes provided
   */
  default void writeBigIntegerList(int bitLength, BigInteger... elements) {
    SSZ.encodeBigIntegerListTo(bitLength, elements, this::writeSSZ);
  }

  /**
   * Write a java.util.List of big integers.
   *
   * @param bitLength The bit length of each integer.
   * @param elements The java.util.List of BigInteger elements to write.
   * @throws IllegalArgumentException if an integer cannot be stored in the number of bytes provided
   */
  default void writeBigIntegerList(int bitLength, List<BigInteger> elements) {
    SSZ.encodeBigIntegerListTo(bitLength, elements, this::writeSSZ);
  }

  /**
   * Write a list of 8-bit two's compliment integers.
   *
   * @param elements The integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeInt8List(int... elements) {
    writeIntList(8, elements);
  }

  /**
   * Write a list of 16-bit two's compliment integers.
   *
   * @param elements The integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeInt16List(int... elements) {
    writeIntList(16, elements);
  }

  /**
   * Write a list of 32-bit two's compliment integers.
   *
   * @param elements The integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeInt32List(int... elements) {
    writeIntList(32, elements);
  }

  /**
   * Write a list of 64-bit two's compliment integers.
   *
   * @param elements The integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeInt64List(int... elements) {
    writeIntList(64, elements);
  }

  /**
   * Write a list of unsigned integers.
   *
   * @param bitLength The bit length of the encoded integers (must be a multiple of 8).
   * @param elements The integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeUIntList(int bitLength, int... elements) {
    SSZ.encodeUIntListTo(bitLength, elements, this::writeSSZ);
  }

  /**
   * Write a java.util.List of unsigned integers.
   *
   * @param bitLength The bit length of the encoded integers (must be a multiple of 8).
   * @param elements The java.util.List of unsigned Integer elements to write.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeUIntList(int bitLength, List<Integer> elements) {
    SSZ.encodeUIntListTo(bitLength, elements, this::writeSSZ);
  }

  /**
   * Write a list of unsigned long integers.
   *
   * @param bitLength The bit length of the encoded integers (must be a multiple of 8).
   * @param elements The long integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeULongIntList(int bitLength, long... elements) {
    SSZ.encodeULongIntListTo(bitLength, elements, this::writeSSZ);
  }

  /**
   * Write a java.util.List of unsigned long integers.
   *
   * @param bitLength The bit length of the encoded integers (must be a multiple of 8).
   * @param elements The java.util.List of unsigned Long elements to write.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeULongIntList(int bitLength, List<Long> elements) {
    SSZ.encodeULongIntListTo(bitLength, elements, this::writeSSZ);
  }

  /**
   * Write a list of 8-bit unsigned integers.
   *
   * @param elements The integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeUInt8List(int... elements) {
    writeUIntList(8, elements);
  }

  /**
   * Write a list of 16-bit unsigned integers.
   *
   * @param elements The integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeUInt16List(int... elements) {
    writeUIntList(16, elements);
  }

  /**
   * Write a list of 32-bit unsigned integers.
   *
   * @param elements The integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeUInt32List(long... elements) {
    writeULongIntList(32, elements);
  }

  /**
   * Write a list of 64-bit unsigned integers.
   *
   * @param elements The integers to write as a list.
   * @throws IllegalArgumentException If any values are too large for the specified bit length.
   */
  default void writeUInt64List(long... elements) {
    writeULongIntList(64, elements);
  }

  /**
   * Write a list of unsigned 256-bit integers.
   *
   * @param elements The integers to write as a list.
   */
  default void writeUInt256List(UInt256... elements) {
    SSZ.encodeUInt256ListTo(elements, this::writeSSZ);
  }

  /**
   * Write a java.util.List of unsigned 256-bit integers.
   *
   * @param elements The java.util.List of UInt256 elements to write.
   */
  default void writeUInt256List(List<UInt256> elements) {
    SSZ.encodeUInt256ListTo(elements, this::writeSSZ);
  }

  /**
   * Write a list of hashes.
   *
   * @param elements The hashes to write as a list.
   */
  default void writeHashList(Bytes... elements) {
    SSZ.encodeHashListTo(elements, this::writeSSZ);
  }

  /**
   * Write a java.util.List of hashes.
   *
   * @param elements The java.util.List of Bytes (hash) elements to write.
   */
  default void writeHashList(List<? extends Bytes> elements) {
    SSZ.encodeHashListTo(elements, this::writeSSZ);
  }

  /**
   * Write a list of addresses.
   *
   * @param elements The addresses to write as a list.
   * @throws IllegalArgumentException If any {@code address.size != 20}.
   */
  default void writeAddressList(Bytes... elements) {
    SSZ.encodeAddressListTo(elements, this::writeSSZ);
  }

  /**
   * Write a java.util.List of addresses.
   *
   * @param elements The java.util.List of Bytes (address) elements to write.
   * @throws IllegalArgumentException If any {@code address.size != 20}.
   */
  default void writeAddressList(List<? extends Bytes> elements) {
    SSZ.encodeAddressListTo(elements, this::writeSSZ);
  }

  /**
   * Write a list of booleans.
   *
   * @param elements The booleans to write as a list.
   */
  default void writeBooleanList(boolean... elements) {
    SSZ.encodeBooleanListTo(elements, this::writeSSZ);
  }

  /**
   * Write a java.util.List of booleans.
   *
   * @param elements The java.util.List of Boolean elements to write.
   */
  default void writeBooleanList(List<Boolean> elements) {
    SSZ.encodeBooleanListTo(elements, this::writeSSZ);
  }
}
