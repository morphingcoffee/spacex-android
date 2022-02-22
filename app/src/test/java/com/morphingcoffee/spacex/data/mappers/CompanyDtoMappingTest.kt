package com.morphingcoffee.spacex.data.mappers

import com.morphingcoffee.spacex.data.local.model.CompanyEntity
import com.morphingcoffee.spacex.data.remote.model.CompanyDto
import com.morphingcoffee.spacex.data.remote.model.toDomainModel
import com.morphingcoffee.spacex.data.remote.model.toEntity
import com.morphingcoffee.spacex.domain.model.Company
import com.morphingcoffee.spacex.shared.fakes.FakeCompanyDto
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class CompanyDtoMappingTest {

    private lateinit var dto: CompanyDto

    @Before
    fun setUp() {
        dto = FakeCompanyDto
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `dto should correctly map to domain model`() {
        val subject = dto.toDomainModel()

        expectThat(subject).isA<Company>()
        expectThat(subject.companyName).isEqualTo(dto.name)
        expectThat(subject.foundedYear).isEqualTo(dto.founded)
        expectThat(subject.founderName).isEqualTo(dto.founder)
        expectThat(subject.numOfEmployees).isEqualTo(dto.employees)
        expectThat(subject.launchSites).isEqualTo(dto.launchSites)
        expectThat(subject.valuationInUsd).isEqualTo(dto.valuation)
    }

    @Test
    fun `dto should correctly map to entity model`() {
        val subject = dto.toEntity()

        expectThat(subject).isA<CompanyEntity>()
        expectThat(subject.name).isEqualTo(dto.name)
        expectThat(subject.founded).isEqualTo(dto.founded)
        expectThat(subject.founder).isEqualTo(dto.founder)
        expectThat(subject.employees).isEqualTo(dto.employees)
        expectThat(subject.launchSites).isEqualTo(dto.launchSites)
        expectThat(subject.valuation).isEqualTo(dto.valuation)
    }

}