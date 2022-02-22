package com.morphingcoffee.spacex.data.mappers

import com.morphingcoffee.spacex.data.local.model.CompanyEntity
import com.morphingcoffee.spacex.data.local.model.toDomainModel
import com.morphingcoffee.spacex.domain.model.Company
import com.morphingcoffee.spacex.shared.fakes.FakeCompanyEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class CompanyEntityMappingTest {

    private lateinit var entity: CompanyEntity

    @Before
    fun setUp() {
        entity = FakeCompanyEntity
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `entity should correctly map to domain model`() {
        val subject = entity.toDomainModel()

        expectThat(subject).isA<Company>()
        expectThat(subject.companyName).isEqualTo(entity.name)
        expectThat(subject.foundedYear).isEqualTo(entity.founded)
        expectThat(subject.founderName).isEqualTo(entity.founder)
        expectThat(subject.numOfEmployees).isEqualTo(entity.employees)
        expectThat(subject.launchSites).isEqualTo(entity.launchSites)
        expectThat(subject.valuationInUsd).isEqualTo(entity.valuation)
    }

}