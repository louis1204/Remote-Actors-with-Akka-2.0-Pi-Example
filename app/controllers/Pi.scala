/*-------------------------------------------------------------------------*
 * Louis Wong															   *
 * ZiftIt																   *
 * 7/31/2012															   *
 *-------------------------------------------------------------------------*/

package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.mvc.Results.BadRequest
import views._
import models._
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }
import akka.routing.RoundRobinRouter

object Pi extends Controller {
  // Global Variables
  val RemoteSystem = ActorSystem("RemoteCreation", ConfigFactory.load.getConfig("remotecreation")) // Actor System
  val worker = RemoteSystem.actorOf(Props[Worker].withDispatcher("my-thread-pool-dispatcher").withRouter(RoundRobinRouter(5)), "Worker") // Worker Actor with custom dispatcher and router	

  // form for options
  val optionsForm: Form[Options] = Form(
    // Define a mapping that will handle User values
    mapping(
      "numWorkers" -> of[Int],
      "numElements" -> of[Int],
      "numMessages" -> of[Int])(Options.apply)(Options.unapply)) // The mapping signature doesn't match the User case class signature,
      // so we have to define custom binding/unbinding functions
/*      {
        // Binding: Create a Options from the mapping result			
        (numWorkers, numElements, NumMessages) => Options(numWorkers, numElements, NumMessages)
      } {
        // Unbinding: Create the mapping values from an existing Options value
        options => Some(options.numWorkers, options.numElements, options.numMessages)
      })
*/
  // defines the action after the user hitting the submit button
  def submit = Action { implicit request =>
    optionsForm.bindFromRequest.fold(
      // Form has errors, redisplay it
      errors => BadRequest(html.form(errors)),

      // We got a valid User value, display the summary
      options => {

        // create a master, passing in the values from the 
        val master = RemoteSystem.actorOf(Props(new Master(options.numWorkers.toInt, options.numElements.toInt, options.numMessages.toInt)))
        master ! (Calculate) // send the message to master to calculate
        Ok(html.form(this.optionsForm)) // reload the form
      })
  }

  /*
   * Display an empty form.
   */
  def form = Action {
    Ok(html.form(optionsForm));
  }
}