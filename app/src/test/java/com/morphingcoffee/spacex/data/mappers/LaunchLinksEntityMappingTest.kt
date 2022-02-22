package com.morphingcoffee.spacex.data.mappers

import com.morphingcoffee.spacex.data.local.model.LaunchLinksEntity
import com.morphingcoffee.spacex.data.local.model.toDomainModel
import com.morphingcoffee.spacex.domain.model.Links
import com.morphingcoffee.spacex.domain.model.PatchImage
import com.morphingcoffee.spacex.shared.fakes.FakeLaunchLinksEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class LaunchLinksEntityMappingTest {

    private lateinit var entity: LaunchLinksEntity

    @Before
    fun setUp() {
        entity = FakeLaunchLinksEntity
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `entity should correctly map to domain model`() {
        val subject = entity.toDomainModel()

        expectThat(subject).isA<Links>()
        expectThat(subject.articleURL).isEqualTo(entity.articleURL)
        expectThat(subject.wikiURL).isEqualTo(entity.wikiURL)
        expectThat(subject.webcastURL).isEqualTo(entity.youtubeURL)
        with(expectThat(subject.patchImage).isA<PatchImage>()) {
            get(PatchImage::smallURL).isEqualTo(entity.patchEntity!!.small)
            get(PatchImage::largeURL).isEqualTo(entity.patchEntity!!.large)
        }
    }

}