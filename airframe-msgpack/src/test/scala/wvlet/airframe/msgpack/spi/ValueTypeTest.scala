/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wvlet.airframe.msgpack.spi

import wvlet.airframe.msgpack.spi.Code._
import wvlet.airspec.AirSpec

/**
  *
  */
class ValueTypeTest extends AirSpec {
  scalaJsSupport

  def `have name`: Unit = {
    val vt = Seq(
      ValueType.NIL,
      ValueType.BOOLEAN,
      ValueType.INTEGER,
      ValueType.FLOAT,
      ValueType.STRING,
      ValueType.BINARY,
      ValueType.ARRAY,
      ValueType.MAP,
      ValueType.EXTENSION
    )
    for (v <- vt) {
      v.name shouldBe v.getClass.getSimpleName.replaceAll("\\$", "")
    }
  }

  def `lookup ValueType from a byte value`: Unit = {

    def check(b: Byte, tpe: ValueType): Unit = {
      MessageFormat.of(b).valueType shouldBe tpe
    }

    for (i <- 0 until 0x7f) {
      check(i.toByte, ValueType.INTEGER)
    }

    for (i <- 0x80 until 0x8f) {
      check(i.toByte, ValueType.MAP)
    }

    for (i <- 0x90 until 0x9f) {
      check(i.toByte, ValueType.ARRAY)
    }

    check(NIL, ValueType.NIL)

    MessageFormat.of(NEVER_USED).valueType == null shouldBe true

    check(TRUE, ValueType.BOOLEAN)
    check(FALSE, ValueType.BOOLEAN)

    for (t <- Seq(BIN8, BIN16, BIN32)) {
      check(t, ValueType.BINARY)
    }

    for (t <- Seq(FIXEXT1, FIXEXT2, FIXEXT4, FIXEXT8, FIXEXT16, EXT8, EXT16, EXT32)) {
      check(t, ValueType.EXTENSION)
    }

    for (t <- Seq(INT8, INT16, INT32, INT64, UINT8, UINT16, UINT32, UINT64)) {
      check(t, ValueType.INTEGER)
    }

    for (t <- Seq(STR8, STR16, STR32)) {
      check(t, ValueType.STRING)
    }

    for (t <- Seq(FLOAT32, FLOAT64)) {
      check(t, ValueType.FLOAT)
    }

    for (t <- Seq(ARRAY16, ARRAY32)) {
      check(t, ValueType.ARRAY)
    }

    for (i <- 0xe0 until 0xff) {
      check(i.toByte, ValueType.INTEGER)
    }
  }
}
