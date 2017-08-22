// Copyright: 2017 https://github.com/fommil/stalactite/graphs
// License: http://www.gnu.org/licenses/lgpl-3.0.en.html
package stalactite

import java.lang.String

import scala.Any
import scala.StringContext
import scala.collection.immutable.{ ::, List, Map, Nil }
import scala.Predef.wrapRefArray

import scala.annotation.{ compileTimeOnly, StaticAnnotation }
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

@compileTimeOnly("deriving annotation should have been removed")
class deriving extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any =
    macro DerivingMacros.generateImplicits
}

class DerivingMacros(val c: Context) {
  lazy val custom: Map[String, String] = c.settings
    .find(_.startsWith("stalactite="))
    .map { args =>
      args
        .substring(11)
        .split("\\|")
        .toList
        .filterNot(_.isEmpty)
        .map { setting =>
          val List(from, to) = setting.split("=").toList
          (from, to)
        }
        .toMap
    }
    .getOrElse(Map.empty)

  import c.universe._

  def generateImplicits(annottees: c.Expr[Any]*): c.Expr[Any] = {
    scala.Predef.println(s"settings = $custom")

    annottees.map(_.tree) match {
      case (data: ClassDef) :: Nil =>
        scala.Predef.println(s"DATA $data")
        c.Expr(q"""$data""")
      case (data: ClassDef) :: (companion: ModuleDef) :: Nil =>
        scala.Predef.println(s"DATA $data\nCOMPANION $companion")
        c.Expr(q"""$data
                   $companion""")
      /*      case other =>
        c.abort(
          c.enclosingPosition,
          "@deriving can only be applied to classes and sealed traits"
        )*/
    }

  }

}
