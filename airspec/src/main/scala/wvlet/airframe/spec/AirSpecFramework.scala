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
package wvlet.airframe.spec

import sbt.testing
import sbt.testing.{Fingerprint, SubclassFingerprint}
import wvlet.airframe.spec.runner.AirSpecRunner
import wvlet.log.LogSupport

/**
  * Include this class to your build.sbt:
  * testFrameworks += new TestFramework("wvlet.airframe.spec.AirSpecFramework")
  */
class AirSpecFramework extends sbt.testing.Framework with LogSupport {
  import AirSpecFramework._
  override def name(): String                     = "airspec"
  override def fingerprints(): Array[Fingerprint] = Array(AirSpecClassFingerPrint, AirSpecObjectFingerPrint)
  override def runner(args: Array[String], remoteArgs: Array[String], testClassLoader: ClassLoader): testing.Runner = {
    AirSpecRunner.newRunner(args, remoteArgs, testClassLoader)
  }

  // This method is necessary for Scala.js
  def slaveRunner(args: Array[String],
                  remoteArgs: Array[String],
                  testClassLoader: ClassLoader,
                  send: String => Unit): testing.Runner =
    runner(args, remoteArgs, testClassLoader)
}

object AirSpecFramework {

  object AirSpecClassFingerPrint extends SubclassFingerprint {
    override def isModule: Boolean                  = false
    override def superclassName(): String           = "wvlet.airframe.spec.spi.AirSpec"
    override def requireNoArgConstructor(): Boolean = true
  }

  object AirSpecObjectFingerPrint extends SubclassFingerprint {
    override def isModule: Boolean                  = true
    override def superclassName(): String           = "wvlet.airframe.spec.spi.AirSpec"
    override def requireNoArgConstructor(): Boolean = false
  }

}