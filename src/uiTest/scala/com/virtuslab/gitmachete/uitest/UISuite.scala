package com.virtuslab.gitmachete.uitest

import org.virtuslab.ideprobe.dependencies.IntelliJVersion
import org.virtuslab.ideprobe.{IdeProbeFixture, IntelliJFixture}

trait UISuite extends RunningIntelliJPerSuite with IdeProbeFixture with RunningIntelliJFixtureExtension {

  lazy val intelliJVersion: IntelliJVersion = {
    val version = sys.props
      .get("ui-test.intellij.version")
      .filterNot(_.isEmpty)
      .getOrElse(throw new Exception("IntelliJ version is not provided"))
    // We're cheating here a bit since `version` might be either a build number or a release number,
    // while we're always treating it as a build number.
    // Still, as of ide-probe 0.26.0, even when release number like `2020.3` is passed as `build`, UI tests work just fine.
    IntelliJVersion(build = version, release = None)
  }

  override protected def baseFixture: IntelliJFixture = {
    fixtureFromConfig("ideprobe.conf").withVersion(intelliJVersion)
  }

  def waitAndCloseProject(): Unit = {
    intelliJ.probe.await()
    // Note that we shouldn't wait for a response here (so we shouldn't use org.virtuslab.ideprobe.ProbeDriver#closeProject),
    // since the response sometimes never comes (due to the project being closed), depending on the specific timing.
    intelliJ.ide.closeOpenedProjects()
  }

}