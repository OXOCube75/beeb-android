// SPDX-License-Identifier: GPL-3.0-or-later
package org.beebplayer.android

/**
 * Links other apps use to open a programme or an episode in Beeb.
 *
 *   beeb://programme/<pid>  opens the programme's page
 *   beeb://episode/<pid>    opens the programme's page with that episode's panel up
 *
 * The pid is the BBC's eight-character id, the same one iPlayer's own links carry. Anything
 * else is not a programme link and falls through to the existing handling.
 */
sealed interface ProgrammeLink {
    val pid: String
    data class Show(override val pid: String) : ProgrammeLink
    data class Episode(override val pid: String) : ProgrammeLink

    companion object {
        const val SCHEME = "beeb"
        private val PID = Regex("[b-df-hj-np-tv-z0-9]{8}")

        /** Pure, so it is unit-tested without Android: the intent's action, and its data's
         *  scheme, host and path segments. */
        fun parse(action: String?, scheme: String?, host: String?, segments: List<String>): ProgrammeLink? {
            if (action != "android.intent.action.VIEW" || scheme != SCHEME || segments.size != 1) return null
            val pid = segments.single().takeIf { PID.matches(it) } ?: return null
            return when (host) {
                "programme" -> Show(pid)
                "episode"   -> Episode(pid)
                else        -> null
            }
        }

        fun parse(intent: android.content.Intent): ProgrammeLink? {
            val uri = intent.data ?: return null
            return parse(intent.action, uri.scheme, uri.host, uri.pathSegments)
        }
    }
}
