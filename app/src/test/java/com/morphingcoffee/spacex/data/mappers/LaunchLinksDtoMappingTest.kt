package com.morphingcoffee.spacex.data.mappers

import com.morphingcoffee.spacex.data.local.model.LaunchLinksEntity
import com.morphingcoffee.spacex.data.local.model.LaunchPatchEntity
import com.morphingcoffee.spacex.data.remote.model.LaunchLinksDto
import com.morphingcoffee.spacex.data.remote.model.toDomainModel
import com.morphingcoffee.spacex.data.remote.model.toEntity
import com.morphingcoffee.spacex.domain.model.Links
import com.morphingcoffee.spacex.domain.model.PatchImage
import com.morphingcoffee.spacex.shared.fakes.FakeLaunchLinksDto
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class LaunchLinksDtoMappingTest {

    private lateinit var dto: LaunchLinksDto

    @Before
    fun setUp() {
        dto = FakeLaunchLinksDto
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `dto should correctly map to domain model`() {
        val subject = dto.toDomainModel()

        expectThat(subject).isA<Links>()
        expectThat(subject.articleURL).isEqualTo(dto.articleURL)
        expectThat(subject.wikiURL).isEqualTo(dto.wikiURL)
        expectThat(subject.webcastURL).isEqualTo(dto.youtubeURL)
        with(expectThat(subject.patchImage).isA<PatchImage>()) {
            get(PatchImage::smallURL).isEqualTo(dto.patchDto!!.small)
            get(PatchImage::largeURL).isEqualTo(dto.patchDto!!.large)
        }
    }

    @Test
    fun `dto should correctly map to entity model`() {
        val subject = dto.toEntity()

        expectThat(subject).isA<LaunchLinksEntity>()
        expectThat(subject.articleURL).isEqualTo(dto.articleURL)
        expectThat(subject.wikiURL).isEqualTo(dto.wikiURL)
        expectThat(subject.youtubeURL).isEqualTo(dto.youtubeURL)
        with(expectThat(subject.patchEntity).isA<LaunchPatchEntity>()) {
            get(LaunchPatchEntity::small).isEqualTo(dto.patchDto!!.small)
            get(LaunchPatchEntity::large).isEqualTo(dto.patchDto!!.large)
        }
    }

}