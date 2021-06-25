package nl.ritogames.networkhandler.wrapper.time

import java.time.OffsetDateTime
import java.time.temporal.Temporal

class OffsetDateTimeWrapper extends Time {
  override def now(): Temporal = OffsetDateTime.now()

  override def parse(str: String): Temporal = OffsetDateTime.parse(str)
}
