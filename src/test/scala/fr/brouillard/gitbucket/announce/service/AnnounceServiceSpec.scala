package fr.brouillard.gitbucket.announce.service

import org.scalatest.funsuite.AnyFunSuite

class AnnounceServiceSpec extends AnyFunSuite {

  test("accepts plain addresses") {
    assert(EmailAddress.isValid("user@example.com"))
    assert(EmailAddress.isValid("USER@EXAMPLE.COM"))
    assert(EmailAddress.isValid("user.name@sub.example.com"))
  }

  test("accepts addresses with an apostrophe in the local part") {
    assert(EmailAddress.isValid("o'brien@example.com"))
  }

  test("accepts other RFC 5322 atext characters in the local part") {
    assert(EmailAddress.isValid("user+tag@example.com"))
    assert(EmailAddress.isValid("user_name@example.com"))
    assert(EmailAddress.isValid("user-name@example.com"))
  }

  test("rejects addresses without an @") {
    assert(!EmailAddress.isValid("not-an-email"))
  }

  test("rejects addresses with spaces") {
    assert(!EmailAddress.isValid("user name@example.com"))
  }

  test("rejects empty string") {
    assert(!EmailAddress.isValid(""))
  }

  // RFC 5322 dot-atom-text = 1*atext *("." 1*atext) -- "." is not itself an
  // atext character, so it can only appear strictly between two non-empty
  // atext runs: never leading, trailing, or doubled.
  test("rejects a local part starting with a dot") {
    assert(!EmailAddress.isValid(".user@example.com"))
  }

  test("rejects a local part ending with a dot") {
    assert(!EmailAddress.isValid("user.@example.com"))
  }

  test("rejects a local part with consecutive dots") {
    assert(!EmailAddress.isValid("user..name@example.com"))
  }

  // RFC 5322's domain grammar (dot-atom) doesn't forbid a label ending in a
  // hyphen, even though such a label could never be a real DNS host name
  // (RFC 1034/1123). isValid only checks RFC 5322 syntax, so this is accepted
  // by design -- not something to "fix" by tightening the domain pattern.
  test("accepts a domain label ending in a hyphen (RFC 5322 permits it, even though DNS wouldn't)") {
    assert(EmailAddress.isValid("user@example-"))
  }
}
