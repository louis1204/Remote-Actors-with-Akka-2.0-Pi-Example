/*-------------------------------------------------------------------------*
 * Louis Wong															   *
 * ZiftIt																   *
 * 7/31/2012															   *
 *-------------------------------------------------------------------------*/

package controllers

import play.api._
import play.api.mvc._

import models._
import views._

object Application extends Controller {

  def index = Action { // loads the homepage
    Ok(html.index())
  }

}