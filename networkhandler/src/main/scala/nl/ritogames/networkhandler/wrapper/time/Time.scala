package nl.ritogames.networkhandler.wrapper.time

import java.time.temporal.Temporal

trait Time {

  def now(): Temporal

  def parse(str:String) : Temporal

}
