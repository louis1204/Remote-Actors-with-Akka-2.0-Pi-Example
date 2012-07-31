/*-------------------------------------------------------------------------*
 * Louis Wong															   *
 * ZiftIt																   *
 * 7/31/2012															   *
 *-------------------------------------------------------------------------*/

package models

import akka.actor._
import Actor._
import java.util.concurrent.CountDownLatch
import akka.routing._
import akka.dispatch.Future
import views._
import controllers._
import com.typesafe.config.ConfigFactory

class Master(numWorkers: Int, numElements: Int, numMessages: Int) extends Actor {

  var start: Long = _ // helps measure the calculation time

  def receive = {
    case (Calculate) => {
      System.out.println("Master Started!")

      val worker = context.actorFor("akka://RemoteCreation@10.0.100.255:2554/user/Worker") // this finds the worker that was already created in Pi
      for (i <- 0 until numWorkers) { // does the work i times
        System.out.println("Worker " + i)

        for (j <- 0 until numMessages) { // sends j messages to the worker
          System.out.println("Worker " + i + " Message " + j)
          worker ! Work(0, numElements) // send the message to the worker
        }
      }
      postStop // prints out the time it took to perform the task
    }

    case PiResult(start, numTerms, acc) => println("Pi Result: " + acc) // print out the result

    case _ => System.out.println("Something Weird Happened")
  }

  override def preStart() {
    System.out.println("Reached")
    start = System.currentTimeMillis
  }

  override def postStop() {
    println(
      "\n\tCalculation time: \t%s millis"
        .format(System.currentTimeMillis - start))
  }
}