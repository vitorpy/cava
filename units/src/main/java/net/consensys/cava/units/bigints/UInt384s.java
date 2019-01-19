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
package net.consensys.cava.units.bigints;

/** Static utility methods on UInt384 values. */
public final class UInt384s {
  private UInt384s() {}

  /**
   * Returns the maximum of two UInt384 values.
   *
   * @param v1 The first value.
   * @param v2 The second value.
   * @return The maximum of {@code v1} and {@code v2}.
   * @param <T> The concrete type of the two values.
   */
  public static <T extends UInt384Value<T>> T max(T v1, T v2) {
    return (v1.compareTo(v2)) >= 0 ? v1 : v2;
  }

  /**
   * Returns the minimum of two UInt384 values.
   *
   * @param v1 The first value.
   * @param v2 The second value.
   * @return The minimum of {@code v1} and {@code v2}.
   * @param <T> The concrete type of the two values.
   */
  public static <T extends UInt384Value<T>> T min(T v1, T v2) {
    return (v1.compareTo(v2)) < 0 ? v1 : v2;
  }
}
