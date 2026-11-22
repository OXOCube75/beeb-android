package org.beebplayer.android

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProgrammeLinkTest {
    private val view = "android.intent.action.VIEW"

    @Test fun programmeAndEpisodeLinksAreRead() {
        assertEquals(ProgrammeLink.Show("b006m86d"), ProgrammeLink.parse(view, "beeb", "programme", listOf("b006m86d")))
        assertEquals(ProgrammeLink.Episode("m002cgz3"), ProgrammeLink.parse(view, "beeb", "episode", listOf("m002cgz3")))
    }

    @Test fun anythingElseIsNotAProgrammeLink() {
        assertNull(ProgrammeLink.parse(view, "beeb", "live", listOf("bbc_one_london")))
        assertNull(ProgrammeLink.parse(view, "beeb", "programme", listOf("../evil00")))
        assertNull(ProgrammeLink.parse(view, "beeb", "programme", listOf("b006m86d", "extra")))
        assertNull(ProgrammeLink.parse(view, "beeb", "programme", emptyList()))
        assertNull(ProgrammeLink.parse(view, "https", "programme", listOf("b006m86d")))
        assertNull(ProgrammeLink.parse("android.intent.action.MAIN", "beeb", "programme", listOf("b006m86d")))
        // Vowels never appear in a BBC pid.
        assertNull(ProgrammeLink.parse(view, "beeb", "episode", listOf("aeiou123")))
    }
}
