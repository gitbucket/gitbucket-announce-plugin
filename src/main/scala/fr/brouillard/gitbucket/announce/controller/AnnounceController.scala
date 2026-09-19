package fr.brouillard.gitbucket.announce.controller

import javax.mail.SendFailedException

import fr.brouillard.gitbucket.announce.html
import fr.brouillard.gitbucket.announce.service.AnnounceService
import gitbucket.core.controller.ControllerBase
import gitbucket.core.service.{AccountService, SystemSettingsService}
import gitbucket.core.util.{AdminAuthenticator, Mailer}
import io.github.gitbucket.markedj.{Marked, Options}
import org.scalatra.forms._
import org.apache.commons.mail.EmailException
import org.slf4j.LoggerFactory
import gitbucket.core.util.Implicits._

class AnnounceController extends AnnounceControllerBase
  with AnnounceService with AccountService with AdminAuthenticator

trait AnnounceControllerBase extends ControllerBase {
  self: AnnounceService with SystemSettingsService with AdminAuthenticator =>

  private val logger = LoggerFactory.getLogger(classOf[AnnounceController])

  case class AnnounceForm(content: String, subject: String, to: String)

  private val announceForm = mapping(
    "content" -> trim(label("Announce", text(required))),
    "subject" -> trim(label("Subject", text(required))),
    "to" -> trim(label("To", text(required)))
  )(AnnounceForm.apply)

  get("/admin/announce")(adminOnly {
    html.announce(flash.get("info"))
  })

  post("/admin/announce", announceForm)(adminOnly { form =>
    val systemSettings = context.settings

    if (systemSettings.useSMTP && systemSettings.smtp.nonEmpty) {
      if (logger.isDebugEnabled) {
        logger.debug("sending announce: {}", form.content)
        logger.debug("sending email subject: {}", form.subject)
        logger.debug("sending email content: {}", form.content)
      }

      val mailer = new Mailer(systemSettings)

      val opts = new Options()
      opts.setSanitize(true)
      val html = Marked.marked(form.content, opts)

      val bcc = getTargetAddresses(form.to)

      if (logger.isDebugEnabled) {
        logger.debug("sending email to: {}", form.to)
        logger.debug("sending email to EmailAddress: {}", bcc.mkString(", "))
      }

      try {
        mailer.sendBcc(bcc, form.subject, form.content, Some(html), context.loginAccount)
        flash.update("info", "Announce has been sent.")
      } catch {
        case t:  EmailException => {
          t.getCause match {
            case ex: SendFailedException => {
              logger.error("found invalid email address while sending notification", ex)
              val invalidAddresses = Option(ex.getInvalidAddresses()).map(_.toList).getOrElse(Nil)
              val unsentAddresses = Option(ex.getValidUnsentAddresses()).map(_.toList).getOrElse(Nil)
              invalidAddresses.foreach(ia => logger.error("invalid email address: {}", ia.toString()))
              unsentAddresses.foreach(ua => logger.error("email not sent to: {}", ua.toString()))
              flash.update("info",
                s"Announce has been sent, but ${invalidAddresses.size} address(es) were invalid and " +
                  s"${unsentAddresses.size} address(es) were not sent. See server log for details.")
            }
            case _ => {
              logger.error("failure sending email", t)
              flash.update("info", "Announce cannot be sent, verify log errors.")
            }
          }
        }
        case e: Exception => {
          logger.error("unexpected exception while sending email", e)
          flash.update("info", "Announce cannot be sent, verify log errors.")
        }
      }
    } else {
      flash.update("info", "Announce cannot be sent, verify SMTP settings")
    }

    redirect("/admin/announce")
  })
}