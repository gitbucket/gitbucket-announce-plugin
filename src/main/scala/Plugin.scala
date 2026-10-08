import fr.brouillard.gitbucket.announce.controller.AnnounceController
import gitbucket.core.controller.Context
import gitbucket.core.plugin.{Link, PluginRegistry}
import io.github.gitbucket.solidbase.model.Version

class Plugin extends gitbucket.core.plugin.Plugin {
  override val pluginId: String = "announce"

  override val pluginName: String = "Announce Plugin"

  override val description: String = "Provide announces for gitbucket users and groups"

  override val versions: List[Version] = List(
    new Version("1.0.0")
    , new Version("1.1.0")
    , new Version("1.2.0")
    , new Version("1.3.0")
    , new Version("1.4.0")
    , new Version("1.5.0")
    , new Version("1.6.0")
    , new Version("1.7.0")
    , new Version("1.7.1")
    , new Version("1.7.2")
    , new Version("1.8.0")
    , new Version("1.9.0")
    , new Version("1.10.0")
    , new Version("1.11.0")
    , new Version("1.12.0")
    , new Version("1.13.0")
    , new Version("1.14.0")
    , new Version("1.15.0")
  )

  override val systemSettingMenus: Seq[(Context) => Option[Link]] = Seq(
    (ctx: Context) => Some(Link("announce", "Global Announce", "admin/announce"))
  )

  override val controllers = Seq(
    "/*" -> new AnnounceController()
  )
}
