/*-------------------------------------------------------------------------*
 * Louis Wong															   *
 * ZiftIt																   *
 * 7/31/2012															   *
 *-------------------------------------------------------------------------*/

package models
// definitions of classes and objects
trait PiMessage
case class Work(start: Int, numTerms: Int) extends PiMessage
case object Calculate extends PiMessage
case class PiResult(start: Int, numTerms: Int, result: Double)
