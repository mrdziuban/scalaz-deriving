// Copyright: 2017 - 2018 Sam Halliday
// License: http://www.gnu.org/licenses/lgpl-3.0.en.html

package scalaz

import java.lang.String

import org.scalatest._

import examples.anyvals._
import examples.adt._
import examples.recadt._
import examples.recgadt._

import Scalaz._

class ContravariantSpec extends FlatSpec with NonImplicitAssertions {

  val bar: Foo = Bar("hello")
  val baz: Foo = Baz
  val faz: Foo = Faz(true, 1)

  "anyvals" should "behave as expected" in {
    assert(Thing("greetings") === Thing("greetings"))
    assert(Thing("greetings") /== Thing("blessings"))
  }

  "products" should "behave as expected" in {
    assert(Bar("hello") === Bar("hello"))

    assert(Bar("hello") /== Bar("goodbye"))
  }

  "coproducts" should "behave as expected" in {
    assert(bar === bar)
    assert(bar /== baz)
    assert(baz /== bar)
    assert(baz === baz)
    assert(bar /== faz)
    assert(baz /== faz)
    assert(faz === faz)
  }

  val leaf1        = Leaf("hello")
  val leaf2        = Leaf("goodbye")
  val branch       = Branch(leaf1, leaf2)
  val tree1: ATree = Branch(leaf1, branch)
  val tree2: ATree = Branch(leaf2, branch)

  "recursive products" should "behave as expected" in {
    assert(leaf1 === leaf1)
    assert(leaf2 === leaf2)
    assert(leaf1 /== leaf2)
  }

  "recursive coproducts" should "behave as expected" in {
    assert(tree1 === tree1)
    assert(tree1 /== tree2)
  }

  val gleaf1: GLeaf[String]    = GLeaf("hello")
  val gleaf2: GLeaf[String]    = GLeaf("goodbye")
  val gbranch: GBranch[String] = GBranch(gleaf1, gleaf2)
  val gtree1: GTree[String]    = GBranch(gleaf1, gbranch)
  val gtree2: GTree[String]    = GBranch(gleaf2, gbranch)

  "recursive GADT products" should "behave as expected" in {
    assert(gleaf1 === gleaf1)
    assert(gleaf2 === gleaf2)
    assert(gleaf1 /== gleaf2)
  }

  "recursive GADT coproducts" should "behave as expected" in {
    assert(gtree1 === gtree1)
    assert(gtree1 /== gtree2)
  }

}
