/*-------------------------------------------------------------------------*
 * Louis Wong															   *
 * ZiftIt																   *
 * 7/31/2012															   *
 *-------------------------------------------------------------------------*/

package models

import akka.actor._
import controllers._
import views._

class Worker extends Actor {

  def receive = {
    case Work(start, numTerms) =>
      calculatePiFor(start, numTerms) // perform the work

    case _ => System.out.println("Something Weird Happened")
  }

  def calculatePiFor(start: Int, numTerms: Int): Double = {
    var acc = 0.0
    for (i <- start until (start + numTerms))
      acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
    sender ! PiResult(start, numTerms, acc) // send back to the master the start, number of terms and result
    acc
  }
}