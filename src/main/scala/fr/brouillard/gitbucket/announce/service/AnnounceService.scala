package fr.brouillard.gitbucket.announce.service

import javax.mail.internet.{AddressException, InternetAddress}

import gitbucket.core.model.Profile.profile.blockingApi._
import gitbucket.core.model.Account
import gitbucket.core.model.Profile.{Accounts, GroupMembers}
import gitbucket.core.service.AccountService
import org.slf4j.LoggerFactory

object EmailAddress {
  // Uses InternetAddress, already used in GitBucket's mail-sending path (commons-email -> javax.mail).
  def isValid(email: String): Boolean =
    try {
      new InternetAddress(email).validate()
      true
    } catch {
      case _: AddressException => false
    }
}

trait AnnounceService {
  self: AccountService =>

  private val logger = LoggerFactory.getLogger(classOf[AnnounceService])

  def getAccountByGroupName(groupName: String)(implicit s: Session): List[Account] = {
    val needs = GroupMembers
      .filter(_.groupName === groupName.bind)
      .sortBy(_.userName)
      .map(_.userName)
      .list

    Accounts
      .filter(t => (t.userName inSetBind needs) && (t.removed === false.bind))
      .list
  }

  def getTargetAddresses(to: String)(implicit s: Session): List[String] = {
    to.split(",").map(_.trim).flatMap { groupAccount =>
      val userMailAddress: List[Account] = if(groupAccount.toUpperCase == "ALL"){
        getAllUsers(false)
      } else {
        getAccountByGroupName(groupAccount)
      }
      userMailAddress.collect { case x
        if !x.isGroupAccount && x.mailAddress.nonEmpty =>
          x.mailAddress :: getAccountExtraMailAddresses(x.userName)
      }
    }
    .flatten
    .distinct
    .partition(EmailAddress.isValid) match {
      case (validMails, invalidMails) =>
        if (invalidMails.nonEmpty) {
          logger.warn("skipping malformed email address(es): {}", invalidMails.mkString(", "))
        }
        validMails.toList
    }
  }
}